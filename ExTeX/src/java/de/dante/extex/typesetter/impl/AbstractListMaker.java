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
import de.dante.extex.i18n.PanicException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This abstract class provides some methods common to all ListMakers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.18 $
 */
public abstract class AbstractListMaker implements ListMaker {

    /**
     * The field <tt>manager</tt> contains the manager to ask for global
     * changes.
     */
    private Manager manager;

    /**
     * Creates a new object.
     *
     * @param theManager the manager to ask for global changes
     */
    public AbstractListMaker(final Manager theManager) {

        super();
        this.manager = theManager;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#add(
     *      de.dante.extex.typesetter.type.noad.Noad)
     */
    public void add(final Noad noad) throws GeneralException {

        throw new PanicException(getMyLocalizer(), "UnexpectedNoad");
    }

    /**
     * Getter for the localizer.
     *
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(this.getClass().getName());
    }

    /**
     * Getter for manager.
     *
     * @return the manager.
     */
    public Manager getManager() {

        return manager;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getMode()
     */
    public abstract Mode getMode();

    /**
     * Getter for the localizer.
     *
     * @return the localizer
     */
    protected Localizer getMyLocalizer() {

        return LocalizerFactory.getLocalizer(AbstractListMaker.class.getName());
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {

        throw new GeneralException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void setSpacefactor(final Count f) throws GeneralException {

        throw new GeneralException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatMathShift(
     *      de.dante.extex.scanner.Token, TokenSource)
     */
    public void treatMathShift(final Token t, final TokenSource source)
            throws GeneralException {

        Token next = source.getToken();

        if (next == null) {
            throw new MathHelpingException(t.toString());
        } else if (!next.isa(Catcode.MATHSHIFT)) {
            source.push(next);
            manager.push(new MathListMaker(manager));
        } else {
            manager.push(new DisplaymathListMaker(manager));
        }
        //TODO everymath
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatSubMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void treatSubMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        throw new MathHelpingException(token.toString());
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatSupMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void treatSupMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        throw new MathHelpingException(token.toString());
    }

    /**
     * @see de.dante.extex.typesetter.Typesetter#tab(
     *      Context,
     *      TokenSource, de.dante.extex.scanner.Token)
     */
    public void tab(final Context context, final TokenSource source, final Token token)
            throws GeneralException {

        throw new HelpingException(getMyLocalizer(), "TTP.MisplacedTabMark",
                token.toString());
    }
}