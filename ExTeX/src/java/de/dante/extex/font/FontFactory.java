/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.font;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.afm.AfmFont;
import de.dante.extex.font.type.efm.EfmReader;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.extex.font.type.vf.VFFont;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This interface describes a factory to manage fonts.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.31 $
 */
public interface FontFactory {

    /**
     * AFM-Extension
     */
    String AFM_EXTENSION = "afm";

    /**
     * EFM-Extension
     */
    String EFM_EXTENSION = "efm";

    /**
     * OTF-Extension
     */
    String OTF_EXTENSION = "otf";

    /**
     * TFM-Extension
     */
    String TFM_EXTENSION = "tfm";

    /**
     * TTF-Extension
     */
    String TTF_EXTENSION = "ttf";

    /**
     * VF-Extension
     */
    String VF_EXTENSION = "vf";

    /**
     * Return a new instance.
     *
     * If the name is empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param key the fount key
     *
     * @return Returns the new font instance.
     *
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font error occurred.
     */
    Font getInstance(FountKey key) throws ConfigurationException, FontException;

    /**
     * Return a new instance.
     *
     * If the name is empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param tfm the tfm font
     * @param key the fount key
     *
     * @return the new font instance.
     *
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font error occurred.
     */
    Font getInstance(TFMFont tfm, FountKey key)
            throws ConfigurationException,
                FontException;

    /**
     * Return a new instance.
     *
     * If the name is empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param vf the vf-font
     * @param key the fount key
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font error occurred.
     */
    Font getInstance(VFFont vf, FountKey key)
            throws ConfigurationException,
                FontException;

    /**
     * Returns the psfontmapreader.
     *
     * @return Returns the psfm
     *
     * @throws FontException if a font error occurs.
     * @throws ConfigurationException from the config-system.
     */
    PSFontsMapReader getPsfm() throws FontException, ConfigurationException;

    /**
     * Read an afm font.
     *
     * @param name the name of the afm file
     *
     * @return the afm font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of an font error
     */
    AfmFont readAFMFont(String name)
            throws ConfigurationException,
                FontException;

    /**
     * Read a tfm font.
     *
     * @param name the name of the tfm file
     *
     * @return the tfm font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of an font error
     */
    EfmReader readEFMFont(String name)
            throws ConfigurationException,
                FontException;

    /**
     * Read a tfm font.
     *
     * @param name the name of the tfm file
     *
     * @return the tfm font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of an font error
     */
    TFMFont readTFMFont(String name)
            throws ConfigurationException,
                FontException;

    /**
     * Read a vf font.
     *
     * @param name  the name of the vf file
     *
     * @return the vf font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of an font error
     */
    VFFont readVFFont(String name) throws ConfigurationException, FontException;

}
