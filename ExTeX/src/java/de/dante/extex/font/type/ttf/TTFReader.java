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

package de.dante.extex.font.type.ttf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.jdom.Element;

import de.dante.extex.font.Kerning;
import de.dante.extex.font.type.FontMetric;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.util.GeneralException;
import de.dante.util.file.random.RandomAccessInputStream;

/**
 * This class read a TTF-file.
 * <p>
 * For more information use <tt>TrueType 1.0 Font Files</tt>
 * Technical Specification, Revision 1.66, November 1995
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TTFReader implements FontMetric {

    /**
     * Create a new object.
     *
     * @param instream  inputstream for reading
     * @param fontname  the fontname
     * @throws IOException if an IO-error occurs
     * @throws GeneralException if a ttf-table does not exists
     */
    public TTFReader(final InputStream instream, final String fontname)
            throws IOException, GeneralException {

        fontid = fontname;

        RandomAccessInputStream in = new RandomAccessInputStream(instream);

        TTFFont font = new TTFFont(in);

        createEfmElement(font);
    }

    /**
     * fontid
     */
    private String fontid;

    /**
     * max glyphs
     */
    private static final int MAXGLYPHS = 0xffff;

    /**
     * Create the efm-element
     * @param  ttffont    the TTF-Font
     * @throws IOException ...
     */
    private void createEfmElement(final TTFFont ttffont) throws IOException {

        efmelement = new Element("fontgroup");

        String fontFamily = ttffont.getFontFamilyName();
        short unitsPerEm = ttffont.getHeadTable().getUnitsPerEm();
        //        String panose = ttffont.getOS2Table().getPanose().toString();
        //        int ascent = ttffont.getAscent();
        //        int descent = ttffont.getDescent();
        //        //int baseline = 0; // bit 0 of head.flags will indicate if this is true
        //
        efmelement.setAttribute("name", fontFamily);
        efmelement.setAttribute("id", fontid);
        efmelement.setAttribute("default-size", "12");
        efmelement.setAttribute("empr", "100");
        efmelement.setAttribute("units-per-em", String.valueOf(unitsPerEm));

        // int horiz_advance_x = font.getOS2Table().getAvgCharWidth();

        Element font = new Element("font");
        efmelement.addContent(font);

        font.setAttribute("font-name", fontid);
        font.setAttribute("font-family", fontFamily);
        font.setAttribute("type", "ttf");

        // Use the Unicode cmap encoding
        TTFTableCMAP.Format cmapFmt = ttffont.getCmapTable().getFormat(
                TTFTableCMAP.PLATFORM_MICROSOFT, TTFTableCMAP.ENCODING_UGL);
        if (cmapFmt == null) {
            // Symbol font -> "undefined" encoding
            cmapFmt = ttffont.getCmapTable().getFormat(
                    TTFTableCMAP.PLATFORM_MICROSOFT,
                    TTFTableCMAP.ENCODING_UNDEFINED);
        }
        if (cmapFmt == null) {
            throw new IOException("Cannot find a suitable cmap table");
        }

        // Output kerning pairs from the requested range
        TTFTableKERN kern = (TTFTableKERN) ttffont.getTable(TTFFont.KERN);
        ArrayList kernlist = new ArrayList();
        if (kern != null) {
            TTFTableKERN.KernSubtable kst = kern.getTable(0);
            TTFTablePOST posttable = ttffont.getPostTable();
            for (int i = 0; i < kst.getKerningCount(); i++) {

                Kerning kp = new Kerning();
                kp.setIdleft(String.valueOf(kst.getKerning(i).getLeft()));
                kp.setNameleft(String.valueOf(posttable.getGlyphName(kst
                        .getKerning(i).getLeft())));
                kp.setId(String.valueOf(kst.getKerning(i).getRight()));
                kp.setName(String.valueOf(posttable.getGlyphName(kst
                        .getKerning(i).getRight())));
                // SVG kerning values are inverted from TrueType's.
                kp.setSize(new Dimen(-kst.getKerning(i).getValue()));
                kernlist.add(kp);
            }
        }

        // get all glyphs
        for (int i = 0; i <= MAXGLYPHS; i++) {

            int glyphIndex = cmapFmt.mapCharCode(i);

            if (glyphIndex > 0) {

                Element glyph = new Element("glyph");
                font.addContent(glyph);

                glyph.setAttribute("ID", String.valueOf(i));
                glyph.setAttribute("glyph-number", String.valueOf(glyphIndex));
                glyph.setAttribute("glyph-name", ttffont.getPostTable()
                        .getGlyphName(glyphIndex));

                TTFTableGLYF.Descript gd = ((TTFTableGLYF) ttffont
                        .getTable(TTFFont.GLYF)).getDescription(glyphIndex);

                if (gd != null) {
                    glyph.setAttribute("width", String.valueOf(gd.getXMin()
                            + gd.getXMax()));
                    if (gd.getYMin() < 0) {
                        glyph.setAttribute("depth", String.valueOf(-gd
                                .getYMin()));
                    } else {
                        glyph.setAttribute("depth", "0");
                    }
                    if (gd.getYMax() > 0) {
                        glyph.setAttribute("height", String.valueOf(gd
                                .getYMax()));
                    } else {
                        glyph.setAttribute("height", "0");
                    }
                }

                // kerning
                for (int k = 0; k < kernlist.size(); k++) {
                    Kerning kp = (Kerning) kernlist.get(k);
                    int idleft = Integer.parseInt(kp.getIdleft());
                    if (idleft == glyphIndex) {
                        Element kerning = new Element("kerning");
                        glyph.addContent(kerning);
                        int val = ttffont.getPostTable().getGlyphValue(
                                Integer.parseInt(kp.getId()));
                        kerning.setAttribute("glyph-id", String.valueOf(val));
                        //kerning.setAttribute("glyph-number", String.valueOf(kp.idpost));
                        kerning.setAttribute("glyph-name", kp.getName());
                        kerning.setAttribute("size", String.valueOf(kp
                                .getSize().getValue()));
                    }
                }
            }

        }

    }

    /**
     * efm-element
     */
    private Element efmelement;

    /**
     * @see de.dante.extex.font.type.FontMetric#getFontMetric()
     */
    public Element getFontMetric() {

        return efmelement;
    }

    /**
     * only for test
     * @param args co   commandline
     * @throws Exception if an error occured
     */
    public static void main(final String[] args) throws Exception {

        InputStream in = new FileInputStream("src/font/Gara.ttf");

        new TTFReader(in, "Gara");
    }
}