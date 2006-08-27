/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter.svg;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.MultipleDocumentStream;
import de.dante.extex.backend.documentWriter.OutputStreamFactory;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterIOException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
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
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.xml.XMLStreamWriter;

/**
 * This is a svg implementation of a document writer.
 *
 * TODO incomplete !!!
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class SVGDocumentWriter
        implements
            DocumentWriter,
            MultipleDocumentStream,
            NodeVisitor {

    /**
     * DIN-A4 height.
     */
    private static final double DINA4HEIGHT = 29.7d;

    /**
     * DIN-A4 width.
     */
    private static final double DINA4WIDTH = 21.0d;

    /**
     * current x position.
     */
    private Dimen currentX = new Dimen();

    /**
     * current y position.
     */
    private Dimen currentY = new Dimen();

    /**
     * debug.
     */
    private boolean debug = true;

    /**
     * The option for the document writer.
     */
    private DocumentWriterOptions docoptions;

    /**
     * The Encoding.
     */
    private String encoding = "ISO-8859-1";

    /**
     * The paper height.
     */
    private Dimen paperheight;

    /**
     * The paper width.
     */
    private Dimen paperwidth;

    /**
     * The field <tt>shippedPages</tt>.
     */
    private int shippedPages = 0;

    /**
     * The XML writer.
     */
    private XMLStreamWriter writer;

    /**
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public SVGDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
        docoptions = options;
        FORMAT.setGroupingUsed(false);
        FORMAT.setMaximumFractionDigits(2);

        if (cfg != null) {
            String tmp = cfg.getAttribute("encoding");
            if (tmp != null && !tmp.equals("")) {
                encoding = tmp;
            }
            tmp = cfg.getAttribute("debug");
            if (tmp != null) {
                debug = Boolean.valueOf(tmp).booleanValue();
            }
        }
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() throws DocumentWriterException {

        // do nothing
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "svg";
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * Set the Attribute for an element with sp, bp, mm.
     * @param name      the attribute-name
     * @param dimen     the dimen
     * @throws IOException if an IO-error occurred.
     */
    private void setDimenLength(final String name, final FixedDimen dimen)
            throws IOException {

        FixedDimen d = dimen;
        if (dimen == null) {
            d = Dimen.ZERO_PT;
        }
        writer.writeAttribute(name, FORMAT.format(Unit.getDimenAsMM(d)) + "mm");
    }

    //    /**
    //     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setOutputStream(java.io.OutputStream)
    //     */
    //    public void setOutputStream(final OutputStream outStream) {
    //
    //        out = outStream;
    //    }

    /**
     * the writer factory.
     */
    private OutputStreamFactory writerFactory;

    /**
     * @see de.dante.extex.backend.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.backend.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory writerfactory) {

        writerFactory = writerfactory;
    }

    /**
     * The map for the parameters.
     */
    private Map param = new HashMap();

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        param.put(name, value);
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public int shipout(final Page page) throws DocumentWriterException,
            GeneralException {

        try {
            NodeList nodes = page.getNodes();

            writer = new XMLStreamWriter(writerFactory
                    .getOutputStream(getExtension()), encoding);
            if (debug) {
                writer.setBeauty(true);
            }
            writer.setDocType("svg", "//W3C//DTD SVG 1.1//EN",
                    "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
            writer.writeStartDocument();
            if (debug) {
                printParameterComment();
            }
            writer.writeStartElement("svg");
            writer.writeAttribute("xmlns", "http://www.w3.org/2000/svg");
            writer.writeAttribute("version", "1.1");
            setPaperHW();

            // set start point
            currentX.set(Dimen.ONE_INCH);
            currentY.set(Dimen.ONE_INCH);

            nodes.visit(this, nodes);

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();

            shippedPages++;
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return shippedPages;
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
     * The decimal format.
     */
    private static final NumberFormat FORMAT = NumberFormat
            .getInstance(Locale.US);

    /**
     * Set the paper height and width.
     * @throws IOException if an IO-error occurred.
     */
    private void setPaperHW() throws IOException {

        // TeX primitives should set the papersize in any way:
        // o \paperwidth   / \paperheight,
        // o \pdfpagewidth / \pdfpageheight <-- pdfTeX
        // o \mediawidth   / \mediaheight   <-- VTeX

        String page = (String) param.get("Paper");
        if (page != null) {
            if (page.equalsIgnoreCase("A4")) {
                // use DIN A4
                paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
                paperheight = Unit.createDimenFromCM(DINA4HEIGHT);

                // TODO incomplete
            } else {
                // use DIN A4
                paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
                paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
            }
        } else if (docoptions != null) {
            paperwidth = (Dimen) docoptions.getDimenOption("paperwidth");
            paperheight = (Dimen) docoptions.getDimenOption("paperheight");
            if (paperheight.getValue() == 0 || paperwidth.getValue() == 0) {
                // use DIN A4
                paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
                paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
            }
        } else {
            // use DIN A4
            paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
            paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
        }
        setDimenLength("width", paperwidth);
        setDimenLength("height", paperheight);
    }

    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
     * java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode value, final Object value2) {

        //        Element element = new Element("adjust");
        //        AdjustNode node = (AdjustNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
     * java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode value, final Object value2) {

        //        Element element = new Element("aftermath");
        //        AfterMathNode node = (AfterMathNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode value,
            final Object value2) {

        //        Element element = new Element("alignedleaders");
        //        AlignedLeadersNode node = (AlignedLeadersNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2) {

        //        Element element = new Element("beforemath");
        //        BeforeMathNode node = (BeforeMathNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        //        Element element = new Element("centeredleaders");
        //        CenteredLeadersNode node = (CenteredLeadersNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
     * java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value)
            throws DocumentWriterException {

        try {
            UnicodeChar uc = node.getCharacter();
            Font font = node.getTypesettingContext().getFont();

            // ------- text --------------
            writer.writeStartElement("text");
            setDimenLength("x", currentX);
            Dimen y = new Dimen(currentY);
            setDimenLength("y", y);
            writer.writeAttribute("font-family", font.getFontName() + ",serif");
            writer.writeAttribute("font-size", String.valueOf(Unit
                    .getDimenAsPT(font.getEm())));
            writer.writeCharacters(uc.toString());
            writer.writeEndElement();

            // ---------------------------------
            writer.writeStartElement("rect");
            setDimenLength("x", currentX);
            y.subtract(node.getHeight());
            setDimenLength("y", y);
            setDimenLength("width", node.getWidth());
            Dimen h = new Dimen(node.getHeight());
            h.add(node.getDepth());
            setDimenLength("height", h);
            writer.writeAttribute("fill", "none");
            writer.writeAttribute("stroke", "blue");
            writer.writeAttribute("strike-width", "1pt");
            writer.writeEndElement();

            // -----------------------------------------------

            currentX.add(node.getWidth());
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
            final Object value) {

        //        Element element = new Element("discretionary");
        //        DiscretionaryNode node = (DiscretionaryNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

        //        Element element = new Element("expandedleaders");
        //        ExpandedLeadersNode node = (ExpandedLeadersNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
     * java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value) {

        //        Element element = new Element("glue");
        //        GlueNode node = (GlueNode) value;
        //        currentX.add(node.getWidth());
        //        currentY.add(node.getHeight());
        //        currentY.add(node.getDepth());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws DocumentWriterException,
            GeneralException {

        try {
            writer.writeStartElement("rect");

            setDimenLength("x", currentX);
            setDimenLength("y", currentY);
            setDimenLength("width", node.getWidth());
            Dimen rH = new Dimen(node.getHeight());
            rH.add(node.getDepth());
            setDimenLength("height", rH);
            writer.writeAttribute("fill", "none");
            writer.writeAttribute("stroke", "red");
            writer.writeAttribute("strike-width", "1pt");

            writer.writeEndElement();

            // ------------------------------------------
            Dimen saveX = new Dimen(currentX);
            Dimen saveY = new Dimen(currentY);

            // set x to baseline
            currentY.add(node.getHeight());

            // baseline
            if (node.getDepth().getValue() != 0) {

                writer.writeStartElement("line");
                setDimenLength("x1", currentX);
                setDimenLength("y1", currentY);
                Dimen x2 = new Dimen(currentX);
                x2.add(node.getWidth());
                setDimenLength("x2", x2);
                setDimenLength("y2", currentY);
                writer.writeAttribute("stroke", "red");
                writer.writeAttribute("strike-width", "1pt");

                writer.writeEndElement();
            }

            NodeIterator it = node.iterator();
            while (it.hasNext()) {
                Node newnode = it.next();
                newnode.visit(this, node);
            }
            currentX.set(saveX);
            currentY.set(saveY);
            currentY.add(node.getHeight());
            currentY.add(node.getDepth());

        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
     * java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        //        Element element = new Element("insertion");
        //        InsertionNode node = (InsertionNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
     * java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value) {

        //        Element element = new Element("kern");
        //        KernNode node = (KernNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
     * java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value) {

        //        Element element = new Element("ligature");
        //        LigatureNode node = (LigatureNode) value;
        //        Node first = node.getFirst();
        //        Node second = node.getSecond();
        //        if (first != null) {
        //            Element e = getNodeElement(first);
        //            if (e != null) {
        //                element.addContent(e);
        //            }
        //        }
        //        if (second != null) {
        //            Element e = getNodeElement(second);
        //            if (e != null) {
        //                element.addContent(e);
        //            }
        //        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
     * java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value) {

        //        Element element = new Element("mark");
        //        MarkNode node = (MarkNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
     * java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        //        Element element = new Element("penalty");
        //        PenaltyNode node = (PenaltyNode) value;
        //        element.setAttribute("penalty", String.valueOf(node.getPenalty()));
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
     * java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value) {

        //        Element element = new Element("rule");
        //        RuleNode node = (RuleNode) value;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
     * java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value)
            throws DocumentWriterException {

        try {
            writer.writeStartElement("rect");
            setDimenLength("x", currentX);
            Dimen y = new Dimen(currentY);
            y.subtract(new Dimen(2 * Dimen.ONE));
            setDimenLength("y", y);
            setDimenLength("width", node.getWidth());
            writer.writeAttribute("height", "2pt");
            writer.writeAttribute("fill", "green");
            writer.writeEndElement();

            // ------------------------------------------
            currentX.add(node.getWidth());

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
            final Object value) throws DocumentWriterException,
            GeneralException {

        try {
            writer.writeStartElement("rect");
            setDimenLength("x", currentX);
            setDimenLength("y", currentY);
            setDimenLength("width", node.getWidth());
            Dimen rH = new Dimen(node.getHeight());
            rH.add(node.getDepth());
            setDimenLength("height", rH);
            writer.writeAttribute("fill", "none");
            writer.writeAttribute("stroke", "yellow");
            writer.writeAttribute("strike-width", "2pt");
            writer.writeEndElement();

            // ------------------------------------------
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

        } catch (IOException e) {
            throw new DocumentWriterIOException(e.getMessage());
        }
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

        //        Element element = new Element("whatsit");
        //        WhatsItNode node = (WhatsItNode) value;
        return null;
    }
}
