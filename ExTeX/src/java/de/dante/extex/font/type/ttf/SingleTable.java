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

import java.io.IOException;

import de.dante.util.file.random.RandomAccessR;

/**
 * SingleTable
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public abstract class SingleTable extends LookupTable {

    /**
     * format 1
     */
    private static final int FORMAT1 = 1;

    /**
     * format 2
     */
    private static final int FORMAT2 = 2;

    /**
     * Create a new object.
     * @param format    the format
     */
    SingleTable(final int format) {

        super(format);

    }

    /**
     * Retunrs the subsitute.
     * @param glyphId   hte glyph id
     * @return Retunrs the subsitute.
     */
    public abstract int substitute(final int glyphId);

    /**
     * Create a new Instance.
     * @param rar       the input
     * @param offset    the offset
     * @return Returns the new instance.
     * @throws IOException if an IO-error occurs
     */
    public static SingleTable newInstance(final RandomAccessR rar,
            final int offset) throws IOException {

        SingleTable s = null;
        rar.seek(offset);
        int format = rar.readUnsignedShort();
        if (format == 1) {
            s = new SingleTableFormat1(rar, offset);
        } else if (format == 2) {
            s = new SingleTableFormat2(rar, offset);
        }
        return s;
    }

    /**
     * SingleTable for format 1
     */
    public static class SingleTableFormat1 extends SingleTable {

        /**
         * coverageOffset
         */
        private int coverageOffset;

        /**
         * deltaGlyphID
         */
        private short deltaGlyphID;

        /**
         * coverage
         */
        private Coverage coverage;

        /**
         * Create a new object.
         * @param rar       the input
         * @param offset    the offset
         * @throws IOException if an IO_error occurs
         */
        SingleTableFormat1(final RandomAccessR rar, final int offset)
                throws IOException {

            super(FORMAT1);
            coverageOffset = rar.readUnsignedShort();
            deltaGlyphID = rar.readShort();
            rar.seek(offset + coverageOffset);
            coverage = Coverage.newInstance(rar);
        }

        /**
         * @see de.dante.extex.font.type.ttf.SingleTable#substitute(int)
         */
        public int substitute(final int glyphId) {

            int i = coverage.findGlyph(glyphId);
            if (i > -1) {
                return glyphId + deltaGlyphID;
            }
            return glyphId;
        }
    }

    /**
     * SingleTable for format 2
     */
    public static class SingleTableFormat2 extends SingleTable {

        /**
         * coverageOffset
         */
        private int coverageOffset;

        /**
         * glyphCount
         */
        private int glyphCount;

        /**
         * substitutes
         */
        private int[] substitutes;

        /**
         * coverage
         */
        private Coverage coverage;

        /**
         * Create a new object.
         * @param rar       the input
         * @param offset    the offset
         * @throws IOException if an IO-error occurs
         */
        SingleTableFormat2(final RandomAccessR rar, final int offset)
                throws IOException {

            super(FORMAT2);
            coverageOffset = rar.readUnsignedShort();
            glyphCount = rar.readUnsignedShort();
            substitutes = new int[glyphCount];
            for (int i = 0; i < glyphCount; i++) {
                substitutes[i] = rar.readUnsignedShort();
            }
            rar.seek(offset + coverageOffset);
            coverage = Coverage.newInstance(rar);
        }

        /**
         * @see de.dante.extex.font.type.ttf.SingleTable#substitute(int)
         */
        public int substitute(final int glyphId) {

            int i = coverage.findGlyph(glyphId);
            if (i > -1) {
                return substitutes[i];
            }
            return glyphId;
        }
    }
}

