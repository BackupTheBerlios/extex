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
 * Abstract class for all coverage
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public abstract class XtfCoverage {

    /**
     * format 1
     */
    static final int FORMAT1 = 1;

    /**
     * format 2
     */
    static final int FORMAT2 = 2;

    /**
     * format
     */
    private int format;

    /**
     * Create a new onject
     *
     * @param fm the format
     */
    XtfCoverage(final int fm) {

        format = fm;
    }

    /**
     * Returns the index of the glyph within the coverage
     *
     * @param glyphId The ID of the glyph to find.
     * @return Returns the index of the glyph within the coverage, or -1 if the glyph
     * can't be found.
     */
    public abstract int findGlyph(final int glyphId);

    /**
     * Create a new instance and read the coverage
     *
     * @param rar   input
     * @return Returns the new coverage
     * @throws IOException if an IO-error occrs
     */
    static XtfCoverage newInstance(final RandomAccessR rar) throws IOException {

        XtfCoverage c = null;
        int format = rar.readUnsignedShort();
        if (format == FORMAT1) {
            c = new CoverageFormat1(rar);
        } else if (format == FORMAT2) {
            c = new CoverageFormat2(rar);
        }
        return c;
    }

    /**
     * Returns the format
     * @return Returns the format
     */
    public int getFormat() {

        return format;
    }

    /**
     * Coverage for FORMAT1
     */
    public static class CoverageFormat1 extends XtfCoverage {

        /**
         * glyph count
         */
        private int glyphCount;

        /**
         * glyph ids
         */
        private int[] glyphIds;

        /**
         * Create a new object
         *
         * @param rar   input
         * @throws IOException if an IO-error occurs
         */
        CoverageFormat1(final RandomAccessR rar) throws IOException {

            super(XtfCoverage.FORMAT1);

            glyphCount = rar.readUnsignedShort();
            glyphIds = new int[glyphCount];
            for (int i = 0; i < glyphCount; i++) {
                glyphIds[i] = rar.readUnsignedShort();
            }
        }

        /**
         * @see de.dante.extex.font.type.ttf.Coverage#findGlyph(int)
         */
        public int findGlyph(final int glyphId) {

            for (int i = 0; i < glyphCount; i++) {
                if (glyphIds[i] == glyphId) {
                    return i;
                }
            }
            return -1;
        }

    }

    /**
     * Coverage for FORMAT2
     */
    public static class CoverageFormat2 extends XtfCoverage {

        /**
         * range count
         */
        private int rangeCount;

        /**
         * records
         */
        private RangeRecord[] rangeRecords;

        /**
         * Create a new object
         *
         * @param rar   input
         * @throws IOException if an IO-error occurs
         */
        CoverageFormat2(final RandomAccessR rar) throws IOException {

            super(XtfCoverage.FORMAT2);

            rangeCount = rar.readUnsignedShort();
            rangeRecords = new RangeRecord[rangeCount];
            for (int i = 0; i < rangeCount; i++) {
                rangeRecords[i] = new RangeRecord(rar);
            }
        }

        /**
         * @see de.dante.extex.font.type.ttf.Coverage#findGlyph(int)
         */
        public int findGlyph(final int glyphId) {

            for (int i = 0; i < rangeCount; i++) {
                int n = rangeRecords[i].getCoverageIndex(glyphId);
                if (n > -1) {
                    return n;
                }
            }
            return -1;
        }

    }

    /**
     * Coverage Index (GlyphID) = StartCoverageIndex + GlyphID - Start GlyphID
     */
    public class RangeRecord {

        /**
         * start
         */
        private int start;

        /**
         * end
         */
        private int end;

        /**
         * start index
         */
        private int startCoverageIndex;

        /**
         * Create anew object
         * @param rar       the input
         * @throws IOException if an IO-error occurs
         */
        RangeRecord(final RandomAccessR rar) throws IOException {

            start = rar.readUnsignedShort();
            end = rar.readUnsignedShort();
            startCoverageIndex = rar.readUnsignedShort();
        }

        /**
         * Check, if the glyph id is in the range.
         * @param glyphId   the glyph id
         * @return Check, if the glyph id is in the range.
         */
        public boolean isInRange(final int glyphId) {

            return (start <= glyphId && glyphId <= end);
        }

        /**
         * Returns the coverage index
         * @param glyphId   the glyph id
         * @return Returns the coverage index
         */
        public int getCoverageIndex(final int glyphId) {

            if (isInRange(glyphId)) {
                return startCoverageIndex + glyphId - start;
            }
            return -1;
        }
    }
}