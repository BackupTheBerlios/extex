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

package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\relax</code>.
 *
 * <doc name="parshape">
 * <h3>The Primitive <tt>\parshape</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;parshape&rang;
 *        &rarr; <tt>\parshape</tt>  ... </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \parshape 3 20pt \linewidth
 *                20pt \linewidth
 *                0pt \linewidth </pre>
 *  <pre class="TeXSample">
 *    \parshape 0 </pre>
 * </p>
 *
 * <h3>\parshape as special register</h3>
 * <p>
 * \parshape acts as special register which can be queried. It returns the
 * size of the current parshape specification or 0 if none is present.
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class Parshape extends AbstractCode implements CountConvertible, Theable {

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        return new Tokens(context, Long.toString(context.getParshape()
                .getSize()));
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Parshape(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        return context.getParshape().getSize();
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        long n = source.scanInteger();
        if (n <= 0) {
            context.setParshape(null);
        } else {
            ParagraphShape parshape = new ParagraphShape();
            while (n >= 0) {
                Dimen left = new Dimen(context, source);
                Dimen right = new Dimen(context, source);
                parshape.add(left, right);
            }
            context.setParshape(parshape);
        }
        return true;
    }
}