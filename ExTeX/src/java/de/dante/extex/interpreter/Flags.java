/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
 * @version $Revision: 1.7 $
 */
public class Flags {

    /**
     * The field <tt>globalP</tt> contains the ...
     */
    private boolean globalP = false;

    /**
     * The field <tt>longP</tt> contains the ...
     */
    private boolean longP = false;

    /**
     * The field <tt>outerP</tt> contains the ...
     */
    private boolean outerP = false;

    /**
     * The field <tt>expandedP</tt> contains the ...
     */
    private boolean expandedP = false;

    /**
     * The field <tt>immediateP</tt> contains the ...
     */
    private boolean immediateP = false;

    /**
     * Creates a new object.
     * Initially no flags are set.
     */
    public Flags() {
        super();
    }

    /**
     * This method clears all flags.
     */
    public void clear() {
        globalP = false;
        longP = false;
        outerP = false;
        expandedP = false;
        immediateP = false;
    }

    /**
     * Setter for the global flag.
     */
    public void setGlobal() {
        globalP = true;
    }

    /**
     * Setter for the global flag.
     *
     * @param value the new value for the global flag
     */
    public void setGlobal(final boolean value) {
        globalP = value;
    }

    /**
     * Setter for the long flag.
     */
    public void setLong() {
        longP = true;
    }

    /**
     * Setter for the outer flag.
     */
    public void setOuter() {
        outerP = true;
    }

    /**
     * Setter for the immediate flag.
     */
    public void setImmediate() {
        immediateP = true;
    }

    /**
     * Setter for the expanded flag.
     */
    public void setExpanded() {
        expandedP = true;
    }

    /**
     * Getter for the global flag.
     *
     * @return the current value of the global flag
     */
    public boolean isGlobal() {
        return globalP;
    }

    /**
     * Getter for the long flag.
     *
     * @return the current value of the long flag
     */
    public boolean isLong() {
        return longP;
    }

    /**
     * Getter for the outer flag.
     *
     * @return the current value of the outer flag
     */
    public boolean isOuter() {
        return outerP;
    }

    /**
     * Getter for the immediate flag.
     *
     * @return the current value of the immediate flag
     */
    public boolean isImmediate() {
        return immediateP;
    }

    /**
     * Getter for the expanded flag.
     *
     * @return the current value of the expanded flag
     */
    public boolean isExpanded() {
        return expandedP;
    }

}
