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
 * @version $Revision: 1.2 $
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
     */
    public Flags() {
        super();
    }

    public void clear() { value = NONE;
    }

    public void setGlobal() { value |= GLOBAL; }
    public void setLong() { value |= LONG; }
    public void setOuter() { value |= OUTER; }
    public void setImmediate() { value |= IMMEDIATE; }
    public void setExpanded() { value |= EXPANDED; }

    public boolean isGlobal() { return (value & GLOBAL)!=0; }
    public boolean isLong() { return (value & LONG)!=0; }
    public boolean isOuter() { return (value & OUTER)!=0; }
    public boolean isImmediate() { return (value & IMMEDIATE)!=0; }
    public boolean isExpanded() { return (value & EXPANDED)!=0; }
}
