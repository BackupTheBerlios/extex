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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccess for a File (Input)
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class RandomAccessInputFile implements RandomAccessR {

    /**
     * Shift 8
     */
    private static final int SHIFT8 = 8;

    /**
     * Shift 16
     */
    private static final int SHIFT16 = 16;

    /**
     * RandomAccessFile
     */
    private RandomAccessFile raf;

    /**
     * Create a new object
     * @param file  file for reading
     * @throws IOException if an IO-error occured
     */
    public RandomAccessInputFile(final File file) throws IOException {

        super();

        raf = new RandomAccessFile(file, "r");
    }

    /**
     * Create a new object
     * @param filename  filename for reading
     * @throws IOException if an IO-error occured
     */
    public RandomAccessInputFile(final String filename) throws IOException {

        this(new File(filename));
    }

    /**
     * Reads a byte of data from this file. The byte is returned as an
     * integer in the range 0 to 255 (<code>0x00-0x0ff</code>).
     * @throws IOException if an IO-error occurs.
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             file has been reached.
     */
    public int read() throws IOException {

        return raf.read();
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#close()
     */
    public void close() throws IOException {

        raf.close();
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#length()
     */
    public long length() throws IOException {

        return raf.length();
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#seek(long)
     */
    public void seek(final long arg0) throws IOException {

        raf.seek(arg0);
    }

    /**
     * @see java.io.DataInput#readBoolean()
     */
    public boolean readBoolean() throws IOException {

        return raf.readBoolean();
    }

    /**
     * @see java.io.DataInput#readByte()
     */
    public byte readByte() throws IOException {

        return raf.readByte();
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#readByteAsInt()
     */
    public int readByteAsInt() throws IOException {

        int ch = this.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
    }

    /**
     * Reads a Unicode character from the input.
     *
     * If the bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1,&nbsp;b2&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (char)((b1 &lt;&lt; 8) | b2)
     * </pre></blockquote>
     * <p>
     *
     * @see java.io.DataInput#readChar()
     */
    public char readChar() throws IOException {

        return raf.readChar();
    }

    /**
     * Reads a <code>double</code> from the input.
     *
     * @see java.io.DataInput#readDouble()
     */
    public double readDouble() throws IOException {

        return raf.readDouble();
    }

    /**
     * Reads a <code>float</code> from the input.
     *
     * @see java.io.DataInput#readFloat()
     */
    public float readFloat() throws IOException {

        return raf.readFloat();
    }

    /**
     * @see java.io.DataInput#readFully(byte[], int, int)
     */
    public void readFully(final byte[] b, final int off, final int len)
            throws IOException {

        raf.readFully(b, off, len);
    }

    /**
     * @see java.io.DataInput#readFully(byte[])
     */
    public void readFully(final byte[] b) throws IOException {

        raf.readFully(b);
    }

    /**
     * Reads a signed 32-bit integer from the input.
     *
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, <code>b3</code>, and <code>b4</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b1 &lt;&lt; 24) | (b2 &lt;&lt; 16) + (b3 &lt;&lt; 8) + b4
     * </pre></blockquote>
     * <p>
     * @see java.io.DataInput#readInt()
     */
    public int readInt() throws IOException {

        return raf.readInt();
    }

    /**
     * Reads a signed 24-bit integer from the input.
     *
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, and <code>b3</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b1 &lt;&lt; 16) + (b2 &lt;&lt; 8) + b3
     * </pre></blockquote>
     * <p>
     * @see java.io.DataInput#readInt()
     */
    public int readInt24() throws IOException {

        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        if ((ch1 | ch2 | ch3) < 0) {
            throw new EOFException();
        }
        return ((ch1 << SHIFT16) + (ch2 << SHIFT8) + (ch3 << 0));
    }

    /**
     * Reads the next line of text from the input.
     *
     * @see java.io.DataInput#readLine()
     */
    public String readLine() throws IOException {

        return raf.readLine();
    }

    /**
     * Reads a signed 64-bit integer from the input.
     *
     * If the bytes read, in order, are
     * <code>b1</code>, <code>b2</code>, <code>b3</code>,
     * <code>b4</code>, <code>b5</code>, <code>b6</code>,
     * <code>b7</code>, and <code>b8,</code> where:
     * <blockquote><pre>
     *     0 &lt;= b1, b2, b3, b4, b5, b6, b7, b8 &lt;=255,
     * </pre></blockquote>
     * <p>
     * then the result is equal to:
     * <p><blockquote><pre>
     *     ((long)b1 &lt;&lt; 56) + ((long)b2 &lt;&lt; 48)
     *     + ((long)b3 &lt;&lt; 40) + ((long)b4 &lt;&lt; 32)
     *     + ((long)b5 &lt;&lt; 24) + ((long)b6 &lt;&lt; 16)
     *     + ((long)b7 &lt;&lt; 8) + b8
     * </pre></blockquote>
     * <p>
     * @see java.io.DataInput#readLong()
     */
    public long readLong() throws IOException {

        return raf.readLong();
    }

    /**
     * Reads a signed 16-bit number from the input.
     *
     * If the two bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where each of the two values is
     * between <code>0</code> and <code>255</code>, inclusive, then the
     * result is equal to:
     * <blockquote><pre>
     *     (short)((b1 &lt;&lt; 8) | b2)
     * </pre></blockquote>
     * <p>
     *
     * @see java.io.DataInput#readShort()
     */
    public short readShort() throws IOException {

        return raf.readShort();
    }

    /**
     * Reads an unsigned eight-bit number from the input.
     *
     * @see java.io.DataInput#readUnsignedByte()
     */
    public int readUnsignedByte() throws IOException {

        return raf.readUnsignedByte();
    }

    /**
     * Reads an unsigned 16-bit number from the input.
     *
     * If the bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b1 &lt;&lt; 8) | b2
     * </pre></blockquote>
     * <p>
     * @see java.io.DataInput#readUnsignedShort()
     */
    public int readUnsignedShort() throws IOException {

        return raf.readUnsignedShort();
    }

    /**
     * Reads in a string from this file. The string has been encoded
     * using a modified UTF-8 format.
     *
     * @see java.io.DataInput#readUTF()
     */
    public String readUTF() throws IOException {

        return raf.readUTF();
    }

    /**
     * Attempts to skip over <code>n</code> bytes of input discarding the
     * skipped bytes.
     *
     * @see java.io.DataInput#skipBytes(int)
     */
    public int skipBytes(final int n) throws IOException {

        return raf.skipBytes(n);
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#getPointer()
     */
    public long getPointer() throws IOException {

        return raf.getFilePointer();
    }
}