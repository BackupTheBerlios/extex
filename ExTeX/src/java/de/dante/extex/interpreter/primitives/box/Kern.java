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
import de.dante.extex.interpreter.type.node.KernNode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\kern</code>.
  *
 * <doc name="kern">
 * <h3>The Primitive <tt>\kern</tt></h3>
 * <p>
 *  This primitive produces a horizontal or vertical kerning. This is a (minor)
 *  adjustment of the position. The meaning depends on the current mode of the
 *  typesetter. In vertical modes it means a vertival adjustment. Otherwise it
 *  means a horizontal adjustment.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;kern&rang;
 *        := <tt>\kern</tt>&lang;dimen&rang;  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \kern 12pt  </pre>
 *  <pre class="TeXSample">
 *    \kern -3mm  </pre>
 *  <pre class="TeXSample">
 *    \kern -\dimen123  </pre>
 * </p>
 * </doc>
 *
 * <p>
 *  The effect of the primitive is that a
 *  {@link de.dante.extex.interpreter.type.node.KernNode KernNode} is is sent to
 *  the typesetter.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class Kern extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Kern(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Dimen kern = new Dimen();
        try {
            kern.set(context, source);
        } catch (GeneralException e) {
            typesetter.add(new KernNode(kern));
            throw e;
        }
        typesetter.add(new KernNode(kern));
        return true;
    }

}