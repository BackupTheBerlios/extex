/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.util.GroupEntries;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * The name table(tag name: 'name') allows you to include human-readable
 * names for features and settings, copyright notices, font names,
 * style names, and other information related to your font.
 *
 * <table border="1">
 *   <tbody>
 *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>Description</b></td></tr>
 *   </tbody>
 *   <tr><td>UInt16</td><td>format</td><td>Format selector. Set to 0.</td></tr>
 *   <tr><td>UInt16</td><td>count</td><td>
 *              The number of nameRecords in this name table.</td></tr>
 *   <tr><td>UInt16</td><td>stringOffset</td><td>
 *              Offset in bytes to the beginning of the
 *              name character strings.</td></tr>
 *   <tr><td>NameRecord</td><td>nameRecord[count]</td><td>
 *              The name records array.</td></tr>
 *   <tr><td>variable</td><td>name</td><td>
 *              character strings The character strings of the names.
 *              Note that these are not necessarily ASCII!</td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class TtfTableNAME extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible {

    /**
     * format.
     */
    private short format;

    /**
     * count.
     */
    private short count;

    /**
     * stringOffset.
     */
    private short stringOffset;

    /**
     * records.
     */
    private NameRecord[] namerecords;

    /**
     * Create a new object.
     *
     * @param tablemap  the tablemap
     * @param de        directory entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TtfTableNAME(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());
        format = rar.readShort();
        count = rar.readShort();
        stringOffset = rar.readShort();
        namerecords = new NameRecord[count];

        // Load the records, which contain the encoding information and string offsets
        for (int i = 0; i < count; i++) {
            namerecords[i] = new NameRecord(rar);
        }

        // Now load the strings
        for (int i = 0; i < count; i++) {
            namerecords[i].loadString(rar, de.getOffset() + stringOffset);
        }
    }

    /**
     * ID: COPYRIGHTNOTICE.
     */
    public static final short COPYRIGHTNOTICE = 0;

    /**
     * ID: FONTFAMILYNAME.
     */
    public static final short FONTFAMILYNAME = 1;

    /**
     * ID: FONTSUBFAMILYNAME.
     */
    public static final short FONTSUBFAMILYNAME = 2;

    /**
     * ID: UNIQUEFONTIDENTIFIER.
     */
    public static final short UNIQUEFONTIDENTIFIER = 3;

    /**
     * ID: FULLFONTNAME.
     */
    public static final short FULLFONTNAME = 4;

    /**
     * ID: VERSIONSTRING.
     */
    public static final short VERSIONSTRING = 5;

    /**
     * ID: POSTSCRIPTNAME.
     */
    public static final short POSTSCRIPTNAME = 6;

    /**
     * ID: TRADEMARK.
     */
    public static final short TRADEMARK = 7;

    /**
     * ID - name.
     */
    public static final String[] IDNAME = {"COPYRIGHTNOTICE", "FONTFAMILYNAME",
            "FONTSUBFAMILYNAM", "UNIQUEFONTIDENTIFIER", "FULLFONTNAME",
            "VERSIONSTRING", "POSTSCRIPTNAME", "TRADEMARK"};

    /**
     * platform apple unicode.
     */
    public static final short APPLE_UNICODE = 0;

    /**
     * platform macintosh.
     */
    public static final short MACINTOSH = 1;

    /**
     * platform iso.
     */
    public static final short ISO = 2;

    /**
     * platform microsoft.
     */
    public static final short MICROSOFT = 3;

    /**
     * platform names.
     */
    public static final String[] PLATFORMNAME = {"APPLEUNICODE", "MACINTOSH",
            "ISO", "MICROSOFT"};

    /**
     * Returns the record.
     * @param nameId    name id
     * @return Returns the record.
     */
    public String getRecord(final short nameId) {

        // Search for the first instance of this name ID
        for (int i = 0; i < count; i++) {
            if (namerecords[i].getNameId() == nameId) {
                return namerecords[i].getRecordString();
            }
        }
        return "";
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return XtfReader.NAME;
    }

    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "name";
    }

    /**
     * Returns the format.
     * @return Returns the format.
     */
    public short getFormat() {

        return format;
    }

    /**
     * Returns the count.
     * The number of nameRecords in this name table.
     * @return Returns the count.
     */
    public short getCount() {

        return count;
    }

    /**
     * Returns the namerecords.
     * @return Returns the namerecords.
     */
    public NameRecord[] getNameRecords() {

        return namerecords;
    }

    /**
     * Check, if a record for the platform exists.
     * @param platfromid    Teh platform id.
     * @return Returns <code>true</code>, if a record for the platform exists.
     */
    public boolean existsPlatfrom(final int platfromid) {

        for (int i = 0; i < namerecords.length; i++) {
            if (namerecords[i].getPlatformId() == platfromid) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the namerecords for a platfrom id.
     * @param platfromid The platform id.
     * @return Returns the namerecords for a platfrom id.
     */
    public NameRecord[] getNameRecords(final int platfromid) {

        int cnt = 0;
        for (int i = 0; i < namerecords.length; i++) {
            if (namerecords[i].getPlatformId() == platfromid) {
                cnt++;
            }
        }

        NameRecord[] pltrec = new NameRecord[cnt];
        cnt = 0;
        for (int i = 0; i < namerecords.length; i++) {
            if (namerecords[i].getPlatformId() == platfromid) {
                pltrec[cnt++] = namerecords[i];
            }
        }
        return pltrec;
    }

    /**
     * Returns the namerecords for a platfrom id.
     * @param platfromid The platform id.
     * @return Returns the namerecords for a platfrom id.
     */
    public String getRecordString(final int platfromid, final int id) {

        NameRecord[] rec = getNameRecords(platfromid);

        for (int i = 0; i < rec.length; i++) {
            if (rec[i].getNameId() == id) {
                return rec[i].getRecordString();
            }
        }
        return "???";
    }

    /**
     * Returns the stringOffset.
     * @return Returns the stringOffset.
     */
    public short getStringOffset() {

        return stringOffset;
    }

    /**
     * Returns the platform ids.
     * @return Returns the platform ids.
     */
    public int[] getPlatformIDs() {

        GroupEntries en = new GroupEntries();

        for (int i = 0; i < count; i++) {
            int id = namerecords[i].getPlatformId();
            en.add(id);
        }

        return en.toIntArray();
    }

    /**
     * Returns the platform name for a id.
     * @param id    The platform id.
     * @return Returns the platform name for a id.
     */
    public static String getPlatformName(final int id) {

        if (id >= 0 && id < PLATFORMNAME.length) {
            return PLATFORMNAME[id];
        }
        return "? " + id + " ?";
    }

    /**
     * Returns the name for a id.
     * @param id    The name id.
     * @return Returns the name name for a id.
     */
    public static String getIdName(final int id) {

        if (id >= 0 && id < IDNAME.length) {
            return IDNAME[id];
        }
        return "? " + id + " ?";
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeAttribute("format", String.valueOf(format));
        writer.writeAttribute("count", String.valueOf(count));
        writer.writeAttribute("stringoffset", String.valueOf(stringOffset));
        for (int i = 0; i < count; i++) {
            namerecords[i].writeXML(writer);
        }
        writer.writeEndElement();
    }

    // ----------------------------------------
    // ----------------------------------------
    // ----------------------------------------
    // ----------------------------------------

    /**
     * NameRecord
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Type</b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>USHORT</td><td>Platform ID.</td></tr>
     *   <tr><td>USHORT</td><<td>Platform-specific encoding ID.</td></tr>
     *   <tr><td>USHORT</td><td>Language ID.</td></tr>
     *   <tr><td>USHORT</td><td>Name ID.</td></tr>
     *   <tr><td>USHORT</td><td>String length (in bytes).</td>/tr>
     *   <tr><td>USHORT</td><td>String offset from start
     *                   of storage area (in bytes).</td></tr>
     * </table>
     *
     * <p><b>Platform ID</b></p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>ID</b></td><td><b>Platform</b></td><td><b>Specific encoding</b></td></tr>
     *   </tbody>
     *   <tr><td>0</td><td>Unicode</td><td>none</td></tr>
     *   <tr><td>1</td><td>Macintosh</td><td>Script manager code</td></tr>
     *   <tr><td>2</td><td>ISO</td><td>ISO encoding</td></tr>
     *   <tr><td>3</td><td>Microsoft</td><td>Microsoft encoding</td></tr>
     * </table>
     *
     * <p><b>Microsoft platform-specific encoding ID&rsquo;s (platform ID = 3)</b></p>
     *
     * <table BORDER="1">
     *   <tbody>
     *     <tr><td><b>Code</b></td><td><b>Description</b></td></tr>
     *   </tbody>
     *   <tr><td>0</td><td>Undefined character set or indexing scheme</td></tr>
     *   <tr><td>1</td><td>UGL character set  with Unicode indexing scheme (see
     *                     chapter, &ldquo;Character Sets.&rdquo;)</td></tr>
     * </table>
     */
    public class NameRecord implements XMLWriterConvertible {

        /**
         * platformId.
         */
        private short platformId;

        /**
         * encodingId.
         */
        private short encodingId;

        /**
         * languageId.
         */
        private short languageId;

        /**
         * nameId.
         */
        private short nameId;

        /**
         * stringLength.
         */
        private short stringLength;

        /**
         * stringOffset.
         */
        private short stringOffset;

        /**
         * record.
         */
        private String record;

        /**
         * Create a new object.
         *
         * @param rar       input
         * @throws IOException if an IO-error occurs
         */
        NameRecord(final RandomAccessR rar) throws IOException {

            platformId = rar.readShort();
            encodingId = rar.readShort();
            languageId = rar.readShort();
            nameId = rar.readShort();
            stringLength = rar.readShort();
            stringOffset = rar.readShort();
        }

        /**
         * Returns the encoding id.
         * @return Returns the encoding id
         */
        public short getEncodingId() {

            return encodingId;
        }

        /**
         * Returns the language id.
         * @return Returns the language id
         */
        public short getLanguageId() {

            return languageId;
        }

        /**
         * Returns the name id.
         * @return Returns the name id
         */
        public short getNameId() {

            return nameId;
        }

        /**
         * Returns the platform id.
         * @return Returns the platform id
         */
        public short getPlatformId() {

            return platformId;
        }

        /**
         * Returns the record string.
         * @return Returns the record string
         */
        public String getRecordString() {

            return record;
        }

        /**
         * Load the string.
         * @param rar                   input
         * @param stringStorageOffset   offsset
         * @throws IOException if an IO-error occurs
         */
        public void loadString(final RandomAccessR rar,
                final int stringStorageOffset) throws IOException {

            StringBuffer sb = new StringBuffer();
            rar.seek(stringStorageOffset + stringOffset);
            switch (platformId) {
                case APPLE_UNICODE :
                    for (int i = 0; i < stringLength / 2; i++) {
                        sb.append(rar.readChar());
                    }
                    break;
                case MACINTOSH :
                    for (int i = 0; i < stringLength; i++) {
                        sb.append((char) rar.readByte());
                    }
                    break;
                case ISO :
                    for (int i = 0; i < stringLength; i++) {
                        sb.append((char) rar.readByte());
                    }
                case MICROSOFT :
                    for (int i = 0; i < stringLength / 2; i++) {
                        sb.append(rar.readChar());
                    }
                default :
                    break;
            }
            record = sb.toString();
        }

        /**
         * Returns the record.
         * @return Returns the record.
         */
        public String getRecord() {

            return record;
        }

        /**
         * Returns the stringLength.
         * @return Returns the stringLength.
         */
        public short getStringLength() {

            return stringLength;
        }

        /**
         * Returns the stringOffset.
         * @return Returns the stringOffset.
         */
        public short getStringOffset() {

            return stringOffset;
        }

        /**
         * Returns the info for this class.
         * @return Returns the info for this class
         */
        public String toString() {

            StringBuffer buf = new StringBuffer();
            buf.append("Name Record\n");
            buf.append("   ").append(record).append('\n');
            return buf.toString();
        }

        /**
         * @see de.dante.util.XMLWriterConvertible#writeXML(
         *      de.dante.util.xml.XMLStreamWriter)
         */
        public void writeXML(final XMLStreamWriter writer) throws IOException {

            writer.writeStartElement("namerecord");
            writer.writeAttribute("platformid", String.valueOf(platformId));
            writer.writeAttribute("platform", platformId >= PLATFORMNAME.length
                    ? ""
                    : PLATFORMNAME[platformId]);
            writer.writeAttribute("encodingid", String.valueOf(encodingId));
            writer.writeAttribute("languageid", String.valueOf(languageId));
            writer.writeAttribute("nameid", String.valueOf(nameId));
            writer.writeAttribute("name", nameId >= IDNAME.length
                    ? ""
                    : IDNAME[nameId]);
            writer.writeAttribute("stringlength", String.valueOf(stringLength));
            writer.writeAttribute("stringoffset", String.valueOf(stringOffset));
            writer.writeCharacters(record);
            writer.writeEndElement();
        }
    }
}
