/*
 * Copyright (C) 2005 The ExTeX Group
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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.MultipleDocumentStream;
import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.Unit;
import de.dante.util.configuration.Configuration;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class PdfSinglePageDocumentWriter
        implements
            DocumentWriter,
            MultipleDocumentStream {

    /**
     * width A4 in bp
     */
    private static final int WIDTH_A4_BP = 595;

    /**
     * height A$ in bp
     */
    private static final int HEIGHT_A4_BP = 842;

    /**
     * the output factroy
     */
    private OutputStreamFactory outFactory = null;

    /**
     * the number of page which are shipped out
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
    public PdfSinglePageDocumentWriter(final Configuration cfg,
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

        // do nothing
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
     * @see de.dante.extex.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory writerFactory) {

        outFactory = writerFactory;
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
     * the pdf nodevisitor
     */
    private NodeVisitor visitor;

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException,
            GeneralException {

        try {

            // get the output from the factory
            OutputStream out = outFactory.getOutputStream();

            // create a pdf document
            document = new Document();
            writer = PdfWriter.getInstance(document, out);
            document.open();
            cb = writer.getDirectContent();
            visitor = new PdfNodeVisitor(cb, currentX, currentY);

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

            nodes.visit(visitor, nodes);

            // close the page output
            out.close();
            shippedPages++;

        } catch (DocumentException e) {
            // TODO delete after test
            e.printStackTrace();
            throw new GeneralException(e.getMessage());
        }
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

}