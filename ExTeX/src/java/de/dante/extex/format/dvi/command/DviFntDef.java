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

package de.dante.extex.format.dvi.command;

import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * DVI: fnt_def
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class DviFntDef extends DviCommand {

    /**
     * the font (number)
     */
    private int font;

    /**
     * the checkusm
     */
    private int checksum;

    /**
     * the scalefactor
     */
    private int scale;

    /**
     * the design size
     */
    private int designsize;

    /**
     * the area
     */
    private String area;

    /**
     * the name
     */
    private String fname;

    /**
     * Create a new object.
     * @param oc    the opcode
     * @param sp    the start pointer
     * @param k     the font (number)
     * @param c     the checksum
     * @param s     the scalefactor
     * @param d     the designsize
     * @param a     the area
     * @param l     the name
     */
    public DviFntDef(final int oc, final int sp, final int k, final int c,
            final int s, final int d, final String a, final String l) {

        super(oc, sp);
        font = k;
        checksum = c;
        scale = s;
        designsize = d;
        area = a;
        fname = l;
    }

    /**
     * Returns the area.
     * @return Returns the area.
     */
    public String getArea() {

        return area;
    }

    /**
     * Returns the checksum.
     * @return Returns the checksum.
     */
    public int getChecksum() {

        return checksum;
    }

    /**
     * Returns the designsize.
     * @return Returns the designsize.
     */
    public int getDesignsize() {

        return designsize;
    }

    /**
     * Returns the font.
     * @return Returns the font.
     */
    public int getFont() {

        return font;
    }

    /**
     * Returns the fname.
     * @return Returns the fname.
     */
    public String getFName() {

        return fname;
    }

    /**
     * Returns the scale.
     * @return Returns the scale.
     */
    public int getScale() {

        return scale;
    }

    /**
     * Returns the scale as <code>Dimen</code>.
     * @return Returns the scale as <code>Dimen</code>.
     */
    public Dimen getScaleAsDimen() {

        return new Dimen(scale);
    }

    /**
     * round
     */
    private static final double ROUND = 0.5d;

    /**
     * Calculate the scaled from a font (with times 1000).
     *
     * @param mag   the maginification
     * @return Returns the scaled.
     */
    public int getScaled(final int mag) {

        return (int) ((double) mag * scale / designsize + ROUND);
    }

    /**
     * Returns the scaled from a font as <code>Count</code>.
     * @param mag   the maginification
     * @return Returns the scaled from a font as <code>Count</code>.
     */
    public Count getScaledAsCount(final int mag) {

        return new Count(getScaled(mag));
    }

    /**
     * fnt_def_1
     */
    private static final int FNT_DEF1 = 243;

    /**
     * @see de.dante.extex.format.dvi.command.DviCommand#getName()
     */
    public String getName() {

        StringBuffer buf = new StringBuffer();
        buf.append("fntdef").append(getOpcode() - FNT_DEF1 + 1);
        return buf.toString();
    }
}
