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

import de.dante.extex.font.type.tfm.exception.TFMReadFileException;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class for TFM header length table.
 *
 * <p>
 * The first 24 bytes of a TFM file contain
 * the lengths of the various subsequent portions of the file.
 * (12 x 16-bit interger) or (6 words x 32 bit)
 * </p>
 *
 * <table border="1">
 *  <thead>
 *    <tr><td><b>name</b></td><td><b>description</b></td></tr>
 *  </thead
 *  <tr><td>lf</td><td>length of the entire file, in words</td></tr>
 *  <tr><td>lh</td><td>length of the header data, in words</td></tr>
 *  <tr><td>bc</td><td>smallest character code in the font</td></tr>
 *  <tr><td>ec</td><td>largest character code in the font</td></tr>
 *  <tr><td>nw</td><td>number of words in the width table</td></tr>
 *  <tr><td>nh</td><td>number of words in the height table</td></tr>
 *  <tr><td>nd</td><td>number of words in the depth table</td></tr>
 *  <tr><td>ni</td><td>number of words in the italic correction table</td></tr>
 *  <tr><td>nl</td><td>number of words in the lig/kern table</td></tr>
 *  <tr><td>nk</td><td>number of words in the kern table</td></tr>
 *  <tr><td>ne</td><td>number of words in the extensible character table</td></tr>
 *  <tr><td>np</td><td>number of font parameter words</td></tr>
 * </table>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class TFMHeaderLengths implements XMLWriterConvertible, Serializable {

    /**
     * max chars
     */
    private static final int MAXCHARS = 255;

    /**
     * Bytes in the Header of the TFM-file
     */
    private static final int HEADERBYTES = 6;

    /**
     * length of the entire file
     */
    private short lf;

    /**
     * length of the header data
     */
    private short lh;

    /**
     * smallest character code in the font
     */
    private short bc;

    /**
     * largest character code in the font
     */
    private short ec;

    /**
     * number of words in the width table
     */
    private short nw;

    /**
     * number of words in the height table
     */
    private short nh;

    /**
     * number of words in the depth table
     */
    private short nd;

    /**
     * number of words in the italic correction table
     */
    private short ni;

    /**
     * number of words in the lig/kern table
     */
    private short nl;

    /**
     * number of words in the kern table
     */
    private short nk;

    /**
     * number of words in the extensible character table
     */
    private short ne;

    /**
     * number of font parameter words
     */
    private short np;

    /**
     * number of character
     */
    private int cc;

    /**
     * Create a new object
     * @param rar   the input
     * @throws IOException if an IO-error occurs.
     */
    public TFMHeaderLengths(final RandomAccessR rar) throws IOException {

        lf = rar.readShort();
        lh = rar.readShort();
        bc = rar.readShort();
        ec = rar.readShort();
        nw = rar.readShort();
        nh = rar.readShort();
        nd = rar.readShort();
        ni = rar.readShort();
        nl = rar.readShort();
        nk = rar.readShort();
        ne = rar.readShort();
        np = rar.readShort();

        // check
        if (lf == 0
                || lf < HEADERBYTES
                || lh < 2
                || (bc > ec + 1 || ec > MAXCHARS)
                || (ne > MAXCHARS + 1)
                || ((HEADERBYTES + lh + (ec - bc + 1) + nw + nh + nd + ni + nl
                        + nk + ne + np) != lf)
                || (nw == 0 || nh == 0 || nd == 0 || ni == 0)) {
            throw new TFMReadFileException();
        }

        cc = ec + 1 - bc;

        if (cc == 0) {
            bc = 0;
        }
    }

    /**
     * Returns the bc.
     * @return Returns the bc.
     */
    public short getBc() {

        return bc;
    }

    /**
     * Returns the cc.
     * @return Returns the cc.
     */
    public int getCc() {

        return cc;
    }

    /**
     * Returns the ec.
     * @return Returns the ec.
     */
    public short getEc() {

        return ec;
    }

    /**
     * Returns the lf.
     * @return Returns the lf.
     */
    public short getLf() {

        return lf;
    }

    /**
     * Returns the lh.
     * @return Returns the lh.
     */
    public short getLh() {

        return lh;
    }

    /**
     * Returns the nd.
     * @return Returns the nd.
     */
    public short getNd() {

        return nd;
    }

    /**
     * Returns the ne.
     * @return Returns the ne.
     */
    public short getNe() {

        return ne;
    }

    /**
     * Returns the nh.
     * @return Returns the nh.
     */
    public short getNh() {

        return nh;
    }

    /**
     * Returns the ni.
     * @return Returns the ni.
     */
    public short getNi() {

        return ni;
    }

    /**
     * Returns the nk.
     * @return Returns the nk.
     */
    public short getNk() {

        return nk;
    }

    /**
     * Returns the nl.
     * @return Returns the nl.
     */
    public short getNl() {

        return nl;
    }

    /**
     * Returns the np.
     * @return Returns the np.
     */
    public short getNp() {

        return np;
    }

    /**
     * Returns the nw.
     * @return Returns the nw.
     */
    public short getNw() {

        return nw;
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("headerlength");
        writer.writeAttribute("lf", String.valueOf(lf));
        writer.writeAttribute("lh", String.valueOf(lh));
        writer.writeAttribute("bc", String.valueOf(bc));
        writer.writeAttribute("ec", String.valueOf(ec));
        writer.writeAttribute("nw", String.valueOf(nw));
        writer.writeAttribute("nh", String.valueOf(nh));
        writer.writeAttribute("nd", String.valueOf(nd));
        writer.writeAttribute("ni", String.valueOf(ni));
        writer.writeAttribute("nl", String.valueOf(nl));
        writer.writeAttribute("nk", String.valueOf(nk));
        writer.writeAttribute("lf", String.valueOf(lf));
        writer.writeAttribute("ne", String.valueOf(ne));
        writer.writeAttribute("lf", String.valueOf(lf));
        writer.writeAttribute("np", String.valueOf(np));
        writer.writeAttribute("cc", String.valueOf(cc));
        writer.writeEndElement();
    }
}