/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public interface Flags {

    /**
     * The constant <tt>NONE</tt> contains a instance where no flags are set.
     * Beware of changing this instance!
     */
    public static final Flags NONE = new FlagsImpl();

    /**
     * This method clears all flags.
     */
    void clear();

    /**
     * Getter for the expanded flag.
     *
     * @return the current value of the expanded flag
     */
    boolean isExpanded();

    /**
     * Getter for the global flag.
     *
     * @return the current value of the global flag
     */
    boolean isGlobal();

    /**
     * Getter for the immediate flag.
     *
     * @return the current value of the immediate flag
     */
    boolean isImmediate();

    /**
     * Getter for the long flag.
     *
     * @return the current value of the long flag
     */
    boolean isLong();

    /**
     * Getter for the outer flag.
     *
     * @return the current value of the outer flag
     */
    boolean isOuter();

    /**
     * Setter for the expanded flag.
     */
    void setExpanded();

    /**
     * Setter for the global flag.
     */
    void setGlobal();

    /**
     * Setter for the global flag.
     *
     * @param value the new value for the global flag
     */
    void setGlobal(final boolean value);

    /**
     * Setter for the immediate flag.
     */
    void setImmediate();

    /**
     * Setter for the long flag.
     */
    void setLong();

    /**
     * Setter for the outer flag.
     */
    void setOuter();

}
