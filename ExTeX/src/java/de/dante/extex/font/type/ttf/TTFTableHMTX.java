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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'hmtx' table contains metric information for the
 * horizontal layout each of the glyphs in the font.
 *
 * <table BORDER="1">
 *   <tbody>
 *     <tr><td><b>Field</b></td><td><b>Type</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>hMetrics</td><td>longHorMetric[numberOfHMetrics]</td><td>
 *          Paired advance width and left side bearing values
 *          for each glyph. The value numOfHMetrics comes from the &lsquo;hhea&rsquo;
 *          table. If the font is monospaced, only one entry need be in the
 *          array, but that entry is required. The last entry applies to all
 *          subsequent glyphs.</td></tr>
 *   <tr><td>leftSideBearing</td><td>FWord[ ]</td><td>
 *          Here the advanceWidth is assumed to be the same as
 *          the advanceWidth for the last entry above. The number of entries
 *          in this array is derived from numGlyphs (from &lsquo;maxp&rsquo;
 *          table) minus numberOfHMetrics. This generally is used with a run
 *          of monospaced glyphs (e.g., Kanji fonts or Courier fonts). Only
 *          one run is allowed and it must be at the end. This allows a
 *          monospaced font to vary the left side bearing values for each
 *          glyph.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class TTFTableHMTX extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * buffer
     */
    private byte[] buf = null;

    /**
     * horizontal metricx
     */
    private int[] hMetrics = null;

    /**
     * leftsidebearing
     */
    private short[] leftSideBearing = null;

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entray
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableHMTX(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        buf = new byte[de.getLength()];
        rar.readFully(buf);

        // buf !!!
    }

    /**
     * length of hmetrics
     */
    private int hMetricslength;

    /**
     * length of lsb
     */
    private int lsblength;

    /**
     * @see de.dante.extex.font.type.ttf.TTFTable#getInitOrder()
     */
    public int getInitOrder() {

        return 1;
    }

    /**
     * @see de.dante.extex.font.type.ttf.TTFTable#init()
     */
    public void init() {

        TTFTableHHEA hhea = (TTFTableHHEA) getTableMap().get(TTFFont.HHEA);
        TTFTableMAXP maxp = (TTFTableMAXP) getTableMap().get(TTFFont.MAXP);
        if (hhea == null || maxp == null) {
            return;
        }

        int numberOfHMetrics = hhea.getNumberOfHMetrics();
        int lsbCount = maxp.getNumGlyphs() - hhea.getNumberOfHMetrics();

        if (buf == null) {
            return;
        }

        hMetricslength = numberOfHMetrics;
        lsblength = lsbCount;

        hMetrics = new int[numberOfHMetrics];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        for (int i = 0; i < numberOfHMetrics; i++) {
            hMetrics[i] = (bais.read() << TTFConstants.SHIFT24
                    | bais.read() << TTFConstants.SHIFT16
                    | bais.read() << TTFConstants.SHIFT8 | bais.read());
        }
        if (lsbCount > 0) {
            leftSideBearing = new short[lsbCount];
            for (int i = 0; i < lsbCount; i++) {
                leftSideBearing[i] = (short) (bais.read() << TTFConstants.SHIFT8 | bais
                        .read());
            }
        }
        buf = null;
    }

    /**
     * Returns the advanced width
     * @param i index
     * @return Returns the advanced width
     */
    public int getAdvanceWidth(final int i) {

        if (hMetrics == null) {
            return 0;
        }
        if (i < hMetrics.length) {
            return hMetrics[i] >> TTFConstants.SHIFT16;
        } else {
            return hMetrics[hMetrics.length - 1] >> TTFConstants.SHIFT16;
        }
    }

    /**
     * Return the left side bearing
     * @param i index
     * @return Return the left side bearing
     */
    public short getLeftSideBearing(final int i) {

        if (hMetrics == null) {
            return 0;
        }
        if (i < hMetrics.length) {
            return (short) (hMetrics[i] & TTFConstants.CONSTXFFFF);
        } else {
            return leftSideBearing[i - hMetrics.length];
        }
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.HMTX;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("hmtx");
        table.setAttribute("id", TTFFont.convertIntToHexString(getType()));
        Element hmetrics = new Element("hmetrics");
        table.addContent(hmetrics);
        for (int i = 0; i < hMetricslength; i++) {
            Element hmetric = new Element("hmetric");
            hmetric.setAttribute("id", String.valueOf(i));
            hmetric.setAttribute("value", String.valueOf(hMetrics[i]));
            hmetrics.addContent(hmetric);
        }
        Element lsbs = new Element("leftsidebearing");
        table.addContent(lsbs);
        for (int i = 0; i < lsblength; i++) {
            Element lsb = new Element("lsb");
            lsb.setAttribute("id", String.valueOf(i));
            lsb.setAttribute("value", String.valueOf(leftSideBearing[i]));
            lsbs.addContent(lsb);
        }
        TTFTableMAXP maxp = (TTFTableMAXP) getTableMap().get(TTFFont.MAXP);
        TTFTablePOST post = (TTFTablePOST) getTableMap().get(TTFFont.POST);
        TTFTableGLYF glyf = (TTFTableGLYF) getTableMap().get(TTFFont.GLYF);

        if (maxp != null && post != null && glyf != null) {
            Element glyphs = new Element("glyphs");
            table.addContent(glyphs);
            int numGlyphs = maxp.getNumGlyphs();
            for (int i = 0; i < numGlyphs; i++) {
                Element glyph = new Element("glyph");
                glyphs.addContent(glyph);
                glyph.setAttribute("id", String.valueOf(i));
                int aw = getAdvanceWidth(i);
                glyph.setAttribute("width", String.valueOf(aw));
                int lsb = getLeftSideBearing(i);
                glyph.setAttribute("lsb", String.valueOf(lsb));
                glyph.setAttribute("name", post.getGlyphName(i));

                // For any glyph, xmax and xmin are given in glyf table,
                // lsb and aw are given in hmtx table.
                // rsb is calculated as follows: rsb = aw - (lsb + xmax - xmin)
                TTFTableGLYF.Descript des = glyf.getDescription(i);
                if (des != null) {
                    int xmax = des.getXMax();
                    int xmin = des.getXMin();
                    int rsb = aw - (lsb + xmax - xmin);
                    glyph.setAttribute("xmax", String.valueOf(xmax));
                    glyph.setAttribute("xmin", String.valueOf(xmin));
                    glyph.setAttribute("rsb", String.valueOf(rsb));

                    // If pp1 and pp2 are phantom points used to control lsb
                    // and rsb, their initial position in x is calculated
                    // as follows:
                    // pp1 = xmin - lsb
                    // pp2 = pp1 + aw
                    int pp1 = xmin - lsb;
                    int pp2 = pp1 + aw;
                    glyph.setAttribute("pp1", String.valueOf(pp1));
                    glyph.setAttribute("pp2", String.valueOf(pp2));
                }
            }
        }
        return table;
    }
}