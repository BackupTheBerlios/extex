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
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.scanner.stream.impl32.TokenStreamStringImpl;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * A test for the real-regsiter.
 *
 * <p>
 * use extex-extension
 * </p>
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class RealRegisterTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 ...
     */
    public RealRegisterTest(final String arg0) {

        super(arg0);
    }

    /**
     * ...
     * @param args ...
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(RealRegisterTest.class);
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
        Interpreter source = intf.newInstance();

        source.addStream(new TokenStreamStringImpl("1234"));
        Real r = new Real(null, source);
        assertEquals("1234.0", r.toString());
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
        Interpreter source = intf.newInstance();

        source.addStream(new TokenStreamStringImpl("1234.567"));
        Real r = new Real(null, source);
        assertEquals("1234.567", r.toString());
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
        Interpreter source = intf.newInstance();

        source.addStream(new TokenStreamStringImpl("1234,567"));
        Real r = new Real(null, source);
        assertEquals("1234.567", r.toString());
    }

    //    /**
    //     * ...
    //     * @throws Exception ...
    //     */
    //    public void testSp4() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        Configuration typesettercfg = config.getConfiguration("Typesetter");
    //
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //        ContextExtension context = (ContextExtension) interpreter.getContext();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("\\real7=1234,567"));
    //
    //        Typesetter typesetter = new TypesetterFactory(typesettercfg)
    //          .newInstance((Context)context);
    //
    //        interpreter.setTypesetter(typesetter);
    //        interpreter.run();
    //
    //        Real r = context.getReal("real#7");
    //        assertEquals("1234.567", r.toString());
    //    }

}