/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.efm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontIOException;
import de.dante.extex.font.type.efm.exception.FontSAXException;

/**
 * Reader for a efm-file.
 *
 * <p>If the type is tfm, then all dimens are fixwords!!!</p>
 * 
 *
 * TODO DTD check is missing!
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmReader implements Serializable {

    /**
     * the default font size
     */
    private static final float DEFAULT_FONT_SIZE = 10.0f;

    /**
     * the default units per em
     */
    private static final int DEFAULT_UNITS_PER_EM = 1000;

    /**
     * Create a new object.
     * @param in    the input
     * @throws FontException if a font-error occurs.
     */
    public EfmReader(final InputStream in) throws FontException {

        super();
        try {
            parse(in);
            in.close();
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        } catch (SAXException e) {
            throw new FontSAXException(e.getMessage());
        }
    }

    /**
     * Create a new object.
     * @param fn    the file name
     * @throws FontException if a font-error occurs.
     * @throws IOException if a IO-error occurs.
     */
    public EfmReader(final String fn) throws IOException, FontException {

        this(new FileInputStream(fn));
    }

    /**
     * the buffer size
     */
    private static final int BUFFERSIZE = 0xffff;

    /**
     * Parse the input.
     *
     * @param in    the input
     * @throws IOException if an IO-error occurs.
     * @throws SAXException it a XML-error occurs.
     */
    private void parse(final InputStream in) throws IOException, SAXException {

        BufferedInputStream input = new BufferedInputStream(in, BUFFERSIZE);

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            SAXParser parser = factory.newSAXParser();

            EfmDefaultHandler defaulthandler = new EfmDefaultHandler();

            parser.parse(input, defaulthandler);

        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }

    }

    /**
     * Defaulthandler for EFM.
     */
    private class EfmDefaultHandler extends DefaultHandler {

        /**
         * Create a new object.
         */
        public EfmDefaultHandler() {

            super();
        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#startElement(
         *      java.lang.String, java.lang.String, java.lang.String,
         *      org.xml.sax.Attributes)
         */
        public void startElement(final String uri, final String localName,
                final String qName, final Attributes attributes)
                throws SAXException {

            super.startElement(uri, localName, qName, attributes);

            if (FONT.equals(qName)) {
                setFont(attributes);
            } else if (FONTDIMEN.equals(qName)) {
                setFontDimen(attributes);
            } else if (GLYPH.equals(qName)) {
                setStartGlyph(attributes);
            } else if (LIGATURE.equals(qName)) {
                setStartLigature(attributes);
            } else if (KERNING.equals(qName)) {
                setStartKerning(attributes);
            }
        }

        /**
         * @see org.xml.sax.helpers.DefaultHandler#endElement(
         *      java.lang.String, java.lang.String, java.lang.String)
         */
        public void endElement(final String uri, final String localName,
                final String qName) throws SAXException {

            super.endElement(uri, localName, qName);

            if (GLYPH.equals(qName)) {
                setEndGlyph();
            }
        }
    }

    /**
     * Attribute: font - id
     */
    public static final String FONT_ID = "id";

    /**
     * Attribute: font - type
     */
    public static final String FONT_TYPE = "type";

    /**
     * Attribute: font - default-size
     */
    public static final String FONT_DEFAULT_SIZE = "default-size";

    /**
     * Attribute: font - units-per-em
     */
    public static final String FONT_UNITS_PER_EM = "units-per-em";

    /**
     * Element: fontdimen
     */
    public static final String FONTDIMEN = "fontdimen";

    // ----------------------------------------

    /**
     * the id
     */
    private String id = "";

    /**
     * the default size
     */
    private float defaultsize = DEFAULT_FONT_SIZE;

    /**
     * the type (e.g. tfm , ttf )
     */
    private String type = "";

    /**
     * units per em
     */
    private int unitsperem = DEFAULT_UNITS_PER_EM;

    // ----------------------------------------

    /**
     * Set the attributes for 'fontdimen'.
     * <pre>
     * <fontdimen SLANT="0" SPACE="326.385498" STRETCH="163.192749"
     *            SHRINK="108.795166" XHEIGHT="430.556297" QUAD="979.156494"
     *            EXTRASPACE="108.795166" />
     * </pre>
     *
     * @param attributes    the attributes
     */
    private void setFontDimen(final Attributes attributes) {

        for (int i = 0, count = attributes.getLength(); i < count; i++) {
            String qname = attributes.getQName(i);
            String value = attributes.getValue(i);
            float val = 0.0f;
            try {
                val = Float.parseFloat(value);
            } catch (Exception e) {
                // use default
                val = 0.0f;
            }
            dimenmap.put(qname, new Float(val));
        }
    }

    /**
     * the map for the dimen values.
     */
    private Map dimenmap = new HashMap();

    // ----------------------------------------

    /**
     * Set the attributes for 'font'.
     * <pre>
     * <font font-name="CMR" font-family="CMR" checksum="1487622411"
     *       type="tfm-normal" filename="cmr12.pfb">
     * </pre>
     *
     * @param attributes    the attributes
     */
    private void setFont(final Attributes attributes) {

        for (int i = 0, count = attributes.getLength(); i < count; i++) {
            String qname = attributes.getQName(i);
            String value = attributes.getValue(i);

            if (FONT_ID.equals(qname)) {
                id = value;
            } else if (FONT_NAME.equals(qname)) {
                fontname = value;
            } else if (FONT_FAMILY.equals(qname)) {
                fontfamily = value;
            } else if (FONT_DEFAULT_SIZE.equals(qname)) {
                try {
                    defaultsize = Float.parseFloat(value);
                } catch (Exception e) {
                    // use default
                    defaultsize = DEFAULT_FONT_SIZE;
                }
            } else if (FONT_TYPE.equals(qname)) {
                type = value;
            } else if (FONT_UNITS_PER_EM.equals(qname)) {
                try {
                    unitsperem = Integer.parseInt(value);
                } catch (Exception e) {
                    // use default
                    unitsperem = DEFAULT_UNITS_PER_EM;
                }
            } else if (FONT_CHECKSUM.equals(qname)) {
                try {
                    checksum = Integer.parseInt(value);
                } catch (Exception e) {
                    // use default
                    checksum = -1;
                }
            } else if (FONT_SUBTYPE.equals(qname)) {
                subtype = value;
            } else if (FONT_FILENAME.equals(qname)) {
                filename = value;
            }
        }
    }

    /**
     * Element: font
     */
    public static final String FONT = "font";

    /**
     * Attribute: font - fontname
     */
    public static final String FONT_NAME = "font-name";

    /**
     * Attribute: font - fontfamily
     */
    public static final String FONT_FAMILY = "font-family";

    /**
     * Attribute: font - checksum
     */
    public static final String FONT_CHECKSUM = "checksum";

    /**
     * Attribute: font - subtype
     */
    public static final String FONT_SUBTYPE = "type";

    /**
     * Attribute: font - filename
     */
    public static final String FONT_FILENAME = "filename";

    /**
     * the font name
     */
    private String fontname = "";

    /**
     * the font family
     */
    private String fontfamily = "";

    /**
     * the checksum
     */
    private int checksum = -1;

    /**
     * the sub type (e.g. tfm-normal)
     */
    private String subtype = "";

    /**
     * the filename (e.g. cmr12.pfb)
     */
    private String filename = "";

    // -------------------------------------------------------

    /**
     * Set the attributes for 'glyph'.
     * <pre>
     *  <glyph ID="11" glyph-number="11" width="598920" height="728177"
     *         depth="0" italic="72121">
     *     <ligature letter-id="108" letter="l" lig-id="15" />
     *     <kerning glyph-id="39" char="'" size="73362" />
     *  </glyph>
     * </pre>
     *
     * @param attributes    the attributes
     */
    private void setStartGlyph(final Attributes attributes) {

        actualglyph = new EfmGlyph();

        for (int i = 0, count = attributes.getLength(); i < count; i++) {
            String qname = attributes.getQName(i);
            String value = attributes.getValue(i);

            if (GLYPH_ID.equals(qname)) {
                actualglyph.setId(value);
            } else if (GLYPH_NUMBER.equals(qname)) {
                actualglyph.setNumber(value);
            } else if (GLYPH_NAME.equals(qname)) {
                actualglyph.setName(value);
            } else if (GLYPH_WIDTH.equals(qname)) {
                actualglyph.setWidth(value);
            } else if (GLYPH_HEIGHT.equals(qname)) {
                actualglyph.setHeight(value);
            } else if (GLYPH_DEPTH.equals(qname)) {
                actualglyph.setDepth(value);
            } else if (GLYPH_ITALIC.equals(qname)) {
                actualglyph.setItalic(value);
            }
        }

        glyphmap.put(actualglyph.getIdasString(), actualglyph);
    }

    /**
     * Close the glyph element.
     */
    private void setEndGlyph() {

        actualglyph = null;
    }

    /**
     * the actual glyph
     */
    private EfmGlyph actualglyph = null;

    /**
     * the map for the glyphs
     */
    private Map glyphmap = new HashMap();

    /**
     * Element: glyph
     */
    public static final String GLYPH = "glyph";

    /**
     * Attribut: glyph - id
     */
    public static final String GLYPH_ID = "ID";

    /**
     * Attribut: glyph - number
     */
    public static final String GLYPH_NUMBER = "glyph-number";

    /**
     * Attribut: glyph - name
     */
    public static final String GLYPH_NAME = "gylph-name";

    /**
     * Attribut: glyph - width
     */
    public static final String GLYPH_WIDTH = "width";

    /**
     * Attribut: glyph - height
     */
    public static final String GLYPH_HEIGHT = "height";

    /**
     * Attribut: glyph - depth
     */
    public static final String GLYPH_DEPTH = "depth";

    /**
     * Attribut: glyph - italic
     */
    public static final String GLYPH_ITALIC = "italic";

    // -------------------------------------------------------

    /**
     * Set the attributes for 'ligature'.
     * <pre>
     *  <ligature letter-id="108" letter="l" lig-id="15" />
     * </pre>
     *
     * @param attributes    the attributes
     */
    private void setStartLigature(final Attributes attributes) {

        EfmLigature lig = new EfmLigature();

        for (int i = 0, count = attributes.getLength(); i < count; i++) {
            String qname = attributes.getQName(i);
            String value = attributes.getValue(i);

            if (LIGATURE_ID.equals(qname)) {
                lig.setId(value);
            } else if (LIGATURE_LETTER.equals(qname)) {
                lig.setLetter(value);
            } else if (LIGATURE_LIGID.equals(qname)) {
                lig.setLigid(value);
            }
        }

        if (actualglyph != null) {
            actualglyph.addLigature(lig.getIdasString(), lig);
        }
    }

    /**
     * Element: ligature
     */
    public static final String LIGATURE = "ligature";

    /**
     * Attribute: ligature - id
     */
    public static final String LIGATURE_ID = "letter-id";

    /**
     * Attribute: ligature - letter
     */
    public static final String LIGATURE_LETTER = "letter";

    /**
     * Attribute: ligature - ligid
     */
    public static final String LIGATURE_LIGID = "lig-id";

    // -------------------------------------------------------

    /**
     * Set the attributes for 'kerning'.
     * <pre>
     *   <kerning glyph-id="39" char="'" size="69.73362" />
     * </pre>
     *
     * @param attributes    the attributes
     */
    private void setStartKerning(final Attributes attributes) {

        EfmKerning kern = new EfmKerning();

        for (int i = 0, count = attributes.getLength(); i < count; i++) {
            String qname = attributes.getQName(i);
            String value = attributes.getValue(i);

            if (KERNING_ID.equals(qname)) {
                kern.setId(value);
            } else if (KERNING_CHAR.equals(qname)) {
                kern.setCharacter(value);
            } else if (KERNING_SIZE.equals(qname)) {
                kern.setSize(value);
            }
        }

        if (actualglyph != null) {
            actualglyph.addKerning(kern.getIdasString(), kern);
        }
    }

    /**
     * Element: kerning
     */
    public static final String KERNING = "kerning";

    /**
     * Attribute: kerning - id
     */
    public static final String KERNING_ID = "glyph-id";

    /**
     * Attribute: kerning - char
     */
    public static final String KERNING_CHAR = "char";

    /**
     * Attribute: kerning - sizte
     */
    public static final String KERNING_SIZE = "size";

    // -------------------------------------------------------

    /**
     * Returns the checksum.
     * @return Returns the checksum.
     */
    public int getChecksum() {

        return checksum;
    }

    /**
     * Returns the defaultsize.
     * @return Returns the defaultsize.
     */
    public float getDefaultsize() {

        return defaultsize;
    }

    /**
     * Returns the dimenmap.
     * @return Returns the dimenmap.
     */
    public Map getDimenmap() {

        return dimenmap;
    }

    /**
     * Returns the filename.
     * @return Returns the filename.
     */
    public String getFilename() {

        return filename;
    }

    /**
     * Returns the fontfamily.
     * @return Returns the fontfamily.
     */
    public String getFontfamily() {

        return fontfamily;
    }

    /**
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * Returns the glyphmap.
     * @return Returns the glyphmap.
     */
    public Map getGlyphmap() {

        return glyphmap;
    }

    /**
     * Returns the id.
     * @return Returns the id.
     */
    public String getId() {

        return id;
    }

    /**
     * Returns the subtype.
     * @return Returns the subtype.
     */
    public String getSubtype() {

        return subtype;
    }

    /**
     * Returns the type.
     * @return Returns the type.
     */
    public String getType() {

        return type;
    }

    /**
     * Returns the unitsperem.
     * @return Returns the unitsperem.
     */
    public int getUnitsperem() {

        return unitsperem;
    }

}
