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

package de.dante.extex.documentWriter.svg;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.ClosedChannelException;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.interpreter.type.dimen.Dimen;
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
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;
import de.dante.util.configuration.Configuration;

/**
 * This is a svg implementation of a document writer.
 *
 * At the moment, only one page!!!!
 *
 * TODO incomplete !!!
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class SVGDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            NodeVisitor {

    /**
     * DIN-A4 width
     */
    private static final double DINA4WIDTH = 21.0d;

    /**
     * DIN-A4 height
     */
    private static final double DINA4HEIGHT = 29.7d;

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * the root Element
     */
    private Element root;

    /**
     * the parent element
     */
    private Element parent;

    /**
     * documentwriter options
     */
    private DocumentWriterOptions docoptions;

    /**
     * encdoing
     */
    private String encoding = "ISO-8859-1";

    /**
     * debug
     */
    private boolean debug = true;

    /**
     * Naemspace for svg
     */
    private static final Namespace SVGNAMESPACE = Namespace
            .getNamespace("http://www.w3.org/2000/svg");

    /**
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public SVGDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
        root = new Element("svg", SVGNAMESPACE);
        root.setAttribute("version", "1.1");
        docoptions = options;

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
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {

        if (out != null) {
            // write to xml-file
            XMLOutputter xmlout = new XMLOutputter();
            xmlout.setEncoding(encoding);
            if (debug) {
                xmlout.setIndent("   ");
                xmlout.setNewlines(true);
                xmlout.setTrimAllWhite(true);
            } else {
                xmlout.setIndent("");
                xmlout.setNewlines(false);
                xmlout.setTrimAllWhite(false);
            }
            BufferedOutputStream bout = new BufferedOutputStream(out);
            Document doc = new Document();
            doc.setDocType(new DocType("svg", "//W3C//DTD SVG 1.1//EN",
                    "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd"));
            doc.setRootElement(root);
            xmlout.output(doc, bout);
            bout.close();
            out.close();
            out = null;
        } else {
            throw new ClosedChannelException();
        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "svg";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
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
     * paperwidth
     */
    private Dimen paperwidth;

    /**
     * paperheight
     */
    private Dimen paperheight;

    /**
     * current x position
     */
    private Dimen currentX = new Dimen();

    /**
     * current y postition
     */
    private Dimen currentY = new Dimen();

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException,
            GeneralException {

        if (shippedPages == 0) {
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
            } else {
                // use DIN A4
                paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
                paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
            }
            //            setDimenLength(root, "width", paperwidth);
            //            setDimenLength(root, "height", paperheight);
            root.setAttribute("width", "20cm");
            root.setAttribute("height", "6cm");

            // set start point
            currentX.set(Dimen.ONE_INCH);
            currentY.set(Dimen.ONE_INCH);

            parent = root;

            Object o = nodes.visit(this, nodes);
            if (o instanceof Element) {
                parent.addContent((Element) o);
            }
            shippedPages++;
        }
    }

    /**
     * Set the Attribute for an element with sp, bp, mm
     * @param element   the element
     * @param name      the attribute-name
     * @param dimen     the dimen
     */
    private void setDimenLength(final Element element, final String name,
            final Dimen dimen) {

        Dimen d = dimen;
        if (dimen == null) {
            d = new Dimen();
        }
        element.setAttribute(name, String.valueOf(Unit.getDimenAsMM(d)) + "mm");
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
    public Object visitChar(final CharNode node, final Object value) {

        UnicodeChar uc = node.getCharacter();
        Font font = node.getTypesettingContext().getFont();

        // ------- text --------------
        Element text = new Element("text", SVGNAMESPACE);
        setDimenLength(text, "x", currentX);
        Dimen y = new Dimen(currentY);
        setDimenLength(text, "y", y);
        text.setAttribute("font-family", font.getFontName() + ",serif");
        text.setAttribute("font-size", String.valueOf(Unit.getDimenAsPT(font
                .getEm())));
        text.setText(uc.toString());
        parent.addContent(text);

        // ---------------------------------
        Element rect = new Element("rect", SVGNAMESPACE);
        setDimenLength(rect, "x", currentX);
        y.subtract(node.getHeight());
        setDimenLength(rect, "y", y);
        setDimenLength(rect, "width", node.getWidth());
        Dimen h = new Dimen(node.getHeight());
        h.add(node.getDepth());
        setDimenLength(rect, "height", h);
        rect.setAttribute("fill", "none");
        rect.setAttribute("stroke", "blue");
        rect.setAttribute("strike-width", "1pt");

        parent.addContent(rect);

        // -----------------------------------------------

        currentX.add(node.getWidth());
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
            final Object value) throws GeneralException {

        Element rect = new Element("rect", SVGNAMESPACE);

        setDimenLength(rect, "x", currentX);
        setDimenLength(rect, "y", currentY);
        setDimenLength(rect, "width", node.getWidth());
        Dimen rH = new Dimen(node.getHeight());
        rH.add(node.getDepth());
        setDimenLength(rect, "height", rH);
        rect.setAttribute("fill", "none");
        rect.setAttribute("stroke", "red");
        rect.setAttribute("strike-width", "1pt");

        parent.addContent(rect);

        // ------------------------------------------
        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        // set x to baseline
        currentY.add(node.getHeight());

        // baseline
        if (node.getDepth().getValue() != 0) {

            Element line = new Element("line", SVGNAMESPACE);
            setDimenLength(line, "x1", currentX);
            setDimenLength(line, "y1", currentY);
            Dimen x2 = new Dimen(currentX);
            x2.add(node.getWidth());
            setDimenLength(line, "x2", x2);
            setDimenLength(line, "y2", currentY);
            line.setAttribute("stroke", "red");
            line.setAttribute("strike-width", "1pt");

            parent.addContent(line);
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
    public Object visitSpace(final SpaceNode node, final Object value) {

        Element rect = new Element("rect", SVGNAMESPACE);

        setDimenLength(rect, "x", currentX);
        Dimen y = new Dimen(currentY);
        y.subtract(new Dimen(2 * Dimen.ONE));
        setDimenLength(rect, "y", y);
        setDimenLength(rect, "width", node.getWidth());
        rect.setAttribute("height", "2pt");
        rect.setAttribute("fill", "green");

        parent.addContent(rect);

        // ------------------------------------------
        currentX.add(node.getWidth());

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
     * java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        Element rect = new Element("rect", SVGNAMESPACE);

        setDimenLength(rect, "x", currentX);
        setDimenLength(rect, "y", currentY);
        setDimenLength(rect, "width", node.getWidth());
        Dimen rH = new Dimen(node.getHeight());
        rH.add(node.getDepth());
        setDimenLength(rect, "height", rH);
        rect.setAttribute("fill", "none");
        rect.setAttribute("stroke", "yellow");
        rect.setAttribute("strike-width", "2pt");

        parent.addContent(rect);

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