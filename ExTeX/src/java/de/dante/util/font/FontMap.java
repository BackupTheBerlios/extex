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
 *
 */

package de.dante.util.font;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.omg.IOP.ENCODING_CDR_ENCAPS;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.dante.extex.font.type.tfm.enc.exception.FontEncodingFileNotFoundException;
import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.exception.FontIOException;
import de.dante.extex.unicodeFont.format.efm.EfmFont;
import de.dante.extex.unicodeFont.format.pfb.PfbParser;
import de.dante.extex.unicodeFont.format.tex.psfontmap.PsFontEncoding;
import de.dante.extex.unicodeFont.format.tex.psfontmap.PsFontsMapReader;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncFactory;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmFont;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmReader;
import de.dante.extex.unicodeFont.glyphname.GlyphName;
import de.dante.extex.unicodeFont.key.FontKey;
import de.dante.extex.unicodeFont.key.FontKeyFactory;
import de.dante.extex.unicodeFont.type.FontPfb;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.xml.XMLStreamWriter;

/**
 * This class manage the font map files.
 *
 * <p>Parameter:</p>
 * <ul>
 *  <li>-fontmap &lt;file&gt;   : the fontmap file   </li>
 *  <li>-efmoutput  &lt>dir&ft; : the output directory for the efm file.</li>
 *  <li>-encoutput  &lt>dir&ft; : the output directory for the enc file.</li>
 *  <li>-add &lt;psfont.map&gt; : add the entries from the psfonts.map</li>
 * </ul>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class FontMap extends AbstractFontUtil {

    /**
     * The default encoding for the xml files.
     */
    public static final String DEFAULTENCODING = "ISO8859-1";

    /**
     * The font map file (default).
     */
    private String fontMapFile = "src/java/config/fontmap.xml";

    /**
     * The output directory for efm files.
     */
    private String efmoutput = ".";

    /**
     * The output directory for enc files.
     */
    private String encoutput = ".";

    /**
     * The mapping glyphname - unicode.
     */
    private GlyphName glyphName;

    /**
     * The font eky factory.
     */
    private FontKeyFactory fontKeyFactory;

    /**
     * Create a new object.
     *
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if an font error occurred.
     */
    public FontMap() throws ConfigurationException, FontException {

        super(FontMap.class);

        fontmap = new HashMap();
        try {
            glyphName = GlyphName.getInstance();
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        }
        fontKeyFactory = new FontKeyFactory();
    }

    /**
     * main.
     *
     * @param args  The command line.
     * @throws Exception if an error occurred.
     */
    public static void main(final String[] args) throws Exception {

        FontMap map = new FontMap();
        map.getConsoleHandler().setLevel(Level.ALL);

        if (args.length == 0) {
            map.getLogger().severe(map.getLocalizer().format("FontMap.Call"));
            System.exit(1);
        }

        int i = 0;
        while (i < args.length) {
            if ("-fontmap".equals(args[i])) {
                i++;
                if (i < args.length) {
                    map.setFontMapFile(args[i]);
                }
            } else if ("-efmoutput".equals(args[i])) {
                i++;
                if (i < args.length) {
                    map.setEfmoutput(args[i]);
                }
            } else if ("-encoutput".equals(args[i])) {
                i++;
                if (i < args.length) {
                    map.setEncoutput(args[i]);
                }
            } else if ("-add".equals(args[i])) {
                i++;
                if (i < args.length) {
                    map.addPsFontMap(args[i]);
                }
            }
            i++;
        }
        map.close();

    }

    /**
     * Close the font map and save the map in the xml file.
     *
     * @throws FontException if an font-error occurred.
     */
    public void close() throws FontException {

        if (addEntries) {
            // save
            getLogger().info(
                    getLocalizer().format("FontMap.Close", fontMapFile));

            try {
                XMLStreamWriter out = new XMLStreamWriter(new FileOutputStream(
                        fontMapFile), DEFAULTENCODING);
                out.setBeauty(true);

                out.writeStartDocument();
                out.writeComment(getLocalizer().format(
                        "FontMap.CommentFontMapFile"));
                out.writeStartElement(TAG_MAP);

                // sort the keys
                String[] keys = new String[fontmap.size()];
                keys = (String[]) fontmap.keySet().toArray(keys);
                Arrays.sort(keys);

                for (int i = 0; i < keys.length; i++) {
                    MapEntry entry = (MapEntry) fontmap.get(keys[i]);
                    out.writeStartElement(TAG_ENTRY);
                    out.writeAttribute(ATT_TEXFONT, entry.getTexFont());
                    if (entry.getEncVec().length() > 0) {
                        out.writeAttribute(ATT_ENCVEC, entry.getEncVec());
                    }
                    if (entry.getEfmFile().length() > 0) {
                        out.writeAttribute(ATT_EFM, entry.getEfmFile());
                    }
                    out.writeEndElement();
                }

                out.writeEndElement();
                out.writeEndDocument();
                out.close();

            } catch (IOException e) {
                throw new FontIOException(e.getMessage());
            }
        }
    }

    /**
     * The PsFontMaprReader.
     */
    private PsFontsMapReader psFontMapReader;

    /**
     * Add the entries from the psfont.map file to the map.
     *
     * @param psfontfile    The psfont.map file.
     * @throws FontException if an error occurred.
     */
    public void addPsFontMap(final String psfontfile) throws FontException {

        try {
            getLogger().info(
                    getLocalizer()
                            .format("FontMap.Read.psfont.map", psfontfile));

            BufferedInputStream input = new BufferedInputStream(
                    new FileInputStream(psfontfile), BUFFERSIZE);

            psFontMapReader = new PsFontsMapReader(input);

            addEntries = true;

            Map encmap = psFontMapReader.getPsFontEncodingMap();
            Iterator it = encmap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                PsFontEncoding fe = (PsFontEncoding) encmap.get(key);
                addEntries(fe);
            }

            input.close();
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        } catch (ConfigurationException e) {
            throw new FontException(e.getMessage());
        }
    }

    /**
     * Adds the entries.
     * @param fe    The PsFontEncoding.
     * @throws ConfigurationException from the config system.
     * @throws FontException if an font error occurred.
     */
    private void addEntries(final PsFontEncoding fe)
            throws ConfigurationException, FontException {

        String texfontname = fe.getFilename();
        getLogger().info(getLocalizer().format("FontMap.Add", texfontname));
        String texencfile = fe.getEncfile();
        String encfile = "";
        String efmfile = texfontname + ".efm";
        String exfontfile = fe.getFontfile();

        if (encfactory == null) {
            encfactory = new EncFactory(getFinder());
        }

        // encoding file:   file.enc.xml
        if (texencfile != null && texencfile.length() > 0) {
            encfile = texencfile + ".xml";
            InputStream texenc = getFinder().findResource(encfile, "");
            if (texenc == null) {
                // create enc.xml file
                createEncVector(texencfile, encfile);
            }
        }

        // read tfm, if the correspond efm missing
        InputStream efm = getFinder().findResource(efmfile, "");
        if (efm == null) {
            // create efm file from the tfm file
            createEfmFile(texfontname, efmfile, exfontfile, texencfile);
        }

        MapEntry entry = new MapEntry(texfontname, encfile, efmfile, exfontfile);
        if (fontmap.containsKey(texfontname)) {
            getLogger().info(
                    getLocalizer().format("FontMap.EntryExists", texfontname));
        }
        fontmap.put(texfontname, entry);

    }

    /**
     * Type tfm font.
     */
    private static final String TFM = "tfm";

    /**
     * Type vf font.
     */
    private static final String VF = "vf";

    /**
     * Create a efm file from a tfm file.
     * @param texfontname   The tex font name.
     * @param efmfile       The efm file.
     * @param exfontfile    The font file e.g. cmr12.pfb.
     * @param texencfile    The tex encoding file.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if an font error occurred.
     */
    private void createEfmFile(final String texfontname, final String efmfile,
            final String exfontfile, final String texencfile)
            throws ConfigurationException, FontException {

        try {
            // vf ?
            InputStream in = getFinder().findResource(texfontname, VF);
            if (in != null) {
                getLogger().info(
                        getLocalizer().format("FontMap.VfFont", texfontname));
            } else {

                InputStream tfmin = getFinder().findResource(texfontname, TFM);

                if (tfmin == null) {
                    getLogger().info(
                            getLocalizer().format("FontMap.TfmFileNotFound",
                                    texfontname));
                } else {

                    getLogger().info(
                            getLocalizer().format("FontMap.CreateEfm",
                                    efmoutput + File.separator + efmfile));

                    TfmReader tfmReader = new TfmReader(tfmin, texfontname);

                    // pfb exists
                    if (exfontfile != null && exfontfile.endsWith(".pfb")) {
                        InputStream pfbin = getFinder().findResource(
                                exfontfile, "");
                        if (pfbin == null) {
                            getLogger().info(
                                    getLocalizer().format(
                                            "FontMap.ExFontFileNotFound",
                                            exfontfile));
                        } else {
                            getLogger().info(
                                    getLocalizer().format("FontMap.ReadEfm",
                                            exfontfile));
                            PfbParser pfbParser = new PfbParser(pfbin);
                            if (pfbin != null) {
                                tfmReader.setPfbParser(pfbParser);
                            }
                        }
                    }

                    tfmReader.setFontMapEncoding(psFontMapReader, encfactory);

                    EfmFont efmFont = new EfmFont(tfmReader);
                    efmFont.write(new FileOutputStream(efmoutput
                            + File.separator + efmfile), DEFAULTENCODING);

                }
            }
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        }
    }

    /**
     * The encoding vector factory.
     */
    private EncFactory encfactory;

    /**
     * Create a encoding vector file.
     * @param texencfile    The tex encoding vector file.
     * @param encfile       The new file.
     * @throws ConfigurationException from the configuration system.
     * @throws FontException if an font error occurred.
     */
    private void createEncVector(final String texencfile, final String encfile)
            throws ConfigurationException, FontException {

        try {
            getLogger().info(
                    getLocalizer().format("FontMap.CreateEnc",
                            encoutput + File.separator + encfile));

            String[] encv = encfactory.getEncodingTableWithoutSlash(texencfile);

            XMLStreamWriter out = new XMLStreamWriter(new FileOutputStream(
                    encoutput + File.separator + encfile), DEFAULTENCODING);
            out.setBeauty(true);

            out.writeStartDocument();
            out.writeComment(getLocalizer().format(
                    "FontMap.CommentEncodingFile", texencfile));
            out.writeStartElement(TAG_ENCODING);

            for (int i = 0; i < encv.length; i++) {
                out.writeStartElement(TAG_ENTRY);
                out.writeAttribute(ATT_ID, i);
                UnicodeChar uc = glyphName.getUnicode(encv[i]);
                if (uc != null) {
                    out.writeAttribute(ATT_UNICODE, uc.getCodePoint());
                    String un = uc.getUnicodeName();
                    if (un != null) {
                        out.writeAttribute(ATT_UNICODENAME, un);
                    } else {
                        out.writeAttribute(ATT_UNICODENAME, "");
                    }
                } else {
                    out.writeAttribute(ATT_UNICODE, "");
                    out.writeAttribute(ATT_UNICODENAME, "");
                }
                out.writeAttribute(ATT_GLYPHNAME, encv[i]);
                out.writeEndElement();
            }
            out.writeEndElement();
            out.writeEndDocument();
            out.close();

        } catch (IOException e) {
            getLogger().severe(e.getMessage());
            throw new FontIOException(e.getMessage());
        }

    }

    /**
     * Shows, that entries are added in the map.
     */
    private boolean addEntries = false;

    /**
     * Returns the fontMapFile.
     * @return Returns the fontMapFile.
     */
    public String getFontMapFile() {

        return fontMapFile;
    }

    /**
     * Set the fontMapFile.
     *
     * @param file The fontMapFile to set.
     * @throws IOException if an IO-error occurred.
     */
    public void setFontMapFile(final String file) throws IOException {

        fontMapFile = file;
        read();
    }

    /**
     * The map for the fontmap entries.
     */
    private Map fontmap;

    /**
     * the buffer size.
     */
    private static final int BUFFERSIZE = 0x7fff;

    /**
     * Read the file.
     *
     * @throws IOException if an IO-error occurred.
     */
    private void read() throws IOException {

        try {
            File f = new File(fontMapFile);
            if (f.exists()) {
                getLogger().info(
                        getLocalizer().format("FontMap.Read", fontMapFile));

                BufferedInputStream input = new BufferedInputStream(
                        new FileInputStream(f), BUFFERSIZE);

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                MyDefaultHandler defaulthandler = new MyDefaultHandler();
                parser.parse(input, defaulthandler);
                input.close();
            }
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * The tag for entries.
     */
    private static final String TAG_ENTRY = "entry";

    /**
     * The tag for encoding.
     */
    private static final String TAG_ENCODING = "encoding";

    /**
     * The tag for map.
     */
    private static final String TAG_MAP = "map";

    /**
     * The attribute texfont.
     */
    private static final String ATT_TEXFONT = "texfont";

    /**
     * The attribute encvec.
     */
    private static final String ATT_ENCVEC = "encvec";

    /**
     * The attribute efm.
     */
    private static final String ATT_EFM = "efm";

    /**
     * The attribute exfile.
     */
    private static final String ATT_EXFILE = "exfile";

    /**
     * The attribute id.
     */
    private static final String ATT_ID = "id";

    /**
     * The attribute unicode.
     */
    private static final String ATT_UNICODE = "unicode";

    /**
     * The attribute unicodename.
     */
    private static final String ATT_UNICODENAME = "unicodename";

    /**
     * The attribute glyphname.
     */
    private static final String ATT_GLYPHNAME = "glyphname";

    /**
     * Default handler.
     */
    private class MyDefaultHandler extends DefaultHandler {

        /**
         * Create a new object.
         */
        public MyDefaultHandler() {

            super();
        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#startElement(
         *      java.lang.String, java.lang.String,
         *      java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(final String uri, final String localName,
                final String qName, final Attributes attributes)
                throws SAXException {

            super.startElement(uri, localName, qName, attributes);

            if (TAG_ENTRY.equals(qName)) {
                MapEntry entry = new MapEntry(attributes);
                fontmap.put(entry.getTexFont(), entry);
            }
        }
    }

    /**
     * Class for the map entry.
     *
     */
    public static class MapEntry {

        /**
         * The tex font name.
         */
        private String texFont = "";

        /**
         * The encoding vector.
         */
        private String encVec = "";

        /**
         * The efm file.
         */
        private String efmFile = "";

        /**
         * The external font file.
         */
        private String exFontFile = "";

        /**
         * Create a new Object.
         *
         * @param attributes    The xml attributes.
         */
        public MapEntry(final Attributes attributes) {

            setTexFont(attributes.getValue(ATT_TEXFONT));
            setEncVec(attributes.getValue(ATT_ENCVEC));
            setEfmFile(attributes.getValue(ATT_EFM));
        }

        /**
         * Create a new object.
         *
         * @param atexfontname  The tex font name.
         * @param aencfile      The encoding vector
         * @param aefmfile      The efm file.
         * @param aexfontfile   The font file e.g. cmr12.pfb.
         */
        public MapEntry(final String atexfontname, final String aencfile,
                final String aefmfile, final String aexfontfile) {

            texFont = atexfontname;
            encVec = aencfile;
            efmFile = aefmfile;
            exFontFile = aexfontfile;
        }

        /**
         * Returns the efmFile.
         * @return Returns the efmFile.
         */
        public String getEfmFile() {

            return efmFile;
        }

        /**
         * Set the efmFile.
         * @param aefmFile The efmFile to set.
         */
        public void setEfmFile(final String aefmFile) {

            if (aefmFile != null) {
                efmFile = aefmFile;
            }
        }

        /**
         * Returns the encVec.
         * @return Returns the encVec.
         */
        public String getEncVec() {

            return encVec;
        }

        /**
         * Set the encVec.
         * @param aencVec The encVec to set.
         */
        public void setEncVec(final String aencVec) {

            if (aencVec != null) {
                encVec = aencVec;
            }
        }

        /**
         * Returns the texFont.
         * @return Returns the texFont.
         */
        public String getTexFont() {

            return texFont;
        }

        /**
         * Set the texFont.
         * @param atexFont The texFont to set.
         */
        public void setTexFont(final String atexFont) {

            if (atexFont != null) {
                texFont = atexFont;
            }
        }

        /**
         * Print the values.
         * @return Returns the <code>String</code> for the entry.
         */
        public String toString() {

            return texFont + " : " + encVec + " : " + efmFile + " : "
                    + exFontFile;
        }

        /**
         * Returns the exFontFile.
         * @return Returns the exFontFile.
         */
        public String getExFontFile() {

            return exFontFile;
        }

        /**
         * Set the exFontFile.
         * @param file The exFontFile to set.
         */
        public void setExFontFile(final String file) {

            if (file != null) {
                exFontFile = file;
            }
        }
    }

    /**
     * Set the efm output directory.
     * @param dir The efm output directory to set.
     */
    public void setEfmoutput(final String dir) {

        efmoutput = dir;
    }

    /**
     * Set the encoding output.
     * @param dir The encoding output to set.
     */
    public void setEncoutput(final String dir) {

        encoutput = dir;
    }

}
