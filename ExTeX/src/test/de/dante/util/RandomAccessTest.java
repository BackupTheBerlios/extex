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

package de.dante.util;

import java.io.FileInputStream;
import java.io.RandomAccessFile;

import junit.framework.TestCase;
import de.dante.util.file.random.RandomAccessInputStream;

/**
 * Test cases for RandomAccessInputStream
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class RandomAccessTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 the name
     */
    public RandomAccessTest(final String arg0) {

        super(arg0);
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(RandomAccessTest.class);
    }

    /**
     * Test the RandomAccess
     *
     * @throws Exception in case of an error
     */
    public void test0() throws Exception {

        RandomAccessInputStream rar = new RandomAccessInputStream(
                new FileInputStream("src/font/Gara.ttf"));
        RandomAccessFile raf = new RandomAccessFile("src/font/Gara.ttf", "r");

        assertEquals(rar.length(), raf.length());

        for (int i = 0; i < raf.length(); i++) {
            assertEquals(rar.read(), raf.read());
        }

    }

    /**
     * Test the RandomAccess
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        RandomAccessInputStream rar = new RandomAccessInputStream(
                new FileInputStream("src/font/Gara.ttf"));
        RandomAccessFile raf = new RandomAccessFile("src/font/Gara.ttf", "r");

        assertEquals(rar.length(), raf.length());

        for (int i = 0; i < raf.length(); i++) {
            assertEquals(rar.readByte(), raf.readByte());
        }

    }

    /**
     * Test the RandomAccess
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        RandomAccessInputStream rar = new RandomAccessInputStream(
                new FileInputStream("src/font/Gara.ttf"));
        RandomAccessFile raf = new RandomAccessFile("src/font/Gara.ttf", "r");

        assertEquals(rar.length(), raf.length());

        assertEquals(rar.readByte(), raf.readByte());

        rar.skipBytes(1000);
        raf.skipBytes(1000);

        assertEquals(rar.readByte(), raf.readByte());
        assertEquals(new Double(rar.readDouble()), new Double(raf.readDouble()));
        assertEquals(new Long(rar.readLong()), new Long(raf.readLong()));

        rar.skipBytes(1000);
        raf.skipBytes(1000);

        for (int i = 0; i < 5000; i++) {
            assertEquals(rar.readByte(), raf.readByte());
        }

    }

    /**
     * Test the RandomAccess
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        RandomAccessInputStream rar = new RandomAccessInputStream(
                new FileInputStream("src/font/Gara.ttf"));
        RandomAccessFile raf = new RandomAccessFile("src/font/Gara.ttf", "r");

        assertEquals(rar.length(), raf.length());

        rar.seek(1000);
        raf.seek(1000);

        for (int i = 0; i < 1000; i++) {
            assertEquals(rar.readByte(), raf.readByte());
        }

    }

    /**
     * Test the RandomAccess
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        RandomAccessInputStream rar = new RandomAccessInputStream(
                new FileInputStream("src/font/Gara.ttf"));
        RandomAccessFile raf = new RandomAccessFile("src/font/Gara.ttf", "r");

        assertEquals(rar.length(), raf.length());

        rar.seek(1000);
        raf.seek(1000);

        byte[] b1 = new byte[1000];
        byte[] b2 = new byte[1000];

        rar.readFully(b1);
        raf.readFully(b2);
        for (int i = 0; i < b1.length; i++) {
            assertEquals(b1[i], b2[i]);
        }
    }

    /**
     * Test the RandomAccess
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        RandomAccessInputStream rar = new RandomAccessInputStream(
                new FileInputStream("src/font/Gara.ttf"));
        RandomAccessFile raf = new RandomAccessFile("src/font/Gara.ttf", "r");

        //System.out.println("length " + rar.length() + " : " + raf.length());
        assertEquals(rar.length(), raf.length());

        for (int i = 0; i < 1000; i++) {
            int i1 = rar.readInt();
            int i2 = raf.readInt();
            //  System.out.println(i1 + "  : " + i2);
            assertEquals(i1, i2);
        }
    }

}