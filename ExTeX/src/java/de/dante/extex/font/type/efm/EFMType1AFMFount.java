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

import de.dante.extex.font.FontFile;
import de.dante.extex.font.PfbFontFile;
import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.resource.ResourceFinder;

/**
 * This class implements a efm-type1-font
 * (create from a AFM-file).
 *
 * TODO at the moment only one font per fontgroup
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class EFMType1AFMFount extends EFMFount implements ModifiableFount {

    /**
     * Creates a new object.
     * @param   doc         the efm-document
     * @param   fontname    the fontname
     * @param   size        the designsize of the font
     * @param   sf          the scale factor in 1000
     * @param   ls          the letterspaced
     * @param   lig         ligature on/off
     * @param   kern        kerning on/off
     * @param   filefinder  the fileFinder-object
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the configsystem.
     */
    public EFMType1AFMFount(final Document doc, final String fontname,
            final Dimen size, final Count sf, final Glue ls, final Boolean lig,
            final Boolean kern, final ResourceFinder filefinder)
            throws FontException, ConfigurationException {

        super(doc, fontname, size, sf, ls, lig, kern, filefinder);
    }

    /**
     * Return String for this class.
     * @return the String for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("<fontname (EFMType1AFM): ").append(getFontName());
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

        return new PfbFontFile(file);
    }
}