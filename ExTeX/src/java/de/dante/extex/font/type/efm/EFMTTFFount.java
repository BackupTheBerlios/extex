/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.efm;

import java.io.File;

import org.jdom.Document;

import de.dante.extex.font.FontFactory;
import de.dante.extex.font.FontFile;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.TtfFontFile;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.resource.ResourceFinder;

/**
 * This class implements a efm-TTF-font
 *
 * TODO at the moment only one font per fontgroup
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class EFMTTFFount extends EFMFount implements ModifiableFount {

    /**
     * Creates a new object.
     * @param   doc         the efm-document
     * @param   key         the fount key
     * @param   filefinder  the fileFinder-object
     * @param   ff          the font factory
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config system.
     */
    public EFMTTFFount(final Document doc, final FountKey key,
            final ResourceFinder filefinder, final FontFactory ff)
            throws FontException, ConfigurationException {

        super(doc, key, filefinder, ff);
    }

    /**
     * Return String for this class.
     * @return the String for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("<fontname (EFMTTF): ").append(getFontName());
        buf.append(" with size ").append(getEmsize().toString());
        buf.append(" unitsperem = ").append(getUnitsperem());
        buf.append(" ex = ").append(getEx());
        buf.append(" em = ").append(getEm().toString());
        buf.append(" (with ").append(getEmpr()).append("%)");
        buf.append(" letterspaced ");
        buf.append(getLetterSpacing().toString());
        buf.append(" : number of glyphs = ").append(getGylphMapSize());
        buf.append(" >");
        return buf.toString();
    }

    /**
     * @see de.dante.extex.font.type.efm.EFMFount#getFontFile(java.io.File)
     */
    protected FontFile getFontFile(final File file) {

        return new TtfFontFile(file);
    }
}