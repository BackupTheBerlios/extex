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
 *
 */

package de.dante.util.file.random;

import java.io.DataInput;
import java.io.IOException;

/**
 * Interface for random access (read only)
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public interface RandomAccessR extends DataInput {

    /**
     * @see java.io.RandomAccessFile#close()
     */
    void close() throws IOException;

    /**
     * @see java.io.RandomAccessFile#length()
     */
    long length() throws IOException;

    /**
     * @see java.io.RandomAccessFile#seek(long)
     */
    void seek(long arg0) throws IOException;

    /**
     * Returns the pointer in the buffer.
     *
     * @throws IOException if an IO-error occurs
     * @return Returns the pointer in the buffer
     */
    long getPointer() throws IOException;

    /**
     * Reads a byte of data from this file. The byte is returned as an
     * integer in the range 0 to 255 (<code>0x00-0x0ff</code>).
     * @throws IOException if an IO-error occurs.
     * @return     the next byte of data.
     */
    int readByteAsInt() throws IOException;

    /**
     * Reads a int with 24 bit (3x8).
     * @return  Returns a int value.
     * @throws IOException if an IO-error occurs.
     */
    int readInt24() throws IOException;

    /**
     * get bit 24
     */
    int X24 = 0x800000;

    /**
     * get low 23 bit
     */
    int L24 = 0x7f0000;

    /**
     * Reads a sign int with 24 bit (3x8).
     * @return  Returns a int value.
     * @throws IOException if an IO-error occurs.
     */
    int readSignInt24() throws IOException;

    /**
     * Reads a int with 16 bit (2x8).
     * @return  Returns a int value.
     * @throws IOException if an IO-error occurs.
     */
    int readInt16() throws IOException;

    /**
     * Reads a int with 8 bit (1x8).
     * @return  Returns a int value.
     * @throws IOException if an IO-error occurs.
     */
    int readInt8() throws IOException;

    /**
     * Check, if EOF is reaged.
     * @return Returns <code>true</code>, if EOF is reaged.
     * @throws IOException if an IO-erorr occurs.
     */
    boolean isEOF() throws IOException;
}