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

package de.dante.extex.language.hyphenation.base;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.ModifiableLanguage;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.util.UnicodeChar;

/**
 * This class stores the values for hyphenations and hypernates words.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class BaseHyphenationTable implements ModifiableLanguage {

    /**
     * The field <tt>exceptionMap</tt> contains the exception words for
     * hyphenation.
     */
    private Map exceptionMap = new HashMap();

    /**
     * The field <tt>hyphenactive</tt> contains the indicator whether these
     * hyphenation is active. If the value is <code>false</code> then no
     * hyphenation points will be inserted.
     */
    private boolean hyphenactive = true;

    /**
     * The field <tt>lefthyphenmin</tt> contains the minimum distance from the
     * left side of a word before hyphenation is performed.
     */
    private long lefthyphenmin = 0;

    /**
     * The field <tt>ligatureBuilder</tt> contains the ligature builder.
     */
    private LigatureBuilder ligatureBuilder = null;

    /**
     * The field <tt>righthyphenmin</tt> contains the minimum distance from the
     * right side of a word before hyphenation is performed.
     */
    private long righthyphenmin = 0;

    /**
     * Creates a new object.
     */
    public BaseHyphenationTable() {

        super();
    }

    /**
     * @see de.dante.extex.language.Language#addHyphenation(
     *      de.dante.extex.interpreter.type.tokens.Tokens,
     *      TypesetterOptions)
     */
    public void addHyphenation(final Tokens word,
            final TypesetterOptions context) throws HyphenationException {

        try {
            exceptionMap.put(createHyphenation(word, context), word);
        } catch (CatcodeException e) {
            throw new HyphenationException(e);
        }
    }

    /**
     * @see de.dante.extex.language.Language#addPattern(
     *      Tokens)
     */
    public void addPattern(final Tokens pattern) throws HyphenationException {

    }

    /**
     * Create the name for the <code>HyphenationTable</code>.
     *
     * @param pattern the pattern
     * @param context the interpreter context
     *
     * @return the name
     *
     * @throws CatcodeException in case of an error
     */
    protected Tokens createHyphenation(final Tokens pattern,
            final TypesetterOptions context) throws CatcodeException {

        Tokens ret = new Tokens();
        TokenFactory tokenFactory = context.getTokenFactory();
        Token t;

        for (int i = 0; i < pattern.length(); i++) {
            t = pattern.get(i);
            if (!t.equals(Catcode.OTHER, '-')) {

                UnicodeChar uc = t.getChar();
                UnicodeChar lc = context.getLccode(uc);
                ret.add(tokenFactory.createToken(Catcode.OTHER, //
                        lc == null ? uc : lc, ""));
            }
        }

        return ret;
    }

    /**
     * @see de.dante.extex.language.Language#getLeftHyphenmin()
     */
    public long getLeftHyphenmin() throws HyphenationException {

        return lefthyphenmin;
    }

    /**
     * @see de.dante.extex.language.ModifiableLanguage#setLigatureBuilder(
     *      de.dante.extex.language.ligature.LigatureBuilder)
     */
    public void setLigatureBuilder(final LigatureBuilder builder) {

        this.ligatureBuilder = builder;
    }

    /**
     * @see de.dante.extex.language.Language#getRightHyphenmin()
     */
    public long getRightHyphenmin() throws HyphenationException {

        return righthyphenmin;
    }

    /**
     * @see de.dante.extex.language.hyphenation.Hyphenator#hyphenate(
     *      de.dante.extex.typesetter.type.node.HorizontalListNode,
     *      de.dante.extex.typesetter.TypesetterOptions,
     *      de.dante.util.UnicodeChar)
     */
    public HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final TypesetterOptions context, final UnicodeChar hyphen)
            throws HyphenationException {

        if (!hyphenactive || nodelist.size() == 0) {
            return nodelist;
        }

        Node n;
        /*
         Font font; // assumption all chars have the same font
         n = nodelist.get(0);
         if (n instanceof CharNode) {
         font = ((CharNode) n).getTypesettingContext().getFont();
         } else if (n instanceof LigatureNode) {
         font = ((LigatureNode) n).getTypesettingContext().getFont();
         } else {
         //TODO gene: error: unimplemented
         throw new RuntimeException("unimplemented");
         }
         UnicodeChar hc = font.getHyphenChar();
         if (hc == null) {
         return nodelist; //TODO gene: check
         }
         */

        TokenFactory factory = context.getTokenFactory();
        Tokens list = new Tokens();

        try {
            for (int i = 0; i < nodelist.size(); i++) {
                n = nodelist.get(i);
                if (n instanceof CharNode) {
                    list.add(factory.createToken(Catcode.OTHER, ((CharNode) n)
                            .getCharacter(), ""));
                } else if (n instanceof LigatureNode) {
                    list.add(factory.createToken(Catcode.OTHER,
                            ((LigatureNode) n).getCharacter(), "")); //TODO gene: check
                } else {
                    //TODO gene: ???
                    return nodelist;
                }
            }
        } catch (CatcodeException e) {
            throw new HyphenationException(e);
        }

        Tokens word = (Tokens) exceptionMap.get(list);
        if (word == null) {
            return nodelist;
        }

        HorizontalListNode nodes = new HorizontalListNode();
        CharNodeFactory cnf = new CharNodeFactory();
        int i = 0;
        for (int j = 0; j < word.length(); j++) {
            Token t = word.get(j);
            if (t.equals(Catcode.OTHER, '-')) {
                nodes.add(new DiscretionaryNode(null, new HorizontalListNode(
                        cnf
                                .newInstance(context.getTypesettingContext(),
                                        hyphen)), null));
            } else {
                nodes.add(nodelist.get(i++));
            }
        }
        return nodes;
    }

    /**
     * @see de.dante.extex.language.ligature.LigatureBuilder#insertLigatures(
     *      de.dante.extex.typesetter.type.NodeList, int)
     */
    public int insertLigatures(final NodeList list, int start)
            throws HyphenationException {

        return this.ligatureBuilder.insertLigatures(list, start);
    }

    /**
     * @see de.dante.extex.language.Language#isHyphenActive()
     */
    public boolean isHyphenActive() throws HyphenationException {

        return hyphenactive;
    }

    /**
     * @see de.dante.extex.language.Language#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active)
            throws HyphenationException {

        hyphenactive = active;
    }

    /**
     * @see de.dante.extex.language.Language#setLeftHyphenmin(long)
     */
    public void setLeftHyphenmin(final long left) throws HyphenationException {

        lefthyphenmin = left;
    }

    /**
     * @see de.dante.extex.language.Language#setRightHyphenmin(long)
     */
    public void setRightHyphenmin(final long right) throws HyphenationException {

        righthyphenmin = right;
    }

}