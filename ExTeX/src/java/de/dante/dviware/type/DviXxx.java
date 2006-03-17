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
public class DviXxx extends AbstractDviCode {

    /**
     * The field <tt>content</tt> contains the ...
     */
    private byte[] content;

    /**
     * Creates a new object.
     */
    public DviXxx(byte[] content) {

        super();
        this.content = content;
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        int len;
        if (content.length < 1) {
            return 0;
        } else if (content.length < 256) {
            len = 2;
            stream.write(Dvi.XXX1);
            stream.write(content.length);
        } else if (content.length < 0xffff) {
            len = 3;
            stream.write(Dvi.XXX2);
            write2(stream,content.length);
        } else if (content.length < 0xffffff) {
            len = 4;
            stream.write(Dvi.XXX2);
            write3(stream,content.length);
        } else {
            len = 5;
            stream.write(Dvi.XXX4);
            write4(stream,content.length);
        }
        return len + content.length;
    }

}
