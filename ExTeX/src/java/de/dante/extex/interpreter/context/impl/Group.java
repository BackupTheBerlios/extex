/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter.context.impl;

import java.io.Serializable;

import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.TypesettingContext;
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
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;

/**
 * This is the implementation of a group object. A group is the container for
 * all data which might have group local values. In contrast ton TeX the groups
 * are organized as linked objects. A new group contains just the values which
 * have local definitions. Thus the opening and closing of groups are rather
 * fast. The access to the value might be slow when many groups have to be
 * passed to find the one containing the value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public interface Group extends Tokenizer, Serializable {

    /**
     * Setter for active characters in the current group.
     *
     * @param name the name of the active character, i.e. a single letter
     *            string
     * @param code the new code
     */
    void setActive(String name, Code code);

    /**
     * Setter for active characters in the requested group.
     *
     * @param name the name of the active character, i.e. a single letter
     *            string
     * @param code the new code
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setActive(String name, Code code, boolean global);

    /**
     * Getter for the definition of an active character. In fact the name is
     * assumed to be a string containing a single character but it can be any
     * string.
     *
     * @param name the name of the active character
     *
     * @return the code associated to the name or <code>null</code> if none
     *         is defined yet
     */
    Code getActive(String name);

    /**
     * Getter for the tokens which are inserted after the group has been
     * closed.
     *
     * @return the after group tokens
     */
    Tokens getAfterGroup();

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Box box}register
     * in the current group. Count registers are named, either with a number or
     * an arbitrary string. The numbered registers where limited to 256 in TeX.
     * This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     */
    void setBox(String name, Box value);

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.Box box}register
     * in all requested groups. Count registers are named, either with a number
     * or an arbitrary string. The numbered registers where limited to 256 in
     * TeX. This restriction does no longer hold for ExTeX.
     *
     * @param name the name or the number of the register
     * @param value the new value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setBox(String name, Box value, boolean global);

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.Box box}register.
     * Count registers are named, either with a number or an arbitrary string.
     * The numbered registers where limited to 256 in TeX. This restriction
     * does no longer hold for ExTeX.
     *
     * @param name the name or number of the count register
     *
     * @return the count register or <code>null</code> if it is not defined
     */
    Box getBox(String name);

    /**
     * Setter for the catcode of a character in the current group.
     *
     * @param c the character
     * @param code the catcode
     */
    void setCatcode(UnicodeChar c, Catcode code);

    /**
     * Setter for the catcode of a character in the specified groups.
     *
     * @param c the character
     * @param code the catcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setCatcode(UnicodeChar c, Catcode code, boolean global);

    /**
     * Setter for the count register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setCount(String name, Count value);

    /**
     * Setter for a count register in the requested groups.
     *
     * @param name the name of the count register
     * @param value the value of the count register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setCount(String name, Count value, boolean global);

    /**
     * Getter for the named count register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered count registers is emulated. The
     * other case can be used to store special count values.
     *
     * Note: The number of count registers is not limited to 256 as in TeX.
     *
     * As a default value 0 is returned.
     *
     * @param name the name of the count register
     *
     * @return the value of the count register or its default
     */
    Count getCount(String name);

    /**
     * Setter for a dimen register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setDimen(String name, Dimen value);

    /**
     * Setter for a dimen register in the requested groups.
     *
     * @param name the name of the count register
     * @param value the value of the count register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDimen(String name, Dimen value, boolean global);

    /**
     * Getter for the named dimen register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered dimen registers is emulated. The
     * other case can be used to store special dimen values.
     *
     * Note: The number of dimen registers is not limited to 256 as in TeX.
     *
     * As a default value 0 is returned.
     *
     * @param name the name of the dimen register
     *
     * @return the value of the dimen register or its default
     */
    Dimen getDimen(String name);

    /**
     * Setter for the value of the booleans in the current group.
     *
     * @param name the name of the boolean
     * @param value the truth value
     */
    void setIf(String name, boolean value);

    /**
     * Setter for the value of the booleans in all groups.
     *
     * @param name the name of the boolean
     * @param value the truth value
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setIf(String name, boolean value, boolean global);

    /**
     * Getter for the boolean value.
     *
     * @param name the name of the boolean
     *
     * @return the value
     */
    boolean getIf(String name);

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
     * Setter for the interaction mode in the current group.
     *
     * @param interaction the new interaction mode
     */
    void setInteraction(Interaction interaction);

    /**
     * Setter for the interaction mode in the requested groups.
     *
     * @param interaction the new interaction mode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setInteraction(Interaction interaction, boolean global);

    /**
     * Getter for the current interaction mode.
     *
     * @return the interaction mode
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
     * Setter for the definition of a macro in the current group.
     *
     * @param name the name of the macro
     * @param code the new definition
     */
    void setMacro(String name, Code code);

    /**
     * Setter for the definition of a macro in all groups.
     *
     * @param name the name of the macro
     * @param code the new definition
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMacro(String name, Code code, boolean global);

    /**
     * Getter for the definition of a macro.
     *
     * @param name the name of the macro
     *
     * @return the definition of <code>null</code>
     */
    Code getMacro(String name);

    /**
     * Setter for a muskip register in all groups.
     *
     * @param name the name of the count register
     * @param value the value of the count register
     */
    void setMuskip(String name, Muskip value);

    /**
     * Setter for the muskip register in the requested groups.
     *
     * @param name the name of the register
     * @param value the value of the register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMuskip(String name, Muskip value, boolean global);

    /**
     * Getter for the named muskip register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered muskip registers is emulated.
     * The other case can be used to store special muskip values.
     *
     * Note: The number of muskip registers is not limited to 256 as in TeX.
     *
     * As a default value 0 is returned.
     *
     * @param name the name of the count register
     *
     * @return the value of the count register or its default
     */
    Muskip getMuskip(String name);

    /**
     * Getter for the next group in the linked list. Maybe this method should
     * be hidden.
     *
     * @return the next group
     */
    Group getNext();

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
     * @return ...
     */
    OutFile getOutFile(String name);

    /**
     * Setter for the skip register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setSkip(String name, Glue value);

    /**
     * Setter for a skip register in all groups.
     *
     * @param name the name of the count register
     * @param value the value of the count register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setSkip(String name, Glue value, boolean global);

    /**
     * Getter for the named skip register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered skip registers is emulated. The
     * other case can be used to store special skip values.
     *
     * Note: The number of skip registers is not limited to 256 as in TeX.
     *
     * As a default value 0 is returned.
     *
     * @param name the name of the count register
     *
     * @return the value of the count register or its default
     */
    Glue getSkip(String name);

    /**
     * Setter for the toks register in the current group.
     *
     * @param name the name of the register
     * @param value the value of the register
     */
    void setToks(String name, Tokens value);

    /**
     * Setter for a toks register in all groups.
     *
     * @param name the name of the toks register
     * @param value the value of the toks register
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setToks(String name, Tokens value, boolean global);

    /**
     * Getter for the named toks register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered toks registers is emulated. The
     * other case can be used to store special toks values.
     *
     * Note: The number of toks registers is not limited to 256 as in TeX.
     *
     * As a default value the empty toks register is returned.
     *
     * @param name the name of the toks register
     *
     * @return the value of the toks register or its default
     */
    Tokens getToks(String name);

    /**
     * Setter for the typesetting context in the current group.
     *
     * @param context the new typesetting context
     */
    void setTypesettingContext(TypesettingContext context);

    /**
     * Setter for the typesetting context in the specified groups.
     *
     * @param context the new typesetting context
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setTypesettingContext(TypesettingContext context, boolean global);

    /**
     * ...
     *
     * @return ...
     */
    TypesettingContext getTypesettingContext();

    /**
     * ...
     *
     * @param t ...
     */
    void afterGroup(Token t);

    /**
     * ...
     *
     * @param observer ...
     */
    void afterGroup(Observer observer);

    /**
     * ...
     *
     * @param source ...
     * @param typesetter ...
     *
     *  @throws GeneralException ...
     */
    void runAfterGroup(Observable source, Typesetter typesetter)
            throws GeneralException;

}
