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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;

/**
 * InputLineDecoder for a stream.
 *
 * <p>
 * Read a line from a <code>BufferedInputStream</code>
 * as a <code>ByteArray</code> and decode it to a
 * <code>CharArray</code>.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */

public class InputLineDecodeStream implements InputLineDecoder {

    /**
     * the inputstream
     */
    private InputStream in;

    /**
     * the linenumber
     */
    private int linenumber = 0;

    /**
     * Create a new object
     * @param   input the inputstream for reading
     */
    public InputLineDecodeStream(final InputStream input) {

        in = input;
    }

    /**
     * buffer for the <code>ArrayList</code> for a line
     */
    private static final int ARRAYLISTBUFFER = 0x1000;

    /**
     * markbuffer
     */
    private static final int MARK = 10;

    /**
     * @see de.dante.util.file.InputLineDecoder#readLine(java.lang.String)
     */
    public CharBuffer readLine(final String encoding) throws IOException {

        if (in == null) {
            return null;
        }
        if (encoding == null) {
            throw new IllegalArgumentException("encoding is null!");
        }

        if (!Charset.isSupported(encoding)) {
            throw new IllegalArgumentException("encoding not supported!");
        }

        // ArrayList for the bytes (initsize 0x1000)
        ArrayList barr = new ArrayList(ARRAYLISTBUFFER);

        // read until '\r' or '\n'
        int ib;
        while ((ib = in.read()) != -1) {
            char c = (char) ib;
            if (c == '\r' || c == '\n') {
                in.mark(MARK);
                int c2 = in.read();
                if (c2 == -1) {
                    break;
                }
                if (c2 != '\n') {
                    in.reset();
                } else {
                    if (c != '\r') {
                        in.reset();
                    }
                }

                // add '\r' for a tex-line
                barr.add(new Byte((byte) '\r'));
                break;
            }
            barr.add(new Byte((byte) ib));
        }

        if (barr.size() == 0) {
            return null;
        }

        // copy to bytearray
        byte[] nbarr = new byte[barr.size()];
        for (int i = 0; i < barr.size(); i++) {
            nbarr[i] = ((Byte) barr.get(i)).byteValue();
        }
        barr = null;

        ByteBuffer bytebuffer = ByteBuffer.wrap(nbarr);

        Charset cs = Charset.forName(encoding);
        CharsetDecoder cd = cs.newDecoder();
        CharBuffer charbuffer = cd.decode(bytebuffer);
        linenumber++;
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

        return linenumber;
    }
}