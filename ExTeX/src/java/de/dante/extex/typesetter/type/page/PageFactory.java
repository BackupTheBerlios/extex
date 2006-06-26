/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type.page;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.max.StringSource;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.count.ImmutableCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.NodeList;
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
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides a factory for page instances.
 *
 * <p>
 *  The separation of the page into a logical page and a physical page is
 *  depicted in the figure below.
 * </p>
 * <div class="figure">
 *  <img src="doc-files/page-1.png" title="Dimensions of a Page"/>
 *  <div class="caption">
 *   Dimensions of a page
 *  </div>
 * </div>
 * <p>
 *  The physical page denotes the real paper. DVI has no notion of the physical
 *  page but PDF knows of those bounds. The logical page is placed somewhere on
 *  the physical page. The physical page has the width <tt>\mediawidth</tt>
 *  and the height <tt>\mediaheight</tt>.
 * </p>
 * <p>
 *  The logical page is the area used by <logo>TeX</logo> to place material on.
 *  It has a reference point which is in its upper left corner. This reference
 *  point is 1&nbsp;in right and 1&nbsp;in down from the corner of the physical
 *  page. The reference point can be shifted further by using the dimen
 *  registers <tt>\hoffset</tt> and <tt>\voffset</tt>.
 * </p>
 *
 *
 * <h2>Parameters</h2>
 *
 * <doc name="mediawidth" type="register">
 * <h3>The Dimen Parameter <tt>\mediawidth</tt></h3>
 * <p>
 *  The dimen parameter <tt>\mediawidth</tt> contains the physical width of the
 *  page. The logical page is usually smaller.
 * </p>
 * <p>
 *  The value of this  parameter is used when a page is shipped out and
 *  attached to the page. Any modifications of the parameter have no effect
 *  to the value stored.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;mediawidth&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\mediawidth</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@link
 *        de.dante.extex.interpreter.type.dimen.Dimen#parse(Context,TokenSource,Typesetter)
 *        &lang;dimen value&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang; </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \mediawidth=210mm </pre>
 * </doc>
 *
 *
 * <doc name="mediaheight" type="register">
 * <h3>The Dimen Parameter <tt>\mediaheight</tt></h3>
 * <p>
 *  The dimen parameter <tt>\mediaheight</tt> contains the physical height of
 *  the page. The logical page is usually smaller.
 * </p>
 * <p>
 *  The value of this  parameter is used when a page is shipped out and
 *  attached to the page. Any modifications of the parameter have no effect
 *  to the value stored.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;mediaheight&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\mediaheight</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@link
 *        de.dante.extex.interpreter.type.dimen.Dimen#parse(Context,TokenSource,Typesetter)
 *        &lang;dimen value&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang; </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \mediaheight=297mm </pre>
 * </doc>
 *
 *
 * <doc name="hoffset" type="register">
 * <h3>The Dimen Parameter <tt>\hoffset</tt></h3>
 * <p>
 *  The logical page is placed on the physical page such that the upper left
 *  corner of the logical page is 1&nbsp;in down and 1&nbsp;in to the right of
 *  the physical page. This placement can be influence by the dimen parameter
 *  <tt>\hoffset</tt>.
 *  The dimen parameter <tt>\hoffset</tt> contains the horizontal offset
 *  added to the reference point when placing the logical page.
 *  The default value is 0&nbsp;pt. Thus the reference point is 1&nbsp;in to the
 *  right. A positive value shifts the reference point rightwards.
 * </p>
 * <p>
 *  The value of this  parameter is used when a page is shipped out and
 *  attached to the page. Any modifications of the parameter have no effect
 *  to the value stored.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hoffset&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\hoffset</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@link
 *        de.dante.extex.interpreter.type.dimen.Dimen#parse(Context,TokenSource,Typesetter)
 *        &lang;dimen value&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang; </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \hoffset=-.5in </pre>
 * </doc>
 *
 *
 * <doc name="voffset" type="register">
 * <h3>The Dimen Parameter <tt>\voffset</tt></h3>
 * <p>
 *  The logical page is placed on the physical page such that the upper left
 *  corner of the logical page is 1&nbsp;in down and 1&nbsp;in to the right of
 *  the physical page. This placement can be influence by the dimen parameter
 *  <tt>\voffset</tt>.
 *  The dimen parameter <tt>\voffset</tt> contains the vertical offset
 *  added to the reference point when placing the logical page.
 *  The default value is 0&nbsp;pt. Thus the reference point is 1&nbsp;in down.
 *  A positive value shifts the reference point downwards.
 * </p>
 * <p>
 *  The value of this  parameter is used when a page is shipped out and
 *  attached to the page. Any modifications of the parameter have no effect
 *  to the value stored.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;voffset&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\voffset</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@link
 *        de.dante.extex.interpreter.type.dimen.Dimen#parse(Context,TokenSource,Typesetter)
 *        &lang;dimen value&rang;}
 *
 *    &lang;optional prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt> &lang;optional prefix&rang; </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \voffset=1in </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class PageFactory implements LogEnabled {

    /**
     * The field <tt>x</tt> contains the ...
     */
    private static String[] NO = {"0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9"};

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private Logger logger = null;

    /**
     * The field <tt>sizePattern</tt> contains the pattern for matching the
     * <tt>papersize</tt> special.
     */
    private Pattern sizePattern;

    /**
     * The field <tt>visitor</tt> contains the node visitor to determine which
     * nodes to keep and to post-process the nodes.
     */
    private PageFactoryNodeVisitor visitor = new PageFactoryNodeVisitor() {

        /**
         * The field <tt>context</tt> contains the interpreter context.
         */
        private Context context;

        /**
         * The field <tt>page</tt> contains the page.
         */
        private Page page;

        /**
         * The field <tt>typesetter</tt> contains the typesetter.
         */
        private Typesetter typesetter;

        /**
         * @see de.dante.extex.typesetter.type.page.PageFactoryNodeVisitor#setContext(
         *      de.dante.extex.interpreter.context.Context)
         */
        public void setContext(final Context context) {

            this.context = context;
        }

        /**
         * @see de.dante.extex.typesetter.type.page.PageFactoryNodeVisitor#setPage(
         *      de.dante.extex.typesetter.type.page.Page)
         */
        public void setPage(final Page page) {

            this.page = page;
        }

        /**
         * @see de.dante.extex.typesetter.type.page.PageFactoryNodeVisitor#setTypesetter(
         *      de.dante.extex.typesetter.Typesetter)
         */
        public void setTypesetter(final Typesetter typesetter) {

            this.typesetter = typesetter;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
         *      de.dante.extex.typesetter.type.node.AdjustNode,
         *      java.lang.Object)
         */
        public Object visitAdjust(final AdjustNode node, final Object value)
                throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
         *      de.dante.extex.typesetter.type.node.AfterMathNode,
         *      java.lang.Object)
         */
        public Object visitAfterMath(final AfterMathNode node,
                final Object value) throws GeneralException {

            if (((Boolean) value).booleanValue()) {
                if (node.getWidth().eq(Dimen.ZERO_PT)) {
                    return null;
                }
            } else if (node.getVerticalSize().eq(Dimen.ZERO_PT)) {
                return null;
            }
            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
         *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitAlignedLeaders(final AlignedLeadersNode node,
                final Object value) throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
         *      de.dante.extex.typesetter.type.node.BeforeMathNode,
         *      java.lang.Object)
         */
        public Object visitBeforeMath(final BeforeMathNode node,
                final Object value) throws GeneralException {

            if (((Boolean) value).booleanValue()) {
                if (node.getWidth().eq(Dimen.ZERO_PT)) {
                    return null;
                }
            } else if (node.getVerticalSize().eq(Dimen.ZERO_PT)) {
                return null;
            }
            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
         *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
         *      java.lang.Object)
         */
        public Object visitCenteredLeaders(final CenteredLeadersNode node,
                final Object value) throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
         *      de.dante.extex.typesetter.type.node.CharNode,
         *      java.lang.Object)
         */
        public Object visitChar(final CharNode node, final Object value)
                throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
         *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
         *      java.lang.Object)
         */
        public Object visitDiscretionary(final DiscretionaryNode node,
                final Object value) throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
         *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
         *      java.lang.Object)
         */
        public Object visitExpandedLeaders(final ExpandedLeadersNode node,
                final Object value) throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
         *      de.dante.extex.typesetter.type.node.GlueNode,
         *      java.lang.Object)
         */
        public Object visitGlue(final GlueNode node, final Object value)
                throws GeneralException {

            if (((Boolean) value).booleanValue()) {
                if (node.getWidth().eq(Dimen.ZERO_PT)) {
                    return null;
                }
            } else if (node.getVerticalSize().eq(Dimen.ZERO_PT)) {
                return null;
            }
            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
         *      de.dante.extex.typesetter.type.node.HorizontalListNode,
         *      java.lang.Object)
         */
        public Object visitHorizontalList(final HorizontalListNode node,
                final Object value) throws GeneralException {

            return (node.size() == 0 ? null : node);
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
         *      de.dante.extex.typesetter.type.node.InsertionNode,
         *      java.lang.Object)
         */
        public Object visitInsertion(final InsertionNode node,
                final Object value) throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
         *      de.dante.extex.typesetter.type.node.KernNode,
         *      java.lang.Object)
         */
        public Object visitKern(final KernNode node, final Object value)
                throws GeneralException {

            if (((Boolean) value).booleanValue()) {
                if (node.getWidth().eq(Dimen.ZERO_PT)) {
                    return null;
                }
            } else if (node.getVerticalSize().eq(Dimen.ZERO_PT)) {
                return null;
            }
            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
         *      de.dante.extex.typesetter.type.node.LigatureNode,
         *      java.lang.Object)
         */
        public Object visitLigature(final LigatureNode node, final Object value)
                throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
         *      de.dante.extex.typesetter.type.node.MarkNode,
         *      java.lang.Object)
         */
        public Object visitMark(final MarkNode node, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
         *      de.dante.extex.typesetter.type.node.PenaltyNode,
         *      java.lang.Object)
         */
        public Object visitPenalty(final PenaltyNode node, final Object value)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
         *      de.dante.extex.typesetter.type.node.RuleNode,
         *      java.lang.Object)
         */
        public Object visitRule(final RuleNode node, final Object value)
                throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
         *      de.dante.extex.typesetter.type.node.SpaceNode,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceNode node, final Object value)
                throws GeneralException {

            return node;
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
         *      de.dante.extex.typesetter.type.node.VerticalListNode,
         *      java.lang.Object)
         */
        public Object visitVerticalList(final VerticalListNode node,
                final Object value) throws GeneralException {

            return (node.size() == 0 ? null : node);
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitVirtualChar(
         *      de.dante.extex.typesetter.type.node.VirtualCharNode,
         *      java.lang.Object)
         */
        public Object visitVirtualChar(final VirtualCharNode node,
                final Object value) throws GeneralException {

            return node.getNodes();
        }

        /**
         * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
         *      de.dante.extex.typesetter.type.node.WhatsItNode,
         *      java.lang.Object)
         */
        public Object visitWhatsIt(final WhatsItNode node, final Object value)
                throws GeneralException {

            if (node instanceof SpecialNode) {

                String text = ((SpecialNode) node).getText();

                if (text.startsWith("papersize=")) {
                    Matcher m = sizePattern.matcher(text);
                    if (m.matches()) {
                        try {
                            Dimen width = Dimen.parse(context, //
                                    new StringSource(m.group(1)), typesetter);
                            Dimen height = Dimen.parse(context, //
                                    new StringSource(m.group(2)), typesetter);
                            page.setMediaWidth(width);
                            page.setMediaHeight(height);
                        } catch (ConfigurationException e) {
                            logger.log(Level.SEVERE, "", e);
                        }
                    } else {
                        logger.warning("...");
                    }

                } else if (text.equals("landscape")) {

                    Dimen h = page.getMediaHeight();
                    page.setMediaHeight(page.getMediaWidth());
                    page.setMediaWidth(h);
                }
            }
            return node;
        }

    };

    /**
     * Creates a new object.
     */
    public PageFactory() {

        super();
        sizePattern = Pattern.compile("papersize="
                + "([0-9.]+[a-z][a-z]),([0-9.]+[a-z][a-z])");
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger log) {

        logger = log;
    }

    /**
     * Get a new instance of a page.
     *
     * @param nodes the nodes contained
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @return the new instance or <code>null</code> if the page would be empty
     *
     * @throws GeneralException in case of an error
     */
    public Page newInstance(final NodeList nodes, final Context context,
            final Typesetter typesetter) throws GeneralException {

        FixedCount[] pageNo = new FixedCount[10];
        for (int i = 0; i < 10; i++) {
            pageNo[i] = new ImmutableCount(context.getCount(NO[i]));
        }
        PageImpl page = new PageImpl(nodes, pageNo);

        page.setMediaWidth(context.getDimen("mediawidth"));
        page.setMediaHeight(context.getDimen("mediaheight"));
        Dimen off = new Dimen(Dimen.ONE_INCH);
        off.add(context.getDimen("hoffset"));
        page.setMediaHOffset(off);
        off.set(Dimen.ONE_INCH);
        off.add(context.getDimen("voffset"));
        page.setMediaVOffset(off);

        context.startMarks();
        visitor.setPage(page);
        visitor.setContext(context);
        visitor.setTypesetter(typesetter);

        if (nodes.atShipping(context, typesetter, visitor, false) == null) {
            return null;
        }
        return page;
    }

}
