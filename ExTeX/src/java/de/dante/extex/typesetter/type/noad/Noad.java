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

import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.Knot;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * The interface Noad is a type of data structure which represents mathematical
 * constructions.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public interface Noad extends Knot {

    /**
     * Getter for the subscript.
     *
     * @return the subscript.
     */
    Noad getSubscript();

    /**
     * Getter for the superscript.
     *
     * @return the superscript.
     */
    Noad getSuperscript();

    /**
     * Setter for the subscript.
     *
     * @param subscript the subscript to set.
     */
    void setSubscript(final Noad subscript);

    /**
     * Setter for the superscript.
     *
     * @param superscript the superscript to set.
     */
    void setSuperscript(final Noad superscript);

    /**
     * Produce a printable representation of the noad in a StringBuffer.
     *
     * @param sb the string buffer
     */
    void toString(StringBuffer sb);

    /**
     * Produce a printable representation to a certain depth of the noad.
     *
     * @param sb the string buffer
     * @param depth the depth to which the full information should be given
     */
    void toString(StringBuffer sb, int depth);

    /**
     * Translate a Noad into a NodeList.
     *
     * @param noads the list of noads currently processed
     * @param index the index of the current node in the list
     * @param list the list to add the nodes to. This list contains the Nodes
     *  previously typeset. Thus it can be used to look back
     * @param mathContext the context to consider
     * @param context the interpreter context
     * @param logger the logger for debugging and tracing information
     *
     * @return the index of the next noad to consider
     *
     * @throws TypesetterException in case of a problem
     * @throws ConfigurationException in case of a configuration problem
     */
    int typeset(NoadList noads, int index, NodeList list,
            MathContext mathContext, TypesetterOptions context, Logger logger)
            throws TypesetterException,
                ConfigurationException;

}
