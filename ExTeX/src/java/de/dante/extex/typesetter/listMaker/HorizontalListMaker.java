/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.font.Glyph;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.ParagraphObserver;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.InvalidSpacefactorException;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.exception.TypesetterUnsupportedException;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.AfterMathNode;
import de.dante.extex.typesetter.type.node.BeforeMathNode;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.ImplicitKernNode;
import de.dante.extex.typesetter.type.node.SpaceNode;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * Maker for a horizontal list.
 * <p>
 * After <code>par()</code>, the line breaking and hyphenation are applied.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.34 $
 */
public class HorizontalListMaker extends AbstractListMaker {

    /**
     * The constant <tt>DEFAULT_SPACEFACTOR</tt> contains the default value for
     * the space factor. It is 1000 according to <logo>TeX</logo>.
     */
    private static final int DEFAULT_SPACEFACTOR = 1000;

    /**
     * The constant <tt>SPACEFACTOR_THRESHOLD</tt> contains the threshold for
     * the space factor above which the space is handled different.
     */
    private static final int SPACEFACTOR_THRESHOLD = 2000;

    /**
     * The field <tt>afterParagraphObservers</tt> contains the observers to be
     * invoked after the paragraph has been completed.
     */
    private List afterParagraphObservers = new ArrayList();

    /**
     * The field <tt>nodes</tt> contains the node list encapsulated by this
     * class.
     */
    private HorizontalListNode nodes = new HorizontalListNode();

    /**
     * The field <tt>spaceFactor</tt> contains the current space factor.
     *
     * @see "<logo>TeX</logo> &ndash; The Program [212]"
     */
    private long spaceFactor = DEFAULT_SPACEFACTOR;

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     * @param locator the locator
     */
    public HorizontalListMaker(final ListManager manager, final Locator locator) {

        super(manager, locator);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.type.Node)
     */
    public void add(final Node c)
            throws TypesetterException,
                ConfigurationException {

        nodes.add(c);
        spaceFactor = DEFAULT_SPACEFACTOR;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addAndAdjust(
     *      de.dante.extex.typesetter.type.NodeList,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void addAndAdjust(final NodeList list,
            final TypesetterOptions options) throws TypesetterException {

        nodes.add(list); // TODO gene: correct?
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public void add(final FixedGlue g) throws TypesetterException {

        nodes.addSkip(g);
        spaceFactor = DEFAULT_SPACEFACTOR;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext context, final Count sfCount)
            throws TypesetterException,
                ConfigurationException {

        long sf = (sfCount != null ? sfCount.getValue() : spaceFactor);
        Glue space = new Glue(context.getFont().getSpace());

        // gene: maybe my interpretation of the TeXbook is slightly wrong
        if (sf != DEFAULT_SPACEFACTOR) { // normal case handled first
            if (sf == 0) {
                return;
            } else if (sf >= SPACEFACTOR_THRESHOLD) {
                TypesetterOptions options = getManager().getOptions();
                FixedGlue xspaceskip = options.getGlueOption("xspaceskip");
                FixedGlue spaceskip = options.getGlueOption("spaceskip");

                if (xspaceskip != null) {
                    space = xspaceskip.copy();
                } else if (spaceskip != null) {
                    space = spaceskip.copy();
                    space.multiplyStretch(sf, DEFAULT_SPACEFACTOR);
                    space.multiplyShrink(DEFAULT_SPACEFACTOR, sf);
                } else {
                    space = space.copy();
                    space.multiplyStretch(sf, DEFAULT_SPACEFACTOR);
                    space.multiplyShrink(DEFAULT_SPACEFACTOR, sf);
                }
            } else {
                space = space.copy();
                space.multiplyStretch(sf, DEFAULT_SPACEFACTOR);
                space.multiplyShrink(DEFAULT_SPACEFACTOR, sf);
            }
        }

        add(new SpaceNode(space));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#afterParagraph(
     *      de.dante.extex.typesetter.ParagraphObserver)
     */
    public void afterParagraph(final ParagraphObserver observer) {

        afterParagraphObservers.add(observer);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     */
    public NodeList complete(final TypesetterOptions context)
            throws TypesetterException,
                ConfigurationException {

        return getManager().buildParagraph(nodes);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#cr(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void cr(final Context context, final TypesettingContext tc,
            final UnicodeChar uc) throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLastNode()
     */
    public Node getLastNode() {

        return (nodes.empty() ? null : nodes.get(nodes.size() - 1));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.HORIZONTAL;
    }

    /**
     * Getter for nodes.
     *
     * @return the nodes.
     */
    protected HorizontalListNode getNodes() {

        return this.nodes;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getSpacefactor()
     */
    public long getSpacefactor() {

        return spaceFactor;
    }

    /**
     * Add a character node to the list.
     *
     * @param context the interpreter context
     * @param tc the typesetting context for the symbol
     * @param symbol the symbol to add
     * @param locator the locator
     *
     * @see de.dante.extex.typesetter.ListMaker#letter(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.Locator)
     * @see "The TeXbook [p.76]"
     */
    public boolean letter(final Context context, final TypesettingContext tc,
            final UnicodeChar symbol, final Locator locator)
            throws TypesetterException {

        UnicodeChar c = symbol;
        Node node;
        int size = nodes.size();
        if (size > 0) {
            Node n = nodes.get(size - 1);
            if (n instanceof CharNode) {
                CharNode cn = ((CharNode) n);
                Font f = tc.getFont();
                if (cn.getTypesettingContext().getFont().equals(f)) {
                    UnicodeChar lig = tc.getLanguage().getLigature(
                            cn.getCharacter(), symbol, f);
                    if (lig != null) {
                        nodes.remove(size - 1);
                        c = lig;
                    } else {
                        FixedDimen kerning = f.getKerning(c, symbol);
                        if (kerning != null && kerning.ne(Dimen.ZERO_PT)) {
                            nodes.add(new ImplicitKernNode(kerning, true));
                        }
                    }
                }
            }
        }
        node = getManager().getNodeFactory().getNode(tc, c);
        if (node == null) {
            return true;
        }

        nodes.add(node);

        if (node instanceof CharNode) {
            int f = ((CharNode) node).getSpaceFactor();

            if (f != 0) {
                spaceFactor = (spaceFactor < DEFAULT_SPACEFACTOR
                        && f > DEFAULT_SPACEFACTOR //
                ? DEFAULT_SPACEFACTOR : f);
            }
        }
        return false;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public void par() throws TypesetterException, ConfigurationException {

        try {
            // Note: the observers have to be run in reverse order to restore
            // the language properly.
            for (int i = afterParagraphObservers.size() - 1; i >= 0; i--) {
                ((ParagraphObserver) afterParagraphObservers.get(i))
                        .atParagraph(nodes);
            }
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        }
        getManager().endParagraph();
    }

    /**
     * Extract non-word Nodes from nodes into a NodeList.
     *
     * @param start the start index
     * @param list the target list
     * @param size the length of the node list
     *
     * @return the index of the first node not considered
     */
    private int parseNonChars(final int start, final NodeList list,
            final int size) {

        for (int i = start; i < size; i++) {
            Node n = nodes.get(i);
            if (n instanceof BeforeMathNode) {
                do {
                    list.add(n);
                    i++;
                    if (i >= size) {
                        return i;
                    }
                    n = nodes.get(i);
                } while (!(n instanceof AfterMathNode));
                i--;
            } else if (n instanceof CharNode) {
                return i;
            }
            list.add(n);
        }
        return size;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

        nodes.remove(nodes.size() - 1);
    }

    /**
     * Setter for nodes.
     *
     * @param nodes the nodes to set.
     */
    protected void setNodes(final HorizontalListNode nodes) {

        this.nodes = nodes;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public void setSpacefactor(final FixedCount f)
            throws TypesetterUnsupportedException,
                InvalidSpacefactorException {

        long sf = f.getValue();
        if (sf <= 0) {
            throw new InvalidSpacefactorException();
        }
        spaceFactor = sf;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#showlist(
     *      java.lang.StringBuffer, long, long)
     */
    public void showlist(final StringBuffer sb, final long l, final long m) {

        sb.append("spacefactor ");
        sb.append(spaceFactor);
        sb.append('\n');
    }

}