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

package de.dante.extex.documentWriter.dump;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.configuration.Configurable;

/**
 * This is an implementation of a document writer wich can act both as sample
 * and as.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public class DumpDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            Configurable {

    /**
     * This class provides the internal node visitor to traverse the nodes.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.20 $
     */
    private static class Visitor implements NodeVisitor {

        /**
         * The field <tt>out</tt> contains the output stream.
         */
        private OutputStream out;

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
                if (vmode) {
                    out.write('\n');
                }
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
         * Setter for the output stream.
         *
         * @param outStream the output stream
         */
        public void setOutputStream(final OutputStream outStream) {

            out = outStream;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(Node, java.lang.Object)
         */
        public Object visitAdjust(final Node oNode, final Object oOut)
                throws GeneralException {

            // TODO gene: visitAdjust unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(Node, java.lang.Object)
         */
        public Object visitAfterMath(final Node oNode, final Object oOut)
                throws GeneralException {

            write("\\)");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(Node, java.lang.Object)
         */
        public Object visitAlignedLeaders(final Node oNode, final Object oOut)
                throws GeneralException {

            // TODO gene: visitAlignedLeaders unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(Node, java.lang.Object)
         */
        public Object visitBeforeMath(final Node oNode, final Object oOut)
                throws GeneralException {

            write("\\(");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(Node, java.lang.Object)
         */
        public Object visitCenteredLeaders(final Node oNode, final Object oOut)
                throws GeneralException {

            // TODO gene: visitCenteredLeaders unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(Node, java.lang.Object)
         */
        public Object visitChar(final Node oNode, final Object oOut)
                throws GeneralException {

            write(((CharNode) oNode).getCharacter().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(Node, java.lang.Object)
         */
        public Object visitDiscretionary(final Node oNode, final Object oOut)
                throws GeneralException {

            write("--");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(Node, java.lang.Object)
         */
        public Object visitExpandedLeaders(final Node oNode, final Object oOut)
                throws GeneralException {

            // TODO gene: visitExpandedLeaders unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(Node, java.lang.Object)
         */
        public Object visitGlue(final Node oNode, final Object oOut)
                throws GeneralException {

            write(' ');
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(Node, java.lang.Object)
         */
        public Object visitHorizontalList(final Node oNode, final Object oOut)
                throws GeneralException {

            boolean mode = vmode;
            vmode = false;
            NodeList list = (HorizontalListNode) oNode;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).visit(this, oOut);
            }
            vmode = mode;
            nl();
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(Node, java.lang.Object)
         */
        public Object visitInsertion(final Node oNode, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(Node, java.lang.Object)
         */
        public Object visitKern(final Node oNode, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(Node, java.lang.Object)
         */
        public Object visitLigature(final Node oNode, final Object oOut)
                throws GeneralException {

            write(((LigatureNode) oNode).getCharacter().getCodePoint());
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(Node, java.lang.Object)
         */
        public Object visitMark(final Node oNode, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(Node, java.lang.Object)
         */
        public Object visitPenalty(final Node oNode, final Object oOut)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(Node, java.lang.Object)
         */
        public Object visitRule(final Node oNode, final Object oOut)
                throws GeneralException {

            write("---");
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(Node, java.lang.Object)
         */
        public Object visitSpace(final Node oNode, final Object oOut)
                throws GeneralException {

            write(' ');
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(Node, java.lang.Object)
         */
        public Object visitVerticalList(final Node oNode, final Object oOut)
                throws GeneralException {

            boolean mode = vmode;
            vmode = true;
            NodeList list = (VerticalListNode) oNode;
            for (int i = 0; i < list.size(); i++) {
                list.get(i).visit(this, oOut);
            }
            vmode = mode;
            nl();
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(Node, java.lang.Object)
         */
        public Object visitWhatsIt(final Node oNode, final Object oOut)
                throws GeneralException {

            return null;
        }
    }

    /**
     * The field <tt>nv</tt> contains the node visitor instance to use.
     */
    private Visitor nodeVisitor = new Visitor();

    /**
     * The field <tt>out</tt> contains the outut stream to use.
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> contains the number of pages already
     * shipped out.
     */
    private int shippedPages = 0;

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
    public void close() {

        // nothing to do
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        tree = Boolean.getBoolean(config.getAttribute("tree"));
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "out";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
        nodeVisitor.setOutputStream(out);
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String, java.lang.String)
     */
    public void setParameter(final String name, final String value) {

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException {

        if (tree) {
            StringBuffer sb = new StringBuffer();
            nodes.toString(sb, "\n");
            out.write(sb.toString().getBytes());
            out.write('\n');
        } else {
            try {
                nodes.visit(nodeVisitor, out);
            } catch (GeneralException e) {
                throw new IOException(e.getLocalizedMessage());
            }
        }
        shippedPages++;
    }

}