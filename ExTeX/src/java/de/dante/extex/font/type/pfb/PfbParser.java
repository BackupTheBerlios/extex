/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.font.type.pfb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontIOException;
import de.dante.extex.font.type.pfb.exception.PfbIncorrectRecordTypeException;
import de.dante.extex.font.type.pfb.exception.PfbStartMarkerMissingException;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Parser for a pfb-file.
 * 
 * <p>Adobe Type 1 Font Format</p>
 * <p>see 2.2 Font Dictionary</p>
 * <p>Type 1 Font Programm [ASCII - eexec encryption (Binary only) - ASCII]<p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class PfbParser implements XMLWriterConvertible, Serializable {

    /**
     * Create a new object.
     * @param filename  the file name
     * @throws FontException if an font error occurs.
     */
    public PfbParser(final String filename) throws FontException {

        try {
            parsePfb(new RandomAccessInputFile(filename));
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        }
    }

    /**
     * Create a new object.
     * @param in   The input.
     * @throws FontException if an IO-error occurs.
     */
    public PfbParser(final InputStream in) throws FontException {

        try {
            parsePfb(new RandomAccessInputStream(in));
        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        }
    }

    /**
     * the pdf header length:
     * (start-marker (1 byte), ascii-/binary-marker (1 byte), size (4 byte))
     * 3*6 == 18
     */
    private static final int PFB_HEADER_LENGTH = 18;

    /**
     * the start marker
     */
    private static final int START_MARKER = 0x80;

    /**
     * the ascii marker
     */
    private static final int ASCII_MARKER = 0x01;

    /**
     * the binary marker
     */
    private static final int BINARY_MARKER = 0x02;

    /**
     * The record types in the pfb-file.
     */
    private static final int[] PFB_RECORDS = {ASCII_MARKER, BINARY_MARKER,
            ASCII_MARKER};

    /**
     * the parsed pfb-data.
     */
    private byte[] pfbdata;

    /**
     * the lengths of the records
     */
    private int[] lengths;

    /**
     * the stasrts of the records
     */
    private int[] starts;

    // sample (pfb-file)
    // 00000000 80 01 8b 15  00 00 25 21  50 53 2d 41  64 6f 62 65  

    /**
     * Parse the pfb-array.
     * @param pfb   The pfb-Array
     * @throws IOException in an IO-error occurs.
     * @throws FontException if a font error occurs.
     */
    private void parsePfb(final RandomAccessR rar) throws IOException,
            FontException {

        pfbdata = new byte[(int) (rar.length() - PFB_HEADER_LENGTH)];
        lengths = new int[PFB_RECORDS.length];
        starts = new int[PFB_RECORDS.length];
        int pointer = 0;
        for (int records = 0; records < PFB_RECORDS.length; records++) {

            if (rar.readByteAsInt() != START_MARKER) {
                throw new PfbStartMarkerMissingException();
            }

            if (rar.readByteAsInt() != PFB_RECORDS[records]) {
                throw new PfbIncorrectRecordTypeException();
            }

            starts[records] = pointer;
            int size = rar.readByteAsInt();
            size += rar.readByteAsInt() << 8;
            size += rar.readByteAsInt() << 16;
            size += rar.readByteAsInt() << 24;
            lengths[records] = size;
            rar.readFully(pfbdata, pointer, size);
            pointer += size;
        }
    }

    /**
     * Returns the part of the array
     * @params  idx the part index
     * @return Returns the part of the array
     */
    public byte[] getPart(final int idx) {

        byte[] tmp = new byte[lengths[idx]];
        System.arraycopy(pfbdata, starts[idx], tmp, 0, lengths[idx]);
        return tmp;
    }

    /**
     * Returns the lengths.
     * @return Returns the lengths.
     */
    public int[] getLengths() {

        return lengths;
    }

    /**
     * Returns the pfbdata.
     * @return Returns the pfbdata.
     */
    public byte[] getPfbdata() {

        return pfbdata;
    }

    /**
     * Returns the pfb data as stream.
     * @return Returns the pfb data as stream.
     */
    public InputStream getInputStream() {

        return new ByteArrayInputStream(pfbdata);
    }

    /**
     * Returns the size of the pfb-data.
     * @return Returns the size of the pfb-data.
     */
    public int size() {

        return pfbdata.length;
    }

    /**
     * the encoding string 
     */
    private static final String ENCODING = "/Encoding ";

    /**
     * the dup string 
     */
    private static final String DUP = "dup ";

    /**
     * the put string 
     */
    private static final String PUT = "put";

    /**
     * Returns the encoding (id - glyphanme).
     * @return Returns the encoding (id - glyphanme).
     */
    public String[] getEncoding() {

        String s = new String(getPart(0));

        // read '/Encoding 256 array' 
        int pos = s.indexOf(ENCODING);
        int size = 0;
        if (pos >= 0) {
            pos += ENCODING.length();
            StringBuffer b = new StringBuffer();
            char c;
            do {
                c = s.charAt(pos++);
                b.append(c);
            } while (c != ' ');
            try {
                size = Integer.parseInt(b.toString().trim());
            } catch (Exception e) {
                // use zero
                size = 0;
            }
        }
        String[] enc = new String[size];

        // search 'dup 32/space put'
        while (pos < s.length()) {
            int start = -1;
            pos = s.indexOf(DUP, pos);
            if (pos >= 0) {
                pos += DUP.length();
                start = pos;
            }
            int stop = -1;
            pos = s.indexOf(PUT, pos);
            if (pos >= 0) {
                stop = pos;
                pos += PUT.length();
            }
            if (start >= 0 && stop >= 0) {
                insert(enc, s.substring(start, stop));
            } else {
                break;
            }
        }

        return enc;
    }

    /**
     * Insert the values 'dup 32/space put' into the string array.
     * @param enc  the string array
     * @param s    the string
     */
    private void insert(final String[] enc, final String s) {

        String[] split = s.split("/");
        try {
            int nr = Integer.parseInt(split[0].trim());
            enc[nr] = split[1].trim();
        } catch (Exception e) {
            // ignore
        }
    }

    //    /**
    //     * only for test
    //     * @param args  the commandline
    //     * @throws Exception if an error occurs.
    //     */
    //    public static void main(String[] args) throws Exception {
    //
    //        PfbParser p = new PfbParser("/home/mgn/extex/src/font/lmr12.pfb");
    //        System.out.println(new String(p.getPart(0)));
    //        System.out.println("----------");
    //        //        System.out.println(new String(p.getPart(2)));
    //        //        System.out.println("fertig!");
    //        String[] x = p.getEncoding();
    //        for (int i = 0; i < x.length; i++) {
    //            System.out.println(i + "  " + x[i]);
    //        }
    //    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("pfb");
        writer.writeStartElement("encoding");
        String[] enc = getEncoding();
        for (int i = 0; i < enc.length; i++) {
            writer.writeStartElement("enc");
            writer.writeAttribute("id", String.valueOf(i));
            writer.writeAttribute("name", enc[i] == null ? "" : enc[i]);
            writer.writeEndElement();
        }
        writer.writeEndElement();
        for (int i = 0; i < lengths.length; i++) {
            writer.writeStartElement("part");
            writer.writeAttribute("number", String.valueOf(i));
            // write binary as Base64 data in a CDATA
            if (PFB_RECORDS[i] == ASCII_MARKER) {
                writer.writeCDATA(getPart(i));
            } else {
     //           writer.writeCDATA(Base64.encode(getPart(i)));
            }
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}
