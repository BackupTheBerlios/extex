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
 * This class provides an implementation for the primitive <code>\moveright</code>.
 *
 * <doc name="moveright">
 * <h3>The Primitive <tt>\moveright</tt></h3>
 * <p>
 *  The primitive <tt>\moderight</tt> takes a box and a length and moves it
 *  rightwards by the amount specified by the length.
 *  If the length is negative then the move is done leftwards.
 * </p>
 * <p>
 *  The primitive <tt>\moveright</tt> is the counterpart to
 *  {@link de.dante.extex.interpreter.primitives.typesetter.displace.Moveleft <tt>\moveleft</tt>}.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;moveright&rang;
 *      &rarr; <tt>\moveright</tt> {@linkplain
 *        de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *        &lang;dimen&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getBox(Context,de.dante.extex.typesetter.Typesetter)
 *        &lang;box&rang;}  </pre>
 *
 * <p>
 *  The color from the typographic context is taken as foregroud color for the
 *  rule. The default color is black.
 * </p>
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \moveright 2em \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \moveright -1pt \hbox to 120pt {abc}  </pre>
 *  <pre class="TeXSample">
 *    \moveright 2mm \hbox spread 12pt {abc}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public class Moveright extends AbstractBoxPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Moveright(final String name) {

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

        Dimen move = Dimen.parse(context, source, typesetter);
        Box box = source.getBox(null, context, typesetter);
        if (box != null && !box.isVoid()) {
            move.add(box.getMove());
            box.setMove(move);
        }
        return box;
    }

}
