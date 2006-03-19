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
 * This class represents the DVI instruction <tt>pre</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DviPreamble extends AbstractDviCode {

    /**
     * The comment <tt>MAX_COMMENT_LEN</tt> contains the maximal length of the
     * comment string.
     */
    private static final int MAX_COMMENT_LEN = 255;

    /**
     * The field <tt>comment</tt> contains the comment string.
     */
    private String comment;

    /**
     * The field <tt>mag</tt> contains the magnification.
     */
    private int mag;

    /**
     * Creates a new object.
     *
     * @param mag the magnification
     * @param comment the comment
     */
    public DviPreamble(final int mag, final String comment) {

        super();

        if (comment.length() > MAX_COMMENT_LEN) {
            throw new IllegalArgumentException("comment");
        }
        this.mag = mag;
        this.comment = comment;
    }

    /**
     * @see de.dante.dviware.type.DviCode#getName()
     */
    public String getName() {

        return "pre";
    }

    /**
     * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
     */
    public int write(final OutputStream stream) throws IOException {

        stream.write(Dvi.PRE);
        stream.write(Dvi.DVI_ID);
        write4(stream, Dvi.DVI_UNIT_NUMERATOR);
        write4(stream, Dvi.DVI_UNIT_DENOMINATOR);
        write4(stream, mag);
        int length = comment.length();
        stream.write(length);
        for (int i = 0; i < length; i++) {
            stream.write(comment.charAt(i));
        }
        return 15 + length;
    }

}
