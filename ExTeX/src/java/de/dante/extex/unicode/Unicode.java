/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicode;

import de.dante.util.UnicodeChar;

/**
 * Central utility class for Unicode compatibility in <logo>ExTeX</logo>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public final class Unicode {

    /**
     * The field <tt>BREAK_PERMITTED_HERE</tt> contains the ...
     */
    public static final UnicodeChar BREAK_PERMITTED_HERE = new UnicodeChar(0x82);

    /**
     * The field <tt>NEXT_LINE</tt> contains the next line control caracter.
     */
    public static final UnicodeChar NEXT_LINE = new UnicodeChar(0x85);

    /**
     * The field <tt>NO_BREAK_HERE</tt> contains the ...
     */
    public static final UnicodeChar NO_BREAK_HERE = new UnicodeChar(0x83);

    /**
     * The field <tt>NO_BREAK_SPACE</tt> contains the non-breakable space.
     */
    public static final UnicodeChar NO_BREAK_SPACE = new UnicodeChar(0xa0);

    /**
     * The constant <tt>OFFSET</tt> contains the offset for shifting code points
     * to a private Unicode area.
     */
    public static final int OFFSET = 0xEE00;
    //public static final int OFFSET = 0;

    /**
     * The constant <tt>SHY</tt> contains the soft hyphenation character.
     */
    public static final UnicodeChar SHY = new UnicodeChar(0xad);

    /**
     * The field <tt>SPACE</tt> contains the space control character.
     */
    public static final UnicodeChar SPACE = new UnicodeChar(0x20);

    /**
     * Creates a new object.
     */
    private Unicode() {

        super();
    }

}
