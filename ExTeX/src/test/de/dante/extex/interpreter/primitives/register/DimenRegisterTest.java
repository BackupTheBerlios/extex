/*
 * Copyright (C) 2004 Gerd Neugebauer
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

package de.dante.extex.interpreter.primitives.register;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.scanner.stream.impl32.TokenStreamStringImpl;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * Test cases for dimen registers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
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

    //TODO change InterpreterFactory

    /**
     * Test that the parsing of scaled points works.
     * A value in the middle (1234sp) is used.
     *
     * @throws Exception in case of an error
     */
    public void testSp1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1234 sp"));
        assertEquals("1234sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.000pt) is used. This results into 65536sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.000pt"));
        assertEquals("65536sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.5pt) is used. This results into 98304sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.5pt"));
        assertEquals("98304sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.50pt) is used. This results into 98304sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt3() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.50pt"));
        assertEquals("98304sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.33pt) is used. This results into 87163sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt4() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.33pt"));
        assertEquals("87163sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of points works.
     * A value in the middle (1.333pt) is used. This results into 87359sp.
     *
     * @throws Exception in case of an error
     */
    public void testPt5() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.333pt"));
        assertEquals("87359sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of millimeter works.
     * A value in the middle (1mm) is used. This results into 186467sp.
     *
     * @throws Exception in case of an error
     */
    public void testMm1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1mm"));
        assertEquals("186467sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of millimeter works.
     * A value in the middle (1.33mm) is used. This results into 248002sp.
     *
     * @throws Exception in case of an error
     */
    public void testMm2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.33mm"));
        assertEquals("248002sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of centimeter works.
     * A value in the middle (1cm) is used. This results into 1864679sp.
     *
     * @throws Exception in case of an error
     */
    public void testCm1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1cm"));
        assertEquals("1864679sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of millimeter works.
     * A value in the middle (1.33mm) is used. This results into 2480027sp.
     *
     * @throws Exception in case of an error
     */
    public void testCm2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.33cm"));
        assertEquals("2480027sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of inch works.
     * A value in the middle (1in) is used. This results into 4736286sp.
     *
     * @throws Exception in case of an error
     */
    public void testIn1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1in"));
        assertEquals("4736286sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of didot points works.
     * A value in the middle (1dd) is used. This results into 70124sp.
     *
     * @throws Exception in case of an error
     */
    public void testDd1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1dd"));
        assertEquals("70124sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of didot points works.
     * A value in the middle (1.25dd) is used. This results into 87655sp.
     *
     * @throws Exception in case of an error
     */
    public void testDd2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.25dd"));
        assertEquals("87655sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of picas works.
     * A value in the middle (1.25pc) is used. This results into 983040sp.
     *
     * @throws Exception in case of an error
     */
    public void testPc1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.25pc"));
        assertEquals("983040sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of picas works.
     * A value in the middle (1.7pc) is used. This results into 1336932sp.
     *
     * @throws Exception in case of an error
     */
    public void testPc2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.7pc"));
        assertEquals("1336932sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of big points works.
     * A value in the middle (1.25bp) is used. This results into 82227sp.
     *
     * @throws Exception in case of an error
     */
    public void testBp1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("1.25bp"));
        assertEquals("82227sp", new Dimen(null, source).toString());
    }

    /**
     * Test that the parsing of ciceros works.
     * A value in the middle (7,777cc) is used. This results into 6544254sp.
     *
     * @throws Exception in case of an error
     */
    public void testCc1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        InterpreterFactory interpreterFactory = new InterpreterFactory();
        interpreterFactory.configure(config.getConfiguration("Interpreter"));
        Interpreter source = interpreterFactory.newInstance();
        source.addStream(new TokenStreamStringImpl("7,777cc"));
        assertEquals("6544254sp", new Dimen(null, source).toString());
    }

}