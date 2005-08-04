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

package de.dante.extex.interpreter.context;

import java.io.Serializable;

import de.dante.extex.color.ColorVisitor;
import de.dante.util.GeneralException;

/**
 * This interface declares some methods to access the color with an alpha
 * channel.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public interface Color extends Serializable {

    /**
     * The constant <tt>MAX_VALUE</tt> contains the maximal values for all
     * channels.
     */
    int MAX_VALUE = 0xffff;

    /**
     * Getter for the alpha channel.
     * The range of the value is 0x00 to 0xffff.
     *
     * @return the alpha channel
     */
    int getAlpha();

    /**
     * Check that the current color is identical to another one.
     *
     * @param other the other object to compare to
     *
     * @return <code>true</code> iff the color system is identical and the
     *  colors are the same
     */
    boolean equals(Object other);

    /**
     * This method provides an entry point for the visitor pattern.
     *
     * @param visitor the visitor to apply
     * @param value the argument for the visitor
     *
     * @return the result of the method invocation of the visitor
     *
     * @throws GeneralException in case of an error
     */
    Object visit(ColorVisitor visitor, Object value) throws GeneralException;

}
