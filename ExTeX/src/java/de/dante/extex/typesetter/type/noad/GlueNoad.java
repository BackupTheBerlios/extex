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

import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.util.configuration.ConfigurationException;


/**
 * This Noad carries a muglue value. This value is translated into a GlueNode
 * with the translated glue value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
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
     *
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) throws ConfigurationException {

        list.add(new GlueNode(mathContext.convert(muglue)));
    }

}
