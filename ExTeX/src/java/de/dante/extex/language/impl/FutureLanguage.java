/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

import java.util.List;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.UnicodeChar;

/**
 * This class implements the future pattern for a language object. The real
 * object creation or loading is delayed until it is clear whether the
 * loading or the creation should be performed.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class FutureLanguage implements Language {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>creator</tt> contains the creator which should be contacted
     * to perform the real task.
     */
    private LanguageCreator creator;

    /**
     * The field <tt>index</tt> contains the name of the language for the
     * creator.
     */
    private String index;

    /**
     * The field <tt>language</tt> contains the language for which we are acting
     * as proxy.
     */
    private Language language = null;

    /**
     * Creates a new object.
     *
     * @param index the name of the language for the creator
     * @param creator the creator which should be contacted to perform the
     *  real task
     */
    public FutureLanguage(final String index, final LanguageCreator creator) {

        super();
        this.index = index;
        this.creator = creator;
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#addHyphenation(
     *      de.dante.extex.interpreter.type.tokens.Tokens,
     *      TypesetterOptions)
     */
    public void addHyphenation(final Tokens word,
            final TypesetterOptions context) throws HyphenationException {

        load().addHyphenation(word, context);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#addPattern(
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void addPattern(final Tokens pattern) throws HyphenationException {

        create().addPattern(pattern);
    }

    /**
     * Create a new instance if required.
     *
     * @return the language instance to be used
     *
     * @throws HyphenationException in case of an error
     */
    private Language create() throws HyphenationException {

        if (language == null) {
            language = creator.createLanguageInstance(index);
        }
        return language;
    }

    /**
     * @see de.dante.extex.language.word.WordTokenizer#findWord(
     *      de.dante.extex.typesetter.type.NodeList, int, java.util.List)
     */
    public int findWord(final NodeList nodes, final int start, final List word)
            throws HyphenationException {

        return create().findWord(nodes, start, word);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#getLeftHyphenmin()
     */
    public long getLeftHyphenmin() throws HyphenationException {

        return load().getLeftHyphenmin();
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#getRightHyphenmin()
     */
    public long getRightHyphenmin() throws HyphenationException {

        return load().getRightHyphenmin();
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#hyphenate(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      de.dante.extex.typesetter.TypesetterOptions,
     *      de.dante.util.UnicodeChar,
     *      int,
     *      boolean,
     *      de.dante.extex.typesetter.type.node.factory.NodeFactory)
     */
    public boolean hyphenate(final HorizontalListNode nodelist,
            final TypesetterOptions context, final UnicodeChar hyphen,
            final int start, final boolean forall, final NodeFactory nodeFactory)
            throws HyphenationException {

        return load().hyphenate(nodelist, context, hyphen, start, forall,
                nodeFactory);
    }

    /**
     * @see de.dante.extex.language.ligature.LigatureBuilder#insertLigatures(
     *      de.dante.extex.typesetter.type.NodeList, int)
     */
    public int insertLigatures(final NodeList list, final int start)
            throws HyphenationException {

        return load().insertLigatures(list, start);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#isHyphenActive()
     */
    public boolean isHyphenActive() throws HyphenationException {

        return load().isHyphenActive();
    }

    /**
     * Load or create a new instance if required.
     *
     * @return the new instance
     *
     * @throws HyphenationException in case of an error
     */
    private Language load() throws HyphenationException {

        if (language == null) {
            language = creator.loadLanguageInstance(index);
        }
        return language;
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active)
            throws HyphenationException {

        create().setHyphenActive(active);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#setLeftHyphenmin(long)
     */
    public void setLeftHyphenmin(final long left) throws HyphenationException {

        create().setLeftHyphenmin(left);
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#setRightHyphenmin(long)
     */
    public void setRightHyphenmin(final long right) throws HyphenationException {

        create().setRightHyphenmin(right);
    }

}
