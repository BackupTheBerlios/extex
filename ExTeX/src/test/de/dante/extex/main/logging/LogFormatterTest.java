/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import junit.framework.TestCase;

/**
 * This class contains test cases for the log formatter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class LogFormatterTest extends TestCase {

    /**
     * Command line interface.
     *
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(LogFormatterTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param name the name
     */
    public LogFormatterTest(final String name) {

        super(name);
    }

    /**
     * <testcase class="LogFormatter">
     *   Test case checking that a simple string comes through.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        StringBuffer res = new StringBuffer();
        LogFormatter formatter = new LogFormatter();
        res.append(formatter.format(new LogRecord(Level.ALL, "abc")));
        assertEquals("abc", res.toString());
    }

    /**
     * <testcase class="LogFormatter">
     *   Test case checking that a simple string comes through.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        StringBuffer res = new StringBuffer();
        LogFormatter formatter = new LogFormatter();
        res.append(formatter.format(new LogRecord(Level.ALL, "abc")));
        res.append(formatter.format(new LogRecord(Level.ALL, "def")));
        assertEquals("abcdef", res.toString());
    }

    /**
     * <testcase class="LogFormatter">
     *   Test case checking that a simple string comes through.
     * <testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        StringBuffer res = new StringBuffer();
        LogFormatter formatter = new LogFormatter();
        res.append(formatter.format(new LogRecord(Level.ALL,
                "abc def ghi jkl mno pqr stu vwx yz "
                        + "abc def ghi jkl mno pqr stu vwx yz "
                        + "abc def ghi jkl mno pqr stu vwx yz "
                        + "abc def ghi jkl mno pqr stu vwx yz ")));
        assertEquals("abc def ghi jkl mno pqr stu vwx yz "
                + "abc def ghi jkl mno pqr stu vwx yz "
                + "abc def\nghi jkl mno pqr stu vwx yz "
                + "abc def ghi jkl mno pqr stu vwx yz ", res.toString());
    }

}
