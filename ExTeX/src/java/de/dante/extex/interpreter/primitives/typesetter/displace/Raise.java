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

package de.dante.extex.interpreter.primitives.typesetter.displace;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.typesetter.box.AbstractBoxPrimitive;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\raise</code>.
 *
 * <doc name="raise">
 * <h3>The Primitive <tt>\raise</tt></h3>
 * <p>
 *  The primitive <tt>\raise</tt> takes a box and a length and shifts up the
 *  amount specified by the length. If the length is negative then the shift
 *  is done downwards.
 * </p>
 * <p>
 *  The primitive <tt>\lower</tt> is the counterpart to
 *  {@link de.dante.extex.interpreter.primitives.typesetter.displace.Lower <tt>\lower</tt>}.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;raise&rang;
 *      &rarr; <tt>\raise</tt> {@linkplain
 *        de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *        &lang;dimen&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getBox(Context,de.dante.extex.typesetter.Typesetter)
 *        &lang;box&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \raise 2em \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \raise -1pt \hbox to 120pt {abc}  </pre>
 *  <pre class="TeXSample">
 *    \raise 2mm \hbox spread 12pt {abc}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class Raise extends AbstractBoxPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Raise(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Dimen amount = Dimen.parse(context, source, typesetter);
        Box box = source.getBox(null, context, typesetter);
        if (box != null) {
            amount.subtract(box.getShift());
            box.setShift(amount);
        }
        return box;
    }

}
