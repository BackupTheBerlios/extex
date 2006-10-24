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

package de.dante.extex.interpreter.primitives.file;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.loader.SerialLoader;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.TokenFactoryImpl;
import de.dante.test.NoFlagsPrimitiveTester;

/**
 * This is a test suite for the primitive <tt>\dump</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class DumpTest extends NoFlagsPrimitiveTester {

    /**
     * Method for running the tests standalone.
     *
     * @param args command line parameter
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DumpTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public DumpTest(final String arg) {

        super(arg, "dump", "", "", "Beginning to dump on file ."
                + System.getProperty("file.separator") + "texput.fmt\n");
        new File("texput.fmt").delete();
    }

    /**
     * <testcase primitive="\dump">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        assertOutput(//--- input code ---
                "\\dump \\end",
                //--- log message ---
                "Beginning to dump on file ."
                + System.getProperty("file.separator") + "texput.fmt\n", "");
    }

    /**
     * <testcase primitive="\dump">
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        assertOutput(//--- input code ---
                "\\font\\x= cmr10 \\count1=123 \\dump \\end",
                //--- log message ---
                "Beginning to dump on file ."
                + System.getProperty("file.separator") + "texput.fmt\n", "");

        File fmt = new File("texput.fmt");
        Context context = new SerialLoader().load(new FileInputStream(fmt));
        assertNotNull(context);
        Calendar calendar = Calendar.getInstance();
        assertEquals("texput " + //
                calendar.get(Calendar.YEAR) + "."
                + (calendar.get(Calendar.MONTH) + 1) + "."
                + calendar.get(Calendar.DAY_OF_MONTH), context.getId());
        assertEquals(0, context.getIfLevel());
        assertEquals(0, context.getGroupLevel());
        assertNull(context.getFontFactory());
        assertNull(context.getTokenFactory());
        assertNull(context.getConditional());
        assertNull(context.getAfterassignment());
        assertEquals(1000, context.getMagnification());
        assertEquals(123, context.getCount("1").getValue());
        Code code = context.getCode((CodeToken) new TokenFactoryImpl()
                .createToken(Catcode.ESCAPE, null, "x",
                        Namespace.DEFAULT_NAMESPACE));
        assertNotNull(code);
        fmt.delete();
    }

}
