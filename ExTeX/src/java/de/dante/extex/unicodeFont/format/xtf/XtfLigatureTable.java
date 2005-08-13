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

import java.io.IOException;

import de.dante.util.file.random.RandomAccessR;

/**
 * LookupTable for a ligature
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public abstract class XtfLigatureTable extends XtfLookupTable {

    /**
     * format 1
     */
    private static final int FORMAT1 = 1;

    // TODO other formats!

    /**
     * Create a new object.
     * @param format    the format
     */
    XtfLigatureTable(final int format) {

        super(format);

    }

    /**
     * Create a new instance.
     * @param rar       the input
     * @param offset    the offset
     * @return Returns the new ligaturetable
     * @throws IOException if an IO-error occurs.
     */
    static XtfLigatureTable newInstance(final RandomAccessR rar, final int offset)
            throws IOException {

        XtfLigatureTable ls = null;
        rar.seek(offset);
        int format = rar.readUnsignedShort();
        if (format == FORMAT1) {
            ls = new LigatureTableFormat1(rar, offset);
        }
        return ls;
    }

    /**
     * Table for format1
     */
    public static class LigatureTableFormat1 extends XtfLigatureTable {

        /**
         * coverageOffset
         */
        private int coverageOffset;

        /**
         * ligSetCount
         */
        private int ligSetCount;

        /**
         * ligatureSetOffsets
         */
        private int[] ligatureSetOffsets;

        /**
         * coverage
         */
        private XtfCoverage coverage;

        /**
         * ligatureSets
         */
        private LigatureSet[] ligatureSets;

        /**
         * Create a new object.
         * @param rar       the input
         * @param offset    the offset
         * @throws IOException if an IO-error occurs
         */
        LigatureTableFormat1(final RandomAccessR rar, final int offset)
                throws IOException {

            super(FORMAT1);
            coverageOffset = rar.readUnsignedShort();
            ligSetCount = rar.readUnsignedShort();
            ligatureSetOffsets = new int[ligSetCount];
            ligatureSets = new LigatureSet[ligSetCount];
            for (int i = 0; i < ligSetCount; i++) {
                ligatureSetOffsets[i] = rar.readUnsignedShort();
            }
            rar.seek(offset + coverageOffset);
            coverage = XtfCoverage.newInstance(rar);
            for (int i = 0; i < ligSetCount; i++) {
                ligatureSets[i] = new LigatureSet(rar, offset
                        + ligatureSetOffsets[i]);
            }
        }

        /**
         * Returns the coverage.
         * @return Returns the coverage.
         */
        public XtfCoverage getCoverage() {

            return coverage;
        }

        /**
         * Returns the coverageOffset.
         * @return Returns the coverageOffset.
         */
        public int getCoverageOffset() {

            return coverageOffset;
        }

        /**
         * Returns the ligatureSetOffsets.
         * @return Returns the ligatureSetOffsets.
         */
        public int[] getLigatureSetOffsets() {

            return ligatureSetOffsets;
        }

        /**
         * Returns the ligatureSets.
         * @return Returns the ligatureSets.
         */
        public LigatureSet[] getLigatureSets() {

            return ligatureSets;
        }

        /**
         * Returns the ligSetCount.
         * @return Returns the ligSetCount.
         */
        public int getLigSetCount() {

            return ligSetCount;
        }
    }

    /**
     * ligature set
     */
    public class LigatureSet {

        /**
         * ligatureCount
         */
        private int ligatureCount;

        /**
         * ligatureOffsets
         */
        private int[] ligatureOffsets;

        /**
         * ligatures
         */
        private Ligature[] ligatures;

        /**
         * Create a new object.
         *
         * @param rar       the input
         * @param offset    the offset
         * @throws IOException if an IO-error occurs
         */
        LigatureSet(final RandomAccessR rar, final int offset)
                throws IOException {

            rar.seek(offset);
            ligatureCount = rar.readUnsignedShort();
            ligatureOffsets = new int[ligatureCount];
            ligatures = new Ligature[ligatureCount];
            for (int i = 0; i < ligatureCount; i++) {
                ligatureOffsets[i] = rar.readUnsignedShort();
            }
            for (int i = 0; i < ligatureCount; i++) {
                rar.seek(offset + ligatureOffsets[i]);
                ligatures[i] = new Ligature(rar);
            }
        }

        /**
         * Returns the ligatureCount.
         * @return Returns the ligatureCount.
         */
        public int getLigatureCount() {

            return ligatureCount;
        }

        /**
         * Returns the ligatureOffsets.
         * @return Returns the ligatureOffsets.
         */
        public int[] getLigatureOffsets() {

            return ligatureOffsets;
        }

        /**
         * Returns the ligatures.
         * @return Returns the ligatures.
         */
        public Ligature[] getLigatures() {

            return ligatures;
        }
    }

    /**
     * ligature
     */
    public class Ligature {

        /**
         * ligGlyph
         */
        private int ligGlyph;

        /**
         * compCount
         */
        private int compCount;

        /**
         * components
         */
        private int[] components;

        /**
         * Create a new object.
         *
         * @param rar       the input
         * @throws IOException if an IO-error occurs
         */
        Ligature(final RandomAccessR rar) throws IOException {

            ligGlyph = rar.readUnsignedShort();
            compCount = rar.readUnsignedShort();
            components = new int[compCount - 1];
            for (int i = 0; i < compCount - 1; i++) {
                components[i] = rar.readUnsignedShort();
            }
        }

        /**
         * Returns the glyph count.
         * @return Returns the glyph count.
         */
        public int getGlyphCount() {

            return compCount;
        }

        /**
         * Returns the glyph id.
         * @param i the index
         * @return Returns the glyph id.
         */
        public int getGlyphId(final int i) {

            return (i == 0) ? ligGlyph : components[i - 1];
        }
    }

}