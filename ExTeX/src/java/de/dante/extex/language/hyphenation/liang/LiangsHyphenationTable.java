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

package de.dante.extex.language.hyphenation.liang;

import java.util.logging.Logger;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.language.hyphenation.exception.DuplicateHyphenationException;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.hyphenation.exception.IllegalTokenHyphenationException;
import de.dante.extex.language.hyphenation.exception.IllegalValueHyphenationException;
import de.dante.extex.language.hyphenation.exception.ImmutableHyphenationException;
import de.dante.extex.language.hyphenation.util.NodeTraverser;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.OtherToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.node.CharNodeFactory;
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
 *  The hyphenation in TeX is based on Liang's thesis.
 *  This algorithm is based on patterns which consist of characters or a
 *  special marker for the beginning and the end of the word. For each
 *  pattern it is characterized how desirable or undesirable it would be
 *  to hyphenate before, between, or after it.
 * </p>
 * <p>
 *  This weighted hyphenation codes cna be represented by integers. The
 *  even integers denote the undesirable positions and the odd numers
 *  denote the optional hyphenation points.
 * </p>
 * <p>
 *  Let us consider the pattern <tt>hyph</tt>} this pattern has associated
 *  to it the code <tt>00300</tt>. The first number corresponds to the
 *  position before the letter h, the second number to the position before
 *  the letter p, and so on. Thus this pattern indicates that a
 *  hyphenation point can be inserted between y and p. This leads to
 *  <tt>hy\-ph</tt> if written explicitely in TeX.
 * </p>
 * <p>
 *  The following table shows some more examples taken from the
 *  original hyphenation patterns of TeX for English. The character .
 *  denotes the beginning or the end of a word. In the TeX patterns the
 *  word pattern and the hyphenation codes are intermixed and the
 *  hyphenation codes 0 are left out.
 * <table>
 *  <tr><td>Word pattern</td><td>Codes</td><td>TeX Pattern</td></tr>
 *  <tr><td>ader.  </td><td> 005000</td><td><tt>ad5er. </tt></td></tr>
 *  <tr><td>.ach   </td><td> 00004 </td><td><tt>.ach4  </tt></td></tr>
 *  <tr><td>sub    </td><td> 0043  </td><td><tt>su4b3  </tt></td></tr>
 *  <tr><td>ty     </td><td> 100   </td><td><tt>1ty    </tt></td></tr>
 *  <tr><td>type   </td><td> 00003 </td><td><tt>type3  </tt></td></tr>
 *  <tr><td>pe.    </td><td> 4000  </td><td><tt>pe.    </tt></td></tr>
 * </table>
 * </p>
 * <p>
 *  To find all hyphenation points in a word all matching patterns have to
 *  be superimposed. During this superposition the higher hyphenation codes
 *  overrule the lower ones.
 * </p>
 * <p>
 *  In the following figure the patterns for the word ``subtype'' are
 *  shown.
 * </p>
 * <pre>
 *  <sub> </sub>s<sub> </sub>u<sub> </sub>b<sub> </sub>t<sub> </sub>y<sub> </sub>p<sub> </sub>e
 *  <sub>0</sub>s<sub>0</sub>u<sub>4</sub>b<sub>3</sub>
 *  <sub> </sub> <sub> </sub> <sub> </sub> <sub>1</sub>t<sub>0</sub>y<sub>0</sub>
 *  <sub> </sub> <sub> </sub> <sub> </sub> <sub>0</sub>t<sub>0</sub>y<sub>0</sub>p<sub>0</sub>e<sub>3</sub>
 *  <sub> </sub> <sub> </sub> <sub> </sub> <sub> </sub> <sub> </sub> <sub>4</sub>p<sub>0</sub>e<sub>3</sub>.
 *  ---------------
 *  <sub>0</sub>s<sub>0</sub>u<sub>4</sub>b<sub>3</sub>t<sub>0</sub>y<sub>4</sub>p<sub>0</sub>e<sub>3</sub>
 * </pre>
 * <p>
 *  The superposition of all patterns leads to the result
 *  <tt>sub\-type\-</tt>. Here two additional parameters come into play.
 *  <tt>\lefthyphenmin</tt> denotes the minimal number of characters before
 *  a hyphenation at the beginning of a word and <tt>\righthyphenmin</tt>
 *  the corresponding length at the end of a word. <tt>\lefthyphenmin</tt>
 *  is set to 2 and <tt>\righthyphenmin</tt> to 3 for English in TeX. Thus
 *  the final hyphen is not considered.
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
     * The field <tt>compressed</tt> contains the indicator that the
     * hyphenation table has been compressed. A compressed table can not be
     * modified any more.
     */
    private boolean compressed = false;

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
     * @see de.dante.extex.language.Language#addPattern(
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
                HyphenTree.superimpose(code, 0, tree.getHyphenationCode());
                expectLetter = false;
            } else {
                throw new IllegalTokenHyphenationException(t.toString());
            }
        }
        tree.setHyphenationCode(code);
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
     * @see de.dante.extex.language.Language#hyphenate(
     *      de.dante.extex.interpreter.type.node.HorizontalListNode,
     *      de.dante.extex.interpreter.context.Context,
     *      Token)
     */
    public HorizontalListNode hyphenate(final HorizontalListNode nodelist,
            final Context context, final Token hyphen) throws GeneralException {

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
        CharNodeFactory cnf = new CharNodeFactory();
        NV nv = null;
        Count index = new Count(0);
        TypesettingContext tc = context.getTypesettingContext(); //TODO gene: is this correct?

        for (int i = 0; i < nodelist.size(); i++) {
            Node node = nodelist.get(i);
            switch (node.countChars()) {
                case 0: // for non-character nodes
                    nodes.add(node);
                    break;
                case 1:
                    if (isHyph[(int) index.getValue()]) {
                        nodes.add(new DiscretionaryNode(null,
                                new HorizontalListNode(cnf.newInstance(//
                                        tc, //
                                        hyphen.getChar())), null));
                    }
                    nodes.add(node);
                    index.add(1);
                    break;
                default:
                    if (nv == null) {
                        nv = new NV(nodes, hyphen, tc, cnf, isHyph);
                    }
                    try {
                        node.visit(nv, index);
                    } catch (GeneralException e) {
                        throw new HyphenationException(e);
                    }
            }
        }

        return nodes;
    }

    /**
     * Getter for compressed.
     *
     * @return the compressed
     */
    protected boolean isCompressed() {

        return this.compressed;
    }

    /**
     * Setter for compressed.
     */
    protected void setCompressed() {

        this.compressed = true;
    }

    /**
     * Write the tree to a logger.
     *
     * @param logger the target logger
     */
    public void dump(final Logger logger) {

        patterns.dump(logger, "");
    }

}