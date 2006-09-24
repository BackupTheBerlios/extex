/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.tc;

import java.io.Serializable;

import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.language.Language;

/**
 * The typesetting context is a container for attributes describing the
 * appearance of glyphs or other nodes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface TypesettingContext extends Serializable {

    /**
     * Getter for the color.
     *
     * @return the current color
     */
    Color getColor();

    /**
     * Getter for the writing direction.
     *
     * @return the current direction
     */
    Direction getDirection();

    /**
     * Getter for the font component.
     *
     * @return the font
     */
    Font getFont();

    /**
     * Getter for the hyphenation table.
     *
     * @return the hyphenation table
     */
    Language getLanguage();

}
