/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.exception.helping.InvalidCharacterException;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.CatcodeVisitor;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
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
 * @version $Revision: 1.38 $
 */
public class TokenStreamImpl extends TokenStreamBaseImpl
        implements
            TokenStream,
            CatcodeVisitor {

    /**
     * This is a type-safe class to represent state information.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.38 $
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
     * The constant <tt>CARET_LIMIT</tt> contains the threshold for the ^
     * notation.
     */
    private static final int CARET_LIMIT = 0100; // 0100 = 64

    /**
     * The constant <tt>CR</tt> contains the one and only CR character.
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
     * @param config the configuration object for this instance; This
     * configuration is ignored in this implementation.
     * @param options ignored here
     * @param theSource the description of the information source; e.g. the
     *   file name
     * @param encoding the encoding to use
     * @param stream the input stream to read
     *
     * @throws ConfigurationException in case of an error in the configuration
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Configuration config,
            final TokenStreamOptions options, final InputStream stream,
            final String theSource, final String encoding)
            throws IOException,
                ConfigurationException {

        super(true);

        int bufferSize = -1;
        String size = config.getAttribute(BUFFERSIZE_ATTRIBUTE).trim();
        if (size != null && !size.equals("")) {
            try {
                bufferSize = Integer.parseInt(size);
            } catch (NumberFormatException e) {
                throw new ConfigurationSyntaxException(e.getLocalizedMessage(),
                        config.toString() + "#" + BUFFERSIZE_ATTRIBUTE);
            }
        }

        InputStream inputStream = stream;
        if (bufferSize > 0) {
            inputStream = new BufferedInputStream(inputStream, bufferSize);
        } else if (bufferSize < 0) {
            inputStream = new BufferedInputStream(inputStream);
        }

        this.source = theSource;
        this.in = new LineNumberReader(new InputStreamReader(inputStream,
                encoding));

    }

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     * configuration is ignored in this implementation.
     * @param options ignored here
     * @param reader the reader
     * @param isFile indicator for file streams
     * @param theSource the description of the input source
     *
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Configuration config,
            final TokenStreamOptions options, final Reader reader,
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
     * @param options ignored here
     * @param theLine the string to use as source for characters
     * @param theSource the description of the input source
     *
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Configuration config,
            final TokenStreamOptions options, final String theLine,
            final String theSource) throws IOException {

        super(false);
        this.in = new LineNumberReader(new StringReader(theLine));
        this.source = theSource;
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
     * @throws ScannerException in the rare case that an IO Exception has
     * occurred.
     */
    private UnicodeChar getChar(final Tokenizer tokenizer)
            throws ScannerException {

        UnicodeChar uc = getRawChar();

        if (uc == null) {
            do {
                if (!refill()) {
                    return null;
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
                    uc = new UnicodeChar(((hexHigh < CARET_LIMIT) ? hexHigh
                            + CARET_LIMIT : hexHigh - CARET_LIMIT));
                }
            } else {
                pointer = savePointer;
            }
        }

        return uc;
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
            final Tokenizer tokenizer) throws ScannerException {

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
                throw new ScannerException(e);
            }
        } while (t == null);

        return t;
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
     * @see de.dante.extex.scanner.stream.TokenStream#isEof()
     */
    public boolean isEof() throws ScannerException {

        if (!super.isEof()) {
            return false;
        }

        for (;;) {

            if (pointer < line.length()) {
                return false;
            }

            if (!refill()) {
                return true;
            }

            pointer = 0;
        }
    }

    /**
     * Get the next line from the input reader to be processed.
     *
     * @return <code>true</code> iff the next line could be acquired.
     *
     * @throws ScannerException in case of some kind of IO error
     */
    protected boolean refill() throws ScannerException {

        if (in == null) {
            return false;
        }
        try {
            if ((line = in.readLine()) == null) {
                in.close();
                in = null;
                return false;
            }
        } catch (IOException e) {
            throw new ScannerException(e);
        }
        return true;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return source + ":" + in.getLineNumber() + "[" + pointer + "]:" + line;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitActive(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitActive(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        TokenFactory tokenFactory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        return tokenFactory.createToken(Catcode.ACTIVE, (UnicodeChar) uc,
                tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitComment(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitComment(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        endLine();
        return null;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitCr(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitCr(final Object oFactory, final Object oTokenizer,
            final Object uchar) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        Token t = null;

        if (state == MID_LINE) {
            t = factory.createToken(Catcode.SPACE, ' ', tokenizer
                    .getNamespace());
        } else if (state == NEW_LINE) {
            t = factory.createToken(Catcode.ESCAPE, (UnicodeChar) uchar, "par",
                    tokenizer.getNamespace());
        }

        endLine();
        return t;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitEscape(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitEscape(final Object oFactory, final Object oTokenizer,
            final Object uchar) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        String namespace = tokenizer.getNamespace();

        if (atEndOfLine()) {
            //empty control sequence; see "The TeXbook, Chapter 8, p. 47"
            return factory.createToken(Catcode.ESCAPE, (UnicodeChar) uchar, "",
                    namespace);
        }

        UnicodeChar uc = getChar(tokenizer);

        if (uc == null) {
            return factory.createToken(Catcode.ESCAPE, (UnicodeChar) uchar, "",
                    namespace);

        } else if (tokenizer.getCatcode(uc) == Catcode.LETTER) {
            StringBuffer sb = new StringBuffer();
            sb.append((char) (uc.getCodePoint()));
            int savedPointer = pointer;
            state = SKIP_BLANKS;

            while (!atEndOfLine() && (uc = getChar(tokenizer)) != null) {
                if (tokenizer.getCatcode(uc) != Catcode.LETTER) {
                    pointer = savedPointer;
                    return factory.createToken(Catcode.ESCAPE,
                            (UnicodeChar) uchar, sb.toString(), namespace);
                }
                sb.append((char) (uc.getCodePoint()));
                savedPointer = pointer;
            }

            return factory.createToken(Catcode.ESCAPE, (UnicodeChar) uchar, sb
                    .toString(), namespace);

        } else {
            state = MID_LINE;
            return factory.createToken(Catcode.ESCAPE, uc, namespace);

        }
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitIgnore(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        return null;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitInvalid(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        state = MID_LINE;

        throw new InvalidCharacterException((UnicodeChar) uc);
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitLeftBrace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitLeftBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.LEFTBRACE,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitLetter(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.LETTER,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitMacroParam(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitMacroParam(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.MACROPARAM,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitMathShift(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitMathShift(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.MATHSHIFT,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitOther(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitOther(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.OTHER,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitRightBrace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitRightBrace(final Object oFactory,
            final Object oTokenizer, final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.RIGHTBRACE,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * This visit method is invoked on a space token.
     *
     * @param oFactory the first argument ias the factory to use
     * @param oTokenizer the second argument is the tokenizer to use
     * @param uc the third argument is the UnicodeCharacer
     *
     * @return a space token if in mid line mode or <code>null</code> otherwise
     *
     * @throws CatcodeException in case of an error
     *
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitSpace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     * @see "The TeXbook [Chapter 8, page 47]"
     */
    public Object visitSpace(final Object oFactory, final Object oTokenizer,
            final Object uc) throws CatcodeException {

        TokenFactory factory = (TokenFactory) oFactory;

        if (state == MID_LINE) {
            state = SKIP_BLANKS;
            return factory.createToken(Catcode.SPACE, ' ',
                    Namespace.DEFAULT_NAMESPACE);
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitSubMark(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitSubMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.SUBMARK,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitSupMark(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitSupMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.SUPMARK,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

    /**
     * @see de.dante.extex.scanner.type.CatcodeVisitor#visitTabMark(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public Object visitTabMark(final Object oFactory, final Object oTokenizer,
            final Object uc) throws GeneralException {

        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        state = MID_LINE;

        return ((TokenFactory) oFactory).createToken(Catcode.TABMARK,
                (UnicodeChar) uc, tokenizer.getNamespace());
    }

}