/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */

package de.dante.util.file;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * InputLineDecoder for a reader.
 *
 * <p>It ignore the encoding!</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class InputLineDecodeReader implements InputLineDecoder {

    /**
     * the reader
     */
    private LineNumberReader in;

    /**
     * Create a new object.
     *
     * @param   reader  the inout reader
     */
    public InputLineDecodeReader(final Reader reader) {

        in = new LineNumberReader(reader);
    }

    /**
     * Read a line from the reader (ignore encoding)!
     * @see de.dante.util.file.InputLineDecoder#readLine(java.lang.String)
     */
    public CharBuffer readLine(final String encoding) throws IOException {

        if (in == null) {
            return null;
        }
        String line = in.readLine() + "\r"; // add CR for a tex-line
        CharBuffer charbuffer = CharBuffer.wrap(line);
        return charbuffer;
    }

    /**
     * @see de.dante.util.file.InputLineDecoder#close()
     */
    public void close() throws IOException {

        if (in == null) {
            return;
        }
        in.close();
        in = null;
    }

    /**
     * @see de.dante.util.file.InputLineDecoder#getLineNumber()
     */
    public int getLineNumber() {

        return in.getLineNumber();
    }
}