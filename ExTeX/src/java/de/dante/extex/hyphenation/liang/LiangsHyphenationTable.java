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

package de.dante.extex.hyphenation.liang;

import de.dante.extex.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.hyphenation.exception.DuplicateHyphenationException;
import de.dante.extex.hyphenation.exception.HyphenationException;
import de.dante.extex.hyphenation.exception.IllegalTokenHyphenationException;
import de.dante.extex.hyphenation.exception.IllegalValueHyphenationException;
import de.dante.extex.hyphenation.exception.ImmutableHyphenationException;
import de.dante.extex.hyphenation.util.NodeTraverser;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.OtherToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class stores the values for hyphenations and
 * hypernates words. It uses Liang's algorithm as described in the TeX Book.
 *
 * <h2>Liangs Algorithm</h2>
 *
 * <p>
 * Liangs algorithm is based on patterns which are matched against the current
 * word at hand. To each pattern has a code assigned to it which is used to
 * determine where hyphenations are feasible.
 * </p>
 * <p>
 * Consider for example the pattern
 * <tt><sub>0</sub>a<sub>0</sub>m<sub>5</sub>i<sub>0</sub>l<sub>0</sub>y<sub>0</sub></tt>
 * where the letters denote characters to by typeset and the subscribed numbers
 * indicate the hyphenation code at the indicated positions. The odd numbers
 * denote positions where no hyphenation is desirable. Odd numbers denote
 * positions where hypehnation is desirable. Higher values overrule lower
 * values which may come later for the same position.
 * </p>
 * <p>
 * To find all hyphenation points for a word the superposition for all known
 * matching patterns at all positions have to be computed. The superposition
 * is determined by the highest value of all hyphenation codes for this
 * position.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class LiangsHyphenationTable extends BaseHyphenationTable {

    /**
     * The field <tt>BORDER</tt> contains the Unicode character internally used
     * as a marker for the beginning and the end of the word.
     */
    private static final UnicodeChar BORDER = null;

    /**
     * The field <tt>patterns</tt> contains the tree of hyphenation patterns.
     */
    private HyphenTree patterns = new HyphenTree(new char[0]);

    /**
     * The field <tt>compressed</tt> contains the indicator that the
     * hyphenation table has been compressed. A compressed table can not be
     * modified any more.
     */
    private boolean compressed = false;

    /**
     * Creates a new object.
     */
    public LiangsHyphenationTable() {

        super();
    }

    /**
     * This methods allows the caller to add another pattern
     *
     * @param pattern a sequence of tokens alternatively of type other and
     *  letter. The other tokens must be numbers.
     *
     * @throws IllegalValueHyphenationException in case that an other token
     *  does not carry a digit
     * @throws IllegalTokenHyphenationException in case that ...
     * @throws DuplicateHyphenationException in case that a hyphenation pattern
     *  is tried to be added a second time
     * @throws ImmutableHyphenationException in case that ...
     *
     * @see de.dante.extex.hyphenation.HyphenationTable#addPattern(
     *      Tokens)
     */
    public void addPattern(final Tokens pattern)
            throws IllegalValueHyphenationException,
                IllegalTokenHyphenationException,
                DuplicateHyphenationException,
                ImmutableHyphenationException {

        if (compressed) {
            throw new ImmutableHyphenationException(null);
        }

        int length = pattern.length();

        if (length == 0) {
            return;
        }

        char[] code = new char[(length + 1) / 2];
        int codeIndex = 0;
        HyphenTree tree = patterns;
        boolean expectLetter = false;

        for (int i = 0; i < length; i++) {
            Token t = pattern.get(i);
            UnicodeChar c = t.getChar();
            if (t instanceof OtherToken) {
                int hyphenCode = c.getCodePoint();
                if (expectLetter) {
                    throw new IllegalTokenHyphenationException(t.toString());
                } else if (hyphenCode < '0' || hyphenCode > '9') {
                    throw new IllegalValueHyphenationException(t.toString());
                }
                code[codeIndex++] = (char) hyphenCode;
                expectLetter = true;
            } else if (t instanceof LetterToken) {
                if (!expectLetter) {
                    throw new IllegalTokenHyphenationException(t.toString());
                }
                tree = tree.insert(c, null);
                HyphenTree.superimpose(code, 0, tree.getHc());
                expectLetter = false;
            } else {
                throw new IllegalTokenHyphenationException(t.toString());
            }
        }
        tree.setHc(code);
        tree.superimposeAll(code);
    }

    /**
     * Getter for patterns.
     * This method is meant for testing purposes only.
     *
     * @return the patterns
     */
    protected HyphenTree getPatterns() {

        return this.patterns;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationTable#hyphenate(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final Context context, final Tokens hyphen) throws GeneralException {

        if (!isHyphenActive()) {
            return nodelist;
        }

        int len = nodelist.countChars();
        if (len <= 1) {
            return nodelist;
        }

        HorizontalListNode nodes = super.hyphenate(nodelist, context, hyphen);
        if (nodes != nodelist) {
            return nodes;
        }

        int leftHyphenMin = (int) getLeftHyphenmin();
        int rightHyphenMin = (int) getRightHyphenmin();
        if (len < leftHyphenMin + rightHyphenMin) {
            return nodelist;
        }

        char[] hyph = new char[len + 2];
        UnicodeChar[] chars = new UnicodeChar[len + 2];
        int idx = 0; // pointer into hyph; in sync with the current char
        NodeTraverser nt = new NodeTraverser(nodelist);
        chars[idx++] = BORDER;

        for (UnicodeChar c = nt.next(); c != null; c = nt.next()) {
            chars[idx++] = c;
        }
        chars[idx] = BORDER;

        for (int i = 0; i < len; i++) {
            HyphenTree.superimpose(hyph, i, patterns.get(chars, i));
        }

        for (int i = 0; i < leftHyphenMin; i++) {
            hyph[i] = '0';
        }
        for (int i = 0; i < rightHyphenMin; i++) {
            hyph[hyph.length - i - 1] = '0';
        }

        boolean hasNoHyphen = true;
        boolean[] isHyph = new boolean[hyph.length];

        for (int i = 0; i < hyph.length; i++) {
            if ((((int) hyph[i]) & 1) != 0) {
                isHyph[i] = true;
                hasNoHyphen = false;
            } else {
                isHyph[i] = false;
            }
        }
        if (hasNoHyphen) {
            return nodelist;
        }

        nodes = new HorizontalListNode();
        NV nv = new NV(nodes, hyphen, isHyph);
        Count index = new Count(0);

        for (int i = 0; i < nodelist.size(); i++) {
            Node node = nodelist.get(i);
            switch (node.countChars()) {
                case 0: // just in case; for non-character nodes
                    nodes.add(node);
                    break;
                case 1:
                    if (isHyph[(int) index.getValue()]) {
                        nodes.add(new DiscretionaryNode(Tokens.EMPTY, hyphen,
                                Tokens.EMPTY));
                    }
                    nodes.add(node);
                    index.add(1);
                    break;
                default:
                    try {
                        node.visit(nv, index);
                    } catch (GeneralException e) {
                        throw new HyphenationException(e);
                    }
            }
        }

        return nodes;
    }

}