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

package de.dante.extex.interpreter.context.impl;

import java.io.Serializable;

import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.exception.InterpreterException;
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
import de.dante.extex.typesetter.Typesetter;
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
 * @version $Revision: 1.26 $
 */
public interface Group extends Tokenizer, Serializable {

    /**
     * Register an observer to be invoked after the group has been closed.
     *
     * @param observer the observer to register
     */
    void afterGroup(Observer observer);

    /**
     * Add the token to the tokens to be inserted after the group is closed.
     *
     * @param t the token to add
     */
    void afterGroup(Token t);

    /**
     * Getter for the tokens which are inserted after the group has been
     * closed.
     *
     * @return the after group tokens
     */
    Tokens getAfterGroup();

    /**
     * Getter for the {@link de.dante.extex.interpreter.type.box.Box box}register.
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
     * Getter for the catcode of a character.
     *
     * @param c the Unicode character to analyze
     *
     * @return the catcode of a character
     */
    Catcode getCatcode(UnicodeChar c);

    /**
     * Getter for the definition of an active character or macro.
     *
     * @param token the name of the active character or macro
     *
     * @return the code associated to the name or <code>null</code> if none
     *         is defined yet
     */
    Code getCode(CodeToken token);

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
     * Getter for the delcode of a charcter.
     * The sfcode is -1 unless changed explicitely.
     *
     * @param uc the character to get the delcode for
     *
     * @return the delcode for the given character
     */
    Count getDelcode(UnicodeChar uc);

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
     * Getter for the current font.
     *
     * @param name the name of the font
     *
     * @return the current font
     */
    Font getFont(String name);

    /**
     * Getter for the boolean value.
     *
     * @param name the name of the boolean
     *
     * @return the value
     */
    boolean getIf(String name);

    /**
     * Getter for the input file descriptor. In the case that the named
     * descriptor doe not exist yet a new one is returned.
     *
     * @param name the name of the descriptor to get
     *
     * @return the input file descriptor
     */
    InFile getInFile(String name);

    /**
     * Getter for the current interaction mode.
     *
     * @return the interaction mode
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
     * Getter for the group level. The group level is the number of groups which
     * are currently open. Thus this number of groups can be closed. Since the
     * top-level group can not be closed this group counts as 0.
     *
     * @return the group level
     */
    long getLevel();

    /**
     * Getter for the mathcode of a character.
     *
     * @param uc the character to get the mathcode for
     *
     * @return the mathcode for the given character
     */
    Count getMathcode(UnicodeChar uc);

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
     * Getter for the current namespace.
     *
     * @return the current namespace
     */
    String getNamespace();

    /**
     * Getter for the next group in the linked list. Maybe this method should
     * be hidden.
     *
     * @return the next group
     */
    Group getNext();

    /**
     * Getter for the output file descriptor.
     *
     * @param name the name of the descriptor to get
     *
     * @return the output file descriptor
     */
    OutFile getOutFile(String name);

    /**
     * Getter for the space factor code of a character.
     * The sfcode is 999 for letters and 1000 for other characters unless
     * changed explicitely.
     *
     * @param uc the character for which the sfcode is requested
     *
     * @return the sfcode of the given character
     */
    Count getSfcode(UnicodeChar uc);

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
     * Getter for the named toks register in the current group. The name can
     * either be a string representing a number or an arbitrary string. In the
     * first case the behavior of the numbered toks registers is emulated. The
     * other case can be used to store special toks values.
     *
     * Note: The number of toks registers is not limited to 256 as in TeX.
     *
     * @param name the name of the toks register
     *
     * @return the value of the toks register or <code>null</code> if none is
     *  defined
     */
    Tokens getToksOrNull(String name);

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
     * Invoke all registered observers for the end-of-group event.
     *
     * @param observable the observable to use as sender
     * @param typesetter the typesetter
     *
     *  @throws InterpreterException in case of an error
     */
    void runAfterGroup(Observable observable, Typesetter typesetter)
            throws InterpreterException;

    /**
     * Setter for the {@link de.dante.extex.interpreter.type.box.Box box}
     * register in all requested groups. Count registers are named, either with
     * a number or an arbitrary string.
     * The numbered registers where limited to 256 in
     * TeX. This restriction does no longer hold for ExTeX.
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
     * @param c the character
     * @param code the catcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setCatcode(UnicodeChar c, Catcode code, boolean global);

    /**
     * Setter for active characters or macros in the requested group.
     *
     * @param token the name of the active character, i.e. a single letter
     *            string
     * @param code the new code
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setCode(Token token, Code code, boolean global);

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
     * Setter for the delcode of a character.
     *
     * @param uc the character to set the delcode for
     * @param code the new delcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setDelcode(UnicodeChar uc, Count code, boolean global);

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
     * Setter for the font with a given name.
     *
     * @param name the name of the font
     * @param font the new font
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setFont(String name, Font font, boolean global);

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
     * Setter for an inoputr file.
     *
     * @param name the name of the input file
     * @param file the input file specification
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setInFile(String name, InFile file, boolean global);

    /**
     * Setter for the interaction mode in the requested groups.
     *
     * @param interaction the new interaction mode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setInteraction(Interaction interaction, boolean global);

    /**
     * Declare the translation from an upper case character to a lower case
     * character.
     *
     * @param uc upper case character
     * @param lc lower case equivalent
     */
    void setLccode(UnicodeChar uc, UnicodeChar lc);

    /**
     * Setter for the mathcode of a character.
     *
     * @param uc the character to set the mathcode for
     * @param code the new mathcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setMathcode(UnicodeChar uc, Count code, boolean global);

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
     * Setter for the namespace.
     * @param namespace the new namespace
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setNamespace(String namespace, boolean global);

    /**
     * Setter for the output file for a given name.
     *
     * @param name the name of the output file
     * @param file the output file specification
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setOutFile(String name, OutFile file, boolean global);

    /**
     * Setter for the sfcode of a character.
     *
     * @param uc the character to set the sfcode for
     * @param code the new sfcode
     * @param global the indicator for the scope; <code>true</code> means all
     *            groups; otherwise the current group is affected only
     */
    void setSfcode(UnicodeChar uc, Count code, boolean global);

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
     * Setter for standardTokenStream.
     *
     * @param standardTokenStream the standardTokenStream to set.
     */
    void setStandardTokenStream(TokenStream standardTokenStream);

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
     * Declare the translation from a lower case character to an upper case
     * character.
     *
     * @param lc lower  case character
     * @param uc uppercase equivalent
     */
    void setUccode(UnicodeChar lc, UnicodeChar uc);

}