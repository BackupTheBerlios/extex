/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.documentWriter.text;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;

/**
 * This is a text dummy implementation of a document writer (very simple).
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class TextDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            NodeVisitor {

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public TextDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() {

        // nothing to do
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "txt";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * process Node
     * @param nodes the nodelist
     * @throws IOException ...
     */
    private void processNodes(final NodeList nodes) throws IOException {

        NodeIterator it = nodes.iterator();
        showNode(nodes);
        while (it.hasNext()) {
            Node n = it.next();
            if (n instanceof NodeList) {
                processNodes((NodeList) n);
            } else {
                showNode(n);
            }
        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException {

        processNodes(nodes);
        out.write('\n');
        out.flush();
        shippedPages++;
    }

    /**
     * show node
     * @param node the node
     * @throws IOException ...
     */
    private void showNode(final Node node) throws IOException {

        try {
            Object o = node.visit(this, node);
            if (o != null) {
                if (o instanceof String) {
                    out.write(((String) o).getBytes());
                }
            }
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(Node,
     * java.lang.Object)
     */
    public Object visitAdjust(final Node value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(Node,
     * java.lang.Object)
     */
    public Object visitAfterMath(final Node value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(Node,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final Node value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(Node,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final Node node, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(Node,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(Node,
     * java.lang.Object)
     */
    public Object visitChar(final Node node, final Object value) {

        return node.toString();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(Node,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(Node,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(Node,
     * java.lang.Object)
     */
    public Object visitGlue(final Node node, final Object value) {

        return " ";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(Node,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final Node node, final Object value) {

        return "\n";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(Node,
     * java.lang.Object)
     */
    public Object visitInsertion(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(Node,
     * java.lang.Object)
     */
    public Object visitKern(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(Node,
     * java.lang.Object)
     */
    public Object visitLigature(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(Node,
     * java.lang.Object)
     */
    public Object visitMark(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(Node,
     * java.lang.Object)
     */
    public Object visitPenalty(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(Node,
     * java.lang.Object)
     */
    public Object visitRule(final Node node, final Object value) {

        return "\n---------------------------------------------------------------------";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(Node,
     * java.lang.Object)
     */
    public Object visitSpace(final Node node, final Object value) {

        return " ";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(Node,
     * java.lang.Object)
     */
    public Object visitVerticalList(final Node node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(Node,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final Node nde, final Object value) {

        return null;
    }
}