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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.ClosedChannelException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.ibm.icu.text.DecimalFormat;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.node.AdjustNode;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.AlignedLeadersNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.ExpandedLeadersNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;
import de.dante.util.configuration.Configuration;

/**
 * This is a xml implementation of a document writer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class XMLDocumentWriter
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
     * show visible chars
     */
    private boolean showvisible = true;

    /**
     * xml indent
     */
    private String indent = "   ";

    /**
     * xml newlines
     */
    private boolean newlines = true;

    /**
     * xml trimallwhitespace
     */
    private boolean trimallwhitespace = true;

    /**
     * use sp
     */
    private boolean usesp = true;

    /**
     * use bp
     */
    private boolean usebp = true;

    /**
     * use mm
     */
    private boolean usemm = true;

    /**
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public XMLDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
        root = new Element("root");
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
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {

        if (out != null) {
            // write to xml-file
            XMLOutputter xmlout = new XMLOutputter();
            xmlout.setEncoding(encoding);
            xmlout.setIndent(indent);
            xmlout.setNewlines(newlines);
            xmlout.setTrimAllWhite(trimallwhitespace);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            Document doc = new Document(root);
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

        return "xml";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    //    /**
    //     * process Node
    //     * @param nodes     the nodelist
    //     * @throws IOException ...
    //     */
    //    private void processNodes(final NodeList nodes) throws IOException {
    //
    //        Element oldparent = parent;
    //        NodeIterator it = nodes.iterator();
    //        Element element = getNodeElement(nodes);
    //        parent.addContent(element);
    //        parent = element;
    //        while (it.hasNext()) {
    //            Node n = it.next();
    //            if (n instanceof NodeList) {
    //                State oldstate = state;
    //                Dimen saveX = new Dimen(currentX);
    //                Dimen saveY = new Dimen(currentY);
    //
    //                if (n instanceof VerticalListNode) {
    //                    state = VERTICAL;
    //                } else {
    //                    state = HORIOZONTAL;
    //                }
    //
    //                processNodes((NodeList) n);
    //
    //                state = oldstate;
    //                currentX.set(saveX);
    //                currentY.set(saveY);
    //                if (n instanceof VerticalListNode) {
    //                    // vertical
    //                    currentY.add(n.getDepth());
    //                    currentY.add(n.getHeight());
    //                } else {
    //                    // horizontal
    //                    currentX.add(n.getWidth());
    //                }
    //            } else {
    //                parent.addContent(getNodeElement(n));
    //            }
    //        }
    //        parent = oldparent;
    //    }

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

        // try {
        Element page = new Element("page");
        page.setAttribute("id", String.valueOf(shippedPages + 1));
        root.addContent(page);

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
            setDimenLength(page, "paperwidth", paperwidth);
            setDimenLength(page, "paperheight", paperheight);
        } else {
            // use DIN A4
            paperwidth = Unit.createDimenFromCM(DINA4WIDTH);
            paperheight = Unit.createDimenFromCM(DINA4HEIGHT);
        }

        // set start point
        currentX.set(Dimen.ONE_INCH);
        currentY.set(Dimen.ONE_INCH);

        parent = page;

        Object o = nodes.visit(this, nodes);
        if (o instanceof Element) {
            parent.addContent((Element) o);
        }
        shippedPages++;
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
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
        if (usesp) {
            element.setAttribute(name + "_sp", String.valueOf(d.getValue()));
        }
        if (usebp) {
            element.setAttribute(name + "_bp", String.valueOf(Unit
                    .getDimenAsBP(d)));
        }
        if (usemm) {
            String s = format.format(Unit.getDimenAsMM(d));
            element.setAttribute(name + "_mm", s);
        }
    }

    /**
     * only for test
     */
    private static final DecimalFormat format = new DecimalFormat("#0.00");

    /**
     * return the node element
     * @param node      the node
     * @return Returns the node-element
     */
    private Element getNodeElement(final Node node) {

        Element element = null;
        try {
            Object o = node.visit(this, node);
            if (o != null) {
                if (o instanceof Element) {
                    element = (Element) o;
                }
            }
        } catch (GeneralException e) {
            e.printStackTrace();
        }
        return element;
    }

    /**
     * Add some Attributes to the node-element
     * @param node      the node
     * @param element   the element
     */
    private void addNodeAttributes(final Node node, final Element element) {

        Dimen wd = new Dimen(node.getWidth());
        Dimen ht = new Dimen(node.getHeight());
        Dimen dp = new Dimen(node.getDepth());

        Dimen move = new Dimen();
        Dimen shift = new Dimen();

        // --- nodelist
        //        if (node instanceof AbstractNodeList) {
        //            AbstractNodeList nodelist = (AbstractNodeList) node;
        //            move = new Dimen(nodelist.getMove());
        //            shift = new Dimen(nodelist.getShift());
        //            setDimenLength(element, "move", move);
        //            setDimenLength(element, "shift", shift);
        //            setDimenLength(element, "targetwidth", nodelist.getTargetWidth());
        //            setDimenLength(element, "targetheight", nodelist.getTargetHeight());
        //            setDimenLength(element, "targetdepth", nodelist.getTargetDepth());
        //            element.setAttribute("size", String.valueOf(nodelist.size()));
        //        }

        // TODO shift + move

        // --------------------------------------------------------
        setDimenLength(element, "x", currentX);
        setDimenLength(element, "y", currentY);
        setDimenLength(element, "width", wd);
        setDimenLength(element, "height", ht);
        setDimenLength(element, "depth", dp);

        // ---- debug ----
        if (debug) {
            element.setText(node.toString());
        }
    }

    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(Node,
     * java.lang.Object)
     */
    public Object visitAdjust(final Node value, final Object value2) {

        Element element = new Element("adjust");
        AdjustNode node = (AdjustNode) value;
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(Node,
     * java.lang.Object)
     */
    public Object visitAfterMath(final Node value, final Object value2) {

        Element element = new Element("aftermath");
        AfterMathNode node = (AfterMathNode) value;
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(Node,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final Node value, final Object value2) {

        Element element = new Element("alignedleaders");
        AlignedLeadersNode node = (AlignedLeadersNode) value;
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(Node,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final Node node, final Object value2) {

        Element element = new Element("beforemath");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(Node,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final Node node, final Object value) {

        Element element = new Element("centeredleaders");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(Node,
     * java.lang.Object)
     */
    public Object visitChar(final Node node, final Object value) {

        Element element = new Element("char");
        CharNode cnode = (CharNode) node;
        UnicodeChar uc = cnode.getCharacter();
        addNodeAttributes(node, element);
        element.setAttribute("font", cnode.getTypesettingContext().getFont()
                .getFontName());
        element.setAttribute("codepoint", String.valueOf(uc.getCodePoint()));
        String ucname = uc.getUnicodeName();
        if (ucname != null) {
            element.setAttribute("unicode", ucname);
        }
        if (showvisible) {
            String c = ".";
            if (uc.isPrintable()) {
                c = uc.toString();
            }
            element.setAttribute("visiblechar", c);
        }
        currentX.add(node.getWidth());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(Node,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final Node node, final Object value) {

        Element element = new Element("discretionary");
        DiscretionaryNode dnode = (DiscretionaryNode) node;
        addNodeAttributes(dnode, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(Node,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final Node node, final Object value) {

        Element element = new Element("expandedleaders");
        ExpandedLeadersNode lnode = (ExpandedLeadersNode) node;
        addNodeAttributes(lnode, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(Node,
     * java.lang.Object)
     */
    public Object visitGlue(final Node node, final Object value) {

        Element element = new Element("glue");
        addNodeAttributes(node, element);
        currentX.add(node.getWidth());
        currentY.add(node.getHeight());
        currentY.add(node.getDepth());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(Node,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final Node node, final Object value)
            throws GeneralException {

        Element element = new Element("horizontallist");
        HorizontalListNode lnode = (HorizontalListNode) node;
        addNodeAttributes(node, element);
        //        element.setAttribute("x_depth",String.valueOf(node.getDepth()));
        //        setBaseline(node);

        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        NodeIterator it = lnode.iterator();
        while (it.hasNext()) {
            Node newnode = it.next();
            Object o = newnode.visit(this, node);
            if (o instanceof Element) {
                element.addContent((Element) o);
            }
        }
        currentX.set(saveX);
        currentY.set(saveY);
        currentX.add(node.getWidth());

        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(Node,
     * java.lang.Object)
     */
    public Object visitInsertion(final Node node, final Object value) {

        Element element = new Element("insertion");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(Node,
     * java.lang.Object)
     */
    public Object visitKern(final Node node, final Object value) {

        Element element = new Element("kern");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(Node,
     * java.lang.Object)
     */
    public Object visitLigature(final Node node, final Object value) {

        Element element = new Element("ligature");
        LigatureNode lnode = (LigatureNode) node;
        addNodeAttributes(node, element);
        Node first = lnode.getFirst();
        Node second = lnode.getSecond();
        if (first != null) {
            Element e = getNodeElement(first);
            if (e != null) {
                element.addContent(e);
            }
        }
        if (second != null) {
            Element e = getNodeElement(second);
            if (e != null) {
                element.addContent(e);
            }
        }
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(Node,
     * java.lang.Object)
     */
    public Object visitMark(final Node node, final Object value) {

        Element element = new Element("mark");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(Node,
     * java.lang.Object)
     */
    public Object visitPenalty(final Node node, final Object value) {

        Element element = new Element("penalty");
        PenaltyNode pnode = (PenaltyNode) node;
        addNodeAttributes(node, element);
        element.setAttribute("penalty", String.valueOf(pnode.getPenalty()));
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(Node,
     * java.lang.Object)
     */
    public Object visitRule(final Node node, final Object value) {

        Element element = new Element("rule");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(Node,
     * java.lang.Object)
     */
    public Object visitSpace(final Node node, final Object value) {

        Element element = new Element("space");
        addNodeAttributes(node, element);
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(Node,
     * java.lang.Object)
     */
    public Object visitVerticalList(final Node node, final Object value)
            throws GeneralException {

        Element element = new Element("verticallist");
        VerticalListNode lnode = (VerticalListNode) node;
        addNodeAttributes(node, element);

        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        NodeIterator it = lnode.iterator();
        while (it.hasNext()) {
            Node newnode = it.next();

            Object o = newnode.visit(this, node);

            if (o instanceof Element) {
                element.addContent((Element) o);
            }
        }
        currentX.set(saveX);
        currentY.set(saveY);
        currentY.add(node.getDepth());
        currentY.add(node.getHeight());

        return element;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(Node,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final Node nde, final Object value) {

        Element element = new Element("whatsit");
        WhatsItNode node = (WhatsItNode) nde;
        addNodeAttributes(node, element);
        return element;
    }
}