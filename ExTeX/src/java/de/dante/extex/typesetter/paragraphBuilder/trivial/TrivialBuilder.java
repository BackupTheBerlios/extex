/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.paragraphBuilder.trivial;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.AfterMathNode;
import de.dante.extex.interpreter.type.node.BeforeMathNode;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.DiscretionaryNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.KernNode;
import de.dante.extex.interpreter.type.node.PenaltyNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
import de.dante.extex.logging.LogFormatter;
import de.dante.extex.typesetter.Discartable;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeIterator;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.FixedParagraphShape;
import de.dante.extex.typesetter.paragraphBuilder.HangingParagraphShape;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class implements a trivial paragraph builder.
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class TrivialBuilder implements ParagraphBuilder, LogEnabled {

    /**
     * The constant <tt>DEVELOP</tt> contains a boolean indicating whether the
     * develop traces should be written.
     */
    private static final boolean DEVELOP = false;

    /**
     * The constant <tt>EJECT_PENALTY</tt> contains the penalty which forces
     * a line break. This is an equivalent to -&infin;.
     */
    private static final int EJECT_PENALTY = -10000;

    /**
     * The constant <tt>INF_BAD</tt> contains the value for infinite penalty.
     * This is an equivalent to &infin;.
     */
    private static final int INF_PENALTY = 10000;

    /**
     * The field <tt>fixedParshape</tt> contains the data object used to
     * transport the fixed paragraph shape to the appropriate places. The values
     * stored in it will be overwritten whenever this object will be used for
     * the current paragraph.
     */
    private FixedParagraphShape fixedParshape = new FixedParagraphShape(
            Dimen.ZERO_PT);

    /**
     * The field <tt>hangingParshape</tt> contains the data object used to
     * transport the hanging paragraph shape to the appropriate places. The
     * values stored in it will be overwritten whenever this object will be
     * used for the current paragraph.
     */
    private HangingParagraphShape hangingParshape = new HangingParagraphShape(
            0, Dimen.ZERO_PT, Dimen.ZERO_PT);

    /**
     * The field <tt>logger</tt> contains the logger to be used.
     * This field is initialized from the framework by invoking the appropriate
     * setter.
     */
    private Logger logger = null;

    /**
     * The field <tt>options</tt> contains the reference to the options object.
     */
    private TypesetterOptions options = null;

    /**
     * The field <tt>parshape</tt> contains the paragraph shape specification.
     * This field is initialized at the beginning of the line breaking if it is
     * <code>null</code>. At the end of the line breaking it is reset to
     * <code>null</code>.
     */
    private ParagraphShape parshape = null;

    /**
     * The field <tt>tracer</tt> contains the logger used for tracing.
     */
    private Logger tracer = null;

    /**
     * Creates a new object.
     */
    public TrivialBuilder() {

        super();

        if (DEVELOP) {
            tracer = Logger.getLogger(ParagraphBuilder.class.getName());
            tracer.setUseParentHandlers(false);
            Handler handler = new ConsoleHandler();
            handler.setFormatter(new LogFormatter());
            handler.setLevel(Level.ALL);
            tracer.addHandler(handler);
            tracer.setLevel(Level.ALL);
        }
    }

    /**
     * ...
     *
     * @param start the index of the first node to consider
     * @param len the length of nodes
     * @param nodes the node list to take the nodes from
     * @param hlist the target list to put the nodes into
     * @param width the target width
     * @param accumulator an accumulator for the glue
     * @param height the accumulator for te height
     * @param depth the accuulator for the depth
     *
     * @return the index of the first node after the ones already processed
     */
    private int breakLine(final int start, final int len,
            final HorizontalListNode nodes, final HorizontalListNode hlist,
            final Dimen width, final Glue accumulator, final Dimen height,
            final Dimen depth) {

        Node node = nodes.get(start);
        hlist.add(node);
        node.addWidthTo(accumulator);
        int i = start + 1;
        Glue w = new Glue(0);

        while (i < len) {

            int point = findNextBreakPoint(nodes, i, w);
            if (w.gt(width)) {
                if (i == start + 1) {
                    //avoid infinite loop and accept overful box
                    i = saveNodes(nodes, i, point, hlist, accumulator, height,
                            depth);
                }
                return discartNodes(i, len, nodes);
            }

            i = saveNodes(nodes, i, point, hlist, accumulator, height, depth);
        }

        return i;
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#build(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode)
     */
    public NodeList build(final HorizontalListNode nodes) {

        if (options.getCountOption("tracingparagraphs").getValue() > 0) {
            tracer = logger;
        }

        if (DEVELOP) {
            tracer.info("\nbreaking paragraph: " + nodes.toText());
        }

        FixedGlue leftskip = options.getGlueOption("leftskip");
        FixedGlue rightskip = options.getGlueOption("rightskip");

        prepareParshape();

        // remove final node if it is glue; [TTB p99--100]
        int lastNodeIndex = nodes.size() - 1;
        Node node = (lastNodeIndex < 0 ? null : nodes.get(lastNodeIndex));
        if (node instanceof GlueNode) {
            nodes.remove(lastNodeIndex);
        }

        // [TTB p100]
        nodes.add(new PenaltyNode(INF_PENALTY));
        nodes.add(new GlueNode(options.getGlueOption("parfillskip")));
        nodes.add(new PenaltyNode(EJECT_PENALTY));

        VerticalListNode vlist = new VerticalListNode();
        int len = nodes.size();
        int i = 0;
        int line = 0;
        Dimen wd = new Dimen();
        Dimen adjustLeftRight = new Dimen(leftskip.getLength());
        adjustLeftRight.add(rightskip.getLength());
        FixedGlue baselineskip = options.getGlueOption("baselineskip");
        FixedGlue lineskip = options.getGlueOption("lineskip");
        FixedDimen lineskiplimit = options.getDimenOption("lineskiplimit");
        Glue accumulator = new Glue(0);

        while (i < len) {
            accumulator.set(Dimen.ZERO_PT);
            wd.set(parshape.getRight(line));
            wd.subtract(parshape.getLeft(line));
            Dimen width = new Dimen(wd);
            wd.subtract(adjustLeftRight);
            HorizontalListNode hlist = new HorizontalListNode();

            hlist.addSkip(leftskip);
            accumulator.add(leftskip);
            i = breakLine(i, len, nodes, hlist, wd, accumulator, hlist
                    .getHeight(), hlist.getDepth());
            hlist.addSkip(rightskip);
            accumulator.add(rightskip);

            spread(hlist, width, accumulator);

            addLine(vlist, hlist, baselineskip, lineskip, lineskiplimit);
            line++;
        }

        options.setParshape(null);
        return vlist;
    }

    /**
     * Add a new line to a vlist.
     * Ensure that a minimum distance between the lines exists. Usually the
     * distance <tt>\baselineskip</tt> between the lines is desirable. For this
     * purpose the depth of the previous line and the height of the current line
     * is subtracted. If the remaining distance is less than
     * <tt>\lineskiplimit</tt> then the value of <tt>\lineskip</tt> is used
     * instead.
     *
     * @param vlist the target list
     * @param hlist the line to add
     * @param baselineskip the parameter \baselineskip
     * @param lineskip the parameter \lineskip
     * @param lineskiplimit the parameter \lineskiplimit
     */
    private void addLine(final VerticalListNode vlist,
            final HorizontalListNode hlist, final FixedGlue baselineskip,
            final FixedGlue lineskip, final FixedDimen lineskiplimit) {

        int end = vlist.size() - 1;

        Glue g = new Glue(baselineskip);
        g.subtract(hlist.getHeight());
        if (end >= 0) {
            g.subtract(vlist.get(end).getDepth());
        }
        vlist.add(new GlueNode(g.lt(lineskiplimit) ? lineskip : g));

        vlist.add(hlist);
    }

    /**
     * Skip over any discartable nodes and return the index of the next
     * non-discartable node.
     *
     * @param start the index to start at
     * @param len the length of the node list
     * @param nodes the node list to take into account
     *
     * @return the index of the next non-discartable node
     */
    private int discartNodes(final int start, final int len,
            final NodeList nodes) {

        int i = start;
        while (i < len && nodes.get(i) instanceof Discartable) {
            i++;
        }
        return i;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * Determine the index of the next break point.
     *
     * @param nodes the list of nodes to consider
     * @param start the initial index
     * @param width an accumulator for the width
     *
     * @return the index of the next break point or the index of the element
     *  past the end of the list if none is found
     */
    private int findNextBreakPoint(final NodeList nodes, final int start,
            final Glue width) {

        Node node;
        boolean math = false;
        int len = nodes.size();
        int i = start;

        while (i < len && nodes.get(i) instanceof Discartable) {
            nodes.get(i).addWidthTo(width);
            i++;
        }

        for (; i < len; i++) {
            node = nodes.get(i);

            if (node instanceof CharNode) {
                //node.addWidthTo(width);
                //continue;
            } else if (node instanceof GlueNode
                    && !(nodes.get(i - 1) instanceof Discartable)) {

                return i;
            } else if (node instanceof KernNode && !math
                    && nodes.get(i + 1) instanceof GlueNode) {

                node.addWidthTo(width);
                return i;
            } else if (node instanceof PenaltyNode) {
                int penalty = (int) ((PenaltyNode) node).getPenalty();
                if (penalty < INF_PENALTY) {
                    node.addWidthTo(width);
                    return i;
                }
            } else if (node instanceof BeforeMathNode) {
                math = true;
            } else if (node instanceof AfterMathNode) {
                if (nodes.get(i + 1) instanceof GlueNode) {

                    node.addWidthTo(width);
                    return i;
                }
                math = false;

            } else if (node instanceof DiscretionaryNode) {

                //breakList.add(new BreakPoint(i, w, wd,
                //        (((DiscretionaryNode) node).getPreBreak().length() != 0
                //                ? hyphenpenalty
                //                : exhyphenpenalty)));
                //node.addWidthTo(width);
                return i;
            }
            node.addWidthTo(width);
        }
        return len;
    }

    /**
     * Initializes the field <tt>parshape</tt> if not set already.
     * For this purpose the options are considered.
     */
    private void prepareParshape() {

        parshape = options.getParshape();

        if (parshape == null) {
            int hangafter = (int) options.getCountOption("hangafter")
                    .getValue();

            if (hangafter != 0) {
                hangingParshape.setHangafter(hangafter);
                hangingParshape.setHangindent(options
                        .getDimenOption("hangindent"));
                hangingParshape.setHsize(options.getDimenOption("hsize"));
                parshape = hangingParshape;
            } else {
                fixedParshape.setHsize(options.getDimenOption("hsize"));
                parshape = fixedParshape;
            }
        }
    }

    /**
     * Copy nodes from one list into another.
     *
     * @param nodes the list of nodes to consider
     * @param start the initial index
     * @param end the index of the element after the ones to save
     * @param hlist the destination list
     * @param accumulator the accumulator for the glue of the saved nodes
     * @param height the accumulator for te height
     * @param depth the accuulator for the depth
     *
     * @return the index of the first node which has not been copied
     */
    private int saveNodes(final HorizontalListNode nodes, final int start,
            final int end, final HorizontalListNode hlist,
            final Glue accumulator, final Dimen height, final Dimen depth) {

        Node node;
        for (int i = start; i < end; i++) {
            node = nodes.get(i);
            if (!(node instanceof PenaltyNode)) {
                hlist.add(node);
                node.addWidthTo(accumulator);
            }
            if (height.lt(node.getHeight())) {
                height.set(node.getHeight());
            }
            if (depth.lt(node.getDepth())) {
                depth.set(node.getDepth());
            }
        }
        return end;
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#setOptions(
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void setOptions(final TypesetterOptions options) {

        this.options = options;
    }

    /**
     * Adjust the width of the current line.
     *
     * @param hlist the target list to put the nodes into
     * @param targetWidth the target width to which the hlist should be
     *  adjusted
     * @param w the accumulated width of the hlist
     */
    private void spread(final HorizontalListNode hlist,
            final FixedDimen targetWidth, final Glue w) {

        Dimen width = w.getLength();
        FixedGlueComponent component = (width.lt(targetWidth)
                ? w.getStretch()
                : w.getShrink());

        Dimen wd = new Dimen(targetWidth.getValue() - width.getValue());
        NodeIterator it = hlist.iterator();
        while (it.hasNext()) {
            it.next().spread(wd, component);
        }

        hlist.setWidth(targetWidth);
    }

}