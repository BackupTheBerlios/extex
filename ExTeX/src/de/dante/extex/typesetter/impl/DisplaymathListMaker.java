/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.typesetter.impl;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
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
 * @version $Revision: 1.9 $
 */
public class DisplaymathListMaker extends AbstractListMaker implements
        ListMaker {

    /**
     * The field <tt>nodes</tt> ...
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
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public void add(final TypesettingContext font, final UnicodeChar symbol) {

        nodes.add(getManager().getCharNodeFactory().newInstance(font, symbol));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.interpreter.type.Count)
     */
    public void addSpace(final TypesettingContext typesettingContext,
            final Count spacefactor) throws GeneralException {

        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(
     *      de.dante.extex.interpreter.type.Glue)
     */
    public void addGlue(final Glue g) throws GeneralException {

        // TODO Auto-generated method stub
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     * @see "TeX -- The Program [1047]"
     */
    public void par() throws GeneralException {

        getManager().closeTopList();
        throw new GeneralHelpingException("TTP.MissingDollar");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public Mode getMode() {

        return Mode.DISPLAYMATH;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() {

        return nodes;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
     */
    public void toggleDisplaymath() throws GeneralException {

        getManager().closeTopList();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleMath()
     * @see "TeX -- The Program [1197]"
     */
    public void toggleMath() throws GeneralException {

        getManager().closeTopList();
        throw new GeneralHelpingException("TTP.DisplayMathEnd");
    }

}