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

package de.dante.extex.interpreter.primitives.register;

import junit.framework.TestCase;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.bool.Bool;
import de.dante.extex.scanner.stream.impl32.TokenStreamStringImpl;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * A test for the bool-regsiter.
 *
 * <p>
 * use extex-extension
 * </p>
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class BoolRegisterTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 ...
     */
    public BoolRegisterTest(final String arg0) {

        super(arg0);
    }

    /**
     * ...
     * @param args ...
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(BoolRegisterTest.class);
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex32.xml");
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory(intcfg);
        Interpreter interpreter = intf.newInstance();

        interpreter.addStream(new TokenStreamStringImpl("true"));
        Bool b = new Bool(null, interpreter);
        assertTrue(b.getValue());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp2() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex32.xml");
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory(intcfg);
        Interpreter interpreter = intf.newInstance();

        interpreter.addStream(new TokenStreamStringImpl("false"));
        Bool b = new Bool(null, interpreter);
        assertFalse(b.getValue());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp3() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex32.xml");
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory(intcfg);
        Interpreter interpreter = intf.newInstance();

        interpreter.addStream(new TokenStreamStringImpl("on"));
        Bool b = new Bool(null, interpreter);
        assertTrue(b.getValue());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp4() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex32.xml");
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory(intcfg);
        Interpreter interpreter = intf.newInstance();

        interpreter.addStream(new TokenStreamStringImpl("off"));
        Bool b = new Bool(null, interpreter);
        assertFalse(b.getValue());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp5() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex32.xml");
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory(intcfg);
        Interpreter interpreter = intf.newInstance();

        Context context = interpreter.getContext();

        interpreter.addStream(new TokenStreamStringImpl("1      %\r"));
        Bool b = new Bool(context, interpreter);
        assertTrue(b.getValue());
    }

    /**
     * ...
     * @throws Exception ...
     */
    public void testSp6() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex32.xml");
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory(intcfg);
        Interpreter interpreter = intf.newInstance();

        Context context = interpreter.getContext();

        interpreter.addStream(new TokenStreamStringImpl("0    %\r"));
        Bool b = new Bool(context, interpreter);
        assertFalse(b.getValue());
    }

}