/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.tfm;

import java.io.IOException;
import java.io.Serializable;

import org.jdom.Element;

import de.dante.extex.font.type.PlFormat;
import de.dante.extex.font.type.PlWriter;
import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * Class for TFM header information.
 *
 * <p>
 * header : array [0 .. (lh-1)] of stuff
 * </p>
 *
 * <table border="1">
 *   <tr><td>header[0]</td><td>a 32-bit check sum</td></tr>
 *   <tr><td>header[1]</td><td>a fix word containing the design size
 *                             of the font, in units of TEX points
 *                             (7227 TEX points =254 cm)</td></tr>
 *   <tr><td>header[2..11]</td><td>if present, contains 40 bytes that
 *                             identify the character coding scheme.</td></tr>
 *   <tr><td>header[12..16]</td><td>if present, contains 20 bytes that
 *                             name the font family.</td></tr>
 *   <tr><td>header[17]</td><td>if present, contains a first byte
 *                              called the seven_bit_safe_flag,
 *                              then two bytes that are ignored,
 *                              and a fourth byte called the face.
 *   <tr><td>header[18..(lh-1)]</td><td>might also be present:
 *                              the individual words are simply called
 *                              header[18], header[19], etc.,
 *                              at the moment.</td></tr>
 * </table>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
class TFMHeaderArray implements XMLConvertible, PlFormat, Serializable {

    /**
     * header[0]: checksum
     */
    private int checksum;

    /**
     * header[1]: design size
     */
    private TFMFixWord designsize;

    /**
     * header[2..11]: coding scheme
     */
    private String codingscheme;

    /**
     * Size of coding scheme header information in words
     */
    private static final int CODING_SCHEME_SIZE = 10;

    /**
     * the font type (VANILLA, MATHSY, MATHEX)
     */
    private TFMFontType fonttype;

    /**
     * header[12..16]: font family
     */
    private String fontfamily;

    /**
     * Size of font family header information in words
     */
    private static final int FONT_FAMILY_SIZE = 5;

    /**
     * True if only 7 bit character codes are used.
     */
    private boolean sevenBitSafe = false;

    /**
     * Font Xerox face code
     */
    private int xeroxfacecode = -1;

    /**
     * Uninterpreted rest of the header
     */
    private int[] headerrest = null;

    /**
     * header[18..]
     */
    private static final int HEADER_REST_SIZE = 18;

    /**
     * Create a new object
     * @param rar   the input
     * @param lh    length of the header data
     * @throws IOException if an IO-error occurs.
     */
    public TFMHeaderArray(final RandomAccessR rar, final short lh)
            throws IOException {

        int hr = lh;
        checksum = rar.readInt();
        hr--;
        designsize = new TFMFixWord(rar.readInt(),
                TFMFixWord.FIXWORDDENOMINATOR);
        hr--;

        // optional: coding scheme
        if (hr >= CODING_SCHEME_SIZE) {
            codingscheme = readBCPL(rar, TFMConstants.CONST_4
                    * CODING_SCHEME_SIZE);
            fonttype = new TFMFontType(codingscheme);
            hr -= CODING_SCHEME_SIZE;

            // optional: font family
            if (hr >= FONT_FAMILY_SIZE) {
                fontfamily = readBCPL(rar, TFMConstants.CONST_4
                        * FONT_FAMILY_SIZE);
                hr -= FONT_FAMILY_SIZE;
                // optional: seven_bit_safe_flag
                if (hr >= 1) {
                    sevenBitSafe = (rar.readByte() > TFMConstants.CONST_127);
                    // ignore 2 bytes
                    rar.skipBytes(2);
                    // face
                    xeroxfacecode = rar.readByteAsInt();
                    hr--;
                    // optional rest
                    if (hr > 0) {
                        headerrest = new int[hr];
                        for (int i = 0; i < hr; i++) {
                            headerrest[i] = rar.readInt();
                        }
                    }
                }
            }
        }
    }

    /**
     * Reads a character string from the header.
     * The string is stored as its length in first byte
     * then the string (the rest of area is not used).
     *
     * @param rar   the input
     * @param size  the size of string area in the header.
     * @return the string
     * @throws IOException if an I/O error occured
     */
    private String readBCPL(final RandomAccessR rar, final int size)
            throws IOException {

        int len = rar.readByte();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size - 1; i++) {
            char c = Character.toUpperCase((char) rar.readByte());
            if (i < len) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Returns the checksum.
     * @return Returns the checksum.
     */
    public int getChecksum() {

        return checksum;
    }

    /**
     * Returns the codingscheme.
     * @return Returns the codingscheme.
     */
    public String getCodingscheme() {

        return codingscheme;
    }

    /**
     * Returns the designsize.
     * @return Returns the designsize.
     */
    public TFMFixWord getDesignsize() {

        return designsize;
    }

    /**
     * Returns the face.
     * @return Returns the face.
     */
    public int getFace() {

        return xeroxfacecode;
    }

    /**
     * Returns the fontfamily.
     * @return Returns the fontfamily.
     */
    public String getFontfamily() {

        return fontfamily;
    }

    /**
     * Returns the fontype.
     * @return Returns the fontype.
     */
    public TFMFontType getFontype() {

        return fonttype;
    }

    /**
     * Returns the headerrest.
     * @return Returns the headerrest.
     */
    public int[] getHeaderrest() {

        return headerrest;
    }

    /**
     * Returns the sevenBitSafe.
     * @return Returns the sevenBitSafe.
     */
    public boolean isSevenBitSafe() {

        return sevenBitSafe;
    }

    /**
     * @see de.dante.extex.font.type.PlFormat#toPL(java.io.OutputStream)
     */
    public void toPL(final PlWriter out) throws IOException {

        if (fontfamily != null) {
            out.plopen("FAMILY").addStr(fontfamily).plclose();
        }
        if (xeroxfacecode >= 0) {
            out.plopen("FACE").addFace(xeroxfacecode).plclose();
        }

        if (headerrest != null) {
            for (int i = 0; i < headerrest.length; i++) {
                out.plopen("HEADER").addDec(i + HEADER_REST_SIZE).addOct(
                        headerrest[i]).plclose();
            }
        }
        if (codingscheme != null) {
            out.plopen("CODINGSCHEME").addStr(codingscheme).plclose();
        }
        out.plopen("DESIGNSIZE").addReal(designsize).plclose();
        out.addComment("DESIGNSIZE IS IN POINTS");
        out.addComment("OTHER SIZES ARE MULTIPLES OF DESIGNSIZE");
        out.plopen("CHECKSUM").addOct(checksum).plclose();
        if (sevenBitSafe) {
            out.plopen("SEVENBITSAFEFLAG").addBool(sevenBitSafe).plclose();
        }
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element h = new Element("header");
        h.setAttribute("checksum", String.valueOf(checksum));
        h.setAttribute("desingsize", designsize.toString());
        h.setAttribute("units", String.valueOf(TFMConstants.CONST_1000));
        if (codingscheme != null) {
            h.setAttribute("codingscheme", codingscheme);
        }
        if (fonttype != null) {
            h.setAttribute("fonttype", fonttype.toString());
        }
        if (fontfamily != null) {
            h.setAttribute("fontfamily", fontfamily);
        }
        h.setAttribute("sevenbitsafe", String.valueOf(sevenBitSafe));
        h.setAttribute("xeroxfacecode", String.valueOf(xeroxfacecode));
        if (headerrest != null) {
            for (int i = 0; i < headerrest.length; i++) {
                Element rest = new Element("header");
                rest.setAttribute("id", String.valueOf(i + HEADER_REST_SIZE));
                rest.setAttribute("value", String.valueOf(headerrest[i]));
                h.addContent(rest);
            }
        }
        return h;
    }
}