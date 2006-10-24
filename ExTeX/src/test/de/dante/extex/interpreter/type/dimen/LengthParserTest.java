/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.dimen;

import de.dante.test.ExTeXLauncher;

/**
 * This is a test suite for the for the parser of data type Dimen.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class LengthParserTest extends ExTeXLauncher {

    /**
     * Constructor for DimenTest.
     *
     * @param arg the argument
     */
    public LengthParserTest(final String arg) {

        super(arg);
    }

    /**
     * <testcase>
     *  Test case showing that 0pt is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPt0() throws Exception {

        assertSuccess(//
                "\\dimen0=0pt\\the\\dimen0\\end",
                //
                "0.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a positive number with fraction is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPt1() throws Exception {

        assertSuccess(//
                "\\dimen0=123.4pt\\the\\dimen0\\end",
                //
                "123.4pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a negative number with fraction is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPt2() throws Exception {

        assertSuccess(//
                "\\dimen0=-123.4pt\\the\\dimen0\\end",
                //
                "-123.4pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a positive fraction number is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPt3() throws Exception {

        assertSuccess(//
                "\\dimen0=.45pt\\the\\dimen0\\end",
                //
                "0.45pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a negative fraction number is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testPt4() throws Exception {

        assertSuccess(//
                "\\dimen0=-.45pt\\the\\dimen0\\end",
                //
                "-0.45pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVar1() throws Exception {

        assertSuccess(//
                "\\dimen1=1.23pt"
                + "\\dimen0=\\dimen1 "
                + "\\the\\dimen0\\end",
                //
                "1.23pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a negated dimen variable is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVar2() throws Exception {

        assertSuccess(//
                "\\dimen1=1.23pt"
                + "\\dimen0=-\\dimen1 "
                + "\\the\\dimen0\\end",
                //
                "-1.23pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a double negated dimen variable is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testVar3() throws Exception {

        assertSuccess(//
                "\\dimen1=1.23pt"
                + "\\dimen0=--\\dimen1 "
                + "\\the\\dimen0\\end",
                //
                "1.23pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable... by a fraction
     *  is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testAdd1() throws Exception {

        assertSuccess(//
                "\\dimen1=2pt"
                + "\\dimen0=(\\dimen1+1.23pt)"
                + "\\the\\dimen0\\end",
                //
                "3.23pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable... by a fraction
     *  is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testSubtract1() throws Exception {

        assertSuccess(//
                "\\dimen1=2pt"
                + "\\dimen0=(\\dimen1-1.23pt)"
                + "\\the\\dimen0\\end",
                //
                "0.77pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a integer multiplied dimen variable is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMult1() throws Exception {

        assertSuccess(//
                "\\dimen1=1.23pt"
                + "\\dimen0=(2*\\dimen1)"
                + "\\the\\dimen0\\end",
                //
                "2.45999pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable multiplied by an integer
     *  is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMult2() throws Exception {

        assertSuccess(//
                "\\dimen1=1.23pt"
                + "\\dimen0=(\\dimen1 *2)"
                + "\\the\\dimen0\\end",
                //
                "2.45999pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a fraction multiplied dimen variable is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMult3() throws Exception {

        assertSuccess(//
                "\\dimen1=2pt"
                + "\\dimen0=(1.23*\\dimen1)"
                + "\\the\\dimen0\\end",
                //
                "2.45999pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable multiplied by a fraction
     *  is parsed.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMult4() throws Exception {

        assertSuccess(//
                "\\dimen1=2pt"
                + "\\dimen0=(\\dimen1*1.23)"
                + "\\the\\dimen0\\end",
                //
                "2.45999pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable divided by zero leads to an
     *  error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDivError1() throws Exception {

        assertFailure(//
                "\\dimen1=2pt"
                + "\\dimen0=(\\dimen1/0)",
                //
                "Arithmetic overflow");
    }

    /**
     * <testcase>
     *  Test case showing that a dimen variable divided by zero leads to an
     *  error.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDiv1() throws Exception {

        assertSuccess(//
                "\\dimen1=2pt"
                + "\\dimen0=(\\dimen1*\\dimen1/1pt)"
                + "\\the\\dimen0\\end",
                //
                "4.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testTerm1() throws Exception {

        assertSuccess(//
                "\\dimen1=1.2pt"
                + "\\dimen0=((1+2)*\\dimen1)"
                + "\\the\\dimen0\\end",
                //
                "3.59999pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUnitError1() throws Exception {

        assertFailure(//
                "\\dimen1=2pt"
                + "\\dimen0=(1)",
                //
                "Missing a valid unit for a length value");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUnitError2() throws Exception {

        assertFailure(//
                "\\dimen1=2pt"
                + "\\dimen0=(1/\\dimen1)",
                //
                "Illegal unit for a length value found: sp^-1");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUnitError3() throws Exception {

        assertFailure(//
                "\\dimen0=(1pt*1pt)",
                //
                "Illegal unit for a length value found: sp^2");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testUnitError4() throws Exception {

        assertFailure(//
                "\\dimen0=(1pt*1pt*1pt)",
                //
                "Illegal unit for a length value found: sp^3");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMax1() throws Exception {

        assertSuccess(//
                "\\dimen1=1.2pt"
                + "\\dimen0=max(\\dimen1)"
                + "\\the\\dimen0\\end",
                //
                "1.2pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMax2() throws Exception {

        assertSuccess(//
                "\\dimen0=max(1pt, 2pt, 3pt)"
                + "\\the\\dimen0\\end",
                //
                "3.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMax3() throws Exception {

        assertSuccess(//
                "\\dimen0=max(4pt, 2pt, 3pt)"
                + "\\the\\dimen0\\end",
                //
                "4.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMaxError1() throws Exception {

        assertFailure(//
                "\\dimen0=max()",
                //
                "Missing number, treated as zero");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMaxError2() throws Exception {

        assertFailure(//
                "\\dimen0=max(1.2, 12pt)",
                //
                "Incompatible unit for max found: sp^0 <> sp^1");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMin1() throws Exception {

        assertSuccess(//
                "\\dimen1=1.2pt"
                + "\\dimen0=min(\\dimen1)"
                + "\\the\\dimen0\\end",
                //
                "1.2pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMin2() throws Exception {

        assertSuccess(//
                "\\dimen0=min(1pt, 2pt, 3pt)"
                + "\\the\\dimen0\\end",
                //
                "1.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMin3() throws Exception {

        assertSuccess(//
                "\\dimen0=min(4pt, 2pt, 3pt)"
                + "\\the\\dimen0\\end",
                //
                "2.0pt" + TERM);
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMinError1() throws Exception {

        assertFailure(//
                "\\dimen0=min()",
                //
                "Missing number, treated as zero");
    }

    /**
     * <testcase>
     *  Test case showing that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testMinError2() throws Exception {

        assertFailure(//
                "\\dimen0=min(1.2, 12pt)",
                //
                "Incompatible unit for min found: sp^0 <> sp^1");
    }

}
