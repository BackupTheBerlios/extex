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

package de.dante.extex.hyphenation.impl;

import junit.framework.TestCase;
import de.dante.extex.font.FontFactory;
import de.dante.extex.font.FontFile;
import de.dante.extex.font.Glyph;
import de.dante.extex.font.Kerning;
import de.dante.extex.font.Ligature;
import de.dante.extex.font.type.BoundingBox;
import de.dante.extex.font.type.tfm.TFMFixWord;
import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.hyphenation.base.BaseHyphenationTable;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.CodeChangeObserver;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.Direction;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.scanner.type.TokenFactoryImpl;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.DiscretionaryNode;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.observer.Observer;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class BaseHyphenationTableTest extends TestCase {

    /**
     * This mock implementation is for test purposes only.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
     */
    private class MockContext implements Context {

        /**
         * The field <tt>tokenFactory</tt> contains the ...
         */
        private TokenFactory tokenFactory = new TokenFactoryImpl();

        /**
         * @see de.dante.extex.interpreter.context.ContextGroup#afterGroup(de.dante.util.observer.Observer)
         */
        public void afterGroup(final Observer observer) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextGroup#afterGroup(de.dante.extex.scanner.type.Token)
         */
        public void afterGroup(final Token t) throws InterpreterException {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextGroup#closeGroup(de.dante.extex.typesetter.Typesetter, de.dante.extex.interpreter.TokenSource)
         */
        public void closeGroup(final Typesetter typesetter,
                final TokenSource source) throws InterpreterException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#esc(java.lang.String)
         */
        public String esc(final String name) {

            return "\\" + name;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#esc(de.dante.extex.scanner.type.Token)
         */
        public String esc(final Token token) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#escapechar()
         */
        public char escapechar() {

            return '\\';
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#expand(de.dante.extex.interpreter.type.tokens.Tokens, de.dante.extex.typesetter.Typesetter)
         */
        public Tokens expand(final Tokens tokens, final Typesetter typesetter)
                throws GeneralException {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getAfterassignment()
         */
        public Token getAfterassignment() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getBox(java.lang.String)
         */
        public Box getBox(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.Tokenizer#getCatcode(de.dante.util.UnicodeChar)
         */
        public Catcode getCatcode(final UnicodeChar c) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getCode(de.dante.extex.scanner.type.CodeToken)
         */
        public Code getCode(final CodeToken t) throws InterpreterException {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getCount(java.lang.String)
         */
        public Count getCount(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getDelcode(de.dante.util.UnicodeChar)
         */
        public Count getDelcode(final UnicodeChar c) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextDimen#getDimen(java.lang.String)
         */
        public Dimen getDimen(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextErrorCount#getErrorCount()
         */
        public int getErrorCount() {

            return 0;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFont#getFont(java.lang.String)
         */
        public Font getFont(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFont#getFontFactory()
         */
        public FontFactory getFontFactory() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getGlue(java.lang.String)
         */
        public Glue getGlue(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextGroup#getGroupLevel()
         */
        public long getGroupLevel() {

            return 0;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getHyphenationTable(java.lang.String)
         */
        public HyphenationTable getHyphenationTable(final String language)
                throws InterpreterException {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getId()
         */
        public String getId() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFile#getInFile(java.lang.String)
         */
        public InFile getInFile(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getInteraction()
         */
        public Interaction getInteraction() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getLccode(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getLccode(final UnicodeChar uc) {

            // TODO gene: getLccode unimplemented
            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getMagnification()
         */
        public long getMagnification() {

            return 0;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getMathcode(de.dante.util.UnicodeChar)
         */
        public Count getMathcode(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getMuskip(java.lang.String)
         */
        public Muskip getMuskip(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.Tokenizer#getNamespace()
         */
        public String getNamespace() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFile#getOutFile(java.lang.String)
         */
        public OutFile getOutFile(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getParshape()
         */
        public ParagraphShape getParshape() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getSfcode(de.dante.util.UnicodeChar)
         */
        public Count getSfcode(final UnicodeChar uc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getTokenFactory()
         */
        public TokenFactory getTokenFactory() {

            return tokenFactory;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getTokenizer()
         */
        public Tokenizer getTokenizer() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getToks(java.lang.String)
         */
        public Tokens getToks(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getToksOrNull(java.lang.String)
         */
        public Tokens getToksOrNull(final String name) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getTypesettingContext()
         */
        public TypesettingContext getTypesettingContext() {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#getUccode(de.dante.util.UnicodeChar)
         */
        public UnicodeChar getUccode(final UnicodeChar lc) {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextErrorCount#incrementErrorCount()
         */
        public int incrementErrorCount() {

            return 0;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextGroup#isGlobalGroup()
         */
        public boolean isGlobalGroup() {

            return false;
        }

        /**
         * @see de.dante.extex.interpreter.context.ContextGroup#openGroup()
         */
        public void openGroup() throws ConfigurationException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#popConditional()
         */
        public Conditional popConditional() throws InterpreterException {

            return null;
        }

        /**
         * @see de.dante.extex.interpreter.context.Context#pushConditional(de.dante.util.Locator, boolean)
         */
        public void pushConditional(final Locator locator, final boolean value) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#registerCodeChangeObserver(de.dante.extex.interpreter.context.CodeChangeObserver, de.dante.extex.scanner.type.Token)
         */
        public void registerCodeChangeObserver(
                final CodeChangeObserver observer, final Token name) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setAfterassignment(de.dante.extex.scanner.type.Token)
         */
        public void setAfterassignment(final Token token) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setBox(java.lang.String, de.dante.extex.interpreter.type.box.Box, boolean)
         */
        public void setBox(final String name, final Box value,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setCatcode(de.dante.util.UnicodeChar, de.dante.extex.scanner.type.Catcode, boolean)
         */
        public void setCatcode(final UnicodeChar c, final Catcode cc,
                final boolean global) throws HelpingException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setCode(de.dante.extex.scanner.type.CodeToken, de.dante.extex.interpreter.type.Code, boolean)
         */
        public void setCode(final CodeToken t, final Code code,
                final boolean global) throws InterpreterException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setCount(java.lang.String, long, boolean)
         */
        public void setCount(final String name, final long value,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setDelcode(de.dante.util.UnicodeChar, de.dante.extex.interpreter.type.count.Count, boolean)
         */
        public void setDelcode(final UnicodeChar c, final Count code,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextDimen#setDimen(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, boolean)
         */
        public void setDimen(final String name, final Dimen value,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextDimen#setDimen(java.lang.String, long, boolean)
         */
        public void setDimen(final String name, final long value,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFont#setFont(java.lang.String, de.dante.extex.interpreter.type.font.Font, boolean)
         */
        public void setFont(final String name, final Font font,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFont#setFontFactory(de.dante.extex.font.FontFactory)
         */
        public void setFontFactory(final FontFactory fontFactory) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setGlue(java.lang.String, de.dante.extex.interpreter.type.glue.Glue, boolean)
         */
        public void setGlue(final String name, final Glue value,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setId(java.lang.String)
         */
        public void setId(final String id) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFile#setInFile(java.lang.String, de.dante.extex.interpreter.type.file.InFile, boolean)
         */
        public void setInFile(final String name, final InFile file,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setInteraction(de.dante.extex.interpreter.Interaction, boolean)
         */
        public void setInteraction(final Interaction interaction,
                final boolean global) throws InterpreterException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setLccode(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
         */
        public void setLccode(final UnicodeChar uc, final UnicodeChar lc) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setMagnification(long)
         */
        public void setMagnification(final long mag) throws HelpingException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setMathcode(de.dante.util.UnicodeChar, de.dante.extex.interpreter.type.count.Count, boolean)
         */
        public void setMathcode(final UnicodeChar uc, final Count code,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setMuskip(java.lang.String, de.dante.extex.interpreter.type.muskip.Muskip, boolean)
         */
        public void setMuskip(final String name, final Muskip value,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setNamespace(java.lang.String, boolean)
         */
        public void setNamespace(final String namespace, final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.ContextFile#setOutFile(java.lang.String, de.dante.extex.interpreter.type.file.OutFile, boolean)
         */
        public void setOutFile(final String name, final OutFile file,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setParshape(de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
         */
        public void setParshape(final ParagraphShape shape) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setSfcode(de.dante.util.UnicodeChar, de.dante.extex.interpreter.type.count.Count, boolean)
         */
        public void setSfcode(final UnicodeChar uc, final Count code,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setStandardTokenStream(de.dante.extex.scanner.stream.TokenStream)
         */
        public void setStandardTokenStream(final TokenStream standardTokenStream) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setTokenFactory(de.dante.extex.scanner.type.TokenFactory)
         */
        public void setTokenFactory(final TokenFactory factory) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setToks(java.lang.String, de.dante.extex.interpreter.type.tokens.Tokens, boolean)
         */
        public void setToks(final String name, final Tokens toks,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(de.dante.extex.interpreter.context.Color)
         */
        public void setTypesettingContext(final Color color)
                throws ConfigurationException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(de.dante.extex.interpreter.context.Direction)
         */
        public void setTypesettingContext(final Direction direction)
                throws ConfigurationException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(de.dante.extex.interpreter.type.font.Font)
         */
        public void setTypesettingContext(final Font font)
                throws ConfigurationException {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(de.dante.extex.interpreter.context.TypesettingContext)
         */
        public void setTypesettingContext(final TypesettingContext context) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(de.dante.extex.interpreter.context.TypesettingContext, boolean)
         */
        public void setTypesettingContext(final TypesettingContext context,
                final boolean global) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#setUccode(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
         */
        public void setUccode(final UnicodeChar lc, final UnicodeChar uc) {

        }

        /**
         * @see de.dante.extex.interpreter.context.Context#unregisterCodeChangeObserver(de.dante.extex.interpreter.context.CodeChangeObserver, de.dante.extex.scanner.type.Token)
         */
        public void unregisterCodeChangeObserver(
                final CodeChangeObserver observer, final Token name) {

        }
    }

    /**
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
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
     * TODO gene: missing JavaDoc.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.2 $
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
     * The command line interface.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(BaseHyphenationTableTest.class);
    }

    /**
     * The field <tt>context</tt> contains the ...
     */
    private Context context;

    /**
     * The field <tt>table</tt> contains the ...
     */
    private HyphenationTable table;

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

        context = new MockContext();
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