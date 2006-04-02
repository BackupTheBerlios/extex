/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.MissingMathException;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.InvalidSpacefactorException;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.exception.TypesetterHelpingException;
import de.dante.extex.typesetter.exception.TypesetterUnsupportedException;
import de.dante.extex.typesetter.listMaker.math.DisplaymathListMaker;
import de.dante.extex.typesetter.listMaker.math.MathListMaker;
import de.dante.util.Locator;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This abstract class provides some methods common to all ListMakers.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.22 $
 */
public abstract class AbstractListMaker implements ListMaker {

    /**
     * The field <tt>locator</tt> contains the locator pointing to the start.
     */
    private Locator locator;

    /**
     * The field <tt>manager</tt> contains the manager to ask for global
     * changes.
     */
    private ListManager manager;

    /**
     * Creates a new object.
     *
     * @param theManager the manager to ask for global changes
     * @param locator the locator
     */
    public AbstractListMaker(final ListManager theManager, final Locator locator) {

        super();
        this.manager = theManager;
        this.locator = locator;
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
     * @see de.dante.extex.typesetter.ListMaker#getLocator()
     */
    public Locator getLocator() {

        return locator;
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
     * @see de.dante.extex.typesetter.ListMaker#getPrevDepth()
     */
    public FixedDimen getPrevDepth() throws TypesetterUnsupportedException {

        throw new TypesetterUnsupportedException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#getSpacefactor()
     */
    public long getSpacefactor() throws TypesetterUnsupportedException {

        throw new TypesetterUnsupportedException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#leftBrace()
     */
    public void leftBrace() {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#mathShift(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void mathShift(final Context context, final TokenSource source,
            final Token t) throws TypesetterException, ConfigurationException {

        try {
            Token next = source.getToken(context);

            if (next == null) {
                throw new TypesetterException(new MissingMathException(t
                        .toString()));
            } else if (!next.isa(Catcode.MATHSHIFT)) {
                source.push(next);
                manager.push(new MathListMaker(manager, source.getLocator()));
                source.push(context.getToks("everymath"));
            } else {
                manager.push(new DisplaymathListMaker(manager, source
                        .getLocator()));
                source.push(context.getToks("everydisplay"));
            }
            //TODO gene: ??? context.setCount("fam", -1, false);

        } catch (TypesetterException e) {
            throw e;
        } catch (InterpreterException e) {
            throw new TypesetterException(e);
        }
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#rightBrace()
     */
    public void rightBrace() throws TypesetterException {

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setPrevDepth(
     *      de.dante.extex.interpreter.type.dimen.Dimen)
     */
    public void setPrevDepth(final FixedDimen pd)
            throws TypesetterUnsupportedException {

        throw new TypesetterUnsupportedException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#setSpacefactor(
     *      de.dante.extex.interpreter.type.count.Count)
     */
    public void setSpacefactor(final FixedCount f)
            throws TypesetterUnsupportedException,
                InvalidSpacefactorException {

        throw new TypesetterUnsupportedException();
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#subscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void subscriptMark(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token token)
            throws TypesetterException {

        throw new TypesetterException(
                new MissingMathException(token.toString()));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#superscriptMark(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void superscriptMark(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final Token token) throws TypesetterException {

        throw new TypesetterException(
                new MissingMathException(token.toString()));
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#tab(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void tab(final Context context, final TokenSource source,
            final Token token)
            throws TypesetterException,
                ConfigurationException {

        throw new TypesetterHelpingException(getMyLocalizer(),
                "TTP.MisplacedTabMark", token.toString());
    }

}