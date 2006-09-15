/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter.dvix;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.dante.dviware.Dvi;
import de.dante.dviware.type.DviBop;
import de.dante.dviware.type.DviCode;
import de.dante.dviware.type.DviDown;
import de.dante.dviware.type.DviEop;
import de.dante.dviware.type.DviFnt;
import de.dante.dviware.type.DviPostamble;
import de.dante.dviware.type.DviPreamble;
import de.dante.dviware.type.DviPutChar;
import de.dante.dviware.type.DviRight;
import de.dante.dviware.type.DviSetChar;
import de.dante.dviware.type.DviSetRule;
import de.dante.dviware.type.DviXxx;
import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.SingleDocumentStream;
import de.dante.extex.color.ColorAware;
import de.dante.extex.color.ColorConverter;
import de.dante.extex.color.model.CmykColor;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.typesetter.type.Node;
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
import de.dante.extex.typesetter.type.node.SpecialNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a base implementation of a DVI document writer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class DviDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            ColorAware,
            Configurable {

    /**
     * The constant <tt>MAX_4_BYTE</tt> contains the maximal value of a signed
     * 4-byte value.
     */
    private static final int MAX_4_BYTE = 0x7fffffff;

    /**
     * The field <tt>MINUTES_PER_OUR</tt> contains the number of minutes per
     * hour.
     */
    private static final int MINUTES_PER_OUR = 60;

    /**
     * The field <tt>bopPointer</tt> contains the pointer to the last BOP.
     */
    private int bopPointer = -1;

    /**
     * The field <tt>colorSpecials</tt> contains the indicator whether or not to
     * include color specials.
     */
    private boolean colorSpecials = false;

    /**
     * The field <tt>converter</tt> contains the color converter to use.
     */
    private ColorConverter colorConverter = null;

    /**
     * The field <tt>dviH</tt> contains the h value of the DVI interpreter.
     */
    private int dviH;

    /**
     * The field <tt>dviStack</tt> contains the stack of the DVI interpreter.
     */
    private Stack dviStack = new Stack();

    /**
     * The field <tt>dviV</tt> contains the v value of the DVI interpreter.
     */
    private int dviV;

    /**
     * The field <tt>dviW</tt> contains the w value of the DVI interpreter.
     */
    private int dviW;

    /**
     * The field <tt>dviX</tt> contains the x value of the DVI interpreter.
     */
    private int dviX;

    /**
     * The field <tt>dviY</tt> contains the y value of the DVI interpreter.
     */
    private int dviY;

    /**
     * The field <tt>dviZ</tt> contains the z value of the DVI interpreter.
     */
    private int dviZ;

    /**
     * The field <tt>fontIndex</tt> contains the font number currently active.
     */
    private int fontIndex;

    /**
     * The field <tt>notInitialized</tt> contains the indicator that the
     * preamble still needs to be written.
     */
    private boolean notInitialized = true;

    /**
     * The field <tt>options</tt> contains the options to use.
     */
    private DocumentWriterOptions options;

    /**
     * The field <tt>pointer</tt> contains the index of the next byte to be
     * written.
     */
    private int pointer = 0;

    /**
     * The field <tt>postamble</tt> contains the postamble carrying the
     * font list.
     */
    private DviPostamble postamble;

    /**
     * The field <tt>stacksize</tt> contains the maximum depth of the stack
     * needed to process all push and pop instructions.
     */
    private int stacksize = 1;

    /**
     * The field <tt>stream</tt> contains the target.
     */
    private OutputStream stream;

    /**
     * The field <tt>textColor</tt> contains the current text color.
     */
    private Color textColor;

    /**
     * The field <tt>visitor</tt> contains the visitor carrying the methods for
     * translating nodes to DVI instructions.
     */
    private NodeVisitor visitor = new NodeVisitor() {

        /**
         * The field <tt>horizontal</tt> contains the indicator that the
         * processing is in horizontal mode. Otherwise it is in vertical mode.
         */
        private boolean horizontal = true;

        /**
         * Move the reference point to vertically.
         * This means move it downwards or upwards if the argument is
         * negative.
         *
         * @param dist the distance to move down
         * @param list the list with DVI instructions.
         */
        private void down(final List list, final long dist) {

            if (dist != 0) {

                dviV += dist;

                for (int i = list.size() - 1; i >= 0; i--) {
                    Object n = list.get(i);
                    if (n instanceof DviDown) {
                        ((DviDown) n).add((int) dist);
                        return;
                    } else if (!(n instanceof DviRight)) {
                        break;
                    }
                }

                list.add(new DviDown((int) dist));
            }
        }

        /**
         * Pop the state from the DVI stack.
         *
         * @param list the list with DVI instructions.
         */
        private void pop(final List list) {

            int size = list.size();
            while (size > 0) {
                DviCode code = (DviCode) list.get(size - 1);
                if (code instanceof DviRight || code instanceof DviDown) {
                    list.remove(--size);
                } else {
                    break;
                }
            }

            list.add(DviCode.POP);
            int[] frame = (int[]) dviStack.pop();
            dviW = frame[0];
            dviX = frame[1];
            dviY = frame[2];
            dviZ = frame[3];
        }

        /**
         * Push the state to the DVI stack.
         *
         * @param list the list with DVI instructions.
         */
        private void push(final List list) {

            list.add(DviCode.PUSH);
            dviStack.push(new int[]{dviW, dviX, dviY, dviZ});
        }

        /**
         * Move the reference point to horizontally.
         * This means move it rightwards or leftwards if the argument is
         * negative.
         *
         * @param dist the distance to move right
         * @param list the list with DVI instructions.
         */
        private void right(final List list, final long dist) {

            if (dist != 0) {
                dviH += dist;

                for (int i = list.size() - 1; i >= 0; i--) {
                    Object n = list.get(i);
                    if (n instanceof DviRight) {
                        ((DviRight) n).add((int) dist);
                        return;
                    } else if (!(n instanceof DviDown)) {
                        break;
                    }
                }

                list.add(new DviRight((int) dist));
            }
        }

        /**
         * Insert a color switching special if the current color is not set to
         * the expected value and remember the current color.
         *
         * @param dviCode list of DVI instructions to add the special to
         * @param color the new color
         */
        private void switchColors(final List dviCode, final Color color) {

            if (!color.equals(textColor)) {
                textColor = color;
                String cc = color(textColor);
                if (cc != null) {
                    dviCode.add(new DviXxx("color " + cc));
                }
            }
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
         *      de.dante.extex.typesetter.type.node.AdjustNode,
         *      java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object value)
                throws GeneralException {

            // silently ignored
            return Boolean.TRUE;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
         *      de.dante.extex.typesetter.type.node.AfterMathNode,
         *      java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node,
                final Object value) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
         *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object value) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
         *      de.dante.extex.typesetter.type.node.BeforeMathNode,
         *      java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object value) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
         *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
         *      java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object value) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
         *      de.dante.extex.typesetter.type.node.CharNode,
         *      java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object code)
                throws GeneralException {

            List dviCode = (List) code;
            Font font = node.getTypesettingContext().getFont();
            int f = postamble.mapFont(font, dviCode);
            if (f != fontIndex) {
                dviCode.add(new DviFnt(f));
                fontIndex = f;
            }
            if (colorSpecials) {
                switchColors(dviCode, node.getTypesettingContext().getColor());
            }

            if (horizontal) {
                dviH += node.getWidth().getValue();
                dviCode.add(new DviSetChar(node.getCharacter().getCodePoint()));
                return Boolean.TRUE; // do not move any more
            } else {
                dviCode.add(new DviPutChar(node.getCharacter().getCodePoint()));
                return null;
            }
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
         *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
         *      java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object value) throws GeneralException {

            NodeList n = node.getNoBreak();
            if (n != null) {
                return n.visit(this, value);
            }
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
         *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object value) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
         *      de.dante.extex.typesetter.type.node.GlueNode,
         *      java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see "TTP [619]"
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
         *      de.dante.extex.typesetter.type.node.HorizontalListNode,
         *      java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode node,
                final Object value) throws GeneralException {

            Node n;
            boolean save = horizontal;
            horizontal = true;
            List list = (List) value;
            down(list, node.getShift().getValue());
            right(list, node.getMove().getValue());
            int size = node.size();
            int v0 = dviV;

            for (int i = 0; i < size; i++) {
                n = node.get(i);
                if (n.visit(this, value) == null) {
                    right(list, n.getWidth().getValue());
                }
                down(list, dviV - v0);
            }
            horizontal = save;
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
         *      de.dante.extex.typesetter.type.node.InsertionNode,
         *      java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node,
                final Object value) throws GeneralException {

            // silently ignored
            return Boolean.TRUE;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
         *      de.dante.extex.typesetter.type.node.KernNode,
         *      java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
         *      de.dante.extex.typesetter.type.node.LigatureNode,
         *      java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object value)
                throws GeneralException {

            return visitChar(node, value);
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
         *      de.dante.extex.typesetter.type.node.MarkNode,
         *      java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object value)
                throws GeneralException {

            // silently ignored
            return Boolean.TRUE;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
         *      de.dante.extex.typesetter.type.node.PenaltyNode,
         *      java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object value)
                throws GeneralException {

            // silently ignored
            return Boolean.TRUE;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
         *      de.dante.extex.typesetter.type.node.RuleNode,
         *      java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object code)
                throws GeneralException {

            List dviCode = (List) code;
            int width = (int) node.getWidth().getValue();
            int height = (int) node.getHeight().getValue();

            if (colorSpecials) {
                switchColors(dviCode, node.getTypesettingContext().getColor());
            }

            dviCode.add(new DviSetRule(height, width));
            dviH += width;
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
         *      de.dante.extex.typesetter.type.node.SpaceNode,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see "TTP [618]"
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
         *      de.dante.extex.typesetter.type.node.VerticalListNode,
         *      java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode node,
                final Object value) throws GeneralException {

            Node n;
            List list = (List) value;
            boolean save = horizontal;
            horizontal = false;
            down(list, node.getShift().getValue());
            right(list, node.getMove().getValue());
            int size = node.size();
            int h0 = dviH;

            for (int i = 0; i < size; i++) {
                n = node.get(i);
                down(list, n.getHeight().getValue());
                if (n.visit(this, value) == null) {
                    down(list, n.getDepth().getValue());
                }
                right(list, h0 - dviH);
            }
            horizontal = save;
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
         *      de.dante.extex.typesetter.type.node.VirtualCharNode,
         *      java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object value) throws GeneralException {

            return visitChar(node, value);
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
         *       de.dante.extex.typesetter.type.node.WhatsItNode,
         *       java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object value)
                throws GeneralException {

            if (node instanceof SpecialNode) {
                List list = (List) value;
                String text = ((SpecialNode) node).getText();
                int length = text.length();
                byte[] content = new byte[length];
                for (int i = 0; i < length; i++) {
                    content[i] = (byte) text.charAt(i);
                }
                list.add(new DviXxx(content));
                return null;
            }
            return null;
        }

    };

    /**
     * Creates a new object.
     *
     * @param options the document writer options
     */
    public DviDocumentWriter(final DocumentWriterOptions options) {

        super();
        this.options = options;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close() throws GeneralException, IOException {

        if (!notInitialized) {

            postamble.setBop(bopPointer);
            postamble.setOffset(pointer);
            pointer += postamble.write(stream);

            while (pointer % 4 != 0) {
                stream.write(Dvi.PADDING_BYTE);
                pointer++;
            }
        }

        stream.close();
    }

    /**
     * Get the color representation for the specials.
     *
     * @param color the color
     *
     * @return the string representation
     */
    private String color(final Color color) {

        if (color instanceof RgbColor) {
            RgbColor rgb = (RgbColor) color;
            return "rgb " + ((double) rgb.getRed()) / Color.MAX_VALUE + " "
                    + ((double) rgb.getGreen()) / Color.MAX_VALUE + " "
                    + ((double) rgb.getBlue()) / Color.MAX_VALUE;
        } else if (color instanceof CmykColor) {
            CmykColor cmyk = (CmykColor) color;
            return "cmyk " + ((double) cmyk.getCyan()) / Color.MAX_VALUE + " "
                    + ((double) cmyk.getMagenta()) / Color.MAX_VALUE + " "
                    + ((double) cmyk.getYellow()) / Color.MAX_VALUE + " "
                    + ((double) cmyk.getBlack()) / Color.MAX_VALUE;
        } else if (color instanceof GrayscaleColor) {
            GrayscaleColor gray = (GrayscaleColor) color;
            return "gray " + ((double) gray.getGray()) / Color.MAX_VALUE;
        }
        if (colorConverter != null) {
            Color c = colorConverter.toRgb(color);
            if (c != null) {
                return color(c);
            }
            c = colorConverter.toCmyk(color);
            if (c != null) {
                return color(c);
            }
        }
        return null;
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        String col = config.getAttribute("color");
        if (col != null) {
            colorSpecials = Boolean.valueOf(col).booleanValue();
        }
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "dvi";
    }

    /**
     * Optimize the description of a single page.
     *
     * @param list the list of codes for the page
     */
    protected void optimize(final List list) {

    }

    /**
     * @see de.dante.extex.color.ColorAware#setColorConverter(
     *      de.dante.extex.color.ColorConverter)
     */
    public void setColorConverter(final ColorConverter converter) {

        this.colorConverter = converter;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.SingleDocumentStream#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream writer) {

        this.stream = writer;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        if ("color".equals(name)) {
            colorSpecials = Boolean.valueOf(value).booleanValue();
        }
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
     */
    public int shipout(final Page page) throws GeneralException, IOException {

        if (notInitialized) {
            writePreamble();
            notInitialized = false;
        }
        int[] pageno = new int[10];
        FixedCount[] pn = page.getPageNo();
        for (int i = 0; i < pn.length; i++) {
            long val = pn[i].getValue();
            pageno[i] = (val > MAX_4_BYTE ? MAX_4_BYTE : (int) val);
        }
        int p = pointer;
        pointer += new DviBop(pageno, bopPointer).write(stream);
        bopPointer = p;
        dviH = 0;
        dviV = 0;
        dviW = 0;
        dviX = 0;
        dviY = 0;
        dviZ = 0;
        fontIndex = -1;
        List dviCode = new ArrayList();
        NodeList nodes = page.getNodes();

        if (colorSpecials) {
            String col = color(page.getColor());
            if (col != null) {
                dviCode.add(new DviXxx("background " + col));
            }
        }

        nodes.visit(visitor, dviCode);

        optimize(dviCode);

        int stackDepth = 0;

        for (int i = 0; i < dviCode.size(); i++) {
            DviCode code = (DviCode) dviCode.get(i);
            pointer += code.write(stream);
            if (code == DviCode.PUSH) {
                stackDepth++;
                if (stackDepth > stacksize) {
                    stacksize = stackDepth;
                }
            } else if (code == DviCode.POP) {
                stackDepth--;
            }
        }

        pointer += new DviEop().write(stream);
        postamble.recognizePage(nodes.getHeight(), nodes.getDepth(), nodes
                .getWidth(), stacksize);
        return 1;
    }

    /**
     * Ensure that a string has at least two characters length by padding it
     * with 0 if necessary.
     *
     * @param name the name of the count register
     *
     * @return the string representation of the value padded with a leading 0
     */
    private String two(final String name) {

        String s = options.getCountOption(name).toString();
        return (s.length() > 1 ? s : "0" + s);
    }

    /**
     * Write the preamble.
     *
     * @throws IOException in case of an error
     *
     * @see "TTP [617]"
     */
    private void writePreamble() throws IOException {

        long time = options.getCountOption("time").getValue();
        String comment = " ExTeX output "
                + two("year")
                + "."
                + two("month")
                + "."
                + two("day")
                + ":"
                + Long.toString((time / MINUTES_PER_OUR) * 100 + time
                        % MINUTES_PER_OUR);
        long mag = options.getMagnification();
        if (mag > MAX_4_BYTE) {
            mag = MAX_4_BYTE;
        }
        pointer += new DviPreamble((int) mag, comment).write(stream);

        postamble = new DviPostamble((int) mag);
    }

}
