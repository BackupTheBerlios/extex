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
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DisplaymathListMaker extends AbstractListMaker
        implements
            NoadConsumer {

    /**
     * @see de.dante.extex.typesetter.listMaker.NoadConsumer#add(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void add(final Noad noad) throws GeneralException {

        // TODO unimplemented

    }

    /**
     * The field <tt>nodes</tt> contains the horizontal node list.
     */
    private HorizontalListNode nodes = new HorizontalListNode();

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public DisplaymathListMaker(final ListManager manager) {

        super(manager);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.Node)
     */
    public void add(final Node c) {

        nodes.add(c);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        // TODO Auto-generated method stub
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) throws GeneralException {

        // TODO Auto-generated method stub
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() {

        return nodes;
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

        return Mode.DISPLAYMATH;
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
        throw new MathHelpingException("\\par"); //TODO other string?
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#removeLastNode()
     */
    public void removeLastNode() {

        nodes.remove(nodes.size() - 1);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatLetter(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void treatLetter(final TypesettingContext font,
            final UnicodeChar symbol) {

        nodes.add(getManager().getCharNodeFactory().newInstance(font, symbol));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      Context, TokenSource, de.dante.extex.scanner.Token)
     * @see "TeX -- The Program [1197]"
     */
    public void mathShift(Context context, final TokenSource source, final Token t)
            throws GeneralException {

        Token next = source.getToken();

        if (next == null) {
            throw new MathHelpingException(t.toString());
        } else if (!next.isa(Catcode.MATHSHIFT)) {
            throw new HelpingException(getLocalizer(), "TTP.DisplayMathEnd");
        }

        getManager().endParagraph();

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        //TODO _ unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#superscriptMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void superscriptMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        //TODO ^ unimplemented
        throw new RuntimeException("unimplemented");
    }

}