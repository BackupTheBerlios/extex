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
package de.dante.extex.typesetter;

import de.dante.extex.i18n.Messages;

/**
 * This class provides type-safe constants for the modes of a typesetter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Mode {
    /** constant for the horizontal mode of the typesetter */
    public static final Mode HORIZONTAL = new Mode("Mode.HorizontalMode");

    /** constant for the vertical mode of the typesetter */
    public static final Mode VERTICAL = new Mode("Mode.VerticalMode");

    /** constant for the restricted horizontal mode of the typesetter */
    public static final Mode RESTRICTED_HORIZONTAL = new Mode("Mode.HorizontalMode");

    /** constant for the inner vertical mode of the typesetter */
    public static final Mode INNER_VERTICAL = new Mode("Mode.VerticalMode");

    /** constant for the math mode of the typesetter */
    public static final Mode MATH = new Mode("Mode.MathMode");

    /** constant for the display math mode of the typesetter */
    public static final Mode DISPLAYMATH = new Mode("Mode.DisplayMathMode");

    /** this attribute contains the key for the message (cf. i18n)
     *  to be used as a short description of the mode.
     */
    private String tag;

    /**
     * Creates a new object.
     * This constructor is private since only a limited number of modes should 
     * be usable. Those are provided by the static constants in this class.
     *
     * @param tag the tag of the mode
     */
    private Mode(String tag) {
        super();
        this.tag = tag;
    }

    /**
     * Getter for the string representation.
     *
     * @return the string representation
     */
    public String toString() {
        return Messages.format(tag);
    }
}
