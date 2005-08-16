/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a token source which is fed from a string.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.23 $
 */
public class StringSource extends Moritz {

    /**
     * This Token stream is fed from a CharSequence.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.23 $
     */
    private class TStream implements TokenStream {

        /**
         * The field <tt>next</tt> contains the pointer to the next char to read.
         */
        private int next = 0;

        /**
         * The field <tt>cs</tt> contains the char sequence containing the chars
         * to read.
         */
        private CharSequence cs;

        /**
         * The field <tt>stack</tt> contains the stack for pushed tokens.
         */
        private List stack = new ArrayList();

        /**
         * Creates a new object.
         *
         * @param cs the character sequence to read from
         */
        protected TStream(final CharSequence cs) {

            this.cs = cs;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
         */
        public boolean closeFileStream() {

            next = cs.length();
            return false;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#get(
         *      de.dante.extex.scanner.type.TokenFactory,
         *      de.dante.extex.interpreter.Tokenizer)
         */
        public Token get(final TokenFactory factory, final Tokenizer tokenizer)
                throws ScannerException {

            int size = stack.size();
            if (size > 0) {
                return (Token) stack.remove(size - 1);
            }
            if (next < cs.length()) {
                UnicodeChar c = new UnicodeChar(cs.charAt(next++));
                try {
                    return factory.createToken(tokenizer.getCatcode(c), c,
                            Namespace.DEFAULT_NAMESPACE);
                } catch (CatcodeException e) {
                    throw new ScannerException(e);
                }
            }
            return null;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
         */
        public Locator getLocator() {

            return new Locator("", 0, cs.toString(), next);
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isEof()
         */
        public boolean isEof() throws ScannerException {

            return next >= cs.length();
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
         */
        public boolean isFileStream() {

            return false;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#put(
         *      de.dante.extex.scanner.type.Token)
         */
        public void put(final Token token) {

            stack.add(token);
        }
    }

    /**
     * Creates a new object.
     *
     * @param cs the character sequence to read from
     *
     * @throws ConfigurationException in case of errors in the configuration
     */
    public StringSource(final CharSequence cs) throws ConfigurationException {

        super();
        addStream(new TStream(cs));
    }

}