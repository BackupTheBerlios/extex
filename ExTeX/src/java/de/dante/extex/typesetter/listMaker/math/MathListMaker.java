/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.EofException;
import de.dante.extex.interpreter.exception.MissingMathException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.listMaker.AbstractListMaker;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.type.MathClass;
import de.dante.extex.typesetter.type.MathDelimiter;
import de.dante.extex.typesetter.type.MathGlyph;
import de.dante.extex.typesetter.type.noad.FractionNoad;
import de.dante.extex.typesetter.type.noad.GlueNoad;
import de.dante.extex.typesetter.type.noad.MathList;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.extex.typesetter.type.noad.NoadFactory;
import de.dante.extex.typesetter.type.noad.StyleNoad;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This is the list maker for the inline math formulae.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathListMaker extends AbstractListMaker implements NoadConsumer {

    /**
     * This inner class is a memento of the state of the math list maker.
     * It is used to store to the stack and restore the state from the stack.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class MathMemento {

        /**
         * The field <tt>ip</tt> contains the ...
         */
        private MathList ip;

        /**
         * The field <tt>noads</tt> contains the ...
         */
        private Noad noads;

        /**
         * Creates a new object.
         *
         * @param ip ...
         * @param noads ...
         */
        public MathMemento(final MathList ip, final Noad noads) {

            super();
            this.ip = ip;
            this.noads = noads;
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
     * The field <tt>stack</tt> contains the ...
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

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        throw new UnsupportedOperationException();
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
     * @see de.dante.extex.typesetter.ListMaker#close(TypesetterOptions)
     * @see "TeX -- The Program [719]"
     */
    public NodeList close(final TypesetterOptions context) {

        HorizontalListNode list = new HorizontalListNode();

        System.err.println(noads.toString());

        noads.typeset(list, new MathContext(StyleNoad.TEXTSTYLE, context),
                context);
        //TODO gene: ???
        return list;
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
     * Notification method to deal the case that a left brace has been
     * encountered.
     */
    public void leftBrace() {

        stack.push(new MathMemento(insertionPoint, noads));
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
        //noads.remove(noads.size() - 1); // TODO gene: allow this?
    }

    /**
     * Notification method to deal the case that a right brace has been
     * encountered.
     */
    public void rightBrace() {

        if (stack.empty()) {
            //TODO gene: unimplemented
            throw new RuntimeException("unimplemented");
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
        MathListMaker ml = (MathListMaker) getManager().pop();

        switch (ml.insertionPoint.size()) { //TODO gene: accessing the attribute directly is horrible
            case 0:
                //TODO gene: error unimplemented
                throw new RuntimeException("unimplemented");
            case 1:
                return ml.insertionPoint.get(0);
            default:
                return ml.insertionPoint;
        }
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