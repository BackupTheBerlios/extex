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
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.scanner.stream.impl.TokenStreamStringImpl;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 *
 * @author gene
 * @version $Revision: 1.7 $
 */
public class DimenRegisterTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 ...
     */
    public DimenRegisterTest(final String arg0) {
        super(arg0);
    }

    /**
     * ...
     * @param args ...
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(DimenRegisterTest.class);
    }

    //TODO change InterpreterFactory

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1234 sp"));
        assertEquals("1234sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testPt1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.000pt"));
        assertEquals("65536sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testPt2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.5pt"));
        assertEquals("98304sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testPt3() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.50pt"));
        assertEquals("98304sp", new Dimen(null, source).toString());
    }

/**
     * ...
     * @throws Exception ...
     */
    public void testPt4() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.33pt"));
        assertEquals("87163sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testPt5() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.333pt"));
        assertEquals("87359sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testMm1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1mm"));
        assertEquals("186467sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testMm2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.33mm"));
        assertEquals("248002sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testCm1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1cm"));
        assertEquals("1864679sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testCm2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.33cm"));
        assertEquals("2480027sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testIn1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1in"));
        assertEquals("4736286sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testDd1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1dd"));
        assertEquals("70124sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testDd2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.25dd"));
        assertEquals("87655sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testPc1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.25pc"));
        assertEquals("983040sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testPc2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.7pc"));
        assertEquals("1336932sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testBp1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("1.25bp"));
        assertEquals("82227sp", new Dimen(null, source).toString());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testCc1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Interpreter source = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        source.addStream(new TokenStreamStringImpl("7,777cc"));
        assertEquals("6544254sp", new Dimen(null, source).toString());
    }

}
