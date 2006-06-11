/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter.paragraph;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;

/**
 * This class provides an implementation for the primitive <code>\relax</code>.
 *
 * <doc name="parshape">
 * <h3>The Primitive <tt>\parshape</tt></h3>
 * <p>
 *  The primitive <tt>\parshape</tt> is a declaration of the shape of the
 *  paragraph. With its help it is possible to control the left and right margin
 *  of the current paragraph.
 * </p>
 * <p>
 *  The shape of the paragraph is controlled on a line base. For each line the
 *  left indentation and the width are given. The first argument of
 *  <tt>\parshape</tt> determines the number of such pairs to follow.
 * </p>
 * <p>
 *  When the paragraph is typeset the lines are indented and adjusted according
 *  to the specification given. If there are more lines specified as actually
 *  present in the current paragraph then the remaining specifications are
 *  discarded at the end of the paragraph. If there are less lines then the last
 *  specification is repeated.
 * </p>
 * <p>
 *  If several <tt>\parshape</tt> declarations are given in one paragraph then
 *  the one is used which is in effect at the end of the paragraph. This means
 *  that later declarations overrule earlier ones.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;parshape&rang;
 *        &rarr; <tt>\parshape</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *        &lang;8-bit&nbsp;number&rang;} ... </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \parshape 3 20pt \linewidth
 *                20pt \linewidth
 *                0pt \linewidth </pre>
 *  <pre class="TeXSample">
 *    \parshape 0 </pre>
 *
 * <h3><tt>\parshape</tt> as special integer</h3>
 * <p>
 *  <tt>\parshape</tt> acts as special count register which can be queried.
 *  It returns the size of the current parshape specification or 0 if none is
 *  present.
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \count1=\parshape  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Parshape extends AbstractCode implements CountConvertible, Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

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
            final Typesetter typesetter) throws InterpreterException {

        ParagraphShape parshape = context.getParshape();
        return (parshape != null ? parshape.getSize() : 0);
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

        long n = Count.scanInteger(context, source, typesetter);
        if (n <= 0) {
            context.setParshape(null);
        } else {
            ParagraphShape parshape = new ParagraphShape();
            while (n-- > 0) {
                Dimen left = Dimen.parse(context, source, typesetter);
                Dimen right = Dimen.parse(context, source, typesetter);
                parshape.add(left, right);
            }
            context.setParshape(parshape);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        ParagraphShape parshape = context.getParshape();
        return new Tokens(context, parshape != null ? Long.toString(parshape
                .getSize()) : "0");
    }

}
