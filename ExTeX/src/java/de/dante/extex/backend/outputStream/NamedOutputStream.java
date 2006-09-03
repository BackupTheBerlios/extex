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

package de.dante.extex.backend.outputStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class NamedOutputStream extends OutputStream {

    /**
     * The field <tt>name</tt> contains the ...
     */
    private String name;

    /**
     * The field <tt>stream</tt> contains the ...
     */
    private OutputStream stream;

    /**
     * Creates a new object.
     *
     * @param name the name
     * @param stream the stream
     */
    public NamedOutputStream(final String name, final OutputStream stream) {

        super();
        this.name = name;
        this.stream = stream;
    }

    /**
     * @see java.io.OutputStream#close()
     */
    public void close() throws IOException {

        this.stream.close();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {

        return this.stream.equals(obj);
    }

    /**
     * @see java.io.OutputStream#flush()
     */
    public void flush() throws IOException {

        this.stream.flush();
    }

    /**
     * Getter for the destination.
     * The destination is some printable representation describing where the
     * output went to.
     *
     * @return the name of the destination
     */
    String getName() {

        return name;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return this.stream.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return this.stream.toString();
    }

    /**
     * @see java.io.OutputStream#write(byte[])
     */
    public void write(byte[] b) throws IOException {

        this.stream.write(b);
    }

    /**
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(byte[] b, int off, int len) throws IOException {

        this.stream.write(b, off, len);
    }

    /**
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException {

        this.stream.write(b);
    }

}
