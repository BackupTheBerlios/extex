/*
 * Copyright (C) 2003  Gerd Neugebauer
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
 * @version $Revision: 1.4 $
 */
public class Flags {
    /** Symbolc constant for the empty prefix */
    private static final int NONE = 0x00;

    /** Symbolc constant for the prefix \global */
    private static final int GLOBAL = 0x01;

    /** Symbolc constant for the prefix \long */
    private static final int LONG = 0x02;

    /** Symbolc constant for the prefix \outer */
    private static final int OUTER = 0x04;

    /** Symbolc constant for the pseudo prefix for \xdef an d \edef */
    private static final int EXPANDED = 0x08;

    /** Symbolc constant for the prefix \immediate */
    private static final int IMMEDIATE = 0x10;


    /** the encapsulated value */
    private int value = NONE;

    /**
     * Creates a new object.
     * Initially No flags are set.
     */
    public Flags() {
        super();
    }

    /**
     * This method clears all flags.
     */
    public void clear() {
        value = NONE;
    }

    /**
     * Setter for the global flag.
     */
    public void setGlobal() {
        value |= GLOBAL;
    }
    
    /**
     * Setter for the long flag.
     */
    public void setLong() {
        value |= LONG;
    }
    
    /**
     * Setter for the outer flag.
     */
    public void setOuter() {
        value |= OUTER;
    }
    
    /**
     * Setter for the immediate flag.
     */
    public void setImmediate() {
        value |= IMMEDIATE;
    }
    
    /**
     * Setter for the expanded flag.
     */
    public void setExpanded() {
        value |= EXPANDED;
    }

    /**
     * Getter for the global flag.
     *
     * @return the current value of the global flag
     */
    public boolean isGlobal() {
        return (value & GLOBAL) != 0;}
    
    /**
     * Getter for the long flag.
     *
     * @return the current value of the long flag
     */
    public boolean isLong() {
        return (value & LONG) != 0;
    }

    /**
     * Getter for the outer flag.
     *
     * @return the current value of the outer flag
     */
    public boolean isOuter() {
        return (value & OUTER) != 0;
    }

    /**
     * Getter for the immediate flag.
     *
     * @return the current value of the immediate flag
     */
    public boolean isImmediate() {
        return (value & IMMEDIATE) != 0;
    }

    /**
     * Getter for the expanded flag.
     *
     * @return the current value of the expanded flag
     */
    public boolean isExpanded() {
        return (value & EXPANDED) != 0;
    }

}
