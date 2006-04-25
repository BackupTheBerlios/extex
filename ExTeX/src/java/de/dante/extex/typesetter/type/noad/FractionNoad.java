/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import java.util.logging.Logger;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathFontParameter;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad represents some mathematical material stacked above some other
 * mathematical material.
 *
 * @see "TTP [683]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class FractionNoad extends AbstractNoad {

    /**
     * The field <tt>denominator</tt> contains the denominator part.
     */
    private MathList denominator;

    /**
     * The field <tt>leftDelimiter</tt> contains the left delimiter or
     * <code>null</code> if none is set.
     */
    private MathDelimiter leftDelimiter;

    /**
     * The field <tt>numerator</tt> contains the numerator part.
     */
    private MathList numerator;

    /**
     * The field <tt>tc</tt> contains the typesetting context.
     */
    private TypesettingContext tc;

    /**
     * The field <tt>rightDelimiter</tt> contains the right delimiter or
     * <code>null</code> if none is set
     */
    private MathDelimiter rightDelimiter;

    /**
     * The field <tt>thickness</tt> contains the thickness of the fraction rule.
     * The value <code>null</code> indicates that the default rule thickness of
     * the current size should be used.
     */
    private FixedDimen thickness = null;

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
     * @param tc the typesetting context for the rule
     */
    public FractionNoad(final MathList denom, final MathList num,
            final MathDelimiter leftDelimiter,
            final MathDelimiter rightDelimiter, final FixedDimen thickness,
            final TypesettingContext tc) {

        super();
        this.denominator = denom;
        this.numerator = num;
        this.leftDelimiter = leftDelimiter;
        this.rightDelimiter = rightDelimiter;
        this.thickness = thickness;
        this.tc = tc;
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
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public int typeset(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        NodeList vlist = new VerticalListNode();

        HorizontalListNode num = new HorizontalListNode();
        StyleNoad style = mathContext.getStyle();
        mathContext.setStyle(style.num());
        numerator.typeset(noads, index, num, mathContext, logger);

        mathContext.setStyle(style.denom());
        HorizontalListNode den = new HorizontalListNode();
        denominator.typeset(noads, index, den, mathContext, logger);
        mathContext.setStyle(style);

        Dimen wNum = num.getWidth();
        Dimen wDen = den.getWidth();
        if (wNum.lt(wDen)) {
            WideGlue wg = new WideGlue();
            num.add(0, new GlueNode(FixedGlue.S_S, true));
            num.add(new GlueNode(FixedGlue.S_S, true));
            num.addWidthTo(wg);
            wDen.subtract(wNum);
            num.spreadWidth(wDen, wg.getStretch());
            wNum = wDen;
        } else if (wNum.gt(wDen)) {
            WideGlue wg = new WideGlue();
            den.add(0, new GlueNode(FixedGlue.S_S, true));
            den.add(new GlueNode(FixedGlue.S_S, true));
            den.addWidthTo(wg);
            wNum.subtract(wDen);
            den.spreadWidth(wNum, wg.getStretch());
        }

        vlist.add(num);
        if (thickness == null) {
            thickness = mathContext
                    .mathParameter(MathFontParameter.DEFAULT_RULE_THICKNESS);
        }

        if (!thickness.ne(Dimen.ZERO)) {
            vlist.add(new RuleNode(wNum, thickness, Dimen.ZERO_PT, tc, true));
        }

        vlist.add(den);
        //TODO gene: adjust the move of num and den

        if (leftDelimiter != null) {
            leftDelimiter.typeset(list, mathContext, vlist.getHeight(), vlist
                    .getDepth());
        }

        list.add(vlist);

        if (rightDelimiter != null) {
            rightDelimiter.typeset(list, mathContext, vlist.getHeight(), vlist
                    .getDepth());
        }

        return index + 1;
    }

}
