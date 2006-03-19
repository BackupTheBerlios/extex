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

package de.dante.dviware.type;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.dviware.Dvi;

/**
 * This class represents the DVI instruction <tt>fnt</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DviFnt extends AbstractDviCode {

    /**
     * The field <tt>index</tt> contains the index of the font to use.
     */
    private int index;

    /**
     * Creates a new object.
     *
     * @param index the index of the font to use
     */
    public DviFnt(final int index) {

        super();
        this.index = index;
    }

    /**
     * @see de.dante.dviware.type.DviCode#getName()
     */
    public String getName() {

        return "fnt" + variant(index);
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        return opcode(Dvi.FNT1, index, stream);
    }

}
