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
import de.dante.extex.hyphenation.util.NodeTraverser;
import de.dante.extex.interpreter.context.Context;
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
 * @version $Revision: 1.1 $
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
     * Creates a new object.
     */
    public LiangsHyphenationTable() {

        super();
    }

    /**
     * This methods allows the caller to add another pattern
     *
     * @throws IllegalValueHyphenationException
     * @throws IllegalTokenHyphenationException
     * @throws DuplicateHyphenationException
     *
     * @see de.dante.extex.hyphenation.HyphenationTable#addPattern(
     *      Tokens)
     */
    public void addPattern(final Tokens pattern)
            throws IllegalValueHyphenationException,
                IllegalTokenHyphenationException,
                DuplicateHyphenationException {

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
                superimpose(code, 0, tree.getHc());
                expectLetter = false;
            } else {
                throw new IllegalTokenHyphenationException(t.toString());
            }
        }
        tree.setHc(code);
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
     * Insert discretionaries into a list of nodes.
     *
     * @param nodelist the nodelist to augment with discretionary nodes
     * @param hyph the hyphen codes
     * @param hyphen the hyphen mark to insert
     *
     * @return the horizontal list containing additional discretionary nodes
     *  as required
     *
     * @throws HyphenationException in case of an error
     */
    private HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final char[] hyph, final Tokens hyphen) throws HyphenationException {

        boolean hasNoHyphen = true;
        for (int i = 0; i < hyph.length; i++) {
            if ((((int) hyph[i]) & 1) != 0) {
                hasNoHyphen = false;
            }
        }
        if (hasNoHyphen) {
            return nodelist;
        }

        HorizontalListNode nodes = new HorizontalListNode();
        int idx = 0;
        NV nv = new NV(nodes);

        for (int i = 0; i < nodelist.size(); i++) {
            Node node = nodelist.get(i);
            switch (node.countChars()) {
                case 0: // just in case; this should never happen
                    if ((((int) hyph[idx]) & 1) != 0) {
                        nodes.add(new DiscretionaryNode(Tokens.EMPTY, hyphen,
                                Tokens.EMPTY));
                    }
                    nodes.add(node);
                    break;
                case 1:
                    if ((((int) hyph[idx]) & 1) != 0) {
                        nodes.add(new DiscretionaryNode(Tokens.EMPTY, hyphen,
                                Tokens.EMPTY));
                    }
                    nodes.add(node);
                    idx++;
                    break;
                default:
                    try {
                        node.visit(nv, null);
                    } catch (GeneralException e) {
                        throw new HyphenationException(e);
                    }
            }
        }

        return nodes;
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
        if (len == 0) {
            return nodelist;
        }

        HorizontalListNode nodes = super.hyphenate(nodelist, context, hyphen);
        if (nodes != nodelist) {
            return nodes;
        }

        char[] hyph = new char[len + 2];
        UnicodeChar[] chars = new UnicodeChar[len + 2];
        int i = 0; // pointer into hyph; in sync with the current char
        NodeTraverser nt = new NodeTraverser(nodelist);
        chars[i++] = BORDER;

        for (UnicodeChar c = nt.next(); c != null; c = nt.next()) {
            chars[i++] = c;
        }
        chars[i] = BORDER;

        for (i = 0; i < len; i++) {
            superimpose(hyph, i, patterns.get(chars, i));
        }

        for (i = (int) getLeftHyphenmin() - 1; i >= 0; i--) {
            hyph[i] = '0';
        }
        for (i = (int) getRightHyphenmin() - 1; i >= 0; i--) {
            hyph[hyph.length - i - 1] = '0';
        }

        return hyphenate(nodelist, hyph, hyphen);
    }

    /**
     * Superimpose two hyphenation code arrays.
     * The target array is modified. It is considered from a starting position
     * onwards. The target array has to be long enough such that the source
     * array fits into it entirely.
     * <p>
     * At any position considered the maximum value of target and source is
     * stored in the target. The comparison of the array is performed at the
     * positions <i>i</i> in <tt>source</tt> and <i>i + start</i> in
     * <tt>target</tt> for each <i>i</i> in pointing to a hyphenation code in
     * <tt>source</tt>.
     * </p>
     * <p>
     * If source is <code>null</code> then nothing is done.
     * </p>
     *
     * @param code the target array to modify
     * @param start the start index in code
     * @param source the reference array to read from
     */
    protected void superimpose(final char[] code, final int start,
            final char[] source) {

        if (source == null) {
            return;
        }
        int j = start;
        for (int i = 0; i < source.length; i++) {
            if (code[j] < source[i]) {
                code[j] = source[i];
            }
            j++;
        }
    }
}