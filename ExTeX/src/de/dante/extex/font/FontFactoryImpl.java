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

package de.dante.extex.font;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;
import de.dante.util.file.FileFinder;

/**
 * Factory to load a font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
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
     * the logger
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param fileFinder the filefinder
     */
    public FontFactoryImpl(final FileFinder fileFinder) {

        super();
        finder = fileFinder;
        logger = Logger.getLogger(getClass().getName());
    }

    /**
     * @see de.dante.extex.font.FontFactory#getInstance(
     *      java.lang.String, Dimen)
     */
    public Font getInstance(final String name, final Dimen size)
            throws GeneralException, ConfigurationException {

        return getInstance(name, size, new Glue(0), true);
    }

    /**
     * @see de.dante.extex.font.FontFactory#getInstance(
     *      java.lang.String, Dimen, Glue, boolean)
     */
    public Font getInstance(final String name, final Dimen size,
            final Glue letterspaced, final boolean ligatures)
            throws GeneralException, ConfigurationException {

        String filename;
        if (name != null) {
            filename = name.trim();
        } else {
            filename = "null";
        }

        FontKey key = new FontKey(filename, size, letterspaced, ligatures);
        Font font = (Font) (fontmap.get(key));
        if (font == null) {

            Document doc = loadEFMDocument(filename);
            Type type = getFontType(doc);

            if (type == TYPE1) {
                font = new EFMType1AFMFont(doc, filename, size, letterspaced,
                        ligatures, finder);
            } else if (type == TTF) {
                font = new EFMTTFFont(doc, filename, size, letterspaced,
                        ligatures, finder);
            } else if (type == TFMNORMAL) {
                font = new EFMType1TFMNOFont(doc, filename, size, letterspaced,
                        ligatures, finder);
            } else if (type == TFMMATHEXT) {
                font = new EFMType1TFMMathextFont(doc, filename, size,
                        letterspaced, ligatures, finder);
            } else if (type == TFMMATHSYML) {
                font = new EFMType1TFMMathsymlFont(doc, filename, size,
                        letterspaced, ligatures, finder);
            } else {
                // UNKNOWN
                throw new ConfigurationIOException("unknown font-type");
                // TODO i18n
            }
            // ---------------
            // logger.info(font.toString());
            // -----------------------------
            // TODO delete after test
        }
        return font;
    }

    /**
     * Return the type of a font
     * @param doc   the efm-document
     * @return the type of the font
     */
    private Type getFontType(final Document doc) {

        Element root = doc.getRootElement();
        Element font = EFMFont.scanForElement(root, "font");
        Attribute typeattr = font.getAttribute("type");
        if ("type1".equals(typeattr.getValue().toLowerCase())) {
            return TYPE1;
        } else if ("ttf".equals(typeattr.getValue().toLowerCase())) {
            return TTF;
        } else if ("tfm-mathsyml".equals(typeattr.getValue().toLowerCase())) {
            return TFMMATHSYML;
        } else if ("tfm-mathext".equals(typeattr.getValue().toLowerCase())) {
            return TFMMATHEXT;
        } else if ("tfm-normal".equals(typeattr.getValue().toLowerCase())) {
            return TFMNORMAL;
        }
        return UNKNOWN;
    }

    /**
     * type1-font
     */
    private static final Type TYPE1 = new Type();

    /**
     * ttf-font
     */
    private static final Type TTF = new Type();

    /**
     * tfm-normal
     */
    private static final Type TFMNORMAL = new Type();

    /**
     * tfm-mathext
     */
    private static final Type TFMMATHEXT = new Type();

    /**
     * tfm-mathsyml
     */
    private static final Type TFMMATHSYML = new Type();

    /**
     * unknown
     */
    private static final Type UNKNOWN = new Type();

    /**
     * Type of a font
     */
    private static class Type {

        /**
         * Create a new object.
         */
        public Type() {

            super();
        }
    }

    /**
     * EFM-Extension
     */
    private static final String EFMEXTENSION = "efm";

    /**
     * Load the efm-Font
     * @param name  the name of the efm-file
     * @return  the efm as Document or <code>null</code>, if not found
     * @throws ConfigurationException ...
     */
    private Document loadEFMDocument(final String name)
            throws ConfigurationException {

        Document doc = null;
        if (name != null) {
            File fontfile = finder.findFile(name, EFMEXTENSION);
            if (fontfile != null && fontfile.exists()) {
                try {
                    // create a document with SAXBuilder (without validate)
                    SAXBuilder builder = new SAXBuilder(false);
                    doc = builder.build(fontfile);
                } catch (Exception e) {
                    throw new ConfigurationIOException(e.getMessage());
                }
            }
        }
        if (doc == null) {
            throw new ConfigurationIOException("efm-file not found");
            //TODO i18n
        }
        return doc;
    }

    /**
     * Font-key-class for the <code>HashMap</code>.
     */
    private static class FontKey {

        /**
         * The name of the font
         */
        private final String name;

        /**
         * The size of the font
         */
        private final Dimen size;

        /**
         * The glue for letterspace
         */
        private Glue letterspaced;

        /**
         * ligature on/off
         */
        private boolean ligatures;

        /**
         * Create a new object.
         * @param n     the name
         * @param s     the size
         * @param ls    the letterspace
         * @param lig   the ligature
         */
        public FontKey(final String n, final Dimen s, final Glue ls,
                final boolean lig) {

            name = n;
            size = s;
            letterspaced = ls;
            ligatures = lig;
        }
    }
}