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

package de.dante.extex.backend.documentWriter.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDRectangle;
import org.pdfbox.pdmodel.edit.PDPageContentStream;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterClosedChannelException;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterIOException;
import de.dante.extex.backend.documentWriter.pdf.exception.DocumentWriterPdfException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.Unit;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configuration;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @version $Revision: 1.2 $
 */
public class PdfDocumentWriter implements DocumentWriter, SingleDocumentStream {

    /**
     * width A4 in bp
     */
    public static final int WIDTH_A4_BP = 595;

    /**
     * height A4 in bp
     */
    public static final int HEIGHT_A4_BP = 842;

    /**
     * the output
     */
    private OutputStream out = null;

    /**
     * the number of page which are shipped out
     */
    private int shippedPages = 0;

    /**
     * document writer options
     */
    private DocumentWriterOptions docoptions;

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
     * the PDFDocument
     */
    private PDDocument document = null;

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() throws DocumentWriterException {

        if (out != null) {
            try {
                if (document != null) {
                    System.err.println("\nSAVE\n");
                    document.save(out);
                    document.close();
                }
                out.close();
            } catch (IOException e) {
                new DocumentWriterIOException(e);
            } catch (COSVisitorException e) {
                new DocumentWriterPdfException(e);
            }
            out = null;
        } else {
            throw new DocumentWriterClosedChannelException("closed channel");
        }
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "pdf";
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setOutputStream(java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
    }

    /**
     * map for the parameters.
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
     * paper width
     */
    private Dimen paperwidth = new Dimen();

    /**
     * paper height
     */
    private Dimen paperheight = new Dimen();

    /**
     * paper height in BP
     */
    private float phBP = HEIGHT_A4_BP;

    /**
     * current x position
     */
    private Dimen currentX = new Dimen();

    /**
     * current y position
     */
    private Dimen currentY = new Dimen();

    /**
     * the pdf node visitor
     */
    private NodeVisitor nodeVisitor;

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public int shipout(Page p) throws DocumentWriterException {

        NodeList nodes = p.getNodes();
        try {

            if (document == null) {
                document = new PDDocument();
            }
            PDPage page = new PDPage();
            PDPageContentStream contentStream = new PDPageContentStream(
                    document, page);

            document.addPage(page);
            nodeVisitor = new PdfNodeVisitor(document, contentStream, currentX, currentY);

            shippedPages++;
            //            System.err.print("[" + shippedPages + "]");

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
                    phBP = Unit.getDimenAsBP(paperheight);
                    if (nodeVisitor instanceof PdfNodeVisitor) {
                        ((PdfNodeVisitor) nodeVisitor)
                                .setPaperheight(paperheight);
                    }
                }
            }

            // set page size and margin
            PDRectangle pagesize = new PDRectangle(Unit
                    .getDimenAsBP(paperwidth), Unit.getDimenAsBP(paperheight));
            page.setMediaBox(pagesize);

            // set start point
            currentX.set(Dimen.ONE_INCH);
            currentY.set(Dimen.ONE_INCH);

            // -------------------------------------
            //            cb.setColorStroke(Color.RED);
            //            cb.moveTo(0, phBP);
            //            cb.lineTo(0, 0);
            //            cb.stroke();
            //            cb.setColorStroke(Color.GREEN);
            //            cb.moveTo(0, phBP);
            //            cb.lineTo(pagesize.width(), phBP);
            //            cb.stroke();
            //            cb.setColorStroke(Color.BLUE);
            //            cb.moveTo(pagesize.width(), phBP);
            //            cb.lineTo(pagesize.width(), 0);
            //            cb.stroke();
            //            cb.setColorStroke(Color.YELLOW);
            //            cb.moveTo(0, 0);
            //            cb.lineTo(pagesize.width(), 0);
            //            cb.stroke();
            // --------------------------------------

            nodes.visit(nodeVisitor, nodes);

            contentStream.close();

        } catch (IOException e) {
            throw new DocumentWriterIOException(e);
        } catch (GeneralException e) {
            throw new DocumentWriterException(e);
        }
        return 1;
    }
}