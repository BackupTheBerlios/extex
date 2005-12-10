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
import de.dante.extex.backend.documentWriter.PdftexSupport;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
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
import de.dante.extex.typesetter.type.node.pdftex.PdfAnnotation;
import de.dante.extex.typesetter.type.node.pdftex.PdfObject;
import de.dante.extex.typesetter.type.node.pdftex.PdfRefXImage;
import de.dante.extex.typesetter.type.node.pdftex.PdfXForm;
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
 * @version $Revision: 1.5 $
 */
public class DumpDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            PdftexSupport,
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
         * Print a new line in vertical mode.
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
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
         *      de.dante.extex.typesetter.type.node.WhatsItNode,
         *      java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object oOut)
                throws GeneralException {

            return null;
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
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {

        if (out != null) {
            out.close();
            out = null;
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        tree = Boolean.valueOf(config.getAttribute("tree")).booleanValue();
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#getAnnotation(de.dante.extex.typesetter.type.node.RuleNode, java.lang.String)
     */
    public PdfAnnotation getAnnotation(final RuleNode node,
            final String annotation) throws InterpreterException {

        return null;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "out";
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#getObject(java.lang.String, boolean, java.lang.String)
     */
    public PdfObject getObject(final String attr, final boolean isStream,
            final String text) throws InterpreterException {

        return null;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#getXForm(java.lang.String, java.lang.String, de.dante.extex.interpreter.type.box.Box)
     */
    public PdfXForm getXForm(final String attr, final String resources,
            final Box box) throws InterpreterException {

        return null;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#getXImage(java.lang.String, de.dante.extex.typesetter.type.node.RuleNode, java.lang.String, long, boolean)
     */
    public PdfRefXImage getXImage(final String resource, final RuleNode rule,
            final String attr, final long page, final boolean immediate)
            throws InterpreterException {

        return null;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdfcatalog(java.lang.String, de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec)
     */
    public void pdfcatalog(final String text, final ActionSpec action) {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdffontname(de.dante.extex.interpreter.type.font.Font)
     */
    public String pdffontname(final Font font) {

        return "42";
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdffontobjnum(de.dante.extex.interpreter.type.font.Font)
     */
    public long pdffontobjnum(final Font font) {

        return 0;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdfincludechars(de.dante.extex.interpreter.type.font.Font, java.lang.String)
     */
    public void pdfincludechars(final Font font, final String text) {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdfinfo(java.lang.String)
     */
    public void pdfinfo(final String text) {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdflastannot()
     */
    public long pdflastannot() {

        return 0;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdflastobj()
     */
    public long pdflastobj() {

        return 0;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdflastxform()
     */
    public long pdflastxform() {

        return 0;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdflastximage()
     */
    public long pdflastximage() {

        return 0;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdfnames(java.lang.String)
     */
    public void pdfnames(final String text) {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.PdftexSupport#pdfoutline(de.dante.extex.interpreter.primitives.pdftex.util.action.ActionSpec, long, java.lang.String)
     */
    public void pdfoutline(final ActionSpec action, final long count,
            final String text) {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.SingleDocumentStream#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String, java.lang.String)
     */
    public void setParameter(final String name, final String value) {

    }

    /**
     * Setter for tree.
     *
     * @param tree the tree to set
     */
    public void setTree(final boolean tree) {

        this.tree = tree;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
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