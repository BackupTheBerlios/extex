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

package de.dante.extex.typesetter.type.noad;

import java.util.logging.Logger;

import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a container for Noads and Nodes.
 *
 * @see "TTP [???]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class MathList extends AbstractNoad {

    /**
     * The field <tt>list</tt> is the container for the elements of this node
     * list.
     */
    private NoadList nucleus = new NoadList();

    /**
     * Creates a new object without any items.
     */
    public MathList() {

        super();
    }

    /**
     * Add an arbitrary knot to the list.
     *
     * @param noad the noad to add
     */
    public void add(final Noad noad) {

        nucleus.add(noad);
    }

    /**
     * Test whether the node list is empty.
     *
     * @return <code>true</code>, if the list is empty,
     *  otherwise <code>false</code>.
     */
    public boolean empty() {

        return nucleus.size() == 0;
    }

    /**
     * Getter for a node at a given position.
     *
     * @param index the position
     *
     * @return the node at position <i>index</i> of <code>null</code> if index
     *  is out of bounds
     */
    public Noad get(final int index) {

        return (Noad) nucleus.get(index);
    }

    /**
     * Getter for the last noad previously stored.
     *
     * @return the last noad or <code>null</code> if none is available
     */
    public Noad getLastNoad() {

        return nucleus.getLastNoad();
    }

    /**
     * Remove an element at a given position.
     *
     * @param index the position
     *
     * @return the element previously located at position <i>index</i>
     */
    public Noad remove(final int index) {

        return (Noad) this.nucleus.remove(index);
    }

    /**
     * Return the size of the <code>MathList</code>.
     *
     * @return the size of the <code>MathList</code>
     */
    public int size() {

        return nucleus.size();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        for (int i = 0; i < nucleus.size(); i++) {
            nucleus.get(i).toString(sb);
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#toString(
     *      java.lang.StringBuffer, int)
     */
    public void toString(final StringBuffer sb, final int depth) {

        if (depth < 0) {
            sb.append("{}");
        } else {
            for (int i = 0; i < nucleus.size(); i++) {
                nucleus.get(i).toString(sb, depth - 1);
            }
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public int typeset(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        for (int i = 0; i < nucleus.size();) {
            i = nucleus.get(i).typeset(nucleus, i, list, mathContext, logger);
        }
        return index + 1;
    }

}
