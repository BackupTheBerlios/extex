/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\lower</code>.
 *
 * <doc name="lower">
 * <h3>The Primitive <tt>\lower</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;lower&rang;
 *      &rarr; <tt>\lower</tt> {@linkplain
 *        de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *        &lang;dimen&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getBox(de.dante.extex.typesetter.Typesetter)
 *        &lang;box&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \lower 2em \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \lower -1pt \hbox to 120pt {abc}  </pre>
 *  <pre class="TeXSample">
 *    \lower 2mm \hbox spread 12pt {abc}  </pre>
 * </p>
 * </doc>
 *
 * <p>
 * Examples
 * </p>
 * <pre>
 *  \lower 2em \hbox{abc}
 *  \lower -1pt \hbox to 120pt {abc}
 *  \lower 2mm \hbox spread 12pt {abc}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class Lower extends AbstractCode implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Lower(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Box box = getBox(context, source, typesetter);
        typesetter.add(box.getNodes());
        return true;
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Dimen amount = new Dimen(context, source);
        Token t = source.getToken();
        if (t == null || !(t instanceof CodeToken)) {
            throw new HelpingException(getLocalizer(), "TTP.BoxExpected");
        }
        Code code = context.getCode((CodeToken) t);
        if (code == null || !(code instanceof Boxable)) {
            throw new HelpingException(getLocalizer(), "TTP.BoxExpected");
        }
        Box box = ((Boxable) code).getBox(context, source, typesetter);

        amount.add(box.getShift());
        box.setShift(amount);
        return box;
    }

}