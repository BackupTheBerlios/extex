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
 * The 'loca' table stores the offsets to the locations
 * of the glyphs in the font relative to the beginning of
 * the 'glyf' table. Its purpose is to provide quick
 * access to the data for a particular character.
 *
 * <table border="1">
 *   <tbody>
 *     <tr><td><b>Glyph Index</b></td><td><b>Offset</b></td><td><b>Glyph length</b></td></tr>
 *   </tbody>
 *   <tr><td>0</td><td>0</td><td>100</td></tr>
 *   <tr><td>1</td><td>100</td><td>150</td></tr>
 *   <tr><td>2</td><td>250</td><td></td></tr>
 *   <tr><td><td>...</td><td>...</td><td>...</td></tr>
 *   <tr><td>n-1</td><td>1170</td><td>120</td></tr>
 *   <tr><td>extra</td><td>1290</td><td></td></tr>
 * </table>
 *
 * <p><code>'loca'</code> short version</p>
 * <table border="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>uint16</td><td>offsets[n]</td><td>
 *          The actual local offset divided by 2 is stored.
 *          The value of n is the number of glyphs in the font + 1.
 *          The number of glyphs in the font is found in the maximum
 *          profile table.</td></tr>
 * </table>
 *
 * <p><code>'loca'</code> long version</p>
 *
 * <table border="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>uint32</td><td>offsets[n]</td><td>
 *          The actual local offset is stored. The value of n is the
 *          number of glyphs in the font + 1. The number of glyphs
 *          in the font is found in the maximum profile table.</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class TTFTableLOCA extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * buffer
     */
    private byte[] buf = null;

    /**
     * offsets
     */
    private int[] offsets = null;

    /**
     * factor:
     * <p>short : 2</p>
     * <p>long : 1</p>
     */
    private short factor = 0;

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableLOCA(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        buf = new byte[de.getLength()];
        rar.readFully(buf); // mgn rar.read(buf)
    }

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

        TTFTableHEAD head = (TTFTableHEAD) getTableMap().get(TTFFont.HEAD);
        TTFTableMAXP maxp = (TTFTableMAXP) getTableMap().get(TTFFont.MAXP);
        if (head == null || maxp == null) {
            return;
        }
        int numGlyphs = maxp.getNumGlyphs();
        boolean shortEntries = head.getIndexToLocFormat() == 0;

        if (buf == null) {
            return;
        }

        offsets = new int[numGlyphs + 1];
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        if (shortEntries) {
            factor = 2;
            for (int i = 0; i <= numGlyphs; i++) {
                offsets[i] = (bais.read() << 8 | bais.read());
            }
        } else {
            factor = 1;
            for (int i = 0; i <= numGlyphs; i++) {
                offsets[i] = (bais.read() << 24 | bais.read() << 16
                        | bais.read() << 8 | bais.read());
            }
        }
        buf = null;
    }

    /**
     * Returns the offset
     * @param i index
     * @return Returns the offset
     */
    public int getOffset(final int i) {

        if (offsets == null) {
            return 0;
        }
        return offsets[i] * factor;
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.LOCA;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("table");
        table.setAttribute("name", "loca");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        for (int i = 0; i < offsets.length; i++) {
            Element offset = new Element("offset");
            offset.setAttribute("id", String.valueOf(i));
            offset.setAttribute("value", String.valueOf(getOffset(i)));
            table.addContent(offset);
        }
        return table;
    }

}