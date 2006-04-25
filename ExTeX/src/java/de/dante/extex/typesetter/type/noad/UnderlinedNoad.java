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
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.noad.util.MathFontParameter;
import de.dante.extex.typesetter.type.node.ExplicitKernNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an underlining for the nucleus.
 *
 * @see "TTP [687]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.16 $
 */
public class UnderlinedNoad extends AbstractNucleusNoad {

    /**
     * Creates a new object.
     *
     * @param nucleus the nucleus to be underlined
     * @param tc the typesetting context for the color
     */
    public UnderlinedNoad(final Noad nucleus, final TypesettingContext tc) {

        super(nucleus, tc);
    }

    /**
     * @see "TTP [696]"
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#toStringAdd(
     *      java.lang.StringBuffer,
     *      int)
     */
    protected void toStringAdd(final StringBuffer sb, final int depth) {

        sb.append("underline");
    }

    /**
     * @see "TTP [735]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final NoadList noads, final int index,
            final NodeList list, final MathContext mathContext,
            final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        HorizontalListNode hlist = new HorizontalListNode();
        getNucleus().typeset(noads, index, hlist, mathContext, logger);

        FixedDimen thickness = mathContext
                .mathParameter(MathFontParameter.DEFAULT_RULE_THICKNESS);
        VerticalListNode vlist = new VerticalListNode();
        vlist.add(hlist);
        vlist.add(new ExplicitKernNode(new Dimen(3 * thickness.getValue()),
                false));
        vlist.add(new RuleNode(hlist.getWidth(), thickness, Dimen.ZERO_PT,
                getTypesettingContext(), true));
        vlist.add(new ExplicitKernNode(thickness, false));
        list.add(vlist);

        Dimen h = new Dimen(vlist.getHeight());
        h.add(vlist.getDepth());
        vlist.setHeight(hlist.getHeight());
        h.subtract(hlist.getHeight());
        vlist.setDepth(h);
    }

}
