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

package de.dante.extex.language.hyphenation.liang;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.tc.ModifiableTypesettingContext;
import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.context.tc.TypesettingContextImpl;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.language.ModifiableLanguage;
import de.dante.extex.language.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.language.ligature.impl.LigatureBuilderImpl;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.scanner.type.token.TokenFactoryImpl;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.factory.CachingNodeFactory;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.UnicodeChar;

/**
 * This is the test class for NV.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class NVTest extends TestCase {

    /**
     * This is a mock implementation of a font.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.13 $
     */
    private class MockFont extends NullFont {

        /**
         * The field <tt>serialVersionUID</tt> contains the ...
         */
        private static final long serialVersionUID = 1L;

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
         * The field <tt>hyphen</tt> contains the hyphen character.
         */
        private UnicodeChar hyphen = UnicodeChar.get('-');

        /**
         * The field <tt>map</tt> contains the glyph cache.
         */
        private Map map = new HashMap();

        /**
         * Creates a new object.
         */
        public MockFont() {

            super();
            map.put(hyphen, new MockGlyph('-'));
            map.put(UnicodeChar.get('f'), new MockGlyph('f'));
            map.put(UnicodeChar.get('l'), new MockGlyph('l'));
            map.put(UnicodeChar.get(FL), new MockGlyph(FL));
            map.put(UnicodeChar.get(FFL), new MockGlyph(FFL));
            map.put(UnicodeChar.get(FF), new MockGlyph(FF));
        }

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
     * @version $Revision: 1.13 $
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
        public MockGlyph(final char c) {

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
        public FontByteArray getExternalFile() {

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
                    return UnicodeChar.get(MockFont.FF);
                } else if (uc.getCodePoint() == 'l') {
                    return UnicodeChar.get(MockFont.FL);
                }
            } else if (c == MockFont.FF) {
                if (uc.getCodePoint() == 'l') {
                    return UnicodeChar.get(MockFont.FFL);
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
        public void setExternalFile(final FontByteArray file) {

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
     * The field <tt>tokenFactory</tt> contains the token factory.
     */
    private static TokenFactory tokenFactory;

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
    private NodeFactory cnf;

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
     * TODO gene: missing JavaDoc
     *
     * @param context the typesetting context
     * @param uc the character
     *
     * @return a new character node
     */
    private CharNode getCharNode(final TypesettingContext context,
            final UnicodeChar uc) {

        return (CharNode) cnf.getNode(context, uc);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {

        super.setUp();
        font = new MockFont();
        cnf = new CachingNodeFactory();
        tokenFactory = new TokenFactoryImpl();
        f = tokenFactory.createToken(Catcode.LETTER, 'f', Namespace.DEFAULT_NAMESPACE);
        l = tokenFactory.createToken(Catcode.LETTER, 'l', Namespace.DEFAULT_NAMESPACE);
        hyphen = font.getHyphenChar();
        tc = new TypesettingContextImpl(font);
        ModifiableLanguage lang = new BaseHyphenationTable();
        lang.setLigatureBuilder(new LigatureBuilderImpl());
        ((ModifiableTypesettingContext) tc).setLanguage(lang);
    }

    /**
     * f-fl ((f f) l)
     *
     * @throws Exception in case of an error
     */
//    public void test1() throws Exception {
//
//        NodeList list = new HorizontalListNode();
//        LigatureNode ffl = new LigatureNode(tc, UnicodeChar.get(MockFont.FFL), //
//                getCharNode(tc, f.getChar()), //
//                new LigatureNode(tc, UnicodeChar.get(MockFont.FF), //
//                        getCharNode(tc, f.getChar()), //
//                        getCharNode(tc, l.getChar())));
//
//        NV nv = new NV(list, hyphen, tc, cnf, //
//                new boolean[]{false, true, false, false});
//
//        Count idx = new Count(0);
//        ffl.visit(nv, idx);
//        assertEquals(1, idx.getValue());
//        assertEquals(1, list.size());
//        assertTrue(list.get(0) instanceof DiscretionaryNode);
//        DiscretionaryNode d = (DiscretionaryNode) list.get(0);
//        assertEquals(2, d.getPreBreak().size());
//        assertEquals(1, d.getPostBreak().size());
//    }

    /**
     * f-fl (f (fl))
     *
     * @throws Exception in case of an error
     */
//    public void test2() throws Exception {
//
//        NodeList list = new HorizontalListNode();
//        LigatureNode ffl = new LigatureNode(tc, UnicodeChar.get(MockFont.FFL), //
//                new LigatureNode(tc, UnicodeChar.get(MockFont.FF), //
//                        getCharNode(tc, f.getChar()), //
//                        getCharNode(tc, f.getChar())), //
//                getCharNode(tc, l.getChar()));
//
//        NV nv = new NV(list, hyphen, tc, cnf, //
//                new boolean[]{false, true, false, false});
//
//        Count idx = new Count(0);
//        ffl.visit(nv, idx);
//        assertEquals(1, idx.getValue());
//        assertEquals(1, list.size());
//        assertTrue(list.get(0) instanceof DiscretionaryNode);
//        DiscretionaryNode d = (DiscretionaryNode) list.get(0);
//        assertEquals(2, d.getPreBreak().size());
//        assertEquals(1, d.getPostBreak().size());
//    }

    /**
     * f-f-l ((f f) l)
     *
     * @throws Exception in case of an error
     */
//    public void testDouble() throws Exception {
//
//        NodeList list = new HorizontalListNode();
//        LigatureNode ffl = new LigatureNode(tc, UnicodeChar.get(MockFont.FFL), //
//                getCharNode(tc, f.getChar()), //
//                new LigatureNode(tc, UnicodeChar.get(MockFont.FF), //
//                        getCharNode(tc, f.getChar()), //
//                        getCharNode(tc, l.getChar())));
//
//        NV nv = new NV(list, hyphen, tc, cnf, //
//                new boolean[]{false, false, false, false});
//
//        Count idx = new Count(0);
//        ffl.visit(nv, idx);
//        assertEquals(0, idx.getValue());
//        assertEquals(1, list.size());
//        assertEquals(list.get(0), ffl);
//    }

    /**
     * ffl ((f f) l)
     *
     * @throws Exception in case of an error
     */
//    public void testNone() throws Exception {
//
//        NodeList list = new HorizontalListNode();
//        LigatureNode ffl = new LigatureNode(tc, UnicodeChar.get(MockFont.FFL), //
//                getCharNode(tc, f.getChar()), //
//                new LigatureNode(tc, UnicodeChar.get(MockFont.FF), //
//                        getCharNode(tc, f.getChar()), //
//                        getCharNode(tc, l.getChar())));
//
//        NV nv = new NV(list, hyphen, tc, cnf, //
//                new boolean[]{false, false, false, false});
//
//        Count idx = new Count(0);
//        ffl.visit(nv, idx);
//        assertEquals(0, idx.getValue());
//        assertEquals(1, list.size());
//        assertEquals(list.get(0), ffl);
//    }

    /**
     * -ffl ((f f) l)
     *
     * @throws Exception in case of an error
     */
//    public void testPre() throws Exception {
//
//        NodeList list = new HorizontalListNode();
//        LigatureNode ffl = new LigatureNode(tc, UnicodeChar.get(MockFont.FFL), //
//                getCharNode(tc, f.getChar()), //
//                new LigatureNode(tc, UnicodeChar.get(MockFont.FF), //
//                        getCharNode(tc, f.getChar()), //
//                        getCharNode(tc, l.getChar())));
//
//        NV nv = new NV(list, hyphen, tc, cnf, //
//                new boolean[]{true, false, false, false});
//
//        Count idx = new Count(0);
//        ffl.visit(nv, idx);
//        assertEquals(1, idx.getValue());
//        assertEquals(2, list.size());
//        assertTrue(list.get(0) instanceof DiscretionaryNode);
//        assertEquals(list.get(1), ffl);
//    }

}