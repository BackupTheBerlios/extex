/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.edit.PDPageContentStream;
import org.pdfbox.pdmodel.font.PDFont;
import org.pdfbox.pdmodel.font.PDType1Font;
import org.pdfbox.pdmodel.graphics.path.BasePath;

import de.dante.extex.color.ColorVisitor;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.documentWriter.exception.DocumentWriterIOException;
import de.dante.extex.documentWriter.pdf.pdfbox.PdfBoxType1Font;
import de.dante.extex.font.FountKey;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeIterator;
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
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.Unit;

/**
 * PDF NodeVisitor.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */

public class PdfNodeVisitor implements NodeVisitor {

    /**
     * current x position
     */
    private Dimen currentX;

    /**
     * current y position
     */
    private Dimen currentY;

    /**
     * paper height in BP
     */
    private float phBP = PdfDocumentWriter.HEIGHT_A4_BP;

    /**
     * pdf content stream
     */
    private PDPageContentStream contentStream;

    /**
     * the pdf color visitor
     */
    private ColorVisitor colorVisitor;

    /**
     * the pdf document
     */
    private PDDocument document;

    /**
     * Create a new object.
     *
     * @param doc    the pdf document
     * @param cs     the pdf content stream
     * @param cx     the current x
     * @param cy     the current y
     */
    public PdfNodeVisitor(final PDDocument doc, final PDPageContentStream cs,
            final Dimen cx, final Dimen cy) {

        document = doc;
        contentStream = cs;
        currentX = cx;
        currentY = cy;
        colorVisitor = new PdfColorVisitor();
    }

    /**
     * Draw a box around the node (only for test).
     *
     * @param node  the node
     * @throws DocumentWriterException if an error occurred.
     */
    private void drawNode(final Node node) throws DocumentWriterException {

        try {
            if (node instanceof VerticalListNode) {
                contentStream.setStrokingColor(Color.RED);
            } else if (node instanceof HorizontalListNode) {
                contentStream.setStrokingColor(Color.YELLOW);
            } else {
                contentStream.setStrokingColor(Color.GREEN);
            }
            float cx = Unit.getDimenAsBP(currentX);
            float cy = Unit.getDimenAsBP(currentY);
            float w = Unit.getDimenAsBP(node.getWidth());
            float h = Unit.getDimenAsBP(node.getHeight());
            float d = Unit.getDimenAsBP(node.getDepth());
            BasePath path = new BasePath();
            path.setLineWidth(0.1f);
            path.moveTo(cx, phBP - cy);
            path.lineTo(cx + w, phBP - cy);
            path.stroke();
            path.moveTo(cx, phBP - cy);
            path.lineTo(cx, phBP - cy + h);
            path.stroke();
            path.moveTo(cx + w, phBP - cy);
            path.lineTo(cx + w, phBP - cy + h);
            path.stroke();
            path.moveTo(cx + w, phBP - cy + h);
            path.lineTo(cx, phBP - cy + h);
            path.stroke();
            if (node.getDepth().getValue() != 0) {
                path.moveTo(cx, phBP - cy);
                path.lineTo(cx, phBP - cy - d);
                path.stroke();
                path.moveTo(cx, phBP - cy - d);
                path.lineTo(cx + w, phBP - cy - d);
                path.stroke();
                path.moveTo(cx + w, phBP - cy - d);
                path.lineTo(cx + w, phBP - cy);
                path.stroke();
            }
            contentStream.drawPath(path);
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        }
    }

    /**
     * Set the paper height.
     * @param ph The paper height to set.
     */
    public void setPaperheight(final Dimen ph) {

        phBP = Unit.getDimenAsBP(ph);
    }

    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    // -----------------------------------------
    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode,
     * java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode,
     * java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode,
     * java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode,
     * java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value2) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode,
     * java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        return null;
    }

    /**
     * the color from the character before
     */
    private de.dante.extex.interpreter.context.Color oldcolor = null;

    /**
     * the fount key from the character before
     */
    private FountKey oldfountkey = null;

    /**
     * the pdf font
     */
    private PDFont pdfont = PDType1Font.HELVETICA;

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode,
     * java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        try {
            UnicodeChar uc = node.getCharacter();
            Font newfont = node.getTypesettingContext().getFont();
            FountKey newfountkey = newfont.getFontKey();
            de.dante.extex.interpreter.context.Color newcolor = node
                    .getTypesettingContext().getColor();

            if (!newfountkey.eq(oldfountkey)) {
                pdfont = PdfBoxType1Font.getInstance(document, newfont);
                oldfountkey = newfountkey;
            }

            // the same color?
            if (!newcolor.equals(oldcolor)) {
                newcolor.visit(colorVisitor, contentStream);
                oldcolor = newcolor;
            }
            contentStream.beginText();
            contentStream.setFont(pdfont, (float) Unit.getDimenAsPT(newfont
                    .getActualSize()));
            contentStream.moveTextPositionByAmount(Unit.getDimenAsBP(currentX),
                    phBP - Unit.getDimenAsBP(currentY));
            contentStream.drawString(uc.toString());
            contentStream.endText();

            drawNode(node);

            currentX.add(node.getWidth());
            //        } catch (DocumentException e) {
            //            throw new DocumentWriterException(e);
        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
            //        } catch (FontException e) {
            //            throw new DocumentWriterFontException(e);
            //        } catch (PdfException e) {
            //            throw new DocumentWriterPdfDocumentException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode,
     * java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode,
     * java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

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

        //        try {
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
        //        } catch (PdfException e) {
        //            throw new DocumentWriterPdfDocumentException(e);
        //        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode,
     * java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode,
     * java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object value) {

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

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode,
     * java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode,
     * java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode,
     * java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object value) {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode,
     * java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        //        try {
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
        //        } catch (PdfException e) {
        //            throw new DocumentWriterPdfDocumentException(e);
        //        }
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
     *      de.dante.extex.typesetter.type.node.VirtualCharNode,
     *      java.lang.Object)
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
    public Object visitWhatsIt(final WhatsItNode node, final Object value) {

        return null;
    }
}
