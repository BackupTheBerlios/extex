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
import java.nio.CharBuffer;

/**
 * InputLineDecoder for a string.
 *
 * <p>It ignore the encoding!</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class InputLineDecodeString implements InputLineDecoder {

    /**
     * the line
     */
    private String line;

    /**
     * Create a new object.
     *
     * @param   s   the string
     */
    public InputLineDecodeString(final String s) {

        line = s;
    }

    /**
     * Read a line from the reader (ignore encoding)!
     * @see de.dante.util.file.InputLineDecoder#readLine(java.lang.String)
     */
    public CharBuffer readLine(final String encoding) throws IOException {

        if (line == null) {
            return null;
        }
        CharBuffer charbuffer = CharBuffer.wrap(line);
        line = null;
        return charbuffer;
    }

    /**
     * @see de.dante.util.file.InputLineDecoder#close()
     */
    public void close() throws IOException {

        line = null;
    }

    /**
     * @see de.dante.util.file.InputLineDecoder#getLineNumber()
     */
    public int getLineNumber() {

        return 1;
    }
}