/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

/**
 * Data structure for raw character information from tfm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TFMAuxCharInfo {

    /**
     * Create a new object.
     * 
     * @param wi    withindex
     * @param hi    heightdepthindex
     * @param ii    italicindextag
     * @param r     remainder
     */
    TFMAuxCharInfo(final byte wi, final byte hi, final byte ii, final byte r) {

        widthindex = wi;
        heightdepthindex = hi;
        italicindextag = ii;
        remainder = r;

        //            widthindex = (byte) readByte();
        //            heightdepthindex = (byte) readByte();
        //            italicindextag = (byte) readByte();
        //            remainder = (byte) readByte();
    }

    /**
     * Index to the width table
     */
    private byte widthindex;

    /**
     * Indexes to the height and depth tables.
     */
    private byte heightdepthindex;

    /**
     * Index to the italic correction table and the tag
     */
    private byte italicindextag;

    /**
     * Remainder which meaning is determined by value of tag
     */
    private byte remainder;

    /**
     * Index to newly created <code>ligKernTable</code> which is set
     * during translation of the original raw lig/kern table in the tfm
     * file.
     */
    private int ligkernstart;

    /**
     * Tells if the character of this <code>AuxCharInfo</code> exists in
     * the font.
     *
     * @return Returns <code>true</code> if the character exists.
     */
    public boolean exists() {

        return widthindex != 0;
    }

    /**
     * Gives the index to the width table from the tfm file.
     *
     * @return Returns the index to <code>widthTable</code>.
     */
    public int widthIndex() {

        return widthindex & 0xff;
    }

    /**
     * Gives the index to the height table from the tfm file.
     *
     * @return Returns the index to <code>heightTable</code>.
     */
    public int heightIndex() {

        return heightdepthindex >> 4 & 0x0f;
    }

    /**
     * Gives the index to the depth table from the tfm file.
     *
     * @return Returns the index to <code>depthTable</code>.
     */
    public int depthIndex() {

        return heightdepthindex & 0x0f;
    }

    /**
     * Gives the index to the italic correction table from the tfm file.
     *
     * @return Returns the index to <code>italicTable</code>.
     */
    public int italicIndex() {

        return italicindextag >> 2 & 0x3f;
    }

    /**
     * Gives the tag field of the character information data.
     *
     * @return Returns the tag value.
     */
    public byte tag() {

        return (byte) (italicindextag & 0x03);
    }

    /**
     * Resets the tag field to NOTAG (zero) value.
     */
    public void resetTag() {

        italicindextag &= ~0x03;
    }

    /**
     * Gives the value of remainder which meaning is dependent on the tag
     * field value.
     *
     * @return Returns the uninterpreted tag.
     */
    public short remainder() {

        return (short) (remainder & 0xff);
    }

    /**
     * Gives the remainder value interpreted as the index to the raw
     * lig/kern table from tfm file.
     *
     * @return Returns starting of the lig/kern program in <code>ligAuxTab</code>.
     */
    public int ligStart() {

        return remainder();
    }

    /**
     * Gives the remainder value interpreted as the code of next character
     * in the chain of larger characters.
     *
     * @return Returns the next larger character.
     */
    public short biggerChar() {

        return remainder();
    }

    /**
     * Gives the remainder value interpreted as the index to the table of
     * extensible recipes from tfm file.
     *
     * @return Returns the index to the <code>extAuxTab</code>.
     */
    public int extenIndex() {

        return remainder();
    }
    
    /**
     * @return Returns the ligkernstart.
     */
    public int getLigkernstart() {

        return ligkernstart;
    }
    
    /**
     * Set the new value for ligkernstart
     *
     * @param lkstart The ligkernstart to set.
     */
    public void setLigkernstart(final int lkstart) {

        this.ligkernstart = lkstart;
    }
}