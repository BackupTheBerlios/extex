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

package de.dante.extex.typesetter.listMaker;

import de.dante.extex.i18n.EofHelpingException;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.i18n.MathHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.MathClass;
import de.dante.extex.typesetter.type.MathGlyph;
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
 * @version $Revision: 1.5 $
 */
public class MathListMaker extends AbstractListMaker implements NoadConsumer {

    /**
     * The field <tt>cnf</tt> contains the char noad factory.
     */
    private NoadFactory noadFactory = new NoadFactory();

    /**
     * The field <tt>nodes</tt> contains the list of nodes encapsulated in this
     * instance.
     */
    private MathList noads = new MathList();

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public MathListMaker(final ListManager manager) {

        super(manager);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void add(final Noad noad) throws GeneralException {

        noads.add(noad);
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.NoadConsumer#add(
     *      de.dante.extex.typesetter.type.MathClass,
     *      de.dante.extex.typesetter.type.MathGlyph)
     */
    public void add(final MathClass mclass, final MathGlyph mg)
            throws GeneralException {

        add(noadFactory.getNoad(mclass, mg));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.Node)
     */
    public void add(final Node node) {

        //TODO gene: error unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        // TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * Spaces are ignored in math mode.
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

        noads.typeset(list, new MathContext(StyleNoad.TEXTSTYLE), context);
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
    protected MathList getNoades() {

        return this.noads;
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
        throw new MathHelpingException("\\par"); //TODO gene: other string?
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

        noads.remove(noads.size() - 1); // TODO gene: allow this?
    }

    /**
     * @see de.dante.extex.typesetter.listMaker.NoadConsumer#scanNoad(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Noad scanNoad(final Context context, final TokenSource source)
            throws GeneralException {

        Token t = source.getToken();
        if (t == null) {
            throw new EofHelpingException(null);
        }
        getManager().push(new MathListMaker(getManager()));
        if (t.isa(Catcode.LEFTBRACE)) {
            source.executeGroup();
        } else {
            source.execute(t);
        }
        MathListMaker ml = (MathListMaker) getManager().pop();

        switch (ml.noads.size()) { //TODO gene: accessing the attribute directly is horrible
            case 0:
                //TODO gene: error unimplemented
                throw new RuntimeException("unimplemented");
            case 1:
                return ml.noads.get(0);
            default:
                return ml.noads;
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Token token) throws GeneralException {

        Noad sub = scanNoad(context, source);
        if (noads.size() == 0) {
            add(new MathList());
        }
        Noad noad = noads.get(noads.size() - 1);
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
        if (noads.size() == 0) {
            add(new MathList());
        }
        Noad noad = noads.get(noads.size() - 1);
        if (noad.getSuperscript() != null) {
            throw new HelpingException(getLocalizer(), "TTP.DoubleSuperscript");
        }

        noad.setSubscript(sup);
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
    public void treatLetter(final Context context, final TypesettingContext tc,
            final UnicodeChar symbol) {

        Count mcode = context.getMathcode(symbol);
        noads.add(noadFactory.getNoad((mcode == null ? 0 : mcode.getValue())));
    }
}