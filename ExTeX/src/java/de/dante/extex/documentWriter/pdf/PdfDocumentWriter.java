/*
 * Copyright (C) 2004 The ExTeX Group
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

package de.dante.extex.documentWriter.pdf;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.fop.apps.FOPException;
import org.apache.fop.layout.FontInfo;
import org.apache.fop.layout.FontState;
import org.apache.fop.layout.Page;
import org.apache.fop.pdf.PDFAnnotList;
import org.apache.fop.pdf.PDFColor;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFResources;
import org.apache.fop.pdf.PDFStream;
import org.apache.fop.render.pdf.FontSetup;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.configuration.Configuration;
import de.dante.util.GeneralException;
import de.dante.util.Unit;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 * @see org.apache.fop.render.pdf.PDFRenderer
 * @see org.apache.fop.svg.PDFGraphics2D
 */
public class PdfDocumentWriter implements DocumentWriter, NodeVisitor {

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;
    
    /**
     * The field <tt>cfg</tt> ...
     */    
    private Configuration cfg = null;

    /**
     * Creates a new object.
     * @param    cfg the configuration
     */
    public PdfDocumentWriter(final Configuration cfg) {
        super(); this.cfg = cfg;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {
        return shippedPages;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {
        return "pdf";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(java.io.Writer)
     */
    public void setOutputStream(final OutputStream outStream) {
        this.out = outStream;
    }

    /**
     * shipout
     * @param   nodes   the nodelist
     * @throws IOException ...
     * @throws GeneralException ...
     */
    public void shipout(final NodeList nodes) throws IOException, GeneralException {
        newPage();
        shippedPages++;
        markOrigin();
        nodes.visit(this, nodes, null);
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {
        FontSetup.addToResources(this.pdfDoc, fontInfo); // ??? //
        pdfDoc.outputTrailer(this.out);
    }

    /**
     * pdfdoc
     */
    private PDFDocument pdfDoc = null;

    /**
     * pdfstream
     */
    private PDFStream cs = null;

    /**
     * pdfresource
     */
    private PDFResources pdfResources = null;

    /**
     * pdfanolist
     */
    private PDFAnnotList currentAnnotList = null;

    /**
     * currentpage
     */
    private PDFPage currentPage = null;

    /**
     * color
     */
    private PDFColor currentColor = null;

    /**
     * page
     */
    private Page page = null;

    /**
     * operators
     */
    private PDFOperators op = null;

    /**
     * pageWD
     */
    private int pageWD = 595; // "bp"

    /**
     * pageHT
     */
    private int pageHT = 842; // "bp"  -- A4

    // TeX primitives should set the papersize in any way:
    // o \paperwidth   / \paperheight, 
    // o \pdfpagewidth / \pdfpageheight <-- pdfTeX
    // o \mediawidth   / \mediaheight   <-- VTeX
    /**
     * fontinfo
     */
    private FontInfo fontInfo = null;

    /**
     * fontstate
     */
    private FontState fontState = null;

    /**
     * x,y ...
     */
    private Dimen lastX = new Dimen(), lastY = new Dimen(), 
      currentX = new Dimen(), currentY = new Dimen(), lastDP = new Dimen();

    /**
     * onlyStroke
     */
    private boolean onlyStroke;

    /**
     * adjust
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitAdjust(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * aftermath
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitAfterMath(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * alignedleader
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitAlignedLeaders(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * beforemath
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitBeforeMath(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * centerleaders
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitCenteredLeaders(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * discretionary
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitDiscretionary(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * expandleaders
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitExpandedLeaders(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * glue
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitGlue(final Object value, final Object value2) {
        Node node = (Node) value;
        StringBuffer operators = new StringBuffer(256);
        showNode(node, operators);
        debugNode(node);
        if (state == HORIOZONTAL) {
            System.out.println("HOR");
        } else {
            System.out.println("VER");
        }
        setPosition(node);
        return null;
    }

    /**
     * insertion
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitInsertion(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * kern
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitKern(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * ligature
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitLigature(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * mark
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitMark(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * penalty
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitPenalty(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * rule
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitRule(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * space
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitSpace(final Object value, final Object value2) {
        Node node = (Node) value;
        StringBuffer operators = new StringBuffer(256);
        operators.append(op.fillColor(Color.YELLOW));
        showNode(node, operators);
        setPosition(node);
        return null;
    }

    /**
     * whatsit
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitWhatsIt(final Object value, final Object value2) {
        Node node = (Node) value;
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * verticallist
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitVerticalList(final Object value, final Object value2) {
        NodeList nodes = (NodeList) value;
        StringBuffer operators = new StringBuffer(256);

        State oldstate = state; state = VERTICAL;
	
	Dimen ht = new Dimen(nodes.getHeight());
	Dimen saveX = new Dimen(lastX); Dimen saveY = new Dimen(lastY);
	
	currentX.set(lastX); lastY.add(ht);
	currentY.set(lastY);

        operators.append(op.fillColor(Color.LIGHT_GRAY));
	
        showNode(nodes, operators); debugNode(nodes);

        lastX.set(saveX); lastY.set(saveY); lastY.subtract(ht); lastDP.set(ht);
	
        NodeIterator it = nodes.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            try {
                node.visit(this, node, null);
            } catch (Exception e) {
                e.printStackTrace(); // TODO: handle exception
            }
        }

        state = oldstate;
        return null;
    }

    /**
     * horizontallist
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitHorizontalList(final Object value, final Object value2) {

        NodeList nodes = (HorizontalListNode) value;

        State oldstate = state; state = HORIOZONTAL;
	
	Dimen ht = new Dimen(nodes.getHeight());
	Dimen dp = new Dimen(nodes.getDepth());
	
        currentX.set(lastX);
	currentY.set(lastY); currentY.add(lastDP); currentY.add(ht);
        lastDP.set(dp);

        debugNode(nodes);

        NodeIterator it = nodes.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            try {
                node.visit(this, node, null);
            } catch (Exception e) {
                e.printStackTrace(); // TODO: handle exception
            }

        }
        setPosition(nodes);
        lastY = currentY;
        state = oldstate;
        return null;
    }

    /**
     * char
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitChar(final Object value, final Object value2) {
        Node node = (Node) value;
        StringBuffer operators = new StringBuffer(256);
        operators.append(op.fillColor(Color.GREEN));
        showNode(node, operators);
        debugNode(node);
        setPosition(node);
        return null;
    }

    // -------------------------------------------------

    /**
     * NodeVisitor for debug.
     */
    private class DebugVisitor implements NodeVisitor {

        public Object visitAdjust(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Adjust");
            sb.append(metric(node));
            return null;
        }

        public Object visitAfterMath(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("AfterMath");
            sb.append(metric(node));
            return null;
        }

        public Object visitAlignedLeaders(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("AlignedLeaders");
            sb.append(metric(node));
            return null;
        }

        public Object visitBeforeMath(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("BeforeMath");
            sb.append(metric(node));
            return null;
        }

        public Object visitCenteredLeaders(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("CenterLeaders");
            sb.append(metric(node));
            return null;
        }

        public Object visitChar(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Char");
            sb.append(metric(node));
            sb.append("\t" + node.toString());
            return null;
        }

        public Object visitDiscretionary(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Discretionary");
            sb.append(metric(node));
            return null;
        }

        public Object visitExpandedLeaders(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("ExpandedLeaders");
            sb.append(metric(node));
            return null;
        }

        public Object visitGlue(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            GlueNode node = (GlueNode) value2;
            sb.append("Glue");
            sb.append(metric(node));
            return null;
        }

        public Object visitHorizontalList(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("HorizontalList");
            sb.append(metric(node));
            return null;
        }

        public Object visitInsertion(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Insertion");
            sb.append(metric(node));
            return null;
        }

        public Object visitKern(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Kern");
            sb.append(metric(node));
            return null;
        }

        public Object visitLigature(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Ligature");
            sb.append(metric(node));
            return null;
        }

        public Object visitMark(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Mark");
            sb.append(metric(node));
            return null;
        }

        public Object visitPenalty(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Penalty");
            sb.append(metric(node));
            return null;
        }

        public Object visitRule(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Rule");
            sb.append(metric(node));
            return null;
        }

        public Object visitSpace(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Space");
            sb.append(metric(node));
            return null;
        }

        public Object visitVerticalList(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("VerticalList");
            sb.append(metric(node));
            return null;
        }

        public Object visitWhatsIt(Object value, Object value2) {
            StringBuffer sb = (StringBuffer) value;
            Node node = (Node) value2;
            sb.append("Whatsit");
            sb.append(metric(node));
            return null;
        }

        private String metric(final Node node) {
            return " (wd=" + node.getWidth().toPT() + "\tht=" + node.getHeight().toPT() + "\tdp=" + node.getDepth().toPT() + ")" + "\t";
        }

    }

    /**
     * debug
     */
    private void debugNode(Node node) {
        StringBuffer sb = new StringBuffer(256);
        try {
            node.visit(new DebugVisitor(), sb, node);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
    }

    // ------------------------------------------------------

    private void showNode(Node node, StringBuffer operators) {
	
	Dimen wd = new Dimen(node.getWidth());
	Dimen ht = new Dimen(node.getHeight());
	Dimen dp = new Dimen(node.getDepth());

        onlyStroke = false;

        cs.add(op.gSave());
        cs.add(operators.toString());
        cs.add(op.lineWidth(.3f));
	
	float rX = (float)Unit.getDimenAsBP(currentX);
	float rY = (float)Unit.getDimenAsBP(currentY) -
	           (float)Unit.getDimenAsBP(ht);
        float rWD  = (float)Unit.getDimenAsBP(wd);
	float rHT  = (float)Unit.getDimenAsBP(ht) +
	             (float)Unit.getDimenAsBP(dp);

	cs.add(op.addRectangle(rX, rY, rWD, rHT));
	
        if (onlyStroke)
            cs.add(op.stroke());
        else
            cs.add(op.fillStroke());
	    
	if (!dp.le(Dimen.ZERO_PT)) { // baseline
	    rY = (float)Unit.getDimenAsBP(currentY);
            cs.add(op.gSave());
            cs.add(op.setLineDash(.3f, .3f));
            cs.add(op.moveTo(rX, rY));
            cs.add(op.rLineTo(rWD, 0f));
            cs.add(op.stroke());
            cs.add(op.gRestore());
        }
        cs.add(op.gRestore());
    }

    private void setPosition(Node node) {
        if (state == HORIOZONTAL) {
	    currentX.add(node.getWidth());
        } else {
	    currentY.add(node.getHeight()); currentY.add(node.getDepth());
        }
    }

    private void markOrigin() {
        cs.add(op.gSave());
        cs.add(op.lineWidth(.6f));
        cs.add(op.strokeColor(Color.RED));
        cs.add(op.circle(72, 72, 5));
        cs.add(op.moveTo(72 - 5, 72));
        cs.add(op.rLineTo(5, 0));
        cs.add(op.moveTo(72, 72 - 5));
        cs.add(op.rLineTo(0, 5));
        cs.add(op.stroke());
        cs.add(op.gRestore());
    }

    /**
     * Opens/setups the document
     */
    private void initDocument() throws IOException {

        pdfDoc = new PDFDocument();

        pdfDoc.setProducer("ExTeX-0.00"); // Where is this defined?

        /*

         How can we set the following?

         /Author, /Title, /Creator, /Keywords, /CreationDate 

         How can we switch off the compression?

        */

        fontInfo = new FontInfo();

        try {
            FontSetup.setup(fontInfo);
            fontState = new FontState(fontInfo, "Helvetica", "normal", "normal", 12, 0);
        } catch (FOPException e) {
            e.printStackTrace();
        }

        pdfResources = pdfDoc.getResources();

        pdfDoc.outputHeader(this.out);

        op = new PDFOperators();

    }

    /**
     * Creates and setups a new page
     */
    private void newPage() throws IOException {

        if (pdfDoc == null)
            initDocument();

        pdfDoc.output(this.out);

        cs = pdfDoc.makeStream();

        currentPage = pdfDoc.makePage(pdfResources, cs, pageWD, pageHT, page);

        // TeX/SVG-Koordinatensystem.
        cs.add(op.concat(1, 0, 0, -1, 0, pageHT));
	
	lastX.set(Dimen.ONE_INCH); lastY.set(Dimen.ONE_INCH); 
	lastDP.setValue(0L);
    }

    // ---------------------------------------------------------------------------
    /**
     * State
     */
    private static class State {

        public State() {
            super();
        }
    }

    /**
     * in vertical mode
     */
    private final static State VERTICAL = new State();

    /**
     * in horizontal mode
     */
    private final static State HORIOZONTAL = new State();

    /**
     * the current mode
     */
    private State state = VERTICAL;
    // ---------------------------------------------------------------------------

}
