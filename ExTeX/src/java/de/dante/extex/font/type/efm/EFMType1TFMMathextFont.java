/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import org.jdom.Document;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.resource.ResourceFinder;

/**
 * This class implements a efm-type1-font (math-ext)
 * (create from a TFM-file).
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class EFMType1TFMMathextFont extends EFMType1TFMNOFont implements Font {

    /**
     * names for the parameter
     */
    public static final String[] PARAM = {"SLANT", "SPACE", "STRETCH",
            "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE", "DEFAULTRULETHICKNESS",
            "BIGOPSPACING1", "BIGOPSPACING2", "BIGOPSPACING3", "BIGOPSPACING4",
            "BIGOPSPACING5"};

    /**
     * Creates a new object.
     * @param   doc         the efm-document
     * @param   fontname    the fontname
     * @param   size        the emsize of the font
     * @param   ls          the letterspaced
     * @param   lig         ligature on/off
     * @param   kern        kerning on/off
     * @param   filefinder  the ResourceFinder-object
     * @throws GeneralException ...
     * @throws ConfigurationException ...
     */
    public EFMType1TFMMathextFont(final Document doc, final String fontname,
            final Dimen size, final Glue ls, final Boolean lig,
            final Boolean kern, final ResourceFinder filefinder)
            throws GeneralException, ConfigurationException {

        super(doc, fontname, size, ls, lig, kern, filefinder);
    }

    /**
     * Return String for this class.
     * @return the String for this class
     */
    public String toString() {

        return "<fontname (EFMType1TFMMathext): " + getFontName()
                + " with size " + getEmsize().toString() + " unitsperem = "
                + getUnitsperem() + " ex = " + getEx() + " em = "
                + getEm().toString() + " (with " + getEmpr() + "%)"
                + " number of glyphs = " + getGylphMapSize() + " >";
    }

}