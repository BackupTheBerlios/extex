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

// created: 2004-07-27
package de.dante.extex.documentWriter.dvi;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.KernNode;
import de.dante.extex.interpreter.type.node.RuleNode;
import de.dante.extex.interpreter.type.node.WhatsItNode;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import java.io.IOException;
import java.io.OutputStream;




/**
 * This is a implementation of a dvi document writer.
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.2 $
 */
public class DviDocumentWriter implements DocumentWriter {
    // TODO: docu (TE)
    /*
     * TODO:
     * - perhaps it is better to put the mode in the visitor-argument
     * - handle first vertical box special
     * (TE)
     */


    /**
     * The variable <code>debug</code> turn debug on or off.
     *
     */
    private final boolean debug = false;


    /**
     * Configuration of ExTeX.
     *
     */
    private Configuration configuration = null;


    /**
     * DviWriter used to write the DviFile.
     *
     */
    private DviWriter dviWriter = null;


    /**
     * Set iff we are at the beginning of the dvi-file.
     *
     */
    private boolean isBeginDviFile = true;


    /**
     * Saves the current font.  Need the check if there is a font
     * change needed.
     *
     */
    private Font currentFont = null;


    /**
     * Visitor for the nodes.
     *
     */
    private DebugableNodeVisitor visitor = null;


    /**
     * Options of the document writer.
     *
     */
    private DocumentWriterOptions documentWriterOptions = null;


    /**
     * Current mode (<code>{@link
     * de.dante.extex.typesetter.Mode#VERTICAL Mode.VERTICAL}</code>
     * or <code>{@link de.dante.extex.typesetter.Mode#HORIZONTAL
     * Mode.HORIZONTAL}</code>).
     *
     */
    private Mode mode = Mode.VERTICAL;


    /**
     * Internal <code>NodeVisitor</code> of this class.
     *
     */
    private final class DviVisitor
        implements NodeVisitor,
                   DebugableNodeVisitor {

        /**
         * Writer for the dvi code.  The writer knows the dvi format.
         *
         */
        private DviWriter dviWriter = null;

        /**
         * Visitor for nested nodes.  This is normally
         * <code>this</code>.  It changes during debugging.
         *
         */
        private NodeVisitor visitor = this;



        /**
         * Creates a new instance.
         *
         * @param theDviWriter writer for the dvi output
         */
        public DviVisitor (final DviWriter theDviWriter) {
            dviWriter = theDviWriter;
        }


        public void setVisitor(final NodeVisitor theVisitor) {
            // this method is needed for debugging

            visitor = theVisitor;
        }


        private void writeNodes(final NodeList nodes) throws GeneralException {
            NodeIterator iterator = nodes.iterator();

            dviWriter.saveCurrentPositions();

            dviWriter.writeHorizontalSpace(nodes.getMove());
            dviWriter.writeVerticalSpace(nodes.getShift());
            while (iterator.hasNext()) {
                Node node = iterator.next();
                node.visit(visitor, node, null);
            }
            dviWriter.restoreCurrentPositions();
        }


        public Object visitAdjust(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitAfterMath(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitAlignedLeaders(final Object value,
                                          final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitBeforeMath(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitCenteredLeaders(final Object value,
                                           final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitChar(final Object value, final Object value2)
            throws GeneralException {

            CharNode node = (CharNode) value;
            Font font = node.getTypesettingContext().getFont();

            if (currentFont != font) {
                dviWriter.selectFont(font);
                currentFont = font;
            }

            dviWriter.writeNode(node);

            return null;
        }


        public Object visitDiscretionary(final Object value,
                                         final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitExpandedLeaders(final Object value,
                                           final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitGlue(final Object value, final Object value2)
            throws GeneralException {

            GlueNode node = (GlueNode) value;

            dviWriter.writeSpace(node.getWidth(), mode);

            return null;
        }


        public Object visitHorizontalList(final Object value,
                                          final Object value2)
            throws GeneralException {

            NodeList nodes = (NodeList) value;
            Mode oldMode = mode;


            mode = Mode.HORIZONTAL;

            writeNodes(nodes);

            mode = oldMode;
            return null;
        }


        public Object visitInsertion(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitKern(final Object value, final Object value2)
            throws GeneralException {

            KernNode node = (KernNode) value;

            dviWriter.writeSpace(node.getWidth(), mode);

            return null;
        }


        public Object visitLigature(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitMark(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitPenalty(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitRule(final Object value, final Object value2)
            throws GeneralException {

            dviWriter.writeNode((RuleNode) value);

            return null;
        }


        public Object visitSpace(final Object value, final Object value2)
            throws GeneralException {

            // TODO
            return null;
        }


        public Object visitVerticalList(final Object value, final Object value2)
            throws GeneralException {

            NodeList nodes = (NodeList) value;
            Mode oldMode = mode;

            mode = Mode.VERTICAL;

            writeNodes(nodes);


            mode = oldMode;
            return null;
        }


        public Object visitWhatsIt(final Object value, final Object value2)
            throws GeneralException {

            dviWriter.writeNode((WhatsItNode) value);
            return null;
        }

    } // end class DviVisitor



    /**
     * Creates a new instance.
     *
     * @param theCfg configuration of ExTeX
     * @param options options for <code>DviDocumentWriter</code>
     */
    public DviDocumentWriter(final Configuration theCfg,
                             final DocumentWriterOptions options) {
        super();
        this.configuration = theCfg;
        documentWriterOptions = options;
    }


    /**
     * This method is invoked upon the end of the processing.  End of
     * dvi file is written.
     *
     * @exception GeneralException if an error occurs
     * @exception IOException if an error occurs
     */
    public void close() throws GeneralException, IOException {
        dviWriter.endDviFile();
    }


    /**
     * Getter for the extension associated with dvi output.
     *
     * @return normally "dvi"
     */
    public String getExtension() {
        return "dvi";
    }


    /**
     * Setter for the output stream.  This method throws no exception.
     * If somethings goes wrong {@link
     * #shipout(de.dante.extex.typesetter.NodeList) shipout(NodeList)}
     * informs the caller.
     *
     * @param writer an <code>OutputStream</code> value
     */
    public void setOutputStream(final OutputStream writer) {
        dviWriter = new DviWriter(writer, documentWriterOptions);
        visitor = new DviVisitor(dviWriter);

        if (debug) {
            visitor = new DebugNodeVisitor(visitor);
        }
    }


    /**
     * Get the number of written pages until now.
     *
     * @return the number of written pages
     */
    public int getPages() {
        return dviWriter.getPages();
    }


    /**
     * This is the entry point for the document writer.  Exceptions of
     * the initialisation of the class will be thrown here.
     *
     * @param nodes a <code>NodeList</code> value
     * @exception GeneralException if an error occurs
     * @exception IOException if an error occurs
     */
    public void shipout(final NodeList nodes)
        throws GeneralException, IOException {

        GeneralException error;

        if (isBeginDviFile) {
            isBeginDviFile = false;
            dviWriter.beginDviFile();
        }

        currentFont = null;

        mode = Mode.VERTICAL;
        dviWriter.beginPage();

        nodes.visit(visitor, nodes, null);

        dviWriter.endPage();

        error = dviWriter.getError();
        if (error != null) {
            throw new GeneralException(error);
        }
    }


    /**
     * Setter of an named parameter.  This Documentwriter supports no
     * parameters yet.
     *
     * @param name a <code>String</code> value
     * @param value a <code>String</code> value
     */
    public void setParameter(final String name, final String value) {

        // there no paramters yes
    }
}
