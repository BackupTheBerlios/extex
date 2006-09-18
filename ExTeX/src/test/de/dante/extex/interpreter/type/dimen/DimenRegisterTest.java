/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import junit.framework.TestCase;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.context.MockContext;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Test cases for dimen registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class DimenRegisterTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 the name
     */
    public DimenRegisterTest(final String arg0) {

        super(arg0);
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DimenRegisterTest.class);
    }

    /**
     * Performs test on a given String.
     *
     * @param spec the String to parse
     *
     * @return the sp of the Dimen returned
     *
     * @throws ConfigurationException in case of an error in the configuration
     * @throws GeneralException in case of an error during parsing
     */
    private long doTest(final String spec)
            throws ConfigurationException,
                GeneralException {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        TokenStreamFactory fac = new TokenStreamFactory(config
                .getConfiguration("Scanner"), "base");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter"), null).newInstance(null, null);
        source.addStream(fac.newInstance(spec));
        source.setTokenStreamFactory(fac);

        return Dimen.parse(new MockContext(), source, null).getValue();
    }

    /**
     * Test that the parsing of scaled points works.
     * A value in the middle (1234sp) is used.
     *
     * @throws Exception in case of an error
     */
    public void testSp1() throws Exception {

        assertEquals(1234, doTest("1234 sp"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.000pt) is used. This results into 65536sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt1() throws Exception {

        assertEquals(65536, doTest("1.000pt"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.000pt) is used. This results into 65536sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt1b() throws Exception {

        assertEquals(65536, doTest("1 pt"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.000pt) is used. This results into 65536sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt1c() throws Exception {

        assertEquals(65536, doTest("1. pt"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.5pt) is used. This results into 98304sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt2() throws Exception {

        assertEquals(98304, doTest("1.5pt"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.50pt) is used. This results into 98304sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt3() throws Exception {

        assertEquals(98304, doTest("1.50pt"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.33pt) is used. This results into 87163sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt4() throws Exception {

        assertEquals(87163, doTest("1.33pt"));
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.333pt) is used. This results into 87359sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt5() throws Exception {

        assertEquals(87359, doTest("1.333pt"));
    }

    /**
     * Test that the parsing of millimeter works.
     * A value in the middle (1mm) is used. This results into 186467sp.
     *
     * @throws Exception in case of an error
     */
    public void testMm1() throws Exception {

        assertEquals(186467, doTest("1mm"));
    }

    /**
     * Test that the parsing of millimeter works.
     * A value in the middle (1.33mm) is used. This results into 248002sp.
     *
     * @throws Exception in case of an error
     */
    public void testMm2() throws Exception {

        assertEquals(248002, doTest("1.33mm"));
    }

    /**
     * Test that the parsing of centimeter works.
     * A value in the middle (1cm) is used. This results into 1864679sp.
     *
     * @throws Exception in case of an error
     */
    public void testCm1() throws Exception {

        assertEquals(1864679, doTest("1cm"));
    }

    /**
     * Test that the parsing of millimeter works.
     * A value in the middle (1.33mm) is used. This results into 2480027sp.
     *
     * @throws Exception in case of an error
     */
    public void testCm2() throws Exception {

        assertEquals(2480027, doTest("1.33cm"));
    }

    /**
     * Test that the parsing of inch works.
     * A value in the middle (1in) is used. This results into 4736286sp.
     *
     * @throws Exception in case of an error
     */
    public void testIn1() throws Exception {

        assertEquals(4736286, doTest("1in"));
    }

    /**
     * Test that the parsing of didot points works.
     * A value in the middle (1dd) is used. This results into 70124sp.
     *
     * @throws Exception in case of an error
     */
    public void testDd1() throws Exception {

        assertEquals(70124, doTest("1dd"));
    }

    /**
     * Test that the parsing of didot points works.
     * A value in the middle (1.25dd) is used. This results into 87655sp.
     *
     * @throws Exception in case of an error
     */
    public void testDd2() throws Exception {

        assertEquals(87655, doTest("1.25dd"));
    }

    /**
     * Test that the parsing of picas works.
     * A value in the middle (1.25pc) is used. This results into 983040sp.
     *
     * @throws Exception in case of an error
     */
    public void testPc1() throws Exception {

        assertEquals(983040, doTest("1.25pc"));
    }

    /**
     * Test that the parsing of picas works.
     * A value in the middle (1.7pc) is used. This results into 1336932sp.
     *
     * @throws Exception in case of an error
     */
    public void testPc2() throws Exception {

        assertEquals(1336932, doTest("1.7pc"));
    }

    /**
     * Test that the parsing of big points works.
     * A value in the middle (1.25bp) is used. This results into 82227sp.
     *
     * @throws Exception in case of an error
     */
    public void testBp1() throws Exception {

        assertEquals(82227, doTest("1.25bp"));
    }

    /**
     * Test that the parsing of ciceros works.
     * A value in the middle (7,777cc) is used. This results into 6544254sp.
     *
     * @throws Exception in case of an error
     */
    public void testCc1() throws Exception {

        assertEquals(6544254, doTest("7,777cc"));
    }

}
