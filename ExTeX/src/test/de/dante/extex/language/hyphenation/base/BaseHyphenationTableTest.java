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

package de.dante.extex.language.hyphenation.base;

import junit.framework.TestCase;
import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.interpreter.context.MockContext;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.language.Language;
import de.dante.extex.language.word.impl.TeXWords;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.SpaceNode;
import de.dante.extex.typesetter.type.node.factory.CachingNodeFactory;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;

/**
 * Test suite for the base hyphenation table.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class BaseHyphenationTableTest extends TestCase {

    /**
     * Mock implementation of a font.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.6 $
     */
    private class MockFont extends NullFont {

        /**
         * The field <tt>hyphen</tt> contains the hyphenation character.
         */
        private UnicodeChar hyphen = UnicodeChar.get('-');

        /**
         * The field <tt>hyphenGlyph</tt> contains the hyphen glyph.
         */
        private Glyph hyphenGlyph = new MockGlyph();

        /**
         * @see de.dante.extex.font.type.Fount#getActualSize()
         */
        public FixedDimen getActualSize() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getBoundingBox()
         */
        public BoundingBox getBoundingBox() {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getCheckSum()
         */
        public int getCheckSum() {

            return 0;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getDesignSize()
         */
        public FixedDimen getDesignSize() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getEm()
         */
        public FixedDimen getEm() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getEx()
         */
        public FixedDimen getEx() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontByteArray()
         */
        public FontByteArray getFontByteArray() {

            return null; // add by mgn
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
         */
        public FixedDimen getFontDimen(final String key) {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontKey()
         */
        public FountKey getFontKey() {

            return new FountKey("mockfont"); // add by mgn
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontName()
         */
        public String getFontName() {

            return "mock";
        }

        /**
         * @see de.dante.extex.font.type.Fount#getGlyph(de.dante.util.UnicodeChar)
         */
        public Glyph getGlyph(final UnicodeChar c) {

            if (hyphen.equals(c)) {
                return hyphenGlyph;
            }
            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getHyphenChar()
         */
        public UnicodeChar getHyphenChar() {

            return hyphen;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getLetterSpacing()
         */
        public FixedGlue getLetterSpacing() {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getProperty(java.lang.String)
         */
        public String getProperty(final String key) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getSkewChar()
         */
        public UnicodeChar getSkewChar() {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getSpace()
         */
        public FixedGlue getSpace() {

            return new Glue(10 * Dimen.ONE);
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(
         *      java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setFontDimen(final String key, final Dimen value) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setHyphenChar(
         *      de.dante.util.UnicodeChar)
         */
        public void setHyphenChar(final UnicodeChar hyphen) {

            this.hyphen = hyphen;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(
         *      de.dante.util.UnicodeChar)
         */
        public void setSkewChar(final UnicodeChar skew) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setEfcode(de.dante.util.UnicodeChar, long)
         */
        public void setEfcode(UnicodeChar uc, long code) {

            // TODO gene: setEfcode unimplemented
            
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getEfcode()
         */
        public long getEfcode(UnicodeChar uc) {

            // TODO gene: getEfcode unimplemented
            return 0;
        }

    }

    /**
     * This is a mock implementation of a glyph.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.6 $
     */
    private class MockGlyph implements Glyph {

        /**
         * @see de.dante.extex.font.Glyph#addKerning(de.dante.extex.font.Kerning)
         */
        public void addKerning(final Kerning kern) {

        }

        /**
         * @see de.dante.extex.font.Glyph#addLigature(de.dante.extex.font.Ligature)
         */
        public void addLigature(final Ligature lig) {

        }

        public Dimen getDepth() {

            return null;
        }

        public FontByteArray getExternalFile() {

            return null;
        }

        public Dimen getHeight() {

            return null;
        }

        public Dimen getItalicCorrection() {

            return null;
        }

        public Dimen getKerning(final UnicodeChar uc) {

            return null;
        }

        public Dimen getLeftSpace() {

            return null;
        }

        public UnicodeChar getLigature(final UnicodeChar uc) {

            return null;
        }

        public String getName() {

            return null;
        }

        public String getNumber() {

            return null;
        }

        public Dimen getRightSpace() {

            return null;
        }

        public Dimen getWidth() {

            return null;
        }

        public void setDepth(final Dimen d) {

        }

        public void setDepth(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        public void setDepth(final TFMFixWord size, final Dimen em) {

        }

        public void setExternalFile(final FontByteArray file) {

        }

        public void setHeight(final Dimen h) {

        }

        public void setHeight(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        public void setHeight(final TFMFixWord size, final Dimen em) {

        }

        public void setItalicCorrection(final Dimen d) {

        }

        public void setItalicCorrection(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        public void setItalicCorrection(final TFMFixWord size, final Dimen em) {

        }

        public void setLeftSpace(final Dimen ls) {

        }

        public void setName(final String n) {

        }

        public void setNumber(final String nr) {

        }

        public void setRightSpace(final Dimen rs) {

        }

        public void setWidth(final Dimen w) {

        }

        public void setWidth(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        public void setWidth(final TFMFixWord size, final Dimen em) {

        }
    }

    /**
     * This mock implementation is for test purposes only.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.6 $
     */
    private class MyMockContext extends MockContext {

        /**
         * The constant <tt>serialVersionUID</tt> contains the id for
         * serialization.
         */
        private static final long serialVersionUID = 1L;

        /**
         * @see de.dante.extex.interpreter.context.Context#getLccode(
         *      de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLccode(final UnicodeChar uc) {

            return null;
        }
    }

    /**
     * The field <tt>nodeFactory</tt> contains the node factory.
     */
    private NodeFactory nodeFactory = new CachingNodeFactory();

    /**
     * The field <tt>HYPHEN</tt> contains the yphen character.
     */
    private static final UnicodeChar HYPHEN = UnicodeChar.get('-');

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(BaseHyphenationTableTest.class);
    }

    /**
     * The field <tt>context</tt> contains the mock context for the tests.
     */
    private TypesetterOptions context;

    /**
     * The field <tt>language</tt> contains the language.
     */
    private Language language;

    /**
     * Create a hlist from a string.
     *
     * @param s the string with the characters to encode
     *
     * @return a horizontal list
     */
    private HorizontalListNode hlist(final String s) {

        TypesettingContext tc = new TypesettingContextImpl(new MockFont());
        HorizontalListNode n = new HorizontalListNode();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                n.add(new SpaceNode(tc.getFont().getSpace()));
            } else {
                n.add(new CharNode(tc, UnicodeChar.get(c)));
            }
        }
        return n;
    }

    /**
     * Create a new object to test.
     *
     * @return the object to test
     */
    protected Language makeLanguage() {

        BaseHyphenationTable lang = new BaseHyphenationTable();
        lang.setWordTokenizer(new TeXWords());
        return lang;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        context = new MyMockContext();
        language = makeLanguage();
        language.addHyphenation(makeList("abc-def"), context);
        language.addHyphenation(makeList("d-e-f"), context);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param s ...
     * @return ...
     */
    private UnicodeCharList makeList(final CharSequence s) {

        UnicodeCharList list = new UnicodeCharList();
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            if (c == '-') {
                c = 0xad;
            }
            list.add(UnicodeChar.get(c));
        }
        return list;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        HorizontalListNode nodes = hlist("");
        language.hyphenate(nodes, context, HYPHEN, 0, true, nodeFactory);
        assertEquals(0, nodes.size());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        HorizontalListNode nodes = hlist("abc");
        language.hyphenate(nodes, context, HYPHEN, 0, true, nodeFactory);
        assertEquals(3, nodes.size());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        HorizontalListNode nodes = hlist("abcdef");
        language.hyphenate(nodes, context, HYPHEN, 0, true, nodeFactory);
        assertEquals(7, nodes.size());
        assertTrue(nodes.get(3) instanceof DiscretionaryNode);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        HorizontalListNode nodes = hlist("abcdefgh");
        language.hyphenate(nodes, context, HYPHEN, 0, true, nodeFactory);
        assertEquals(8, nodes.size());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        HorizontalListNode nodes = hlist("def");
        language.hyphenate(nodes, context, HYPHEN, 0, true, nodeFactory);
        assertEquals(5, nodes.size());
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        assertTrue(nodes.get(3) instanceof DiscretionaryNode);
    }

}
