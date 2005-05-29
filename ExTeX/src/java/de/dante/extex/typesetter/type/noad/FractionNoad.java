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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * This Noad represents some mathematical material stacked above some other
 * mathematical material.
 *
 * @see "TTP [683]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class FractionNoad extends AbstractNoad implements Noad {

    /**
     * The field <tt>denominator</tt> contains the denominator part.
     */
    private MathList denominator;

    /**
     * The field <tt>nominator</tt> contains the numerator part.
     */
    private MathList numerator;

    /**
     * The field <tt>thickness</tt> contains the thickness of the fraction rule.
     * The value <code>null</code> indicates that the default rule thickness of
     * the current size should be used.
     */
    private Dimen thickness = null;

    /**
     * The field <tt>leftDelimiter</tt> contains the left delimiter or
     * <code>null</code> if none is set.
     */
    private MathDelimiter leftDelimiter;

    /**
     * The field <tt>rightDelimiter</tt> contains the right delimiter or
     * <code>null</code> if none is set
     */
    private MathDelimiter rightDelimiter;

    /**
     * Creates a new object.
     *
     * @param denom the denominator
     * @param num the numerator
     * @param leftDelimiter the delimiter for the left side or
     *  <code>null</code> for none
     * @param rightDelimiter the delimiter for the right side or
     *  <code>null</code>  for none
     * @param thickness the thickness of the rule or <code>null</code> for the
     *  default thickness
     */
    public FractionNoad(final MathList denom, final MathList num,
            final MathDelimiter leftDelimiter, final MathDelimiter rightDelimiter,
            final Dimen thickness) {

        super();
        this.denominator = denom;
        this.numerator = num;
        this.leftDelimiter = leftDelimiter;
        this.rightDelimiter = rightDelimiter;
        this.thickness = thickness;
    }

    /**
     * @see "TTP [697]"
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    public void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("fraction, thickness ");
        if (thickness == null) {
            sb.append("= default");
        } else {
            thickness.toString(sb);
        }
        if (leftDelimiter != null) {
            sb.append(", left delimiter ");
            leftDelimiter.toString(sb);
        }
        if (rightDelimiter != null) {
            sb.append(", right delimiter ");
            rightDelimiter.toString(sb);
        }
        toStringSubsidiaray(sb, numerator, depth, "\\");
        toStringSubsidiaray(sb, denominator, depth, "/");
    }

    /**
     * @see "TTP [704,743]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        //TODO gene: typeset() unimplemented
        throw new RuntimeException("unimplemented");
    }

}