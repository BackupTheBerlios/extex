/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.font.FontFactory;
import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
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
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.observer.Observer;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.26 $
 */
public interface Context extends Serializable {

    /**
     * Register a observer to be called at the end of the group.
     *
     * @param observer the observer to register
     */
    void afterGroup(Observer observer);

    /**
     * Add a token to the tokens inserted after the group has been closed.
     *
     * @param t the token to add
     *
     * @throws GeneralException in case of an error
     */
    void afterGroup(Token t) throws GeneralException;

    /**
     * Perform all actions required upon the closing of a group.
     *
     * @param typesetter the typesetter to invoke if needed
     * @param source the source to get Tokens from if needed
     *
     * @throws GeneralException in case of an error
     */
    void closeGroup(Typesetter typesetter, TokenSource source)
            throws GeneralException;

    /**
     * Getter for the afterassignment token.
     *
     * @return the afterassignment token.
     */
    Token getAfterassignment();

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.box.Box box}
     * register. Count registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in TeX.
     * This restriction does no longer hold for ExTeX.
     *
     * @param name the name or number of the count register
     *
     * @return the count register or <code>null</code> if it is void
     */
    Box getBox(String name);

    /**
     * Convenience method to get the code assigned to a Token.
     * If the Token is a ControlSequenceToken then the macro is returned.
     * If the Token is a ActiveCharacterToken then the active value is returned.
     * Otherwise <code>null</code> is returned.
     *
     * @param t the Token to differentiate on
     *
     * @return the code for the token
     *
     * @throws GeneralException in case of an error
     */
    Code getCode(Token t) throws GeneralException;

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.count.Count count}
     * register. Count registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in TeX.
     * This restriction does no longer hold for ExTeX.
     *
     * @param name the name or number of the count register
     *
     * @return the count register or <code>null</code> if it is not defined
     */
    Count getCount(String name);

    /**
     * Getter for the delcode mapping.
     *
     * @param c the character to which the delcode is assigned
     *
     * @return the delcode for the given character
     */
    Count getDelcode(UnicodeChar c);

    /**
     * Get the current value of the dimen register with a given name.
     *
     * @param name the name or the number of the register
     *
     * @return the dimen register for the given name
     */
    Dimen getDimen(String name);

    /**
     * Getter for a current font register.
     *
     * @param name the name or the number of the register
     *
     * @return the named font register or <code>null</code> if none is set
     */
    Font getFont(String name);

    /**
     * Getter for the font factory.
     *
     * @return the fontFactory.
     */
    FontFactory getFontFactory();

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
     * Getter for the hyphenation record for a given language. The language is
     * used to find the hyphenation table. If the language is not known an
     * attempt is made to load it. Otherwise the default hyphenation table is
     * returned.
     *
     * @param language the name of the language to use
     *
     * @return the hyphenation table for the requested language
     *
     * @throws GeneralException in case of an error
     */
    HyphenationTable getHyphenationTable(int language) throws GeneralException;

    /**
     * Getter for a input file register.
     *
     * @param name the name or the number of the file register
     *
     * @return the input file descriptor
     */
    InFile getInFile(String name);

    /**
     * Getter for the interaction. The interaction determines how verbose the
     * actions are reported and how the interaction with the user is performed
     * in case of an error.
     *
     * @return the current interaction
     */
    Interaction getInteraction();

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
     * Getter for the magnification factor in permille. The default value is
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
     * Getter for the current namespace.
     *
     * @return the current namespace
     */
    String getNamespace();

    /**
     * Getter for an output file descriptor.
     *
     * @param name the name or the number of the file register
     *
     * @return the output file descriptor
     */
    OutFile getOutFile(String name);

    /**
     * Getter for the spacefactor code of a character.
     *
     * @param uc the Unicode character
     *
     * @return the spacefactor code.
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
     * Getter for the {@link de.dante.extex.interpreter.type.tokens.Tokens toks}
     * register. Tokens registers are named, either with a number or an
     * arbitrary string. The numbered registers where limited to 256 in TeX.
     * This restriction does no longer hold for ExTeX.
     *
     * @param name the name or number of the token register
     *
     * @return the token register or <code>null</code> if it is not defined
     */
    Tokens getToks(String name);

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
     * Test whether this group is the first one, which means that there is no
     * group before and closing this group would fail.
     *
     * @return <code>true</code> iff this is the first group
     */
    boolean isGlobalGroup();

    /**
     * This method can be used to open another group. The current group is
     * pushed onto the stack to be reactivated when the new group will be
     * closed.
     *
     * @throws ConfigurationException in case of an error in the configuration,
     *             e.g. the class for the group can not be determined.
     */
    void openGroup() throws ConfigurationException;

    /**
     * Pop the management information for a conditional from the stack and
     * return it. If the stack is empty then <code>null</code> is returned.
     *
     * @return the formerly topmost element from the conditional stack
     *
     * @throws GeneralException in case of an error
     */
    Conditional popConditional() throws GeneralException;

    /**
     * Put a value onto the conditional stack.
     *
     * @param locator the locator for the start of the if statement
     * @param value the value to push
     */
    void pushConditional(Locator locator, boolean value);

    /**
     * Register an observer for code change events.
     * Code change events are triggered when the assignment of a macro or
     * active character changes. In this case the appropriate method in the
     * observer is invoked.
     *
     * @param observer the observer to receive the events
     * @param name the token to be observed. This should be a macro or
     * active character token.
     */
    void registerCodeChangeObserver(CodeChangeObserver observer, Token name);

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
     * 256 in TeX. This restriction does no longer hold for ExTeX.
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
     * Setter for the code assigned to a Token.
     * The Token has to be either a
     * {@link de.dante.extex.scanner.ActiveCharacterToken ActiveCharacterToken}
     * or a
     * {@link de.dante.extex.scanner.ControlSequenceToken ControlSequenceToken}.
     *
     * @param t the Token to set the code for
     * @param code the code for the token
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws GeneralException in case of an error
     */
    void setCode(Token t, Code code, boolean global) throws GeneralException;

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.count.Count count}
     * register in all requested groups. Count registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setCount(String name, long value, boolean global);

    /**
     * Setter for the delcode mapping.
     *
     * @param c the character to which the delcode is assigned
     * @param code the del code
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDelcode(UnicodeChar c, Count code, boolean global);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.dimen.Dimen Dimen}
     * register in all requested groups. Dimen registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDimen(String name, Dimen value, boolean global);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.dimen.Dimen Dimen}
     * register in all requested groups. Dimen registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDimen(String name, long value, boolean global);

    /**
     * Setter for font registers.
     *
     * @param name the name or the number of the register
     * @param font the new Font value
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setFont(String name, Font font, boolean global);

    /**
     * Setter for the font factory.
     *
     * @param fontFactory the fontFactory to set.
     */
    void setFontFactory(FontFactory fontFactory);

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
     * Setter for the {@link de.dante.extex.interpreter.type.file.InFile InFile}
     * register in all requested groups. InFile registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 16 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the file register
     * @param file the input file descriptor
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setInFile(String name, InFile file, boolean global);

    /**
     * Setter for the interaction in all requested groups. The interaction
     * determines how verbose the actions are reported and how the interaction
     * with the user is performed in case of an error.
     *
     * @param interaction the new value of the interaction
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws GeneralException in case of an error
     */
    void setInteraction(Interaction interaction, boolean global)
            throws GeneralException;

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
     * permille. The default value is 1000. It can only take positive numbers
     * as values. A maximal value can be enforced by an implementation.
     *
     * @param mag the new magnification factor
     *
     * @throws HelpingException in case that the magnification factor is
     *             not in the allowed range or that the magnification has been
     *             set to a different value earlier.
     */
    void setMagnification(long mag) throws HelpingException;

    /**
     * Setter for the mathcode of a character
     *
     * @param uc the character index
     * @param code the new mathcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMathcode(UnicodeChar uc, Count code, boolean global);

    /**
     * Setter for a muskip register.
     *
     * @param name the name or the number of the register
     * @param value the new value
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMuskip(String name, Muskip value, boolean global);

    /**
     * Setter for the namespace.
     *
     * @param namespace the new namespace
     */
    void setNamespace(String namespace);

    /**
     * Setter for a outfile descriptor.
     *
     * @param name the name or the number of the file register
     * @param file the descriptor of the output file
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setOutFile(String name, OutFile file, boolean global);

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
     * Setter for the {@link de.dante.extex.interpreter.type.tokens.Tokens toks}
     * register in the specified groups. Tokens registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param toks the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setToks(String name, Tokens toks, boolean global);

    /**
     * Setter for the color in the current typesetting context.
     *
     * @param color the new color
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void setTypesettingContext(Color color) throws ConfigurationException;

    /**
     * Setter for the direction in the current typesetting context.
     *
     * @param direction the new direction
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void setTypesettingContext(Direction direction)
            throws ConfigurationException;

    /**
     * Setter for the font in the current typesetting context.
     *
     * @param font the new font
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    void setTypesettingContext(Font font) throws ConfigurationException;

    /**
     * Setter for the typesetting context in the current group.
     *
     * @param context the new typesetting context
     */
    void setTypesettingContext(TypesettingContext context);

    /**
     * Setter for the typesetting context in the specified groups.
     *
     * @param context the processor context
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setTypesettingContext(TypesettingContext context, boolean global);

    /**
     * Declare the translation from a lower case character to an upper case
     * character.
     *
     * @param lc lower  case character
     * @param uc uppercase equivalent
     */
    void setUccode(UnicodeChar lc, UnicodeChar uc);

    /**
     * Remove a registered observer for code change events.
     * Code change events are triggered when the assignment of a macro or
     * active character changes. In this case the appropriate method in the
     * observer is invoked.
     *
     * @param observer the observer to receive the events
     * @param name the token to be observed. This should be a macro or
     * active character token.
     */
    void unregisterCodeChangeObserver(CodeChangeObserver observer, Token name);

}