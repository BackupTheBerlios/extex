/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.AdjustNode;
import de.dante.extex.interpreter.type.node.AfterMathNode;
import de.dante.extex.interpreter.type.node.AlignedLeadersNode;
import de.dante.extex.interpreter.type.node.BeforeMathNode;
import de.dante.extex.interpreter.type.node.CenteredLeadersNode;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.DiscretionaryNode;
import de.dante.extex.interpreter.type.node.ExpandedLeadersNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.InsertionNode;
import de.dante.extex.interpreter.type.node.KernNode;
import de.dante.extex.interpreter.type.node.LigatureNode;
import de.dante.extex.interpreter.type.node.MarkNode;
import de.dante.extex.interpreter.type.node.PenaltyNode;
import de.dante.extex.interpreter.type.node.RuleNode;
import de.dante.extex.interpreter.type.node.SpaceNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.interpreter.type.node.WhatsItNode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;

/**
 * This is a xml implementation of a document writer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class XMLDocumentWriter implements DocumentWriter, NodeVisitor {

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
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public XMLDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
        root = new Element("root");
        docoptions = options;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {

        if (out != null) {
            // write to xml-file
            XMLOutputter xmlout = new XMLOutputter();
            xmlout.setEncoding("ISO-8859-1"); // TODO change (config)
            xmlout.setIndent("   ");
            xmlout.setNewlines(true);
            xmlout.setTrimAllWhite(true);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            Document doc = new Document(root);
            xmlout.output(doc, bout);
            bout.close();
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
     * process Node
     * @param nodes     the nodelist
     * @throws IOException ...
     */
    private void processNodes(final NodeList nodes) throws IOException {

        Element oldparent = parent;
        NodeIterator it = nodes.iterator();
        Element element = getNodeElement(nodes);
        parent.addContent(element);
        parent = element;
        while (it.hasNext()) {
            Node n = it.next();
            if (n instanceof NodeList) {
                processNodes((NodeList) n);
            } else {
                parent.addContent(getNodeElement(n));
            }
        }
        parent = oldparent;
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

        Element page = new Element("page");
        page.setAttribute("id", String.valueOf(shippedPages + 1));
        root.addContent(page);

        parent = page;

        processNodes(nodes);
        shippedPages++;
    }

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
     * Add the Attribute for each node (width, height, depth)
     * @param element   the element
     * @param node      the node
     */
    private void addNodeAttributes(final Element element, final Node node) {

        if (node != null) {
            element.setAttribute("width", getDimenAsString(node.getWidth()));
            element.setAttribute("height", getDimenAsString(node.getHeight()));
            element.setAttribute("depth", getDimenAsString(node.getDepth()));
        }
    }

    /**
     * Return the Dimen as String.
     * If the Dimen equals null, then a empty string is returned.
     *
     * @param dimen the Dimen
     * @return Returns the String for the Dimen
     */
    private String getDimenAsString(final Dimen dimen) {

        String s = "";
        if (dimen != null) {
            s = dimen.toString();
        }
        return s;
    }

    /**
     * Return the Glue as String.
     * If the Glue equals null, then a empty string is returned.
     *
     * @param glue  the Glue
     * @return Returns the String for the Glue
     */
    private String getGlueAsString(final Glue glue) {

        String s = "";
        if (glue != null) {
            s = glue.toString();
        }
        return s;
    }

    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitAdjust(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitAdjust(final Object value, final Object value2) {

        Element element = new Element("adjust");
        AdjustNode node = (AdjustNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitAfterMath(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitAfterMath(final Object value, final Object value2) {

        Element element = new Element("aftermath");
        AfterMathNode node = (AfterMathNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitAlignedLeaders(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final Object value, final Object value2) {

        Element element = new Element("alignedleaders");
        AlignedLeadersNode node = (AlignedLeadersNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitBeforeMath(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final Object value, final Object value2) {

        Element element = new Element("beforemath");
        BeforeMathNode node = (BeforeMathNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitCenteredLeaders(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final Object value, final Object value2) {

        Element element = new Element("centeredleaders");
        CenteredLeadersNode node = (CenteredLeadersNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitChar(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitChar(final Object value, final Object value2) {

        Element element = new Element("char");
        CharNode node = (CharNode) value;
        addNodeAttributes(element, node);
        element.setAttribute("font", node.getTypesettingContext().getFont()
                .getFontName());
        element.setAttribute("codepoint", String.valueOf(node.getCharacter()
                .getCodePoint()));
        element.setAttribute("unicode", node.getCharacter().getUnicodeName());
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitDiscretionary(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final Object value, final Object value2) {

        Element element = new Element("discretionary");
        DiscretionaryNode node = (DiscretionaryNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitExpandedLeaders(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final Object value, final Object value2) {

        Element element = new Element("expandedleaders");
        ExpandedLeadersNode node = (ExpandedLeadersNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitGlue(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitGlue(final Object value, final Object value2) {

        Element element = new Element("glue");
        GlueNode node = (GlueNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitHorizontalList(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final Object value, final Object value2) {

        Element element = new Element("horizontallist");
        HorizontalListNode node = (HorizontalListNode) value;
        addNodeAttributes(element, node);
        element.setAttribute("move", getDimenAsString(node.getMove()));
        element.setAttribute("shift", getDimenAsString(node.getShift()));
        element.setAttribute("targetwidth", getDimenAsString(node
                .getTargetWidth()));
        element.setAttribute("targetheight", getDimenAsString(node
                .getTargetHeight()));
        element.setAttribute("targetdepth", getDimenAsString(node
                .getTargetDepth()));
        element.setAttribute("size", String.valueOf(node.size()));
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitInsertion(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitInsertion(final Object value, final Object value2) {

        Element element = new Element("insertion");
        InsertionNode node = (InsertionNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitKern(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitKern(final Object value, final Object value2) {

        Element element = new Element("kern");
        KernNode node = (KernNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitLigature(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitLigature(final Object value, final Object value2) {

        Element element = new Element("ligature");
        LigatureNode node = (LigatureNode) value;
        addNodeAttributes(element, node);
        Node first = node.getFirst();
        Node second = node.getSecond();
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
        //element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitMark(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitMark(final Object value, final Object value2) {

        Element element = new Element("mark");
        MarkNode node = (MarkNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitPenalty(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitPenalty(final Object value, final Object value2) {

        Element element = new Element("penalty");
        PenaltyNode node = (PenaltyNode) value;
        addNodeAttributes(element, node);
        element.setAttribute("penalty" , String.valueOf(node.getPenalty()));
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitRule(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitRule(final Object value, final Object value2) {

        Element element = new Element("rule");
        RuleNode node = (RuleNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitSpace(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitSpace(final Object value, final Object value2) {

        Element element = new Element("space");
        SpaceNode node = (SpaceNode) value;
        addNodeAttributes(element, node);
        element.setAttribute("gluewidth",getGlueAsString(node.getGlueWidth()));
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitVerticalList(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitVerticalList(final Object value, final Object value2) {

        Element element = new Element("verticallist");
        VerticalListNode node = (VerticalListNode) value;
        addNodeAttributes(element, node);
        element.setAttribute("move", getDimenAsString(node.getMove()));
        element.setAttribute("shift", getDimenAsString(node.getShift()));
        element.setAttribute("targetwidth", getDimenAsString(node
                .getTargetWidth()));
        element.setAttribute("targetheight", getDimenAsString(node
                .getTargetHeight()));
        element.setAttribute("targetdepth", getDimenAsString(node
                .getTargetDepth()));
        element.setAttribute("size", String.valueOf(node.size()));
        return element;
    }

    /**
     * @see de.dante.extex.typesetter.NodeVisitor#visitWhatsIt(java.lang.Object,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final Object value, final Object value2) {

        Element element = new Element("whatsit");
        WhatsItNode node = (WhatsItNode) value;
        addNodeAttributes(element, node);
        element.setText(node.toString());
        return element;
    }
}