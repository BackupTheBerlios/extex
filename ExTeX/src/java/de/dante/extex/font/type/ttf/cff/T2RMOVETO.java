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

package de.dante.extex.font.type.ttf.cff;

import org.jdom.Element;

/**
 * rmoveto |- dx1 dy1 rmoveto (21) |
 *
 * <p>
 * moves the current point to a position at the
 * relative coordinates (dx1, dy1).
 * </p>
 * <p>
 * Note: The first stack-clearing operator, which must be one of hstem,
 * hstemhm, vstem, vstemhm, cntrmask, hintmask, hmoveto, vmoveto,
 * rmoveto, or endchar, takes an additional argument the width
 * (as described earlier), which may be expressed as zero or one
 * numeric argument.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class T2RMOVETO extends T2PathConstruction {

    /**
     * Create a new obejct.
     */
    public T2RMOVETO() {

        super();
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#getBytes()
     */
    public short[] getBytes() {

        return null;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        return null;
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2Operator#getName()
     */
    public String getName() {

        return "rmoveto";
    }
}