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
 * The table 'gasp'.
 *
 * <p>
 * This table contains information which describes the preferred
 * rasterization techniques for the typeface when it is rendered
 * on grayscale-capable devices. This table also has some use for
 * monochrome devices, which may use the table to turn off hinting
 * at very large or small sizes, to improve performance.
 * </p>
 * <table border="1">
 *  <thead>
 *  <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *  </thead>
 *  <tr><td>USHORT</td><td>version</td><td>Version number (set to 0)</td></tr>
 *  <tr><td>USHORT</td><td>numRanges</td><td>Number of records to follow</td></tr>
 *  <tr><td>GASPRANGE</td><td>gaspRange[numRanges]</td><td>Sorted by ppem</td></tr>
 * </table>
 *
 * <p>GASPRANGE</p>
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </thead>
 *   <tr><td>USHORT</td><td>rangeMaxPPEM</td>
 *       <td>Upper limit of range, in PPEM</td></tr>
 *   <tr><td>USHORT</td><td>rangeGaspBehavior</td>
 *       <td>Flags describing desired rasterizer behavior.</td></tr>
 * </table>
 *
 * <p>There are two flags for the rangeGaspBehavior flags:<p>
 * <table border="1">
 *   <thead>
 *     <tr><td>Flag</b></td><td><b>Meaning</b></td></tr>
 *   </thead>
 *   <tr><td>GASP_GRIDFIT</td><td>Use gridfitting</td></tr>
 *   <tr><td>GASP_DOGRAY</td><td>Use grayscale rendering</td></tr>
 * </table>
 *
 * <p>The four currently defined values of rangeGaspBehavior
 *    would have the following uses:</p>
 * <table border="1">
 *   <thead>
 *     <tr><td>Flag</b></td><td><b>Value</b></td><td><b>Meaning</b></td></tr>
 *   </thead>
 *   <tr><td>GASP_DOGRAY</td><td>0x0002</td>
 *       <td>small sizes, typically ppem&lt;9</td></tr>
 *   <tr><td>GASP_GRIDFIT</td><td>0x0001</td>
 *       <td>medium sizes, typically 9&lt;=ppem&lt;=16</td></tr>
 *   <tr><td>GASP_DOGRAY|GASP_GRIDFIT</td><td>0x0003</td>
 *       <td>large sizes, typically ppem&gt;16</td></tr>
 *   <tr><td>(neither)</td><td>0x0000</td>
 *       <td>optional for very large sizes, typically ppem&gt;2048</td></tr>
 * </table>
 *
 * <p>
 * The records in the gaspRange[] array must be sorted in order of
 * increasing rangeMaxPPEM value. The last record should use 0xFFFF
 * as a sentinel value for rangeMaxPPEM and should describe the behavior
 * desired at all sizes larger than the previous record's upper limit.
 * If the only entry in 'gasp' is the 0xFFFF sentinel value, the behavior
 * described will be used for all sizes.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TtfTableGASP extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible {

    /**
     * GASP_GRIDFIT
     */
    public static final int GASP_GRIDFIT = 0x0001;

    /**
     * GASP_DOGRAY
     */
    public static final int GASP_DOGRAY = 0x0002;

    /**
     * GASP_DOGRAY_GRIDFIT
     */
    public static final int GASP_DOGRAY_GRIDFIT = 0x0003;

    /**
     * GASP_NONE
     */
    public static final int GASP_NONE = 0x0000;

    /**
     * Create a new object
     *
     * @param tablemap  the table map
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TtfTableGASP(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        version = rar.readUnsignedShort();
        numRanges = rar.readUnsignedShort();
        gaspRange = new GaspRange[numRanges];
        for (int i = 0; i < numRanges; i++) {
            gaspRange[i] = new GaspRange(rar);
        }
    }

    /**
     * GaspRange
     */
    public class GaspRange implements XMLWriterConvertible {

        /**
         * Create a new object.
         *
         * @param rar   the input
         * @throws IOException if an IO-error occurred.
         */
        public GaspRange(final RandomAccessR rar) throws IOException {

            rangeMaxPPEM = rar.readUnsignedShort();
            rangeGaspBehavior = rar.readUnsignedShort();
        }

        /**
         * rangeMaxPPEM
         */
        private int rangeMaxPPEM;

        /**
         * rangeGaspBehavior
         */
        private int rangeGaspBehavior;

        /**
         * Returns the rangeGaspBehavior.
         * @return Returns the rangeGaspBehavior.
         */
        public int getRangeGaspBehavior() {

            return rangeGaspBehavior;
        }

        /**
         * Returns the rangeMaxPPEM.
         * @return Returns the rangeMaxPPEM.
         */
        public int getRangeMaxPPEM() {

            return rangeMaxPPEM;
        }

        /**
         * Returns the Flags as String.
         * @return Returns the Flags as String.
         */
        public String getFlags() {

            if (rangeGaspBehavior == GASP_DOGRAY_GRIDFIT) {
                return "Use grayscale rendering and gridfitting";
            } else if (rangeGaspBehavior == GASP_DOGRAY) {
                return "Use grayscale rendering";
            } else if (rangeGaspBehavior == GASP_GRIDFIT) {
                return "Use gridfitting";
            } else if (rangeGaspBehavior == GASP_NONE) {
                return "none";
            }
            return "???";
        }

        /**
         * @see de.dante.util.XMLWriterConvertible#writeXML(
         *      de.dante.util.xml.XMLStreamWriter)
         */
        public void writeXML(final XMLStreamWriter writer) throws IOException {

            writer.writeStartElement("gasprange");
            writer.writeAttribute("rangemaxPPEM", String.valueOf(rangeMaxPPEM));
            writer.writeAttribute("rangegaspbehavior", String
                    .valueOf(rangeGaspBehavior));
            writer.writeAttribute("flags", getFlags());
            writer.writeEndElement();
        }
    }

    /**
     * version
     */
    private int version;

    /**
     * number of records
     */
    private int numRanges;

    /**
     * the GaspRange array
     */
    private GaspRange[] gaspRange;

    /**
     * Returns the gaspRange.
     * @return Returns the gaspRange.
     */
    public GaspRange[] getGaspRange() {

        return gaspRange;
    }

    /**
     * Returns the numRanges.
     * @return Returns the numRanges.
     */
    public int getNumRanges() {

        return numRanges;
    }

    /**
     * Returns the version.
     * @return Returns the version.
     */
    public int getVersion() {

        return version;
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return XtfReader.GASP;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "gasp";
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeAttribute("version", String.valueOf(version));
        writer.writeAttribute("numranges", String.valueOf(numRanges));
        for (int i = 0; i < numRanges; i++) {
            gaspRange[i].writeXML(writer);
        }
        writer.writeEndElement();
    }
}