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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;

/**
 * InputLineDecoderStream.
 * <p>
 * Read a line from a <code>BufferedInputStream</code>
 * as a <code>ByteArray</code> and decode it to a
 * <code>CharArray</code>.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */

public class InputLineDecodeStream {

    /**
     * the inputstream
     */
    private BufferedInputStream bin;

    /**
     * the linenumber
     */
    private int linenumber = 0;

    /**
     * Create a new object
     * @param   binput the inputstream for reading
     */
    public InputLineDecodeStream(final BufferedInputStream binput) {

        bin = binput;
    }

    /**
     * buffer for the <code>ArrayList</code> for a line
     */
    private static final int ARRAYLISTBUFFER = 0x1000;

    /**
     * Read a line from the inputstream.
     *
     * @param   encoding    the encoding for this line.
     * @return  the line in a <code>CharBuffer</code>
     * @throws  java.io.IOException from BufferedInputStream
     */
    public CharBuffer readLine(final String encoding) throws IOException {

        if (bin == null) {
            return null;
        }
        if (encoding == null) {
            throw new IllegalArgumentException("encoding is null!"); // TODO change
        }

        if (!Charset.isSupported(encoding)) {
            throw new IllegalArgumentException("encoding not supported!"); // TODO change
        }

        // ArrayList for the bytes (initsize 0x1000)
        ArrayList barr = new ArrayList(ARRAYLISTBUFFER);

        // read until '\r' or '\n'
        int ib;
        while ((ib = bin.read()) != -1) {
            char c = (char) ib;
            if (c == '\r' || c == '\n') {
                bin.mark(10);
                int c2 = bin.read();
                if (c2 == -1) {
                    break;
                }
                if (c2 != '\n') {
                    bin.reset();
                } else {
                    if (c != '\r') {
                        bin.reset();
                    }
                }

                // add '\r' for a tex-line
                barr.add(new Byte((byte) '\r'));
                break;
            } else {
                barr.add(new Byte((byte) ib));
            }
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
     * Close the Stram
     * @throws IOException ...
     */
    public void close() throws IOException {

        if (bin == null) {
            return;
        }
        bin.close();
        bin = null;
    }

    /**
     * @return Returns the linenumber.
     */
    public int getLineNumber() {

        return linenumber;
    }
}
