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
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontFile;
import de.dante.extex.interpreter.type.font.Glyph;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.NodeVisitor;
import de.dante.util.GeneralException;
import de.dante.util.Unit;
import de.dante.util.configuration.Configuration;

/**
 * Implementation of a pdf document writer.
 *
 * @author <a href="mailto:Rolf.Niepraschk@ptb.de">Rolf Niepraschk</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.11 $
 * @see org.apache.fop.render.pdf.PDFRenderer
 * @see org.apache.fop.svg.PDFGraphics2D
 */
public class PdfDocumentWriter implements DocumentWriter, NodeVisitor {

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(java.lang.String, java.lang.String)
     */
    public void setParameter(String name, String value) {

        // TODO Auto-generated method stub

    }
    // -------------------------------------------------

    /**
     * NodeVisitor for debug.
     */
    private class DebugVisitor implements NodeVisitor {

        private String metric(final Node node) {

            return " (wd=" + node.getWidth().toString() + "\tht="
                    + node.getHeight().toString() + "\tdp="
                    + node.getDepth().toString() + ")";
        }

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
            CharNode node = (CharNode) value2;
            Font font = node.getTypesettingContext().getFont();

            if (font != null) {
                Glyph glyph = font.getGlyph(node.getCharacter());
                FontFile file = glyph.getExternalFile();

                /*
                 System.out.println("-----------------------------------------");
                 System.out.println("Glyph : " + node.getCharacter() + " : "
                 + glyph.getName() + " " + glyph.getNumber() + "  aus "
                 + glyph.getExternalFile());
                 System.out.println("-----------------------------------------");
                 */

                sb.append(" " + node.getCharacter() + " [" + glyph.getNumber()
                        + "] ");
                sb.append("Char");
                sb.append(metric(node));
                sb.append(" " + file.getFile());
            }
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

    private static final int HEIGHT_A4 = 842; // "bp"

    /**
     * in horizontal mode
     */
    private final static State HORIOZONTAL = new State();

    private static int uniqueCounter = 1;

    /**
     * in vertical mode
     */
    private final static State VERTICAL = new State();

    // TeX primitives should set the papersize in any way:
    // o \paperwidth   / \paperheight, 
    // o \pdfpagewidth / \pdfpageheight <-- pdfTeX
    // o \mediawidth   / \mediaheight   <-- VTeX
    private static final int WIDTH_A4 = 595; // "bp"

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

    private final boolean embedBase14 = true;

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
     * @param cfg the configuration
     */
    public PdfDocumentWriter(final Configuration cfg) {

        super();
        this.cfg = cfg;
    }

    /**
     * Adds base14 font structures to the pdf document. 
     */
    private void addBase14Fonts() {

        String name;
        int nb;

        for (int i = 0; i < 14; i++) {
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
                } catch (Exception e) {
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
     *   Note: The flag entry was wrong (&quot;0&quot;). Should be &quot;34&quot; in this case.
     *   And the embed entry was empty: '&lt;embed file=&quot;file:font/cmr12.pfb&quot;/&gt;' added.<p>
     *
     * Example (from a TTF file):<p>
     *
     * <code> java -cp lib/fop.jar org.apache.fop.fonts.apps.TTFReader \</code><br>
     * <code> -enc ansi cmtt12.ttf fop-cmtt12.xml </code><p>     
     *       
     *   Note: The parameter '-enc ansi' is required because only single byte
     *   fonts are supported this time. '&lt;embed file=&quot;file:font/cmtt12.ttf&quot;/&gt;' added.<p>
     *
     * (How works embedding of font subsets? RN)<p>
     *
     * @param full If true, embeds also base14 fonts.
     */
    private void addEmbedFonts(boolean full) {

        for (int i = (full) ? 0 : 14; i < fontNameList.size(); i++) {
            String name = (String) fontNameList.elementAt(i);
            if (name != null) {
                FontReader reader = null;
                int nb = i + 1;
                try {
                    reader = new FontReader("file:font/fop-" + name + ".xml");
                } catch (FOPException e) {
                    e.printStackTrace();
                }
                Typeface font = reader.getFont();
                debugFont(font, nb);
                // Prepend a prefix to the Adobe names.
                if (i < 14)
                    name = uniquePrefix() + "+" + name;
                PDFFont pdfFont = pdfDoc.getFactory().makeFont("F" + nb, name,
                        font.getEncoding(), font, (FontDescriptor) font);
                pdfDoc.getResources().addFont(pdfFont);
            }
        }
    }

    private void addFonts() {

        if (embedBase14)
            addEmbedFonts(true);
        else {
            addBase14Fonts();
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

    private void debugFont(Typeface font, int nb) {

        if (debug) {
            boolean isEmbeddable;

            if (font instanceof FontDescriptor)
                isEmbeddable = ((FontDescriptor) font).isEmbeddable();
            else
                isEmbeddable = false;

            System.out.println("Font /F" + nb + ": " + font.getFontName()
                    + " (" + font.getFontType().getName() + ", kerning: "
                    + font.hasKerningInfo() + ", embedded: " + isEmbeddable
                    + ")");
        }
    }

    /**
     * debug
     */
    private void debugNode(Node node) {

        if (debug) {
            StringBuffer sb = new StringBuffer(256);
            try {
                node.visit(new DebugVisitor(), sb, node);
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

    private int getFontNumber(String name) {

        if (fontNameList.isEmpty()) { // Preserve space for base14 fonts.
            for (int i = 0; i < 14; i++)
                fontNameList.addElement(null);
        }

        int idx = fontNameList.indexOf(name);

        if (idx == -1) {
            if (name.equals("Times-Roman"))
                idx = 0;
            else if (name.equals("Times-Bold"))
                idx = 1;
            else if (name.equals("Times-Italic"))
                idx = 2;
            else if (name.equals("Times-BoldItalic"))
                idx = 3;
            else if (name.equals("Helvetica"))
                idx = 4;
            else if (name.equals("Helvetica-Bold"))
                idx = 5;
            else if (name.equals("Helvetica-Oblique"))
                idx = 6;
            else if (name.equals("Helvetica-BoldOblique"))
                idx = 7;
            else if (name.equals("Courier"))
                idx = 8;
            else if (name.equals("Courier-Bold"))
                idx = 9;
            else if (name.equals("Courier-Oblique"))
                idx = 10;
            else if (name.equals("Courier-BoldOblique"))
                idx = 11;
            else if (name.equals("Symbol"))
                idx = 12;
            else if (name.equals("ZapfDingbats"))
                idx = 13;

            if (idx > -1)
                fontNameList.setElementAt(name, idx);
            else {
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
     * newPage Creates and setups a new page
     * @param pageWD    the page width in bp
     * @param pageHT    the page height in bp
     * @throws IOException ...
     */
    private void newPage(int pageWD, int pageHT) throws IOException {

        if (pdfDoc == null)
            beginPdfDocument();
        else
            pdfDoc.output(this.out);

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

    private void setPosition(Node node) {

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
    public void shipout(final NodeList nodes) throws IOException,
            GeneralException {

        newPage(WIDTH_A4, HEIGHT_A4);
        shippedPages = pdfDoc.getPages().getCount();
        markOrigin();
        nodes.visit(this, nodes, null);
    }

    private void showNode(Node node, StringBuffer operators) {

        Dimen wd = new Dimen(node.getWidth());
        Dimen ht = new Dimen(node.getHeight());
        Dimen dp = new Dimen(node.getDepth());

        onlyStroke = false;

        cs.add(op.gSave());
        cs.add(operators.toString());
        cs.add(op.lineWidth(.3f));

        float rX = (float) Unit.getDimenAsBP(currentX);
        float rY = (float) Unit.getDimenAsBP(currentY)
                - (float) Unit.getDimenAsBP(ht);
        float rWD = (float) Unit.getDimenAsBP(wd);
        float rHT = (float) Unit.getDimenAsBP(ht)
                + (float) Unit.getDimenAsBP(dp);

        if (rHT <= 0.0) {
            rHT = -1.5f;
        }

        cs.add(op.addRectangle(rX, rY, rWD, rHT));

        if (onlyStroke)
            cs.add(op.stroke());
        else
            cs.add(op.fillStroke());

        if (!dp.le(Dimen.ZERO_PT)) { // baseline
            rY = (float) Unit.getDimenAsBP(currentY);
            cs.add(op.gSave());
            cs.add(op.setLineDash(.3f, .3f));
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
        int ctm = (int) (System.currentTimeMillis() & 0xffff);
        return new String(cnt + "E" + Integer.toHexString(ctm));
    }

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
     * char
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitChar(final Object value, final Object value2) {

        CharNode node = (CharNode) value;
        StringBuffer operators = new StringBuffer(256);
        operators.append(op.fillColor(Color.GREEN));
        showNode(node, operators);
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
     * horizontallist
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitHorizontalList(final Object value, final Object value2) {

        NodeList nodes = (HorizontalListNode) value;

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
     * verticallist
     * @param value     the value
     * @param value2    the next value
     * @return null
     */
    public Object visitVerticalList(final Object value, final Object value2) {

        NodeList nodes = (NodeList) value;
        StringBuffer operators = new StringBuffer(256);

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
}