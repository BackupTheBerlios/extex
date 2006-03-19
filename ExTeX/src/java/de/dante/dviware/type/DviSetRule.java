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
 * This class represents the DVI instruction <tt>set_rule</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DviSetRule extends AbstractDviCode {

    /**
     * The field <tt>a</tt> contains the ...
     */
    private int a;

    /**
     * The field <tt>b</tt> contains the ...
     */
    private int b;

    /**
     * Creates a new object.
     *
     * @param a ...
     * @param b ...
     */
    public DviSetRule(final int a, final int b) {

        super();
        this.a = a;
        this.b = b;
    }

    /**
     * @see de.dante.dviware.type.DviCode#getName()
     */
    public String getName() {

        return "set_rule";
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        stream.write(Dvi.SET_RULE);
        write4(stream, a);
        write4(stream, b);
        return 9;
    }

}
