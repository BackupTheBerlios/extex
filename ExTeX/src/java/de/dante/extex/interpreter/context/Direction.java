/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.interpreter.context;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public final class Direction {
    /**
     * The constant <tt>LR</tt> contains the direction for left-to-right
     * languages.
     */
    public static final Direction LR = new Direction();

    /**
     * The field <tt>RL</tt> contains the direction for right-to-left
     * languages.
     */
    public static final Direction RL = new Direction();

    /**
     * Creates a new object.
     * This constructor is private since only a very limited of instances
     * of this class is allowed.
     */
    private Direction() {
        super();
    }
}
