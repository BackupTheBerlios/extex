/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
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
 * @version $Revision: 1.5 $
 */
public abstract class AbstractListMaker implements ListMaker {
    /** The manager to ask for global changes.
     */
    protected Manager manager;

    /**
     * Creates a new object.
     *
     * @param manager the manager to ask for global changes
     */
    public AbstractListMaker(final Manager manager) {
        super();
        this.manager = manager;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public abstract Mode getMode();

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(int)
     */
    public void setSpacefactor(final Count f) throws GeneralException {
        throw new GeneralHelpingException("TTP.ImproperSForPD",
                                          "spacefactor");
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.typesetter.Node)
     */
    public abstract void add(final Node node) throws GeneralException;

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.util.UnicodeChar)
     */
    public abstract void add(final TypesettingContext font,
        final UnicodeChar symbol) throws GeneralException;

    /**
     * @see de.dante.extex.typesetter.ListMaker#addGlue(de.dante.extex.interpreter.type.Glue)
     */
    public abstract void addGlue(final Glue g) throws GeneralException;

    /**
     * @see de.dante.extex.typesetter.ListMaker#addSpace(TypesettingContext)
     */
    public abstract void addSpace(final TypesettingContext typesettingContext,
        final Count spacefactor) throws GeneralException;

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public abstract NodeList close();

    /**
     * @see de.dante.extex.typesetter.ListMaker#par()
     */
    public abstract void par() throws GeneralException;

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleDisplaymath()
     */
    public void toggleDisplaymath() throws GeneralException {
        ListMaker hlist = new DisplaymathListMaker(manager);
        manager.push(hlist);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#toggleMath()
     */
    public void toggleMath() throws GeneralException {
        ListMaker hlist = new MathListMaker(manager);
        manager.push(hlist);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(de.dante.extex.interpreter.type.Dimen)
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {
        throw new GeneralHelpingException("TTP.ImproperSForPD",
                                          "prevdepth");
    }
}
