/*
 * Copyright (C) 2003-2004 The ExTeX Group
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

package de.dante.extex.font;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Font;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.FileFinder;

/**
 * Factory to load a font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class FontFactoryImpl implements FontFactory {

    /**
     * Fontmap
     */
    private Map fontmap = new HashMap();

    /**
     * the file finder
     */
    private FileFinder finder;

    /**
     * Creates a new object.
     *
     * @param fileFinder ...
     */
    public FontFactoryImpl(final FileFinder fileFinder) {
        super();
        finder = fileFinder;
    }

    /**
     * @see de.dante.extex.font.FontFactory#getInstance(java.lang.String)
     */
    // TODO the name is not only the key for the font!
    public Font getInstance(final String name, final Dimen size)
            throws GeneralException, ConfigurationException {

        Font font = (Font) (fontmap.get(name));
        if (font == null) {
            font = new EFMFont(name, size, finder);
            // System.err.println(font);
        }
        return font;
    }
}
