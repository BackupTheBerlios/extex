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

package de.dante.extex.typesetter.listMaker;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingMathException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.listMaker.math.DisplaymathListMaker;
import de.dante.extex.typesetter.listMaker.math.MathListMaker;
import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This abstract class provides some methods common to all ListMakers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public abstract class AbstractListMaker implements ListMaker {

    /**
     * The field <tt>manager</tt> contains the manager to ask for global
     * changes.
     */
    private ListManager manager;

    /**
     * Creates a new object.
     *
     * @param theManager the manager to ask for global changes
     */
    public AbstractListMaker(final ListManager theManager) {

        super();
        this.manager = theManager;
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
    public ListManager getManager() {

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
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      Context, TokenSource, de.dante.extex.scanner.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws GeneralException {

        Token next = source.getToken(context);

        if (next == null) {
            throw new MissingMathException(t.toString());
        } else if (!next.isa(Catcode.MATHSHIFT)) {
            source.push(next);
            manager.push(new MathListMaker(manager));
            source.push(context.getToks("everymath"));
        } else {
            manager.push(new DisplaymathListMaker(manager));
            source.push(context.getToks("everydisplay"));
        }
        //TODO gene: ??? context.setCount("fam", -1, false);
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setPrevDepth(final Dimen pd) throws GeneralException {

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void setSpacefactor(final Count f) throws GeneralException {

        throw new UnsupportedOperationException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Token token) throws GeneralException {

        throw new MissingMathException(token.toString());
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#superscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Token token)
            throws GeneralException {

        throw new MissingMathException(token.toString());
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#tab(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token token) throws GeneralException {

        throw new HelpingException(getMyLocalizer(), "TTP.MisplacedTabMark",
                token.toString());
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#leftBrace()
     */
    public void leftBrace() {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#rightBrace()
     */
    public void rightBrace() throws GeneralException {

    }

}