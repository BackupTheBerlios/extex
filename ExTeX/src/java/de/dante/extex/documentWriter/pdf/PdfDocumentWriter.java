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
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.FontDescriptor;
import org.apache.fop.fonts.FontReader;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFFilterList;
import org.apache.fop.pdf.PDFFont;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFResources;
import org.apache.fop.pdf.PDFStream;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.SingleDocumentStream;
import de.dante.extex.font.FontFile;
import de.dante.extex.font.Glyph;
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
import de.dante.util.Unit;
import de.dante.util.configuration.Configuration;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.19 $
 * @see org.apache.fop.render.pdf.PDFRenderer
 * @see org.apache.fop.svg.PDFGraphics2D
 */
public class PdfDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            NodeVisitor {

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        // TODO Auto-generated method stub

    }

    // -------------------------------------------------

    /**
     * NodeVisitor for debug.
     */
    private class DebugVisitor implements NodeVisitor {

        /**
         * TODO: missing JavaDoc
         *
         * @param node ...
         * @return ...
         */
        private String metric(final Node node) {

            return " (wd=" + node.getWidth().toString() + "  ht="
                    + node.getHeight().toString() + "  dp="
                    + node.getDepth().toString() + ")";
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(AdjustNode, java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Adjust");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(AfterMathNode, java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("AfterMath");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(AlignedLeadersNode, java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("AlignedLeaders");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(BeforeMathNode, java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("BeforeMath");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(CenteredLeadersNode, java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("CenterLeaders");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(CharNode, java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            CharNode cnode = (CharNode) node;
            Font font = null;
            FontFile file = null;
            Glyph glyph = null;

            font = cnode.getTypesettingContext().getFont();

            if (font != null) {
                glyph = font.getGlyph(cnode.getCharacter());
            }

            sb.append(" " + cnode.getCharacter() + " [Nb="
                    + ((glyph != null) ? glyph.getNumber() : "??") + "] ");
            sb.append("Char");
            sb.append(metric(node));

            if (glyph != null) {
                file = glyph.getExternalFile();
            }

            sb.append(" filename="
                    + ((file != null) ? "" + file.getFile() : "??"));

            /*
             System.out.println("-----------------------------------------");
             System.out.println("Glyph : " + node.getCharacter() + " : "
             + glyph.getName() + " " + glyph.getNumber() + "  aus "
             + glyph.getExternalFile());
             System.out.println("-----------------------------------------");
             */

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(DiscretionaryNode, java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Discretionary");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(ExpandedLeadersNode, java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("ExpandedLeaders");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(GlueNode, java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Glue");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(HorizontalListNode, java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("HorizontalList");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(InsertionNode, java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Insertion");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(KernNode, java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Kern");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(LigatureNode, java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Ligature");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(MarkNode, java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Mark");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(PenaltyNode, java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Penalty");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(RuleNode, java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Rule");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(SpaceNode, java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Space");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(VerticalListNode, java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode node,
                final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("VerticalList");
            sb.append(metric(node));
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(WhatsItNode, java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object value) {

            StringBuffer sb = (StringBuffer) value;
            sb.append("Whatsit");
            sb.append(metric(node));
            return null;
        }

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
     * in horizontal mode
     */
    private static final State HORIOZONTAL = new State();

    /**
     * The field <tt>uniqueCounter</tt> contains the ...
     */
    private static int uniqueCounter = 1;

    /**
     * in vertical mode
     */
    private static final State VERTICAL = new State();

    // TeX primitives should set the papersize in any way:
    // o \paperwidth   / \paperheight,
    // o \pdfpagewidth / \pdfpageheight <-- pdfTeX
    // o \mediawidth   / \mediaheight   <-- VTeX
    private static final int WIDTH_A4 = 595; // "bp"

    private static final int HEIGHT_A4 = 842; // "bp"

    private static final int LAST_BASE_FONT = 14;

    private static final int BUFFER_SIZE = 256;

    private static final int ONE_INCH_IN_BP = 72;

    private static final int MARKER_RADIUS = 5;

    private static final float THIN_LINE = 0.3f;

    private static final float THICK_LINE = 0.6f;

    private static final float DASH_LEN = 0.3f;

    private static final float DASH_DISTANCE = 0.3f;

    private static final float DUMMY_HEIGHT = -1.5f;

    private static final int UNIQUE_MASK = 0xffff;

    private static final int NOT_FOUND = -1;

    /**
     * The internal font number (e.g. /F7) for the basefont "Times-Roman".
     * The value must be in the range from 1 to 14.
     */
    private static final int TIMESROMAN_ID = 1;

    /**
     * The internal font number (e.g. /F7) for the basefont "Times-Bold".
     * The value must be in the range from 1 to 14.
     */
    private static final int TIMESBOLD_ID = 2;

    /**
     * The internal font number (e.g. /F7) for the basefont "Times-Italic".
     * The value must be in the range from 1 to 14.
     */
    private static final int TIMESITALIC_ID = 3;

    /**
     * The internal font number (e.g. /F7) for the basefont "Times-BoldItalic".
     * The value must be in the range from 1 to 14.
     */
    private static final int TIMESBOLDITALIC_ID = 4;

    /**
     * The internal font number (e.g. /F7) for the basefont "Helvetica".
     * The value must be in the range from 1 to 14.
     */
    private static final int HELVETICA_ID = 5;

    /**
     * The internal font number (e.g. /F7) for the basefont "Helvetica-Bold".
     * The value must be in the range from 1 to 14.
     */
    private static final int HELVETICABOLD_ID = 6;

    /**
     * The internal font number (e.g. /F7) for the basefont "Helvetica-Oblique".
     * The value must be in the range from 1 to 14.
     */
    private static final int HELVETICAOBLIQUE_ID = 7;

    /**
     * The internal font number (e.g. /F7) for the basefont "Helvetica-BoldOblique".
     * The value must be in the range from 1 to 14.
     */
    private static final int HELVETICABOLDOBLIQUE_ID = 8;

    /**
     * The internal font number (e.g. /F7) for the basefont "Courier".
     * The value must be in the range from 1 to 14.
     */
    private static final int COURIER_ID = 9;

    /**
     * The internal font number (e.g. /F7) for the basefont "Courier-Bold".
     * The value must be in the range from 1 to 14.
     */
    private static final int COURIERBOLD_ID = 10;

    /**
     * The internal font number (e.g. /F7) for the basefont "Courier-Oblique".
     * The value must be in the range from 1 to 14.
     */
    private static final int COURIEROBLIQUE_ID = 11;

    /**
     * The internal font number (e.g. /F7) for the basefont "Courier-BoldOblique".
     * The value must be in the range from 1 to 14.
     */
    private static final int COURIERBOLDOBLIQUE_ID = 12;

    /**
     * The internal font number (e.g. /F7) for the basefont "Symbol".
     * The value must be in the range from 1 to 14.
     */
    private static final int SYMBOL_ID = 13;

    /**
     * The internal font number (e.g. /F7) for the basefont "ZapfDingbats".
     * The value must be in the range from 1 to 14.
     */
    private static final int ZAPFDINGBATS_ID = 14;

    /**
     * The field <tt>cfg</tt> ...
     */
    private Configuration cfg = null;

    /**
     * pdfstream
     */
    private PDFStream cs = null;

    /**
     * currentpage
     */
    private PDFPage currentPage = null;

    private final boolean debug = true;

    private final boolean embedBaseFonts = true;

    private Vector fontNameList = new Vector();

    /**
     * fontinfo
     */
    //private FontInfo fontInfo = null;
    /**
     * fontstate
     */
    //private FontState fontState = null;
    /**
     * x,y ...
     */
    private Dimen lastX = new Dimen(), lastY = new Dimen(),
            currentX = new Dimen(), currentY = new Dimen(),
            lastDP = new Dimen();

    /**
     * onlyStroke
     */
    private boolean onlyStroke;

    /**
     * operators
     */
    private PDFOperators op = null;

    /**
     * The field <tt>out</tt> ...
     */
    private OutputStream out = null;

    /**
     * pdfdoc
     */
    private PDFDocument pdfDoc = null;

    /**
     * pdfresource
     */
    private PDFResources pdfResources = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * the current mode
     */
    private State state = VERTICAL;

    /**
     * Creates a new object.
     * @param theCfg the configuration
     */
    public PdfDocumentWriter(final Configuration theCfg,
            final DocumentWriterOptions options) {

        super();
        this.cfg = theCfg;
    }

    /**
     * Adds the base font structures to the pdf document.
     */
    private void addBaseFonts() {

        String name;
        int nb;

        for (int i = 0; i < LAST_BASE_FONT; i++) {
            name = (String) fontNameList.elementAt(i);
            if (name != null) {
                name = name.replaceAll("-", "");
                nb = i + 1;
                try {
                    Class clazz = Class.forName("org.apache.fop.fonts.base14."
                            + name);
                    Typeface font = (Typeface) clazz.newInstance();
                    debugFont(font, nb);
                    PDFFont pdfFont = pdfDoc.getFactory().makeFont("F" + nb,
                            font.getFontName(), font.getEncoding(), font, null);
                    pdfDoc.getResources().addFont(pdfFont);
                } catch (Exception e) { // Correct the Exection type.
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Embeds fonts to the pdf document. It must exist a
     * metric file with file name 'fop-&quot;fontname&quot;.xml':<p>
     *
     * Example (from a Type1-based pfm file):<p>
     * <code> java -cp lib/fop.jar \ </code><br>
     * <code> org.apache.fop.fonts.apps.PFMReader cmr12.pfm fop-cmr12.xml</code><p>
     *
     *   Note: The flag entry was wrong (&quot;0&quot;). Should be &quot;34&quot;
     *   in this case.
     *   And the embed entry was empty:
     *   '&lt;embed file=&quot;file:font/cmr12.pfb&quot;/&gt;' added.<p>
     *
     * Example (from a TTF file):<p>
     *
     * <code> java -cp lib/fop.jar org.apache.fop.fonts.apps.TTFReader \</code><br>
     * <code> -enc ansi cmtt12.ttf fop-cmtt12.xml </code><p>
     *
     *   Note: The parameter '-enc ansi' is required because only single byte
     *   fonts are supported this time.
     *   '&lt;embed file=&quot;file:font/cmtt12.ttf&quot;/&gt;' added.<p>
     *
     * (How works embedding of font subsets? RN)<p>
     *
     * @param full If true, embeds also base14 fonts.
     */
    private void addEmbedFonts(final boolean full) {

        for (int i = (full) ? 0 : LAST_BASE_FONT; i < fontNameList.size(); i++) {
            String name = (String) fontNameList.elementAt(i);
            if (name != null) {
                FontReader reader = null;
                int nb = i + 1;
                try {
                    reader = new FontReader("file:src/font/fop-" + name
                            + ".xml");
                } catch (FOPException e) {
                    e.printStackTrace();
                }
                Typeface font = reader.getFont();
                debugFont(font, nb);
                // Prepend a prefix to the Adobe names.
                if (i < LAST_BASE_FONT) {
                    name = uniquePrefix() + "+" + name;
                }
                PDFFont pdfFont = pdfDoc.getFactory().makeFont("F" + nb, name,
                        font.getEncoding(), font, (FontDescriptor) font);
                pdfDoc.getResources().addFont(pdfFont);
            }
        }
    }

    private void addFonts() {

        if (embedBaseFonts) {
            addEmbedFonts(true);
        } else {
            addBaseFonts();
            addEmbedFonts(false);
        }
    }

    /**
     * beginPdfDocument Opens/setups the document
     * @throws IOException ...
     */
    private void beginPdfDocument() throws IOException {

        Map filterMap = new java.util.HashMap();
        List filterList = new java.util.ArrayList();

        pdfDoc = new PDFDocument("");

        //filterList.add("flate");
        filterList.add("null");
        filterMap.put(PDFFilterList.DEFAULT_FILTER, filterList);
        filterMap.put(PDFFilterList.CONTENT_FILTER, filterList);
        filterMap.put(PDFFilterList.IMAGE_FILTER, filterList);
        filterMap.put(PDFFilterList.JPEG_FILTER, filterList);
        filterMap.put(PDFFilterList.FONT_FILTER, filterList);
        pdfDoc.setFilterMap(filterMap);

        pdfDoc.getInfo().setProducer("ExTeX-0.00");
        pdfDoc.getInfo().setCreator("LaTeX with hyperref");
        pdfDoc.getInfo().setTitle("Allerlei Probiererei");
        pdfDoc.getInfo().setAuthor("Rolf");
        pdfDoc.getInfo().setSubject("ExTeX-Entwicklung");
        pdfDoc.getInfo().setKeywords("TeX, Java");
        pdfDoc.getInfo().setCreationDate(null); // current system date

        op = new PDFOperators();

        pdfDoc.outputHeader(this.out);

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     * @throws IOException ...
     */
    public void close() throws IOException {

        endPdfDocument();
    }

    private void debugFont(final Typeface font, final int nb) {

        if (debug) {
            boolean isEmbeddable;

            if (font instanceof FontDescriptor) {
                isEmbeddable = ((FontDescriptor) font).isEmbeddable();
            } else {
                isEmbeddable = false;
            }

            System.out.println("Font /F" + nb + ": " + font.getFontName()
                    + " (" + font.getFontType().getName() + ", kerning: "
                    + font.hasKerningInfo() + ", embeddable: " + isEmbeddable
                    + ")");
        }
    }

    /**
     * debug
     * @param node The node which will be debugged.
     */
    private void debugNode(final Node node) {

        if (debug) {
            StringBuffer sb = new StringBuffer(BUFFER_SIZE);
            try {
                node.visit(new DebugVisitor(), sb);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sb.toString());
        }
    }

    /**
     * endPdfDocument Close the document
     * @throws IOException ...
     */
    private void endPdfDocument() throws IOException {

        cs.add("BT\n"); // Begin Text
        cs.add("20 0 0 -20 0 0 Tm\n"); // Text transformation matrix
        cs.add("3.6 -8.0 TD\n"); // Move
        cs.add("/F" + getFontNumber("Helvetica-BoldOblique") + "\n");
        cs.add("1 Tf (Fridolin (Helvetica-BoldOblique; base14)) Tj \n");
        cs.add("0.0 -1.0 TD\n"); // Move
        cs.add("/F" + getFontNumber("cmr12") + "\n");
        cs.add("1 Tf (HUGO  (cmr12; Type1)) Tj \n");
        cs.add("0.0 -1.0 TD\n"); // Move
        cs.add("/F" + getFontNumber("cmtt12") + "\n");
        cs.add("1 Tf (GUSTAV (cmtt12; TTF)) Tj \n");
        cs.add("ET\n"); // End Text

        addFonts();

        pdfDoc.outputTrailer(this.out); // Calls also output(...).
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "pdf";
    }

    private int getFontNumber(final String name) {

        if (fontNameList.isEmpty()) { // Preserve space for the base fonts.
            for (int i = 0; i < LAST_BASE_FONT; i++) {
                fontNameList.addElement(null);
            }
        }

        int idx = fontNameList.indexOf(name);

        if (idx == NOT_FOUND) {
            if (name.equals("Times-Roman")) {
                idx = TIMESROMAN_ID - 1;
            } else if (name.equals("Times-Bold")) {
                idx = TIMESBOLD_ID - 1;
            } else if (name.equals("Times-Italic")) {
                idx = TIMESITALIC_ID - 1;
            } else if (name.equals("Times-BoldItalic")) {
                idx = TIMESBOLDITALIC_ID - 1;
            } else if (name.equals("Helvetica")) {
                idx = HELVETICA_ID - 1;
            } else if (name.equals("Helvetica-Bold")) {
                idx = HELVETICABOLD_ID - 1;
            } else if (name.equals("Helvetica-Oblique")) {
                idx = HELVETICAOBLIQUE_ID - 1;
            } else if (name.equals("Helvetica-BoldOblique")) {
                idx = HELVETICABOLDOBLIQUE_ID - 1;
            } else if (name.equals("Courier")) {
                idx = COURIER_ID - 1;
            } else if (name.equals("Courier-Bold")) {
                idx = COURIERBOLD_ID - 1;
            } else if (name.equals("Courier-Oblique")) {
                idx = COURIEROBLIQUE_ID - 1;
            } else if (name.equals("Courier-BoldOblique")) {
                idx = COURIERBOLDOBLIQUE_ID - 1;
            } else if (name.equals("Symbol")) {
                idx = SYMBOL_ID - 1;
            } else if (name.equals("ZapfDingbats")) {
                idx = ZAPFDINGBATS_ID - 1;
            }

            if (idx > NOT_FOUND) {
                fontNameList.setElementAt(name, idx);
            } else {
                fontNameList.addElement(name);
                idx = fontNameList.size() - 1;
            }
        }

        return ++idx;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * Draws a marker at the position 1in from the left and 1in from top.
     */
    private void markOrigin() {

        cs.add(op.gSave());
        cs.add(op.lineWidth(THICK_LINE));
        cs.add(op.strokeColor(Color.RED));
        cs.add(op.circle(ONE_INCH_IN_BP, ONE_INCH_IN_BP, MARKER_RADIUS));
        cs.add(op.moveTo(ONE_INCH_IN_BP - MARKER_RADIUS, ONE_INCH_IN_BP));
        cs.add(op.rLineTo(MARKER_RADIUS, 0));
        cs.add(op.moveTo(ONE_INCH_IN_BP, ONE_INCH_IN_BP - MARKER_RADIUS));
        cs.add(op.rLineTo(0, MARKER_RADIUS));
        cs.add(op.stroke());
        cs.add(op.gRestore());
    }

    /**
     * newPage Creates and setups a new page
     * @param pageWD    the page width in bp
     * @param pageHT    the page height in bp
     * @throws IOException ...
     */
    private void newPage(final int pageWD, final int pageHT) throws IOException {

        if (pdfDoc == null) {
            beginPdfDocument();
        } else {
            pdfDoc.output(this.out);
        }

        currentPage = pdfDoc.getFactory().makePage(pdfDoc.getResources(),
                pageWD, pageHT);
        pdfDoc.addObject(currentPage);

        cs = pdfDoc.getFactory().makeStream(PDFFilterList.CONTENT_FILTER, true);
        currentPage.setContents(cs);

        // Transform origin at bottom left to origin at top left
        cs.add(op.concat(1, 0, 0, -1, 0, pageHT));

        lastX.set(Dimen.ONE_INCH);
        lastY.set(Dimen.ONE_INCH);
        lastDP.set(0L);
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(java.io.Writer)
     */
    public void setOutputStream(final OutputStream outStream) {

        this.out = outStream;
    }

    /**
     * Sets the current position.
     * @param   node A single node.
     */
    private void setPosition(final Node node) {

        if (state == HORIOZONTAL) {
            currentX.add(node.getWidth());
        } else {
            currentY.add(node.getHeight());
            currentY.add(node.getDepth());
        }
    }

    /**
     * shipout
     * @param   nodes   the nodelist
     * @throws IOException ...
     * @throws GeneralException ...
     */
    public void shipout(final NodeList nodes)
            throws IOException,
                GeneralException {

        newPage(WIDTH_A4, HEIGHT_A4);
        shippedPages = pdfDoc.getPages().getCount();
        markOrigin();
        nodes.visit(this, null);
    }

    /**
     * Draws a colored box with the dimensions of the node.
     * @param  node   A single node
     * @param  operators A buffer preset with some PDF operators.
     */
    private void showNode(final Node node, final StringBuffer operators) {

        Dimen wd = new Dimen(node.getWidth());
        Dimen ht = new Dimen(node.getHeight());
        Dimen dp = new Dimen(node.getDepth());

        onlyStroke = false;

        cs.add(op.gSave());
        cs.add(operators.toString());
        cs.add(op.lineWidth(THIN_LINE));

        float rX = (float) Unit.getDimenAsBP(currentX);
        float rY = (float) Unit.getDimenAsBP(currentY)
                - (float) Unit.getDimenAsBP(ht);
        float rWD = (float) Unit.getDimenAsBP(wd);
        float rHT = (float) Unit.getDimenAsBP(ht)
                + (float) Unit.getDimenAsBP(dp);

        if (rHT <= 0.0) {
            rHT = DUMMY_HEIGHT;
        }

        cs.add(op.addRectangle(rX, rY, rWD, rHT));

        if (onlyStroke) {
            cs.add(op.stroke());
        } else {
            cs.add(op.fillStroke());
        }

        if (!dp.le(Dimen.ZERO_PT)) { // baseline
            rY = (float) Unit.getDimenAsBP(currentY);
            cs.add(op.gSave());
            cs.add(op.setLineDash(DASH_LEN, DASH_DISTANCE));
            cs.add(op.moveTo(rX, rY));
            cs.add(op.rLineTo(rWD, 0f));
            cs.add(op.stroke());
            cs.add(op.gRestore());
        }
        cs.add(op.gRestore());
    }

    /**
     * Create a quasiunique prefix for fontname
     * @return The prefix
     * @see org.apache.fop.fonts.MultiByteFont#MultiByteFont()
     */
    private String uniquePrefix() {

        int cnt = 0;
        synchronized (this.getClass()) {
            cnt = uniqueCounter++;
        }
        int ctm = (int) (System.currentTimeMillis() & UNIQUE_MASK);
        return new String(cnt + "E" + Integer.toHexString(ctm));
    }

    /**
     * adjust
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitAdjust(final AdjustNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * aftermath
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitAfterMath(final AfterMathNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * alignedleader
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * beforemath
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * centerleaders
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * char
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitChar(final CharNode node, final Object value) {

        StringBuffer operators = new StringBuffer(BUFFER_SIZE);
        operators.append(op.fillColor(Color.GREEN));
        showNode(node, operators);
        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * discretionary
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * expandleaders
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * glue
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitGlue(final GlueNode node, final Object value) {

        StringBuffer operators = new StringBuffer(BUFFER_SIZE);
        operators.append(op.fillColor(Color.BLUE));
        showNode(node, operators);
        debugNode(node);
        if (debug) {
            if (state == HORIOZONTAL) {
                System.out.println("==> hor. glue");
            } else {
                System.out.println("==> ver. glue");
            }
        }
        setPosition(node);
        return null;
    }

    /**
     * horizontal list
     * @param nodes    the value
     * @param value    the next value
     * @return null
     */
    public Object visitHorizontalList(final HorizontalListNode nodes,
            final Object value) {

        State oldstate = state;
        state = HORIOZONTAL;

        Dimen ht = new Dimen(nodes.getHeight());
        Dimen dp = new Dimen(nodes.getDepth());

        currentX.set(lastX);
        currentY.set(lastY);
        currentY.add(lastDP);
        currentY.add(ht);
        lastDP.set(dp);

        debugNode(nodes);

        NodeIterator it = nodes.iterator();
        while (it.hasNext()) {
            Node anode = it.next();
            try {
                anode.visit(this, null);
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
     * insertion
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitInsertion(final InsertionNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * kern
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitKern(final KernNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * ligature
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitLigature(final LigatureNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * mark
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitMark(final MarkNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * penalty
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitPenalty(final PenaltyNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * rule
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitRule(final RuleNode node, final Object value) {

        debugNode(node);
        setPosition(node);
        return null;
    }

    /**
     * space
     * @param node     the value
     * @param value    the next value
     * @return null
     */
    public Object visitSpace(final SpaceNode node, final Object value) {

        StringBuffer operators = new StringBuffer(BUFFER_SIZE);
        operators.append(op.fillColor(Color.YELLOW));
        showNode(node, operators);
        setPosition(node);
        return null;
    }

    /**
     * verticallist
     * @param nodes    the value
     * @param value    the next value
     * @return null
     */
    public Object visitVerticalList(final VerticalListNode nodes,
            final Object value) {

        StringBuffer operators = new StringBuffer(BUFFER_SIZE);

        State oldstate = state;
        state = VERTICAL;

        Dimen ht = new Dimen(nodes.getHeight());
        Dimen saveX = new Dimen(lastX);
        Dimen saveY = new Dimen(lastY);

        currentX.set(lastX);
        currentY.set(lastY);
        currentY.add(ht);

        operators.append(op.fillColor(Color.LIGHT_GRAY));

        showNode(nodes, operators);
        debugNode(nodes);

        currentX.set(saveX);
        currentY.set(saveY);

        NodeIterator it = nodes.iterator();
        while (it.hasNext()) {
            Node anode = it.next();
            try {
                anode.visit(this, null);
            } catch (Exception e) {
                e.printStackTrace(); // TODO: handle exception
            }
        }

        state = oldstate;
        return null;
    }

    /**
     * whatsit
     * @param nde     the value
     * @param value    the next value
     * @return null
     */
    public Object visitWhatsIt(final WhatsItNode nde, final Object value) {

        Node node = (Node) nde;
        debugNode(node);
        setPosition(node);
        return null;
    }
}