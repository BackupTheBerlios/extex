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
 * The table 'hdmx' (Horizontal Device Metrics).
 *
 * <p>
 * The hdmx table relates to OpenType fonts with TrueType outlines.
 * The Horizontal Device Metrics table stores integer advance widths
 * scaled to particular pixel sizes. This allows the font manager
 * to build integer width tables without calling the scaler for each glyph.
 * Typically this table contains only selected screen sizes.
 * This table is sorted by pixel size. The checksum for this table
 * applies to both subtables listed.
 * </p>
 * <p>
 * Note that for non-square pixel grids, the character width (in pixels) will
 * be used to determine which device record to use. For example, a 12 point
 * character on a device with a resolution of 72x96 would be 12 pixels high
 * and 16 pixels wide. The hdmx device record for 16 pixel characters would
 * be used.
 * </p>
 * <p>
 * If bit 4 of the flag field in the 'head' table is not set, then it is
 * assumed that the font scales linearly; in this case an 'hdmx' table
 * is not necessary and should not be built. If bit 4 of the flag field
 * is set, then one or more glyphs in the font are assumed to scale nonlinearly.
 * In this case, performance can be improved by including the 'hdmx' table
 * with one or more important DeviceRecord's for important sizes.
 * </p>
 *
 * <p>hdmx Header</p>
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </thead>
 *   <tr><td>USHORT</td><td>version</td><td>
 *       <td>Table version number (0)</td></tr>
 *   <tr><td>SHORT</td><td>numRecords</td>
 *       <td>Number of device records.</td></tr>
 *   <tr><td>LONG</td><td>sizeDeviceRecord</td>
 *        <td>Size of a device record, long aligned.</td></tr>
 *   <tr><td>DeviceRecord</td><td>records[numRecords]</td>
 *        <td>Array of device records.</td></tr>
 * </table>
 *
 * <p>Device Record</p>
 * <table border="1">
 *   <thead>
 *     <tr><<td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </thead>
 *   <tr><td>BYTE</td><td>pixelSize</td>
 *       <td>Pixel size for following widths (as ppem).</td></tr>
 *   <tr><td>BYTE</td><td>maxWidth</td>
 *       <td>Maximum width.</td></tr>
 *   <tr><td>BYTE</td><td>Widths[numGlyphs]</td>
 *       <td>Array of widths (numGlyphs is from the 'maxp' table).</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TtfTableHDMX extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible {

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TtfTableHDMX(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);

        offset = de.getOffset();
        rarinput = rar;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.AbstractXtfTable#init()
     */
    public void init() throws IOException {

        if (rarinput != null) {
            rarinput.seek(offset);

            version = rarinput.readUnsignedShort();
            numRecords = rarinput.readUnsignedShort();
            sizeDeviceRecord = rarinput.readInt();

            TtfTableMAXP maxp = (TtfTableMAXP) getTableMap()
                    .get(XtfReader.MAXP);
            if (maxp != null) {
                int numberOfGlyphs = maxp.getNumGlyphs();

                deviceRecord = new DeviceRecord[numRecords];
                for (int i = 0; i < numRecords; i++) {
                    deviceRecord[i] = new DeviceRecord(rarinput, numberOfGlyphs);
                }
            }
            rarinput = null;
        }
    }

    /**
     * the offset in the input
     */
    private int offset;

    /**
     * the input
     */
    private RandomAccessR rarinput;

    /**
     * the device record
     */
    public class DeviceRecord implements XMLWriterConvertible {

        /**
         * Create a new object.
         *
         * @param rar            the input
         * @param numberOfGlyphs the number of glyphs
         * @throws IOException if an IO-error occurred.
         */
        public DeviceRecord(final RandomAccessR rar, final int numberOfGlyphs)
                throws IOException {

            pixelSize = rar.readByteAsInt();
            maxWidth = rar.readByteAsInt();
            widths = new byte[numberOfGlyphs];
            rar.readFully(widths);
        }

        /**
         * the widths
         */
        private byte[] widths;

        /**
         * the pixel size
         */
        private int pixelSize;

        /**
         * teh maximum width
         */
        private int maxWidth;

        /**
         * Returns the maxWidth.
         * @return Returns the maxWidth.
         */
        public int getMaxWidth() {

            return maxWidth;
        }

        /**
         * Returns the pixelSize.
         * @return Returns the pixelSize.
         */
        public int getPixelSize() {

            return pixelSize;
        }

        /**
         * Returns the widths.
         * @return Returns the widths.
         */
        public byte[] getWidths() {

            return widths;
        }

        /**
         * @see de.dante.util.XMLWriterConvertible#writeXML(
         *      de.dante.util.xml.XMLStreamWriter)
         */
        public void writeXML(final XMLStreamWriter writer) throws IOException {

            writer.writeStartElement("devicerecord");
            writer.writeAttribute("maxwidth", String.valueOf(maxWidth));
            writer.writeAttribute("pixelsize", String.valueOf(pixelSize));
            writer.writeByteArray(widths);
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
    private int numRecords;

    /**
     * Size of a device record
     */
    private int sizeDeviceRecord;

    /**
     * the device record array
     */
    private DeviceRecord[] deviceRecord;

    /**
     * Returns the deviceRecord.
     * @return Returns the deviceRecord.
     */
    public DeviceRecord[] getDeviceRecord() {

        return deviceRecord;
    }

    /**
     * Returns the numRecords.
     * @return Returns the numRecords.
     */
    public int getNumRecords() {

        return numRecords;
    }

    /**
     * Returns the sizeDeviceRecord.
     * @return Returns the sizeDeviceRecord.
     */
    public int getSizeDeviceRecord() {

        return sizeDeviceRecord;
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

        return XtfReader.HDMX;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "hdmx";
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeAttribute("version", String.valueOf(version));
        writer.writeAttribute("numrecords", String.valueOf(numRecords));
        writer.writeAttribute("sizedevicerecord", String
                .valueOf(sizeDeviceRecord));
        for (int i = 0; i < deviceRecord.length; i++) {
            deviceRecord[i].writeXML(writer);
        }
        writer.writeEndElement();
    }
}