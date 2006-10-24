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

package de.dante.extex.scanner.stream.impl32;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.scanner.stream.exception.ScannerNoHexDigitFoundException;
import de.dante.extex.scanner.stream.exception.ScannerNoUnicodeNameException;
import de.dante.extex.scanner.stream.impl.TokenStreamImpl;
import de.dante.extex.scanner.type.Catcode;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class contains an implementation of a token stream which is fed from a
 * Reader.
 * <p>
 * <ul>
 *  <li>'^^^UnicodeName;' use the Unicode name for the character</li>
 *  <li>'^^^^ab07' a la Omega<br/>
 *  It use 4 hex digit or less, if a non hex digit are found.
 *  </li>
 * </ul>
 * </p>
 *
 * @see de.dante.extex.scanner.stream.impl.TokenStreamImpl
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */

public class TokenStreamImpl32 extends TokenStreamImpl {

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     *        configuration is ignored in this implementation.
     * @param options ignored here
     * @param theSource the description of the information source; e.g. the
     *        file name
     * @param encoding the encoding to use
     * @param stream the input stream to read
     *
     * @throws ConfigurationException in case of an error in the configuration
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl32(final Configuration config,
            final TokenStreamOptions options, final InputStream stream,
            final String theSource, final String encoding)
            throws IOException,
                ConfigurationException {

        super(config, options, stream, theSource, encoding);
    }

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     *        configuration is ignored in this implementation.
     * @param options ignored here
     * @param reader the reader
     * @param isFile indicator for file streams
     * @param theSource the description of the input source
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl32(final Configuration config,
            final TokenStreamOptions options, final Reader reader,
            final Boolean isFile, final String theSource) throws IOException {

        super(config, options, reader, isFile, theSource);
    }

    /**
     * Creates a new object.
     *
     * @param config the configuration object for this instance; This
     *        configuration is ignored in this implementation.
     * @param options ignored here
     * @param theLine the string to use as source for characters
     * @param theSource the description of the input source
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl32(final Configuration config,
            final TokenStreamOptions options, final String theLine,
            final String theSource) throws IOException {

        super(config, options, theLine, theSource);
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamImpl#getChar(
     *      de.dante.extex.interpreter.Tokenizer)
     */
    protected UnicodeChar getChar(final Tokenizer tokenizer)
            throws ScannerException {

        if (saveChar != null) {
            UnicodeChar uc = saveChar;
            saveChar = null;
            return uc;
        }

        UnicodeChar uc = getRawChar();

        if (uc == null) {
            do {
                if (!refill()) {
                    return null;
                }
                uc = getRawChar();

            } while (uc == null);

            state = NEW_LINE;
        }

        // CAT_SUP_MARK -> scan ('^^', '^^^', '^^^^')
        if (tokenizer.getCatcode(uc) == Catcode.SUPMARK) {

            int savePointer = pointer;
            UnicodeChar c = getRawChar();

            if (uc.equals(c)) {
                c = getRawChar();
                if (c == null) {
                    return null;
                } else if (tokenizer.getCatcode(c) == Catcode.SUPMARK) {
                    c = getRawChar();
                    if (tokenizer.getCatcode(c) == Catcode.SUPMARK) {
                        // '^^^^'
                        uc = scanHex(MAX_HEX_DIGITS, tokenizer);

                    } else {
                        // '^^^'
                        pointer--;
                        String unicodename = scanUnicodeName();
                        UnicodeChar ucn = UnicodeChar.get(unicodename);

                        if (ucn == null) {
                            throw new ScannerNoUnicodeNameException(unicodename);
                        }
                        uc = ucn;
                    }
                } else {
                    // '^^'
                    int hexHigh = hex2int(c.getCodePoint());
                    if (hexHigh >= 0) {
                        savePointer = pointer;
                        uc = getRawChar();
                        if (uc == null) {
                            uc = UnicodeChar.get(hexHigh);
                        } else {
                            int hexLow = hex2int(uc.getCodePoint());
                            if (hexLow < 0) {
                                pointer = savePointer;
                                uc = UnicodeChar.get(hexHigh);
                            } else {
                                uc = UnicodeChar.get((hexHigh << SHIFT4)
                                        + hexLow);
                            }
                        }
                    } else if (c != null) {
                        hexHigh = c.getCodePoint();
                        uc = UnicodeChar.get(((hexHigh < CARET_LIMIT) ? hexHigh
                                + CARET_LIMIT : hexHigh - CARET_LIMIT));
                    }
                }
            } else {
                pointer = savePointer;
            }
        }

        return uc;
    }

    /**
     * scan a hex number (max. n digits)
     * @param n         number of digits
     * @param tokenizer the tokenizer
     * @return UnicodeChar of a hex number
     * @throws ScannerException if no hex digit is found
     */
    private UnicodeChar scanHex(final int n, final Tokenizer tokenizer)
            throws ScannerException {

        StringBuffer buf = new StringBuffer();
        UnicodeChar uc = null;
        for (int i = 0; i < n; i++) {

            uc = getRawChar();
            if (uc == null) {
                break;
            }

            char hex = hex2char(uc.getCodePoint());

            // hex digit ?
            if (hex > 0) {
                buf.append(hex);
            } else {
                break;
            }
        }
        if (buf.length() == 0) {
            throw new ScannerNoHexDigitFoundException();
        }
        return UnicodeChar.get(Integer.parseInt(buf.toString(), HEX));
    }

    /**
     * Analyze a character and return its hex value as char.
     *
     * @param c     The character code to analyze.
     * @return Returns the char value of a hex digit
     *         or 0 if no hex digit is given.
     */
    private char hex2char(final int c) {

        char ch = (char) Character.toLowerCase((char) c);

        if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
            return ch;
        }
        return (char) 0;

    }

    /**
     * hex
     */
    private static final int HEX = 16;

    /**
     * max hex digits (for Omega 4 digits)
     */
    private static final int MAX_HEX_DIGITS = 4;

    /**
     * shift 4
     */
    private static final int SHIFT4 = 4;

    /**
     * Scan a Unicode name.
     * <pre>
     * ^^^^NAME;
     * </pre>
     *
     * @return Returns Unicode name as <code>String</code>.
     * @throws ScannerException if an error occurs
     */
    private String scanUnicodeName() throws ScannerException {

        StringBuffer buf = new StringBuffer();
        while (true) {
            UnicodeChar uc = getRawChar();

            // parse until ';'
            if (uc.getCodePoint() != CPOINT.getCodePoint()) {
                buf.append(uc.toString());
            } else {
                if (buf.length() == 0) {
                    throw new ScannerNoUnicodeNameException();
                }
                // ';' not used in the name
                break;
            }
        }
        return buf.toString();
    }

    /**
     * Unicode char for ';'
     */
    private static final UnicodeChar CPOINT = UnicodeChar.get(';');

}
