/*
 * Copyright (C) 2003-2004 The ExTeX Group
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.scanner.stream.impl32;

import java.io.IOException;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.main.exception.MainIOException;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeVisitor;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;

/**
 * This class contains an implementation of a token stream which is fed from a
 * buffer (<code>String</code>, <code>CharBuffer</code>, ...).
 * The basic functionality of stacking prefabricated tokens and
 * delivering them is inherited from the base class
 * {@link de.dante.extex.scanner.stream.impl32.TokenStreamBaseImpl TokenStreamBaseImpl}.
 * <p>
 * In addition this class provides an engine to tokenize the input stream and
 * refill the buffer at its end.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public abstract class AbstractTokenStreamImpl extends TokenStreamBaseImpl
        implements
            TokenStream,
            CatcodeVisitor {

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
    protected int pointer = -1;

    /**
     * Return the singel char in the buffer (the pointer is not changed)
     * @return the single char in the buffer
     */
    protected abstract UnicodeChar getSingleChar();

    /**
     * Return the length of the buffer
     * @return the length of the buffer
     */
    protected abstract int bufferLength();

    /**
     * Creates a new object. The input buffer is initially empty.
     */
    protected AbstractTokenStreamImpl() {

        super();
    }

    /**
     * Return the internal buffer as String.
     * @return the buffer of this TokenStream as String
     */
    public abstract String toString();

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
     * @see de.dante.extex.scanner.stream.impl32.TokenStreamBaseImpl#getNext(de.dante.extex.scanner.type.token.TokenFactory,
     *      de.dante.extex.interpreter.Tokenizer)
     */
    public Token getNext(final TokenFactory factory, final Tokenizer tokenizer)
            throws ScannerException {

        Token t = null;
        int c;

        if (bufferLength() == 0) {
            return null;
        }

        do {

            if ((c = getChar(tokenizer)) < 0) {
                return null;
            }

            try {
                UnicodeChar uc = new UnicodeChar(c);
                t = (Token) tokenizer.getCatcode(uc).visit(this, factory,
                        tokenizer, uc);
            } catch (Exception e) {
                throw new ScannerException(e);
            }
        } while (t == null);

        return t;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitActive(java.lang.Object,java.lang.Object)
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
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitComment(java.lang.Object,java.lang.Object)
     */
    public Object visitComment(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        endLine();
        return null;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitCr(java.lang.Object,java.lang.Object)
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
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitEscape(java.lang.Object,java.lang.Object)
     */
    public Object visitEscape(final Object oFactory, final Object oTokenizer,
            final Object uchar) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        int c = getChar(tokenizer);
        UnicodeChar uc;

        if (c < 0
                || tokenizer.getCatcode((uc = new UnicodeChar(c))) == Catcode.CR) {
            //empty control sequence; see "The TeXbook, Chapter 8, p. 47"
            return factory.createToken(Catcode.ESCAPE, "");
        } else if (tokenizer.getCatcode(uc) == Catcode.LETTER) {
            StringBuffer sb = new StringBuffer();
            sb.append((char) c);
            int position = pointer;

            while ((c = getChar(tokenizer)) >= 0
                    && tokenizer.getCatcode(new UnicodeChar(c)) == Catcode.LETTER) {
                sb.append((char) c);
                position = pointer;
            }

            if (c >= 0) {
                pointer = position;
            }
            state = SKIP_BLANKS;
            return factory.createToken(Catcode.ESCAPE, sb.toString());
        } else {
            state = MID_LINE;
            return factory.createToken(Catcode.ESCAPE, Character
                    .toString((char) c));
        }
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitIgnore(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitInvalid(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        throw new HelpingException("TTP.InvalidChar");
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitLeftBrace(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitLeftBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.LEFTBRACE,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitLetter(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;
        return ((TokenFactory) oFactory).createToken(Catcode.LETTER,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitMacroParam(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitMacroParam(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.MACROPARAM,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
     */
    public Object visitMathShift(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.MATHSHIFT,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
     */
    public Object visitOther(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.OTHER,
                getSingleChar());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitRigthBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitRightBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.RIGHTBRACE,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
     * @see "The TeXbook [Chapter 8, page 47]"
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
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSubMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.SUBMARK,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSupMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.SUPMARK,
                (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
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
     * 
     * @throws IOException in case of an IO error; this can be used in a
     *             derived class
     */
    protected boolean refill() throws IOException {

        pointer = -1;
        state = NEW_LINE;
        return false;
    }

    /**
     * Return the next character to process.
     * The pointer is advanced and points to the character returned.
     * <p>
     * This operation might involve that an additional bunch of characters is
     * read in (with {@link #refill() refill()}).
     * </p>
     *
     * @return the character code or <code>-1</code> if no more character is
     * available
     *
     * @throws ScannerException in the rare case that an IOException has
     * occurred.
     */
    private int getChar(final Tokenizer tokenizer)
            throws ScannerException {

        while (++pointer >= bufferLength()) {
            try {
                if (!refill()) {
                    return -1;
                }
            } catch (IOException e) {
                throw new ScannerException(e);
            }

            pointer = -1;
            state = NEW_LINE;
        }

        int c = getSingleChar().getCodePoint();
        // TODO change to UnicodeChar 
        Catcode catcode = tokenizer.getCatcode(new UnicodeChar(c));

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
                    case 1:
                        break;
                    case 2:
                        // '^^'
                        pointer += 2;

                        // noch Zeichen vorhanden
                        if (pointer < bufferLength()) {

                            //UnicodeChar c1 = new UnicodeChar(buffer.get(pointer));
                            char c1 = (char) getSingleChar().getCodePoint();
                            int hex = fromHex(c1);

                            if (hex >= 0) {
                                c = (char) scanHex(maxhexdigits, tokenizer);
                            } else {
                                // 0100 = 64
                                c = (char) ((hex < 0100)
                                        ? hex + 0100
                                        : hex - 0100);
                                pointer++;
                            }
                        } else {
                            throw new ScannerException("TTP.NoDigitFoundAfter");
                        }
                        break;
                    case 3:
                        // '^^^'
                        pointer += 3;

                        if (pointer < bufferLength()) {

                            String unicodename = scanUnicodeName();
                            UnicodeChar uc = new UnicodeChar(unicodename);

                            if (uc.getCodePoint() < 0) {
                                throw new ScannerException("TTP.NoUnicodeName" +
                                        unicodename);
                            }
                            c = uc.getCodePoint(); // TODO change to 32 bit

                        } else {
                            throw new ScannerException(
                                    "TTP.NoUnicodeNameFoundAfter");
                        }

                        break;
                    case 4:
                        // '^^^^'
                        pointer += 4;

                        if (pointer < bufferLength()) {
                            c = scanHex(maxhexdigits, tokenizer);
                        } else {
                            throw new ScannerException("TTP.NoHexDigitFound");
                        }

                        break;
                    default:
                        throw new ScannerException("TTP.TooManySupMarks");
                }
            }
        }
        return c;
    }

    /**
     * scan the Line for '^', '^^', '^^^^' (one '^' alreday read)
     * after scan, the pos is set to the original value!!!!
     *
     * @param tokenizer ...
     *
     * @return count of '^'
     */
    private int scanSupMark(final Tokenizer tokenizer) {

        int supcount = 0;
        int savepos = pointer;

        for (int i = 0; i < 9; i++) {

            if (pointer >= 0) {
                char c = (char) getSingleChar().getCodePoint();
                pointer++;
                Catcode catcode = tokenizer.getCatcode(new UnicodeChar(c));

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
     * scan a hex number (max. n digits)
     * @return int-value of hexnumber
     */
    private int scanHex(final int n, final Tokenizer tokenizer)
            throws ScannerException {

        int hexvalue = 0;
        int i = 0;
        for (i = 0; i < n; i++) {
            if (pointer < bufferLength()) {

                // UnicodeChar c = new UnicodeChar(buffer.get(pointer));
                char c = (char) getSingleChar().getCodePoint();
                int hex = fromHex(c);

                // hexdigit ?
                if (hex >= 0) {
                    pointer++;
                    hexvalue = (hexvalue << 4) + hex;
                } else if (i == 0) {
                    // error no hexdigit found after '^^'
                    throw new ScannerException("TTP.NoHexDigitFound");
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        return hexvalue;
    }

    /**
     * ...
     *
     * @param c ...
     *
     * @return the hex value or -1 if no hex value was found
     */
    private int fromHex(char c) {

        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
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
     */
    private String scanUnicodeName() throws ScannerException {

        StringBuffer buf = new StringBuffer(30);
        while (true) {
            if (pointer < bufferLength()) {

                // Zeichen holen
                char c = (char) getSingleChar().getCodePoint();

                // bis ';' parsen
                if (c != ';') {
                    pointer++;
                    buf.append(c);
                } else {
                    // one char found?
                    if (buf.length() == 0) {
                        throw new ScannerException("TTP.NoLetterFoundAfter");
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

    //    /**
    //     * Analyze a character and return its hex value, i.e. '0' to '9' are mapped
    //     * to 0 to 9 and 'a' to 'f' (case sensitive) are mapped to 10 to 15.
    //     * 
    //     * @param c the character to analyze
    //     * @return the integer value of a hex digit or -1 if no hex digit is given
    //     */
    //    private int hex2int(final char c) {
    //        if ('0' <= c && c <= '9') {
    //            return c - '0';
    //        } else if ('a' <= c && c <= 'f') {
    //            return c - 'a' + 10;
    //            //        } else if ('A' <= c && c <= 'F') {
    //            //            return c - 'A' + 10;
    //        } else {
    //            return -1;
    //        }
    //    }

    /**
     * ...
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.13 $
     */
    private static class State {

        /**
         * Creates a new object.
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