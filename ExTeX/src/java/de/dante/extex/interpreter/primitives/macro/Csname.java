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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CsConvertible;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.SpaceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\csname</code>.
 *
 * <doc name="csname">
 * <h3>The Primitive <tt>\csname</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;csname&rang;
 *      &rarr; <tt>\csname</tt> &lang;...&rang; <tt>\endcsname</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \csname abc\endcsname  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.22 $
 */
public class Csname extends AbstractCode
        implements
            ExpandableCode,
            CsConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Csname(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.CsConvertible#convertCs(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Token convertCs(final Context context, final TokenSource source)
            throws InterpreterException {

        Tokens toks = scanToEndCsname(context, source, null);

        try {
            return context.getTokenFactory().createToken(Catcode.ESCAPE,
                    new UnicodeChar(context.escapechar()), toks.toString(),
                    context.getNamespace());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
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

        Tokens toks = scanToEndCsname(context, source, null);

        try {
            source.push(context.getTokenFactory().createToken(Catcode.ESCAPE,
                    new UnicodeChar(context.escapechar()), toks.toString(),
                    context.getNamespace()));
        } catch (CatcodeException e) {
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

        Tokens toks = scanToEndCsname(context, source, null);

        try {
            source.push(context.getTokenFactory().createToken(Catcode.ESCAPE,
                    new UnicodeChar(context.escapechar()), toks.toString(),
                    context.getNamespace()));
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
        //TODO gene: this might not be correct
    }

    /**
     * Expand tokens and collect the result until <tt>\endcsname</tt> is found.
     * In fact the termination condition is that a Token is found which is
     * assigned to {@link Endcsname Endcsname}.
     *
     * @param context the interpreter context
     * @param source the source fot new tokens
     * @param typesetter the typesetter
     *
     * @return the Tokens found while scanning the input tokens
     *
     * @throws InterpreterException in case of an error
     */
    private Tokens scanToEndCsname(final Context context,
            final TokenSource source, Typesetter typesetter)
            throws InterpreterException {

        Tokens toks = new Tokens();
        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {

            if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);

                if (code == null) {
                    throw new EofException(printableControlSequence(context));
                } else if (code instanceof Endcsname) {
                    return toks;
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                    //TODO gene: handle expansion???
                }

            } else if (!(t instanceof SpaceToken)) {
                toks.add(t);
            }
        }
        return toks;
    }

}