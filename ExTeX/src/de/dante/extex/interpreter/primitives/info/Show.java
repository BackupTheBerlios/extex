/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.primitives.info;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Showable;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\show</code>.
 *
 * <doc name="show">
 * <h3>The Primitive <tt>\show</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;show&rang;
 *     := <tt>\show</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken()
 *       &lang;token&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \show\abc  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Show extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Show(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token t = source.getToken();
        if (t == null) {
            throw new RuntimeException("unimplemented");
        }
        source.update("message", meaning(t, context).toText());
        prefix.clear();
    }

    /**
     * Get the descriptions of a token as token list.
     *
     * @param t the token to describe
     * @param context the interpreter context
     *
     * @return the token list describing the token
     *
     * @throws GeneralException in case of an error
     */
    protected Tokens meaning(final Token t, final Context context)
            throws GeneralException {

        if (!(t instanceof CodeToken)) {
            return new Tokens(context, t.toString());
        }
        Tokens toks;

        if (t instanceof ControlSequenceToken) {
            toks = new Tokens(context, Character.toString((char) (context
                    .getCount("escapechar").getValue())));
        } else {
            toks = new Tokens();
        }
        toks.add(new Tokens(context, t.getValue()));
        toks.add(new Tokens(context, "="));
        Code code = context.getCode(t);
        if (code == null) {
            toks.add(new Tokens(context, "undefined")); // TODO: i18n?
        } else if ((code instanceof Showable)) {
            toks.add(((Showable) code).show(context));
        } else {
            toks.add(new Tokens(context, Character.toString((char) (context
                    .getCount("escapechar").getValue()))));
            toks.add(new Tokens(context, t.getValue()));
        }
        return toks;
    }
}
