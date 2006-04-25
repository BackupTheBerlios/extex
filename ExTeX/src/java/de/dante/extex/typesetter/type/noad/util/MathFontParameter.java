/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.noad.util;

/***
 * This class provides symbolic constants for the font parameters used in
 * math mode.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class MathFontParameter {

    /**
     * The constant <tt>MATH_X_HEIGHT</tt> contains the height of `x'.
     */
    public static final MathFontParameter MATH_X_HEIGHT = new MathFontParameter(
            true, "5");

    /**
     * The constant <tt>MATH_QUAD</tt> contains 18mu.
     */
    public static final MathFontParameter MATH_QUAD = new MathFontParameter(
            true, "6");

    /**
     * The constant <tt>NUM1</tt> contains the numerator shift-up in
     * display styles.
     */
    public static final MathFontParameter NUM1 = new MathFontParameter(true, "8");

    /**
     * The constant <tt>NUM2</tt> contains the numerator shift-up in non-display,
     * non-\atop.
     */
    public static final MathFontParameter NUM2 = new MathFontParameter(true, "9");

    /**
     * The constant <tt>NUM3</tt> contains the numerator shift-up in non-display
     * \atop.
     */
    public static final MathFontParameter NUM3 = new MathFontParameter(true,
            "10");

    /**
     * The constant <tt>DENOM1</tt> contains the denominator shift-down in
     * display styles.
     */
    public static final MathFontParameter DENOM1 = new MathFontParameter(true,
            "11");

    /**
     * The constant <tt>DENOM2</tt> contains the denominator shift-down in
     * non-display styles.
     */
    public static final MathFontParameter DENOM2 = new MathFontParameter(true,
            "12");

    /**
     * The constant <tt>SUP1</tt> contains the superscript shift-up in uncramped
     * display style.
     */
    public static final MathFontParameter SUP1 = new MathFontParameter(true,
            "13");

    /**
     * The constant <tt>SUP2</tt> contains the superscript shift-up in
     * uncramped non-display.
     */
    public static final MathFontParameter SUP2 = new MathFontParameter(true,
            "14");

    /**
     * The constant <tt>SUP3</tt> contains the superscript shift-up in
     * cramped styles.
     */
    public static final MathFontParameter SUP3 = new MathFontParameter(true,
            "15");

    /**
     * The constant <tt>SUB1</tt> contains the subscript shift-down if
     * superscript is absent.
     */
    public static final MathFontParameter SUB1 = new MathFontParameter(true,
            "16");

    /**
     * The constant <tt>SUB2</tt> contains the subscript shift-down if
     * superscript is present.
     */
    public static final MathFontParameter SUB2 = new MathFontParameter(true,
            "17");

    /**
     * The constant <tt>SUP_DROP</tt> contains the superscript baseline below
     * top of large box.
     */
    public static final MathFontParameter SUP_DROP = new MathFontParameter(true,
            "18");

    /**
     * The constant <tt>SUB_DROP</tt> contains the subscript baseline below
     * bottom of large box.
     */
    public static final MathFontParameter SUB_DROP = new MathFontParameter(true,
            "19");

    /**
     * The constant <tt>DELIM1</tt> contains the size of <tt>\atopwithdelims</tt>
     * delimiters in display styles.
     */
    public static final MathFontParameter DELIM1 = new MathFontParameter(true,
            "20");

    /**
     * The constant <tt>DELIM2</tt> contains the size of <tt>\atopwithdelims</tt>
     * delimiters in non-displays.
     */
    public static final MathFontParameter DELIM2 = new MathFontParameter(true,
            "21");

    /**
     * The constant <tt>AXIS_HEIGHT</tt> contains the height of fraction lines
     * above the baseline.
     */
    public static final MathFontParameter AXIS_HEIGHT = new MathFontParameter(
            true, "22");

    /**
     * The constant <tt>DEFAULT_RULE_THICKNESS</tt> contains the thickness of
     * <tt>\over</tt> bars.
     */
    public static final MathFontParameter DEFAULT_RULE_THICKNESS = new MathFontParameter(
            false, "8");

    /**
     * The constant <tt>BIG_OP_SPACING1</tt> contains the minimum clearance
     * above a displayed op.
     */
    public static final MathFontParameter BIG_OP_SPACING1 = new MathFontParameter(
            false, "9");

    /**
     * The constant <tt>BIG_OP_SPACING2</tt> contains the minimum clearance
     * below a displayed op.
     */
    public static final MathFontParameter BIG_OP_SPACING2 = new MathFontParameter(
            false, "10");

    /**
     * The constant <tt>BIG_OP_SPACING3</tt> contains the minimum baselineskip
     * above displayed op.
     */
    public static final MathFontParameter BIG_OP_SPACING3 = new MathFontParameter(
            false, "11");

    /**
     * The constant <tt>BIG_OP_SPACING4</tt> contains the minimum baselineskip
     * below displayed op.
     */
    public static final MathFontParameter BIG_OP_SPACING4 = new MathFontParameter(
            false, "12");

    /**
     * The constant <tt>BIG_OP_SPACING5</tt> contains the padding above and
     * below displayed limits.
     */
    public static final MathFontParameter BIG_OP_SPACING5 = new MathFontParameter(
            false, "13");

    /**
     * The field <tt>inSymbol</tt> contains the indicator that the parameter
     * should be taken from the symbol font. Otherwise it is taken from the
     * extension font.
     */
    private boolean inSymbol;

    /**
     * The field <tt>no</tt> contains the number of the font parameter as string.
     */
    private String no;

    /**
     * Creates a new object.
     *
     * @param inSymbol the indicator that the parameter should be taken from
     *  the symbol font. Otherwise it is taken from the extension font.
     * @param no the number of the font parameter as string.
     */
    private MathFontParameter(final boolean inSymbol, final String no) {

        super();
        this.inSymbol = inSymbol;
        this.no = no;
    }

    /**
     * Getter for inSymbol.
     *
     * @return the inSymbol indicator
     */
    public boolean inSymbol() {

        return this.inSymbol;
    }

    /**
     * Getter for no.
     *
     * @return the no
     */
    public String getNo() {

        return this.no;
    }

}
