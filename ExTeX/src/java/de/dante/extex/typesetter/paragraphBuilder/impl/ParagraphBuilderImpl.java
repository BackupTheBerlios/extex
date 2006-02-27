/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.paragraphBuilder.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.glue.WideGlue;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.typesetter.Badness;
import de.dante.extex.typesetter.Discardable;
import de.dante.extex.typesetter.HyphenationEnabled;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.hyphenator.Hyphenator;
import de.dante.extex.typesetter.paragraphBuilder.FixedParagraphShape;
import de.dante.extex.typesetter.paragraphBuilder.HangingParagraphShape;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.KernNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides a paragraph builder.
 *
 * TODO gene: documentation incomplete.
 *
 * <doc name="emergencystretch" type="register">
 * <h3>The Parameter <tt>\emergencystretch</tt></h3>
 *
 * </doc>
 *
 * <doc name="exhyphenpenalty" type="register">
 * <h3>The Parameter <tt>\exhyphenpenalty</tt></h3>
 *
 * </doc>
 *
 * <doc name="hangafter" type="register">
 * <h3>The Parameter <tt>\hangafter</tt></h3>
 *
 * </doc>
 *
 * <doc name="hangindent" type="register">
 * <h3>The Parameter <tt>\hangindent</tt></h3>
 *
 * </doc>
 *
 * <doc name="hsize" type="register">
 * <h3>The Parameter <tt>\hsize</tt></h3>
 *  The parameter <tt>\hsize</tt> contains the horizotal size of the paragraph
 *  to be build.
 *  See also \parshape, \hangindent, and \hangafter.
 * </doc>
 *
 * <doc name="hyphenpenalty" type="register">
 * <h3>The Parameter <tt>\hyphenpenalty</tt></h3>
 *
 * </doc>
 *
 * <doc name="leftskip" type="register">
 * <h3>The Parameter <tt>\leftskip</tt></h3>
 *
 * <p>
 *  The parameter <tt>\leftskip</tt> contains the glue which is inserted at the
 *  left side of each line in the paragraph. The default is 0&nbsp;pt.
 * </p>
 * <p>
 *  This parameter can be used to flash the line to the left side or center the
 *  line. Those effects can be achieved in combination with the parameter
 *  <tt>\rightskip</tt>.
 * </p>
 * </doc>
 *
 * <doc name="parfillskip" type="register">
 * <h3>The Parameter <tt>\parfillskip</tt></h3>
 *  The parameter <tt>\parfillskip</tt> contains the glue which is added at the
 *  end of each paragraph.
 * </doc>
 *
 * <doc name="parskip" type="register">
 * <h3>The Parameter <tt>\parskip</tt></h3>
 *  The parameter <tt>\parskip</tt> contains the glue which is added to the
 *  vertical list before the beginnng of each paragraph.
 * </doc>
 *
 * <doc name="pretolerance" type="register">
 * <h3>The Parameter <tt>\pretolerance</tt></h3>
 *
 * </doc>
 *
 * <doc name="rightskip" type="register">
 * <h3>The Parameter <tt>\rightskip</tt></h3>
 * <p>
 *  The parameter <tt>\rightskip</tt> contains the glue which is inserted at the
 *  right side of each line in the paragraph. The defult is 0&nbsp;pt.
 * </p>
 * <p>
 *  This parameter can be used to flash the line to the right side or center the
 *  line. Those effects can be achieved in combination with the parameter
 *  <tt>\leftskip</tt>.
 * </p>
 * </doc>
 *
 * <doc name="tolerance" type="register">
 * <h3>The Parameter <tt>\tolerance</tt></h3>
 *
 * </doc>
 *
 * <doc name="tracingparagraphs" type="register">
 * <h3>The Parameter <tt>\tracingparagraphs</tt></h3>
 *
 * </doc>
 *
 *
 * <h3>Extension</h3>
 *
 * Treat segments of a paragraph separated by forced breaks separately.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.30 $
 */
public class ParagraphBuilderImpl
        implements
            ParagraphBuilder,
            LogEnabled,
            HyphenationEnabled {

    /**
     * The constant <tt>COMPLETE</tt> contains the indicator that the
     * implementation is complete.
     */
    private static final boolean COMPLETE = true;

    /**
     * The constant <tt>DEVELOP</tt> contains a boolean indicating whether the
     * develop traces should be written.
     */
    private static final boolean DEVELOP = true;

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
     * The field <tt>hyphenator</tt> contains the class to use for hyphenating
     * lines. This means that additional discretionary nodes are inserted.
     */
    private Hyphenator hyphenator = null;

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
    public ParagraphBuilderImpl() {

        super();

        if (DEVELOP) {
            tracer = Logger.getLogger("");
            tracer.setUseParentHandlers(false);
            Handler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            handler.setFormatter(new LogFormatter());
            tracer.addHandler(handler);
            tracer.setLevel(Level.ALL);
        }
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#build(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode)
     */
    public NodeList build(final HorizontalListNode nodes) {

        //TODO gene: split into smaller methods

        if (!COMPLETE) {
            return nodes;
        }

        if (options.getCountOption("tracingparagraphs").getValue() > 0) {
            tracer = logger;
        }

        int hyphenpenalty = (int) options.getCountOption("hyphenpenalty")
                .getValue();
        int exhyphenpenalty = (int) options.getCountOption("exhyphenpenalty")
                .getValue();
        FixedGlue leftskip = options.getGlueOption("leftskip");
        FixedGlue rightskip = options.getGlueOption("rightskip");

        prepareParshape();

        // remove final node if it is glue; [TTB p99--100]
        if (!nodes.empty() && nodes.get(nodes.size() - 1) instanceof GlueNode) {
            nodes.remove(nodes.size() - 1);
        }

        // [TTB p100]
        nodes.add(new PenaltyNode(Badness.INF_PENALTY));
        nodes.add(new GlueNode(options.getGlueOption("parfillskip"), true));
        nodes.add(new PenaltyNode(Badness.EJECT_PENALTY));

        NodeList nl;
        nl = pass1(nodes, hyphenpenalty, exhyphenpenalty, leftskip, rightskip);
        if (nl != null) {
            return nl;
        }

        if (hyphenator != null) {
            if (tracer != null) {
                tracer.log(Level.FINE, "@hyphenating\n");
            }
            hyphenator.hyphenate(nodes);
        }

        if (tracer != null) {
            tracer.log(Level.FINE, "@secondpass\n");
            for (int i = 0; i < nodes.size(); i++) {
                tracer.log(Level.FINE, Integer.toString(i) + "\t"
                        + nodes.get(i).toString() + "\n");
            }
        }
        int tolerance = (int) options.getCountOption("tolerance").getValue();
        BreakPoint[] breakPoints = makeBreakPoints(nodes, hyphenpenalty,
                exhyphenpenalty);

        if (tracer != null) {
            for (int i = 0; i < breakPoints.length; i++) {
                tracer.log(Level.FINE, breakPoints[i].toString() + "\n");
            }
        }

        Breaks breaks = findOptimalBreakPoints(breakPoints, 0, tolerance, 0, 0,
                leftskip, rightskip, Dimen.ZERO_PT);
        if (breaks != null) {
            options.setParshape(null);
            return splitNodeList(nodes, breaks, leftskip, rightskip);
        }

        FixedDimen emergencystretch = options
                .getDimenOption("emergencystretch");
        if (emergencystretch.getValue() > 0) {
            if (tracer != null) {
                tracer.log(Level.FINE, "@thirdpass\n");
            }
            breaks = findOptimalBreakPoints(breakPoints, 0, tolerance, 0, 0,
                    leftskip, rightskip, emergencystretch);
            if (breaks != null) {
                options.setParshape(null);
                return splitNodeList(nodes, breaks, leftskip, rightskip);
            }
        }

        options.setParshape(null);
        return nodes;
    }

    /**
     * Collect the active break points.
     *
     * @param breakPoint the list of possible break points
     * @param depth the number of active break points
     * @param penalty the accumulated penalty
     *
     * @return a breaks container
     */
    private Breaks collect(final BreakPoint[] breakPoint, final int depth,
            final int penalty) {

        int[] a = new int[depth + 1];
        int xi = 0;
        for (int i = 0; i < breakPoint.length; i++) {
            if (breakPoint[i].isActive()) {
                a[xi++] = breakPoint[i].getPosition();
            }
        }
        return new Breaks(penalty, a);
    }

    /**
     * Determine the demerits.
     *
     * @param breakPoint the list of possible break points
     * @param pi the index of the end point of the current line
     * @param leftskip the skip to be included at the left end of each line
     * @param rightskip the skip to be included at the right end of each line
     * @param lineWidth the size of the current line
     * @param threshold the threshold
     *
     * @return the demerits
     *
     * @see "TTP [851]"
     */
    private int computeDemerits(final BreakPoint[] breakPoint, final int pi,
            final FixedGlue leftskip, final FixedGlue rightskip,
            final FixedGlue lineWidth, final int threshold) {

        Glue width = new Glue(breakPoint[pi].getWidth());
        for (int i = pi - 1; i > 0 && !breakPoint[i].isActive(); i--) {
            width.add(breakPoint[i].getWidth());
            width.add(breakPoint[i].getPointWidth());
        }

        int badness = 0;
        Fitness fit;
        if (width.getStretch().getOrder() != 0) {
            fit = Fitness.DECENT;
        } else {
            long line = lineWidth.getLength().getValue();
            long shortfall = line - width.getLength().getValue();

            if (shortfall > 7230584) {

            } else if (shortfall > 0) { //TTP [852]
                if (line < 1663497) {
                    badness = Badness.INF_PENALTY;
                    fit = Fitness.VERY_LOOSE;
                } else {
                    badness = Badness.badness(shortfall, line);
                    fit = (badness < 12 ? Fitness.DECENT : badness < 99
                            ? Fitness.LOOSE
                            : Fitness.VERY_LOOSE);
                }
            } else { //TTP [853]
                long shrink = lineWidth.getShrink().getValue();
                if (-shortfall > shrink) {
                    badness = Badness.INF_PENALTY + 1;
                } else {
                    badness = Badness.badness(-shortfall, shrink);
                    fit = (badness <= 12 ? Fitness.DECENT : Fitness.TIGHT);
                }
            }
        }

        if (badness > threshold) {
            return Badness.INF_PENALTY + 1;
        }

        return badness;
    }

    /**
     * Skip over any discardable nodes and return the index of the next
     * non-discardable node.
     *
     * @param start the index to start at
     * @param len the length of the node list
     * @param nodes the node list to take into account
     * @param wd the  accumulator for the width of the discarded nodes
     *
     * @return the index of the next non-discardable node
     */
    private int discartNodes(final int start, final int len,
            final NodeList nodes, final WideGlue wd) {

        int i = start;
        while (++i < len && nodes.get(i) instanceof Discardable) {
            wd.add(nodes.get(i).getWidth());
        }
        return i - 1;
    }

    /**
     * Setter for hyphenator.
     *
     * @param theHyphenator the hyphenator to set
     */
    public void enableHyphenation(final Hyphenator theHyphenator) {

        this.hyphenator = theHyphenator;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * Compute a optimal list of break positions.
     *
     * @param breakPoint the list of possible break points
     * @param line the starting line number
     * @param threshold the threshold for the penalties of a single line
     * @param depth the current depth of recursion. This is identical to the
     *  length of the list of break points
     * @param pointIndex the index of the point
     * @param leftskip the skip for the left side
     * @param rightskip the skip for the right side
     * @param emergencystretch the emergency stretch to add
     *
     * @return the container with the breaks or <code>null</code> if none is
     *  found.
     */
    private Breaks findOptimalBreakPoints(final BreakPoint[] breakPoint,
            final int line, final int threshold, final int depth,
            final int pointIndex, final FixedGlue leftskip,
            final FixedGlue rightskip, final FixedDimen emergencystretch) {

        if (tracer != null) {
            tracer.log(Level.FINE,
                    "........................................................."
                            .substring(0, depth)
                            + " +++ " + Integer.toString(pointIndex) + "\n");
            for (int i = 0; i < pointIndex; i++) {
                if (breakPoint[i].isActive()) {
                    tracer.log(Level.FINE, " " + breakPoint[i].getPosition()
                            + " [" + breakPoint[i].getPenalty() + "]");
                }
            }
        }
        Breaks b = null;
        int pen = 0;
        Glue lineWidth = new Glue(parshape.getLength(line));
        lineWidth.subtract(parshape.getIndent(line));

        for (int i = pointIndex; i < breakPoint.length; i++) {
            breakPoint[i].setActive();
            pen = computeDemerits(breakPoint, i, leftskip, rightskip,
                    lineWidth, threshold);
            if (pen <= Badness.EJECT_PENALTY || pen < threshold) {
                if (i + 1 < breakPoint.length) {
                    Breaks b2 = findOptimalBreakPoints(breakPoint, line + 1,
                            threshold, depth + 1, i + 1, leftskip, rightskip,
                            emergencystretch);
                    if (b2 != null
                            && (b == null || b.getPenalty() > b2.getPenalty())) {
                        b = b2;
                    }
                } else {
                    b = collect(breakPoint, depth, 9999); //TODO gene: provide accumulated penalty
                }
            }
            breakPoint[i].setPassive();
        }

        return b;
    }

    /**
     * Find all admissible break points.
     *
     * @param nodes the horizontal node list containing all nodes for the
     *  paragraph
     * @param hyphenpenalty penalty for a discretionary node with non-empty
     *  pre-text
     * @param exhyphenpenalty penalty for a discretionary node with empty
     *  pre-text
     *
     * @return a complete list of admissible break points
     */
    private BreakPoint[] makeBreakPoints(final HorizontalListNode nodes,
            final int hyphenpenalty, final int exhyphenpenalty) {

        int len = nodes.size();
        List breakList = new ArrayList(1 + len / 5); // size is a heuristic
        WideGlue w = new WideGlue();
        WideGlue wd = new WideGlue();
        boolean math = false;

        int i = 0;
        Node node = nodes.get(i);
        if (node instanceof GlueNode) {
            node.addWidthTo(w);
            i++;
        }

        for (; i < len; i++) {
            node = nodes.get(i);

            if (node instanceof CharNode) {
                node.addWidthTo(w);

            } else if (node instanceof GlueNode
                    && !(nodes.get(i - 1) instanceof Discardable)) {

                node.addWidthTo(wd);
                breakList.add(new BreakPoint(i, w, wd, 0));
                i = discartNodes(i, len, nodes, wd);
                w = new WideGlue();
                wd = new WideGlue();
            } else if (node instanceof KernNode && !math
                    && nodes.get(i + 1) instanceof GlueNode) {

                node.addWidthTo(wd);
                breakList.add(new BreakPoint(i, w, wd, 0));
                i = discartNodes(i, len, nodes, wd);
                w = new WideGlue();
                wd = new WideGlue();
            } else if (node instanceof BeforeMathNode) {
                math = true;
            } else if (node instanceof AfterMathNode) {
                if (nodes.get(i + 1) instanceof GlueNode) {

                    node.addWidthTo(wd);
                    breakList.add(new BreakPoint(i, w, wd, 0));
                    i = discartNodes(i, len, nodes, wd);
                    w = new WideGlue();
                    wd = new WideGlue();
                }
                math = false;
            } else if (node instanceof PenaltyNode) {
                int penalty = (int) ((PenaltyNode) node).getPenalty();
                if (penalty < Badness.INF_PENALTY) {

                    node.addWidthTo(wd);
                    breakList.add(new BreakPoint(i, w, wd, penalty));
                    i = discartNodes(i, len, nodes, wd);
                    w = new WideGlue();
                    wd = new WideGlue();
                }
            } else if (node instanceof DiscretionaryNode) {

                node.addWidthTo(wd);
                breakList.add(new BreakPoint(i, w, wd,
                        (((DiscretionaryNode) node).getPreBreak() != null
                                ? hyphenpenalty
                                : exhyphenpenalty)));
                i = discartNodes(i, len, nodes, wd);
                w = new WideGlue();
                wd = new WideGlue();
            } else {
                node.addWidthTo(w);
            }
        }

        BreakPoint[] breakPoints = new BreakPoint[breakList.size()];
        breakList.toArray(breakPoints);
        return breakPoints;
    }

    /**
     * Try paragraph building with the pass 1 algorithm: no hyphenations are
     * taken into account.
     *
     * @param nodes the node list to work on
     * @param hyphenpenalty the penalty for hyphenation
     * @param exhyphenpenalty the extra penalty subsequent hyphenation
     * @param leftskip the glue for the left side
     * @param rightskip the glue for the right side
     *
     * @return the resulting vertical node list or <code>null</code> if this
     *  attempt has not been successful
     */
    private NodeList pass1(final HorizontalListNode nodes,
            final int hyphenpenalty, final int exhyphenpenalty,
            final FixedGlue leftskip, final FixedGlue rightskip) {

        int pretolerance = (int) options.getCountOption("pretolerance")
                .getValue();
        if (pretolerance > 0) {
            if (tracer != null) {
                tracer.log(Level.FINE, "@firstpass\n");
            }
            BreakPoint[] breakPoints = makeBreakPoints(nodes, hyphenpenalty,
                    exhyphenpenalty);
            Breaks breaks = findOptimalBreakPoints(breakPoints, 0,
                    pretolerance, 0, 0, leftskip, rightskip, Dimen.ZERO_PT);
            if (breaks != null) {
                options.setParshape(null);
                return splitNodeList(nodes, breaks, leftskip, rightskip);
            }
        }
        return null;
    }

    /**
     * Initializes the field <tt>parshape</tt> if not set already.
     * For this purpose the options are considered.
     *
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
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#setOptions(
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void setOptions(final TypesetterOptions options) {

        this.options = options;
    }

    /**
     * Split the given hlist at the computed break points and construct a vlist
     * containing the lines.
     *
     * @param nodes the hlist to split
     * @param breaks the list of break positions
     * @param leftskip the skip for the left side
     * @param rightskip the skip for the right side
     *
     * @return a vlist with the lines
     */
    private NodeList splitNodeList(final NodeList nodes, final Breaks breaks,
            final FixedGlue leftskip, final FixedGlue rightskip) {

        VerticalListNode vlist = new VerticalListNode();
        int[] a = breaks.getPoints();
        int hi = 0;
        for (int i = 0; i < a.length; i++) {
            HorizontalListNode hlist = new HorizontalListNode();
            while (hi < a[i]) {
                hlist.add(nodes.get(hi++));
            }

            vlist.add(hlist);

            int max = (i + 1 < a.length ? a[i + 1] : nodes.size());
            // skip discartable items at end of line
            while (hi < max && nodes.get(hi) instanceof Discardable) {
                hi++;
            }
        }

        return vlist;
    }
}