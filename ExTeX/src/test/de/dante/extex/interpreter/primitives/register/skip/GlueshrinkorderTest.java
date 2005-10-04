/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.skip;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the primitive <tt>\glueshrinkorder</tt>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class GlueshrinkorderTest extends ExTeXLauncher {

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(GlueshrinkorderTest.class);
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public GlueshrinkorderTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> can not be used to assign
     *  something to it.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testErr1() throws Exception {

        runFailureCode(//--- input code ---
                "\\glueshrinkorder\\skip0=1 ",
                //--- error channel ---
                "You can't use `\\glueshrinkorder\' in vertical mode");
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fixed stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is applicable to
     *  <tt>\the</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2pt minus 3pt" + "\\the\\glueshrinkorder\\skip0 "
                        + "\\end",
                //--- output channel ---
                "0" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fixed stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  dimen register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2pt minus 3pt"
                        + "\\dimen0=\\glueshrinkorder\\skip0 " + "\\the\\dimen0"
                        + "\\end",
                //--- output channel ---
                "0.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fixed stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  count register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2pt minus 3pt"
                        + "\\count0=\\glueshrinkorder\\skip0 " + "\\the\\count0"
                        + "\\end",
                //--- output channel ---
                "0" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fil stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is applicable to
     *  <tt>\the</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test11() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2fil minus 3fil" + "\\the\\glueshrinkorder\\skip0 "
                        + "\\end",
                //--- output channel ---
                "1" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fil stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  dimen register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test12() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2fil minus 3fil"
                        + "\\dimen0=\\glueshrinkorder\\skip0 " + "\\the\\dimen0"
                        + "\\end",
                //--- output channel ---
                "0.00002pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fil stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  count register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test13() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2fil minus 3fil"
                        + "\\count0=\\glueshrinkorder\\skip0 " + "\\the\\count0"
                        + "\\end",
                //--- output channel ---
                "1" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fill stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is applicable to
     *  <tt>\the</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test21() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2fill minus 3fill" + "\\the\\glueshrinkorder\\skip0 "
                        + "\\end",
                //--- output channel ---
                "2" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fill stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  dimen register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test22() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2fill minus 3fill"
                        + "\\dimen0=\\glueshrinkorder\\skip0 " + "\\the\\dimen0"
                        + "\\end",
                //--- output channel ---
                "0.00003pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a fill stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  count register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test23() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2fill minus 3fill"
                        + "\\count0=\\glueshrinkorder\\skip0 " + "\\the\\count0"
                        + "\\end",
                //--- output channel ---
                "2" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a filll stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is applicable to
     *  <tt>\the</tt>.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test31() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2filll minus 3filll" + "\\the\\glueshrinkorder\\skip0 "
                        + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a filll stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  dimen register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test32() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2filll minus 3filll"
                        + "\\dimen0=\\glueshrinkorder\\skip0 " + "\\the\\dimen0"
                        + "\\end",
                //--- output channel ---
                "0.00005pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that <tt>\glueshrinkorder</tt> extracts the correct
     *  value from a filll stretch.
     *  In addition it shows that <tt>\glueshrinkorder</tt> is assignable to a
     *  count register.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test33() throws Exception {

        runCode(//--- input code ---
                "\\skip0=1pt plus 2filll minus 3filll"
                        + "\\count0=\\glueshrinkorder\\skip0 " + "\\the\\count0"
                        + "\\end",
                //--- output channel ---
                "3" + TERM);
    }

}
