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
import java.util.ArrayList;

import org.jdom.Comment;
import org.jdom.Element;

import de.dante.extex.font.type.ttf.cff.T2CharString;
import de.dante.extex.font.type.ttf.cff.T2Operator;
import de.dante.extex.font.type.ttf.cff.T2StandardStrings;
import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessInputArray;
import de.dante.util.file.random.RandomAccessR;

/**
 * The 'CFF' - PostScript font program.
 *
 * <p>
 * This table contains a compact representation of a PostScript Type 1,
 * or CIDFont and is structured according to
 * <a href="http://partners.adobe.com/asn/developer/pdfs/tn/5176.CFF.pdf">
 * Adobe Technical Note #5176: " The Compact Font Format Specification"</a>
 * and
 * <a href="http://partners.adobe.com/asn/developer/pdfs/tn/5177.Type2.pdf">
 * Adobe Technical Note #5177: "Type 2 Charstring Format"</a>.
 * </p>
 *
 * <p>CFF Data Types</p>
 * <table border="1">
 *   <thead>
 *     <tr><td>Name</td><td>Range</td><td>Description</td></tr>
 *   </thead>
 *   <tr><td>Card8</td><td>0   255</td><td>
 *      1-byte unsigned number</td></tr>
 *   <tr><td>Card16</td><td>0   65535</td><td>
 *      2-byte unsigned number</td></tr>
 *   <tr><td>Offset</td><td>varies</td><td>
 *      1, 2, 3, or 4 byte offset (specified by OffSize field)</td></tr>
 *   <tr><td>OffSize</td><td>1 - 4</td><td>
 *      1-byte unsigned number specifies the size of an Offset
 *      field or fields</td></tr>
 *   <tr><td>SID</td><td>0 - 64999</td><td>
 *      2-byte string identifier</td></tr>
 * </table>
 *
 * <p>CFF Data Layout</p>
 *
 * <table border="1">
 *   <thead>
 *     <tr><td><b>Entry</b></td><td><b>Comments</b></td></tr>
 *   </thead>
 *   <tr><td>Header</td><td>-</td></tr>
 *   <tr><td>Name INDEX</td><td>-</td></tr>
 *   <tr><td>Top DICT INDEX</td><td>-</td></tr>
 *   <tr><td>String INDEX</td><td>->/td></tr>
 *   <tr><td>Global Subr INDEX</td><td>-</td></tr>
 *   <tr><td>Encodings</td><td>-</td></tr>
 *   <tr><td>Charsets</td><td>-</td></tr>
 *   <tr><td>FDSelect</td><td>CIDFonts only</td></tr>
 *   <tr><td>CharStrings INDEX</td><td>per-font</td></tr>
 *   <tr><td>Font DICT INDEX</td><td>per-font, CIDFonts only</td></tr>
 *   <tr><td>Private DICT</td><td>per-font</td></tr>
 *   <tr><td>Local Subr INDEX</td><td>per-font or per-Private
 *           DICT for CIDFonts</td></tr>
 *   <tr><td>Copyright and Trademark Notices</td><td>-/td></tr>
 * </table>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class TTFTableCFF extends AbstractTTFTable
        implements
            TTFTable,
            XMLConvertible {

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    TTFTableCFF(final TableMap tablemap, final TableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        // header
        versionmajor = rar.readUnsignedByte();
        versionminor = rar.readUnsignedByte();
        hdrSize = rar.readUnsignedByte();
        offSize = rar.readUnsignedByte();

        // index
        nameindex = new NameINDEX(de.getOffset() + hdrSize, rar);
        topdictindex = new TopDictINDEX(-1, rar);

        System.out.println("pointer: " + Long.toHexString(rar.getPointer()));

        stringindex = new StringIndex(-1, rar);

        // incomplete
    }

    /**
     * The table NameINDEX
     */
    private NameINDEX nameindex;

    /**
     * The table top DICT INDEX
     */
    private TopDictINDEX topdictindex;

    /**
     * The tbale StringINDEX
     */
    private StringIndex stringindex;

    /**
     * offset size
     */
    private int offSize;

    /**
     * header size
     */
    private int hdrSize;

    /**
     * Version major
     */
    private int versionmajor;

    /**
     * Version minor
     */
    private int versionminor;

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return TTFFont.CFF;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element table = new Element("cff");
        table.setAttribute("id", "0x" + Integer.toHexString(getType()));
        table.setAttribute("version", String.valueOf(versionmajor) + "."
                + String.valueOf(versionminor));
        table.setAttribute("hdrsize", String.valueOf(hdrSize));
        table.setAttribute("offsize", String.valueOf(offSize));
        if (nameindex != null) {
            table.addContent(nameindex.toXML());
        }
        if (topdictindex != null) {
            table.addContent(topdictindex.toXML());
        }
        if (stringindex != null) {
            table.addContent(stringindex.toXML());
        }
        Comment c = new Comment("incomplete");
        table.addContent(c);
        return table;

    }

    /**
     * Returns the hdrSize.
     * @return Returns the hdrSize.
     */
    public int getHdrSize() {

        return hdrSize;
    }

    /**
     * Returns the versionmajor.
     * @return Returns the versionmajor.
     */
    public int getVersionmajor() {

        return versionmajor;
    }

    /**
     * Returns the versionminor.
     * @return Returns the versionminor.
     */
    public int getVersionminor() {

        return versionminor;
    }

    /**
     * Returns the nameindex.
     * @return Returns the nameindex.
     */
    public NameINDEX getNameindex() {

        return nameindex;
    }

    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------
    // ---------------------------------------------------

    /**
     * INDEX-Table
     *
     * <table border="1">
     *   <thead>
     *     <tr><td><b>Type</b></td><td><b>Name</b></td><td><b>
     *      Description</b></td></tr>
     *   </thead>
     *   <tr><td>Card16</td><td>count</td><td>
     *      Number of objects stored in INDEX.
     *      An empty INDEX is represented by a count
     *      field with a 0 value and no additional fields.
     *      Thus, the total size of an empty INDEX is 2 bytes.</td></tr>
     *   <tr><td>OffSize</td><td>offSize</td><td>
     *      Offset array element size</td></tr>
     *   <tr><td>Offset</td><td>offset [count+1]</td><td>
     *       Offset array (from byte preceding object data).
     *       Offsets in the offset array are relative to the
     *       byte that precedes the object data. Therefore
     *       the first element of the offset array is always 1.
     *       (This ensures that every object has a corresponding
     *       offset which is always nonzero and permits the efficient
     *       implementation of dynamic object loading.)
     * </td></tr>
     *   <tr><td>Card8</td><td>data [&lt;varies&gt;]</td><td>
     *       Object data</td></tr>
     *  </table>
     */
    abstract class INDEX implements XMLConvertible {

        /**
         * Create a new object.
         *
         * @param offset        the offset
         * @param rar           the input
         * @throws IOException if an IO-error occurs
         */
        INDEX(final int offset, final RandomAccessR rar) throws IOException {

            // go to the offset.
            // is the offset less than zero,
            // the input (pointer) is on the correct offset
            if (offset > 0) {
                rar.seek(offset);
            }

            count = rar.readUnsignedShort();
            if (count > 0) {
                int ioffSize = rar.readUnsignedByte();
                datas = new Object[count];
                int[] offsetarray = new int[count + 1];

                // read all offsets
                for (int offs = 0; offs < offsetarray.length; offs++) {
                    offsetarray[offs] = readOffset(ioffSize, rar);
                    //                    System.out.println("offset [" + offs + "]="
                    //                            + offsetarray[offs]);
                }

                // get data
                for (int i = 0; i < count; i++) {
                    datas[i] = readData(offsetarray[i], offsetarray[i + 1], rar);
                }
            }

        }

        /**
         * Read the data
         * @param start the start offset
         * @param end   the end offset
         * @param rar   the input
         * @return Returns the data
         * @throws IOException if an IO-error occurs
         */
        private short[] readData(final int start, final int end,
                final RandomAccessR rar) throws IOException {

            short[] data = new short[end - start];
            for (int i = 0; i < data.length; i++) {
                data[i] = (short) rar.readUnsignedByte();
            }
            return data;
        }

        /**
         * Read the offset (see offsetsize)
         * @param os        the offsetsize
         * @param rar       the input
         * @return Returns the offset
         * @throws IOException if an IO-error occurs
         */
        private int readOffset(final int os, final RandomAccessR rar)
                throws IOException {

            int offset = 0;
            for (int i = 0; i < os; i++) {
                int b = rar.readUnsignedByte();
                offset = offset << TTFConstants.SHIFT8;
                offset += b;
            }
            return offset;
        }

        /**
         * all datas
         */
        private Object[] datas;

        /**
         * Number of objects
         */
        private int count;

        /**
         * Returns the count.
         * @return Returns the count.
         */
        public int getCount() {

            return count;
        }

        /**
         * Returns the datas.
         * @return Returns the datas.
         */
        public Object[] getDatas() {

            return datas;
        }
    }

    /**
     * This contains the PostScript language names
     * (FontName or CIDFontName) of all the fonts
     * in the FontSet stored in an INDEX structure.
     */
    class NameINDEX extends INDEX {

        /**
         * Create a new object.
         *
         * @param offset    the offset
         * @param rar       the input
         * @throws IOException if an IO-error occurs.
         */
        public NameINDEX(final int offset, final RandomAccessR rar)
                throws IOException {

            super(offset, rar);
        }

        /**
         * Return the name as string
         * @param index     the index for the name
         * @return Returns the name as string
         */
        public String getName(final int index) {

            if (index < 0 || index > getCount()) {
                return null;
            }
            short[] data = (short[]) getDatas()[index];
            StringBuffer buf = new StringBuffer(data.length);

            for (int i = 0; i < data.length; i++) {
                buf.append((char) data[i]);
            }
            return buf.toString();
        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element table = new Element("nameindex");
            table.setAttribute("count", String.valueOf(getCount()));
            // table.setAttribute("offsetsize", String.valueOf(offSize));

            for (int i = 0; i < getDatas().length; i++) {
                Element edata = new Element("name");
                table.addContent(edata);
                edata.setAttribute("id", String.valueOf(i));
                edata.setAttribute("value", String.valueOf(getName(i)));
            }
            return table;
        }
    }

    /**
     * This contains the top-level DICTs of all the fonts
     * in the FontSet stored in an INDEX structure.
     */
    class TopDictINDEX extends INDEX {

        /**
         * Create a new object.
         *
         * @param offset    the offset
         * @param rar       the input
         * @throws IOException if an IO-error occurs.
         */
        public TopDictINDEX(final int offset, final RandomAccessR rar)
                throws IOException {

            super(offset, rar);

            if (getDatas().length > 0) {
                short[] data = (short[]) getDatas()[0];

                RandomAccessInputArray arar = new RandomAccessInputArray(data);

                ArrayList list = new ArrayList();
                try {
                    // read until end of input -> IOException
                    while (true) {
                        T2Operator op = T2CharString.readTopDICTOperator(arar);
                        list.add(op);
                    }
                } catch (IOException e) {
                    values = new T2Operator[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        values[i] = (T2Operator) list.get(i);
                    }
                }
            }
        }

        /**
         * values
         */
        private T2Operator[] values;

        /**
         * Returns the values.
         * @return Returns the values.
         */
        public T2Operator[] getValues() {

            return values;
        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element table = new Element("topdictindex");
            table.setAttribute("count", String.valueOf(getCount()));
            for (int i = 0; i < values.length; i++) {
                table.addContent(values[i].toXML());
            }
            return table;
        }

    }

    /**
     * String INDEX
     *
     * <p>
     * All the strings, with the exception of the FontName
     * and CIDFontName strings which appear in the Name INDEX,
     * used by different fonts within the FontSet are collected
     * together into an INDEX structure and are referenced by
     * a 2-byte unsigned number called a string identifier or SID.
     * Only unique strings are stored in the table thereby removing
     * duplication across fonts. Further space saving is obtained by
     * allocating commonly occurring strings to predefined SIDs.
     * These strings, known as the standard strings, describe all
     * the names used in the ISOAdobe and Expert character sets along
     * with a few other strings common to Type 1 fonts.
     * </p>
     * <p>
     * The client program will contain an array of standard strings
     * with nStdStrings elements. Thus, the standard strings take
     * SIDs in the range 0 to (nStdStrings  1). The first string
     * in the String INDEX corresponds to the SID whose value is
     * equal to nStdStrings, the first non-standard string, and so on.
     * When the client needs to determine the string that corresponds
     * to a particular SID it performs the following: test if SID
     * is in standard range then fetch from internal table, otherwise,
     * fetch string from the String INDEX using a value of
     * (SID  nStdStrings) as the index. An SID is defined as a 2-byte
     * unsigned number but only takes values in the range 0 64999,
     * inclusive. SID values 65000 and above are available for
     * implementation use. A FontSet with zero non-standard strings
     * is represented by an empty INDEX.
     * </p>
     */
    class StringIndex extends INDEX {

        /**
         * Create a new object.
         *
         * @param offset    the offset
         * @param rar       the input
         * @throws IOException if an IO-error occurs.
         */
        public StringIndex(final int offset, final RandomAccessR rar)
                throws IOException {

            super(offset, rar);

            values = new String[getDatas().length];

            for (int i = 0; i < getDatas().length; i++) {
                short[] tmp = (short[]) getDatas()[i];
                System.out.print(" array[" + i + "] ");
                tmp_print(tmp);
                values[i] = convert(tmp);
            }
        }

        /**
         * Convert the array to a string.
         * @param data  the data-array
         * @return Returns the String.
         * @throws IOException if an IO-error occurs.
         */
        private String convert(final short[] data) throws IOException {

            RandomAccessInputArray arar = new RandomAccessInputArray(data);

            int sid = arar.readUnsignedShort();

            System.out.println("sid = " + sid);

            return "";
        }

        /**
         * the values
         */
        private String[] values;

        /**
         * Returns the values.
         * @return Returns the values.
         */
        public String[] getValues() {

            return values;
        }

        /**
         * Returns the String.
         * @param sid   the SID for the string.
         * @return Returns the String.
         */
        public String getString(final int sid) {

            if (sid < T2StandardStrings.getHighestSID()) {
                return T2StandardStrings.getString(sid);
            }
            // incomplete
            return null;
        }

        /**
         * @see de.dante.util.XMLConvertible#toXML()
         */
        public Element toXML() {

            Element table = new Element("stringindex");
            table.setAttribute("count", String.valueOf(getCount()));
            return table;
        }
    }

    /**
     * print the array (only for test)
     * @param data  the array
     */
    private void tmp_print(final short[] data) {

        for (int i = 0; i < data.length; i++) {
            System.out.print("0x" + Integer.toHexString(data[i]) + " ");
            System.out.print("[");
            char c = (char) data[i];
            if (Character.isLetterOrDigit(c)) {
                System.out.print(c);
            }
            System.out.print("]  ");
        }
        System.out.println();
    }

}