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

import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.MathDelimiter;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * This Noad carries a delimiter which is set in the middle between math
 * material surrounding it. This delmitier adjusts its height to the height of
 * the surrounding material.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class MiddleNoad extends AbstractNoad {

    /**
     * The field <tt>delimiter</tt> contains the middle delimiter.
     */
    private MathDelimiter delimiter;

    /**
     * Creates a new object.
     *
     * @param delimiter the delimiter
     */
    public MiddleNoad(final MathDelimiter delimiter) {

        super();
        this.delimiter = delimiter;
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    public void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("middle");
        delimiter.toString(sb);
    }

    /**
     *
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        delimiter.typeset(list, mathContext, context);
    }

}
