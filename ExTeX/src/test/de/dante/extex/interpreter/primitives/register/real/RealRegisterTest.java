/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.real;

import junit.framework.TestCase;

/**
 * A test for the real-regsiter.
 *
 * <p>
 * use extex-extension
 * </p>
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
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
    //    public void testSp1() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("1234   nix"));
    //        Real r = new Real(null, interpreter);
    //        assertEquals("1234.0", r.toString());
    //    }
    /**
     * ...
     * @throws Exception ...
     */
    //    public void testSp2() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("1234.567     nix"));
    //        Real r = new Real(null, interpreter);
    //        assertEquals("1234.567", r.toString());
    //    }
    /**
     * ...
     * @throws Exception ...
     */
    //    public void testSp3() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("1234,567    nix"));
    //        Real r = new Real(null, interpreter);
    //        assertEquals("1234.567", r.toString());
    //    }
    //    /**
    //     * ...
    //     *
    //     * catcode { }  fehlt !!!!
    //     * @throws Exception ...
    //     */
    //    public void testSp4() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //
    //        Context context = interpreter.getContext();
    //
    //        //        interpreter.addStream(new TokenStreamStringImpl("\\catcode`\\{=1"));
    //        //        interpreter.addStream(new TokenStreamStringImpl("\\catcode`\\}=2"));
    //        //
    //        //        interpreter.run();
    //
    //        interpreter
    //                .addStream(new TokenStreamStringImpl("\\mathexpr{2+5}  nix"));
    //        Real r = new Real(context, interpreter);
    //        assertEquals("7.0", r.toString());
    //    }
    /**
     * pi
     * @throws Exception ...
     */
    //    public void testPi() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //        Context context = interpreter.getContext();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("\\mathpi   nix"));
    //        Real r = new Real(context, interpreter);
    //        assertEquals(String.valueOf(Math.PI), r.toString());
    //    }
    /**
     * sin
     * @throws Exception ...
     */
    //    public void testSin1() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //        Context context = interpreter.getContext();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("\\mathsin\\mathpi   nix"));
    //        Real r = new Real(context, interpreter);
    //        assertEquals(String.valueOf(Math.sin(Math.PI)), r.toString());
    //    }
    /**
     * sin
     * @throws Exception ...
     */
    //    public void testSin2() throws Exception {
    //
    //        Configuration config = new ConfigurationFactory()
    //                .newInstance("config/extex32.xml");
    //        Configuration intcfg = config.getConfiguration("Interpreter");
    //        InterpreterFactory intf = new InterpreterFactory(intcfg);
    //        Interpreter interpreter = intf.newInstance();
    //        Context context = interpreter.getContext();
    //
    //        interpreter.addStream(new TokenStreamStringImpl("\\mathsin 1.0   nix"));
    //        Real r = new Real(context, interpreter);
    //        assertEquals("0.8414709848078965", r.toString());
    //    }
}