/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.scanner.stream.impl32;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Stack;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.main.exception.MainIOException;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;

/**
 * This is the base implementation of a token stream. It has an internal stack
 * of tokens which can be enlarged with push() or reduced with pop().
 * <p>
 * It use 32 bit characters!
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public abstract class TokenStreamBaseImpl32
        implements
            TokenStream,
            CatcodeVisitor {

    /**
     * The Token stack for the pushback operation
     */
    private Stack stack = new Stack();

    /**
     * Creates a new object.
     * @param   buf     the buffer with the line
     */
    public TokenStreamBaseImpl32(final UnicodeCharList buf) {

        super();
        buffer = buf;
    }

    /**
     * Creates a new object.
     */
    public TokenStreamBaseImpl32() {

        super();
        buffer = new UnicodeCharList();
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
     */
    public boolean closeFileStream() {

        stack.setSize(0);
        return false;
    }

    /**
     * @see de.dante.extex.token.InputFilter#get()
     */
    public Token get(final TokenFactory factory, final Tokenizer tokenizer)
            throws GeneralException {

        return (stack.size() == 0 ? getNext(factory, tokenizer) : (Token) stack
                .pop());
    }

    /**
     * @see de.dante.extex.token.InputFilter#put(Token)
     */
    public void put(final Token token) {

        if (token != null) {
            stack.push(token);
        }
    }

    /**
     * @see de.dante.extex.token.InputFilter#put(Tokens)
     */
    public void put(final Tokens toks) {

        if (toks != null) {
            stack.push(toks);
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
     */
    public boolean isFileStream() {

        return false;
    }

    // ------------------------------------

    /**
     * new line
     */
    private static final State NEW_LINE = new State();

    /**
     * mid line
     */
    private static final State MID_LINE = new State();

    /**
     * skip blanks
     */
    private static final State SKIP_BLANKS = new State();

    /**
     * state of a line
     */
    private State state = NEW_LINE;

    /**
     * The index in the buffer one before the next character to consider. This
     * is an invariant: to read a character this pointer has to be advanced
     * first.
     */
    private int pointer = -1;

    /**
     * the 32-bit buffer for a line
     */
    private UnicodeCharList buffer;

    /**
     * Set the new buffer
     * @param buf   the new buffer
     */
    protected void setBuffer(final UnicodeCharList buf) {

        buffer = buf;
        pointer = -1;
        state = NEW_LINE;
    }

    /**
     * Set the new buffer
     * @param line  the line for the new buffer
     */
    protected void setBuffer(final String line) {

        UnicodeCharList buf = new UnicodeCharList(line.length());
        UnicodeChar uc = UnicodeChar.NULL;
        for (int i = 0; i < line.length(); i += uc.getChar16Count()) {
            uc = new UnicodeChar(line, i);
            buf.add(uc);
        }
        setBuffer(buf);
    }

    /**
     * Set the new buffer
     * @param line  the line for the new buffer
     */
    protected void setBuffer(final CharBuffer line) {

        UnicodeCharList buf = new UnicodeCharList(line.length());
        UnicodeChar uc = UnicodeChar.NULL;
        for (int i = 0; i < line.length(); i += uc.getChar16Count()) {
            uc = new UnicodeChar(line, i);
            buf.add(uc);
        }
        setBuffer(buf);
    }

    /**
     * Return the singel char in the buffer (the pointer is not changed)
     * @return the single char in the buffer
     */
    private UnicodeChar getSingleChar() {

        return buffer.get(pointer);
    }

    /**
     * Return the length of the buffer
     * @return the length of the buffer
     */
    private int bufferLength() {

        return buffer.size();
    }

    /**
     * Return the internal buffer as String.
     * @return the buffer of this TokenStream as String
     */
    public String toString() {

        return buffer.toString();
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public Locator getLocator() {

        return new Locator(getSource(), getLineno(), toString(), pointer);
    }

    /**
     * Return the name of the source.
     * @return the name of the source
     */
    protected String getSource() {

        return "<buffer>";
    }

    /**
     * Return the linenumber
     * @return the linenumber
     */
    protected int getLineno() {

        return 1;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl32.TokenStreamBaseImpl#getNext(
     *      de.dante.extex.scanner.TokenFactory,de.dante.extex.interpreter.Tokenizer)
     */
    public Token getNext(final TokenFactory factory, final Tokenizer tokenizer)
            throws GeneralException {

        Token t = null;
        UnicodeChar uc;

        if (bufferLength() == 0) {
            return null;
        }

        do {

            if ((uc = getChar(tokenizer)) == null) {
                return null;
            }

            try {
                t = (Token) tokenizer.getCatcode(uc).visit(this, factory,
                        tokenizer, uc);
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        } while (t == null);

        return t;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,java.lang.Object)
     */
    public Object visitActive(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        Token t = ((TokenFactory) oFactory).createToken(Catcode.ACTIVE,
                (UnicodeChar) uc);
        return t;
    }

    /**
     * Comment
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,java.lang.Object)
     */
    public Object visitComment(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        endLine();
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,java.lang.Object)
     */
    public Object visitCr(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Token t = null;

        if (state == MID_LINE) {
            t = factory.createToken(Catcode.SPACE, " ");
        } else if (state == NEW_LINE) {
            t = factory.createToken(Catcode.ESCAPE, "par");
        }

        endLine();
        return t;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,java.lang.Object)
     */
    public Object visitEscape(final Object oFactory, final Object oTokenizer,
            final Object uchar) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        UnicodeChar uc = getChar(tokenizer);

        if (uc == null || tokenizer.getCatcode(uc) == Catcode.CR) {
            //empty control sequence; see "The TeXbook, Chapter 8, p. 47"
            return factory.createToken(Catcode.ESCAPE, "");
        } else if (tokenizer.getCatcode(uc) == Catcode.LETTER) {
            StringBuffer sb = new StringBuffer();
            sb.append(uc.toString());
            int position = pointer;

            while ((uc = getChar(tokenizer)) != null
                    && tokenizer.getCatcode(uc) == Catcode.LETTER) {
                sb.append(uc.toString());
                position = pointer;
            }

            if (uc != null) {
                pointer = position;
            }
            state = SKIP_BLANKS;
            return factory.createToken(Catcode.ESCAPE, sb.toString());
        } else {
            state = MID_LINE;
            return factory.createToken(Catcode.ESCAPE, uc.toString());
        }
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitIgnore(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitInvalid(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        throw new HelpingException("TTP.InvalidChar");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitLeftBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.LEFTBRACE,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitLetter(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;
        return ((TokenFactory) oFactory).createToken(Catcode.LETTER,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitMacroParam(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.MACROPARAM,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
     */
    public Object visitMathShift(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.MATHSHIFT,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
     */
    public Object visitOther(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.OTHER,
                getSingleChar());
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRigthBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitRightBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.RIGHTBRACE,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
     */
    public Object visitSpace(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;

        if (state == MID_LINE) {
            state = SKIP_BLANKS;
            return factory.createToken(Catcode.SPACE, " ");
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSubMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.SUBMARK,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSupMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.SUPMARK,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
     */
    public Object visitTabMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.TABMARK,
                (UnicodeChar) uc);
    }

    /**
     * Get a new line of characters to consider.
     *
     * @return <code>false</code> if no further input is available.
     * @throws IOException in case of an IO error; this can be used in a
     *             derived class
     */
    protected boolean refill() throws IOException {

        pointer = -1;
        state = NEW_LINE;
        return false;
    }

    /**
     * 64 = 0100
     */
    private static final int CONST64 = 0100;

    /**
     * 1-supmark
     */
    private static final int SUP1 = 1;

    /**
     * 2-supmark
     */
    private static final int SUP2 = 2;

    /**
     * 3-supmark
     */
    private static final int SUP3 = 3;

    /**
     * 4-supmark
     */
    private static final int SUP4 = 4;

    /**
     * Return the next character to process.
     * The pointer is advanced and points to the character returned.
     * <p>
     * This operation might involve that an additional bunch of characters is
     * read in (with {@link #refill() refill()}).
     * </p>
     *
     * @param tokenizer the tokenizer
     * @return the character code or <code>null</code> if no more character is available
     * @throws GeneralException in the rare case that an IOException has occurred.
     */
    private UnicodeChar getChar(final Tokenizer tokenizer)
            throws GeneralException {

        while (++pointer >= bufferLength()) {
            try {
                if (!refill()) {
                    return null;
                }
            } catch (IOException e) {
                throw new MainIOException(e);
            }

            pointer = -1;
            state = NEW_LINE;
        }

        UnicodeChar uc = getSingleChar();
        Catcode catcode = tokenizer.getCatcode(uc);

        // CAT_SUP_MARK -> auf ('^^', '^^^', '^^^^') scannen
        if (catcode == Catcode.SUPMARK) {

            // noch Zeichen vorhanden
            if (pointer < bufferLength()) {

                // count the '^'
                int supcount = scanSupMark(tokenizer);

                // TODO charbits missing
                //                // charbits
                //                Num charbits = (Num) ec.getEqTable().get("param.num.charbits");
                //                if (charbits == null) {
                //                    charbits = new Num(32);
                //                }
                //
                //                // max hexdigits ?
                int maxhexdigits = 2;
                //                switch (charbits.intVal()) {
                //                case 8 :
                //                    maxhexdigits = 2;
                //                    break;
                //                case 16 :
                //                    maxhexdigits = 4;
                //                    break;
                //                default :
                maxhexdigits = 8;

                //                }

                // how many ^
                switch (supcount) {
                    case SUP1 :
                        break;
                    case SUP2 :
                        // '^^'
                        pointer += SUP2;

                        // noch Zeichen vorhanden
                        if (pointer < bufferLength()) {

                            UnicodeChar uc1 = getSingleChar();
                            int hex = fromHex(uc1);

                            if (hex >= 0) {
                                uc = scanHex(maxhexdigits, tokenizer);
                            } else {
                                uc = ((hex < CONST64) ? new UnicodeChar(hex
                                        + CONST64) : new UnicodeChar(hex
                                        - CONST64));
                                pointer++;
                            }
                        } else {
                            throw new HelpingException(
                                    "TTP.NoDigitFoundAfter");
                        }
                        break;
                    case SUP3 :
                        // '^^^'
                        pointer += SUP3;

                        if (pointer < bufferLength()) {

                            String unicodename = scanUnicodeName();
                            UnicodeChar ucn = new UnicodeChar(unicodename);

                            if (ucn.getCodePoint() < 0) {
                                throw new HelpingException(
                                        "TTP.NoUnicodeName", unicodename);
                            }
                            uc = new UnicodeChar(ucn.getCodePoint());

                        } else {
                            throw new HelpingException(
                                    "TTP.NoUnicodeNameFoundAfter");
                        }

                        break;
                    case SUP4 :
                        // '^^^^'
                        pointer += SUP4;

                        if (pointer < bufferLength()) {
                            uc = scanHex(maxhexdigits, tokenizer);
                        } else {
                            throw new HelpingException(
                                    "TTP.NoHexDigitFound");
                        }

                        break;
                    default :
                        throw new HelpingException("TTP.TooManySupMarks");
                }
            }
        }
        return uc;
    }

    /**
     * scan the Line for '^', '^^', '^^^^' (one '^' alreday read)
     * after scan, the pos is set to the original value!!!!
     *
     * @param tokenizer the tokenizer
     * @return count of '^'
     */
    private int scanSupMark(final Tokenizer tokenizer) {

        int supcount = 0;
        int savepos = pointer;

        for (int i = 0; i < SUP4; i++) {

            if (pointer >= 0) {
                UnicodeChar uc = getSingleChar();
                pointer++;
                Catcode catcode = tokenizer.getCatcode(uc);

                // CAT_SUP_MARK -> auf ('^^', '^^^', '^^^^') scannen
                if (catcode == Catcode.SUPMARK) {
                    supcount++;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        pointer = savepos;
        return supcount;
    }

    /**
     * shift 4
     */
    private static final int SHIFT4 = 4;

    /**
     * scan a hex number (max. n digits)
     * @param n         number of digits
     * @param tokenizer the tokenizer
     * @return UnicodeChar of a hexnumber
     * @throws GeneralException ...
     */
    private UnicodeChar scanHex(final int n, final Tokenizer tokenizer)
            throws GeneralException {

        int hexvalue = 0;
        int i = 0;
        for (i = 0; i < n; i++) {
            if (pointer < bufferLength()) {

                int hex = fromHex(getSingleChar());

                // hexdigit ?
                if (hex >= 0) {
                    pointer++;
                    hexvalue = (hexvalue << SHIFT4) + hex;
                } else if (i == 0) {
                    // error no hexdigit found after '^^'
                    throw new HelpingException("TTP.NoHexDigitFound");
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return new UnicodeChar(hexvalue);
    }

    /**
     * unicodechar for '0'
     */
    private static final UnicodeChar C0 = new UnicodeChar('0');

    /**
     * unicodechar for '9'
     */
    private static final UnicodeChar C9 = new UnicodeChar('9');

    /**
     * unicodechar for 'a'
     */
    private static final UnicodeChar CA = new UnicodeChar('a');

    /**
     * unicodechar for 'f'
     */
    private static final UnicodeChar CF = new UnicodeChar('f');

    /**
     * unicodechar for ';'
     */
    private static final UnicodeChar CPOINT = new UnicodeChar(';');

    /**
     * basis 10
     */
    private static final int BASIS10 = 10;

    /**
     * Return the int-value for a hex digit
     * @param uc the unicodechar
     * @return the hex value or -1 if no hex value was found
     */
    private int fromHex(final UnicodeChar uc) {

        if (uc.getCodePoint() >= C0.getCodePoint()
                && uc.getCodePoint() <= C9.getCodePoint()) {
            return uc.getCodePoint() - C0.getCodePoint();
        } else if (uc.getCodePoint() >= CA.getCodePoint()
                && uc.getCodePoint() <= CF.getCodePoint()) {
            return uc.getCodePoint() - CA.getCodePoint() + BASIS10;
        }
        return -1;
    }

    /**
     * scan a unicodename
     * <pre>
     * ^^^^NAME;
     * </pre>
     *
     * @return unicodename as <code>String</code>
     * @throws GeneralException ...
     */
    private String scanUnicodeName() throws GeneralException {

        StringBuffer buf = new StringBuffer();
        while (true) {
            if (pointer < bufferLength()) {

                UnicodeChar uc = getSingleChar();

                // parse unti ';'
                if (uc.getCodePoint() != CPOINT.getCodePoint()) {
                    pointer++;
                    buf.append(uc.toString());
                } else {
                    if (buf.length() == 0) {
                        throw new HelpingException(
                                "TTP.NoLetterFoundAfter");
                    }
                    // ';' not use in the name
                    pointer++;
                    break;
                }
            } else {
                break;
            }

        }
        return buf.toString();
    }

    /**
     * End the current line
     */
    private void endLine() {

        pointer = bufferLength();
    }

    /**
     * The state of a line
     */
    private static class State {

        /**
         * Create a new object.
         */
        public State() {

            super();
        }
    }

    
    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isEof()
     */
    public boolean isEof() {
        // TODO mgn incomplete
        return false;
    }
}