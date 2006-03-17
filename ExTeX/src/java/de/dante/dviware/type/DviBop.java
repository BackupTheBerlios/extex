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
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DviBop extends AbstractDviCode {

    /**
     * The field <tt>lastBop</tt> contains the index of the last bop in the
     * stream or -1.
     */
    private int lastBop;

    /**
     * The field <tt>pageNo</tt> contains the array of the page number
     * indicators.
     */
    private int[] pageNo;

    /**
     * Creates a new object.
     *
     * @param pageNo the array of page numbers
     * @param lastBop the index of the last bop or -1 at the beginning
     */
    public DviBop(final int[] pageNo, final int lastBop) {

        super();
        if (pageNo.length != 10) {
            throw new IllegalArgumentException("count");
        }
        this.pageNo = pageNo;
        this.lastBop = lastBop;
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        stream.write(Dvi.BOP);
        int length = pageNo.length;
        for (int i = 0; i < length; i++) {
            write4(stream, pageNo[i]);
        }
        write4(stream, lastBop);
        return 1 + 4 * (length + 1);
    }

}
