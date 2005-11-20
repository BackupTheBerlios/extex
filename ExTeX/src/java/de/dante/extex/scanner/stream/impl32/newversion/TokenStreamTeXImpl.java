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

package de.dante.extex.scanner.stream.impl32.newversion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.scanner.stream.exception.InvalidCharacterScannerException;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.CatcodeVisitor;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.file.InputLineDecodeReader;
import de.dante.util.file.InputLineDecodeStream;
import de.dante.util.file.InputLineDecodeString;
import de.dante.util.file.InputLineDecoder;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationSyntaxException;

/**
 * This class implements a token stream.
 * <p>
 * This implementations adheres as much as possible to the TeX standard.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class TokenStreamTeXImpl extends AbstractTokenStreamImpl
        implements
            TokenStream,
            CatcodeVisitor {

    /**
     * The field <tt>in</tt> contains the reader for lines.
     */
    private InputLineDecoder in;

    /**
     * The field <tt>line</tt> contains the current line of input.
     */
    private CharBuffer line = CharBuffer.allocate(0);

    /**
     * The index in the buffer for the next character to consider. This
     * is an invariant: after a character is read this pointer has to be
     * advanced.
     */
    private int pointer = 1;

    /**
     * The field <tt>source</tt> contains
     * the description of the source for tokens.
     */
    private String source;

    /**
     * The field <tt>state</tt> contains the current state of operation.
     */
    private State state = NEW_LINE;

    /**
     * tokenstream options
     */
    private TokenStreamOptions tsoptions;

    /**
     * the initencoding
     */
    private String initencoding;

    /**
     * Creates a new object.
     *
     * @param config    the configuration object for this instance
     * @param options   the options
     * @param theSource the description of the information source;
     *                  e.g. the file name
     * @param encoding  the encoding
     * @param stream    the input stream to read
     *
     * @throws ConfigurationException in case of an error in the configuration
     * @throws IOException in case of an IO error
     */
    public TokenStreamTeXImpl(final Configuration config,
            final TokenStreamOptions options, final InputStream stream,
            final String theSource, final String encoding)
            throws IOException,
                ConfigurationException {

        super(false);

        int bufferSize = readBufferSize(config);

        InputStream inputStream = stream;
        if (bufferSize > 0) {
            inputStream = new BufferedInputStream(inputStream, bufferSize);
        } else {
            inputStream = new BufferedInputStream(inputStream);
        }

        source = theSource;
        in = new InputLineDecodeStream(inputStream);
        tsoptions = options;
        initencoding = encoding;
    }

    /**
     * Read the buffersize from the config.
     *
     * @param config    the config
     * @return Returns the buffersize or -1 if attribute empty.
     * @throws ConfigurationSyntaxException if an error occurs.
     */
    private int readBufferSize(final Configuration config)
            throws ConfigurationSyntaxException {

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
        return bufferSize;
    }

    /**
     * Creates a new object.
     *
     * @param config    the configuration object for this instance
     * @param options   the options
     * @param reader    the reader
     * @param isFile    indicator for file streams
     * @param theSource the description of the input source
     *
     * @throws IOException in case of an IO error
     * @throws ConfigurationException in case of an error in the configuration
     */
    public TokenStreamTeXImpl(final Configuration config,
            final TokenStreamOptions options, final Reader reader,
            final Boolean isFile, final String theSource)
            throws IOException,
                ConfigurationException {

        super(isFile.booleanValue());

        int bufferSize = readBufferSize(config);

        BufferedReader breader = null;
        if (bufferSize > 0) {
            breader = new BufferedReader(reader, bufferSize);
        } else {
            breader = new BufferedReader(reader);
        }

        in = new InputLineDecodeReader(breader);
        source = theSource;
        tsoptions = options;
        initencoding = null;
    }

    /**
     * Creates a new object.
     *
     * @param config    the configuration object for this instance;
     *                  This configuration is ignored in this implementation.
     * @param options   the options
     * @param theLine   the string to use as source for characters
     * @param theSource the description of the input source
     *
     * @throws IOException in case of an IO error
     */
    public TokenStreamTeXImpl(final Configuration config,
            final TokenStreamOptions options, final String theLine,
            final String theSource) throws IOException {

        super(false);
        in = new InputLineDecodeString(theLine);
        source = theSource;
        tsoptions = options;
        initencoding = null;

    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public Locator getLocator() {

        return new Locator(source, (in == null ? 0 : in.getLineNumber()), line
                .toString(), pointer - 1);
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBaseImpl#getNext(
     *      de.dante.extex.scanner.type.token.TokenFactory,
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
     * Checks whether the pointer is at the end of line.
     *
     * @return Returns  <code>true</code> if the next reading operation
     *         would try to refill the line buffer
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
     * @return Returns the character or <code>null</code>
     *         if no more character is available
     *
     * @throws ScannerException in the rare case that an IOException has
     * occurred.
     */
    private UnicodeChar getChar(final Tokenizer tokenizer)
            throws ScannerException {

        UnicodeChar uc = getRawChar();

        if (uc == null) {
            do {
                try {
                    if (!refill()) {
                        return null;
                    }
                } catch (IOException e) {
                    throw new ScannerException(e);
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
     * Get the next character from the input line.
     *
     * @return the next raw character or <code>null</code> if none is available.
     */
    private UnicodeChar getRawChar() {

        if (pointer < line.length()) {
            return new UnicodeChar(line, pointer++);
        }

        return null; //(pointer++ > line.length() ? null : CR);
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
        // get encoding
        String enc = initencoding;
        if (tsoptions != null) {
            String inputencoding = tsoptions.getToksOption("inputencoding")
                    .toText();
            if (inputencoding != null && !inputencoding.trim().equals("")) {
                enc = inputencoding;
            }
        }
        if ((line = in.readLine(enc)) == null) {
            in.close();
            in = null;
            return false;
        }
        return true;
    }

    // ------------------------------------------------
    // ------------------------------------------------
    // ------------------------------------------------
    // ------------------------------------------------

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
            final Object uc) throws GeneralException {

        TokenFactory factory = (TokenFactory) oFactory;
        Tokenizer tokenizer = (Tokenizer) oTokenizer;
        Token t = null;

        if (state == MID_LINE) {
            t = factory.createToken(Catcode.SPACE, ' ', tokenizer
                    .getNamespace());
        } else if (state == NEW_LINE) {
            t = factory.createToken(Catcode.ESCAPE, new UnicodeChar('\\'),
                    "par", tokenizer.getNamespace());
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

        throw new InvalidCharacterScannerException((UnicodeChar) uc);
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