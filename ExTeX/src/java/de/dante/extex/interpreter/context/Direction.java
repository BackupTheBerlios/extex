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

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * This class provides a limited set of writing directions. The writing
 * directions are defined as constants. The constructor is private to avoid
 * that additional directions are defined.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public final class Direction implements Serializable {

    /**
     * The constant <tt>LR</tt> contains the direction for left-to-right
     * languages.
     */
    public static final Direction LR = new Direction(true);

    /**
     * The constant <tt>RL</tt> contains the direction for right-to-left
     * languages.
     */
    public static final Direction RL = new Direction(false);

    /**
     * The field <tt>lr</tt> contains the indicator which constant this
     * direction corresponds to.
     */
    private boolean lr;

    /**
     * Creates a new object.
     * This constructor is private since only a very limited set of instances
     * of this class is allowed. Those are provided as constants.
     *
     * @param isLR indicator for the direction; <code>true</code> denotes
     *  left to right
     */
    private Direction(final boolean isLR) {

        super();
        this.lr = isLR;
    }

    /**
     * Return the singleton constant object after the serialized instance
     * has been read back in.
     *
     * @return the one and only instance of this object
     *
     * @throws ObjectStreamException never
     */
    protected Object readResolve() throws ObjectStreamException {

        return (lr ? LR : RL);
    }

}