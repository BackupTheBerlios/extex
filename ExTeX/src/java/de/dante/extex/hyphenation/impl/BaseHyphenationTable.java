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

package de.dante.extex.hyphenation.impl;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.hyphenation.exception.HyphenationException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class stores the values for hyphenations and
 * hypernates words.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class BaseHyphenationTable implements HyphenationTable {

    /**
     * The field <tt>hyphenactive</tt> contains the indicator whether these
     * hyphenation is active. If the value is <code>false</code> then no
     * hyphenation points will be inserted.
     */
    private boolean hyphenactive = true;

    /**
     * The field <tt>exceptionMap</tt> contains the exception words for
     * hyphenation.
     */
    private Map exceptionMap = new HashMap();

    /**
     * The field <tt>lefthyphenmin</tt> contains the ...
     */
    private long lefthyphenmin = 0;

    /**
     * The field <tt>righthyphenmin</tt> contains the ...
     */
    private long righthyphenmin = 0;

    /**
     * Creates a new object.
     */
    public BaseHyphenationTable() {

        super();
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#addHyphenation(
     *      de.dante.extex.interpreter.type.tokens.Tokens,
     *      de.dante.extex.interpreter.context.Context)
     */
    public void addHyphenation(final Tokens word, final Context context)
            throws HyphenationException {

        try {
            exceptionMap.put(createHyphenation(word, context), word);
        } catch (CatcodeException e) {
            throw new HyphenationException(e);
        }
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
            final Context context) throws CatcodeException {

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
     * @see de.dante.extex.hyphenation.HyphenationTable#addPattern(
     *      java.lang.String, java.lang.String)
     */
    public void addPattern(final String word, final String pattern) {

    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#getLeftHyphenmin()
     */
    public long getLeftHyphenmin() {

        return lefthyphenmin;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#getRightHyphenmin()
     */
    public long getRightHyphenmin() {

        return righthyphenmin;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#hyphenate(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode,
     *      de.dante.extex.interpreter.context.Context)
     */
    public HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final Context context) throws GeneralException {

        if (!hyphenactive || nodelist.size() == 0) {
            return nodelist;
        }

        Font font; // assumption all chars have the same font
        Node n = nodelist.get(0);
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

        TokenFactory factory = context.getTokenFactory();
        Tokens list = new Tokens();

        for (int i = 0; i < nodelist.size(); i++) {
            n = nodelist.get(i);
            if (n instanceof CharNode) {
                list.add(factory.createToken(Catcode.OTHER, ((CharNode) n)
                        .getCharacter(), ""));
            } else if (n instanceof LigatureNode) {
                list.add(factory.createToken(Catcode.OTHER, ((LigatureNode) n)
                        .getCharacter(), "")); //TODO gene: check
            } else {
                //TODO gene: ???
                return nodelist;
            }
        }

        Tokens word = (Tokens) exceptionMap.get(list);
        if (word == null) {
            return nodelist;
        }

        HorizontalListNode nodes = new HorizontalListNode();

        Tokens hyphen = new Tokens(factory.createToken(Catcode.OTHER, hc, ""));
        int i = 0;
        for (int j = 0; j < word.length(); j++) {
            Token t = word.get(j);
            if (t.equals(Catcode.OTHER, '-')) {
                nodes.add(new DiscretionaryNode(hyphen, Tokens.EMPTY,
                        Tokens.EMPTY));
            } else {
                nodes.add(nodelist.get(i++));
            }
        }
        return nodes;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#isHyphenActive()
     */
    public boolean isHyphenActive() {

        return hyphenactive;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setHyphenActive(boolean)
     */
    public void setHyphenActive(final boolean active) {

        hyphenactive = active;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setLeftHyphenmin(long)
     */
    public void setLeftHyphenmin(final long left) {

        lefthyphenmin = left;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#setRightHyphenmin(long)
     */
    public void setRightHyphenmin(final long right) {

        righthyphenmin = right;
    }

}