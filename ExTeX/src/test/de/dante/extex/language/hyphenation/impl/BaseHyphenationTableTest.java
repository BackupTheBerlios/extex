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

package de.dante.extex.language.hyphenation.impl;

import junit.framework.TestCase;
import de.dante.extex.font.FontFile;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.MockContext;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.UnicodeChar;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class BaseHyphenationTableTest extends TestCase {

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class MockFont implements Font {

        /**
         * The field <tt>hyphen</tt> contains the ...
         */
        private UnicodeChar hyphen = new UnicodeChar('-');

        /**
         * The field <tt>hyphenGlyph</tt> contains the ...
         */
        private Glyph hyphenGlyph = new MockGlyph();

        /**
         * @see de.dante.extex.font.type.Fount#getActualSize()
         */
        public Dimen getActualSize() {

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
        public Dimen getDesignSize() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getEm()
         */
        public Dimen getEm() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getEx()
         */
        public Dimen getEx() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
         */
        public Dimen getFontDimen(final String key) {

            return Dimen.ONE_INCH;
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
        public Glue getLetterSpacing() {

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
        public Glue getSpace() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setFontDimen(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setFontDimen(final String key, final Dimen value) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setHyphenChar(de.dante.util.UnicodeChar)
         */
        public void setHyphenChar(final UnicodeChar hyphen) {

            this.hyphen = hyphen;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(de.dante.util.UnicodeChar)
         */
        public void setSkewChar(final UnicodeChar skew) {

        }
    }

    /**
     * This is a mock implementation of a glyph.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class MockGlyph implements Glyph {

        /**
         * @see de.dante.extex.font.Glyph#addKerning(de.dante.extex.font.Kerning)
         */
        public void addKerning(Kerning kern) {

        }

        /**
         * @see de.dante.extex.font.Glyph#addLigature(de.dante.extex.font.Ligature)
         */
        public void addLigature(Ligature lig) {

        }

        public Dimen getDepth() {

            return null;
        }

        public FontFile getExternalFile() {

            return null;
        }

        public Dimen getHeight() {

            return null;
        }

        public Dimen getItalicCorrection() {

            return null;
        }

        public Dimen getKerning(UnicodeChar uc) {

            return null;
        }

        public Dimen getLeftSpace() {

            return null;
        }

        public UnicodeChar getLigature(UnicodeChar uc) {

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

        public void setDepth(Dimen d) {

        }

        public void setDepth(String gsize, Dimen em, int unitsperem) {

        }

        public void setDepth(TFMFixWord size, Dimen em) {

        }

        public void setExternalFile(FontFile file) {

        }

        public void setHeight(Dimen h) {

        }

        public void setHeight(String gsize, Dimen em, int unitsperem) {

        }

        public void setHeight(TFMFixWord size, Dimen em) {

        }

        public void setItalicCorrection(Dimen d) {

        }

        public void setItalicCorrection(String gsize, Dimen em, int unitsperem) {

        }

        public void setItalicCorrection(TFMFixWord size, Dimen em) {

        }

        public void setLeftSpace(Dimen ls) {

        }

        public void setName(String n) {

        }

        public void setNumber(String nr) {

        }

        public void setRightSpace(Dimen rs) {

        }

        public void setWidth(Dimen w) {

        }

        public void setWidth(String gsize, Dimen em, int unitsperem) {

        }

        public void setWidth(TFMFixWord size, Dimen em) {

        }
    }

    /**
     * This mock implementation is for test purposes only.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private class MyMockContext extends MockContext {

        /**
         * @see de.dante.extex.interpreter.context.Context#getLccode(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLccode(final UnicodeChar uc) {

            // TODO gene: getLccode unimplemented
            return null;
        }

    }

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
    private Context context;

    /**
     * The field <tt>table</tt> contains the ...
     */
    private Language table;

    /**
     * TODO gene: missing JavaDoc
     *
     * @param s the string with the characters to encode
     * @return
     */
    private HorizontalListNode hlist(final String s) {

        TypesettingContext tc = new TypesettingContextImpl(new MockFont());
        HorizontalListNode n = new HorizontalListNode();
        for (int i = 0; i < s.length(); i++) {
            n.add(new CharNode(tc, new UnicodeChar(s.charAt(i))));
        }
        return n;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        context = new MyMockContext();
        table = new BaseHyphenationTable();
        table.addHyphenation(new Tokens(context, "abc-def"), context);
        table.addHyphenation(new Tokens(context, "d-e-f"), context);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        HorizontalListNode nodes = table.hyphenate(hlist(""), context, null);
        assertEquals(0, nodes.size());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        HorizontalListNode nodes = table.hyphenate(hlist("abc"), context, null);
        assertEquals(3, nodes.size());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test3() throws Exception {

        HorizontalListNode nodes = table.hyphenate(hlist("abcdef"), context,
                null);
        assertEquals(7, nodes.size());
        assertTrue(nodes.get(3) instanceof DiscretionaryNode);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test4() throws Exception {

        HorizontalListNode nodes = table.hyphenate(hlist("abcdefgh"), context,
                null);
        assertEquals(8, nodes.size());
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @throws Exception in case of an error
     */
    public void test5() throws Exception {

        HorizontalListNode nodes = table.hyphenate(hlist("def"), context, null);
        assertEquals(5, nodes.size());
        assertTrue(nodes.get(1) instanceof DiscretionaryNode);
        assertTrue(nodes.get(3) instanceof DiscretionaryNode);
    }

}