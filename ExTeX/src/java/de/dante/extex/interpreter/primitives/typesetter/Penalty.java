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

package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.node.PenaltyNode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\penalty</code>.
 *
 * <doc name="penalty">
 * <h3>The Primitive <tt>\penalty</tt></h3>
 * <p>
 *  This primitive inserts penalty into the current node list. In vertical mode
 *  the page builder is also invoked.
 * </p>
 * <p>
 *  A penalty of 10000 or more will inhibit a break at this position. A penalty
 *  of -10000 or less will force a break at this position.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;penalty&rang;
 *        &rarr; <tt>\penalty</tt> {@linkplain
 *    de.dante.extex.interpreter.TokenSource#scanNumber()
 *    &lang;8-bit&nbsp;number&rang;}  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \penalty 123  </pre>
 *  <pre class="TeXSample">
 *    \penalty -456  </pre>
 *  <pre class="TeXSample">
 *    \penalty -\count254  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Penalty extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Penalty(final String name) {

        super(name);
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

        long penalty = 0;
        try {
            penalty = source.scanInteger();
        } catch (GeneralException e) {
            typesetter.add(new PenaltyNode(penalty));
            throw e;
        }
        typesetter.add(new PenaltyNode(penalty));
        return true;
    }

}