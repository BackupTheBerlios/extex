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

package de.dante.extex.language.hyphenation.liang;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.NodeVisitor;
import de.dante.extex.typesetter.type.node.AdjustNode;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.AlignedLeadersNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.CenteredLeadersNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.CharNodeFactory;
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
import de.dante.extex.typesetter.type.node.VirtualCharNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides a node visitor to insert discretionaries into nodes.
 * It covers the general case and should used as last resort if the
 * &ldquo;normal&rdquo; cases do not apply.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
class NV implements NodeVisitor {

    /**
     * The field <tt>hyph</tt> contains the indicator for the hyphenation
     * positions. A hyphenation is possible at a position <i>i</i> whenever the
     * value <i>isHyph[i] == true</i>.
     */
    private boolean[] isHyph;

    /**
     * The field <tt>hyphen</tt> contains the token to insert at a hyphenation
     * position into the discretionary.
     */
    private UnicodeChar hyphen;

    /**
     * The field <tt>nodes</tt> contains the node list to add the current
     * node to, with or without additional discretionary node.
     */
    private NodeList nodes;

    /**
     * The field <tt>tc</tt> contains the typesetting context for the hyphen.
     */
    private TypesettingContext tc;

    /**
     * The field <tt>cnf</tt> contains the factory to acquire new char nodes.
     */
    private CharNodeFactory cnf;

    /**
     * Creates a new object.
     *
     * @param nodes the list of nodes to add the current node to
     * @param hyphen the token to insert into the discretionary
     * @param tc the typesetting context for the hyphen
     * @param factory the factory to acquire new char nodes
     * @param hyph the indocator for allowed hyphenation positions
     */
    public NV(final NodeList nodes, final UnicodeChar hyphen,
            final TypesettingContext tc, final CharNodeFactory factory,
            final boolean[] hyph) {

        super();
        this.nodes = nodes;
        this.hyphen = hyphen;
        this.isHyph = hyph;
        this.cnf = factory;
        this.tc = tc;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAdjust(
     *      de.dante.extex.typesetter.type.node.AdjustNode,
     *      java.lang.final Object)
     */
    public final Object visitAdjust(final AdjustNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAfterMath(
     *      de.dante.extex.typesetter.type.node.AfterMathNode,
     *      java.lang.final Object)
     */
    public final Object visitAfterMath(final AfterMathNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitAlignedLeaders(
     *      de.dante.extex.typesetter.type.node.AlignedLeadersNode,
     *      java.lang.final Object)
     */
    public final Object visitAlignedLeaders(final AlignedLeadersNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitBeforeMath(
     *      de.dante.extex.typesetter.type.node.BeforeMathNode,
     *      java.lang.final Object)
     */
    public final Object visitBeforeMath(final BeforeMathNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitCenteredLeaders(
     *      de.dante.extex.typesetter.type.node.CenteredLeadersNode,
     *      java.lang.final Object)
     */
    public final Object visitCenteredLeaders(final CenteredLeadersNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      de.dante.extex.typesetter.type.node.CharNode,
     *      java.lang.final Object)
     */
    public final Object visitChar(final CharNode node, final Object value)
            throws GeneralException {

        Count index = (Count) value;
        if (isHyph[(int) index.getValue()]) {
            nodes.add(new DiscretionaryNode(null, new HorizontalListNode(cnf
                    .newInstance(tc, hyphen)), null));
        }
        nodes.add(node);
        index.add(1);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitDiscretionary(
     *      de.dante.extex.typesetter.type.node.DiscretionaryNode,
     *      java.lang.final Object)
     */
    public final Object visitDiscretionary(final DiscretionaryNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitExpandedLeaders(
     *      de.dante.extex.typesetter.type.node.ExpandedLeadersNode,
     *      java.lang.final Object)
     */
    public final Object visitExpandedLeaders(final ExpandedLeadersNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitGlue(
     *      de.dante.extex.typesetter.type.node.GlueNode,
     *      java.lang.final Object)
     */
    public final Object visitGlue(final GlueNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * No discretionary are inserted into an inline hbox. They serve as
     * containers which prevent hyphenation.
     *
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitHorizontalList(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      java.lang.final Object)
     */
    public final Object visitHorizontalList(final HorizontalListNode node,
            final Object value) throws GeneralException {

        this.nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitInsertion(
     *      de.dante.extex.typesetter.type.node.InsertionNode,
     *      java.lang.final Object)
     */
    public final Object visitInsertion(final InsertionNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitKern(
     *      de.dante.extex.typesetter.type.node.KernNode,
     *      java.lang.final Object)
     */
    public final Object visitKern(final KernNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * A ligature can be separated into several parts.
     *
     * <p>
     * The structure of the ligature node depends on the ligature builder
     * involved. For example the ligature for ffl can be represented in the
     * following ways:
     * </p>
     * <pre>
     *   (f (f l))
     *   ((f f) l)
     * </pre>
     *
     * <p>
     * If the hyphenation should appear after the first character. The pattern
     * is f-fl. In the first case above we simple can reuse the components of
     * the ligature. In the second case the ligature has to be rearranged. The
     * first part has to be split and the ligature buileder applied again to
     * constitute the fl ligature not contaoned as part.
     * </p>
     *
     *
     * <p>
     * The general case is covered by taking the char nodes and reapplying the
     * ligature builder.
     * </p>
     *
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitLigature(
     *      de.dante.extex.typesetter.type.node.LigatureNode,
     *      java.lang.final Object)
     */
    public final Object visitLigature(final LigatureNode node,
            final Object value) throws GeneralException {

        Count index = (Count) value;
        index.set(process(node, (int) (index.getValue())));
        return null;
    }

    /**
     * Process a ligature node.
     *
     * @param node the ligature node to split for hyphenation
     * @param index the index in the hyphenation list
     *
     * @return the new index in the hyphenation list
     *
     * @throws HyphenationException in case of an error
     */
    private int process(final LigatureNode node, final int index)
            throws HyphenationException {

        int next = index;
        int n = node.countChars();
        int needHyphen = 0;

        for (int i = index + 1; i < index + n; i++) {
            if (isHyph[i]) {
                needHyphen++;
                next++;
            }
        }
        if (isHyph[index]) {
            next++;
            nodes.add(new DiscretionaryNode(//
                    new HorizontalListNode(cnf.newInstance(tc, hyphen)), //
                    new HorizontalListNode(), //
                    new HorizontalListNode(node)));
        }
        switch (needHyphen) {
            case 0:
                nodes.add(node);
                break;
            case 1:
                int leftLen = node.getLeft().countChars();
                if (isHyph[leftLen]) { //todo gene: check off by 1
                    Node h = cnf.newInstance(tc, hyphen);
                    NodeList pre = new HorizontalListNode(node.getLeft(), h);
                    NodeList post = new HorizontalListNode(node.getRight());
                    nodes.add(new DiscretionaryNode(pre, post,
                            new HorizontalListNode(node)));
                    break;
                }
            // fall-through to default processing
            default:

                Node[] chars = node.getChars();
                NodeList pre = new HorizontalListNode();
                NodeList post = new HorizontalListNode();

                int i = 0;
                while (!isHyph[index + i]) {
                    pre.add(chars[i]);
                    i++;
                }
                pre.add(cnf.newInstance(tc, hyphen));

                while (i < index + n) {
                    post.add(chars[i]);
                    i++;
                }

                nodes.add(new DiscretionaryNode(pre, hyphenate(post, index + 1,
                        tc.getLanguage()), new HorizontalListNode(node)));
        }

        return next;
    }

    /**
     * Hyphenate subsequent char nodes from a ligature.
     *
     * <p>
     *  Note that TeX only consideres the first hyphenation point in a ligature.
     *  The others are ignored. Nevertheless the ligature builder is applied
     *  to the remaining characters. This might lead to other ligatures than
     *  the ones encoded in the ligature node.
     * </p>
     *
     * @param list the node list to hyphenate
     * @param index the index in the hyphen array
     * @param ligatureBuilder the ligature builder to use
     *
     * @return the hyphenated node list
     *
     * @throws HyphenationException in case of an error
     */
    private NodeList hyphenate(final NodeList list, final int index,
            final LigatureBuilder ligatureBuilder) throws HyphenationException {

        for (int i = 0; i < list.size(); i = ligatureBuilder.insertLigatures(
                list, i)) {
            // ok
        }
        return list;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitMark(
     *      de.dante.extex.typesetter.type.node.MarkNode,
     *      java.lang.final Object)
     */
    public final Object visitMark(final MarkNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitPenalty(
     *      de.dante.extex.typesetter.type.node.PenaltyNode,
     *      java.lang.final Object)
     */
    public final Object visitPenalty(final PenaltyNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitRule(
     *      de.dante.extex.typesetter.type.node.RuleNode,
     *      java.lang.final Object)
     */
    public final Object visitRule(final RuleNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitSpace(
     *      de.dante.extex.typesetter.type.node.SpaceNode,
     *      java.lang.final Object)
     */
    public final Object visitSpace(final SpaceNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitChar(
     *      de.dante.extex.typesetter.type.node.VirtualCharNode,
     *      java.lang.Object)
     */
    public Object visitVirtualChar(final VirtualCharNode node,
            final Object oOut) throws GeneralException {

        return visitChar(node, oOut);
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitVerticalList(
     *      de.dante.extex.typesetter.type.node.VerticalListNode,
     *      java.lang.final Object)
     */
    public final Object visitVerticalList(final VerticalListNode node,
            final Object value) throws GeneralException {

        nodes.add(node);
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.type.NodeVisitor#visitWhatsIt(
     *      de.dante.extex.typesetter.type.node.WhatsItNode,
     *      java.lang.final Object)
     */
    public final Object visitWhatsIt(final WhatsItNode node, final Object value)
            throws GeneralException {

        nodes.add(node);
        return null;
    }

}