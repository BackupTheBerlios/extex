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
package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\ifvbox</code>.
 *
 * <doc name="ifvbox">
 * <h3>The Primitive <tt>\ifvbox</tt></h3>
 * <p>
 *  The primitive takes one expanded integer argument.
 *  The conditional is true iff the box denoted by the argument is a vertical
 *  box.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\ifvbox</tt> {@linkplain de.dante.extex.interpreter.TokenSource#scanInteger() &lang;number&rang;} &lang;true text&rang; <tt>\fi</tt>
 *
 *    <tt>\ifvbox</tt> {@linkplain de.dante.extex.interpreter.TokenSource#scanInteger() &lang;number&rang;} &lang;true text&rang; <tt>\else</tt> &lang;false text&rang; <tt>\fi</tt> </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \ifvbox255 abc \fi  </pre>
 *  <pre class="TeXSample">
 *    \ifvbox\count120 abc \fi  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class Ifvbox extends AbstractIf {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifvbox(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.conditional.AbstractIf#conditional(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source);
        Box box = context.getBox(key);
        return (box != null && box.isVbox());
    }

    /**
     * Return the key (the number) for the box register.
     *
     * @param source the source for new tokens
     *
     * @return the key for the box register
     *
     * @throws GeneralException in case of an error
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return "box#" + Long.toString(source.scanNumber());
    }

}
