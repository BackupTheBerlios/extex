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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * RandomAccess for a InputStream
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class RandomAccessInputStream implements RandomAccessR {

    /**
     * Buffer
     */
    private short[] buffer;

    /**
     * pointer
     */
    private int pointer = 0;

    /**
     * blocksize
     */
    private static final int BLOCKSIZE = 0x8000;

    /**
     * Shift 8
     */
    private static final int SHIFT8 = 8;

    /**
     * Shift 16
     */
    private static final int SHIFT16 = 16;

    /**
     * Shift 24
     */
    private static final int SHIFT24 = 24;

    /**
     * Shift 32
     */
    private static final int SHIFT32 = 32;

    /**
     * Create a new object
     * @param iostream  stream for reading
     * @throws IOException if an IO-error occured
     */
    public RandomAccessInputStream(final InputStream iostream)
            throws IOException {

        super();

        buffer = readStream(iostream);
        pointer = 0;
    }

    /**
     * Read a stream and store all byte in a short-array
     *
     * @param iostream the stream to read
     * @return Return a short-array
     * @throws IOException in case of an error
     */
    private short[] readStream(final InputStream iostream) throws IOException {

        BufferedInputStream bufin = new BufferedInputStream(iostream, BLOCKSIZE);

        int count = 0;
        short[] buf = new short[BLOCKSIZE];

        int read;
        while ((read = bufin.read()) != -1) {

            int newcount = count + 1;
            if (newcount > buf.length) {
                short[] newbuf = new short[buf.length + BLOCKSIZE];
                System.arraycopy(buf, 0, newbuf, 0, count);
                buf = newbuf;
            }
            buf[count] = (short) read;
            count = newcount;
        }
        bufin.close();
        iostream.close();

        short[] newbuf = new short[count];
        System.arraycopy(buf, 0, newbuf, 0, count);
        return newbuf;
    }

    /**
     * Reads a byte of data from this file. The byte is returned as an
     * integer in the range 0 to 255 (<code>0x00-0x0ff</code>).
     * @return     the next byte of data, or <code>-1</code> if the end of the
     *             file has been reached.
     */
    public int read() {

        if (pointer < buffer.length) {
            return buffer[pointer++];
        }
        return -1;
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#close()
     */
    public void close() throws IOException {

        buffer = null;
        pointer = -1;
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#length()
     */
    public long length() throws IOException {

        if (buffer != null) {
            return buffer.length;
        }
        return -1;
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#seek(long)
     */
    public void seek(final long arg0) throws IOException {

        if (buffer != null) {
            if (arg0 < buffer.length && arg0 < Integer.MAX_VALUE) {
                pointer = (int) arg0;
                return;
            }
        }
        throw new EOFException();

    }

    /**
     * @see java.io.DataInput#readBoolean()
     */
    public boolean readBoolean() throws IOException {

        int ch = this.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (ch != 0);
    }

    /**
     * @see java.io.DataInput#readByte()
     */
    public byte readByte() throws IOException {

        int ch = this.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return (byte) (ch);
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

        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (char) ((ch1 << SHIFT8) + (ch2 << 0));
    }

    /**
     * Reads a <code>double</code> from the input.
     *
     * @see java.io.DataInput#readDouble()
     */
    public double readDouble() throws IOException {

        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads a <code>float</code> from the input.
     *
     * @see java.io.DataInput#readFloat()
     */
    public float readFloat() throws IOException {

        return Float.intBitsToFloat(readInt());
    }

    /**
     * @see java.io.DataInput#readFully(byte[], int, int)
     */
    public void readFully(final byte[] b, final int off, final int len)
            throws IOException {

        try {
            readBytes(b, off, len);
        } catch (Exception e) {
            throw new EOFException();
        }
    }

    /**
     * Reads a sub array as a sequence of bytes.
     * @param b the data to be written
     * @param off the start offset in the data
     * @param len the number of bytes that are written
     */
    private void readBytes(final byte[] b, final int off, final int len) {

        for (int i = 0; i < len; i++) {
            b[off + i] = (byte) buffer[pointer++];
            if (pointer >= buffer.length) {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    /**
     * @see java.io.DataInput#readFully(byte[])
     */
    public void readFully(final byte[] b) throws IOException {

        readFully(b, 0, b.length);
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

        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << SHIFT24) + (ch2 << SHIFT16) + (ch3 << SHIFT8) + (ch4 << 0));
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

        StringBuffer input = new StringBuffer();
        int c = -1;
        boolean eol = false;

        while (!eol) {
            switch (c = read()) {
                case -1 :
                case '\n' :
                    eol = true;
                    break;
                case '\r' :
                    eol = true;
                    long cur = pointer;
                    if ((read()) != '\n') {
                        seek(cur);
                    }
                    break;
                default :
                    input.append((char) c);
                    break;
            }
        }

        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        return input.toString();
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

        return ((long) (readInt()) << SHIFT32) + (readInt() & ANDLONG);
    }

    /**
     * and long
     */
    private static final long ANDLONG = 0xFFFFFFFFL;

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

        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch1 << SHIFT8) + (ch2 << 0));
    }

    /**
     * Reads an unsigned eight-bit number from the input.
     *
     * @see java.io.DataInput#readUnsignedByte()
     */
    public int readUnsignedByte() throws IOException {

        int ch = this.read();
        if (ch < 0) {
            throw new EOFException();
        }
        return ch;
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

        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << SHIFT8) + (ch2 << 0);
    }

    /**
     * Reads in a string from this file. The string has been encoded
     * using a modified UTF-8 format.
     *
     * @see java.io.DataInput#readUTF()
     */
    public String readUTF() throws IOException {

        return DataInputStream.readUTF(this);
    }

    /**
     * Attempts to skip over <code>n</code> bytes of input discarding the
     * skipped bytes.
     *
     * @see java.io.DataInput#skipBytes(int)
     */
    public int skipBytes(final int n) throws IOException {

        long pos;
        long len;
        long newpos;

        if (n <= 0) {
            return 0;
        }
        pos = pointer;
        len = length();
        newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        seek(newpos);

        return (int) (newpos - pos);
    }

    /**
     * @see de.dante.util.file.random.RandomAccessR#getPointer()
     */
    public long getPointer() {

        return pointer;
    }
}