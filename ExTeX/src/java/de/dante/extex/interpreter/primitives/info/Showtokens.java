/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.info;

import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.EofInToksException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.interpreter.type.tokens.TokensConvertible;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\showtokens</code>.
 *
 * <doc name="showtokens">
 * <h3>The Primitive <tt>\showtokens</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;showtokens&rang;
 *       &rarr; <tt>\showtokens</tt> ...  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \showtokens {1234}  </pre>
 *  <pre class="TeXSample">
 *    \showtokens \expandafter{\jobname}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Showtokens extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060603L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Showtokens(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens tokens = getTokens(context, source, typesetter);
        logger.info("\n> " + tokens.toText() + ".\n");
    }

    /**
     * Collect some tokens but expand until the starting brace is found.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the tokens collected
     *
     * @throws InterpreterException in case of an error
     */
    private Tokens getTokens(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Tokens tokens = new Tokens();
        Token token = source.scanToken(context);

        if (token instanceof LeftBraceToken) {
            int balance = 1;

            for (token = source.getToken(context); token != null; token = source
                    .getToken(context)) {

                if (token.isa(Catcode.LEFTBRACE)) {
                    ++balance;
                } else if (token instanceof RightBraceToken && --balance <= 0) {
                    return tokens;
                }

                tokens.add(token);
            }

            throw new EofInToksException(printableControlSequence(context));

        } else if (token instanceof CodeToken) {
            Code code = context.getCode((CodeToken) token);
            if (code instanceof TokensConvertible) {
                return ((TokensConvertible) code).convertTokens(context,
                        source, typesetter);
            }

        } else if (token == null) {
            throw new EofException(getLocalizer().format("Tokens.Text"));
        }

        throw new MissingLeftBraceException("???");
    }

}
