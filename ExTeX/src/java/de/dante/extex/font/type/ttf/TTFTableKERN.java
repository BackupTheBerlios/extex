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

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'kern' table contains the values that adjust the
 * intercharacter spacing for glyphs in a font.
 *
 * <table border="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>fixed32</td><td>version</td><td>
 *          The version number of the kerning table
 *          (0x00010000 for the current version).</td></tr>
 *   <tr><td>uint32</td><td>nTables</td></td>
 *          The number of subtables included in the kerning table.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class TTFTableKERN extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * version
     */
    private int version;

    /**
     * number of tables
     */
    private int nTables;

    /**
     * kerntables
     */
    private KernSubtable[] tables;

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableKERN(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        version = rar.readUnsignedShort();
        nTables = rar.readUnsignedShort();
        tables = new KernSubtable[nTables];
        // read tables
        for (int i = 0; i < nTables; i++) {
            tables[i] = KernSubtable.read(rar);
        }
    }

    /**
     * Returns the number of tables.
     * @return Returns the number of tables.
     */
    public int getNTables() {

        return nTables;
    }

    /**
     * Returns the table
     * @param i index
     * @return Returns the table
     */
    public KernSubtable getTable(final int i) {

        return tables[i];
    }

    /**
     * Get the table type, as a table directory value.
     * @return The table type
     */
    public int getType() {

        return TTFFont.KERN;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("kern");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        table.setAttribute("version", String.valueOf(version));
        table.setAttribute("numberoftables", String.valueOf(nTables));
        for (int i = 0; i < tables.length; i++) {
            table.addContent(tables[i].toXML());
        }
        return table;
    }

    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------

    /**
     * Abstract class for all kerntables
     */
    public abstract static class KernSubtable {

        /**
         * Returns the number of kerning pairs.
         * @return Returns the number of kerning pairs.
         */
        public abstract int getKerningCount();

        /**
         * Returns the kerning
         * @param i index
         * @return  Returns the kerning
         */
        public abstract KerningPair getKerning(final int i);

        /**
         * format 0
         */
        private static final int FORMAT0 = 0;

        /**
         * format 2
         */
        private static final int FORMAT2 = 2;

        /**
         * Read the table
         * @param rar       input
         * @return Returns the table
         * @throws IOException if an IO-error occurs
         */
        public static KernSubtable read(final RandomAccessR rar)
                throws IOException {

            KernSubtable table = null;
            int version = rar.readUnsignedShort();
            int length = rar.readUnsignedShort();
            int coverage = rar.readUnsignedShort();
            int format = coverage >> 8;

            switch (format) {
                case FORMAT0 :
                    table = new KernSubtableFormat0(rar);
                    break;
                case FORMAT2 :
                    table = new KernSubtableFormat2(rar);
                    break;
                default :
                    break;
            }
            return table;
        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element kern = new Element("kern");
            kern.setAttribute("count", String.valueOf(getKerningCount()));
            for (int i = 0; i < getKerningCount(); i++) {
                KerningPair kp = getKerning(i);
                if (kp != null) {
                    kern.addContent(kp.toXML());
                }
            }
            return kern;
        }
    }

    /**
     * Format 0
     * <p>This is the only format that will be properly
     *    interpreted by Windows and OS/2.</p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Type</b></td><td><b>Field</b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>USHORT</td><td>nPairs</td><td>
     *          This gives the number of kerning pairs in the table.</td></tr>
     *   <tr><td>USHORT</td><td>searchRange</td><td>
     *          The largest power of two less than or equal to the
     *          value of nPairs, multiplied by the size in bytes of an entry in
     *          the table.</td></tr>
     *   <tr><td>USHORT</td><td>entrySelector</td><td>
     *          <SUB>2</SUB>
     *          of the largest power of two less than or equal to the value of
     *          nPairs. This value indicates how many iterations of the search
     *          loop will have to be made. (For example, in a list of eight
     *          items, there would have to be three iterations of the loop).</td></tr>
     *   <tr><td>USHORT</td><td>rangeShift</td><td>
     *          The value of nPairs minus the largest power of two
     *          less than or equal to nPairs, and then multiplied by the size in
     *          bytes of an entry in the table.</td></tr>
     * </table>
     *
     * <p>This is followed by the list of kerning pairs and
     *    values. Each has the following format:</p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><b>Type</b></td><td><b>Field</b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>USHORT</td><td>left</td><td>
     *      The glyph index for the left-hand glyph in the
     *      kerning pair.</td></tr>
     *   <tr><td>USHORT</td><td>right</td><td>
     *      The glyph index for the right-hand glyph in the
     *      kerning pair.</td></tr>
     *   <tr><td>FWORD</td><td>value</td><td>
     *      The kerning value for the above pair, in FUnits. If
     *      this value is greater than zero, the characters will be moved
     *      apart. If this value is less than zero, the character will be
     *      moved closer together.</td></tr>
     * </table>
     *
     */
    public static class KernSubtableFormat0 extends KernSubtable {

        /**
         * number of pairs
         */
        private int nPairs;

        /**
         * search range
         */
        private int searchRange;

        /**
         * entry selector
         */
        private int entrySelector;

        /**
         * range shift
         */
        private int rangeShift;

        /**
         * kerning pairs
         */
        private KerningPair[] kerningPairs;

        /**
         * Create a new object
         *
         * @param rar       input
         * @throws IOException if an IO-error occurs
         */
        KernSubtableFormat0(final RandomAccessR rar) throws IOException {

            nPairs = rar.readUnsignedShort();
            searchRange = rar.readUnsignedShort();
            entrySelector = rar.readUnsignedShort();
            rangeShift = rar.readUnsignedShort();
            kerningPairs = new KerningPair[nPairs];

            // read kering pairs
            for (int i = 0; i < nPairs; i++) {
                kerningPairs[i] = new KerningPair(rar);
            }
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableKERN.KernSubtable#getKerningCount()
         */
        public int getKerningCount() {

            return nPairs;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableKERN.KernSubtable#getKerning(int)
         */
        public KerningPair getKerning(final int i) {

            return kerningPairs[i];
        }

        /**
         * Returns the searchRange.
         * @return Returns the searchRange.
         */
        public int getSearchRange() {

            return searchRange;
        }

        /**
         * Returns the entrySelector.
         * @return Returns the entrySelector.
         */
        public int getEntrySelector() {

            return entrySelector;
        }

        /**
         * Returns the rangeShift.
         * @return Returns the rangeShift.
         */
        public int getRangeShift() {

            return rangeShift;
        }
    }

    /**
     * Format 2
     *
     * <p>This subtable is a two-dimensional array of kerning values.</p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Type</b></td><td><b>Field</b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>USHORT</td><td>rowWidth</td><td>
     *      The width, in bytes, of a row in the table.</td></tr>
     *   <tr><td>USHORT</td><td>leftClassTable</td><td>
     *      Offset from beginning of this subtable to left-hand
     *      class table.</td></tr>
     *   <tr><td>USHORT</td><td>rightClassTable</td><td>
     *      Offset from beginning of this subtable to right-hand
     *      class table.</td></tr>
     *   <tr><td>USHORT</td><td>array</td><td>
     *      Offset from beginning of this subtable to the start
     *      of the kerning array.</td></tr>
     * </table>
     *
     * <p>Each class table has the following header:</p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Type</b></td><td><b>Field</b></td><td><b>Description</b></td></tr>
     *   <tbody>
     *   <tr><td>USHORT</td><td>firstGlyph</td><td>First glyph in class range.</td></tr>
     *   <tr><td>USHORT</td><td>nGlyphs</td><td>Number of glyph in class range.</td></tr>
     * </table>
     */
    public static class KernSubtableFormat2 extends KernSubtable {

        /**
         * row width
         */
        private int rowWidth;

        /**
         * left class table
         */
        private int leftClassTable;

        /**
         * rigth class table
         */
        private int rightClassTable;

        /**
         * array
         */
        private int array;

        /**
         * Create a new object.
         *
         * @param rar       input
         * @throws IOException if an IO-error occurs
         */
        KernSubtableFormat2(final RandomAccessR rar) throws IOException {

            rowWidth = rar.readUnsignedShort();
            leftClassTable = rar.readUnsignedShort();
            rightClassTable = rar.readUnsignedShort();
            array = rar.readUnsignedShort();
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableKERN.KernSubtable#getKerningCount()
         */
        public int getKerningCount() {

            return 0;
        }

        /**
         * @see de.dante.extex.font.type.ttf.TTFTableKERN.KernSubtable#getKerning(int)
         */
        public KerningPair getKerning(final int i) {

            return null;
        }

        /**
         * Returns the array.
         * @return Returns the array.
         */
        public int getArray() {

            return array;
        }

        /**
         * Returns the leftClassTable.
         * @return Returns the leftClassTable.
         */
        public int getLeftClassTable() {

            return leftClassTable;
        }

        /**
         * Returns the rightClassTable.
         * @return Returns the rightClassTable.
         */
        public int getRightClassTable() {

            return rightClassTable;
        }

        /**
         * Returns the rowWidth.
         * @return Returns the rowWidth.
         */
        public int getRowWidth() {

            return rowWidth;
        }
    }

    /**
     * KerningPair
     */
    public static class KerningPair implements XMLConvertible {

        /**
         * left
         */
        private int left;

        /**
         * right
         */
        private int right;

        /**
         * value
         */
        private short value;

        /**
         * Create a new object.
         *
         * @param rar       the input
         * @throws IOException if an IO-error occurs
         */
        KerningPair(final RandomAccessR rar) throws IOException {

            left = rar.readUnsignedShort();
            right = rar.readUnsignedShort();
            value = rar.readShort();
        }

        /**
         * Returns the left char.
         * @return Returns the left char.
         */
        public int getLeft() {

            return left;
        }

        /**
         * Returns the right char.
         * @return Returns the right char.
         */
        public int getRight() {

            return right;
        }

        /**
         * Returns the value.
         * @return Returns the value.
         */
        public short getValue() {

            return value;
        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element kp = new Element("kerningpair");
            kp.setAttribute("left", String.valueOf(left));
            kp.setAttribute("right", String.valueOf(right));
            kp.setAttribute("value", String.valueOf(value));
            return kp;
        }
    }
}