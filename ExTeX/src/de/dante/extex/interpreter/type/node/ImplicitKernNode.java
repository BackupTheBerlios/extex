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
package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.dimen.Dimen;


/**
 * This class provides the same functionality as
 * {@link de.dante.extex.interpreter.type.node.KernNode KernNode} but is
 * distinguishable for the sake of some fine differentiations in TeX.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ImplicitKernNode extends KernNode {

    /**
     * Creates a new object.
     * 
     * @param kern the natural size
     */
    public ImplicitKernNode(final Dimen kern) {

        super(kern);
    }

    /**
     * @see de.dante.extex.typesetter.Node#toText(java.lang.StringBuffer,
     *      java.lang.String)
     * @see "TeX -- The Program [191]"
     */
    public void toText(final StringBuffer sb, final String prefix) {

        sb.append("kern ");
        getWidth().toString(sb);
    }


}
