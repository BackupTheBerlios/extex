/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.font;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontIOException;
import de.dante.extex.font.exception.FontMapNotFoundException;
import de.dante.extex.font.exception.FontNotFoundException;
import de.dante.extex.font.exception.FontReadException;
import de.dante.extex.font.type.ModifiableFount;
import de.dante.extex.font.type.afm.AFMReader;
import de.dante.extex.font.type.efm.EFMFount;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.extex.font.type.vf.VFFont;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontImpl;
import de.dante.extex.interpreter.type.font.VirtualFontImpl;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.resource.ResourceFinder;

/**
 * Factory to load a font.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.21 $
 */
public class FontFactory implements Serializable {

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
    private Map fountmap;

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
     * @throws ConfigurationException from the resourcefinder
     */
    public FontFactory(final Configuration config,
            final ResourceFinder resourceFinder) throws ConfigurationException {

        super();
        finder = resourceFinder;

        cfgType = config.getConfiguration(TAG_TYPE);

        fountmap = new HashMap();
    }

    /**
     * Return a new instance.
     *
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resoucefinder.
     * @throws FontException if an font-error occured.
     */
    public Font getInstance() throws ConfigurationException, FontException {

        return new NullFont();
    }

    /**
     * Return a new instance.
     *
     * @param name          the anme of the font
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resourcefinder
     * @throws FontException if a font-error occured
     */
    public Font getInstance(final String name) throws ConfigurationException,
            FontException {

        return getInstance(name, null, null, new Glue(0), false, false);
    }

    /**
     * Return a new instance.
     *
     * @param name          the anme of the font
     * @param size          the size
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resoucefinder.
     * @throws FontException if a font-error occured.
     */
    public Font getInstance(final String name, final Dimen size)
            throws ConfigurationException, FontException {

        return getInstance(name, size, null, new Glue(0), false, false);
    }

    /**
     * Return a new instance.
     *
     * If the name ist empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param name          the anme of the font
     * @param size          the size
     * @param scale         the scale-factor in 1000
     * @param letterspaced  the letterspaced
     * @param ligatures     ligatures on/off
     * @param kerning       lerning on/off
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resourcefinder.
     * @throws FontException if a font-error occured.
     */
    public Font getInstance(final String name, final Dimen size,
            final Count scale, final Glue letterspaced,
            final boolean ligatures, final boolean kerning)
            throws ConfigurationException, FontException {

        if (name == null || name.trim().length() == 0) {
            return new NullFont();
        }

        FountKey key = new FountKey(name, size, scale, letterspaced, ligatures,
                kerning);
        ModifiableFount fount = (ModifiableFount) (fountmap.get(key));
        if (fount == null) {

            Document doc = loadEFMDocument(name);

            fount = loadFontClass(name, size, scale, letterspaced, ligatures,
                    kerning, doc);

            // store fount
            fountmap.put(key, fount);
        }
        if (fount instanceof EFMFount) {
            if (((EFMFount) fount).isVirtual()) {
                return new VirtualFontImpl(fount);
            }
        }
        return new FontImpl(fount);
    }

    /**
     * @param name          the fontname
     * @param size          the size
     * @param scale         the scale
     * @param letterspaced  use lettersspaced
     * @param ligatures     use ligatures
     * @param kerning       use kerning
     * @param doc           the xml-doc
     * @return Returns the fount
     * @throws ConfigurationException from the resourcefinder.
     */
    private ModifiableFount loadFontClass(final String name, final Dimen size,
            final Count scale, final Glue letterspaced,
            final boolean ligatures, final boolean kerning, final Document doc)
            throws ConfigurationException {

        String classname = getFontClass(doc);
        ModifiableFount fount = null;

        try {
            fount = (ModifiableFount) (Class.forName(classname).getConstructor(
                    new Class[]{Document.class, String.class, Dimen.class,
                            Count.class, Glue.class, Boolean.class,
                            Boolean.class, ResourceFinder.class})
                    .newInstance(new Object[]{doc, name, size, scale,
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
        return fount;
    }

    /**
     * Return a new instance.
     *
     * If the name ist empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param tfm           the tfm-font
     * @param size          the size
     * @param scale         the scale-factor in 1000
     * @param letterspaced  the letterspaced
     * @param ligatures     ligatures on/off
     * @param kerning       lerning on/off
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resourcefinder.
     * @throws FontException if a font-error occured.
     */
    public Font getInstance(final TFMFont tfm, final Dimen size,
            final Count scale, final Glue letterspaced,
            final boolean ligatures, final boolean kerning)
            throws ConfigurationException, FontException {

        if (tfm == null) {
            return new NullFont();
        }

        FountKey key = new FountKey(tfm.getFontname(), size, scale,
                letterspaced, ligatures, kerning);
        ModifiableFount fount = (ModifiableFount) (fountmap.get(key));
        if (fount == null) {

            Document doc = new Document(tfm.getFontMetric());

            fount = loadFontClass(tfm.getFontname(), size, scale, letterspaced,
                    ligatures, kerning, doc);

            // store fount
            fountmap.put(key, fount);
        }

        return new FontImpl(fount);
    }

    /**
     * Return a new instance.
     *
     * If the name ist empty or null, then the <code>NullFont</code>
     * are returned.
     *
     * @param vf            the vf-font
     * @param size          the size
     * @param scale         the scale-factor in 1000
     * @param letterspaced  the letterspaced
     * @param ligatures     ligatures on/off
     * @param kerning       lerning on/off
     * @return Returns the new font instance.
     * @throws ConfigurationException from the resourcefinder.
     * @throws FontException if a font-error occured.
     */
    public Font getInstance(final VFFont vf, final Dimen size,
            final Count scale, final Glue letterspaced,
            final boolean ligatures, final boolean kerning)
            throws ConfigurationException, FontException {

        if (vf == null) {
            return new NullFont();
        }

        FountKey key = new FountKey(vf.getFontname(), size, scale,
                letterspaced, ligatures, kerning);
        ModifiableFount fount = (ModifiableFount) (fountmap.get(key));
        if (fount == null) {

            //                Document doc = new Document(vf.getFontMetric());
            //    
            //                fount = loadFontClass(vf.getFontname(), size, scale, letterspaced,
            //                        ligatures, kerning, doc);
            //    
            //                // store fount
            //                fountmap.put(key, fount);
        }
        return new VirtualFontImpl(fount);
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
     * VF-Extension
     */
    private static final String VFEXTENSION = "vf";

    /**
     * AFM-Extension
     */
    private static final String AFMEXTENSION = "afm";

    /**
     * Read a tfm-font.
     *
     * @param name  the name of the tfm-file
     * @return  the tfm-font or <code>null</code>, if not found
     * @throws ConfigurationException from the resourcefinder
     * @throws FontException in case of an font-error
     */
    public TFMFont readTFMFont(final String name)
            throws ConfigurationException, FontException {

        TFMFont font = null;

        if (name != null) {

            // efm
            InputStream fontfile = finder.findResource(name, TFMEXTENSION);

            if (fontfile != null) {

                EncFactory ef = new EncFactory(finder);

                // psfonts.map
                InputStream psin = finder.findResource("psfonts.map", "");

                if (psin == null) {
                    throw new FontMapNotFoundException();
                }

                try {
                    PSFontsMapReader psfm = new PSFontsMapReader(psin);
                    String fontname = name.replaceAll("\\.tfm|\\.TFM", "");

                    font = new TFMFont(new RandomAccessInputStream(fontfile),
                            fontname);

                    font.setFontMapEncoding(psfm, ef);

                } catch (IOException e) {
                    throw new FontIOException(e.getMessage());
                }
            }
        }

        return font;
    }

    /**
     * Read a vf-font.
     *
     * @param name  the name of the vf-file
     * @return  the vf-font or <code>null</code>, if not found
     * @throws ConfigurationException from the resourcefinder
     * @throws FontException in case of an font-error
     */
    public VFFont readVFFont(final String name) throws ConfigurationException,
            FontException {

        VFFont font = null;

        if (name != null) {

            InputStream fontfile = finder.findResource(name, VFEXTENSION);

            if (fontfile != null) {

                //                EncFactory ef = new EncFactory(finder);
                //
                //                // psfonts.map
                //                InputStream psin = finder.findResource("psfonts.map", "");
                //
                //                if (psin == null) {
                //                    throw new FontMapNotFoundException();
                //                }

                try {
                    //                    PSFontsMapReader psfm = new PSFontsMapReader(psin);
                    String fontname = name.replaceAll("\\.vf|\\.VF", "");

                    font = new VFFont(new RandomAccessInputStream(fontfile),
                            fontname, this);

                    //                    font.setFontMapEncoding(psfm, ef);

                } catch (IOException e) {
                    throw new FontIOException(e.getMessage());
                }
            }
        }

        return font;
    }

    /**
     * Load the efm-Font
     * @param name  the name of the efm-file
     * @return  the efm as Document or <code>null</code>, if not found
     * @throws ConfigurationException from the resourcefinder
     * @throws FontException in case of an font-error
     */
    private Document loadEFMDocument(final String name)
            throws ConfigurationException, FontException {

        Document doc = null;
        if (name != null) {

            // efm
            InputStream fontfile = finder.findResource(name, EFMEXTENSION);

            if (fontfile != null) {
                try {
                    // create a document with SAXBuilder (without validate)
                    SAXBuilder builder = new SAXBuilder(false);
                    doc = builder.build(fontfile);
                } catch (Exception e) {
                    throw new FontReadException(e.getMessage());
                }
            } else {

                // try vf
                InputStream vffile = finder.findResource(name /* + ".vf"*/,
                        VFEXTENSION);

                if (vffile != null) {

                    // TODO incomplete

                } else {
                    // try tfm
                    InputStream tfmfile = finder.findResource(
                            name /* + ".tfm"*/, TFMEXTENSION);

                    if (tfmfile != null) {

                        EncFactory ef = new EncFactory(finder);

                        // psfonts.map
                        InputStream psin = finder.findResource("psfonts.map",
                                "");

                        if (psin == null) {
                            throw new FontMapNotFoundException();
                        }

                        try {
                            PSFontsMapReader psfm = new PSFontsMapReader(psin);
                            String fontname = name.replaceAll("\\.tfm|\\.TFM",
                                    "");

                            // // TFM-Reader
                            // TFMReader tfmr = new TFMReader(tfmfile, fontname, psfm,
                            // ef);
                            // TFM-font
                            TFMFont font = new TFMFont(
                                    new RandomAccessInputStream(tfmfile),
                                    fontname);

                            font.setFontMapEncoding(psfm, ef);

                            doc = new Document(font.getFontMetric());

                        } catch (IOException e) {
                            throw new FontIOException(e.getMessage());
                        }
                    } else {

                        // try afm
                        InputStream afmfile = finder.findResource(
                                name /*+ ".afm"*/, AFMEXTENSION);

                        if (afmfile != null) {

                            try {
                                AFMReader afmreader = new AFMReader(afmfile,
                                        name
                                        /*+ ".pfb"*/, "10");

                                doc = new Document(afmreader.getFontMetric());
                            } catch (IOException e) {
                                throw new FontIOException(e.getMessage());
                            }

                        } else {
                            throw new FontNotFoundException();
                        }
                    }
                }
            }
        }
        if (doc == null) {
            throw new FontNotFoundException(name);
        }
        return doc;
    }
}