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

package de.dante.extex.documentWriter.postscript.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.dante.extex.color.ColorAware;
import de.dante.extex.color.ColorConverter;
import de.dante.extex.color.model.GrayscaleColor;
import de.dante.extex.color.model.RgbColor;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.documentWriter.exception.DocumentWriterIOException;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
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
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

/**
 * This class provides a converter to PostScript code.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class PsBasicConverter
        implements
            PsConverter,
            NodeVisitor,
            ResourceConsumer,
            ColorAware {

    /**
     * This inner class is used to keep track of characters which are delayed
     * for output.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    private class Buffer {

        /**
         * The field <tt>charBuffer</tt> contains the dynamic buffer.
         */
        private StringBuffer buffer = new StringBuffer();

        /**
         * The field <tt>currX</tt> contains the x coordinate for the first
         * character.
         */
        private Dimen currX = new Dimen();

        /**
         * The field <tt>currY</tt> contains the y coordinate for the first
         * character.
         */
        private Dimen currY = new Dimen(Long.MIN_VALUE);

        /**
         * The field <tt>empty</tt> contains the indicator that the buffer is
         * empty.
         */
        private boolean empty = true;

        /**
         * The field <tt>shift</tt> contains the indicator that the first
         * character is not on the same y coordinate as the previous one.
         */
        private boolean shift = false;

        /**
         * Creates a new object.
         */
        public Buffer() {

            super();
        }

        /**
         * Add a single character to the output.
         *
         * @param c the character to add
         * @param xx the x position
         * @param yy the y position
         */
        public void add(final UnicodeChar c, final Dimen xx, final Dimen yy) {

            if (empty) {
                shift = currY.ne(yy);
                empty = false;
                this.currX.set(xx);
                this.currY.set(yy);
            }
            int cp = c.getCodePoint();
            switch (cp) {
                case '\\':
                case '(':
                case ')':
                    buffer.append('\\');
                    break;
                default:
                    if (cp < ' ' || cp >= 127) {
                        buffer.append('\\');
                        buffer.append(Integer.toOctalString(cp));
                        return;
                    }
            // nothing to do
            }
            buffer.append(c.toString());
        }

        /**
         * Ship the collected characters out.
         *
         * @param out the target string buffer
         */
        public void clear(final StringBuffer out) {

            if (empty) {
                return;
            }
            if (shift) {
                out.append("(");
                out.append(buffer);
                out.append(")");
                PsUnit.toPoint(currX, out, false);
                out.append(' ');
                PsUnit.toPoint(currY, out, false);
                out.append(" s\n");
            } else {
                out.append("(");
                out.append(buffer);
                out.append(")");
                PsUnit.toPoint(currX, out, false);
                out.append(" x\n");
            }

            buffer.delete(0, buffer.length());
            empty = true;
        }

        /**
         * Reset the stored vertical position.
         */
        public void reset() {

            currY.set(Long.MIN_VALUE);
        }
    }

    /**
     * The field <tt>buffer</tt> contains the character buffer.
     */
    private Buffer buffer = new Buffer();

    /**
     * The field <tt>cc</tt> contains the color converter.
     */
    private ColorConverter cc = null;

    /**
     * The field <tt>currentColor</tt> contains the current color to keep track
     * of the color changing commands.
     */
    private Color currentColor = null;

    /**
     * The field <tt>finder</tt> contains the resource finder.
     */
    private ResourceFinder finder;

    /**
     * The field <tt>fm</tt> contains the font manager.
     */
    private FontManager fm = null;

    /**
     * The field <tt>hm</tt> contains the header manager.
     */
    private HeaderManager hm = null;

    /**
     * The field <tt>paperSize</tt> contains the width and the height of the
     * paper.
     */
    private Dimen[] paperSize = new Dimen[2];

    /**
     * The field <tt>paperSizeScanner</tt> contains the node visitor which
     * traverses the node tree recursively searching for <tt>papersize</tt>
     * specials.
     */
    private NodeVisitor paperSizeScanner = new NodeVisitor() {

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
         *      de.dante.extex.typesetter.type.node.AdjustNode,
         *      java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
         *      de.dante.extex.typesetter.type.node.AfterMathNode,
         *      java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
         *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
         *      de.dante.extex.typesetter.type.node.BeforeMathNode,
         *      java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
         *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
         *      java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
         *      de.dante.extex.typesetter.type.node.CharNode,
         *      java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object coordinates)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
         *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
         *      java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
         *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
         *      de.dante.extex.typesetter.type.node.GlueNode,
         *      java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object coordinates)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
         *      de.dante.extex.typesetter.type.node.HorizontalListNode,
         *      java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode node,
                final Object coordinates) throws GeneralException {

            int len = node.size();

            for (int i = 0; i < len; i++) {
                node.get(i).visit(this, coordinates);
            }
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
         *      de.dante.extex.typesetter.type.node.InsertionNode,
         *      java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
         *      de.dante.extex.typesetter.type.node.KernNode,
         *      java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object coordinates)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
         *      de.dante.extex.typesetter.type.node.LigatureNode,
         *      java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
         *      de.dante.extex.typesetter.type.node.MarkNode,
         *      java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object coordinates)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
         *      de.dante.extex.typesetter.type.node.PenaltyNode,
         *      java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
         *      de.dante.extex.typesetter.type.node.RuleNode,
         *      java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object coordinates)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
         *      de.dante.extex.typesetter.type.node.SpaceNode,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object coordinates)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
         *      de.dante.extex.typesetter.type.node.VerticalListNode,
         *      java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode node,
                final Object coordinates) throws GeneralException {

            int len = node.size();

            for (int i = 0; i < len; i++) {
                node.get(i).visit(this, coordinates);
            }
            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
         *      de.dante.extex.typesetter.type.node.VirtualCharNode,
         *      java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object coordinates) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
         *      de.dante.extex.typesetter.type.node.WhatsItNode,
         *      java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node,
                final Object coordinates) throws GeneralException {

            if (node instanceof SpecialNode) {
                String text = ((SpecialNode) node).getText();
                if (text == null || text.length() == 0) {
                    return null;
                }
                switch (text.charAt(0)) {
                    case 'p':
                        if (text.startsWith("papersize=")) {
                            Count index = new Count(10);
                            long w = PsUnit.getLength(text, index);
                            int c = PsUnit.getChar(text, index);
                            if (c != ',') {
                                throw new NumberFormatException();
                            }
                            long h = PsUnit.getLength(text, index);
                            Dimen[] paper = (Dimen[]) coordinates;
                            paper[0].set(w);
                            paper[1].set(h);
                        }
                        break;
                    case 'l':
                        if (text.equals("landscape")) {
                            Dimen[] paper = (Dimen[]) coordinates;
                            Dimen x = paper[0];
                            paper[0].set(paper[1]);
                            paper[1].set(x);
                        }
                        break;
                    default:
                // ignored on purpose
                }
            }
            return null;
        }

    };

    /**
     * The field <tt>x</tt> contains the current x position.
     */
    private Dimen x = new Dimen();

    /**
     * The field <tt>y</tt> contains the current y position.
     */
    private Dimen y = new Dimen();

    /**
     * Creates a new object.
     *
     * @param width the default width of the paper
     * @param height the default height of the paper
     */
    public PsBasicConverter(final Dimen width, final Dimen height) {

        super();
        paperSize[0] = new Dimen(width);
        paperSize[1] = new Dimen(height);
    }

    /**
     * Perform some initializations for each document.
     *
     * @param header the header manager
     *
     * @throws IOException in case of an error while loading
     */
    public void init(final HeaderManager header) throws IOException {

        String name = this.getClass().getName().replace('.', '/') + ".ps";
        InputStream stream = getClass().getClassLoader().getResourceAsStream(
                name);
        if (stream != null) {
            header.add(stream, name.substring(name.lastIndexOf('/') + 1));
            stream.close();
        }
    }

    /**
     * Translate nodes into PostScript code.
     * This method traverses the nodes tree recursively and produces the
     * corresponding PostScript code for each node visited.
     *
     * @param nodes the nodes to translate into PostScript code
     * @param fontManager the font manager to inform about characters
     * @param headerManager the header manager
     *
     * @return the bytes representing the current page
     *
     * @throws DocumentWriterException in case of an error
     *
     * @see de.dante.extex.documentWriter.postscript.util.PsConverter#nodesToPostScript(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.documentWriter.postscript.util.FontManager,
     *      de.dante.extex.documentWriter.postscript.util.HeaderManager)
     */
    public byte[] nodesToPostScript(final NodeList nodes,
            final FontManager fontManager, final HeaderManager headerManager)
            throws DocumentWriterException {

        try {
            nodes.visit(paperSizeScanner, paperSize);
        } catch (GeneralException e) {
            throw new DocumentWriterException(e);
        }

        x.set(Dimen.ONE_INCH);
        y.set(paperSize[1]);
        y.subtract(Dimen.ONE_INCH);

        fm = fontManager;
        hm = headerManager;
        buffer.reset();
        StringBuffer out = new StringBuffer("TeXDict begin\n");

        try {
            nodes.visit(this, out);
        } catch (GeneralException e) {
            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException) {

                throw new DocumentWriterIOException(cause);
            }
            throw new DocumentWriterException(e);
        }

        out.append("end\n");
        return out.toString().getBytes();
    }

    /**
     * @see de.dante.extex.color.ColorAware#setColorConverter(
     *      de.dante.extex.color.ColorConverter)
     */
    public void setColorConverter(final ColorConverter converter) {

        cc = converter;
    }

    /**
     * @see de.dante.util.resource.ResourceConsumer#setResourceFinder(
     *      de.dante.util.resource.ResourceFinder)
     */
    public void setResourceFinder(final ResourceFinder resourceFinder) {

        this.finder = resourceFinder;
    }

    /**
     * Add some text from a resource to the header section.
     *
     * @param name the name of the resource to add as header
     *
     * @throws GeneralException in case of an error
     */
    private void specialHeader(final String name) throws GeneralException {

        try {
            InputStream s = finder.findResource(name, "pro");
            if (s != null) {
                hm.add(s, name);
                s.close();
            } else {
                throw new DocumentWriterIOException(new FileNotFoundException());
            }
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * Find a PS resource and include its contents into the output stream.
     *
     * @param out the target buffer
     * @param name the name of the resource
     *
     * @throws GeneralException in case of an error
     */
    private void specialPsfile(final StringBuffer out, final String name)
            throws GeneralException {

        try {
            InputStream s = finder.findResource(name, "ps");
            if (s != null) {
                int c;
                while ((c = s.read()) >= 0) {
                    out.append((char) c);
                }
                s.close();
            } else {
                throw new DocumentWriterIOException(new FileNotFoundException());
            }
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * Switch to another color.
     *
     * @param color the color to switch to
     * @param out the target buffer
     */
    private void switchColors(final Color color, final StringBuffer out) {

        out.append(' ');
        if (color instanceof GrayscaleColor) {
            PsUnit.toPoint(new Dimen(((GrayscaleColor) color).getGray()
                    * Dimen.ONE), out, false);
            out.append(" Cg\n");
        } else {
            RgbColor rgb = cc.toRgb(color);
            PsUnit.toPoint(new Dimen(rgb.getRed() * Dimen.ONE), out, false);
            out.append(' ');
            PsUnit.toPoint(new Dimen(rgb.getGreen() * Dimen.ONE), out, false);
            out.append(' ');
            PsUnit.toPoint(new Dimen(rgb.getBlue() * Dimen.ONE), out, false);
            out.append(" Cr\n");
        }
        currentColor = color;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
     *      de.dante.extex.typesetter.type.node.AdjustNode,
     *      java.lang.Object)
     */
    public Object visitAdjust(final AdjustNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
     *      de.dante.extex.typesetter.type.node.AfterMathNode,
     *      java.lang.Object)
     */
    public Object visitAfterMath(final AfterMathNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
     *      java.lang.Object)
     */
    public Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object oOut) throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
     *      de.dante.extex.typesetter.type.node.BeforeMathNode,
     *      java.lang.Object)
     */
    public Object visitBeforeMath(final BeforeMathNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
     *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
     *      java.lang.Object)
     */
    public Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object oOut) throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      de.dante.extex.typesetter.type.node.CharNode,
     *      java.lang.Object)
     */
    public Object visitChar(final CharNode node, final Object oOut)
            throws GeneralException {

        StringBuffer out = (StringBuffer) oOut;
        TypesettingContext tc = node.getTypesettingContext();
        UnicodeChar c = node.getCharacter();
        Font font = tc.getFont();

        String fnt = fm.add(font, c);
        if (fnt != null) {
            buffer.clear(out);
            out.append(fnt);
        }

        Color color = tc.getColor();

        if (color != currentColor) {
            buffer.clear(out);
            switchColors(color, out);
        }

        buffer.add(c, x, y);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
     *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
     *      java.lang.Object)
     */
    public Object visitDiscretionary(final DiscretionaryNode node,
            final Object oOut) throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
     *      java.lang.Object)
     */
    public Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object oOut) throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
     *      de.dante.extex.typesetter.type.node.GlueNode,
     *      java.lang.Object)
     */
    public Object visitGlue(final GlueNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      java.lang.Object)
     */
    public Object visitHorizontalList(final HorizontalListNode node,
            final Object oOut) throws GeneralException {

        StringBuffer out = (StringBuffer) oOut;
        buffer.clear(out);

        Dimen saveX = new Dimen(x);
        Dimen saveY = new Dimen(y);
        x.add(node.getMove());
        y.add(node.getShift());

        Node n;
        int len = node.size();

        for (int i = 0; i < len; i++) {
            n = node.get(i);
            n.visit(this, out);
            x.add(n.getWidth());
        }

        x.set(saveX);
        y.set(saveY);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
     *      de.dante.extex.typesetter.type.node.InsertionNode,
     *      java.lang.Object)
     */
    public Object visitInsertion(final InsertionNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
     *      de.dante.extex.typesetter.type.node.KernNode,
     *      java.lang.Object)
     */
    public Object visitKern(final KernNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
     *      de.dante.extex.typesetter.type.node.LigatureNode,
     *      java.lang.Object)
     */
    public Object visitLigature(final LigatureNode node, final Object oOut)
            throws GeneralException {

        return visitChar(node, oOut);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
     *      de.dante.extex.typesetter.type.node.MarkNode,
     *      java.lang.Object)
     */
    public Object visitMark(final MarkNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
     *      de.dante.extex.typesetter.type.node.PenaltyNode,
     *      java.lang.Object)
     */
    public Object visitPenalty(final PenaltyNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
     *      de.dante.extex.typesetter.type.node.RuleNode,
     *      java.lang.Object)
     */
    public Object visitRule(final RuleNode node, final Object oOut)
            throws GeneralException {

        StringBuffer out = (StringBuffer) oOut;
        buffer.clear(out);

        TypesettingContext tc = node.getContext();
        Color color = tc.getColor();
        if (color != currentColor) {
            switchColors(color, out);
        }

        PsUnit.toPoint(node.getWidth(), out, false);
        out.append(' ');
        PsUnit.toPoint(node.getHeight(), out, false);
        out.append(' ');
        PsUnit.toPoint(x, out, false);
        out.append(' ');
        PsUnit.toPoint(y, out, false);
        out.append(' ');
        out.append("rule");
        out.append('\n');

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
     *      de.dante.extex.typesetter.type.node.SpaceNode,
     *      java.lang.Object)
     */
    public Object visitSpace(final SpaceNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
     *      de.dante.extex.typesetter.type.node.VerticalListNode,
     *      java.lang.Object)
     */
    public Object visitVerticalList(final VerticalListNode node,
            final Object oOut) throws GeneralException {

        StringBuffer out = (StringBuffer) oOut;
        buffer.clear(out);

        Dimen saveX = new Dimen(x);
        Dimen saveY = new Dimen(y);
        x.add(node.getMove());
        y.add(node.getShift());

        Node n;
        int len = node.size();

        for (int i = 0; i < len; i++) {
            n = node.get(i);
            n.visit(this, out);
            y.subtract(n.getHeight());
            y.subtract(n.getDepth());
        }

        x.set(saveX);
        y.set(saveY);

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
     *      de.dante.extex.typesetter.type.node.VirtualCharNode,
     *      java.lang.Object)
     */
    public Object visitVirtualChar(final VirtualCharNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        return visitChar(node, oOut);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
     *      de.dante.extex.typesetter.type.node.WhatsItNode,
     *      java.lang.Object)
     */
    public Object visitWhatsIt(final WhatsItNode node, final Object oOut)
            throws GeneralException {

        buffer.clear((StringBuffer) oOut);
        StringBuffer out = (StringBuffer) oOut;

        if (node instanceof SpecialNode) {
            String text = ((SpecialNode) node).getText();
            if (text == null || text.length() == 0) {
                return null;
            }
            switch (text.charAt(0)) {
                case 'p':
                    if (text.startsWith("ps:")) {
                        out.append(text.substring(3));
                    } else if (text.startsWith("psfile=")) {
                        specialPsfile(out, text.substring(7));
                    }
                    break;
                case 'h':
                    if (text.startsWith("header=")) {
                        specialHeader(text.substring(7));
                    }
                    break;
                case '"':
                    out.append("gsave ");
                    out.append(text.substring(1));
                    out.append("grestore\n");
                    break;
                case '!':
                    try {
                        hm.add(text.substring(1), "!");
                    } catch (IOException e) {
                        throw new GeneralException(e);
                    }
                    break;
                default:
            // ignored on purpose
            }
        }
        return null;
    }
}
