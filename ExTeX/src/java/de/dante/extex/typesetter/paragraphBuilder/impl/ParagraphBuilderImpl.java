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
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.FixedParagraphShape;
import de.dante.extex.typesetter.paragraphBuilder.HangingParagraphShape;
import de.dante.extex.typesetter.paragraphBuilder.Hyphenator;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.configuration.Configuration;
import de.dante.util.framework.logger.LogEnabled;

/**
 * ...
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
 *
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
 *
 * </doc>
 *
 * <doc name="pretolerance" type="register">
 * <h3>The Parameter <tt>\pretolerance</tt></h3>
 *
 * </doc>
 *
 * <doc name="rightskip" type="register">
 * <h3>The Parameter <tt>\rightskip</tt></h3>
 *
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
 * Treat segments of a paragraph separated by forced breakes separately.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ParagraphBuilderImpl implements ParagraphBuilder, LogEnabled {

    private static final boolean COMPLETE = false;
    
    /**
     * The constant <tt>DEVELOP</tt> contains a boolean indicating whether the
     * develop traces should be written.
     */
    private static final boolean DEVELOP = true;

    /**
     * The constant <tt>EJECT_PENALTY</tt> contains the penalty which forces
     * a line break. This is an equivalent to -&infin;.
     */
    private static final int EJECT_PENALTY = -10000;

    /**
     * The constant <tt>INF_BAD</tt> contains the ...
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
     * The field <tt>hangingParshape</tt> contains  data object used to
     * transport the hanging paragraph shape to the appropriate places. The
     * values stored in it will be overwritten whenever this object will be
     * used for the current paragraph.
     */
    private HangingParagraphShape hangingParshape = new HangingParagraphShape(
            0, Dimen.ZERO_PT, Dimen.ZERO_PT);

    /**
     * The field <tt>hyphenator</tt> contains the class to use for hyphenating
     * lines. This means that additional discretionay nodes are inserted.
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
     *
     * @param config the configuration
     */
    public ParagraphBuilderImpl(final Configuration config) {

        super();
    }

    /**
     * Compute badness.
     *
     * @param t ... given t >= 0
     * @param s ...
     *
     * @return ...
     *
     * @see "TTP [108]"
     */
    int badness(final long t, final long s) {

        if (t == 0) {
            return 0;
        } else if (s <= 0) {
            return INF_PENALTY;
        }

        long r; // approximation to a t/s, where a^3 approx 100 * 2^18}

        if (t <= 7230584) {
            r = (t * 297) / s; // 297^3=99.94 × 2^18
        } else if (s >= 1663497) {
            r = t / (s / 297);
        } else {
            r = t;
        }
        return (r > 1290 ? INF_PENALTY //1290^3<2^31<129^3
                : (int) ((r * r * r + 0x20000) / 0x40000));
        // that was r^3/2^18, rounded to the nearest integer
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#build(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode)
     */
    public NodeList build(final HorizontalListNode nodes) {

        if (!COMPLETE) {
            return nodes;
        }

        tracer = (options.getCountOption("tracingparagraphs").getValue() > 0
                ? logger
                : null);

        if (DEVELOP) {
            tracer = Logger.getLogger("");
            Handler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            handler.setFormatter(new LogFormatter());
            tracer.addHandler(handler);
            tracer.setLevel(Level.ALL);
        }

        int hyphenpenalty = (int) options.getCountOption("hyphenpenalty")
                .getValue();
        int exhyphenpenalty = (int) options.getCountOption("exhyphenpenalty")
                .getValue();
        FixedGlue leftskip = options.getGlueOption("leftskip");
        FixedGlue rightskip = options.getGlueOption("rightskip");

        prepareParshape();

        // remove final node if it is glue; [TTB p99--100]
        Node node = (nodes.empty() ? null : nodes.get(nodes.size() - 1));
        if (node != null && node instanceof GlueNode) {
            nodes.remove(nodes.size() - 1);
        }

        // [TTB p100]
        nodes.add(new PenaltyNode(INF_PENALTY));
        nodes.add(new GlueNode(options.getGlueOption("parfillskip")));
        nodes.add(new PenaltyNode(EJECT_PENALTY));

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
                parshape = null;
                return splitNodeList(nodes, breaks, leftskip, rightskip);
            }
        }

        if (hyphenator != null) {
            if (tracer != null) {
                tracer.log(Level.FINE, "@hyphenating\n");
            }
            hyphenator.hyphenate(nodes);
        }

        if (tracer != null) {
            tracer.log(Level.FINE, "@secondpass\n");
        }
        int tolerance = (int) options.getCountOption("tolerance").getValue();
        BreakPoint[] breakPoints = makeBreakPoints(nodes, hyphenpenalty,
                exhyphenpenalty);
        Breaks breaks = findOptimalBreakPoints(breakPoints, 0, tolerance, 0, 0,
                leftskip, rightskip, Dimen.ZERO_PT);
        if (breaks != null) {
            parshape = null;
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
                parshape = null;
                return splitNodeList(nodes, breaks, leftskip, rightskip);
            }
        }

        parshape = null;
        return nodes;
    }

    /**
     * ...
     *
     * @param breakPoint the list of possible break points
     * @param depth ...
     *
     * @return a breaks container
     */
    private Breaks collect(final BreakPoint[] breakPoint, final int depth) {

        int[] a = new int[depth + 1];
        int xi = 0;
        for (int i = 0; i < breakPoint.length; i++) {
            if (breakPoint[i].isActive()) {
                a[xi++] = breakPoint[i].getPosition();
            }
        }
        return new Breaks(999, a); //TODO
    }

    /**
     * ...
     *
     * @param breakPoint the list of possible break points
     * @param pi the index of the end point of the current line
     * @param leftskip the skip to be included at the left end of each line
     * @param rightskip the skip to be included at the right end of each line
     * @param lineWidth the size of the current line
     * @param threshold TODO
     *
     * @return ...
     */
    private int computeDemerits(final BreakPoint[] breakPoint, final int pi,
            final FixedGlue leftskip, final FixedGlue rightskip,
            final FixedGlue lineWidth, int threshold) {

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
                    badness = INF_PENALTY;
                    fit = Fitness.VERY_LOOSE;
                } else {
                    badness = badness(shortfall, line);
                    fit = (badness < 12 ? Fitness.DECENT : badness < 99
                            ? Fitness.LOOSE
                            : Fitness.VERY_LOOSE);
                }
            } else { //TTP [853]
                long shrink = lineWidth.getShrink().getValue();
                if (-shortfall > shrink) {
                    badness = INF_PENALTY + 1;
                } else {
                    badness = badness(-shortfall, shrink);
                    fit = (badness <= 12 ? Fitness.DECENT : Fitness.TIGHT);
                }
            }
        }

        if (badness > threshold) {
            return INF_PENALTY + 1;
        }

        return badness;
    }

    /**
     * ...
     *
     * @param start ...
     * @param len ...
     * @param nodes ...
     * @param wd ...
     *
     * @return ...
     */
    private int discartNodes(final int start, final int len,
            final NodeList nodes, final Glue wd) {

        int i = start;
        while (++i < len && nodes.get(i) instanceof Discartable) {
            wd.add(nodes.get(i).getWidth());
        }
        return i - 1;
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
     * @param breakPoint the list of possible break points
     * @param line the starting line number
     * @param threshold the threshold for the penalties of a single line
     * @param depth ...
     * @param pointIndex the index of the point
     * @param leftskip the skip for the left side
     * @param rightskip the skip for the right side
     * @param emergencystretch TODO
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
                    ".................................................."
                            .substring(0, depth)
                            + " +++ " + Integer.toString(pointIndex) + "\n");
        }
        Breaks b = null;
        int pen = 0;
        Glue lineWidth = new Glue(parshape.getRight(line));
        lineWidth.subtract(parshape.getLeft(line));

        for (int i = pointIndex; i < breakPoint.length; i++) {
            breakPoint[i].setActive();
            pen = computeDemerits(breakPoint, i, leftskip, rightskip,
                    lineWidth, threshold);
            if (pen <= EJECT_PENALTY || pen <= threshold) {
                if (i + 1 < breakPoint.length) {
                    Breaks b2 = findOptimalBreakPoints(breakPoint, line + 1,
                            threshold, depth + 1, i + 1, leftskip, rightskip, emergencystretch);
                    if (b2 != null
                            && (b == null || b.getPenalty() > b2.getPenalty())) {
                        b = b2;
                    }
                } else {
                    b = collect(breakPoint, depth);
                }
            }
            breakPoint[i].setPassive();
        }

        return b;
    }

    /**
     * ...
     * @param nodes the horizontal node list containing all nodes for the
     *   paragraph
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
        Glue w = new Glue(0);
        Glue wd = new Glue(0);
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
                    && !(nodes.get(i - 1) instanceof Discartable)) {

                node.addWidthTo(wd);
                breakList.add(new BreakPoint(i, w, wd, 0));
                i = discartNodes(i, len, nodes, wd);
                w = new Glue(0);
                wd = new Glue(0);
            } else if (node instanceof KernNode && !math
                    && nodes.get(i + 1) instanceof GlueNode) {

                node.addWidthTo(wd);
                breakList.add(new BreakPoint(i, w, wd, 0));
                i = discartNodes(i, len, nodes, wd);
                w = new Glue(0);
                wd = new Glue(0);
            } else if (node instanceof BeforeMathNode) {
                math = true;
            } else if (node instanceof AfterMathNode) {
                if (nodes.get(i + 1) instanceof GlueNode) {

                    node.addWidthTo(wd);
                    breakList.add(new BreakPoint(i, w, wd, 0));
                    i = discartNodes(i, len, nodes, wd);
                    w = new Glue(0);
                    wd = new Glue(0);
                }
                math = false;
            } else if (node instanceof PenaltyNode) {
                int penalty = (int) ((PenaltyNode) node).getPenalty();
                if (penalty < INF_PENALTY) {

                    node.addWidthTo(wd);
                    breakList.add(new BreakPoint(i, w, wd, penalty));
                    i = discartNodes(i, len, nodes, wd);
                    w = new Glue(0);
                    wd = new Glue(0);
                }
            } else if (node instanceof DiscretionaryNode) {

                node.addWidthTo(wd);
                breakList.add(new BreakPoint(i, w, wd,
                        (((DiscretionaryNode) node).getPreBreak().length() != 0
                                ? hyphenpenalty
                                : exhyphenpenalty)));
                i = discartNodes(i, len, nodes, wd);
                w = new Glue(0);
                wd = new Glue(0);
            } else {
                node.addWidthTo(w);
            }
        }

        BreakPoint[] breakArray = new BreakPoint[breakList.size()];
        breakList.toArray(breakArray);

        return breakArray;
    }

    /**
     * Initializes the field <tt>parshape</tt> if not set already.
     * For this purpose the options are considered.
     *
     */
    private void prepareParshape() {

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
     * Setter for hyphenator.
     *
     * @param hyphenator the hyphenator to set.
     */
    public void setHyphenator(final Hyphenator hyphenator) {

        this.hyphenator = hyphenator;
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#setOptions(
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void setOptions(final TypesetterOptions options) {

        this.options = options;
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#setParshape(
     *      de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
     */
    public void setParshape(final ParagraphShape parshape) {

        this.parshape = parshape;
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
            while (hi < max && nodes.get(hi) instanceof Discartable) {
                hi++;
            }
        }

        return vlist;
    }
}