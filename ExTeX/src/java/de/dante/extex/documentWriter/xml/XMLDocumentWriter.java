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

package de.dante.extex.documentWriter.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.documentWriter.exception.DocumentWriterClosedChannelException;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.documentWriter.exception.DocumentWriterIOException;
import de.dante.extex.interpreter.type.dimen.Dimen;
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
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.xml.XMLStreamWriter;

/**
 * This is a xml implementation of a document writer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.21 $
 */
public class XMLDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            NodeVisitor {

    /**
     * DIN-A4 height
     */
    private static final double DINA4HEIGHT = 29.7d;

    /**
     * DIN-A4 width
     */
    private static final double DINA4WIDTH = 21.0d;

    /**
     * format the output for decimal values.
     */
    private static final NumberFormat FORMAT = NumberFormat
            .getInstance(Locale.US);

    /**
     * max fraction
     */
    private static final int MAXFRACTION = 4;

    /**
     * current x position
     */
    private Dimen currentX = new Dimen();

    /**
     * current y postition
     */
    private Dimen currentY = new Dimen();

    /**
     * debug
     */
    private boolean debug = true;

    /**
     * documentwriter options
     */
    private DocumentWriterOptions docoptions;

    /**
     * encdoing
     */
    private String encoding = "ISO-8859-1";

    /**
     * xml indent
     */
    private String indent = "   ";

    /**
     * xml newlines
     */
    private boolean newlines = true;

    /**
     * The output stream.
     */
    private OutputStream out = null;

    /**
     * paperheight
     */
    private Dimen paperheight;

    /**
     * paperwidth
     */
    private Dimen paperwidth;

    /**
     * The number of pages, wich are ship out.
     */
    private int shippedPages = 0;

    /**
     * show visible chars
     */
    private boolean showvisible = true;

    /**
     * xml trimallwhitespace TODO incomplete
     */
    private boolean trimallwhitespace = true;

    /**
     * use bp
     */
    private boolean usebp = true;

    /**
     * use mm
     */
    private boolean usemm = true;

    /**
     * use sp
     */
    private boolean usesp = true;

    /**
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public XMLDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
        docoptions = options;
        FORMAT.setGroupingUsed(false);
        FORMAT.setMaximumFractionDigits(MAXFRACTION);

        if (cfg != null) {
            String tmp = cfg.getAttribute("encoding");
            if (tmp != null && !tmp.equals("")) {
                encoding = tmp;
            }
            tmp = cfg.getAttribute("debug");
            if (tmp != null) {
                debug = Boolean.valueOf(tmp).booleanValue();
            }
            tmp = cfg.getAttribute("showvisible");
            if (tmp != null) {
                showvisible = Boolean.valueOf(tmp).booleanValue();
            }
            tmp = cfg.getAttribute("indent");
            if (tmp != null) {
                indent = tmp;
            }
            tmp = cfg.getAttribute("newlines");
            if (tmp != null) {
                newlines = Boolean.valueOf(tmp).booleanValue();
            }
            tmp = cfg.getAttribute("trimallwhitespace");
            if (tmp != null) {
                trimallwhitespace = Boolean.valueOf(tmp).booleanValue();
            }
            tmp = cfg.getAttribute("usesp");
            if (tmp != null) {
                usesp = Boolean.valueOf(tmp).booleanValue();
            }
            tmp = cfg.getAttribute("usebp");
            if (tmp != null) {
                usebp = Boolean.valueOf(tmp).booleanValue();
            }
            tmp = cfg.getAttribute("usemm");
            if (tmp != null) {
                usemm = Boolean.valueOf(tmp).booleanValue();
            }
        }
    }

    /**
     * Add some Attributes to the node-element
     * @param node      the node
     * @throws IOException if an error occurs.
     */
    private void addNodeAttributes(final Node node) throws IOException {

        Dimen wd = new Dimen(node.getWidth());
        Dimen ht = new Dimen(node.getHeight());
        Dimen dp = new Dimen(node.getDepth());
        // TODO move + shift
        // Dimen move = new Dimen();
        // Dimen shift = new Dimen();

        // --------------------------------------------------------
        setDimenLength("x", currentX);
        setDimenLength("y", currentY);
        setDimenLength("width", wd);
        setDimenLength("height", ht);
        setDimenLength("depth", dp);
        //setDimenLength("move", move);
        //setDimenLength("shift", shift);

        // ---- debug ----
        if (debug) {
            writer.writeCharacters(node.toString());
        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws DocumentWriterException {

        if (out != null) {
            try {
                if (writer != null) {
                    writer.writeEndElement();
                    writer.writeEndDocument();
                    writer.close();
                }
                out.close();
            } catch (IOException e) {
                throw new DocumentWriterIOException(e);
            }
            out = null;
        } else {
            throw new DocumentWriterClosedChannelException("closed channel");
        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "xml";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * Set the Attribute for an element with sp, bp, mm
     * @param name      the attribute-name
     * @param dimen     the dimen
     * @throws IOException if an error occurs.
     */
    private void setDimenLength(final String name, final Dimen dimen)
            throws IOException {

        Dimen d = dimen;
        if (dimen == null) {
            d = new Dimen();
        }
        if (usesp) {
            writer.writeAttribute(name + "_sp", String.valueOf(d.getValue()));
        }
        if (usebp) {
            writer.writeAttribute(name + "_bp", String.valueOf(Unit
                    .getDimenAsBP(d)));
        }
        if (usemm) {
            String s = FORMAT.format(Unit.getDimenAsMM(d));
            writer.writeAttribute(name + "_mm", s);
        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
    }

    /**
     * map for the parameters.
     */
    private Map param = new HashMap();

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        param.put(name, value);
    }

    /**
     * Print the parameter as comment.
     * @throws IOException if an error occurs.
     */
    private void printParameterComment() throws IOException {

        StringBuffer buf = new StringBuffer();
        buf.append("\n");
        Iterator it = param.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            buf.append(name);
            buf.append("=");
            buf.append(param.get(name));
            buf.append("\n");
        }
        writer.writeComment(buf.toString());
    }

    /**
     * Print the parameter as element.
     * @throws IOException if an error occurs.
     */
    private void printParameterElement() throws IOException {

        writer.writeStartElement("parameter");
        Iterator it = param.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            writer.writeStartElement(name);
            writer.writeCharacters((String) param.get(name));
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    /**
     * The XML stream writer.
     */
    private XMLStreamWriter writer;

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes) throws GeneralException {

        try {
            if (writer == null) {
                if (out == null) {
                    throw new DocumentWriterClosedChannelException(
                            "closed channel");
                }
                writer = new XMLStreamWriter(out, encoding);
                writer.setBeauty(newlines);
                writer.setIndent(indent);
                writer.writeStartDocument();
                printParameterComment();
                writer.writeStartElement("root");
                printParameterElement();
            }

            writer.writeStartElement("page");
            writer.writeAttribute("id", String.valueOf(shippedPages + 1));

            // TeX primitives should set the papersize in any way:
            // o \paperwidth   / \paperheight,
            // o \pdfpagewidth / \pdfpageheight <-- pdfTeX
            // o \mediawidth   / \mediaheight   <-- VTeX
            if (docoptions != null) {
                paperwidth = (Dimen) docoptions.getDimenOption("paperwidth");
                paperheight = (Dimen) docoptions.getDimenOption("paperheight");
                if (paperheight.getValue() == 0 || paperwidth.getValue() == 0) {
                    // use DIN A4
                    paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
                    paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
                }
                setDimenLength("paperwidth", paperwidth);
                setDimenLength("paperheight", paperheight);
            } else {
                // use DIN A4
                paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
                paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
            }

            // set start point
            currentX.set(Dimen.ONE_INCH);
            currentY.set(Dimen.ONE_INCH);

            nodes.visit(this, nodes);

            shippedPages++;
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
    }

    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
     * java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object value2)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("adjust");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
     * java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value2)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("aftermath");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value2) throws DocumentWriterException {

        try {
            writer.writeStartElement("alignedleaders");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("beforemath");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) throws DocumentWriterException {

        try {
            writer.writeStartElement("centeredleaders");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
     * java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("char");
            UnicodeChar uc = node.getCharacter();
            addNodeAttributes(node);
            writer.writeAttribute("font", node.getTypesettingContext()
                    .getFont().getFontName());
            writer.writeAttribute("color", node.getTypesettingContext()
                    .getColor().toString());
            writer.writeAttribute("codepoint", String
                    .valueOf(uc.getCodePoint()));
            String ucname = uc.getUnicodeName();
            if (ucname != null) {
                writer.writeAttribute("unicode", ucname);
            }
            if (showvisible) {
                String c = ".";
                if (uc.isPrintable()) {
                    c = uc.toString();
                }
                writer.writeAttribute("visiblechar", c);
            }
            currentX.add(node.getWidth());
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) throws DocumentWriterException {

        try {
            writer.writeStartElement("discretionary");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) throws DocumentWriterException {

        try {
            writer.writeStartElement("expandedleaders");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
     * java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("glue");
            addNodeAttributes(node);
            writer.writeEndElement();
            currentX.add(node.getWidth());
            currentY.add(node.getHeight());
            currentY.add(node.getDepth());
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws GeneralException {

        try {
            writer.writeStartElement("horizontallist");
            addNodeAttributes(node);

            Dimen saveX = new Dimen(currentX);
            Dimen saveY = new Dimen(currentY);

            NodeIterator it = node.iterator();
            while (it.hasNext()) {
                Node newnode = it.next();
                newnode.visit(this, node);
            }
            currentX.set(saveX);
            currentY.set(saveY);
            currentX.add(node.getWidth());

            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
     * java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("insertion");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
     * java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("kern");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
     * java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value)
            throws GeneralException {

        try {
            writer.writeStartElement("ligature");
            addNodeAttributes(node);

            Node first = node.getLeft();
            Node second = node.getRight();
            if (first != null) {
                node.visit(this, first);
            }
            if (second != null) {
                node.visit(this, second);
            }

            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
     * java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("mark");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
     * java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("penalty");
            addNodeAttributes(node);
            writer.writeAttribute("penalty", String.valueOf(node.getPenalty()));
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
     * java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("rule");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
     * java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("space");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
     * java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        try {
            writer.writeStartElement("verticallist");
            addNodeAttributes(node);

            Dimen saveX = new Dimen(currentX);
            Dimen saveY = new Dimen(currentY);

            NodeIterator it = node.iterator();
            while (it.hasNext()) {
                Node newnode = it.next();

                newnode.visit(this, node);
            }
            currentX.set(saveX);
            currentY.set(saveY);
            currentY.add(node.getDepth());
            currentY.add(node.getHeight());

            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
     *      de.dante.extex.typesetter.type.node.VirtualCharNode,
     *      java.lang.Object)
     */
    public Object visitVirtualChar(final VirtualCharNode node,
            final Object value) throws GeneralException {

        try {
            writer.writeStartElement("virtualchar");
            addNodeAttributes(node);

            Dimen saveX = new Dimen(currentX);
            Dimen saveY = new Dimen(currentY);

            NodeIterator it = node.iterator();
            while (it.hasNext()) {
                Node newnode = it.next();

                newnode.visit(this, node);
            }
            currentX.set(saveX);
            currentY.set(saveY);
            currentX.add(node.getWidth());

            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("whatsit");
            addNodeAttributes(node);
            writer.writeEndElement();
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }
}