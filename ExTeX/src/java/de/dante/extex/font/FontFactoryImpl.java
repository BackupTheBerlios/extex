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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.afm.AFMReader;
import de.dante.extex.font.type.efm.EFMFount;
import de.dante.extex.font.type.tfm.TFMReader;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.FontImpl;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.resource.ResourceFinder;

/**
 * Factory to load a font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.28 $
 */
public class FontFactoryImpl implements FontFactory {

    /**
     * The field <tt>TAG_TYPE</tt> contains the name of the tag in the
     * configuration file which contains the specification for the font-type.
     */
    private static final String TAG_TYPE = "Type";

    /**
     * The field <tt>ATTR_DEFAULT</tt> contains the name of the attribut in the
     * configuration file which contains the specification default-type.
     */
    private static final String ATTR_DEFAULT = "default";

    /**
     * Fontmap
     */
    private Map fountmap = new HashMap();

    /**
     * the file finder
     */
    private ResourceFinder finder;

    /**
     * Configuration for <tt>Type</tt>
     */
    private Configuration cfgType;

    /**
     * Creates a new object.
     *
     * @param config    the config-object
     * @param resourceFinder the filefinder
     * @throws ConfigurationException in case of an error
     */
    public FontFactoryImpl(final Configuration config,
            final ResourceFinder resourceFinder) throws ConfigurationException {

        super();
        finder = resourceFinder;

        cfgType = config.getConfiguration(TAG_TYPE);

    }

    /**
     * @see de.dante.extex.font.FontFactory#getInstance(java.lang.String)
     */
    public Font getInstance(final String name) throws GeneralException,
            ConfigurationException {

        return getInstance(name, new Dimen(0), new Glue(0), true, true);
    }

    /**
     * @see de.dante.extex.font.FontFactory#getInstance(
     *      java.lang.String, Dimen)
     */
    public Font getInstance(final String name, final Dimen size)
            throws GeneralException, ConfigurationException {

        return getInstance(name, size, new Glue(0), true, true);
    }

    /**
     * @see de.dante.extex.font.FontFactory#getInstance(
     *      java.lang.String, Dimen, Glue, boolean, boolean)
     */
    public Font getInstance(final String name, final Dimen size,
            final Glue letterspaced, final boolean ligatures,
            final boolean kerning) throws GeneralException,
            ConfigurationException {

        String filename;
        if (name != null) {
            filename = name.trim();
        } else {
            filename = "null";
        }

        FountKey key = new FountKey(filename, size, letterspaced, ligatures,
                kerning);
        ModifiableFount fount = (ModifiableFount) (fountmap.get(key));
        if (fount == null) {

            Document doc = loadEFMDocument(filename);

            String classname = getFontClass(doc);

            try {
                fount = (ModifiableFount) (Class.forName(classname)
                        .getConstructor(
                                new Class[]{Document.class, String.class,
                                        Dimen.class, Glue.class, Boolean.class,
                                        Boolean.class, ResourceFinder.class})
                        .newInstance(new Object[]{doc, filename, size,
                                letterspaced, new Boolean(ligatures),
                                new Boolean(kerning), finder}));
            } catch (IllegalArgumentException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (SecurityException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InstantiationException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (IllegalAccessException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InvocationTargetException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (NoSuchMethodException e) {
                throw new ConfigurationNoSuchMethodException(e);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationClassNotFoundException(classname);
            }

        }

        return new FontImpl(fount);
    }

    /**
     * Return the font-class for a font
     * @param doc   the efm-document
     * @return the font-class for a font
     * @throws ConfigurationException in case off an error
     */
    private String getFontClass(final Document doc)
            throws ConfigurationException {

        Element root = doc.getRootElement();
        Element font = EFMFount.scanForElement(root, "font");
        Attribute typeattr = font.getAttribute("type");

        String classname = null;

        if ("type1".equals(typeattr.getValue().toLowerCase())) {
            Configuration cfg = cfgType.getConfiguration("type1");
            classname = cfg.getAttribute("class");
        } else if ("ttf".equals(typeattr.getValue().toLowerCase())) {
            Configuration cfg = cfgType.getConfiguration("ttf");
            classname = cfg.getAttribute("class");
        } else if ("tfm-mathsyml".equals(typeattr.getValue().toLowerCase())) {
            Configuration cfg = cfgType.getConfiguration("tfm-mathsyml");
            classname = cfg.getAttribute("class");
        } else if ("tfm-mathext".equals(typeattr.getValue().toLowerCase())) {
            Configuration cfg = cfgType.getConfiguration("tfm-mathext");
            classname = cfg.getAttribute("class");
        } else if ("tfm-normal".equals(typeattr.getValue().toLowerCase())) {
            Configuration cfg = cfgType.getConfiguration("tfm-normal");
            classname = cfg.getAttribute("class");
        }

        if (classname == null || classname.trim().length() == 0) {
            Configuration cfg = cfgType.getConfiguration(cfgType
                    .getAttribute(ATTR_DEFAULT));
            classname = cfg.getAttribute("class");
            if (classname == null || classname.trim().length() == 0) {
                throw new ConfigurationMissingAttributeException("default",
                        cfgType);
            }
        }
        return classname;
    }

    /**
     * EFM-Extension
     */
    private static final String EFMEXTENSION = "efm";

    /**
     * TFM-Extension
     */
    private static final String TFMEXTENSION = "tfm";

    /**
     * AFM-Extension
     */
    private static final String AFMEXTENSION = "afm";

    /**
     * Load the efm-Font
     * @param name  the name of the efm-file
     * @return  the efm as Document or <code>null</code>, if not found
     * @throws ConfigurationException in case of an error in the configuration
     */
    private Document loadEFMDocument(final String name)
            throws ConfigurationException {

        Document doc = null;
        if (name != null) {

            // efm
            InputStream fontfile = finder.findResource(name, EFMEXTENSION);

            if (fontfile != null) {
                //System.err.println("\n FONT " + name + "  EFM");
                try {
                    // create a document with SAXBuilder (without validate)
                    SAXBuilder builder = new SAXBuilder(false);
                    doc = builder.build(fontfile);
                } catch (Exception e) {
                    throw new ConfigurationIOException(e.getMessage());
                }
            } else {

                // try tfm
                InputStream tfmfile = finder.findResource(name + ".tfm",
                        TFMEXTENSION);

                if (tfmfile != null) {
                    //System.err.println("\n FONT " + name + "  TFM");

                    EncFactory ef = new EncFactory(finder);

                    // psfonts.map
                    InputStream psin = finder.findResource("psfonts.map", "");

                    if (psin == null) {
                        throw new ConfigurationIOException(
                                "psfonts.map not found");
                        // TODO i18n
                    }

                    try {
                        PSFontsMapReader psfm = new PSFontsMapReader(psin);
                        String fontname = name.replaceAll("\\.tfm|\\.TFM", "");

                        // TFM-Reader
                        TFMReader tfmr = new TFMReader(tfmfile, fontname, psfm,
                                ef);

                        doc = new Document(tfmr.getFontMetric());

                    } catch (HelpingException e) {
                        throw new ConfigurationIOException(e.getMessage());
                    } catch (IOException e) {
                        throw new ConfigurationIOException(e.getMessage());
                    }
                } else {

                    // try afm
                    InputStream afmfile = finder.findResource(name + ".afm",
                            AFMEXTENSION);

                    if (afmfile != null) {
                        //System.err.println("\n FONT " + name + "  AFM");

                        try {
                            AFMReader afmreader = new AFMReader(afmfile, name
                                    + ".pfb", "10");

                            doc = new Document(afmreader.getFontMetric());
                        } catch (IOException e) {
                            throw new ConfigurationIOException(e.getMessage());
                        }

                    } else {
                        throw new ConfigurationIOException(
                                "Sorry, font unknown!!!");
                        // TODO i18n
                    }
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
    private static class FountKey {

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
         * kerning on/off
         */
        private boolean kerning;

        /**
         * Create a new object.
         * @param n     the name
         * @param s     the size
         * @param ls    the letterspace
         * @param lig   the ligature
         * @param kern  the kerning
         */
        public FountKey(final String n, final Dimen s, final Glue ls,
                final boolean lig, final boolean kern) {

            name = n;
            size = s;
            letterspaced = ls;
            ligatures = lig;
            kerning = kern;
        }
    }
}