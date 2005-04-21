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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import de.dante.extex.font.FontFile;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.language.ModifiableLanguage;
import de.dante.extex.language.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.language.ligature.impl.LigatureBuilderImpl;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.scanner.type.TokenFactoryImpl;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.util.UnicodeChar;

/**
 * This is the test class for NV.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class NVTest extends TestCase {

    /**
     * This is a mock implementation of a font.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
     */
    private class MockFont implements Font {

        /**
         * The field <tt>hyphen</tt> contains the hyphen character.
         */
        private UnicodeChar hyphen = new UnicodeChar('-');

        /**
         * The field <tt>map</tt> contains the ...
         */
        private Map map = new HashMap();

        /**
         * The field <tt>FF</tt> contains the ligature character ff.
         */
        public static final char FF = 'F';

        /**
         * The field <tt>FFL</tt> contains the ligature character ffl.
         */
        public static final char FFL = 'L';

        /**
         * The field <tt>FL</tt> contains the ligature character ffl.
         */
        public static final char FL = 'G';

        /**
         * Creates a new object.
         */
        public MockFont() {

            super();
            map.put(hyphen, new MockGlyph('-'));
            map.put(new UnicodeChar('f'), new MockGlyph('f'));
            map.put(new UnicodeChar('l'), new MockGlyph('l'));
            map.put(new UnicodeChar(FL), new MockGlyph(FL));
            map.put(new UnicodeChar(FFL), new MockGlyph(FFL));
            map.put(new UnicodeChar(FF), new MockGlyph(FF));
        }

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

            return (Glyph) map.get(c);
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
     * @version $Revision: 1.2 $
     */
    private class MockGlyph implements Glyph {

        /**
         * The field <tt>c</tt> contains the ...
         */
        private char c;

        /**
         * Creates a new object.
         *
         * 
         */
        public MockGlyph(char c) {

            super();
            this.c = c;
        }

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

        /**
         * @see de.dante.extex.font.Glyph#getDepth()
         */
        public Dimen getDepth() {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#getExternalFile()
         */
        public FontFile getExternalFile() {

            return null;
        }

        /**
         * @see de.dante.extex.font.Glyph#getHeight()
         */
        public Dimen getHeight() {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#getItalicCorrection()
         */
        public Dimen getItalicCorrection() {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#getKerning(de.dante.util.UnicodeChar)
         */
        public Dimen getKerning(final UnicodeChar uc) {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#getLeftSpace()
         */
        public Dimen getLeftSpace() {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#getLigature(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLigature(final UnicodeChar uc) {

            if (c == 'f') {
                if (uc.getCodePoint() == 'f') {
                    return new UnicodeChar(MockFont.FF);
                } else if (uc.getCodePoint() == 'l') {
                    return new UnicodeChar(MockFont.FL);
                }
            } else if (c == MockFont.FF) {
                if (uc.getCodePoint() == 'l') {
                    return new UnicodeChar(MockFont.FFL);
                }
            }
            return null;
        }

        /**
         * @see de.dante.extex.font.Glyph#getName()
         */
        public String getName() {

            return null;
        }

        /**
         * @see de.dante.extex.font.Glyph#getNumber()
         */
        public String getNumber() {

            return null;
        }

        /**
         * @see de.dante.extex.font.Glyph#getRightSpace()
         */
        public Dimen getRightSpace() {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#getWidth()
         */
        public Dimen getWidth() {

            return new Dimen();
        }

        /**
         * @see de.dante.extex.font.Glyph#setDepth(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setDepth(final Dimen d) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setDepth(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setDepth(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setDepth(de.dante.extex.font.type.tfm.TFMFixWord, de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setDepth(final TFMFixWord size, final Dimen em) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setExternalFile(de.dante.extex.font.FontFile)
         */
        public void setExternalFile(final FontFile file) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setHeight(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setHeight(final Dimen h) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setHeight(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setHeight(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setHeight(de.dante.extex.font.type.tfm.TFMFixWord, de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setHeight(final TFMFixWord size, final Dimen em) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setItalicCorrection(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setItalicCorrection(final Dimen d) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setItalicCorrection(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setItalicCorrection(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setItalicCorrection(de.dante.extex.font.type.tfm.TFMFixWord, de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setItalicCorrection(final TFMFixWord size, final Dimen em) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setLeftSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setLeftSpace(final Dimen ls) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setName(java.lang.String)
         */
        public void setName(final String n) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setNumber(java.lang.String)
         */
        public void setNumber(final String nr) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setRightSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setRightSpace(final Dimen rs) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setWidth(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setWidth(final Dimen w) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setWidth(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setWidth(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.font.Glyph#setWidth(de.dante.extex.font.type.tfm.TFMFixWord, de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setWidth(final TFMFixWord size, final Dimen em) {

        }
    }

    /**
     * The field <tt>hyphen</tt> contains the token for the hyphen char.
     */
    private static UnicodeChar hyphen;

    /**
     * The field <tt>tf</tt> contains the token factory.
     */
    private static TokenFactory tf;

    /**
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(NVTest.class);
    }

    /**
     * The field <tt>cnf</tt> contains the char node factory.
     */
    private CharNodeFactory cnf;

    /**
     * The field <tt>f</tt> contains the token for f.
     */
    private Token f;

    /**
     * The field <tt>font</tt> contains the font.
     */
    private Font font;

    /**
     * The field <tt>l</tt> contains the token for l.
     */
    private Token l;

    /**
     * The field <tt>tc</tt> contains the typesetting context.
     */
    private TypesettingContext tc;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {

        super.setUp();
        font = new MockFont();
        cnf = new CharNodeFactory();
        tf = new TokenFactoryImpl();
        f = tf.createToken(Catcode.LETTER, 'f', Namespace.DEFAULT_NAMESPACE);
        l = tf.createToken(Catcode.LETTER, 'l', Namespace.DEFAULT_NAMESPACE);
        hyphen = font.getHyphenChar();
        tc = new TypesettingContextImpl(font);
        ModifiableLanguage lang = new BaseHyphenationTable();
        lang.setLigatureBuilder(new LigatureBuilderImpl());
        tc.setLanguage(lang);
    }

    /**
     * f-fl ((f f) l)
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        NodeList list = new HorizontalListNode();
        LigatureNode ffl = new LigatureNode(tc, new UnicodeChar(MockFont.FFL), //
                cnf.newInstance(tc, f.getChar()), //
                new LigatureNode(tc, new UnicodeChar(MockFont.FF), //
                        cnf.newInstance(tc, f.getChar()), //
                        cnf.newInstance(tc, l.getChar())));

        NV nv = new NV(list, hyphen, tc, cnf, //
                new boolean[]{false, true, false, false});

        Count idx = new Count(0);
        ffl.visit(nv, idx);
        assertEquals(1, idx.getValue());
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof DiscretionaryNode);
        DiscretionaryNode d = (DiscretionaryNode) list.get(0);
        assertEquals(2, d.getPreBreak().size());
        assertEquals(1, d.getPostBreak().size());
    }

    /**
     * f-fl (f (fl))
     *
     * @throws Exception in case of an error
     */
    public void test2() throws Exception {

        NodeList list = new HorizontalListNode();
        LigatureNode ffl = new LigatureNode(tc, new UnicodeChar(MockFont.FFL), //
                new LigatureNode(tc, new UnicodeChar(MockFont.FF), //
                        cnf.newInstance(tc, f.getChar()), //
                        cnf.newInstance(tc, f.getChar())), //
                cnf.newInstance(tc, l.getChar()));

        NV nv = new NV(list, hyphen, tc, cnf, //
                new boolean[]{false, true, false, false});

        Count idx = new Count(0);
        ffl.visit(nv, idx);
        assertEquals(1, idx.getValue());
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof DiscretionaryNode);
        DiscretionaryNode d = (DiscretionaryNode) list.get(0);
        assertEquals(2, d.getPreBreak().size());
        assertEquals(1, d.getPostBreak().size());
    }

    /**
     * ffl ((f f) l)
     *
     * @throws Exception in case of an error
     */
    public void testNone() throws Exception {

        NodeList list = new HorizontalListNode();
        LigatureNode ffl = new LigatureNode(tc, new UnicodeChar(MockFont.FFL), //
                cnf.newInstance(tc, f.getChar()), //
                new LigatureNode(tc, new UnicodeChar(MockFont.FF), //
                        cnf.newInstance(tc, f.getChar()), //
                        cnf.newInstance(tc, l.getChar())));

        NV nv = new NV(list, hyphen, tc, cnf, //
                new boolean[]{false, false, false, false});

        Count idx = new Count(0);
        ffl.visit(nv, idx);
        assertEquals(0, idx.getValue());
        assertEquals(1, list.size());
        assertEquals(list.get(0), ffl);
    }

    /**
     * -ffl ((f f) l)
     *
     * @throws Exception in case of an error
     */
    public void testPre() throws Exception {

        NodeList list = new HorizontalListNode();
        LigatureNode ffl = new LigatureNode(tc, new UnicodeChar(MockFont.FFL), //
                cnf.newInstance(tc, f.getChar()), //
                new LigatureNode(tc, new UnicodeChar(MockFont.FF), //
                        cnf.newInstance(tc, f.getChar()), //
                        cnf.newInstance(tc, l.getChar())));

        NV nv = new NV(list, hyphen, tc, cnf, //
                new boolean[]{true, false, false, false});

        Count idx = new Count(0);
        ffl.visit(nv, idx);
        assertEquals(1, idx.getValue());
        assertEquals(2, list.size());
        assertTrue(list.get(0) instanceof DiscretionaryNode);
        assertEquals(list.get(1), ffl);
    }

    /**
     * f-f-l ((f f) l)
     *
     * @throws Exception in case of an error
     */
    public void testDouble() throws Exception {

        NodeList list = new HorizontalListNode();
        LigatureNode ffl = new LigatureNode(tc, new UnicodeChar(MockFont.FFL), //
                cnf.newInstance(tc, f.getChar()), //
                new LigatureNode(tc, new UnicodeChar(MockFont.FF), //
                        cnf.newInstance(tc, f.getChar()), //
                        cnf.newInstance(tc, l.getChar())));

        NV nv = new NV(list, hyphen, tc, cnf, //
                new boolean[]{false, false, false, false});

        Count idx = new Count(0);
        ffl.visit(nv, idx);
        assertEquals(0, idx.getValue());
        assertEquals(1, list.size());
        assertEquals(list.get(0), ffl);
    }

}