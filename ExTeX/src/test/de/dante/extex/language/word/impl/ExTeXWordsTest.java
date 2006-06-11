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

import junit.framework.TestCase;
import de.dante.extex.interpreter.context.ModifiableTypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.language.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.ligature.impl.LigatureBuilderImpl;
import de.dante.extex.language.word.WordTokenizer;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.extex.typesetter.type.node.factory.SimpleNodeFactory;
import de.dante.test.font.CMR10;
import de.dante.util.UnicodeChar;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ExTeXWordsTest extends TestCase {

    /**
     * The field <tt>wt</tt> contains the word tokenizer to test.
     * The word tokenizer is stateless. Thus a single instance suffices.
     */
    private static WordTokenizer wt = new ExTeXWords();

    /**
     * The field <tt>nf</tt> contains the node factory to use throughout the
     * test cases.
     */
    private static NodeFactory nf = new SimpleNodeFactory();

    /**
     * The field <tt>cmr10</tt> contains the font cmr10 in a memory-only version.
     */
    private static Font cmr10 = new CMR10();

    /**
     * The field <tt>tc</tt> contains the typesetting context.
     */
    private static ModifiableTypesettingContext tc = new TypesettingContextImpl(
            cmr10);

    {
        BaseHyphenationTable lan = new BaseHyphenationTable();
        lan.setLigatureBuilder(new LigatureBuilderImpl());
        lan.setWordTokenizer(wt);
        try {
            lan.setLeftHyphenmin(1L);
            lan.setRightHyphenmin(1L);
        } catch (HyphenationException e) {
            e.printStackTrace();
        }
        tc.setLanguage(lan);
    }

    /**
     * The field <tt>hyphen</tt> contains the hyphen node.
     */
    private static CharNode hyphen = (CharNode) nf.getNode(tc, UnicodeChar.get(
            '-'));

    /**
     * The field <tt>UC_F</tt> contains the character f.
     */
    private static final UnicodeChar UC_F = UnicodeChar.get('f');

    /**
     * The field <tt>UC_I</tt> contains the character i.
     */
    private static final UnicodeChar UC_I = UnicodeChar.get('i');

    /**
     * The field <tt>UC_L</tt> contains the character l.
     */
    private static final UnicodeChar UC_L = UnicodeChar.get('l');

    /**
     * The field <tt>UC_FF</tt> contains the ff ligature.
     */
    private static final UnicodeChar UC_FF = UnicodeChar.get('\013');

    /**
     * The field <tt>UC_FI</tt> contains the fi ligature.
     */
    private static final UnicodeChar UC_FI = UnicodeChar.get('\014');

    /**
     * The field <tt>UC_FL</tt> contains the fl ligature.
     */
    private static final UnicodeChar UC_FL = UnicodeChar.get('\015');

    /**
     * The field <tt>UC_FFI</tt> contains the ffi ligature.
     */
    private static final UnicodeChar UC_FFI = UnicodeChar.get('\016');

    /**
     * The field <tt>UC_FFL</tt> contains the ffl ligature.
     */
    private static final UnicodeChar UC_FFL = UnicodeChar.get('\017');

    /**
     * The main method.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ExTeXWordsTest.class);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param s the characters to insert into the list
     *
     * @return a node list made of te characters
     */
    private static NodeList makeList(final CharSequence s) {

        NodeList nodes = new HorizontalListNode();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\013':
                    nodes.add(new LigatureNode(tc, UC_FF, //
                            (CharNode) nf.getNode(tc, UC_F), //
                            (CharNode) nf.getNode(tc, UC_F)));
                    break;
                case '\014':
                    nodes.add(new LigatureNode(tc, UC_FI, //
                            (CharNode) nf.getNode(tc, UC_F), //
                            (CharNode) nf.getNode(tc, UC_I)));
                    break;
                case '\015':
                    nodes.add(new LigatureNode(tc, UC_FL, //
                            (CharNode) nf.getNode(tc, UC_F), //
                            (CharNode) nf.getNode(tc, UC_L)));
                    break;
                case '\016':
                    nodes.add(new LigatureNode(tc, UC_FFI, //
                            new LigatureNode(tc, UC_FF, //
                                    (CharNode) nf.getNode(tc, UC_F), //
                                    (CharNode) nf.getNode(tc, UC_F)), //
                            (CharNode) nf.getNode(tc, UC_I)));
                    break;
                case '\017':
                    nodes.add(new LigatureNode(tc, UC_FFL, //
                            new LigatureNode(tc, UC_FF, //
                                    (CharNode) nf.getNode(tc, UC_F), //
                                    (CharNode) nf.getNode(tc, UC_F)), //
                            (CharNode) nf.getNode(tc, UC_L)));
                    break;
                default:
                    nodes.add(nf.getNode(tc, UnicodeChar.get(c)));
            }
        }
        return nodes;
    }

    /**
     * <testcase>
     *  Test that the empty spec is accepted.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy1() throws Exception {

        NodeList nodes = new HorizontalListNode();
        wt.insertShy(nodes, 0, new boolean[0], hyphen);
        assertEquals(0, nodes.size());
    }

    /**
     * <testcase>
     *  Test that the empty spec is accepted.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy2() throws Exception {

        NodeList nodes = makeList("a");
        wt.insertShy(nodes, 0, new boolean[0], hyphen);
        assertEquals(1, nodes.size());
    }

    /**
     * <testcase>
     *  Test that the empty spec is accepted.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy3() throws Exception {

        NodeList nodes = makeList("a");
        wt.insertShy(nodes, 1, new boolean[0], hyphen);
        assertEquals(1, nodes.size());
    }

    /**
     * <testcase>
     *  Test that the one element spec is accepted.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy10() throws Exception {

        NodeList nodes = makeList("ab");
        wt.insertShy(nodes, 0, new boolean[]{true}, hyphen);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof DiscretionaryNode);
        assertTrue(nodes.get(1) instanceof CharNode);
        assertTrue(nodes.get(2) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that the one element spec is accepted.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy11() throws Exception {

        NodeList nodes = makeList("ab");
        wt.insertShy(nodes, 0, new boolean[]{false}, hyphen);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy20() throws Exception {

        NodeList nodes = makeList("ab");
        wt.insertShy(nodes, 0, new boolean[]{false, true}, hyphen);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        assertTrue(nodes.get(2) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy21() throws Exception {

        NodeList nodes = makeList("abc");
        wt.insertShy(nodes, 1, new boolean[]{false, true}, hyphen);
        assertEquals(4, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof CharNode);
        assertTrue(nodes.get(2) instanceof DiscretionaryNode);
        assertTrue(nodes.get(3) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy31() throws Exception {

        NodeList nodes = makeList("a\13b");
        wt.insertShy(nodes, 0, new boolean[]{false, true, false}, hyphen);
        assertEquals(4, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        assertTrue(nodes.get(2) instanceof LigatureNode);
        assertTrue(nodes.get(3) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy32() throws Exception {

        NodeList nodes = makeList("a\13b");
        wt.insertShy(nodes, 1, new boolean[]{false, true}, hyphen);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        assertTrue(nodes.get(2) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy33() throws Exception {

        NodeList nodes = makeList("a\13b");
        wt.insertShy(nodes, 1, new boolean[]{false, false, true}, hyphen);
        assertEquals(4, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof LigatureNode);
        assertTrue(nodes.get(2) instanceof DiscretionaryNode);
        assertTrue(nodes.get(3) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy41() throws Exception {

        NodeList nodes = makeList("a\17b");
        wt.insertShy(nodes, 1, //
                new boolean[]{false, false, true, false}, hyphen);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        DiscretionaryNode d = (DiscretionaryNode) nodes.get(1);
        assertTrue(d.getPreBreak().get(0) instanceof LigatureNode);
        assertTrue(d.getPostBreak().get(0) instanceof CharNode);
        assertTrue(nodes.get(2) instanceof CharNode);
    }

    /**
     * <testcase>
     *  Test that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testInsertShy42() throws Exception {

        NodeList nodes = makeList("a\17b");
        wt.insertShy(nodes, 1, //
                new boolean[]{false, true, false}, hyphen);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        DiscretionaryNode d = (DiscretionaryNode) nodes.get(1);
        assertTrue(d.getPreBreak().get(0) instanceof CharNode);
        assertTrue(d.getPostBreak().get(0) instanceof LigatureNode);
        assertTrue(nodes.get(2) instanceof CharNode);
    }

    
    //TODO gene: add test cases for handling of implicit kerns
}
