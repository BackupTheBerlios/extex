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
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.AfterMathNode;
import de.dante.extex.interpreter.type.node.BeforeMathNode;
import de.dante.extex.interpreter.type.node.DiscretionaryNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.KernNode;
import de.dante.extex.interpreter.type.node.PenaltyNode;
import de.dante.extex.interpreter.type.node.VerticalListNode;
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
 * @version $Revision: 1.3 $
 */
public class ParagraphBuilderImpl implements ParagraphBuilder, LogEnabled {

    /**
     * The constant <tt>EJECT_PENALTY</tt> contains the penalty which forces
     * a line break. This is an equivalent to -&infin;.
     */
    private static final int EJECT_PENALTY = -10000;

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
     * The constant <tt>INF_BAD</tt> contains the ...
     * This is an equivalent to &infin;.
     */
    private static final int INF_PENALTY = 10000;

    /**
     * The field <tt>hyphenator</tt> contains the ...
     */
    private Hyphenator hyphenator = null;

    /**
     * The field <tt>logger</tt> contains the ...
     */
    private Logger logger = null;

    /**
     * The field <tt>options</tt> contains the reference to the options object.
     */
    private TypesetterOptions options = null;

    /**
     * The field <tt>parshape</tt> contains the ...
     */
    private ParagraphShape parshape = null;

    /**
     * Creates a new object.
     *
     * @param config the configuration
     */
    public ParagraphBuilderImpl(final Configuration config) {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder#build(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode)
     */
    public NodeList build(final HorizontalListNode nodes) {

        /*
        Logger tracer = (options.getCountOption("tracingparagraphs").getValue() > 0
                ? logger
                : null);

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
                tracer.log(Level.FINE, "@@pass one\n");
            }
            BreakPoint[] breakPoints = makeBreakPoints(nodes, hyphenpenalty,
                    exhyphenpenalty, tracer);
            //paragraph breaking without additional hyphenation points
            Breaks breaks = findOptimalBreakPoints(breakPoints, 0,
                    pretolerance, 0, 0);
            if (breaks != null) {
                return splitNodeList(nodes, breaks);
            }
        }

        // insert optional hyphenation positions
        if (hyphenator != null) {
            if (tracer != null) {
                tracer.log(Level.FINE, "@@hyphenating\n");
            }
            hyphenator.hyphenate(nodes);
        }

        if (tracer != null) {
            tracer.log(Level.FINE, "@@pass two\n");
        }
        int tolerance = (int) options.getCountOption("tolerance").getValue();
        BreakPoint[] breakPoints = makeBreakPoints(nodes, hyphenpenalty,
                exhyphenpenalty, tracer);
        System.err.println("--- " + Integer.toString(breakPoints.length));
        //paragraph breaking second pass
        Breaks breaks = findOptimalBreakPoints(breakPoints, 0, tolerance, 0, 0);
        if (breaks != null) {
            return splitNodeList(nodes, breaks);
        }

        if (tracer != null) {
            tracer.log(Level.FINE, "@@pass three\n");
        }
        //TODO: paragraph breaking third pass
        FixedDimen emergencystretch = options
                .getDimenOption("emergencystretch");

        parshape = null;
*/
        return nodes;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger logger) {

        this.logger = logger;
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
     * Setter for options.
     *
     * @param options the options to set.
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
     * @param pi the index of the point
     *
     * @return ...
     */
    private int computePenalty(final BreakPoint[] breakPoint, final int pi) {

        int index = (pi == 0 ? 0 : breakPoint[pi - 1].getPosition());

        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * ...
     *
     * @param breakPoint the list of possible break points
     * @param line the starting line number
     * @param threshold the threshold for the penalties of a single line
     * @param depth ...
     * @param pi the index of the point
     *
     * @return the container with the breaks or <code>null</code> if none is
     *  found.
     */
    private Breaks findOptimalBreakPoints(final BreakPoint[] breakPoint,
            final int line, final int threshold, final int depth, final int pi) {

        System.err.println("........................................"
                .substring(0, depth)
                + " +++ " + Integer.toString(pi));
        Breaks b = null;
        int pen = 0;

        for (int i = pi; i < breakPoint.length; i++) {
            breakPoint[i].setActive();
            pen = computePenalty(breakPoint, i);
            if (pen <= EJECT_PENALTY || pen <= threshold) {
                if (i + 1 < breakPoint.length) {
                    Breaks b2 = findOptimalBreakPoints(breakPoint, line + 1,
                            threshold, depth + 1, i + 1);
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
     *
     * @param nodes the horizontal node list containing all nodes for the
     *   paragraph
     * @param hyphenpenalty penalty for a discretionary node with non-empty
     *  pre-text
     * @param exhyphenpenalty penalty for a discretionary node with empty
     *  pre-text
     * @param tracer the logger to write trace information to or
     *  <code>null</code> if no logging is desirable
     *
     * @return a complete list of admissible break points
     */
    private BreakPoint[] makeBreakPoints(final HorizontalListNode nodes,
            final int hyphenpenalty, final int exhyphenpenalty,
            final Logger tracer) {

        List bp = new ArrayList();
        //bp.add(new BreakPoint(0, new Glue(0)));
        int len = nodes.size();
        Glue w = new Glue(0);
        Glue wd = new Glue(0);
        boolean math = false;

        for (int i = 0; i < len; i++) {
            Node node = nodes.get(i);

            if (node instanceof GlueNode && i > 0
                    && !(nodes.get(i - 1) instanceof Discartable)) {
                bp.add(new BreakPoint(i, w, wd, 0));
                w = new Glue(0);
            } else if (node instanceof KernNode && !math
                    && nodes.get(i + 1) instanceof GlueNode) {
                bp.add(new BreakPoint(i, w, wd, 0));
                w = new Glue(0);
            } else if (node instanceof BeforeMathNode) {
                math = true;
            } else if (node instanceof AfterMathNode) {
                if (nodes.get(i + 1) instanceof GlueNode) {
                    bp.add(new BreakPoint(i, w, wd, 0));
                    w = new Glue(0);
                }
                math = false;
            } else if (node instanceof PenaltyNode) {
                int penalty = (int) ((PenaltyNode) node).getPenalty();
                if (penalty < INF_PENALTY) {
                    bp.add(new BreakPoint(i, w, wd, penalty));
                    w = new Glue(0);
                }
            } else if (node instanceof DiscretionaryNode) {
                bp.add(new BreakPoint(i, w, wd, (((DiscretionaryNode) node)
                        .getPreBreak().length() != 0
                        ? hyphenpenalty
                        : exhyphenpenalty)));
                w = new Glue(0);
            } else {
                node.addWidthTo(w);
            }
        }

        BreakPoint[] a = new BreakPoint[bp.size()];
        bp.toArray(a);

        return a;
    }

    /**
     * ...
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
     * ...
     *
     * @param nodes ...
     * @param breaks TODO
     *
     * @return ...
     */
    private NodeList splitNodeList(final NodeList nodes, final Breaks breaks) {

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