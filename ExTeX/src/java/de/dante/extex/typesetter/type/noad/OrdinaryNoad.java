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

package de.dante.extex.typesetter.type.noad;

import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.noad.util.MathContext;

/**
 * This Noad represents an ordinary character.
 *
 * @see "TTP [682]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class OrdinaryNoad extends AbstractNucleusNoad {

    /**
     * Creates a new object.
     *
     * @param nucleus the nucleus
     */
    public OrdinaryNoad(final Noad nucleus) {

        super(nucleus);
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.AbstractNoad#stringName()
     */
    protected String stringName() {

        return "mathord";
    }

    /**
     * @see "TTP [752]"
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.type.noad.Noad#visit(
     *      de.dante.extex.typesetter.type.noad.NoadVisitor)
     */
    public void visit(final NoadVisitor visitor) {

        visitor.visitOrd(this);
    }
}