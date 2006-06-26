/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.scanner;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.Locator;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive
 * <code>\scantokens</code>.
 *
 * <doc name="scantokens">
 * <h3>The Primitive <tt>\scantokens</tt></h3>
 * <p>
 *  The primitive <tt>\scantokens</tt> takes an unexpanded list of tokens and
 *  uses them as a new source for an input stream. For this purpose the tokens
 *  are translated into a string which is used as if it where written to a file
 *  and read back in.
 * </p>
 * <p>
 *  The tokens from the tokens register <tt>\everyeof</tt> are inserted when the
 *  stream has no more tokens to read.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;scantokens&rang;
 *      &rarr; <tt>\scantokens</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \scantokens{abc}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Scantokens extends AbstractCode implements ExpandableCode {

    /**
     * This class encapsulates a Token stream pretending that it is a file
     * stream.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.3 $
     */
    private class TokenStreamProxy implements TokenStream {

        /**
         * The field <tt>stream</tt> contains the proxied token stream.
         */
        private TokenStream stream;

        /**
         * Creates a new object.
         *
         */
        public TokenStreamProxy(final TokenStream stream) {

            super();
            this.stream = stream;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
         */
        public boolean closeFileStream() {

            stream.closeFileStream();
            return false;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#get(
         *      de.dante.extex.scanner.type.token.TokenFactory,
         *      de.dante.extex.interpreter.Tokenizer)
         */
        public Token get(final TokenFactory factory, final Tokenizer tokenizer)
                throws ScannerException {

            return stream.get(factory, tokenizer);
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
         */
        public Locator getLocator() {

            return stream.getLocator();
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isEof()
         */
        public boolean isEof() throws ScannerException {

            return stream.isEof();
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isEol()
         */
        public boolean isEol() throws ScannerException {

            return stream.isEol();
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
         */
        public boolean isFileStream() {

            return true;
        }

        /**
         * @see de.dante.extex.scanner.stream.TokenStream#put(
         *      de.dante.extex.scanner.type.token.Token)
         */
        public void put(final Token token) {

            stream.put(token);
        }

    }

    /**
     * The field <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060616L;

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive for debugging
     */
    public Scantokens(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractCode#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens toks = source.getTokens(context, source, typesetter);
        TokenStreamFactory factory = source.getTokenStreamFactory();
        try {
            String t = toks.toText(context.escapechar());
            source.addStream(new TokenStreamProxy(factory.newInstance(t)));
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        execute(prefix, context, source, typesetter);
    }

}
