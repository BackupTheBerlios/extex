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
 */

package de.dante.extex.font;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontIOException;
import de.dante.extex.font.exception.FontMapNotFoundException;
import de.dante.extex.font.exception.FontNotFoundException;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.afm.AfmFont;
import de.dante.extex.font.type.efm.EfmReader;
import de.dante.extex.font.type.efm.ModifiableFountEFM;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.font.type.tfm.ModifiableFountTFM;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.extex.font.type.vf.VFFont;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontImpl;
import de.dante.extex.interpreter.type.font.VirtualFontImpl;
import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.RegistrarException;
import de.dante.util.framework.RegistrarObserver;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Factory to load a font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.35 $
 */
public class FontFactoryImpl implements FontFactory, PropertyConfigurable {

    /**
     * The field <tt>ATTR_DEFAULT</tt> contains the name of the attribute in the
     * configuration file which contains the specification default-type.
     */
    private static final String ATTR_DEFAULT = "default";

    /**
     * The null font
     */
    private static final NullFont NULLFONT = new NullFont();

    /**
     * The field <tt>TAG_TYPE</tt> contains the name of the tag in the
     * configuration file which contains the specification for the font-type.
     */
    private static final String TAG_TYPE = "Type";

    /**
     * Configuration for <tt>Type</tt>
     */
    private Configuration cfgType;

    /**
     * encoder factory
     */
    private EncFactory ef;

    /**
     * the file finder
     */
    private ResourceFinder finder;

    /**
     * Font map
     */
    private Map fountMap = new HashMap();

    /**
     * properties
     */
    private Properties prop;

    /**
     * the reader for psfont.map
     */
    private PSFontsMapReader psfm;

    /**
     * Creates a new object.
     *
     * @param config the configuration object
     * @param resourceFinder the file finder
     *
     * @throws ConfigurationException from the resource finder
     */
    public FontFactoryImpl(final Configuration config,
            final ResourceFinder resourceFinder) throws ConfigurationException {

        super();
        finder = resourceFinder;
        cfgType = config.getConfiguration(TAG_TYPE);

        Registrar.register(new RegistrarObserver() {

            public Object reconnect(Object object) throws RegistrarException {

                FontImpl fi = (FontImpl) object;
                try {
                    fi.setFount(getFount(fi.getFontKey()));
                } catch (ConfigurationException e) {
                    throw new RegistrarException(e);
                } catch (FontException e) {
                    throw new RegistrarException(e);
                }

                return fi;
            }

        }, FontImpl.class);
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(
     *      java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        return this.finder.findResource(name, type);
    }

    /**
     * Return a new instance.
     *
     * If the name is empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param key the fount key
     *
     * @return the new font instance.
     *
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font-error occurred.
     */
    public Font getInstance(final FountKey key)
            throws ConfigurationException,
                FontException {

        if (key == null || key.getName() == null
                || key.getName().trim().length() == 0) {
            return NULLFONT;
        }

        return new FontImpl(getFount(key));
    }

    /**
     * Retrieve the fount; either one already known or a new one.
     *
     * @param key the font key
     *
     * @return the fount
     *
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font-error occurred.
     */
    private ModifiableFount getFount(final FountKey key)
            throws ConfigurationException,
                FontException {

        ModifiableFount fount = (ModifiableFount) (fountMap.get(key));
        if (fount == null) {

            fount = loadFont(key);
            fountMap.put(key, fount);
        }
        return fount;
    }

    /**
     * Return a new instance.
     *
     * If the name is empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param tfm the tfm font
     * @param key the fount key
     *
     * @return Returns the new font instance.
     *
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font error occurred.
     */
    public Font getInstance(final TFMFont tfm, final FountKey key)
            throws ConfigurationException,
                FontException {

        if (tfm == null) {
            return new NullFont();
        }

        ModifiableFount fount = (ModifiableFount) (fountMap.get(key));
        if (fount == null) {
            fount = new ModifiableFountTFM(key, tfm);
            fountMap.put(key, fount);
        }

        return new FontImpl(fount);
    }

    /**
     * Return a new instance.
     *
     * If the name is empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param vf the vf font
     * @param key the fount key
     *
     * @return Returns the new font instance.
     *
     * @throws ConfigurationException from the resource finder.
     * @throws FontException if a font-error occurred.
     */
    public Font getInstance(final VFFont vf, final FountKey key)
            throws ConfigurationException,
                FontException {

        if (vf == null) {
            return new NullFont();
        }

        ModifiableFount fount = (ModifiableFount) (fountMap.get(key));
        return new VirtualFontImpl(fount);
    }

    /**
     * Returns the psfontmapreader.
     *
     * @throws FontException if a font-error occurs.
     * @throws ConfigurationException from the config-system.
     * @return Returns the psfm.
     */
    public PSFontsMapReader getPsfm()
            throws FontException,
                ConfigurationException {

        if (psfm == null) {
            ef = new EncFactory(finder);

            InputStream psin = findResource("psfonts.map", "");

            if (psin == null) {
                throw new FontMapNotFoundException();
            }
            psfm = new PSFontsMapReader(psin);
        }
        return psfm;
    }

    /**
     * Load the Font.
     *
     * @param key the fount key
     *
     * @return the font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of a font error
     */
    private ModifiableFount loadFont(final FountKey key)
            throws ConfigurationException,
                FontException {

        // efm ???
        try {
            EfmReader efmfont = readEFMFont(key.getName());
            if (efmfont != null) {
                return new ModifiableFountEFM(key, efmfont);
            }
        } catch (FontException e) {
            // try next
        }

        // tfm ???
        try {
            TFMFont tfmfont = readTFMFont(key.getName());
            if (tfmfont != null) {
                return new ModifiableFountTFM(key, tfmfont);
            }
        } catch (FontException e) {
            // try next
        }

        throw new FontNotFoundException(key.getName());
    }

    /**
     * Read an afm font.
     *
     * @param name the name of the afm file
     *
     * @return the afm font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of a font error
     */
    public AfmFont readAFMFont(final String name)
            throws ConfigurationException,
                FontException {

        AfmFont font = null;

        if (name != null) {

            // efm
            InputStream fontfile = findResource(name, AFM_EXTENSION);

            if (fontfile != null) {
                try {
                    font = new AfmFont(fontfile, name);
                } catch (IOException e) {
                    throw new FontIOException(e.getMessage());
                }
            }
        }
        return font;
    }

    /**
     * Read a tfm font.
     *
     * @param name the name of the tfm file
     *
     * @return the tfm font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of a font error
     */
    public EfmReader readEFMFont(final String name)
            throws ConfigurationException,
                FontException {

        EfmReader font = null;

        if (name != null) {

            InputStream fontfile = findResource(name, EFM_EXTENSION);

            if (fontfile != null) {
                font = new EfmReader(fontfile);
            }
        }
        return font;
    }

    /**
     * Read a tfm font.
     *
     * @param name  the name of the tfm file
     *
     * @return  the tfm font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of a font error
     */
    public TFMFont readTFMFont(final String name)
            throws ConfigurationException,
                FontException {

        if (name == null) {
            return null;
        }

        InputStream fontfile = findResource(name, TFM_EXTENSION);

        if (fontfile == null) {
            return null;
        }

        TFMFont font = null;
        String fontname = name.replaceAll("\\.tfm|\\.TFM", "");

        try {
            font = new TFMFont(new RandomAccessInputStream(fontfile), fontname);

            font.setFontMapEncoding(getPsfm(), ef);

            String pfbfile = font.getPfbfilename();
            if (pfbfile != null) {
                // pfb file
                InputStream pfbin = findResource(pfbfile, "");
                if (pfbin == null) {
                    throw new FileNotFoundException(pfbfile);
                }
                try {

                    font.setPfbParser(new PfbParser(pfbin));
                } catch (Exception e) {
                    throw new FontException(e.getMessage());
                }
            }

            // cache the font ?
            String cachedir = prop.getProperty("fonts.cache");
            if (cachedir != null) {
                StringBuffer buf = new StringBuffer();
                buf.append(cachedir).append("/");
                buf.append(fontname).append(".").append(EFM_EXTENSION);
                // write to xml-file
                XMLStreamWriter writer = new XMLStreamWriter(
                        new FileOutputStream(buf.toString()), "ISO-8859-1");
                writer.setBeauty(true);
                writer.writeStartDocument();
                font.writeEFM(writer);
                writer.writeEndDocument();
                writer.close();
            }
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        }

        return font;
    }

    /**
     * Read a vf font.
     *
     * @param name  the name of the vf-file
     *
     * @return  the vf font or <code>null</code>, if not found
     *
     * @throws ConfigurationException from the resource finder
     * @throws FontException in case of a font error
     */
    public VFFont readVFFont(final String name)
            throws ConfigurationException,
                FontException {

        VFFont font = null;

        if (name != null) {

            InputStream fontfile = findResource(name, VF_EXTENSION);

            if (fontfile != null) {

                try {
                    String fontname = name.replaceAll("\\.vf|\\.VF", "");

                    font = new VFFont(new RandomAccessInputStream(fontfile),
                            fontname, this);

                    //font.setFontMapEncoding(getPsfm(), ef);

                } catch (IOException e) {
                    throw new FontIOException(e.getMessage());
                }
            }
        }

        return font;
    }

    /**
     * @see de.dante.util.resource.PropertyConfigurable#setProperties(java.util.Properties)
     */
    public void setProperties(final Properties properties) {

        prop = properties;
    }

}
