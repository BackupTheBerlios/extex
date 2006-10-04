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

package de.dante.extex.interpreter.context;

import de.dante.extex.font.FontFactory;
import de.dante.extex.interpreter.type.font.Font;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface ContextFont {

    /**
     * Getter for a current font register.
     *
     * @param name the name or the number of the register
     *
     * @return the named font register or <code>null</code> if none is set
     *
     * @see #setFont(String, Font, boolean)
     */
    Font getFont(String name);

    /**
     * Getter for the font factory.
     *
     * @return the fontFactory.
     *
     * @see #setFontFactory(FontFactory)
     */
    FontFactory getFontFactory();

    /**
     * Setter for font registers.
     *
     * @param name the name or the number of the register
     * @param font the new Font value
     * @param global the indicator for the scope; <code>true</code> means all
     *   groups; otherwise the current group is affected only
     *
     * @see #getFont(String)
     */
    void setFont(String name, Font font, boolean global);

    /**
     * Setter for the font factory.
     *
     * @param fontFactory the font factory to set.
     *
     * @see #getFontFactory()
     */
    void setFontFactory(FontFactory fontFactory);

}
