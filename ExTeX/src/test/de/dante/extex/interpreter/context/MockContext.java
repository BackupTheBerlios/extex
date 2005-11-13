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

package de.dante.extex.interpreter.context;

import de.dante.extex.color.ColorConverter;
import de.dante.extex.font.FontFactory;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.observer.group.AfterGroupObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.LanguageManager;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.scanner.type.token.TokenFactoryImpl;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This mock implementation of a context does nothing useful but provide dummy
 * methods. It is meant as a base for derived mock implementations in test
 * classes.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.25 $
 */
public class MockContext implements Context, TypesetterOptions {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The field <tt>tokenFactory</tt> contains the token factory.
     */
    private TokenFactory tokenFactory = new TokenFactoryImpl();

    /**
     * Creates a new object.
     */
    public MockContext() {

        super();
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#afterGroup(AfterGroupObserver)
     */
    public void afterGroup(final AfterGroupObserver observer) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#afterGroup(de.dante.extex.scanner.type.Token)
     */
    public void afterGroup(final Token t) throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#clearSplitMarks()
     */
    public void clearSplitMarks() {

    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#closeGroup(de.dante.extex.typesetter.Typesetter, de.dante.extex.interpreter.TokenSource)
     */
    public void closeGroup(final Typesetter typesetter, final TokenSource source)
            throws InterpreterException {

        throw new RuntimeException("unimplemented");
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

        return token.toText();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#escapechar()
     */
    public char escapechar() {

        return '\\';
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#expand(de.dante.extex.interpreter.type.tokens.Tokens, de.dante.extex.typesetter.Typesetter)
     */
    public Tokens expand(final Tokens tokens, final Typesetter typesetter)
            throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getAfterassignment()
     */
    public Token getAfterassignment() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getBottomMark(java.lang.String)
     */
    public Tokens getBottomMark(final Object name) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getBox(java.lang.String)
     */
    public Box getBox(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.Tokenizer#getCatcode(de.dante.util.UnicodeChar)
     */
    public Catcode getCatcode(final UnicodeChar c) {

        if (c.isLetter()) {
            return Catcode.LETTER;
        }
        switch (c.getCodePoint()) {
            case ' ':
                return Catcode.SPACE;
            case '{':
                return Catcode.LEFTBRACE;
            case '}':
                return Catcode.RIGHTBRACE;
            case '\\':
                return Catcode.ESCAPE;
            default:
                return Catcode.OTHER;
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#getCode(de.dante.extex.scanner.type.CodeToken)
     */
    public Code getCode(final CodeToken t) throws InterpreterException {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextColor#getColorConverter()
     */
    public ColorConverter getColorConverter() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getConditional()
     */
    public Conditional getConditional() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCount#getCount(java.lang.String)
     */
    public Count getCount(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getCountOption(java.lang.String)
     */
    public FixedCount getCountOption(final String name) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getDelcode(de.dante.util.UnicodeChar)
     */
    public MathDelimiter getDelcode(final UnicodeChar c) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextDimen#getDimen(java.lang.String)
     */
    public Dimen getDimen(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getDimenOption(java.lang.String)
     */
    public FixedDimen getDimenOption(final String name) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextErrorCount#getErrorCount()
     */
    public int getErrorCount() {

        return 0;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getFirstMark(java.lang.String)
     */
    public Tokens getFirstMark(final Object name) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFont#getFont(java.lang.String)
     */
    public Font getFont(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFont#getFontFactory()
     */
    public FontFactory getFontFactory() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getGlue(java.lang.String)
     */
    public Glue getGlue(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getGlueOption(java.lang.String)
     */
    public FixedGlue getGlueOption(final String name) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#getGroupLevel()
     */
    public long getGroupLevel() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getId()
     */
    public String getId() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getIfLevel()
     */
    public long getIfLevel() {

        return 0;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFile#getInFile(java.lang.String)
     */
    public InFile getInFile(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getInteraction()
     */
    public Interaction getInteraction() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getLanguage(java.lang.String)
     */
    public Language getLanguage(final String language)
            throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getLccode(de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLccode(final UnicodeChar uc) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMagnification()
     */
    public long getMagnification() {

        return 1000;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMathcode(de.dante.util.UnicodeChar)
     */
    public Count getMathcode(final UnicodeChar uc) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMuskip(java.lang.String)
     */
    public Muskip getMuskip(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.Tokenizer#getNamespace()
     */
    public String getNamespace() {

        return Namespace.DEFAULT_NAMESPACE;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFile#getOutFile(java.lang.String)
     */
    public OutFile getOutFile(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getParshape()
     */
    public ParagraphShape getParshape() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getSfcode(de.dante.util.UnicodeChar)
     */
    public Count getSfcode(final UnicodeChar uc) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getSplitBottomMark(java.lang.String)
     */
    public Tokens getSplitBottomMark(final Object name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getSplitFirstMark(java.lang.String)
     */
    public Tokens getSplitFirstMark(final Object name) {

        throw new RuntimeException("unimplemented");
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

        return this;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextTokens#getToks(java.lang.String)
     */
    public Tokens getToks(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextTokens#getToksOrNull(java.lang.String)
     */
    public Tokens getToksOrNull(final String name) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getTopMark(java.lang.String)
     */
    public Tokens getTopMark(final Object name) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getTypesettingContext()
     */
    public TypesettingContext getTypesettingContext() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getTypesettingContextFactory()
     */
    public TypesettingContextFactory getTypesettingContextFactory() {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getUccode(de.dante.util.UnicodeChar)
     */
    public UnicodeChar getUccode(final UnicodeChar lc) {

        throw new RuntimeException("unimplemented");
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
    public void openGroup() throws ConfigurationException, InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#popConditional()
     */
    public Conditional popConditional() throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#pushConditional(de.dante.util.Locator, boolean, String)
     */
    public void pushConditional(final Locator locator, final boolean value,
            Code primitive, long branch, boolean neg) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(de.dante.extex.interpreter.context.Color)
     */
    public void set(final Color color, final boolean global)
            throws ConfigurationException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(de.dante.extex.interpreter.context.Direction)
     */
    public void set(final Direction direction, final boolean global)
            throws ConfigurationException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(de.dante.extex.interpreter.type.font.Font)
     */
    public void set(final Font font, final boolean global)
            throws ConfigurationException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(de.dante.extex.language.Language)
     */
    public void set(final Language language, final boolean global)
            throws ConfigurationException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(de.dante.extex.interpreter.context.TypesettingContext, boolean)
     */
    public void set(final TypesettingContext context, final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setAfterassignment(de.dante.extex.scanner.type.Token)
     */
    public void setAfterassignment(final Token token) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setBox(java.lang.String, de.dante.extex.interpreter.type.box.Box, boolean)
     */
    public void setBox(final String name, final Box value, final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCatcode(de.dante.util.UnicodeChar, de.dante.extex.scanner.type.Catcode, boolean)
     */
    public void setCatcode(final UnicodeChar c, final Catcode cc,
            final boolean global) throws HelpingException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#setCode(de.dante.extex.scanner.type.CodeToken, de.dante.extex.interpreter.type.Code, boolean)
     */
    public void setCode(final CodeToken t, final Code code, final boolean global)
            throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCount#setCount(java.lang.String, long, boolean)
     */
    public void setCount(final String name, final long value,
            final boolean global) throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDelcode(de.dante.util.UnicodeChar, MathDelimiter, boolean)
     */
    public void setDelcode(final UnicodeChar c, final MathDelimiter delimiter,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextDimen#setDimen(java.lang.String, de.dante.extex.interpreter.type.dimen.Dimen, boolean)
     */
    public void setDimen(final String name, final Dimen value,
            final boolean global) throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextDimen#setDimen(java.lang.String, long, boolean)
     */
    public void setDimen(final String name, final long value,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFont#setFont(java.lang.String, de.dante.extex.interpreter.type.font.Font, boolean)
     */
    public void setFont(final String name, final Font font, final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFont#setFontFactory(de.dante.extex.font.FontFactory)
     */
    public void setFontFactory(final FontFactory fontFactory) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setGlue(java.lang.String, de.dante.extex.interpreter.type.glue.Glue, boolean)
     */
    public void setGlue(final String name, final Glue value,
            final boolean global) throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setId(java.lang.String)
     */
    public void setId(final String id) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFile#setInFile(java.lang.String, de.dante.extex.interpreter.type.file.InFile, boolean)
     */
    public void setInFile(final String name, final InFile file,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setInteraction(de.dante.extex.interpreter.Interaction)
     */
    public void setInteraction(final Interaction interaction)
            throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setLanguageManager(de.dante.extex.hyphenation.HyphenationManager)
     */
    public void setLanguageManager(final LanguageManager manager) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setLccode(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public void setLccode(final UnicodeChar uc, final UnicodeChar lc,
            boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMagnification(long)
     */
    public void setMagnification(final long mag) throws HelpingException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMark(java.lang.String, de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void setMark(Object name, Tokens mark) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMathcode(de.dante.util.UnicodeChar, de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setMathcode(final UnicodeChar uc, final Count code,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMuskip(java.lang.String, de.dante.extex.interpreter.type.muskip.Muskip, boolean)
     */
    public void setMuskip(final String name, final Muskip value,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setNamespace(java.lang.String, boolean)
     */
    public void setNamespace(final String namespace, final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextFile#setOutFile(java.lang.String, de.dante.extex.interpreter.type.file.OutFile, boolean)
     */
    public void setOutFile(final String name, final OutFile file,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setParshape(de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
     */
    public void setParshape(final ParagraphShape shape) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setSfcode(de.dante.util.UnicodeChar, de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setSfcode(final UnicodeChar uc, final Count code,
            final boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#setSplitMark(java.lang.String, de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void setSplitMark(final Object name, final Tokens mark) {

    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setStandardTokenStream(de.dante.extex.scanner.stream.TokenStream)
     */
    public void setStandardTokenStream(final TokenStream standardTokenStream) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setTokenFactory(de.dante.extex.scanner.type.TokenFactory)
     */
    public void setTokenFactory(final TokenFactory factory) {

        tokenFactory = factory;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextTokens#setToks(java.lang.String, de.dante.extex.interpreter.type.tokens.Tokens, boolean)
     */
    public void setToks(final String name, final Tokens toks,
            final boolean global) throws InterpreterException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(de.dante.extex.interpreter.context.TypesettingContext)
     */
    public void setTypesettingContext(final TypesettingContext context) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setUccode(de.dante.util.UnicodeChar, de.dante.util.UnicodeChar)
     */
    public void setUccode(final UnicodeChar lc, final UnicodeChar uc,
            boolean global) {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#startMarks()
     */
    public void startMarks() {

        throw new RuntimeException("unimplemented");
    }

}
