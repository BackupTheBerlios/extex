/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.main.exception.MainIOException;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationSyntaxException;

/**
 * This class contains an implementation of a token stream which is fed from a
 * Reader.
 * <p>
 * The class ignores the encoding in <tt>\inputencoding</tt>!
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.19 $
 */
public class TokenStreamImpl extends TokenStreamBaseImpl implements
        TokenStream, CatcodeVisitor {

    /**
     * This is a type-safe class to represent state information.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.19 $
     */
    private static final class State {

        /**
         * Creates a new object.
         */
        public State() {

            super();
        }
    }

    /**
     * The constant <tt>BUFFERSIZE_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the buffer size.
     */
    private static final String BUFFERSIZE_ATTRIBUTE = "buffersize";

    /**
     * The constant <tt>CARET_LIMIT</tt> contains the ...
     */
    private static final int CARET_LIMIT = 0100;    // 0100 = 64

    /**
     * The constant <tt>CR</tt> contains the ...
     */
    private static final UnicodeChar CR = new UnicodeChar('\r');

    /**
     * The constant <tt>MID_LINE</tt> contains the state for the processing in
     * the middle of a line.
     */
    private static final State MID_LINE = new State();

    /**
     * The constant <tt>NEW_LINE</tt> contains the state for the processing at
     * the beginning of a new line.
     */
    private static final State NEW_LINE = new State();

    /**
     * The constant <tt>SKIP_BLANKS</tt> contains the state for the processing
     * when spaces are ignored.
     */
    private static final State SKIP_BLANKS = new State();

    /**
     * The field <tt>in</tt> contains the buffered reader for lines.
     */
    private LineNumberReader in;

    /**
     * The field <tt>line</tt> contains the current line of input.
     */
    private String line = "";

    /**
     * The field <tt>namespace</tt> contains the currently used namespace.
     */
    private String namespace = "";

    /**
     * The index in the buffer for the next character to consider. This
     * is an invariant: after a character is read this pointer has to be
     * advanced.
     */
    private int pointer = 1;

    /**
     * The field <tt>source</tt> contains the description of the source for
     * tokens.
     */
    private String source;

    /**
     * The field <tt>state</tt> contains the current state of operation.
     */
    private State state = NEW_LINE;

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     * configuration is ignored in this implementation.
     * @param file the file to read
     * @param encoding the encoding to use
     *
     * @throws ConfigurationException in case of an error in the configuration
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Configuration config, final File file,
            final String encoding) throws IOException, ConfigurationException {

        super(false);

        int bufferSize = -1;
        String size = config.getAttribute(BUFFERSIZE_ATTRIBUTE).trim();
        if (size != null && !size.equals("")) {
            try {
                bufferSize = Integer.parseInt(size);
            } catch (NumberFormatException e) {
                throw new ConfigurationSyntaxException(e.getMessage(),
                        config.toString() + "#" + BUFFERSIZE_ATTRIBUTE);
            }
        }

        InputStream inputStream = new FileInputStream(file);
        if (bufferSize > 0) {
            inputStream = new BufferedInputStream(inputStream, bufferSize);
        } else if (bufferSize < 0) {
            inputStream = new BufferedInputStream(inputStream);
        }

        this.source = file.getName();
        this.in = new LineNumberReader(new InputStreamReader(inputStream,
                encoding));

    }

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     * configuration is ignored in this implementation.
     * @param reader the reader
     * @param isFile indicator for file streams
     * @param theSource the description of the input source
     *
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Configuration config, final Reader reader,
            final Boolean isFile, final String theSource) throws IOException {

        super(isFile.booleanValue());
        this.in = new LineNumberReader(reader);
        this.source = theSource;
    }

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     * configuration is ignored in this implementation.
     * @param theLine the string to use as source for characters
     * @param theSource the description of the input source
     *
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Configuration config, final String theLine,
            final String theSource) throws IOException {

        super(false);
        this.in = new LineNumberReader(new StringReader(theLine));
        this.source = theSource;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public Locator getLocator() {

        return new Locator(source, (in == null ? 0 : in.getLineNumber()), line,
                pointer - 1);
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBaseImpl#getNext(
     *      de.dante.extex.scanner.TokenFactory,
     *      de.dante.extex.interpreter.Tokenizer)
     */
    protected Token getNext(final TokenFactory factory,
            final Tokenizer tokenizer) throws GeneralException {

        Token t = null;

        do {
            UnicodeChar uc = getChar(tokenizer);
            if (uc == null) {
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
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitActive(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        Token t = ((TokenFactory) oFactory).newInstance(Catcode.ACTIVE,
                                                        (UnicodeChar) uc,
                                                        namespace);
        return t;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitComment(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        endLine();
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitCr(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Token t = null;

        if (state == MID_LINE) {
            t = factory.newInstance(Catcode.SPACE, ' ');
        } else if (state == NEW_LINE) {
            t = factory.newInstance(Catcode.ESCAPE, "par", namespace);
        }

        endLine();
        return t;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitEscape(final Object oFactory, final Object oTokenizer,
            final Object uchar) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;

        if (atEndOfLine()) {
            //empty control sequence; see "The TeXbook, Chapter 8, p. 47"
            return factory.newInstance(Catcode.ESCAPE, "", namespace);
        }

        UnicodeChar uc = getChar(tokenizer);

        if (uc == null) {
            return factory.newInstance(Catcode.ESCAPE, "", namespace);

        } else if (tokenizer.getCatcode(uc) == Catcode.LETTER) {
            StringBuffer sb = new StringBuffer();
            sb.append((char) (uc.getCodePoint()));
            int savedPointer = pointer;
            state = SKIP_BLANKS;

            while (!atEndOfLine() && (uc = getChar(tokenizer)) != null) {
                if (tokenizer.getCatcode(uc) != Catcode.LETTER) {
                    pointer = savedPointer;
                    return factory.newInstance(Catcode.ESCAPE, sb.toString(),
                                               namespace);
                }
                sb.append((char) (uc.getCodePoint()));
                savedPointer = pointer;
            }

            return factory
                    .newInstance(Catcode.ESCAPE, sb.toString(), namespace);

        } else {
            state = MID_LINE;
            return factory.newInstance(Catcode.ESCAPE, uc, namespace);

        }
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitIgnore(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitInvalid(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        throw new GeneralHelpingException("TTP.InvalidChar");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitLeftBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.LEFTBRACE,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitLetter(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.LETTER,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitMacroParam(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.MACROPARAM,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitMathShift(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.MATHSHIFT,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitOther(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.OTHER,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitRightBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.RIGHTBRACE,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     * @see "The TeXbook [Chapter 8, page 47]"
     */
    public Object visitSpace(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;

        if (state == MID_LINE) {
            state = SKIP_BLANKS;
            return factory.newInstance(Catcode.SPACE, ' ');
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitSubMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.SUBMARK,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitSupMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.SUPMARK,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitTabMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        return ((TokenFactory) oFactory).newInstance(Catcode.TABMARK,
                                                     (UnicodeChar) uc,
                                                     namespace);
    }

    /**
     * Checks whether the pointer is at the end of line.
     *
     * @return <code>true</code> iff the next reading operation would try to
     * refill the line buffer
     */
    private boolean atEndOfLine() {

        return (pointer > line.length());
    }

    /**
     * End the current line.
     */
    private void endLine() {

        pointer = line.length() + 1;
    }

    /**
     * Return the next character to process.
     * The pointer is advanced and points to the character returned.
     * <p>
     * This operation might involve that an additional bunch of characters is
     * read in (with {@link #refill() refill()}).
     * </p>
     *
     * @param tokenizer the classifier for characters
     *
     * @return the character or <code>null</code> if no more character is
     * available
     *
     * @throws MainIOException in the rare case that an IOException has
     * occurred.
     */
    private UnicodeChar getChar(final Tokenizer tokenizer)
            throws MainIOException {

        UnicodeChar uc = getRawChar();

        if (uc == null) {
            do {
                try {
                    if (!refill()) {
                        return null;
                    }
                } catch (IOException e) {
                    throw new MainIOException(e);
                }

                pointer = 0;
                uc = getRawChar();
            } while (uc == null);

            state = NEW_LINE;
        }

        if (tokenizer.getCatcode(uc) == Catcode.SUPMARK) {

            int savePointer = pointer;
            UnicodeChar c = getRawChar();

            if (uc.equals(c)) {
                c = getRawChar();
                if (c == null) {
                    return new UnicodeChar(0);
                }
                int hexHigh = hex2int(c.getCodePoint());
                if (hexHigh >= 0) {
                    savePointer = pointer;
                    uc = getRawChar();
                    if (uc == null) {
                        uc = new UnicodeChar(hexHigh);
                    } else {
                        int hexLow = hex2int(uc.getCodePoint());
                        if (hexLow < 0) {
                            pointer = savePointer;
                            uc = new UnicodeChar(hexHigh);
                        } else {
                            uc = new UnicodeChar((hexHigh << 4) + hexLow);
                        }
                    }
                } else if (c != null) {
                    hexHigh = c.getCodePoint();
                    uc = new UnicodeChar(
                            ((hexHigh < CARET_LIMIT) ? hexHigh + CARET_LIMIT
                                    : hexHigh - CARET_LIMIT));
                }
            } else {
                pointer = savePointer;
            }
        }

        return uc;
    }

    /**
     * Get the next character from the input line.
     *
     * @return the next raw character or <code>null</code> if none is available.
     */
    private UnicodeChar getRawChar() {

        if (pointer < line.length()) {
            return new UnicodeChar(line, pointer++);
        }

        return (pointer++ > line.length() ? null : CR);
    }

    /**
     * Analyze a character and return its hex value, i.e. '0' to '9' are mapped
     * to 0 to 9 and 'a' to 'f' (case sensitive) are mapped to 10 to 15.
     *
     * @param c the character code to analyze
     *
     * @return the integer value of a hex digit or -1 if no hex digit is given
     */
    private int hex2int(final int c) {

        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 10;
            //} else if ('A' <= c && c <= 'F') {
            //    return c - 'A' + 10;
        } else {
            return -1;
        }
    }

    /**
     * Get the next line from the input reader to be processed.
     *
     * @return <code>true</code> iff the next line could be acquired.
     *
     * @throws IOException in case of some kind of IO error
     */
    protected boolean refill() throws IOException {

        if (in == null) {
            return false;
        }
        if ((line = in.readLine()) == null) {
            in.close();
            in = null;
            return false;
        }
        return true;
    }

}