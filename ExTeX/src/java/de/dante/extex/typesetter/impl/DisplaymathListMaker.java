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

package de.dante.extex.typesetter.impl;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.i18n.MathHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public class DisplaymathListMaker extends AbstractListMaker {

    /**
     * The field <tt>nodes</tt> contains the horizontal node list.
     */
    private HorizontalListNode nodes = new HorizontalListNode();

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public DisplaymathListMaker(final Manager manager) {

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
     * @see de.dante.extex.typesetter.ListMaker#treatMathShift(
     *      de.dante.extex.scanner.Token, TokenSource)
     * @see "TeX -- The Program [1197]"
     */
    public void treatMathShift(final Token t, final TokenSource source)
            throws GeneralException {

        Token next = source.getToken();

        if (next == null) {
            throw new MathHelpingException(t.toString());
        } else if (!next.isa(Catcode.MATHSHIFT)) {
            throw new HelpingException(getLocalizer(),"TTP.DisplayMathEnd");
        }

        getManager().endParagraph();

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatSubMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void treatSubMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        //TODO _ unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatSupMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void treatSupMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        //TODO _ unimplemented
        throw new RuntimeException("unimplemented");
    }

}