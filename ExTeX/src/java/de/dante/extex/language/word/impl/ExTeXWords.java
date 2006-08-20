/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language.word.impl;

import de.dante.extex.font.Glyph;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.language.word.WordTokenizer;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.ExplicitKernNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.ImplicitKernNode;
import de.dante.extex.typesetter.type.node.KernNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.WhatsItNode;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;

/**
 * This class tokenizes a list of nodes according to the rules of
 * <logo>ExTeX</logo>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ExTeXWords implements WordTokenizer {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * Hyphenate subsequent char nodes from a ligature.
     *
     * <p>
     *  Note that <logo>TeX</logo> only considers the first hyphenation point
     *  in a ligature. The others are ignored. Nevertheless the ligature builder
     *  is applied to the remaining characters. This might lead to other
     *  ligatures than the ones encoded in the ligature node.
     * </p>
     *
     * @param list the node list to hyphenate
     * @param index the index in the hyphen array
     * @param ligatureBuilder the ligature builder to use
     *
     * @return the hyphenated node list
     *
     * @throws HyphenationException in case of an error
     */
    private static NodeList hyphenate(final NodeList list, final int index,
            final LigatureBuilder ligatureBuilder) throws HyphenationException {

        if (ligatureBuilder != null) {
            for (int i = 0; i < list.size(); i = ligatureBuilder
                    .insertLigatures(list, i)) {
                // ok
            }
        }
        return list;
    }

    /**
     * Process a ligature node.
     *
     * @param nodes the node list to modify
     * @param insertionPoint the index in the nodes to start with
     * @param index the index in the hyphenation list
     * @param node the ligature node to split for hyphenation
     * @param isHyph array of indicators for hyphenation points
     * @param hyphenNode the node to be used to carry the hyphen character
     *
     * @return the new index in the hyphenation list
     *
     * @throws HyphenationException in case of an error
     */
    private static int insertShyIntoLigature(final NodeList nodes,
            final int insertionPoint, final int index, final LigatureNode node,
            final boolean[] isHyph, final CharNode hyphenNode)
            throws HyphenationException {

        int n = node.countChars();
        int needHyphen = 0;

        for (int i = 1; i < n; i++) {
            if (isHyph[index + i]) {
                needHyphen++;
            }
        }

        if (needHyphen == 0) {
            return index + n;
        }
        nodes.remove(insertionPoint);

        int leftLen = node.getLeft().countChars();

        if (needHyphen == 1 && isHyph[index + leftLen]) {
            nodes.add(insertionPoint, //
                    new DiscretionaryNode(//
                            new HorizontalListNode(node.getLeft(), hyphenNode),
                            new HorizontalListNode(node.getRight()),
                            new HorizontalListNode(node)));
            return index + n;
        }

        // only the first hyphenation mark in a ligature is considered!
        Node[] chars = node.getChars();
        NodeList pre = new HorizontalListNode();
        NodeList post = new HorizontalListNode();

        int i = 0;
        for (; !isHyph[i]; i++) {
            pre.add(chars[index + i]);
        }
        pre.add(hyphenNode);

        while (i < n) {
            post.add(chars[i]);
            i++;
        }

        nodes.add(insertionPoint, new DiscretionaryNode(pre, hyphenate(post,
                index + 1, hyphenNode.getTypesettingContext().getLanguage()),
                new HorizontalListNode(node)));

        return index + n;
    }

    /**
     * Creates a new object.
     */
    public ExTeXWords() {

        super();
    }

    /**
     * Add the characters extracted from a char node to the word container.
     *
     * @param node the character node to add to the word
     * @param word the container to add the node to
     */
    private void addWord(final CharNode node, final UnicodeCharList word) {

        if (node instanceof LigatureNode) {
            LigatureNode ln = (LigatureNode) node;
            CharNode[] chars = ln.getChars();
            for (int j = 0; j < chars.length; j++) {
                word.add(chars[j].getCharacter());
            }

        } else {
            word.add(((CharNode) node).getCharacter());
        }
    }

    /**
     * Collect all characters form a node list that make up a word.
     *
     * @param nodes the nodes to process
     * @param word the word with hyphenation marks
     * @param start the start index
     * @param lang the language in effect
     *
     * @return the index of the first node past the ones processed
     *
     * @throws HyphenationException in case of an error
     */
    private int collectWord(final NodeList nodes, final UnicodeCharList word,
            final int start, final Language lang) throws HyphenationException {

        int i = start;
        int size = nodes.size();

        Node n;
        CharNode cn;

        for (; i < size; i++) {
            n = nodes.get(i);
            if (n instanceof CharNode) {
                cn = (CharNode) n;

                if (cn.getTypesettingContext().getLanguage() != lang) {
                    return i;
                }

                addWord(cn, word);

            } else if (n instanceof WhatsItNode) {
                // ignored
            } else if (n instanceof DiscretionaryNode) {
                i = collectWord(nodes, word, i + 1, lang);
                return findWord(nodes, i, word);

            } else if (n instanceof KernNode) {
                if (n instanceof ExplicitKernNode) {
                    break;
                }
            } else {
                break;
            }
        }

        return i;
    }

    /**
     * @see de.dante.extex.language.word.WordTokenizer#findWord(
     *      de.dante.extex.typesetter.type.NodeList,
     *      int,
     *      de.dante.util.UnicodeCharList)
     */
    public int findWord(final NodeList nodes, final int start,
            final UnicodeCharList word) throws HyphenationException {

        int i = start;
        int size = nodes.size();
        word.clear();
        Node n;

        do {
            if (i >= size) {
                return i;
            }
            n = nodes.get(i++);
        } while (!(n instanceof CharNode));

        addWord((CharNode) n, word);

        return collectWord(nodes, word, i, ((CharNode) n)
                .getTypesettingContext().getLanguage());
    }

    /**
     * @see de.dante.extex.language.word.WordTokenizer#insertShy(
     *      de.dante.extex.typesetter.type.NodeList,
     *      int,
     *      boolean[],
     *      de.dante.extex.typesetter.type.node.CharNode)
     */
    public void insertShy(final NodeList nodes, final int insertionPoint,
            final boolean[] spec, final CharNode hyphenNode)
            throws HyphenationException {

        final UnicodeChar hyphen = hyphenNode.getCharacter();
        int insertion = insertionPoint;
        Node n;
        NodeList nobreak = null;
        NodeList post;
        Node prev = null;

        for (int si = 0; si < spec.length;) {

            for (n = nodes.get(insertion); !(n instanceof CharNode); n = nodes
                    .get(insertion)) {
                if (nobreak == null) {
                    nobreak = new HorizontalListNode(n);
                } else {
                    nobreak.add(n);
                }
                insertion++;
            }

            if (spec[si]) {

                if (prev instanceof CharNode) {
                    Glyph g = ((CharNode) prev).getGlyph();
                    UnicodeChar lig = g.getLigature(hyphen);
                    if (lig != null) {
                        nodes.remove(insertion--);
                        post = new HorizontalListNode(new LigatureNode(
                                ((CharNode) prev).getTypesettingContext(), lig,
                                (CharNode) prev, (CharNode) hyphenNode));
                        nobreak.add(prev);
                    } else {
                        Dimen kern = g.getKerning(hyphen);
                        if (kern != null && kern.ne(Dimen.ZERO_PT)) {
                            post = new HorizontalListNode(new ImplicitKernNode(
                                    kern, true));
                        } else {
                            post = new HorizontalListNode();
                        }
                        post.add(hyphenNode);
                    }
                } else {
                    post = new HorizontalListNode(hyphenNode);
                }
                nodes.add(insertion, //
                        new DiscretionaryNode(post, null, nobreak));
                nobreak = null;
            }

            if (n instanceof LigatureNode) {

                si = insertShyIntoLigature(nodes, insertion, si,
                        (LigatureNode) n, spec, hyphenNode);
                prev = null;

            } else {
                si++;
                prev = n;
            }

            insertion++;
        }
    }

    /**
     * @see de.dante.extex.language.word.WordTokenizer#normalize(
     *      de.dante.util.UnicodeCharList,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public UnicodeCharList normalize(final UnicodeCharList word,
            final TypesetterOptions options) throws HyphenationException {

        UnicodeCharList list = new UnicodeCharList();
        int size = word.size();

        for (int i = 0; i < size; i++) {
            list.add(word.get(i).lower());
        }

        return list;
    }

}
