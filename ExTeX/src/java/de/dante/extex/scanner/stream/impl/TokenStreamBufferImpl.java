/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.scanner.stream.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.main.MainIOException;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;

/**
 * This class contains an implementation of a token stream which is fed from a
 * buffer. The basic functionality of stacking prefabricated tokens and
 * delivering them is inherited from the base class
 * {@link de.dante.extex.scanner.stream.impl.TokenStreamBaseImpl TokenStreamBaseImpl}.
 * <p>
 * In addition this class provides an engine to tokenize the input stream and
 * refill the buffer at its end.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.17 $
 */
public class TokenStreamBufferImpl extends TokenStreamBaseImpl
        implements TokenStream, CatcodeVisitor {

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
     * The buffer to read from. The line is empty initially.
     */
    private CharBuffer buffer;

    /**
     * Creates a new object. The input buffer is initially empty.
     */
    protected TokenStreamBufferImpl() {
        super();
    }

    /**
     * Creates a new object.
     *
     * @param line a string to read from
     * @param encoding the encoding to use
     *
     * @throws CharacterCodingException in case of an error
     */
    public TokenStreamBufferImpl(final String line, final String encoding)
            throws CharacterCodingException {

        super();
        buffer = Charset.forName(encoding).newDecoder()
                .decode(ByteBuffer.wrap(line.getBytes()));
    }

    /**
     * Getter for the internal buffer. This method is meant to be used for the
     * look-ahead to determine the format to be used. For this purpose this
     * method has to be used immediately after the constructor.
     * <p>
     * Since the buffer is returned directly the contents may change when the
     * the tokens are acquired.
     * </p>
     *
     * @return the buffer of this TokenStream
     */
    public CharBuffer getBuffer() {
        return buffer;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public Locator getLocator() {
        return new Locator(getSource(), getLineno(), buffer, pointer);
    }

    /**
     * ...
     *
     * @return ...
     */
    protected String getSource() {
        return "<buffer>";
    }

    /**
     * ...
     *
     * @return ...
     */
    protected int getLineno() {
        return 1;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBaseImpl#getNext(de.dante.extex.scanner.TokenFactory,
     *      de.dante.extex.interpreter.Tokenizer)
     */
    public Token getNext(final TokenFactory factory, final Tokenizer tokenizer)
            throws GeneralException {

        Token t = null;
        int c;

        if (buffer == null) {
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

        Token t = ((TokenFactory) oFactory).newInstance(Catcode.ACTIVE,
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
    public Object visitCr(final Object oFactory, final Object oTokenizer, final Object uc) throws GeneralException {
        TokenFactory factory = (TokenFactory) oFactory;
        Token t = null;

        if (state == MID_LINE) {
            t = factory.newInstance(Catcode.SPACE, " ");
        } else if (state == NEW_LINE) {
            t = factory.newInstance(Catcode.ESCAPE, "par");
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
        int c = getChar(tokenizer);
        UnicodeChar uc;

        if (c < 0
            || tokenizer.getCatcode((uc = new UnicodeChar(c))) == Catcode.CR) {
            //empty control sequence; see "The TeXbook, Chapter 8, p. 47"
            return factory.newInstance(Catcode.ESCAPE, "");
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
            return factory.newInstance(Catcode.ESCAPE, sb.toString());
        } else {
            state = MID_LINE;
            return factory.newInstance(Catcode.ESCAPE, Character
                    .toString((char) c));
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

        throw new GeneralHelpingException("TTP.InvalidChar");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitLeftBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.LEFTBRACE,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitLetter(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;
        return ((TokenFactory) oFactory).newInstance(Catcode.LETTER,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,
     *      java.lang.Object)
     */
    public Object visitMacroParam(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.MACROPARAM,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
     */
    public Object visitMathShift(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.MATHSHIFT,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
     */
    public Object visitOther(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.OTHER, buffer
                .get(pointer));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRigthBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitRightBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.RIGHTBRACE,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
     * @see "The TeXbook [Chapter 8, page 47]"
     */
    public Object visitSpace(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;

        if (state == MID_LINE) {
            state = SKIP_BLANKS;
            return factory.newInstance(Catcode.SPACE, " ");
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSubMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.SUBMARK,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSupMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.SUPMARK,
                                                     (UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
     */
    public Object visitTabMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.TABMARK,
                                                     (UnicodeChar) uc);
    }

    /**
     * Set the buffer to a new character array.
     * 
     * @param cs the new buffer
     */
    protected void setBuffer(final CharBuffer cs) {
        buffer = cs;
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
     * @return the character code or <code>-1</code> if no more character is available
     * @throws MainIOException in the rare case that an IOException has occurred.
     */
    private int getChar(final Tokenizer tokenizer) throws MainIOException,
            GeneralException {

        while (++pointer >= buffer.length()) {
            try {
                if (!refill()) {
                    return -1;
                }
            } catch (IOException e) {
                throw new MainIOException(e);
            }

            pointer = -1;
            state = NEW_LINE;
        }

        int c = buffer.get(pointer);
        // TODO change to UnicodeChar 
        Catcode catcode = tokenizer.getCatcode(new UnicodeChar(c));

        // CAT_SUP_MARK -> auf ('^^', '^^^', '^^^^') scannen
        if (catcode == Catcode.SUPMARK) {

            // noch Zeichen vorhanden
            if (pointer < buffer.length()) {

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
                    case 1 :
                        break;
                    case 2 :
                        // '^^'
                        pointer += 2;

                        // noch Zeichen vorhanden
                        if (pointer < buffer.length()) {

                            //UnicodeChar c1 = new UnicodeChar(buffer.get(pointer));
                            char c1 = buffer.get(pointer);
                            int hex = fromHex(c1);

                            if (hex >= 0) {
                                c = (char) scanHex(maxhexdigits, tokenizer);
                            } else {
                                // 0100 = 64
                                c = (char) ((hex < 0100) ? hex + 0100
                                        : hex - 0100);
                                pointer++;
                            }
                        } else {
                            throw new GeneralHelpingException(
                                    "TTP.NoDigitFoundAfter");
                        }
                        break;
                    case 3 :
                        // '^^^'
                        pointer += 3;

                        if (pointer < buffer.length()) {

                            String unicodename = scanUnicodeName();
                            UnicodeChar uc = new UnicodeChar(unicodename);

                            if (uc.getCodePoint() < 0) {
                                throw new GeneralHelpingException(
                                        "TTP.NoUnicodeName", unicodename);
                            }
                            c = uc.getCodePoint(); // TODO change to 32 bit

                        } else {
                            throw new GeneralHelpingException(
                                    "TTP.NoUnicodeNameFoundAfter");
                        }

                        break;
                    case 4 :
                        // '^^^^'
                        pointer += 4;

                        if (pointer < buffer.length()) {
                            c = (scanHex(maxhexdigits, tokenizer));
                        } else {
                            throw new GeneralHelpingException(
                                    "TTP.NoHexDigitFound");
                        }

                        break;
                    default :
                        throw new GeneralHelpingException("TTP.TooManySupMarks");
                }
            }
        }
        return c;
    }

    /**
     * scan the Line for '^', '^^', '^^^^' (one '^' alreday read)
     * after scan, the pos is set to the original value!!!!
     * 
     * @return count of '^'
     */
    private int scanSupMark(final Tokenizer tokenizer) {

        int supcount = 0;
        int savepos = pointer;

        for (int i = 0; i < 9; i++) {

            if (pointer >= 0) {
                char c = buffer.get(pointer++);
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
            throws MainIOException, GeneralException {

        int hexvalue = 0;
        int i = 0;
        for (i = 0; i < n; i++) {
            if (pointer < buffer.length()) {

                // UnicodeChar c = new UnicodeChar(buffer.get(pointer));
                char c = buffer.get(pointer);
                int hex = fromHex(c);

                // hexdigit ?
                if (hex >= 0) {
                    pointer++;
                    hexvalue = (hexvalue << 4) + hex;
                } else if (i == 0) {
                    // error no hexdigit found after '^^'
                    throw new GeneralHelpingException("TTP.NoHexDigitFound");
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        /*
        // more hexdigits ?
        //gene: bullshit; ^^aaax is ok and no error
        if (i == n) {
            if (pointer < buffer.length()) {
                UnicodeChar c = new UnicodeChar(getChar(tokenizer));

                // hexdigit ?
                if (c.isHexDigit()) {
                    throw new GeneralHelpingException(
                            "TTP.TooMuchHexDigitFound");
                }
            }
        }
        */

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
    private String scanUnicodeName() throws GeneralException {
        StringBuffer buf = new StringBuffer(30);
        while (true) {
            if (pointer < buffer.length()) {

                // Zeichen holen
                char c = buffer.get(pointer);

                // bis ';' parsen
                if (c != ';') {
                    pointer++;
                    buf.append(c);
                } else {
                    // one char found?
                    if (buf.length() == 0) {
                        throw new GeneralHelpingException("TTP.NoLetterFoundAfter");
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
     * ...
     * 
     * @param c ...
     */
    private void ungetChar(final char c) {
        buffer.put(pointer--, c);
    }

    /**
     * End the current line
     */
    private void endLine() {
        pointer = buffer.length();
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

    private static class State {
        public State() {
            super();
        }
    }
}
