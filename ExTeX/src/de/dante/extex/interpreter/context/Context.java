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
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.interpreter.type.InFile;
import de.dante.extex.interpreter.type.Muskip;
import de.dante.extex.interpreter.type.OutFile;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
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
 * @version $Revision: 1.15 $
 */
public interface Context extends Serializable {

    /**
     * ...
     *
     * @param t ...
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    Code getCode(Token t) throws GeneralException;

    /**
     * Setter for active characters in the current group.
     *
     * @param name the name of the active character
     * @param code the assigned code
     */
    void setActive(String name, Code code);

    /**
     * Setter for active characters in the requested group.
     *
     * @param name the name of the active character
     * @param code the assigned code
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setActive(String name, Code code, boolean global);

    /**
     * Getter for active characters.
     *
     * @param name the name of the active character
     *
     * @return the code assigned to the active character or <code>null</code>
     *         if none is assigned
     */
    Code getActive(String name);

    /**
     * Getter for the afterassignment token.
     *
     * @return the afterassignment token.
     */
    Token getAfterassignment();

    /**
     * Setter for the afterassignment token.
     *
     * @param token the afterassignment token.
     */
    void setAfterassignment(Token token);

    /**
     * Setter for the catcode of a character in the current group.
     *
     * @param c the character to assign a catcode for
     * @param cc the catcode of the character
     *
     * @throws GeneralHelpingException in case of an error
     */
    void setCatcode(UnicodeChar c, Catcode cc) throws GeneralHelpingException;

    /**
     * Setter for the catcode of a character in the specified groups.
     *
     * @param c the character to assign a catcode for
     * @param cc the catcode of the character
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     *
     * @throws GeneralHelpingException in case of an error
     */
    void setCatcode(UnicodeChar c, Catcode cc, boolean global)
            throws GeneralHelpingException;

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Box box}
     * register in the current group. Count registers are named, either with a
     * number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setBox(String name, Box value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Box box}
     * register in all requested groups. Count registers are named, either with
     * a number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setBox(String name, Box value, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Box box}
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
     * Setter for the {@link de.dante.extex.interpreter.type.Count count}
     * register in the current group. Count registers are named, either with a
     * number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setCount(String name, long value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Count count}
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
     * Getter for the {@link de.dante.extex.interpreter.type.Count count}
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
     * Getter for the font factory.
     *
     * @return the fontFactory.
     */
    FontFactory getFontFactory();

    /**
     * Setter for the font factory.
     *
     * @param fontFactory the fontFactory to set.
     */
    void setFontFactory(FontFactory fontFactory);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Tokens toks}
     * register in the current group. Tokens registers are named, either with a
     * number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param toks the new value of the register
     */
    void setToks(String name, Tokens toks);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Tokens toks}
     * register in the current group. Tokens registers are named, either with a
     * number or an arbitrary string. The numbered registers where limited to
     * 256 in TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param toks the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setToks(String name, Tokens toks, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Tokens toks}
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
     * ...
     *
     * @param name ...
     * @param value ...
     */
    void setDimen(String name, Dimen value);

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     */
    void setDimen(String name, long value);

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDimen(String name, Dimen value, boolean global);

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDimen(String name, long value, boolean global);

    /**
     * ...
     *
     * @param name ...
     *
     * @return ...
     */
    Dimen getDimen(String name);

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     */
    void setGlue(String name, Glue value);

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setGlue(String name, Glue value, boolean global);

    /**
     * ...
     *
     * @param name ...
     *
     * @return ...
     */
    Glue getGlue(String name);

    /**
     * ...
     *
     * @param context ...
     */
    void setTypesettingContext(TypesettingContext context);

    /**
     * ...
     *
     * @param context the processor context
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setTypesettingContext(TypesettingContext context, boolean global);

    /**
     * Getter for the typesetting context.
     *
     * @return the typesetting context
     */
    TypesettingContext getTypesettingContext();

    /**
     * Test whether this group is the first one, which means that there is no
     * group before and closing this group would fail.
     *
     * @return <code>true</code> iff this is the first group
     */
    boolean isGlobalGroup();

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
     * Getter for the interaction. The interaction determines how verbose the
     * actions are reported and how the interaction with the user is performed
     * in case of an error.
     *
     * @return the current interaction
     */
    Interaction getInteraction();

    /**
     * Declare the translation from an upper case character to a lower case
     * character.
     *
     * @param uc upper case character
     * @param lc lower case equivalent
     */
    void setLccode(UnicodeChar uc, UnicodeChar lc);

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
     * Declare the translation from a lower case character to an upper case
     * character.
     *
     * @param lc lower  case character
     * @param uc uppercase equivalent
     */
    void setUccode(UnicodeChar lc, UnicodeChar uc);

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
     * ...
     *
     * @param name the name of the macro
     * @param code ...
     */
    void setMacro(String name, Code code);

    /**
     * ...
     *
     * @param name ...
     * @param code ...
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMacro(String name, Code code, boolean global);

    /**
     * ...
     *
     * @param name ...
     *
     * @return ...
     */
    Code getMacro(String name);

    /**
     * Setter for the magnification. The magnification is a global value which
     * can be assigned at most once. It contains the magnification factor in
     * permille. The default value is 1000. It can only take positive numbers
     * as values. A maximal value can be enforced by an implementation.
     *
     * @param mag the new magnification factor
     *
     * @throws GeneralHelpingException in case that the magnification factor is
     *             not in the allowed range or that the magnification has been
     *             set to a different value earlier.
     */
    void setMagnification(long mag) throws GeneralHelpingException;

    /**
     * Getter for the magnification factor in permille. The default value is
     * 1000. It can only take positive numbers as values.
     *
     * @return the magnification factor
     */
    long getMagnification();

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     */
    void setMuskip(String name, Muskip value);

    /**
     * ...
     *
     * @param name ...
     * @param value ...
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMuskip(String name, Muskip value, boolean global);

    /**
     * ...
     *
     * @param name ...
     *
     * @return ...
     */
    Muskip getMuskip(String name);

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
     * Add a token to the tokens inserted after the group has been closed.
     *
     * @param t the token to add
     *
     * @throws GeneralException in case of an error
     */
    void afterGroup(Token t) throws GeneralException;

    /**
     * Register a observer to be called at the end of the group.
     *
     * @param observer the observer to register
     */
    void afterGroup(Observer observer);

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    void closeGroup(Typesetter typesetter, TokenSource source) throws GeneralException;

    /**
     * ...
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    long popConditional() throws GeneralException;

    /**
     * ...
     *
     * @param locator the locator of the start
     * @param value the boolean value
     */
    void pushConditional(Locator locator, long value);

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
     * ...
     *
     * @param name ...
     * @param file ...
     */
    void setInFile(String name, InFile file);

    /**
     * ...
     *
     * @param name ...
     * @param file ...
     * @param global ...
     */
    void setInFile(String name, InFile file, boolean global);

    /**
     * ...
     *
     * @param name ...
     * @return ...
     */
    InFile getInFile(String name);
    
    /**
     * ...
     *
     * @param name ...
     * @param file ...
     */
    void setOutFile(String name, OutFile file);

    /**
     * ...
     *
     * @param name ...
     * @param file ...
     * @param global ...
     */
    void setOutFile(String name, OutFile file, boolean global);

    /**
     * ...
     *
     * @param name ...
     *
     * @return ...
     */
    OutFile getOutFile(String name);

}
