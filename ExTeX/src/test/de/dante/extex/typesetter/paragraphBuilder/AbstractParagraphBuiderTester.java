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

package de.dante.extex.typesetter.paragraphBuilder;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import de.dante.extex.font.FontByteArray;
import de.dante.extex.font.FountKey;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.context.tc.TypesettingContextFactory;
import de.dante.extex.interpreter.context.tc.TypesettingContextImpl;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.main.logging.LogFormatter;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.extex.typesetter.type.node.PenaltyNode;
import de.dante.extex.typesetter.type.node.RuleNode;
import de.dante.extex.typesetter.type.node.SpaceNode;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.node.factory.NodeFactory;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is the abstract base class to test a paragraph builder.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public abstract class AbstractParagraphBuiderTester extends TestCase {

    /**
     * Inner class for the typesetter options.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.11 $
     */
    private class MockOptions implements TypesetterOptions {

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getCountOption(java.lang.String)
         */
        public FixedCount getCountOption(final String name) {

            if (name.equals("tracingparagraphs")) {
                return new Count(1);
            } else if (name.equals("pretolerance")) {
                return new Count(300);
            } else if (name.equals("tolerance")) {
                return new Count(10);
            } else if (name.equals("tolerance")) {
                return new Count(200);
            } else if (name.equals("hyphenpenalty")) {
                return new Count(20);
            } else if (name.equals("exhyphenpenalty")) {
                return new Count(30);
            }
            return new Count(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getDimenOption(java.lang.String)
         */
        public FixedDimen getDimenOption(final String name) {

            if (name.equals("hsize")) {
                return new Dimen(Dimen.ONE * 23);
            }
            return new Dimen(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getFont(java.lang.String)
         */
        public Font getFont(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getGlueOption(java.lang.String)
         */
        public FixedGlue getGlueOption(final String name) {

            if (name.equals("parfillskip")) {
                return new Glue(1000);
            }
            return new Glue(0);
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getLccode(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLccode(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getMuskip(java.lang.String)
         */
        public Muskip getMuskip(final String name) {

            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getNamespace()
         */
        public String getNamespace() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getParshape()
         */
        public ParagraphShape getParshape() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getTokenFactory()
         */
        public TokenFactory getTokenFactory() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getTypesettingContext()
         */
        public TypesettingContext getTypesettingContext() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#getTypesettingContextFactory()
         */
        public TypesettingContextFactory getTypesettingContextFactory() {

            return null;
        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#setCountOption(
         *      java.lang.String,
         *      de.dante.extex.interpreter.type.count.FixedCount)
         */
        public void setCountOption(final String name, final long value)
                throws GeneralException {

        }

        /**
         * @see de.dante.extex.typesetter.TypesetterOptions#setParshape(
         *      de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
         */
        public void setParshape(final ParagraphShape shape) {

        }
    }

    /**
     * The field <tt>p1</tt> contains the ...
     */
    private static Pattern p1 = null;

    /**
     * The field <tt>p2</tt> contains the ...
     */
    private static Pattern p2 = null;

    /**
     * The field <tt>p3</tt> contains the ...
     */
    private static Pattern p3 = null;

    /**
     * The field <tt>tracer</tt> contains the logger for the output.
     */
    private static Logger tracer = null;;

    /**
     * The field <tt>VPT</tt> contains the constant for 5pt.
     */
    protected static final Dimen VPT = new Dimen(Dimen.ONE * 5);

    /**
     * The field <tt>pb</tt> contains the paragraph builder to test.
     */
    private ParagraphBuilder pb;

    /**
     * The field <tt>tc</tt> contains the mock typesetting context.
     */
    private TypesettingContextImpl tc = new TypesettingContextImpl(new Font() {

        /**
         * The field <tt>hyphenChar</tt> contains the ...
         */
        private UnicodeChar hyphenChar = UnicodeChar.get('-');

        /**
         * The field <tt>skewChar</tt> contains the ...
         */
        private UnicodeChar skewChar = null;

        /**
         * @see de.dante.extex.font.type.Fount#getActualSize()
         */
        public FixedDimen getActualSize() {

            return VPT;
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
         * @see de.dante.extex.interpreter.type.font.Font#getDepth(
         *      de.dante.util.UnicodeChar)
         */
        public FixedGlue getDepth(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getDesignSize()
         */
        public FixedDimen getDesignSize() {

            return VPT;
        }

        public long getEfcode(final UnicodeChar uc) {

            return 1000;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getEm()
         */
        public FixedDimen getEm() {

            return VPT;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getEx()
         */
        public FixedDimen getEx() {

            return VPT;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontByteArray()
         */
        public FontByteArray getFontByteArray() {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontDimen(java.lang.String)
         */
        public FixedDimen getFontDimen(final String key) {

            return Dimen.ZERO_PT;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontKey()
         */
        public FountKey getFontKey() {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getFontName()
         */
        public String getFontName() {

            return "fnt";
        }

        /**
         * @see de.dante.extex.font.type.Fount#getGlyph(de.dante.util.UnicodeChar)
         */
        public Glyph getGlyph(final UnicodeChar c) {

            return new Glyph() {

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

                    return Dimen.ONE_PT;
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

                    return VPT;
                }

                /**
                 * @see de.dante.extex.font.Glyph#getItalicCorrection()
                 */
                public Dimen getItalicCorrection() {

                    return Dimen.ZERO_PT;
                }

                /**
                 * @see de.dante.extex.font.Glyph#getKerning(de.dante.util.UnicodeChar)
                 */
                public Dimen getKerning(final UnicodeChar uc) {

                    return null;
                }

                /**
                 * @see de.dante.extex.font.Glyph#getLeftSpace()
                 */
                public Dimen getLeftSpace() {

                    return null;
                }

                /**
                 * @see de.dante.extex.font.Glyph#getLigature(de.dante.util.UnicodeChar)
                 */
                public UnicodeChar getLigature(final UnicodeChar uc) {

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

                    return null;
                }

                /**
                 * @see de.dante.extex.font.Glyph#getWidth()
                 */
                public Dimen getWidth() {

                    return VPT;
                }

                /**
                 * @see de.dante.extex.font.Glyph#setDepth(de.dante.extex.interpreter.type.dimen.Dimen)
                 */
                public void setDepth(final Dimen d) {

                }

                /**
                 * @see de.dante.extex.font.Glyph#setExternalFile(de.dante.extex.font.FontByteArray)
                 */
                public void setExternalFile(final FontByteArray file) {

                }

                /**
                 * @see de.dante.extex.font.Glyph#setHeight(de.dante.extex.interpreter.type.dimen.Dimen)
                 */
                public void setHeight(final Dimen h) {

                }

                /**
                 * @see de.dante.extex.font.Glyph#setItalicCorrection(de.dante.extex.interpreter.type.dimen.Dimen)
                 */
                public void setItalicCorrection(final Dimen d) {

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
            };
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getHeight(de.dante.util.UnicodeChar)
         */
        public FixedGlue getHeight(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getHyphenChar()
         */
        public UnicodeChar getHyphenChar() {

            return hyphenChar;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getItalicCorrection(de.dante.util.UnicodeChar)
         */
        public FixedDimen getItalicCorrection(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getKerning(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
         */
        public FixedDimen getKerning(final UnicodeChar uc1,
                final UnicodeChar uc2) {

            return null;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getLetterSpacing()
         */
        public FixedGlue getLetterSpacing() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getLigature(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLigature(final UnicodeChar uc1,
                final UnicodeChar uc2) {

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

            return skewChar;
        }

        /**
         * @see de.dante.extex.font.type.Fount#getSpace()
         */
        public FixedGlue getSpace() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#getWidth(
         *      de.dante.util.UnicodeChar)
         */
        public FixedGlue getWidth(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#hasGlyph(
         *      de.dante.util.UnicodeChar)
         */
        public boolean hasGlyph(final UnicodeChar uc) {

            return true;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setEfcode(
         *      de.dante.util.UnicodeChar, long)
         */
        public void setEfcode(final UnicodeChar uc, final long code) {

            // TODO gene: setEfcode unimplemented

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

            hyphenChar = hyphen;
        }

        /**
         * @see de.dante.extex.interpreter.type.font.Font#setSkewChar(de.dante.util.UnicodeChar)
         */
        public void setSkewChar(final UnicodeChar skew) {

            skewChar = skew;
        }
    });
    {
        tc.setLanguage(new Language() {

            /**
             * The field <tt>serialVersionUID</tt> contains the ...
             */
            private static final long serialVersionUID = 1L;

            public void addHyphenation(final UnicodeCharList word,
                    final TypesetterOptions context)
                    throws HyphenationException {

                throw new RuntimeException("unimplemented");
            }

            public void addPattern(final Tokens pattern)
                    throws HyphenationException {

                throw new RuntimeException("unimplemented");
            }

            public int findWord(final NodeList nodes, final int start,
                    final UnicodeCharList word) throws HyphenationException {

                throw new RuntimeException("unimplemented");
            }

            public long getLeftHyphenmin() throws HyphenationException {

                return 0;
            }

            public UnicodeChar getLigature(final UnicodeChar c1,
                    final UnicodeChar c2, Font f) throws HyphenationException {

                return f.getLigature(c1, c2);
            }

            public String getName() {

                throw new RuntimeException("unimplemented");
            }

            public long getRightHyphenmin() throws HyphenationException {

                return 0;
            }

            public boolean hyphenate(NodeList nodelist,
                    TypesetterOptions context, UnicodeChar hyphen, int start,
                    boolean forall, NodeFactory nodeFactory)
                    throws HyphenationException {

                // TODO gene: hyphenate unimplemented
                return false;
            }

            public int insertLigatures(final NodeList list, final int start)
                    throws HyphenationException {

                // TODO gene: insertLigatures unimplemented
                return 0;
            }

            public void insertShy(final NodeList nodes,
                    final int insertionPoint, final boolean[] spec,
                    final CharNode hyphenNode) throws HyphenationException {

                throw new RuntimeException("unimplemented");
            }

            public boolean isHyphenActive() throws HyphenationException {

                return false;
            }

            public UnicodeCharList normalize(final UnicodeCharList word,
                    final TypesetterOptions options)
                    throws HyphenationException {

                throw new RuntimeException("unimplemented");
            }

            public void setHyphenActive(final boolean active)
                    throws HyphenationException {

            }

            public void setLeftHyphenmin(final long left)
                    throws HyphenationException {

            }

            public void setName(final String name) {

                throw new RuntimeException("unimplemented");
            }

            public void setRightHyphenmin(final long right)
                    throws HyphenationException {

            }

        });
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _test4() throws Exception {

        HorizontalListNode nodes = new HorizontalListNode();
        nodes.add(new GlueNode(VPT, true));
        nodes.add(new CharNode(tc, UnicodeChar.get('a')));
        nodes.add(new CharNode(tc, UnicodeChar.get('b')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('c')));
        nodes.add(new CharNode(tc, UnicodeChar.get('d')));
        nodes.add(new DiscretionaryNode(null, null, null));
        nodes.add(new CharNode(tc, UnicodeChar.get('e')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('f')));

        NodeList list = pb.build(nodes);

        assertTrue(list instanceof VerticalListNode);
        assertEquals(2, list.size());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _test5() throws Exception {

        HorizontalListNode nodes = new HorizontalListNode();
        nodes.add(new GlueNode(VPT, true));
        nodes.add(new CharNode(tc, UnicodeChar.get('a')));
        nodes.add(new CharNode(tc, UnicodeChar.get('b')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('c')));
        nodes.add(new CharNode(tc, UnicodeChar.get('d')));
        nodes.add(new DiscretionaryNode(new HorizontalListNode(),
                new HorizontalListNode(), new HorizontalListNode()));
        nodes.add(new CharNode(tc, UnicodeChar.get('e')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('f')));

        NodeList list = pb.build(nodes);

        assertTrue(list instanceof VerticalListNode);
        assertEquals(2, list.size());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _testBreak1() throws Exception {

        NodeList list = pb.build(makeList("a"));

        assertTrue(list instanceof VerticalListNode);
        assertEquals(1, list.size());

        Node node = list.get(0);
        assertTrue(node instanceof HorizontalListNode);
        list = (NodeList) node;
        assertTrue(list.get(0) instanceof CharNode);
        assertTrue(list.get(1) instanceof PenaltyNode);
        assertTrue(list.get(2) instanceof GlueNode);
        assertEquals(3, list.size());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _testBreak2() throws Exception {

        HorizontalListNode nodes = new HorizontalListNode();
        nodes.add(new GlueNode(VPT, true));
        nodes.add(new CharNode(tc, UnicodeChar.get('a')));
        nodes.add(new CharNode(tc, UnicodeChar.get('b')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('c')));
        nodes.add(new CharNode(tc, UnicodeChar.get('d')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('e')));

        NodeList list = pb.build(nodes);

        assertTrue(list instanceof VerticalListNode);
        assertEquals(2, list.size());
    }

    /**
     * <testcase>
     *  Test case checking that discretionary without content may be contained
     *  in the non-broken text.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _testDisc2() throws Exception {

        HorizontalListNode nodes = new HorizontalListNode();
        nodes.add(new GlueNode(VPT, true));
        nodes.add(new CharNode(tc, UnicodeChar.get('a')));
        nodes.add(new CharNode(tc, UnicodeChar.get('b')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('c')));
        nodes.add(new DiscretionaryNode(new HorizontalListNode(),
                new HorizontalListNode(), new HorizontalListNode()));
        nodes.add(new CharNode(tc, UnicodeChar.get('d')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('e')));

        NodeList list = pb.build(nodes);

        assertTrue(list instanceof VerticalListNode);
        assertEquals(2, list.size());
    }

    /**
     * <testcase>
     *  Test case checking that the empty list is treated correctly.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void _testEmpty1() throws Exception {

        NodeList list = pb.build(makeList(""));

        assertTrue(list instanceof VerticalListNode);
        assertEquals(0, list.size());
    }

    /**
     * This method creates a new paragraph builder to be tested.
     *
     * @return the new paragraph builder
     */
    protected abstract ParagraphBuilder getParagraphBuilder();

    /**
     * Build a node list from a string specification.
     *
     * @param spec the spec
     * @return the node list
     */
    protected HorizontalListNode makeList(final String spec) {

        String s = spec;
        if (p1 == null) {
            p1 = Pattern
                    .compile("^\\discretionary\\{([^{}]*)\\}\\{([^{}]*)\\}\\{([^{}]*)\\}(.*)");
            p2 = Pattern.compile("^\\rule\\{([^{}]*)\\}(.*)");
            p3 = Pattern.compile("^\\glue(.*)");
        }
        HorizontalListNode nodes = new HorizontalListNode();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
            } else if (c == '\\') {
                Matcher m = p1.matcher(s.substring(i));
                if (m.matches()) {
                    nodes.add(new DiscretionaryNode(makeList(m.group(1)),
                            makeList(m.group(2)), makeList(m.group(3))));
                    s = m.group(4);
                    i = -1;
                    continue;
                }
                m = p2.matcher(s.substring(i));
                if (m.matches()) {
                    nodes.add(new RuleNode(VPT, Dimen.ONE_PT, Dimen.ONE_PT, tc,
                            true));
                    s = m.group(2);
                    i = -1;
                    continue;
                }
                m = p3.matcher(s.substring(i));
                if (m.matches()) {
                    nodes.add(new GlueNode(Dimen.ONE_PT, true));
                    s = m.group(1);
                    i = -1;
                    continue;
                }
            } else {
                nodes.add(new CharNode(tc, UnicodeChar.get('a')));
            }
        }
        return nodes;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        super.setUp();

        if (tracer == null) {
            tracer = Logger.getLogger(AbstractParagraphBuiderTester.class
                    .getName());
            tracer.setUseParentHandlers(false);
            if (traceonline()) {
                Handler handler = new ConsoleHandler();
                handler.setLevel(Level.ALL);
                handler.setFormatter(new LogFormatter());
                tracer.addHandler(handler);
                tracer.setLevel(Level.ALL);
            }
        }

        pb = getParagraphBuilder();
        if (pb instanceof LogEnabled) {
            ((LogEnabled) pb).enableLogging(tracer);
        }
        pb.setOptions(new MockOptions());
    }

    /**
     * <testcase>
     *  Test case checking that ...
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void test6() throws Exception {

        HorizontalListNode nodes = new HorizontalListNode();
        nodes.add(new GlueNode(VPT, true));
        nodes.add(new CharNode(tc, UnicodeChar.get('a')));
        nodes.add(new CharNode(tc, UnicodeChar.get('b')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('c')));
        nodes.add(new CharNode(tc, UnicodeChar.get('d')));
        nodes.add(new DiscretionaryNode(new HorizontalListNode(new RuleNode(
                new Dimen(0x20), Dimen.ZERO_PT, Dimen.ZERO_PT, tc, true)),
                new HorizontalListNode(new RuleNode(new Dimen(0x30),
                        Dimen.ZERO_PT, Dimen.ZERO_PT, tc, true)),
                new HorizontalListNode(new RuleNode(new Dimen(0x40),
                        Dimen.ZERO_PT, Dimen.ZERO_PT, tc, true))));
        nodes.add(new CharNode(tc, UnicodeChar.get('e')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('f')));

        NodeList list = pb.build(nodes);

        assertTrue(list instanceof VerticalListNode);
        assertEquals(2, list.size());

        Node nl = list.get(0);
        assertTrue(nl instanceof HorizontalListNode);
        assertEquals(8, ((HorizontalListNode) nl).size());
        assertTrue(((HorizontalListNode) nl).get(6) instanceof RuleNode);

        nl = list.get(1);
        assertTrue(nl instanceof HorizontalListNode);
        assertEquals(6, ((HorizontalListNode) nl).size());
        assertTrue(((HorizontalListNode) nl).get(0) instanceof RuleNode);

        StringBuffer sb = new StringBuffer();
        list.toString(sb, "\n", Integer.MAX_VALUE, Integer.MAX_VALUE);
        tracer.info(sb.toString());
    }

    /**
     * <testcase>
     *  Test case checking that discretionary without content may be contained
     *  in the non-broken text.
     * </testcase>
     *
     * @throws Exception in case of an error
     */
    public void testDisc1() throws Exception {

        HorizontalListNode nodes = new HorizontalListNode();
        nodes.add(new GlueNode(VPT, true));
        nodes.add(new CharNode(tc, UnicodeChar.get('a')));
        nodes.add(new CharNode(tc, UnicodeChar.get('b')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('c')));
        nodes.add(new DiscretionaryNode(null, null, null));
        nodes.add(new CharNode(tc, UnicodeChar.get('d')));
        nodes.add(new SpaceNode(new Glue(Dimen.ONE_PT)));
        nodes.add(new CharNode(tc, UnicodeChar.get('e')));

        NodeList list = pb.build(nodes);

        assertTrue(list instanceof VerticalListNode);
        assertEquals(2, list.size());
    }

    /**
     * This method provides an indicator whether or not the tracing should be
     * written to the console.
     * This method is meant to be overwritten by derived classes to change the
     * default behavior.
     *
     * @return <code>true</code> iff the tracing is requested
     */
    protected boolean traceonline() {

        return false;
    }

}
