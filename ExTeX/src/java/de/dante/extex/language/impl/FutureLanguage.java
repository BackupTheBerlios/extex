/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language.impl;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;

/**
 * This class implements the future pattern for a language object. The real
 * object creation or loading is delayed until it is clear whether the
 * loading or the creation should be performed.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FutureLanguage implements Language {

    /**
     * The field <tt>index</tt> contains the ...
     */
    private String index;

    /**
     * The field <tt>creator</tt> contains the ...
     */
    private LanguageCreator creator;

    /**
     * Creates a new object.
     *
     * @param index
     * @param creator
     */
    public FutureLanguage(final String index, final LanguageCreator creator) {

        super();
        this.index = index;
        this.creator = creator;
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#addHyphenation(
     *      de.dante.extex.interpreter.type.tokens.Tokens,
     *      de.dante.extex.interpreter.context.Context)
     */
    public void addHyphenation(final Tokens word, final Context context)
            throws HyphenationException {

        creator.loadLanguageInstance(index).addHyphenation(word, context);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#addPattern(
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void addPattern(final Tokens pattern) throws HyphenationException {

        creator.createLanguageInstance(index).addPattern(pattern);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#getLeftHyphenmin()
     */
    public long getLeftHyphenmin() throws HyphenationException {

        return creator.loadLanguageInstance(index).getLeftHyphenmin();
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#getRightHyphenmin()
     */
    public long getRightHyphenmin() throws HyphenationException {

        return creator.loadLanguageInstance(index).getRightHyphenmin();
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#hyphenate(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.scanner.type.Token)
     */
    public HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final Context context, final Token hyphen) throws GeneralException {

        return creator.loadLanguageInstance(index).hyphenate(nodelist, context,
                hyphen);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#isHyphenActive()
     */
    public boolean isHyphenActive() throws HyphenationException {

        return creator.loadLanguageInstance(index).isHyphenActive();
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active) throws HyphenationException {

        creator.createLanguageInstance(index).setHyphenActive(active);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#setLeftHyphenmin(long)
     */
    public void setLeftHyphenmin(final long left) throws HyphenationException {

        creator.createLanguageInstance(index).setLeftHyphenmin(left);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#setRightHyphenmin(long)
     */
    public void setRightHyphenmin(final long right) throws HyphenationException {

        creator.createLanguageInstance(index).setRightHyphenmin(right);
    }

    /**
     * @see de.dante.extex.language.ligature.LigatureBuilder#insertLigatures(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void insertLigatures(final NodeList list) throws HyphenationException {

        creator.loadLanguageInstance(index).insertLigatures(list);
    }

}