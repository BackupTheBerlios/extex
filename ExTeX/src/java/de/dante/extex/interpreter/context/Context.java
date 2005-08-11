/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

import java.io.Serializable;

import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.language.Language;
import de.dante.extex.language.LanguageManager;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.59 $
 */
public interface Context
        extends
            ContextCode,
            ContextCount,
            ContextDimen,
            ContextFile,
            ContextFont,
            ContextGroup,
            ContextErrorCount,
            ContextInteraction,
            ContextTokens,
            Tokenizer,
            Serializable {

    /**
     * Attach the current escape character in front of a name and return the
     * result.
     * <p>
     * This method is meant to produce a printable version of a control
     * sequence for error messages.
     * </p>
     *
     * @param name the name of the macro
     *
     * @return the control sequence including the escape character
     */
    String esc(String name);

    /**
     * This method is meant to produce a printable version of a control
     * sequence for error messages.
     *
     * @param token the token
     *
     * @return the control sequence including the escape character
     */
    String esc(Token token);

    /**
     * Return the current escape character or <code>\0<code> if it is undefined.
     * This is a convenience method.
     *
     * @return the escape character
     */
    char escapechar();

    /**
     * Getter for the afterassignment token.
     *
     * @return the afterassignment token.
     */
    Token getAfterassignment();

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.box.Box box}
     * register. Count registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in
     * <logo>TeX</logo>.
     * This restriction does no longer hold for <logo>ExTeX</logo>.
     *
     * @param name the name or number of the count register
     *
     * @return the count register or <code>null</code> if it is void
     */
    Box getBox(String name);

    /**
     * Getter for the delimiter code mapping.
     *
     * @param c the character to which the delcode is assigned
     *
     * @return the delcode for the given character
     */
    MathDelimiter getDelcode(UnicodeChar c);

    /**
     * Getter for a glue register.
     *
     * @param name the name of the glue register to acquire.
     *
     * @return the value of the named glue register or <code>null</code>
     *  if none is set
     */
    Glue getGlue(String name);

    /**
     * Getter for the id string. The id string is the classification of the
     * original source like given in the format file. The id string can be
     * <code>null</code> if not known yet.
     *
     * @return the id string
     */
    String getId();

    /**
     * Getter for the hyphenation record for a given language. The language is
     * used to find the hyphenation table. If the language is not known an
     * attempt is made to create it. Otherwise the default hyphenation table is
     * returned.
     *
     * @param language the name of the language to use
     *
     * @return the hyphenation table for the requested language
     *
     * @throws InterpreterException in case of an error
     */
    Language getLanguage(String language)
            throws InterpreterException;

    /**
     * Getter for the lccode mapping of upper case characters to their
     * lower case equivalent.
     *
     * @param uc the upper case character
     *
     * @return the lower case equivalent or null if none exists
     */
    UnicodeChar getLccode(UnicodeChar uc);

    /**
     * Getter for the magnification factor in per mille. The default value is
     * 1000. It can only take positive numbers as values.
     *
     * @return the magnification factor
     */
    long getMagnification();

    /**
     * Getter for the mathcode of a character.
     *
     * @param uc the character index
     *
     * @return the mathcode
     */
    Count getMathcode(UnicodeChar uc);

    /**
     * Getter for a muskip register.
     *
     * @param name the name or the number of the register
     *
     * @return the named muskip or <code>null</code> if none is set
     */
    Muskip getMuskip(String name);

    /**
     * Getter for the current name space.
     *
     * @return the current namespace
     */
    String getNamespace();

    /**
     * Getter fot the paragraph shape.
     *
     * @return the paragraph shape or <code>null</code> if no special shape
     *   is present
     */
    ParagraphShape getParshape();

    /**
     * Getter for the space factor code of a character.
     *
     * @param uc the Unicode character
     *
     * @return the space factor code.
     */
    Count getSfcode(UnicodeChar uc);

    /**
     * Getter for the token factory. The token factory can be used to get new
     * tokens of some kind.
     *
     * @return the token factory
     */
    TokenFactory getTokenFactory();

    /**
     * Getter for the tokenizer.
     *
     * @return the tokenizer
     */
    Tokenizer getTokenizer();

    /**
     * Getter for the typesetting context.
     *
     * @return the typesetting context
     */
    TypesettingContext getTypesettingContext();

    /**
     * Getter for the uccode mapping of lower case characters to their
     * upper case equivalent.
     *
     * @param lc the upper case character
     *
     * @return the upper case equivalent or null if none exists
     */
    UnicodeChar getUccode(UnicodeChar lc);

    /**
     * Pop the management information for a conditional from the stack and
     * return it. If the stack is empty then <code>null</code> is returned.
     *
     * @return the formerly topmost element from the conditional stack
     *
     * @throws InterpreterException in case of an error
     */
    Conditional popConditional() throws InterpreterException;

    /**
     * Put a value onto the conditional stack.
     * @param locator the locator for the start of the if statement
     * @param value the value to push
     * @param primitive the name of the primitive which triggered this
     *  operation
     */
    void pushConditional(Locator locator, boolean value, String primitive);

    /**
     * Setter for the afterassignment token.
     *
     * @param token the afterassignment token.
     */
    void setAfterassignment(Token token);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.box.Box box}
     * register in the current group. Count registers are named, either with a
     * number or an arbitrary string. The numbered registers where limited to
     * 256 in <logo>TeX</logo>. This restriction does no longer hold for
     * <logo>ExTeX</logo>.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setBox(String name, Box value, boolean global);

    /**
     * Setter for the catcode of a character in the specified groups.
     *
     * @param c the character to assign a catcode for
     * @param cc the catcode of the character
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws HelpingException in case of an error
     */
    void setCatcode(UnicodeChar c, Catcode cc, boolean global)
            throws HelpingException;

    /**
     * Setter for the delimiter code mapping.
     *
     * @param c the character to which the delcode is assigned
     * @param delimiter the delimiter code
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDelcode(UnicodeChar c, MathDelimiter delimiter, boolean global);

    /**
     * Setter for a glue register.
     *
     * @param name the name of the glue register
     * @param value the glue value to set
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setGlue(String name, Glue value, boolean global);

    /**
     * Setter for the id string. The id string is the classification of the
     * original source like given in the fmt file.
     *
     * @param id the id string
     */
    void setId(String id);

    /**
     * Setter for the hyphenation manager
     *
     * @param manager the hyphenatin manager
     */
    void setLanguageManager(LanguageManager manager);

    /**
     * Declare the translation from an upper case character to a lower case
     * character.
     *
     * @param uc upper case character
     * @param lc lower case equivalent
     */
    void setLccode(UnicodeChar uc, UnicodeChar lc);

    /**
     * Setter for the magnification. The magnification is a global value which
     * can be assigned at most once. It contains the magnification factor in
     * per mille. The default value is 1000. It can only take positive numbers
     * as values. A maximal value can be enforced by an implementation.
     *
     * @param mag the new magnification factor
     *
     * @throws HelpingException in case that the magnification factor is
     *  not in the allowed range or that the magnification has been
     *  set to a different value earlier.
     */
    void setMagnification(long mag) throws HelpingException;

    /**
     * Setter for the mathcode of a character
     *
     * @param uc the character index
     * @param code the new mathcode
     * @param global the indicator for the scope; <code>true</code> means all
     *  groups; otherwise the current group is affected only
     */
    void setMathcode(UnicodeChar uc, Count code, boolean global);

    /**
     * Setter for a muskip register.
     *
     * @param name the name or the number of the register
     * @param value the new value
     * @param global the indicator for the scope; <code>true</code> means all
     *  groups; otherwise the current group is affected only
     */
    void setMuskip(String name, Muskip value, boolean global);

    /**
     * Setter for the namespace.
     *
     * @param namespace the new namespace
     * @param global the indicator for the scope; <code>true</code> means all
     *  groups; otherwise the current group is affected only
     */
    void setNamespace(String namespace, boolean global);

    /**
     * Setter for the paragraph shape.
     *
     * @param shape the new paragraph shape
     */
    void setParshape(ParagraphShape shape);

    /**
     * Setter for the spece factor code in the specified groups.
     * Any character has an associated space factor. This value can be set
     * with the current method.
     *
     * @param uc the Unicode character to assign the sfcode to
     * @param code the new sfcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setSfcode(UnicodeChar uc, Count code, boolean global);

    /**
     * Setter for standardTokenStream.
     *
     * @param standardTokenStream the standardTokenStream to set.
     */
    void setStandardTokenStream(TokenStream standardTokenStream);

    /**
     * Setter for the token factory.
     *
     * @param factory the new token factory
     */
    void setTokenFactory(TokenFactory factory);

    /**
     * Setter for the color in the current typesetting context.
     *
     * @param color the new color
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void set(Color color, boolean global) throws ConfigurationException;

    /**
     * Setter for the direction in the current typesetting context.
     *
     * @param direction the new direction
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void set(Direction direction, boolean global)
            throws ConfigurationException;

    /**
     * Setter for the font in the current typesetting context.
     *
     * @param font the new font
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void set(Font font, boolean global) throws ConfigurationException;

    /**
     * Setter for the language in the current typesetting context.
     *
     * @param language the new language
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void set(Language language, boolean global) throws ConfigurationException;

    /**
     * Setter for the typesetting context in the specified groups.
     *
     * @param context the processor context
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void set(TypesettingContext context, boolean global);

    /**
     * Declare the translation from a lower case character to an upper case
     * character.
     *
     * @param lc lower  case character
     * @param uc uppercase equivalent
     */
    void setUccode(UnicodeChar lc, UnicodeChar uc);

}