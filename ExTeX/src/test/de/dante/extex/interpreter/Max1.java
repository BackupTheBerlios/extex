/*
 * Copyright (C) 2003 Gerd Neugebauer
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
package de.dante.extex.interpreter;

import junit.framework.TestCase;
import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.logging.Logger;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.Typesetter;
import de.dante.test.logging.NullLogger;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class Max1 extends TestCase {

    private Logger noLogger = new NullLogger();

    /**
     * Constructor for Max1.
     * 
     * @param arg0
     */
    public Max1(String arg0) {
        super(arg0);
    }

    /**
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(Max1.class);
    }

    /**
     * Trivial case: nothing in and nothing out
     */
    public void testEmpty() throws Exception {
        assertEquals("", doTest(""));
    }

    /**
     */
    public void testSingle1() throws Exception {
        assertEquals("a", doTest("a"));
    }

    /**
     */
    public void testSingle2() throws Exception {
        assertEquals("A", doTest("A"));
    }

    /**
     */
    public void testSingle3() throws Exception {
        assertEquals("2", doTest("2"));
    }

    /**
     */
    public void testSingle4() throws Exception {
        assertEquals(".", doTest("."));
    }

    /**
     */
    public void testMacro1() throws Exception {
        assertEquals("", doTest("\\relax"));
    }

    /**
     */
    public void testMacro2() throws Exception {
        assertEquals("\n\\par\n", doTest("\\par"));
    }

    /**
     */
    private String doTest(String in) throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        // TODO FileFinder 
        Interpreter interpreter = new InterpreterFactory(config
                .getConfiguration("Interpreter")).newInstance();
        TokenStreamFactory factory = new TokenStreamFactory(config
                .getConfiguration("Reader"));
        interpreter.setTokenStreamFactory(factory);

        TestTypesetter typesetter = new TestTypesetter();

        interpreter.setTypesetter(typesetter);

        TokenStream stream = new TokenStreamBufferImpl(in,"ISO-8859-1");
        interpreter.run(stream);
        return typesetter.toString();
    }

    /**
     * Inner class to collect the things the typesetter sees.
     */
    private static class TestTypesetter implements Typesetter {

        /**
         */
        private StringBuffer sb = new StringBuffer();

        /**
         */
        public String toString() {
            return sb.toString();
        }

        /**
         */
        public void addGlue(Glue g) {
            sb.append(g.toString());
        }

        /**
         */
        public void addGlyph(String glyph) {
            sb.append(glyph);
        }

        /**
         */
        public void addSpace(TypesettingContext typesettingContext,
                Count spacefactor) {
            sb.append(" ");
        }

        /**
         */
        public CharNodeFactory getCharNodeFactory() {
            return null;
        }

        /**
         */
        public void par() {
            sb.append("\n\\par\n");
        }

        /**
         */
        public void setDocumentWriter(DocumentWriter doc) {
            // nothing to do
        }

        /**
         * @see de.dante.util.configuration.Configurable#configure(de.dante.util.configuration.Configuration)
         */
        public void configure(Configuration config) {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#add(de.dante.extex.interpreter.type.node.CharNode)
         */
        public void add(Node c) {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#close()
         */
        public NodeList close() {
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#getMode()
         */
        public Mode getMode() {
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#open()
         */
        public void open() {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#add(de.dante.extex.interpreter.type.Font,
         *      java.lang.String)
         */
        public void add(TypesettingContext font, UnicodeChar symbol) {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#finish()
         */
        public void finish(Context context) {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
         */
        public void toggleDisplaymath() {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.ListMaker#toggleMath()
         */
        public void toggleMath() {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(int)
         */
        public void setSpacefactor(Count f) {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(de.dante.extex.interpreter.type.Dimen)
         */
        public void setPrevDepth(Dimen pd) {
            // nothing to do
        }

        /**
         * @see de.dante.extex.typesetter.Typesetter#shipout(de.dante.extex.typesetter.NodeList)
         */
        public void shipout(Box nodes) {
            // nothing to do
        }
    }

}
