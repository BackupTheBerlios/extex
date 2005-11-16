/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter.dump;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.interpreter.type.dimen.Dimen;
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
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This is an implementation of a document writer which can act both as sample
 * and as tool for testing.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class DumpDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            Configurable {

    /**
     * The field <tt>nodeVisitor</tt> contains the node visitor instance to use
     * in the form of an anonymous inner class.
     */
    private NodeVisitor nodeVisitor = new NodeVisitor() {

        /**
         * The field <tt>vmode</tt> contains the indicator that a vlist is
         * processed.
         */
        private boolean vmode = false;

        /**
         * Write a string to out.
         *
         * @param s the string to write
         *
         * @throws GeneralException in case of an error
         */
        private void write(final String s) throws GeneralException {

            try {
                out.write(s.getBytes());
                if (vmode) {
                    out.write('\n');
                }
            } catch (IOException e) {
                throw new GeneralException(e);
            }

        }

        /**
         * Write a char to out.
         *
         * @param s the char to write
         *
         * @throws GeneralException in case of an error
         */
        private void write(final int s) throws GeneralException {

            try {
                out.write(s);
            } catch (IOException e) {
                throw new GeneralException(e);
            }
        }

        /**
         * Print a nl in vmode.
         *
         * @throws GeneralException in case of an error
         */
        private void nl() throws GeneralException {

            if (vmode) {
                try {
                    out.write('\n');
                } catch (IOException e) {
                    throw new GeneralException(e);
                }
            }
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
         *      de.dante.extex.typesetter.type.node.AdjustNode,
         *      java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object oOut)
                throws GeneralException {

            write("\n");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
         *      de.dante.extex.typesetter.type.node.AfterMathNode,
         *      java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode oNode,
                final Object oOut) throws GeneralException {

            write("\\)");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
         *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object oOut) throws GeneralException {

            write(" ");
            node.visit(this, oOut);
            node.visit(this, oOut);
            write("  ");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
         *      de.dante.extex.typesetter.type.node.BeforeMathNode,
         *      java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object oOut) throws GeneralException {

            write("\\(");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
         *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
         *      java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object oOut) throws GeneralException {

            write("  ");
            node.visit(this, oOut);
            node.visit(this, oOut);
            write("  ");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
         *      de.dante.extex.typesetter.type.node.CharNode,
         *      java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object oOut)
                throws GeneralException {

            write(node.getCharacter().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
         *      de.dante.extex.typesetter.type.node.VirtualCharNode,
         *      java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object oOut) throws GeneralException {

            write(node.getCharacter().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
         *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
         *      java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object oOut) throws GeneralException {

            write("--");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
         *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object oOut) throws GeneralException {

            write("  ");
            node.visit(this, oOut);
            node.visit(this, oOut);
            write(" ");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
         *      de.dante.extex.typesetter.type.node.GlueNode,
         *      java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object oOut)
                throws GeneralException {

            if (vmode) {
                if (node.getHeight().ne(Dimen.ZERO_PT)
                        && node.getDepth().ne(Dimen.ZERO_PT)) {
                    write('\n');
                }
            } else {
                if (node.getWidth().ne(Dimen.ZERO_PT)) {
                    write(' ');
                }
            }
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
         *      de.dante.extex.typesetter.type.node.HorizontalListNode,
         *      java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode list,
                final Object oOut) throws GeneralException {

            boolean mode = vmode;
            vmode = false;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).visit(this, oOut);
            }
            vmode = mode;
            nl();
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
         *      de.dante.extex.typesetter.type.node.InsertionNode,
         *      java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
         *      de.dante.extex.typesetter.type.node.KernNode,
         *      java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
         *      de.dante.extex.typesetter.type.node.LigatureNode,
         *      java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object oOut)
                throws GeneralException {

            write(node.getCharacter().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
         *      de.dante.extex.typesetter.type.node.MarkNode,
         *      java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
         *      de.dante.extex.typesetter.type.node.PenaltyNode,
         *      java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
         *      de.dante.extex.typesetter.type.node.RuleNode,
         *      java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object oOut)
                throws GeneralException {

            write("---");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
         *      de.dante.extex.typesetter.type.node.SpaceNode,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object oOut)
                throws GeneralException {

            write(' ');
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
         *      de.dante.extex.typesetter.type.node.VerticalListNode,
         *      java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode list,
                final Object oOut) throws GeneralException {

            boolean mode = vmode;
            vmode = true;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).visit(this, oOut);
            }
            vmode = mode;
            nl();
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
         *      de.dante.extex.typesetter.type.node.WhatsItNode,
         *      java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object oOut)
                throws GeneralException {

            return null;
        }
    };

    /**
     * The field <tt>out</tt> contains the output stream to use.
     */
    private OutputStream out = null;

    /**
     * The field <tt>tree</tt> contains the indicator whether to use the tree
     * representation.
     */
    private boolean tree = true;

    /**
     * Creates a new object.
     *
     * @param opts the dynamic access to the context
     */
    public DumpDocumentWriter(final DocumentWriterOptions opts) {

        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {

        if (out != null) {
            out.close();
            out = null;
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        tree = Boolean.valueOf(config.getAttribute("tree")).booleanValue();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "out";
    }

    /**
     * @see de.dante.extex.documentWriter.SingleDocumentStream#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String, java.lang.String)
     */
    public void setParameter(final String name, final String value) {

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *     de.dante.extex.typesetter.type.NodeList)
     */
    public int shipout(final Page page) throws DocumentWriterException {

        NodeList nodes = page.getNodes();
        try {
            if (tree) {
                StringBuffer sb = new StringBuffer();
                nodes.toString(sb, "\n");
                out.write(sb.toString().getBytes());
                out.write('\n');
            } else {
                nodes.visit(nodeVisitor, out);
                out.write('\n');
            }
        } catch (IOException e) {
            throw new DocumentWriterException(e);
        } catch (GeneralException e) {
            Throwable ex = e.getCause();
            throw (ex instanceof DocumentWriterException //
                    ? (DocumentWriterException) ex //
                    : new DocumentWriterException(e.getLocalizedMessage()));
        }
        return 1;
    }

}