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

import de.dante.extex.main.MainUnknownInteractionException;

import java.io.Serializable;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Interaction implements Serializable {
    /** the constant for batch mode */
    public static final Interaction BATCHMODE = new Interaction(0);

    /** the constant for non-stop mode */
    public static final Interaction NONSTOPMODE = new Interaction(1);

    /** the constant for scroll mode*/
    public static final Interaction SCROLLMODE = new Interaction(2);

    /** the constant for error stop mode */
    public static final Interaction ERRORSTOPMODE = new Interaction(3);

    /** the list for mapping integers to modes */
    private static final Interaction[] modeMap = {
                                                     BATCHMODE,
                                                     NONSTOPMODE,
                                                     SCROLLMODE,
                                                     ERRORSTOPMODE
                                                 };

    /** the encapsulated mode */
    private int mode;

    /**
     * Creates a new object.
     * This constructor is private to avoid that other interaction modes than
     * the predefined ones are used.
     */
    private Interaction(int mode) {
        super();
        this.mode = mode;
    }

    /**
     * ...
     *
     * @param mode the integer value for the interaction mode
     *
     * @return the appropriate interaction mode constant
     * 
     * @throws MainUnknownInteractionException
     */
    public static Interaction get(int mode) throws MainUnknownInteractionException {
        if (mode < 0 || mode >= modeMap.length) {
            throw new MainUnknownInteractionException(Integer.toString(mode));
        }

        return modeMap[mode];
    }

    /**
     * ...
     * Allowed values are the numbers 0 to 3 or the
     * symbolic names  batchmode (0), nonstopmode (1), scrollmode (2), and
     * errorstopmode (3). The symbolic names can be abbreviated up to the
     * least unique prefix, i.e. up to one character.
     *
     * @param mode the string representation for the mode
     *
     * @return the appropriate interaction mode constant
     * 
     * @throws MainUnknownInteractionException in case that something is passed
     * in which can not be interpreted as interaction mode
     */
    public static Interaction get(String mode) throws MainUnknownInteractionException {
        if (mode == null || mode.equals("")) {
            throw new MainUnknownInteractionException("");
        } else if ("batchmode".startsWith(mode) || mode.equals("0")) {
            return BATCHMODE;
        } else if ("nonstopmode".startsWith(mode) || mode.equals("1")) {
            return NONSTOPMODE;
        } else if ("scrollmode".startsWith(mode) || mode.equals("2")) {
            return SCROLLMODE;
        } else if ("errorstopmode".startsWith(mode) ||
                       mode.equals("3")) {
            return ERRORSTOPMODE;
        }

        throw new MainUnknownInteractionException(mode);
    }
}
