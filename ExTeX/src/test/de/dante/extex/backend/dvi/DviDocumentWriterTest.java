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

package de.dante.extex.backend.dvi;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import junit.framework.TestCase;
import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.backend.documentWriter.dvi.DviDocumentWriter;
import de.dante.extex.backend.documentWriter.dvi.PanicException;
import de.dante.extex.backend.documentWriter.exception.NoOutputStreamException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.ExplicitKernNode;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.InsertionNode;
import de.dante.extex.typesetter.type.node.MarkNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.page.PageImpl;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;

/**
 * JUnit tests for class <code>DviDocumentWriter</code>.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.1 $
 */
public class DviDocumentWriterTest extends TestCase {

    private DocumentWriter documentWriter = null;

    private NodeList nodeList = null;

    private MockDocumentWriterOptions documentWriterOptions = null;

    private Configuration configuration = null;

    private OutputStream outputStream = null;

    /**
     * The if DviDocumentWriter throws the exception, if the node is
     * added to the NodeList.
     *
     * @param node a <code>Node</code> value
     * @param exception a <code>Class</code> value
     * @throws Exception if an error occurs
     */
    private void checkException(final Node node, final Class exception)
            throws Exception {

        boolean gotException = false;

        nodeList.add(node);

        try {
            FixedCount[] pageNo = null;
            documentWriter.shipout(new PageImpl(nodeList, pageNo));
        } catch (Exception e) {
            if (exception.isInstance(e)) {
                gotException = true;
            } else {
                throw e;
            }
        }
        assertTrue(gotException);
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(DviDocumentWriterTest.class);
    }

    /**
     * Creates a new <code>DviDocumentWriterTest</code> instance.
     */
    public DviDocumentWriterTest() {

        super();
    }

    /**
     * Setup the fixtures.  All class variables get threr value here.
     * This is not done in the constructor so the variables get new
     * values for each test.
     *
     * @throws Exception if an error occurs
     */
    public void setUp() throws Exception {

        // TODO: do not use null for configuration (TE)

        documentWriterOptions = new MockDocumentWriterOptions();
        documentWriter = new DviDocumentWriter(configuration,
                documentWriterOptions);
        nodeList = new VerticalListNode();
        outputStream = new ByteArrayOutputStream();
        ((SingleDocumentStream) documentWriter).setOutputStream(outputStream);
    }

    /**
     * Test if {@link
     *   de.dante.extex.backend.documentWriter.dvi.DviDocumentWriter
     *   DviDocumentWriter }
     * throws a {@link
     *   de.dante.extex.backend.documentWriter.NoOutputStreamException
     * NoOutputStreamException},
     * if there is no OutputStream set before {@link
     *   de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *   de.dante.extex.typesetter.type.NodeList) shipout()}.
     *
     * @throws Exception if an error occurs
     */
    public void testNoOutputStream() throws Exception {

        boolean noOutputStream = false;

        documentWriter = new DviDocumentWriter(configuration,
                documentWriterOptions);
        try {
            FixedCount[] pageNo = null;
            documentWriter.shipout(new PageImpl(nodeList, pageNo));
        } catch (GeneralException e) {
            if (e instanceof NoOutputStreamException) {
                noOutputStream = true;
            }
        }

        assertTrue(noOutputStream);
    }

    /**
     * Test if the DviDocumentWriter throws new Exception if the
     * node list is empty.
     *
     * @throws Exception if an error occurs
     */
    public void testEmptyList() throws Exception {

        FixedCount[] pageNo = null;
        documentWriter.shipout(new PageImpl(nodeList, pageNo));
    }

    /**
     * Test if a mark node throws a panic Exception.
     *
     * @throws Exception if an error occurs
     */
    public void testMarkNode() throws Exception {

        checkException(new MarkNode(Tokens.EMPTY, "0"), PanicException.class);
    }

    /**
     * Test if a insertion node throws a panic Exception.
     *
     * @throws Exception if an error occurs
     */
    public void testInsertionNode() throws Exception {

        checkException(new InsertionNode(42, null), PanicException.class);
    }

    /**
     * Test valid nodes.
     *
     * @throws Exception if an error occurs
     */
    public void testValidNodes() throws Exception {

        // TODO: nodeList.add(new CharNode()); (TE)
        nodeList.add(new ExplicitKernNode(new Dimen(12346), true));
        nodeList.add(new GlueNode(new Glue(1234), true));
        // TODO: nodeList.add(new LigatureNode()); (TE)
        // TODO: nodeList.add(new SpecialNode("Test")); (TE)
        //nodeList.add(new WhatsItNode("Test"));

        FixedCount[] pageNo = null;
        documentWriter.shipout(new PageImpl(nodeList, pageNo));
    }

    /**
     * Check the specified magnification.
     *
     * @param magnification for check
     *
     * @throws Exception if an error occurs
     */
    private void checkMagnification(long magnification) throws Exception {

        documentWriterOptions.setMagnification(magnification);
        documentWriter = new DviDocumentWriter(configuration,
                documentWriterOptions);
        ((SingleDocumentStream) documentWriter).setOutputStream(outputStream);
        FixedCount[] pageNo = null;
        documentWriter.shipout(new PageImpl(nodeList, pageNo));
    }

    /**
     * Test magnifications in the document writer options.
     *
     * @throws Exception if an error occurs
     */
    public void testMagnification() throws Exception {

        boolean gotRangeException = false;

        checkMagnification(-1); // TODO
        checkMagnification(10);
        checkMagnification(100);
        checkMagnification(1000);
        checkMagnification((2l << 30) - 1); // test 2^30-1

        try {
            checkMagnification(2l << 30); // test 2^30
        } catch (GeneralException e) {
            gotRangeException = true;
        }
        assertTrue(gotRangeException);
    }

    private class MockFixedCount implements FixedCount {

        private long value;

        public MockFixedCount(final long theValue) {

            value = theValue;
        }

        public long getValue() {

            return value;
        }

        /**
         * @see de.dante.extex.interpreter.type.count.FixedCount#eq(de.dante.extex.interpreter.type.count.FixedCount)
         */
        public boolean eq(final FixedCount count) {

            // TODO eq unimplemented
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.type.count.FixedCount#ge(de.dante.extex.interpreter.type.count.FixedCount)
         */
        public boolean ge(final FixedCount count) {

            // TODO ge unimplemented
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.type.count.FixedCount#gt(de.dante.extex.interpreter.type.count.FixedCount)
         */
        public boolean gt(final FixedCount count) {

            // TODO gt unimplemented
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.type.count.FixedCount#le(de.dante.extex.interpreter.type.count.FixedCount)
         */
        public boolean le(final FixedCount count) {

            // TODO le unimplemented
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.type.count.FixedCount#lt(de.dante.extex.interpreter.type.count.FixedCount)
         */
        public boolean lt(final FixedCount count) {

            // TODO lt unimplemented
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.type.count.FixedCount#ne(de.dante.extex.interpreter.type.count.FixedCount)
         */
        public boolean ne(final FixedCount count) {

            // TODO ne unimplemented
            return false;
        }

        public void toString(final StringBuffer buffer) {

            buffer.append(toString());
        }

        public Tokens toToks(final Context context) throws InterpreterException {

            return new Tokens(context, value);
        }
    }

    private class MockDocumentWriterOptions implements DocumentWriterOptions {

        /**
         * @see de.dante.extex.backend.documentWriter.DocumentWriterOptions#getTokensOption(java.lang.String)
         */
        public Tokens getTokensOption(String name) {

            return null;
        }

        long magnification = 1000;

        public MockDocumentWriterOptions() {

        }

        public FixedCount getCountOption(final String count) {

            return new MockFixedCount(0);
        }

        public FixedDimen getDimenOption(final String dimen) {

            return null;
        }

        public void setMagnification(long theMagnification) {

            magnification = theMagnification;
        }

        public long getMagnification() {

            return magnification;
        }
    }
}
