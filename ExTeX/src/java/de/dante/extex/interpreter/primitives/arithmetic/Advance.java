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

package de.dante.extex.interpreter.primitives.arithmetic;

import de.dante.extex.i18n.CantUseAfterHelpingException;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.arithmetic.Advanceable;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\advance</code>.
 *
 * <doc name="advance">
 * <h3>The Primitive <tt>\advance</tt></h3>
 * <p>
 *  This primitive implements an assignment. The variable given as next tokens
 *  is incremented by the quantity given after the optional <tt>by</tt>.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *   &lang;advance&rang;
 *     &rarr; &lang;optional prefix&rang; <tt>\advance</tt> &lang;advancable&rang;
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang;
 *
 *   &lang;advancable&rang;
 *     &rarr; &lang;integer variable&rang; &lang;optional <tt>by</tt>&rang; {@linkplain
 *      de.dante.extex.interpreter.TokenSource#scanNumber()
 *      &lang;number&rang;}
 *      |  &lang;dimen variable&rang; &lang;optional <tt>by</tt>&rang; {@linkplain
 *        de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *        &lang;dimen&rang;}
 *      |  &lang;glue variable&rang; &lang;optional <tt>by</tt>&rang; &lang;glue&rang;
 *      |  &lang;muglue variable&rang; &lang;optional <tt>by</tt>&rang; &lang;muglue&rang;
 *
 *   &lang;optional <tt>by</tt>&rang;
 *     &rarr; [by]
 *      |  &lang;optional spaces&rang;
 *   </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \advance\count12 345  </pre>
 *  <pre class="TeXSample">
 *    \advance\count12 by -345  </pre>
 * </p>
 * </doc>
 *
 *
 * @see de.dante.extex.interpreter.type.arithmetic.Advanceable
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class Advance extends AbstractAssignment {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Advance(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.getToken();

        if (!(cs instanceof CodeToken)) {
            throw new CantUseAfterHelpingException(cs.toString(),
                    printableControlSequence(context));
        }

        Code code = context.getCode((CodeToken) cs);

        if (code == null) {
            throw new HelpingException(getLocalizer(), "TTP.UndefinedToken", //
                    cs.toString());
        } else if (code instanceof Advanceable) {
            ((Advanceable) code).advance(prefix, context, source);
        } else {
            throw new CantUseAfterHelpingException(cs.toString(),
                    printableControlSequence(context));
        }
    }

}