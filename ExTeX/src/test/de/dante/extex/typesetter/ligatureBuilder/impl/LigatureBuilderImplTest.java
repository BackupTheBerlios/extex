/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.ligatureBuilder.impl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import de.dante.extex.font.FontFile;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.GlueNode;
import de.dante.extex.interpreter.type.node.HorizontalListNode;
import de.dante.extex.interpreter.type.node.LigatureNode;
import de.dante.extex.scanner.ActiveCharacterTokenTest;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class LigatureBuilderImplTest extends TestCase {

    private static final int CC_FF = '§';

    private static final int CC_FL = '$';

    private static final int CC_FFL = '&';

    private static final UnicodeChar FF = new UnicodeChar(CC_FF);

    private static final UnicodeChar FL = new UnicodeChar(CC_FL);

    private static final UnicodeChar FFL = new UnicodeChar(CC_FFL);

    /**
     * ...
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    private class MockFont extends NullFont {

        private Map cache = new HashMap();

        /**
         * Creates a new object.
         */
        public MockFont() {

            super();
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getGlyph(de.dante.util.UnicodeChar)
         */
        public Glyph getGlyph(UnicodeChar c) {

            Glyph g = (Glyph) cache.get(c);
            if (g == null) {
                switch (c.getCodePoint()) {
                    case 'a':
                        g = new MockGlyph('a');
                        break;
                    case 'f':
                        g = new MockGlyph('f');
                        break;
                    case 'l':
                        g = new MockGlyph('l');
                        break;
                    case CC_FF:
                        g = new MockGlyph(CC_FF);
                        break;
                    case CC_FL:
                        g = new MockGlyph(CC_FL);
                        break;
                    case CC_FFL:
                        g = new MockGlyph(CC_FFL);
                        break;
                }
                cache.put(c, g);
            }
            return g;
        }
    }

    /**
     * ...
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    private class MockGlyph implements Glyph {

        /**
         * The field <tt>c</tt> contains the ...
         */
        private int c;

        /**
         * Creates a new object.
         */
        public MockGlyph(final int cp) {

            super();
            c = cp;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#addKerning(de.dante.extex.interpreter.type.font.Kerning)
         */
        public void addKerning(final Kerning kern) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#addLigature(de.dante.extex.interpreter.type.font.Ligature)
         */
        public void addLigature(final Ligature lig) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getDepth()
         */
        public Dimen getDepth() {

            return Dimen.ZERO_PT;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getExternalFile()
         */
        public FontFile getExternalFile() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getHeight()
         */
        public Dimen getHeight() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getItalicCorrection()
         */
        public Dimen getItalicCorrection() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getKerning(de.dante.util.UnicodeChar)
         */
        public Dimen getKerning(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getLeftSpace()
         */
        public Dimen getLeftSpace() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getLigature(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLigature(final UnicodeChar uc) {

            if (c == 'f') {
                switch (uc.getCodePoint()) {
                    case 'f':
                        return FF;
                    case 'l':
                        return FL;
                }
            } else if (c == CC_FF) {
                switch (uc.getCodePoint()) {
                    case 'l':
                        return FFL;
                }
            }
            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getName()
         */
        public String getName() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getNumber()
         */
        public String getNumber() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getRightSpace()
         */
        public Dimen getRightSpace() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#getWidth()
         */
        public Dimen getWidth() {

            return Dimen.ONE_INCH;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setDepth(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setDepth(final Dimen d) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setDepth(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setDepth(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setExternalFile(de.dante.extex.interpreter.type.font.FontFile)
         */
        public void setExternalFile(final FontFile file) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setHeight(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setHeight(final Dimen h) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setHeight(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setHeight(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setItalicCorrection(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setItalicCorrection(final Dimen d) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setItalicCorrection(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setItalicCorrection(final String gsize, final Dimen em,
                final int unitsperem) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setLeftSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setLeftSpace(final Dimen ls) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setName(java.lang.String)
         */
        public void setName(final String n) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setNumber(java.lang.String)
         */
        public void setNumber(final String nr) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setRightSpace(de.dante.extex.interpreter.type.dimen.Dimen)
         */
        public void setRightSpace(final Dimen rs) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setWidth(FixedDimen)
         */
        public void setWidth(final FixedDimen w) {

        }

        /**
         * @see de.dante.extex.interpreter.type.font.Glyph#setWidth(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, int)
         */
        public void setWidth(final String gsize, final Dimen em,
                final int unitsperem) {

        }
    }

    /**
     * The field <tt>builder</tt> contains the ...
     */
    private static LigatureBuilder builder = new LigatureBuilderImpl();

    /**
     * The field <tt>tc1</tt> contains the ...
     */
    private static TypesettingContext tc1;

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(ActiveCharacterTokenTest.class);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        tc1 = new TypesettingContextImpl(new MockFont());
        super.setUp();
    }

    /**
     * ...
     */
    public void testEmpty() {

        NodeList nodes = new HorizontalListNode();
        builder.insertLigatures(nodes);
        assertEquals(0, nodes.size());
    }

    /**
     * ...
     */
    public void testOne1() {

        NodeList nodes = new HorizontalListNode();
        Node n = new CharNode(tc1, new UnicodeChar('a'));
        nodes.add(n);
        builder.insertLigatures(nodes);
        assertEquals(1, nodes.size());
        assertEquals(n, nodes.get(0));
    }

    /**
     * ...
     */
    public void testOne2() {

        NodeList nodes = new HorizontalListNode();
        Node n = new GlueNode(new Glue(3));
        nodes.add(n);
        builder.insertLigatures(nodes);
        assertEquals(1, nodes.size());
        assertEquals(n, nodes.get(0));
    }

    /**
     * ...
     */
    public void testTwo1() {

        NodeList nodes = new HorizontalListNode();
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        builder.insertLigatures(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof LigatureNode);
        LigatureNode lig = (LigatureNode) nodes.get(0);
        assertEquals(CC_FF, lig.getCharacter().getCodePoint());
    }

    /**
     * ...
     */
    public void testTwo2() {

        NodeList nodes = new HorizontalListNode();
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('a')));
        builder.insertLigatures(nodes);
        assertEquals(2, nodes.size());
    }

    /**
     * ...
     */
    public void testThree0() {

        NodeList nodes = new HorizontalListNode();
        nodes.add(new CharNode(tc1, new UnicodeChar('a')));
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('a')));
        builder.insertLigatures(nodes);
        assertEquals(3, nodes.size());
    }

    /**
     * ...
     */
    public void testThree1() {

        NodeList nodes = new HorizontalListNode();
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('l')));
        builder.insertLigatures(nodes);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof LigatureNode);
        LigatureNode lig = (LigatureNode) nodes.get(0);
        assertEquals(CC_FFL, lig.getCharacter().getCodePoint());
    }

    /**
     * ...
     */
    public void testThree2() {

        NodeList nodes = new HorizontalListNode();
        nodes.add(new CharNode(tc1, new UnicodeChar('a')));
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('l')));
        builder.insertLigatures(nodes);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof LigatureNode);
        LigatureNode lig = (LigatureNode) nodes.get(1);
        assertEquals(CC_FL, lig.getCharacter().getCodePoint());
    }

    /**
     * ...
     */
    public void testFour1() {

        NodeList nodes = new HorizontalListNode();
        nodes.add(new CharNode(tc1, new UnicodeChar('a')));
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        nodes.add(new CharNode(tc1, new UnicodeChar('l')));
        nodes.add(new CharNode(tc1, new UnicodeChar('f')));
        builder.insertLigatures(nodes);
        assertEquals(3, nodes.size());
        assertTrue(nodes.get(0) instanceof CharNode);
        assertTrue(nodes.get(1) instanceof LigatureNode);
        LigatureNode lig = (LigatureNode) nodes.get(1);
        assertEquals(CC_FL, lig.getCharacter().getCodePoint());
        assertEquals('f', ((CharNode) nodes.get(2)).getCharacter().getCodePoint());
    }

}