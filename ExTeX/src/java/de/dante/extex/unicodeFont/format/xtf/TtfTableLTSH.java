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

import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * The table 'LTSH' (The Linear Threshold Table).
 *
 * <p>
 * The LTSH table relates to OpenType fonts containing TrueType outlines.
 * There are noticeable improvements to fonts on the screen when instructions
 * are carefully applied to the sidebearings. The gain in readability is offset
 * by the necessity for the OS to grid fit the glyphs in order to find the
 * actual advance width for the glyphs (since instructions may be moving
 * the sidebearing points). The TrueType outline format already has two
 * mechanisms to side step the speed issues: the 'hdmx' table, where precomputed
 * advance widths may be saved for selected ppem sizes, and the 'vdmx' table,
 * where precomputed vertical advance widths may be saved for selected ppem
 * sizes. The 'LTSH' table (Linear ThreSHold) is a second, complementary method.
 * </p>
 * <p>
 * The LTSH table defines the point at which it is reasonable to assume
 * linearly scaled advance widths on a glyph-by-glyph basis. This table
 * should not be included unless bit 4 of the "flags" field in the 'head'
 * table is set. The criteria for linear scaling is:
 * </p>
 * <p>
 * a. (ppem size is 50) AND (difference between the rounded linear width
 * and the rounded instructed width £ 2% of the rounded linear width)
 * </p>
 * <p>
 * or b. Linear width == Instructed width
 * </p>
 * <p>
 * The LTSH table records the ppem for each glyph at which the scaling becomes
 * linear again, despite instructions effecting the advance width.
 * It is a requirement that, at and above the recorded threshold size,
 * the glyph remain linear in its scaling (i.e., not legal to set threshold
 * at 55 ppem if glyph becomes nonlinear again at 90 ppem). The format for the
 * table is:
 * <p>
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </thead>
 *   <tr><td>USHORT</td><td>version</td><td>Version number (starts at 0).</td></tr>
 *   <tr><td>USHORT</td><td>numGlyphs</td>
 *       <td>Number of glyphs (from "numGlyphs" in 'maxp' table).</td></tr>
 *   <tr><td>BYTE</td><td>yPels[numGlyphs]</td>
 *       <td>The vertical pel height at which the glyph can be assumed to
 *           scale linearly. On a per glyph basis.</td></tr>
 * </table>
 * <p>
 * Note that glyphs which do not have instructions on their sidebearings
 * should have yPels = 1; i.e., always scales linearly.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TtfTableLTSH extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible {

    /**
     * Create a new object
     *
     * @param tablemap  the table map
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TtfTableLTSH(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        version = rar.readUnsignedShort();
        numGlyphs = rar.readUnsignedShort();
        yPels = new byte[numGlyphs];
        rar.readFully(yPels);
    }

    /**
     * the version
     */
    private int version;

    /**
     * the number of glyphs
     */
    private int numGlyphs;

    /**
     * The vertical pel height at which the glyph can be assumed
     * to scale linearly. On a per glyph basis.
     */
    private byte[] yPels;

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return XtfReader.LTSH;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "ltsh";
    }

    /**
     * Returns the numGlyphs.
     * @return Returns the numGlyphs.
     */
    public int getNumGlyphs() {

        return numGlyphs;
    }

    /**
     * Returns the version.
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * Returns the yPels.
     * @return Returns the yPels.
     */
    public byte[] getYPels() {

        return yPels;
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeAttribute("version", version);
        writer.writeAttribute("nummglyphs", numGlyphs);
        writer.writeByteArray(yPels);
        writer.writeEndElement();
    }
}