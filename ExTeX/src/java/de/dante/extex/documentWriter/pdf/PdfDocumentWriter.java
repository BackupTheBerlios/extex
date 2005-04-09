/*
 * Copyright (C) 2004-2005 The ExTeX Group
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

package de.dante.extex.documentWriter.pdf;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.ClosedChannelException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

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
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @version $Revision: 1.20 $
 */
public class PdfDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            NodeVisitor {

    /**
     * width A4 in bp
     */
    private static final int WIDTH_A4_BP = 595;

    /**
     * height A$ in bp
     */
    private static final int HEIGHT_A4_BP = 842;

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * documentwriter options
     */
    private DocumentWriterOptions docoptions;

    /**
     * the pdf-dokument
     */
    private Document document;

    /**
     * the pdf writer
     */
    private PdfWriter writer;

    /**
     * the pdf content
     */
    private PdfContentByte cb;

    /**
     * Creates a new object.
     * @param cfg       the configuration
     * @param options   the options
     */
    public PdfDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions options) {

        super();
        docoptions = options;

        //        if (cfg != null) {
        //            String tmp = cfg.getAttribute("encoding");
        //            if (tmp != null && !tmp.equals("")) {
        //                encoding = tmp;
        //            }
        //        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws IOException {

        if (out != null) {
            if (document != null) {
                document.close();
            }
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

        return "pdf";
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
    private Dimen paperwidth = new Dimen();

    /**
     * paperheight
     */
    private Dimen paperheight = new Dimen();

    /**
     * current x position
     */
    private Dimen currentX = new Dimen();

    /**
     * current y postition
     */
    private Dimen currentY = new Dimen();

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException,
            GeneralException {

        try {

            if (writer == null) {
                // create a pdf document
                document = new Document();
                writer = PdfWriter.getInstance(document, out);
                document.open();
            } else {
                document.newPage();
                shippedPages++;
            }

            // TeX primitives should set the papersize in any way:
            // o \paperwidth   / \paperheight,
            // o \pdfpagewidth / \pdfpageheight <-- pdfTeX
            // o \mediawidth   / \mediaheight   <-- VTeX
            Unit.setDimenFromCM(paperwidth, WIDTH_A4_BP);
            Unit.setDimenFromCM(paperheight, HEIGHT_A4_BP);
            if (docoptions != null) {
                Dimen w = (Dimen) docoptions.getDimenOption("paperwidth");
                Dimen h = (Dimen) docoptions.getDimenOption("paperheight");
                if (!(h.getValue() == 0 || w.getValue() == 0)) {
                    paperheight.set(h);
                    paperwidth.set(w);
                }
            }

            // set page size and margin
            Rectangle pagesize = createRectangle(paperwidth, paperheight);
            document.setPageSize(pagesize);
            document.setMargins(0, 0, 0, 0);

            // set start point
            currentX.set(Dimen.ONE_INCH);
            currentY.set(Dimen.ONE_INCH);

            // Changes the default coordinate system so that the origin
            // is in the upper left corner instead of the lower left corner.
            cb = writer.getDirectContent();
            cb.concatCTM(1f, 0f, 0f, -1f, 0f, pagesize.height());

            // -------------------------------------
            cb.setColorStroke(Color.RED);
            cb.moveTo(0, 0);
            cb.lineTo(0, pagesize.height());
            cb.stroke();
            cb.setColorStroke(Color.GREEN);
            cb.moveTo(0, 0);
            cb.lineTo(pagesize.width(), 0);
            cb.stroke();
            cb.setColorStroke(Color.BLUE);
            cb.moveTo(pagesize.width(), 0);
            cb.lineTo(pagesize.width(), pagesize.height());
            cb.stroke();
            cb.setColorStroke(Color.YELLOW);
            cb.moveTo(0, pagesize.height());
            cb.lineTo(pagesize.width(), pagesize.height());
            cb.stroke();

            //            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
            //                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            //            cb.beginText();
            //            cb.setColorFill(Color.CYAN);
            //            cb.setFontAndSize(bf, 12);
            //            cb
            //                    .showTextAligned(PdfContentByte.ALIGN_LEFT, "\u003A", 100,
            //                            100, 0);
            //            cb.endText();

            // -----------------------------

            nodes.visit(this, nodes);

        } catch (DocumentException e) {
            // TODO delete after test
            e.printStackTrace();
            throw new GeneralException(e.getMessage());
        }
    }

    private void drawNode(final Node node) {

        cb.setLineWidth(0.1f);
        if (node instanceof VerticalListNode) {
            cb.setColorStroke(Color.RED);
        } else if (node instanceof HorizontalListNode) {
            cb.setColorStroke(Color.YELLOW);
        } else {
            cb.setColorStroke(Color.GREEN);
        }
        float cx = (float) Unit.getDimenAsBP(currentX);
        float cy = (float) Unit.getDimenAsBP(currentY);
        float w = (float) Unit.getDimenAsBP(node.getWidth());
        float h = (float) Unit.getDimenAsBP(node.getHeight());
        float d = (float) Unit.getDimenAsBP(node.getDepth());
        cb.moveTo(cx, cy);
        cb.lineTo(cx + w, cy);
        cb.stroke();
        cb.moveTo(cx, cy);
        cb.lineTo(cx, cy - h);
        cb.stroke();
        cb.moveTo(cx + w, cy);
        cb.lineTo(cx + w, cy - h);
        cb.stroke();
        cb.moveTo(cx + w, cy - h);
        cb.lineTo(cx, cy - h);
        cb.stroke();

    }

    /**
     * Create a new <code>Rectangle</code>.
     * @param w the width as Dimen
     * @param h the height as Dimen
     * @return Returns the new Rectangle
     */
    private Rectangle createRectangle(final Dimen w, final Dimen h) {

        return new Rectangle((float) Unit.getDimenAsBP(w), (float) Unit
                .getDimenAsBP(h));
    }

    //    /**
    //     * return the node element
    //     * @param node      the node
    //     * @return Returns the node-element
    //     */
    //    private Element getNodeElement(final Node node) {
    //
    //        Element element = null;
    //        try {
    //            Object o = node.visit(this, node);
    //            if (o != null) {
    //                if (o instanceof Element) {
    //                    element = (Element) o;
    //                }
    //            }
    //        } catch (GeneralException e) {
    //            e.printStackTrace();
    //        }
    //        return element;
    //    }

    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------
    // ----------------------------------------------

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
     * java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object value2) {

        //        Element element = new Element("adjust");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
     * java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value2) {

        //        Element element = new Element("aftermath");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value2) {

        //        Element element = new Element("alignedleaders");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2) {

        //        Element element = new Element("beforemath");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        //        Element element = new Element("centeredleaders");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
     * java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        try {
            UnicodeChar uc = node.getCharacter();
            Font font = node.getTypesettingContext().getFont();
            //        de.dante.extex.interpreter.context.Color color = node
            //                .getTypesettingContext().getColor();

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb.beginText();
            cb.setColorFill(Color.BLACK);
            cb.setFontAndSize(bf, (float) Unit.getDimenAsPT(font
                    .getActualSize()));
            float cy = (float) (Unit.getDimenAsBP(currentY) - Unit
                    .getDimenAsBP(node.getWidth()));
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, uc.toString(),
                    (float) Unit.getDimenAsBP(currentX), cy, 0);
            cb.endText();

            drawNode(node);

            currentX.add(node.getWidth());
        } catch (DocumentException e) {
            throw new GeneralException(e.getMessage());
        } catch (IOException e) {
            throw new GeneralException(e.getMessage());
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
        //        addNodeAttributes(nod
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

        //        Element element = new Element("expandedleaders");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode,
     * java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object value) {

        currentX.add(node.getWidth());
        currentY.add(node.getHeight());
        currentY.add(node.getDepth());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode,
     * java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws GeneralException {

        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        NodeIterator it = node.iterator();
        while (it.hasNext()) {
            Node newnode = it.next();
            newnode.visit(this, node);
        }
        currentX.set(saveX);
        currentY.set(saveY);

        drawNode(node);

        currentX.add(node.getWidth());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
     * java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        //        Element element = new Element("insertion");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
     * java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value) {

        //        Element element = new Element("kern");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode,
     * java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object value) {

        //        Element element = new Element("ligature");
        //        addNodeAttributes(node, element);
        //        Node first = node.getLeft();
        //        Node second = node.getRight();
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
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode,
     * java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object value) {

        //        Element element = new Element("mark");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
     * java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        //        Element element = new Element("penalty");
        //        addNodeAttributes(node, element);
        //        element.setAttribute("penalty", String.valueOf(node.getPenalty()));
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
     * java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value) {

        //        Element element = new Element("rule");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
     * java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value) {

        //        Element element = new Element("space");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
     * java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        Dimen saveX = new Dimen(currentX);
        Dimen saveY = new Dimen(currentY);

        NodeIterator it = node.iterator();
        while (it.hasNext()) {
            Node newnode = it.next();
            newnode.visit(this, node);
        }
        currentX.set(saveX);
        currentY.set(saveY);

        drawNode(node);

        currentY.add(node.getDepth());
        currentY.add(node.getHeight());
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode,
     * java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode node, final Object value) {

        //        Element element = new Element("whatsit");
        //        addNodeAttributes(node, element);
        //        return element;
        return null;
    }
}