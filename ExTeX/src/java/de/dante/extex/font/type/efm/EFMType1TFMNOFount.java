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
 * This class implements a efm-type1-font (normal)
 * (create from a TFM-file).
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class EFMType1TFMNOFount extends EFMFount implements ModifiableFount {

    /**
     * names for the parameter
     */
    public static final String[] PARAM = {"SLANT", "SPACE", "STRETCH",
            "SHRINK", "XHEIGHT", "QUAD", "EXTRASPACE"};

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
     * @throws ConfigurationException from the config system.
     */
    public EFMType1TFMNOFount(final Document doc, final String fontname,
            final Dimen size, final Count sf, final Glue ls, final Boolean lig,
            final Boolean kern, final ResourceFinder filefinder)
            throws FontException, ConfigurationException {

        super(doc, fontname, size, sf, ls, lig, kern, filefinder);
    }

    /**
     * Return the <code>Dimen</code>-value for a key-entry.
     * If no key exists, ZERO-<code>Dimen</code> is returned.
     *
     * @see de.dante.extex.interpreter.type.font.Font#getFontDimen(String)
     */
    public Dimen getFontDimen(final String key) {

        return super.getFontDimen(getKey(key));
    }

    /**
     * Set the <code>Dimen</code>-value for a key-entry.
     *
     * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(String, Dimen)
     */
    public void setFontDimen(final String key, final Dimen value) {

        super.setFontDimen(getKey(key), value);
    }

    /**
     * Return the key for a fontparameter.
     * If the key is '#nr' and the number is in the range of
     * 0 and PARAM.length, the parametername is used instead.
     * @param key   the key
     * @return  the fontkey
     */
    private String getKey(final String key) {

        String paramkey = key;
        if (key.startsWith("#")) {
            try {
                paramkey = PARAM[Integer.parseInt(key.substring(1))];
            } catch (Exception e) {
                // no special number!
                paramkey = key;
            }
        }
        return paramkey;
    }

    /**
     * Return String for this class.
     * @return the String for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("<fontname (EFMType1TFMNO): ").append(getFontName());
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