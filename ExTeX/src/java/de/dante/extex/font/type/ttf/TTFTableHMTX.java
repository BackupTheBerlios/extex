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
 * @version $Revision: 1.1 $
 */
public class TTFTableHMTX implements TTFTable {

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
     * @param de        entray
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableHMTX(final TableDirectory.Entry de, final RandomAccessR rar)
            throws IOException {

        rar.seek(de.getOffset());
        buf = new byte[de.getLength()];
        rar.readFully(buf); // mgn rar.read(buf)

        // buf !!!
    }

    /**
     * init
     * @param numberOfHMetrics  number of horizontal metrics
     * @param lsbCount  lsb count
     */
    public void init(final int numberOfHMetrics, final int lsbCount) {

        if (buf == null) {
            return;
        }
        hMetrics = new int[numberOfHMetrics];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        for (int i = 0; i < numberOfHMetrics; i++) {
            hMetrics[i] = (bais.read() << 24 | bais.read() << 16
                    | bais.read() << 8 | bais.read());
        }
        if (lsbCount > 0) {
            leftSideBearing = new short[lsbCount];
            for (int i = 0; i < lsbCount; i++) {
                leftSideBearing[i] = (short) (bais.read() << 8 | bais.read());
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
            return hMetrics[i] >> 16;
        } else {
            return hMetrics[hMetrics.length - 1] >> 16;
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
            return (short) (hMetrics[i] & 0xffff);
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
}