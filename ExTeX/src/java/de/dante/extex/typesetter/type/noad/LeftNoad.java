/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
package de.dante.extex.typesetter.type.noad;

import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.MathDelimiter;
import de.dante.extex.typesetter.type.noad.util.MathContext;


/**
 * This Noad carries a delimiter which is set on the left side of the math
 * material following it. This delmitier adjusts its height to the height of the
 * following material.
 *
 * @see "TTP [687]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LeftNoad extends AbstractNoad {

    /**
     * The field <tt>left</tt> contains the left delimiter.
     */
    private MathDelimiter left;

    /**
     * Creates a new object.
     *
     * @param left the glue
     */
    public LeftNoad(final MathDelimiter left) {

        super();
        this.left = left;
    }

    /**
     *
     * @see "TTP [762]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        left.typeset(list, mathContext, context);
    }

}
