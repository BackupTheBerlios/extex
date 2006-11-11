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

import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This Noad carries a muglue value. This value is translated into a GlueNode
 * with the translated glue value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class GlueNoad extends AbstractNoad {

    /**
     * The field <tt>muglue</tt> contains the glue.
     */
    private Muskip muglue;

    /**
     * Creates a new object.
     *
     * @param muglue the glue
     */
    public GlueNoad(final Muskip muglue) {

        super();
        this.muglue = muglue;
    }

    /**
     * Getter for muglue.
     *
     * @return the muglue
     */
    public Muskip getMuglue() {

        return this.muglue;
    }

    /**
     * @see de.dante.extex.interpreter.type.muskip.Muskip#isKill()
     */
    public boolean isKill() {

        return this.muglue.isKill();
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.noad.Noad,
     *      de.dante.extex.typesetter.type.noad.NoadList,
     *      int,
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      java.util.logging.Logger)
     */
    public void typeset(final Noad previousNoad, final NoadList noads,
            final int index, final NodeList list,
            final MathContext mathContext, final Logger logger)
            throws TypesetterException,
                ConfigurationException {

        if (previousNoad instanceof GlueNoad
                && ((GlueNoad) previousNoad).isKill()) {
            StyleNoad style = mathContext.getStyle();
            if (style == StyleNoad.SCRIPTSTYLE
                    || style == StyleNoad.SCRIPTSCRIPTSTYLE) {
                return;
            }
        }

        setSpacingClass(previousNoad.getSpacingClass());
        Glue glue = mathContext.convert(muglue);
        if (!glue.eq(Glue.ZERO)) {
            list.add(new GlueNode(glue, true));
        }
    }

}
