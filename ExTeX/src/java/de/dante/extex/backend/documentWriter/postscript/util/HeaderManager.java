/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter.postscript.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The header manager keeps track of the headers for the PostScript file.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class HeaderManager {

    /**
     * The field <tt>header</tt> contains the buffer for the header.
     */
    private StringBuffer header = new StringBuffer();

    /**
     * Creates a new object.
     */
    public HeaderManager() {

        super();
    }

    /**
     * Add the contents read from a stream to the headers.
     *
     * @param stream the stream to read from
     * @param name the name of the stream
     *
     * @throws IOException in case of an error during the writing
     */
    public void add(final InputStream stream, final String name)
            throws IOException {

        header.append("%%BeginProcSet: ");
        header.append(name);
        header.append('\n');
        int c;
        while ((c = stream.read()) >= 0) {
            header.append((char) c);
        }
        header.append("%%EndProcSet \n");
    }

    /**
     * Add a string to the headers.
     *
     * @param value the string to add
     * @param name the name for the DSC
     *
     * @throws IOException in case of an error during the writing
     */
    public void add(final String value, final String name)
            throws IOException {

        header.append("%%BeginProcSet: ");
        header.append(name);
        header.append('\n');
        header.append(value);
        header.append("%%EndProcSet \n");
    }

    /**
     * Clear the header buffer and discard its previous contents.
     */
    public void reset() {

        header = new StringBuffer();
    }

    /**
     * Write the accumulated headers to the output stream.
     *
     * @param stream the target stream
     *
     * @throws IOException in case of an IO error
     */
    public void write(final OutputStream stream) throws IOException {

        stream.write(header.toString().getBytes());
    }

}
