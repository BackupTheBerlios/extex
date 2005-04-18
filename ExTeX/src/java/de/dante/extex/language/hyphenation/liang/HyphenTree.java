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

package de.dante.extex.language.hyphenation.liang;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import de.dante.extex.language.hyphenation.exception.DuplicateHyphenationException;
import de.dante.util.UnicodeChar;

/**
 * <h2>Data Structures for Liangs Algorithm</h2>
 *
 * <p>
 *  The basic data structure to store hyphenation patterns is a tree with
 *  arbitrary branching factor. The path from the root to a node determines the
 *  word under which a hyphenation code is stored.
 * </p>
 * <p>
 *  The hyphenation code is a vector of characters. They are used as a means to
 *  store short numbers.
 * </p>
 * <p>
 *  The code stored in a node of the hyphen tree represents the superposition of
 *  all codes having an initial sequence in common with the current sequence.
 *  Thus the superposition has to be stores when a new pattern is added. At
 *  runtime it is only necessary to retrieve a single code sequence for each
 *  position of the word.
 * </p>
 *
 * TODO gene: missing JavaDoc (incomplete).
 *
 * The value <code>null</code> as character is interpreted as the left
 * or right word boundary.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
class HyphenTree implements Serializable {

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
    public static void superimpose(final char[] code, final int start,
            final char[] source) {

        if (source != null) {
            int j = start;

            for (int i = 0; i < source.length; i++) {
                if (code[j] < source[i]) {
                    code[j] = source[i];
                }
                j++;
            }
        }
    }

    /**
     * The field <tt>hc</tt> contains the hyphenation code for the position
     * left of the character represented by this instance.
     */
    private char[] hc;

    /**
     * The field <tt>nextTree</tt> contains the map for the next characters.
     */
    private Map nextTree = null;

    /**
     * Creates a new object.
     *
     * @param hc the hyphenation code to start with
     */
    public HyphenTree(final char[] hc) {

        super();
        this.hc = hc;
    }

    /**
     * This method dumps a hyphenation tree to a logger.
     *
     * @param logger the target logger
     * @param prefix the initial string prepended before any line of output
     */
    public void dump(final Logger logger, final String prefix) {

        logger.info(toString());
    }

    /**
     * Traverse the tree and return the appropriate hyphenation code vector.
     * If none is found then <code>null</code> is returned.
     * The hyphenation code vector returned is the longest vector stored for
     * any sequence of characters starting with the given word.
     *
     * @param chars the array of characters to analyze
     * @param start the start index in chars to begin with
     *
     * @return the hyphenation code found or <code>null</code>
     */
    public char[] get(final UnicodeChar[] chars, final int start) {

        HyphenTree tree = this;
        char[] hyph = null;

        if (chars.length == 0) {
            return tree.getHyphenationCode();
        }

        for (int i = start; i < chars.length; i++) {
            char[] code = tree.getHyphenationCode();
            if (code != null) {
                hyph = code;
            }
            tree = tree.getNext(chars[i]);
            if (tree == null) {
                return hyph;
            }
        }

        return hyph;
    }

    /**
     * Getter for hyphenation code.
     *
     * @return the hyphenation code
     */
    public char[] getHyphenationCode() {

        return this.hc;
    }

    /**
     * Getter for the next tree.
     *
     * @param uc the unicode character to get the tree for
     *
     * @return the next tree
     */
    public HyphenTree getNext(final UnicodeChar uc) {

        return (this.nextTree != null
                ? (HyphenTree) this.nextTree.get(uc)
                : null);
    }

    /**
     * Create a new branch in the HyphenTree for a given character.
     * The hyphenation code is stored if the branch is new. If the branch exists
     * already then the hyphenation code is stored in the branch after it has
     * been checked for an existing value. An existing value leads to an
     * exception.
     *
     * @param uc the Unicode character to insert
     * @param hyph the hyphenation code to insert
     *
     * @return the next branch associated with the character
     *
     * @throws DuplicateHyphenationException in case that the hyphen code
     *  is already set
     */
    public HyphenTree insert(final UnicodeChar uc, final char[] hyph)
            throws DuplicateHyphenationException {

        if (nextTree == null) {
            nextTree = new HashMap(5);
        }
        HyphenTree tree = (HyphenTree) nextTree.get(uc);
        if (tree != null) {
            tree.setHyphenationCode(hyph);
        } else {
            tree = new HyphenTree(hyph);
            nextTree.put(uc, tree);
        }
        return tree;
    }

    /**
     * Getter for the iterator for the children.
     *
     * @return the iterator for the children
     */
    public Iterator iterator() {

        return nextTree.values().iterator();
    }

    /**
     * Setter for hc.
     *
     * @param code the hc to set
     */
    public void setCode(final char[] code) {

        this.hc = code;
    }

    /**
     * Setter for hc.
     *
     * @param code the hc to set
     *
     * @throws DuplicateHyphenationException in case that the hyphen code
     *  is already set
     */
    public void setHyphenationCode(final char[] code)
            throws DuplicateHyphenationException {

        if (code != null) {
            if (this.hc != null) {
                throw new DuplicateHyphenationException(null);
            }
            this.hc = code;
        }
    }

    /**
     * Superimpose a hyphenation code vector onto the current node and all
     * descendants.
     *
     * @param code the hyphenation code vector to superimpose
     */
    public void superimposeAll(final char[] code) {

        if (nextTree == null) {
            return;
        }
        Iterator iterator = nextTree.values().iterator();
        while (iterator.hasNext()) {
            HyphenTree t = (HyphenTree) iterator.next();
            if (t.hc != null) {
                superimpose(t.hc, 0, code);
            }
            t.superimposeAll(code);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, "");
        return sb.toString();
    }

    /**
     * Create a printable representation of the tree in a StringBuffer.
     *
     * @param sb the target string buffer
     * @param prefix the prefix for each line
     */
    protected void toString(final StringBuffer sb, final String prefix) {

        if (nextTree == null) {
            return;
        }
        String p = prefix + "  ";
        Iterator iter = nextTree.keySet().iterator();

        while (iter.hasNext()) {
            UnicodeChar key = (UnicodeChar) iter.next();
            HyphenTree t = (HyphenTree) nextTree.get(key);
            sb.append(prefix);
            sb.append("'");
            sb.append(key);
            sb.append("' ");
            char[] hyphenationCode = t.getHyphenationCode();
            if (hyphenationCode == null) {
                sb.append("nil");
            } else {
                sb.append("(");
                for (int i = 0; i < hyphenationCode.length; i++) {
                    sb.append(hyphenationCode[i]);
                }
                sb.append(")");
            }
            sb.append("\n");
            t.toString(sb, p);
        }
    }
}