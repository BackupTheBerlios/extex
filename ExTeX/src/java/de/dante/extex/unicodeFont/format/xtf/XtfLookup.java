/*
 * Copyright (C) 20042005 The ExTeX Group and individual authors listed below
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
 * Lookup tables provide a way of looking up information about a glyph index.
 *
 * <p>
 * Some lookup tables do simple array-type lookup. Others involve
 * groupings, allowing you to treat many different glyph indexes
 * in the same way (that is, to look up the same information about
 * them). The top-level description of a lookup table contains a
 * format number and a format-specific header.
 * </p>
 * <p>The format of the Lookup Table header is as follows:</p>
 *
 * <table border="1">
 *   <tbody>
 *     <tr><th>Type</th><th>Name</th><th>Description</th></tr>
 *   </tbody>
 *   <tr><td>uint16</td><td>format</td><td>
 *      Format of this lookup table. There are five lookup table formats,
 *      each with a format number.</td></tr>
 *   <tr><td>variable</td><td>fsHeader</td><td>
 *      Format-specific header (each of these is described in the following
 *      sections), followed by the actual lookup data. The details of
 *      the fsHeader structure are given with the different formats.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class XtfLookup {

    /**
     * LookupFlag IGNORE_BASE_GLYPHS
     */
    public static final int IGNORE_BASE_GLYPHS = 0x0002;

    /**
     * LookupFlag IGNORE_BASE_LIGATURES
     */
    public static final int IGNORE_BASE_LIGATURES = 0x0004;

    /**
     * LookupFlag IGNORE_BASE_MARKS
     */
    public static final int IGNORE_BASE_MARKS = 0x0008;

    /**
     * LookupFlag MARK_ATTACHMENT_TYP
     */
    public static final int MARK_ATTACHMENT_TYPE = 0xFF00;

    /**
     * type
     */
    private int type;

    /**
     * flag
     */
    private int flag;

    /**
     * subtable count
     */
    private int subTableCount;

    /**
     * subtable offsets
     */
    private int[] subTableOffsets;

    /**
     * subtables
     */
    private XtfLookupTable[] subTables;

    /**
     * Create a new object
     *
     * @param rar       input
     * @param offset    offset
     * @throws IOException if an IO-error occurs
     */
    XtfLookup(final RandomAccessR rar, final int offset) throws IOException {

        rar.seek(offset);

        type = rar.readUnsignedShort();
        flag = rar.readUnsignedShort();
        subTableCount = rar.readUnsignedShort();
        subTableOffsets = new int[subTableCount];
        subTables = new XtfLookupTable[subTableCount];
        for (int i = 0; i < subTableCount; i++) {
            subTableOffsets[i] = rar.readUnsignedShort();
        }
        for (int i = 0; i < subTableCount; i++) {
            subTables[i] = newInstance(type, rar, offset + subTableOffsets[i]);
        }
    }

    /**
     * 1 - Single - Replace one glyph with one glyph
     */
    private static final int SINGLE = 1;

    /**
     * 2 - Multiple - Replace one glyph with more than one glyph
     */
    private static final int MULTIPLE = 2;

    /**
     * 3 - Alternate - Replace one glyph with one of many glyphs
     */
    private static final int ALTERNATE = 3;

    /**
     * 4 - Ligature - Replace multiple glyphs with one glyph
     */
    private static final int LIGATURE = 4;

    /**
     * 5 - Context - Replace one or more glyphs in context
     */
    private static final int CONTEXT = 5;

    /**
     * 6 - Chaining - Context Replace one or more glyphs in chained context
     */
    private static final int CHAINING = 6;

    /**
     * Read a subtable.
     *
     * @param tabletype the table type
     * @param rar       input
     * @param offset    the offset
     * @return Returns the subtable
     * @throws IOException if an IO-error occurs
     */
    public XtfLookupTable newInstance(final int tabletype, final RandomAccessR rar,
            final int offset) throws IOException {

        XtfLookupTable s = null;
        switch (tabletype) {
            case SINGLE :
                s = XtfSingleTable.newInstance(rar, offset);
                break;
            case MULTIPLE :
                break;
            case ALTERNATE :
                break;
            case LIGATURE :
                s = XtfLigatureTable.newInstance(rar, offset);
                break;
            case CONTEXT :
                break;
            case CHAINING :
                break;
            default :
                s = null;
        }
        return s;
    }

    /**
     * Returns the type
     * @return Returns the type
     */
    public int getType() {

        return type;
    }

    /**
     * Returns the subtable count
     * @return Returns the subtablecount
     */
    public int getSubtableCount() {

        return subTableCount;
    }

    /**
     * Returns the subtable
     * @param i index
     * @return  Returns the subtable
     */
    public XtfLookupTable getSubtable(final int i) {

        return subTables[i];
    }

    /**
     * Returns the info for this class
     * @return Returns the info for this class
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();
        buf.append("Lookup\n");
        buf.append("   type   : ").append(type).append('\n');
        return buf.toString();
    }
}