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

package de.dante.extex.unicodeFont;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.key.FontKey;
import de.dante.extex.unicodeFont.type.TexFont;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Interface for the Factory for the font system.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */

public interface FontFactory {

    /**
     * Returns a new Instance of the font.
     *
     * @param key   The font key.
     * @return Returns a new Instance of the font.
     * @throws ConfigurationException From the configuration system.
     * @throws FontException if an font-error occurred.
     */
    TexFont newInstance(final FontKey key) throws ConfigurationException,
            FontException;

}
