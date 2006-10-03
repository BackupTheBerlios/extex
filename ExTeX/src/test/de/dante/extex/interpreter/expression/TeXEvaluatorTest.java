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

package de.dante.extex.interpreter.expression;

import junit.framework.TestCase;
import de.dante.extex.interpreter.context.impl.ContextImpl;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.expression.term.Accumulator;
import de.dante.extex.interpreter.max.StringSource;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.scanner.type.token.TokenFactoryImpl;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class is a test suite for the expression evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TeXEvaluatorTest extends TestCase {

    /**
     * Main program.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        
        junit.textui.TestRunner.run(TeXEvaluatorTest.class);
    }
    
    /**
     * TODO gene: missing JavaDoc
     *
     * @param s
     * @return
     *
     * @throws InterpreterException in case of an error
     */
    protected Accumulator apply(final String s)
            throws InterpreterException {

        Evaluator ev = getInstanceForTest();
        Accumulator accumulator = new Accumulator();
        ContextImpl context = new ContextImpl();
        context.setTokenFactory(new TokenFactoryImpl());
        try {
            context.configure(new ConfigurationFactory().newInstance(
                    TeXEvaluatorTest.class.getName().replace('.', '/'))
                    .getConfiguration("ExTeX"));
            StringSource source = new StringSource(s);
            source.setContext(context);
            ev.eval(accumulator, context, source, null);
        } catch (ConfigurationException e) {
            e.printStackTrace();
            assertFalse(true);
        }
        return accumulator;
    }

    Evaluator getInstanceForTest() {

        return new TeXEvaluator();
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        try {
            apply("");
            assertFalse(true);
        } catch (InterpreterException e) {
            assertTrue(e instanceof MissingNumberException);
        }
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount1() throws Exception {

        Accumulator a = apply("123");
        assertNotNull(a);
        assertNotNull(a.getValue());
        assertTrue(a.getValue() instanceof Count);
        assertEquals(123, ((Count)a.getValue()).getValue());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount2() throws Exception {

        Accumulator a = apply("123 ");
        assertNotNull(a);
        assertNotNull(a.getValue());
        assertTrue(a.getValue() instanceof Count);
        assertEquals(123, ((Count)a.getValue()).getValue());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount3() throws Exception {

        Accumulator a = apply("-123");
        assertNotNull(a);
        assertNotNull(a.getValue());
        assertTrue(a.getValue() instanceof Count);
        assertEquals(-123, ((Count)a.getValue()).getValue());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCount4() throws Exception {

        Accumulator a = apply("- 123");
        assertNotNull(a);
        assertNotNull(a.getValue());
        assertTrue(a.getValue() instanceof Count);
        assertEquals(-123, ((Count)a.getValue()).getValue());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testCountPlus1() throws Exception {

        Accumulator a = apply("(1+2)");
        assertNotNull(a);
        assertNotNull(a.getValue());
        assertTrue(a.getValue() instanceof Count);
        assertEquals(3, ((Count)a.getValue()).getValue());
    }

}
