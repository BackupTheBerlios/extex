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
package de.dante.extex.interpreter.context;

import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.interpreter.type.Font;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public interface TypesettingContext {

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
     * @param direction ...
     */
    void setDirection(Direction direction);

    /**
     * Getter for the writing direction.
     *
     * @return ...
     */
    Direction getDirection();

    /**
     * ...
     *
     * @param language ...
     */
    void setLanguage(HyphenationTable language);

    /**
     * ...
     *
     * @return ...
     */
    HyphenationTable getLanguage();

    /**
     * Setter for the color.
     *
     * @param color ...
     */
    void setColor(Color color);

    /**
     * Getter for the color.
     *
     * @return ...
     */
    Color getColor();

    /**
     * Setter for the angle.
     *
     * @param angle ...
     */
    void setAngle(int angle);

    /**
     * Getter for the angle.
     *
     * @return ...
     */
    int getAngle();

}
