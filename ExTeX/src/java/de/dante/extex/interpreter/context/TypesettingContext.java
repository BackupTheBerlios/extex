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

import java.io.Serializable;

import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.language.Language;

/**
 * The typesetting context is a container for attributes describing the
 * appearance of glyphs or other nodes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.16 $
 */
public interface TypesettingContext extends Serializable {

    /**
     * Setter for all components.
     *
     * @param context the context to clone
     */
    void set(TypesettingContext context);

    /**
     * Setter for the font component.
     *
     * @param font the font to store
     */
    void setFont(Font font);

    /**
     * Getter for the font component.
     *
     * @return the font
     */
    Font getFont();

    /**
     * Setter for the writing direction.
     *
     * @param direction the new direction
     */
    void setDirection(Direction direction);

    /**
     * Getter for the writing direction.
     *
     * @return the current direction
     */
    Direction getDirection();

    /**
     * Setter for the hyphenation table.
     *
     * @param language the hyphenation table
     */
    void setLanguage(Language language);

    /**
     * Getter for the hyphenation table.
     *
     * @return the hyphenation table
     */
    Language getLanguage();

    /**
     * Setter for the color.
     *
     * @param color the new color
     */
    void setColor(Color color);

    /**
     * Getter for the color.
     *
     * @return the current color
     */
    Color getColor();

}
