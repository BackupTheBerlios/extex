/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.node.RuleNode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\hrule</code>.
 *
 * <doc name="vrule">
 * <h3>The Primitive <tt>\vrule</tt></h3>
 * <p>
 *  This primitive produces a vertical rule. This is a rectangular area of
 *  specified dimensions. If not overwritten the height and depth are 0pt and
 *  the width is 0.4&nbsp;pt (26214&nbsp;sp).
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;vrule&rang;
 *        := <tt>\vrule</tt>&lang;rule specification&rang;
 *
 *    &lang;rule specification&rang;
 *        := {@linkplain de.dante.extex.interpreter.TokenSource#skipSpace()
 *            &lang;optional spaces&rang;}
 *         |  &lang;rule dimension&rang; &lang;rule specification&rang;
 *
 *    &lang;rule dimension&rang;
 *        := <tt>width</tt> &lang;dimen&rang;
 *         |  <tt>height</tt> &lang;dimen&rang;
 *         |  <tt>depth</tt> &lang;dimen&rang;  </pre>
 * </p>
 * <p>
 *  The color from the typographic context is taken as foregroud color for the
 *  rule. The default color is black.
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \vrule  </pre>
 *  <pre class="TeXSample">
 *    \vrule height 2pt  </pre>
 *  <pre class="TeXSample">
 *    \vrule width 2pt depth 3mm height \dimen4  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class Vrule extends AbstractCode {

    /**
     * The constant <tt>DEFAULT_RULE</tt> contains the equivalent to 0.4pt.
     */
    private static final long DEFAULT_RULE = 26214;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Vrule(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     * @see "TeX -- The Program [463]"
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Dimen width = new Dimen(DEFAULT_RULE);
        Dimen height = new Dimen(0);
        Dimen depth = new Dimen(0);

        for (;;) {
            if (source.scanKeyword("width")) {
                width.set(context, source);
            } else if (source.scanKeyword("height")) {
                height.set(context, source);
            } else if (source.scanKeyword("depth")) {
                depth.set(context, source);
            } else {
                break;
            }
        }

        typesetter.add(new RuleNode(width, height, depth, context
                .getTypesettingContext()));
        prefix.clear();
    }
}