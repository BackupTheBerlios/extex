/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker.math;

import java.util.Stack;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingMathException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Mudimen;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.listMaker.AbstractListMaker;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.type.MathClass;
import de.dante.extex.typesetter.type.MathDelimiter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.noad.FractionNoad;
import de.dante.extex.typesetter.type.noad.GlueNoad;
import de.dante.extex.typesetter.type.noad.KernNoad;
import de.dante.extex.typesetter.type.noad.LeftNoad;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.extex.typesetter.type.noad.MathList;
import de.dante.extex.typesetter.type.noad.MiddleNoad;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.extex.typesetter.type.noad.NoadFactory;
import de.dante.extex.typesetter.type.noad.NodeNoad;
import de.dante.extex.typesetter.type.noad.RightNoad;
import de.dante.extex.typesetter.type.noad.StyleNoad;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This is the list maker for the inline math formulae.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class MathListMaker extends AbstractListMaker implements NoadConsumer {

    /**
     * This inner class is a memento of the state of the math list maker.
     * It is used to store to the stack and restore the state from the stack.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.9 $
     */
    private class MathMemento {

        /**
         * The field <tt>block</tt> contains the indicator that this memento
         * corresponds to a block. Otherwise it corresponds to a \left-\right
         * pair.
         */
        private boolean block;

        /**
         * The field <tt>ip</tt> contains the insertion point.
         */
        private MathList ip;

        /**
         * The field <tt>noads</tt> contains the noads.
         */
        private Noad noads;

        /**
         * Creates a new object.
         *
         * @param ip the insertion point to be saved in this memento
         * @param noads the noads to be saved in this memento
         * @param block indicator to distinguish blocks from \left-\right
         *  constructs. a Value of <code>true</code> indicates a block.
         */
        public MathMemento(final MathList ip, final Noad noads,
                final boolean block) {

            super();
            this.ip = ip;
            this.noads = noads;
            this.block = block;
        }

        /**
         * Getter for ip.
         *
         * @return the ip
         */
        public MathList getIp() {

            return this.ip;
        }

        /**
         * Getter for noads.
         *
         * @return the noads
         */
        public Noad getNoads() {

            return this.noads;
        }
    }

    /**
     * The field <tt>noadFactory</tt> contains the noad factory.
     */
    private static final NoadFactory noadFactory = new NoadFactory();

    /**
     * The field <tt>insertionPoint</tt> contains the the MathList to which
     * the next noads should be added.
     */
    private MathList insertionPoint;

    /**
     * The field <tt>nodes</tt> contains the list of nodes encapsulated in this
     * instance.
     */
    private Noad noads;

    /**
     * The field <tt>stack</tt> contains the stack for parsing sub-formulae.
     */
    private Stack stack = new Stack();

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public MathListMaker(final ListManager manager) {

        super(manager);
        insertionPoint = new MathList();
        noads = insertionPoint;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.typesetter.type.MathClass,
     *      de.dante.extex.typesetter.type.MathGlyph)
     */
    public void add(final MathClass mclass, final MathGlyph mg)
            throws GeneralException {

        add(noadFactory.getNoad(mclass, mg));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.interpreter.type.muskip.Mudimen)
     */
    public void add(final Mudimen skip) throws GeneralException {

        insertionPoint.add(new KernNoad(skip));
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#add(
     *      de.dante.extex.interpreter.type.muskip.Muskip)
     */
    public void add(final Muskip glue) throws GeneralException {

        add(new GlueNoad(glue));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void add(final Noad noad) throws GeneralException {

        insertionPoint.add(noad);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.Node)
     */
    public void add(final Node node) {

        insertionPoint.add(new NodeNoad(node));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        insertionPoint.add(new NodeNoad(new GlueNode(g)));
    }

    /**
     * Spaces are ignored in math mode. Thus this method is a noop.
     *
     * @param typesettingContext the typesetting context for the space
     * @param spacefactor the spacefactor to use for this space or
     * <code>null</code> to indicate that the default speacefactor should
     * be used.
     *
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) {

    }

    /**
     * Close the node list.
     * In the course of the closing the Noad list is translated into a Node
     * list.
     *
     * @param context the fragment of the context accessible for the typesetter
     *
     * @return the node list enclosed in this instance
     *
     * @see de.dante.extex.typesetter.ListMaker#complete(TypesetterOptions)
     * @see "TeX -- The Program [719]"
     */
    public NodeList complete(final TypesetterOptions context) throws InterpreterException {

        HorizontalListNode list = new HorizontalListNode();

        //System.err.println(noads.toString());

        noads.typeset(list, new MathContext(StyleNoad.TEXTSTYLE, context),
                context);
        //TODO gene: ???
        return list;
    }

    /**
     * Getter for the contents of the insertion point. If the insertion point
     * does not contain an element then <code>null</code> is returned. If it
     * contains only one element then this element is returned. Otherwiese the
     * complete list is returned.
     *
     * @return the contents of the insertion point
     */
    protected Noad getIp() {

        switch (insertionPoint.size()) {
            case 0:
                return null;
            case 1:
                return insertionPoint.get(0);
            default:
                return insertionPoint;
        }
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#getLastNoad()
     */
    public Noad getLastNoad() throws GeneralException {

        return insertionPoint.getLastNoad();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getLastNode()
     */
    public Node getLastNode() {

        return null;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.MATH;
    }

    /**
     * Getter for noades.
     *
     * @return the noades.
     */
    protected Noad getNoades() {

        return this.noads;
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#left(
     *      de.dante.extex.typesetter.type.MathDelimiter)
     */
    public void left(final MathDelimiter delimiter) throws GeneralException {

        add(new LeftNoad(delimiter));
    }

    /**
     * Notification method to deal the case that a left brace has been
     * encountered.
     *
     * @see de.dante.extex.typesetter.ListMaker#leftBrace()
     */
    public void leftBrace() {

        stack.push(new MathMemento(insertionPoint, noads, true));
        insertionPoint = new MathList();
        noads = insertionPoint;
    }

    /**
     * Add a math character node to the list.
     *
     * @param context the interpreter context
     * @param tc the typesetting context for the symbol. This parameter is
     *  ignored in math mode.
     * @param symbol the symbol to add
     *
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void letter(final Context context, final TypesettingContext tc,
            final UnicodeChar symbol) {

        Count mcode = context.getMathcode(symbol);
        insertionPoint.add(noadFactory.getNoad((mcode == null ? 0 : mcode
                .getValue())));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      Context, TokenSource, de.dante.extex.scanner.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws GeneralException {

        getManager().endParagraph();
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#middle(
     *      de.dante.extex.typesetter.type.MathDelimiter)
     */
    public void middle(final MathDelimiter delimiter) throws GeneralException {

        add(new MiddleNoad(delimiter));
    }

    /**
     * Emitting a new paragraph is not supported in math mode.
     * Thus an exception is thrwon.
     *
     * @throws GeneralException in any case
     *
     * @see de.dante.extex.typesetter.ListMaker#par()
     * @see "TeX -- The Program [1047]"
     */
    public void par() throws GeneralException {

        getManager().endParagraph();
        throw new MissingMathException("\\par"); //TODO gene: other string?
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#right(
     *      de.dante.extex.typesetter.type.MathDelimiter)
     */
    public void right(final MathDelimiter delimiter) throws GeneralException {

        add(new RightNoad(delimiter));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#rightBrace()
     */
    public void rightBrace() throws GeneralException {

        if (stack.empty()) {
            throw new HelpingException(getLocalizer(), "TTP.ExtraOrForgotten");
        }
        Noad n = noads;
        MathMemento memento = (MathMemento) stack.pop();
        insertionPoint = memento.getIp();
        noads = memento.getNoads();
        insertionPoint.add(n);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#scanNoad(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Noad scanNoad(final Context context, final TokenSource source)
            throws GeneralException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException(null);
        }
        getManager().push(new MathListMaker(getManager()));
        if (t.isa(Catcode.LEFTBRACE)) {
            source.executeGroup();
        } else {
            source.execute(t);
        }
        return (((MathListMaker) getManager().pop())).getIp();
    }

    /**
     * Setter for insertionPoint.
     *
     * @param insertionPoint the insertionPoint to set
     */
    protected void setInsertionPoint(final MathList insertionPoint) {

        this.insertionPoint = insertionPoint;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Token token) throws GeneralException {

        Noad sub = scanNoad(context, source);
        if (insertionPoint.size() == 0) {
            add(new MathList());
        }
        Noad noad = insertionPoint.get(insertionPoint.size() - 1);
        if (noad.getSubscript() != null) {
            throw new HelpingException(getLocalizer(), "TTP.DoubleSubscript");
        }

        noad.setSubscript(sub);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#superscriptMark(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Token token)
            throws GeneralException {

        Noad sup = scanNoad(context, source);
        if (insertionPoint.size() == 0) {
            add(new MathList());
        }
        Noad noad = insertionPoint.get(insertionPoint.size() - 1);
        if (noad.getSuperscript() != null) {
            throw new HelpingException(getLocalizer(), "TTP.DoubleSuperscript");
        }

        noad.setSubscript(sup);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.math.NoadConsumer#switchToFraction(
     *      de.dante.extex.typesetter.type.MathDelimiter,
     *      de.dante.extex.typesetter.type.MathDelimiter,
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void switchToFraction(final MathDelimiter leftDelimiter,
            final MathDelimiter rightDelimiter, final Dimen ruleWidth)
            throws GeneralException {

        if (!(noads instanceof MathList)) {
            throw new HelpingException(getLocalizer(), "TTP.AmbiguousFraction");
        }
        insertionPoint = new MathList();
        noads = new FractionNoad((MathList) noads, insertionPoint,
                leftDelimiter, rightDelimiter, ruleWidth);
    }
}