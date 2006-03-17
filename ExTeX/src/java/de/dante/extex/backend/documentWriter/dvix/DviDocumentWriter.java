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
import java.util.logging.Logger;

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
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides a base implementation of a DVI document writer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DviDocumentWriter
        implements
            DocumentWriter,
            SingleDocumentStream,
            Localizable,
            LogEnabled {

    /**
     * The constant <tt>MAX_4_BYTE</tt> contains the maximal value of a signed
     * 4-byte value.
     */
    private static final int MAX_4_BYTE = 0x7fffffff;

    /**
     * The field <tt>bopPointer</tt> contains the pointer to the last BOP.
     */
    private int bopPointer = -1;

    /**
     * The field <tt>fnt</tt> contains the font number currently active.
     */
    private int fnt = -1;

    /**
     * The field <tt>h</tt> contains the h value of the DVI interpreter.
     */
    private int h;

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer;

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private Logger logger;

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
     * The field <tt>stack</tt> contains the stack of the DVI interpreter.
     */
    private Stack stack = new Stack();

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
     * The field <tt>v</tt> contains the v value of the DVI interpreter.
     */
    private int v;

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
                list.add(new DviDown((int) dist));
                v += dist;
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
            int[] frame = (int[]) stack.pop();
            w = frame[0];
            x = frame[1];
            y = frame[2];
            z = frame[3];
        }

        /**
         * Push the state to the DVI stack.
         *
         * @param list the list with DVI instructions.
         */
        private void push(final List list) {

            list.add(DviCode.PUSH);
            stack.push(new int[]{w, x, y, z});
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
                list.add(new DviRight((int) dist));
                h += dist;
            }
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
         *      de.dante.extex.typesetter.type.node.AdjustNode,
         *      java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object value)
                throws GeneralException {

            // TODO gene: visitAdjust unimplemented
            return null;
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
        public Object visitChar(final CharNode node, final Object value)
                throws GeneralException {

            List list = (List) value;
            Font font = node.getTypesettingContext().getFont();
            int f = postamble.mapFont(font, list);
            if (f != fnt) {
                list.add(new DviFnt(f));
                fnt = f;
            }

            if (horizontal) {
                h += node.getGlyph().getWidth().getValue();
                list.add(new DviSetChar(node.getCharacter().getCodePoint()));
                return Boolean.TRUE; // do not move any more
            } else {
                list.add(new DviPutChar(node.getCharacter().getCodePoint()));
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
            right(list, node.getShift().getValue());
            down(list, node.getMove().getValue());
            int size = node.size();
            int v0 = v;

            for (int i = 0; i < size; i++) {
                n = node.get(i);
                if (n.visit(this, value) == null) {
                    right(list, n.getWidth().getValue());
                }
                down(list, v - v0);
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
        public Object visitRule(final RuleNode node, final Object value)
                throws GeneralException {

            List list = (List) value;
            int a = (int) node.getWidth().getValue();
            int b = (int) node.getHeight().getValue();
            list.add(new DviSetRule(b, a));
            h += a;
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
            boolean save = horizontal;
            horizontal = false;
            List list = (List) value;
            right(list, node.getShift().getValue());
            down(list, node.getMove().getValue());
            int size = node.size();
            int h0 = h;

            for (int i = 0; i < size; i++) {
                n = node.get(i);
                down(list, n.getHeight().getValue());
                if (n.visit(this, value) == null) {
                    down(list, n.getDepth().getValue());
                }
                right(list, h0 - h);
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
     * The field <tt>w</tt> contains the w value of the DVI interpreter.
     */
    private int w;

    /**
     * The field <tt>x</tt> contains the x value of the DVI interpreter.
     */
    private int x;

    /**
     * The field <tt>y</tt> contains the y value of the DVI interpreter.
     */
    private int y;

    /**
     * The field <tt>z</tt> contains the z value of the DVI interpreter.
     */
    private int z;

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
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer l) {

        this.localizer = l;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger l) {

        this.logger = l;
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
        h = 0;
        v = 0;
        w = 0;
        x = 0;
        y = 0;
        z = 0;
        List list = new ArrayList();
        NodeList nodes = page.getNodes();
        nodes.visit(visitor, list);

        optimize(list);

        int stackDepth = 0;

        for (int i = 0; i < list.size(); i++) {
            DviCode code = (DviCode) list.get(i);
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
     *  if necessary
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
        String comment = " TeX output " + two("year") + "." + two("month")
                + "." + two("day") + ":"
                + Long.toString((time / 60) * 100 + time % 60);
        long mag = options.getMagnification();
        if (mag > MAX_4_BYTE) {
            mag = MAX_4_BYTE;
        }
        pointer += new DviPreamble((int) mag, comment).write(stream);

        postamble = new DviPostamble((int) mag);
    }

}
