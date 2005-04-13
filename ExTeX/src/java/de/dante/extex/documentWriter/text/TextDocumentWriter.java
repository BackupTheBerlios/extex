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
import de.dante.extex.documentWriter.exception.DocumentWriterClosedChannelException;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.documentWriter.exception.DocumentWriterIOException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.node.AdjustNode;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.AlignedLeadersNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.CenteredLeadersNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.ExpandedLeadersNode;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.InsertionNode;
import de.dante.extex.typesetter.type.node.KernNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.MarkNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.SpaceNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;

/**
 * This is a text dummy implementation of a document writer (very simple).
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
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
    public void close() throws DocumentWriterIOException {

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                throw new DocumentWriterIOException(e);
            }
            out = null;
        } else {
            throw new DocumentWriterClosedChannelException("clodes channel");
        }
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
    private void processNodes(final NodeList nodes)
            throws DocumentWriterException {

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
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes) throws DocumentWriterException {

        try {
            processNodes(nodes);
            out.write('\n');
            out.flush();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        shippedPages++;
    }

    /**
     * show node
     * @param node the node
     * @throws IOException ...
     */
    private void showNode(final Node node) throws DocumentWriterException {

        try {
            Object o = node.visit(this, node);
            if (o != null) {
                if (o instanceof String) {
                    out.write(((String) o).getBytes());
                }
            }
        } catch (GeneralException e) {
            e.printStackTrace();

        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
     * java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
     * java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode value, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode value,
            final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
     * java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value) {

        return node.toString();
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
     * java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value) {

        return " ";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object value) {

        return "\n";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
     * java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
     * java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
     * java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
     * java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
     * java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
     * java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value) {

        return "\n---------------------------------------------------------------------";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
     * java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value) {

        return " ";
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
     * java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(de.dante.extex.typesetter.type.node.VirtualCharNode, java.lang.Object)
     */
    public Object visitVirtualChar(final VirtualCharNode node,
            final Object value) throws GeneralException {

        // TODO visitVirtualChar unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode nde, final Object value) {

        return null;
    }
}