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

package de.dante.extex.unicodeFont.format.efm;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.unicode.Unicode;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmCharInfoArray;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmCharInfoWord;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmFixWord;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmKerning;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmLigKern;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmLigature;
import de.dante.extex.unicodeFont.format.tex.tfm.TfmReader;
import de.dante.extex.unicodeFont.glyphname.GlyphName;
import de.dante.util.UnicodeChar;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class for the efm metric.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EfmMetric {

    /**
     * The attribute <code>italic</code>.
     */
    private static final String ATT_ITALIC = "italic";

    /**
     * The attribute <code>height</code>.
     */
    private static final String TAG_HEIGHT = "height";

    /**
     * The attribute <code>depth</code>.
     */
    private static final String ATT_DEPTH = "depth";

    /**
     * The attribute <code>width</code>.
     */
    private static final String ATT_WIDTH = "width";

    /**
     * The attribute <code>type</code>.
     */
    private static final String ATT_TYPE = "type";

    /**
     * The tag <code>whdi</code>.
     */
    private static final String TAG_WHDI = "whdi";

    /**
     * The attribute <code>glyphname</code>.
     */
    private static final String ATT_GLPYHNAME = "glpyhname";

    /**
     * The attribute <code>alias</code>.
     */
    private static final String ATT_ALIAS = "alias";

    /**
     * The attribute <code>uchex</code>.
     */
    private static final String ATT_UCHEX = "uchex";

    /**
     * The attribute <code>unicode</code>
     */
    private static final String ATT_UNICODE = "unicode";

    /**
     * The element <code>glpyh</code>
     */
    private static final String TAG_GLYPH = "glyph";

    /**
     * The tag <code>metric</code>.
     */
    private static final String TAG_METRIC = "metric";

    /**
     * The map with the metrics.
     */
    private Map metricMap;

    /**
     * Create a new object.
     *
     * @param tfm   The tfm metric.
     */
    public EfmMetric(final TfmReader tfm) {

        metricMap = new HashMap();

        TfmCharInfoArray charinfo = tfm.getCharinfo();
        TfmCharInfoWord[] charinfoword = charinfo.getCharinfoword();

        for (int i = 0; i < charinfoword.length; i++) {

            // get char
            TfmCharInfoWord ci = charinfoword[i];

            if (ci != null) {

                EfmGlyph glyph = new EfmGlyph();

                UnicodeChar uc = new UnicodeChar(Unicode.OFFSET + i);
                glyph.setUc(uc);
                metricMap.put(glyph.getUc(), glyph);

                glyph.setGlyphNumber(i + charinfo.getBc());
                if (ci.getGlyphname() != null) {
                    glyph.setGlyphName(ci.getGlyphname().replaceAll("/", ""));
                }
                TfmEfmWHDI m = new TfmEfmWHDI();
                m.setWidth(ci.getWidth());
                m.setHeight(ci.getHeight());
                m.setDepth(ci.getDepth());
                m.setItalic(ci.getItalic());
                glyph.setWhdi(m);

                // ligature
                int ligstart = ci.getLigkernstart();
                TfmLigKern[] ligKernTable = tfm.getLigkern().getLigKernTable();

                if (ligstart != TfmCharInfoWord.NOINDEX) {

                    for (int k = ligstart; k != TfmCharInfoWord.NOINDEX; k = ligKernTable[k]
                            .nextIndex(k)) {
                        TfmLigKern lk = ligKernTable[k];

                        if (lk instanceof TfmLigature) {
                            TfmLigature lig = (TfmLigature) lk;

                            EfmLigature ligature = new EfmLigature();
                            ligature.setLetterUc(new UnicodeChar(Unicode.OFFSET
                                    + lig.getNextChar()));
                            ligature.setLigUc(new UnicodeChar(Unicode.OFFSET
                                    + lig.getAddingChar()));

                            glyph.addLigature(ligature);

                        } else if (lk instanceof TfmKerning) {
                            TfmKerning kern = (TfmKerning) lk;

                            TfmEfmKerning kerning = new TfmEfmKerning();

                            kerning.setUnicodeChar(new UnicodeChar(
                                    Unicode.OFFSET + kern.getNextChar()));
                            kerning.setSize(kern.getKern());
                            glyph.addKerning(kerning);
                        }
                    }
                }
            }
        }

    }

    /**
     * The kerning for e tfm metric.
     */
    private class TfmEfmKerning extends EfmAbstractKerning {

        /**
         * The kerning size.
         */
        private TfmFixWord size = TfmFixWord.ZERO;

        /**
         * Create a new object.
         */
        public TfmEfmKerning() {

            super();
        }

        /**
         * Set the size.
         * @param asize The size to set.
         */
        public void setSize(final TfmFixWord asize) {

            size = asize;
        }

    }

    /**
     * The WHDI for a tfm metric.
     */
    private class TfmEfmWHDI implements EfmWHDI {

        /**
         * Character width.
         */
        private TfmFixWord width = TfmFixWord.ZERO;

        /**
         * Character height.
         */
        private TfmFixWord height = TfmFixWord.ZERO;

        /**
         * Character depth.
         */
        private TfmFixWord depth = TfmFixWord.ZERO;

        /**
         * Character italic correction.
         */
        private TfmFixWord italic = TfmFixWord.ZERO;

        /**
         * Create a new object.
         */
        public TfmEfmWHDI() {

            super();
        }

        /**
         * Set the depth.
         * @param adepth The depth to set.
         */
        public void setDepth(final TfmFixWord adepth) {

            depth = adepth;
        }

        /**
         * Set the height.
         * @param aheight The height to set.
         */
        public void setHeight(final TfmFixWord aheight) {

            height = aheight;
        }

        /**
         * Set the italic.
         * @param aitalic The italic to set.
         */
        public void setItalic(final TfmFixWord aitalic) {

            italic = aitalic;
        }

        /**
         * Set the width.
         * @param awidth The width to set.
         */
        public void setWidth(final TfmFixWord awidth) {

            width = awidth;
        }

        /**
         * @see de.dante.extex.unicodeFont.format.efm.EfmWHDI#write(
         *      de.dante.util.xml.XMLStreamWriter)
         */
        public void write(final XMLStreamWriter writer) throws IOException {

            writer.writeStartElement(TAG_WHDI);
            writer.writeAttribute(ATT_TYPE, "tfm");
            writer.writeAttribute(ATT_WIDTH, width.getValue());
            writer.writeAttribute(ATT_DEPTH, depth.getValue());
            writer.writeAttribute(TAG_HEIGHT, height.getValue());
            writer.writeAttribute(ATT_ITALIC, italic.getValue());
            writer.writeEndElement();
        }
    }

    /**
     * Write the data to the xml file.
     * @param writer    The writer.
     * @throws IOException if an IO-error occurred.
     */
    public void write(final XMLStreamWriter writer) throws IOException {

        GlyphName glyphName = GlyphName.getInstance();

        writer.writeStartElement(TAG_METRIC);

        UnicodeChar[] ucs = new UnicodeChar[metricMap.size()];
        ucs = (UnicodeChar[]) metricMap.keySet().toArray(ucs);
        Arrays.sort(ucs, new Comparator() {

            /**
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            public int compare(final Object o1, final Object o2) {

                UnicodeChar uc1 = (UnicodeChar) o1;
                UnicodeChar uc2 = (UnicodeChar) o2;
                if (uc1.getCodePoint() >= uc2.getCodePoint()) {
                    return 1;
                }
                return -1;
            }
        });
        for (int i = 0; i < ucs.length; i++) {
            EfmGlyph glyph = (EfmGlyph) metricMap.get(ucs[i]);

            // glyph in the private unicode area
            writer.writeStartElement(TAG_GLYPH);
            writer.writeAttribute(ATT_UNICODE, glyph.getUc().getCodePoint());
            writer.writeAttribute(ATT_UCHEX, "0x"
                    + Integer.toHexString(glyph.getUc().getCodePoint()));
            String name = glyph.getGlyphName();
            writer.writeAttribute(ATT_GLPYHNAME, name);

            // metric
            glyph.getWhdi().write(writer);

            writer.writeEndElement();

            // glyph in normal array (map with the glyphname)
            if (!"".equals(name)) {
                UnicodeChar uc = glyphName.getUnicode(name);
                if (uc != null) {
                    writer.writeStartElement(TAG_GLYPH);
                    writer.writeAttribute(ATT_UNICODE, uc.getCodePoint());
                    writer.writeAttribute(ATT_UCHEX, "0x"
                            + Integer.toHexString(uc.getCodePoint()));
                    writer.writeAttribute(ATT_ALIAS, glyph.getUc()
                            .getCodePoint());
                    writer.writeAttribute(ATT_GLPYHNAME, name);
                    writer.writeEndElement();
                }
            }
        }

        writer.writeEndElement();
    }
}
