/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.math.MathDelimiter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad carries a delimiter which is set on the left side of the math
 * material. This delimiter adjusts its height to the height of the
 * material following.
 *
 * @see "TTP [687]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class LeftNoad extends AbstractNoad {

    /**
     * The field <tt>delimiter</tt> contains the left delimiter.
     */
    private MathDelimiter delimiter;

    /**
     * The field <tt>noad</tt> contains the noad following the left delimiter.
     */
    private Noad noad;

    /**
     * Creates a new object.
     *
     * @param noad the noad following the left delimiter
     * @param delimiter the glue
     */
    public LeftNoad(final Noad noad, final MathDelimiter delimiter) {

        super();
        this.delimiter = delimiter;
        this.noad = noad;
    }

    /**
     * @see "TTP [696]"
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    public void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("left");
        delimiter.toString(sb);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        throw new UnsupportedOperationException();
    }

    /**
     * Translate a Noad into a NodeList.
     *
     * @param previousNoad the previous noad
     * @param noads the list of noads currently processed
     * @param index the index of the current node in the list
     * @param list the list to add the nodes to. This list contains the Nodes
     *  previously typeset. Thus it can be used to look back
     * @param mathContext the context to consider
     * @param logger th logger for debugging
     * @param height the target height. If <code>null</code> then the natural
     *  height is used
     * @param depth the target depth. If <code>null</code> then the natural
     *  depth is used
     *
     * @throws TypesetterException in case of a problem
     * @throws ConfigurationException in case of a configuration problem
     *
     * @see "TTP [762]"
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger,
            final Dimen height, final Dimen depth)
            throws TypesetterException,
                ConfigurationException {

        getSpacingClass().addClearance(
                (previousNoad != null ? previousNoad.getSpacingClass() : null),
                list, mathContext);

        HorizontalListNode hlist = new HorizontalListNode();

        noad.typeset(previousNoad, noads, index, hlist, mathContext, logger);
        height.max(hlist.getHeight());
        depth.max(hlist.getDepth());
        delimiter.typeset(previousNoad, noads, index, list, mathContext,
                logger);
        list.add(hlist);
    }

}
