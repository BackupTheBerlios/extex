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

import de.dante.extex.i18n.MathHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.type.noad.MathCharNoad;
import de.dante.extex.typesetter.type.noad.MathList;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathListMaker extends AbstractListMaker implements NoadConsumer {

    /**
     * The field <tt>nodes</tt> contains the list of nodes encapsulated in this
     * instance.
     */
    private MathList noades = new MathList();

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

        noades.add(noad);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.Node)
     */
    public void add(final Node node) {

        noades.add(node);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.glue.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        // TODO unimplemented
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
     * @return the node list enclosed in this instance
     *
     * @see de.dante.extex.typesetter.ListMaker#close()
     * @see "TeX -- The Program [719]"
     */
    public NodeList close() {

        HorizontalListNode hlist = new HorizontalListNode();
        int i = mlistToHlist(hlist, 0);
        while (i < noades.size()) {
            i = mlistToHlist(hlist, i);
        }
        return hlist;
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
     * Convert a single noad of this mlist into the corresponding hlist.
     *
     * @param hlist the target hlist
     * @param index the index of the noad to convert
     *
     * @return the next index of an unconverted noad
     *
     * @see "TeX -- The Program [720]"
     */
    private int mlistToHlist(final HorizontalListNode hlist, final int index) {

        // TODO unimplemented
        throw new RuntimeException("unimplemented");
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

        noades.remove(noades.size() - 1);
    }

    /**
     * Add a math character node to the list.
     *
     * @param tc the typesetting context for the symbol. This  parameter is
     *  ignored in math mode.
     * @param symbol the symbol to add
     *
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void treatLetter(final TypesettingContext tc,
            final UnicodeChar symbol) {

        int fam = 0; //TODO determine correct family
        //TODO: use a factory for math chars
        noades.add(new MathCharNoad(fam, symbol));
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
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final TypesettingContext context,
            final Token token) throws GeneralException {

        //TODO _ unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#superscriptMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void superscriptMark(final TypesettingContext context,
            final Token token) throws GeneralException {

        //TODO ^ unimplemented
        throw new RuntimeException("unimplemented");
    }

}