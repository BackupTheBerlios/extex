/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

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
 * @version $Revision: 1.1 $
 */
public class TtfTableHMTX extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible {

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
    TtfTableHMTX(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
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

        TtfTableHHEA hhea = (TtfTableHHEA) getTableMap().get(XtfReader.HHEA);
        TtfTableMAXP maxp = (TtfTableMAXP) getTableMap().get(XtfReader.MAXP);
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
            hMetrics[i] = (bais.read() << XtfConstants.SHIFT24
                    | bais.read() << XtfConstants.SHIFT16
                    | bais.read() << XtfConstants.SHIFT8 | bais.read());
        }
        if (lsbCount > 0) {
            leftSideBearing = new short[lsbCount];
            for (int i = 0; i < lsbCount; i++) {
                leftSideBearing[i] = (short) (bais.read() << XtfConstants.SHIFT8 | bais
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
            return hMetrics[i] >> XtfConstants.SHIFT16;
        }
        return hMetrics[hMetrics.length - 1] >> XtfConstants.SHIFT16;

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
            return (short) (hMetrics[i] & XtfConstants.CONSTXFFFF);
        }
        return leftSideBearing[i - hMetrics.length];

    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return XtfReader.HMTX;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "hmtx";
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeStartElement("hmetrics");
        for (int i = 0; i < hMetricslength; i++) {
            writer.writeStartElement("hmetric");
            writer.writeAttribute("id", String.valueOf(i));
            writer.writeAttribute("value", String.valueOf(hMetrics[i]));
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeStartElement("leftsidebearing");
        for (int i = 0; i < lsblength; i++) {
            writer.writeStartElement("lsb");
            writer.writeAttribute("id", String.valueOf(i));
            writer.writeAttribute("value", String.valueOf(leftSideBearing[i]));
            writer.writeEndElement();
        }
        writer.writeEndElement();
        TtfTableMAXP maxp = (TtfTableMAXP) getTableMap().get(XtfReader.MAXP);
        TtfTablePOST post = (TtfTablePOST) getTableMap().get(XtfReader.POST);
        TtfTableGLYF glyf = (TtfTableGLYF) getTableMap().get(XtfReader.GLYF);

        if (maxp != null && post != null && glyf != null) {
            writer.writeStartElement("glyphs");
            int numGlyphs = maxp.getNumGlyphs();
            for (int i = 0; i < numGlyphs; i++) {
                writer.writeStartElement("glyph");
                writer.writeAttribute("id", String.valueOf(i));
                int aw = getAdvanceWidth(i);
                writer.writeAttribute("width", String.valueOf(aw));
                int lsb = getLeftSideBearing(i);
                writer.writeAttribute("lsb", String.valueOf(lsb));
                writer.writeAttribute("name", post.getGlyphName(i));

                // For any glyph, xmax and xmin are given in glyf table,
                // lsb and aw are given in hmtx table.
                // rsb is calculated as follows: rsb = aw - (lsb + xmax - xmin)
                TtfTableGLYF.Descript des = glyf.getDescription(i);
                if (des != null) {
                    int xmax = des.getXMax();
                    int xmin = des.getXMin();
                    int rsb = aw - (lsb + xmax - xmin);
                    writer.writeAttribute("xmax", String.valueOf(xmax));
                    writer.writeAttribute("xmin", String.valueOf(xmin));
                    writer.writeAttribute("rsb", String.valueOf(rsb));

                    // If pp1 and pp2 are phantom points used to control lsb
                    // and rsb, their initial position in x is calculated
                    // as follows:
                    // pp1 = xmin - lsb
                    // pp2 = pp1 + aw
                    int pp1 = xmin - lsb;
                    int pp2 = pp1 + aw;
                    writer.writeAttribute("pp1", String.valueOf(pp1));
                    writer.writeAttribute("pp2", String.valueOf(pp2));
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}