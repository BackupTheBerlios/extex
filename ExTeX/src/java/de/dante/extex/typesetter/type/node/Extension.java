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

package de.dante.extex.typesetter.type.node;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;

/**
 * This interface describes the capabilities for an extension object to be
 * inserted into an extension node.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface Extension {

    /**
     * Getter for the width of the node.
     *
     * @return the width
     */
    Dimen getWidth();

    /**
     * Getter for the height of the node.
     *
     * @return the height
     */
    Dimen getHeight();

    /**
     * Getter for the depth of the node.
     *
     * @return the depth
     */
    Dimen getDepth();

    /**
     * Setter for the width of the node.
     *
     * @param width the new width
     */
    void setWidth(FixedDimen width);

    /**
     * Setter for the height of the node.
     *
     * @param height the new height
     */
    void setHeight(FixedDimen height);

    /**
     * Setter for the depth of the node.
     *
     * @param depth the nde depth
     */
    void setDepth(FixedDimen depth);

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a short form only as it is used in error
     * messages to the user.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     */
    void toText(StringBuffer sb, String prefix);

    /**
     * This method puts the printable representation into the string buffer.
     * This is meant to produce a exhaustive form as it is used in tracing
     * output to the log file.
     *
     * @param sb the output string buffer
     * @param prefix the prefix string inserted at the beginning of each line
     */
    void toString(StringBuffer sb, String prefix);

}
