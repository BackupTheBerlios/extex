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
package de.dante.extex.interpreter.context;

import java.io.Serializable;

import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.configuration.Configurable;
import de.dante.util.configuration.ConfigurationException;

/**
 * This interface describes the container for all data of an interpreter
 * context.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public interface Context extends Configurable, Serializable {

	/**
	 * Setter for active characters in the current group.
	 * 
	 * @param name
	 *                 the name of the active character
	 * @param code
	 *                 the assigned code
	 */
	public abstract void setActive(String name, Code code);

	/**
	 * Setter for active characters in the requested group.
	 * 
	 * @param name
	 *                 the name of the active character
	 * @param code
	 *                 the assigned code
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setActive(String name, Code code, boolean global);

	/**
	 * Getter for active characters.
	 * 
	 * @param name
	 *                 the name of the active character
	 * 
	 * @return the code assigned to the active character or <code>null</code>
	 *            if none is assigned
	 */
	public abstract Code getActive(String name);

	/**
	 * Setter for the catcode of a character in the current group.
	 * 
	 * @param c
	 *                 the character to assign a catcode for
	 * @param cc
	 *                 the catcode of the character
	 * 
	 * @throws GeneralHelpingException
	 *                 in case of an error
	 */
	public abstract void setCatcode(char c, Catcode cc) throws GeneralHelpingException;

	/**
	 * Setter for the catcode of a character in the specified groups.
	 * 
	 * @param c
	 *                 the character to assign a catcode for
	 * @param cc
	 *                 the catcode of the character
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 * 
	 * @throws GeneralHelpingException
	 *                 in case of an error
	 */
	public abstract void setCatcode(char c, Catcode cc, boolean global) throws GeneralHelpingException;

	/**
	 * Setter for the {@link de.dante.extex.interpreter.type.Count count}
	 * register in the current group. Count registers are named, either with a
	 * number or an arbitrary string. The numbered registers where limited to
	 * 256 in TeX. This restriction does no longer hold for ExTeX.
	 * 
	 * @param name
	 *                 the name or the number of the register
	 * @param value
	 *                 the new value of the register
	 */
	public abstract void setCount(String name, long value);

	/**
	 * Setter for the {@link de.dante.extex.interpreter.type.Count count}
	 * register in all requested groups. Count registers are named, either with
	 * a number or an arbitrary string. The numbered registers where limited to
	 * 256 in TeX. This restriction does no longer hold for ExTeX.
	 * 
	 * @param name
	 *                 the name or the number of the register
	 * @param value
	 *                 the new value of the register
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setCount(String name, long value, boolean global);

	/**
	 * Getter for the {@link de.dante.extex.interpreter.type.Count count}
	 * register. Count registers are named, either with a number or an
	 * arbitrary string. The numbered registers where limited to 256 in TeX.
	 * This restriction does no longer hold for ExTeX.
	 * 
	 * @param name
	 *                 the name or number of the count register
	 * 
	 * @return the count register or <code>null</code> if it is not defined
	 */
	public abstract Count getCount(String name);

	/**
	 * Setter for the {@link de.dante.extex.interpreter.type.Real real}
	 * register in the current group. Count registers are named, either with a
	 * number or an arbitrary string. The numbered registers where limited to
	 * 256 in TeX. This restriction does no longer hold for ExTeX.
	 * 
	 * @param name the name or the number of the register
	 * @param value the new value of the register
	 */
	public abstract void setReal(String name, Real value);

	/**
	 * Setter for the {@link de.dante.extex.interpreter.type.Real real}
	 * register in all requested groups. Count registers are named, either with
	 * a number or an arbitrary string. The numbered registers where limited to
	 * 256 in TeX. This restriction does no longer hold for ExTeX.
	 * 
	 * @param name the name or the number of the register
	 * @param value the new value of the register
	 * @param global the indicator for the scope; <code>true</code> means all
	 *                       groups; otherwise the current group is affected only
	 */
	public abstract void setReal(String name, Real value, boolean global);

	/**
	 * Getter for the {@link de.dante.extex.interpreter.type.Real real}
	 * register. Count registers are named, either with a number or an
	 * arbitrary string. The numbered registers where limited to 256 in TeX.
	 * This restriction does no longer hold for ExTeX.
	 * 
	 * @param name the name or number of the count register
	 * 
	 * @return the real register or <code>null</code> if it is not defined
	 */
	public abstract Real getReal(String name);

	/**
	 * Setter for the {@link de.dante.extex.interpreter.type.Tokens toks}
	 * register in the current group. Tokens registers are named, either with a
	 * number or an arbitrary string. The numbered registers where limited to
	 * 256 in TeX. This restriction does no longer hold for ExTeX.
	 * 
	 * @param name
	 *                 the name or the number of the register
	 * @param toks
	 *                 the new value of the register
	 */
	public abstract void setToks(String name, Tokens toks);

	/**
	 * Setter for the {@link de.dante.extex.interpreter.type.Tokens toks}
	 * register in the current group. Tokens registers are named, either with a
	 * number or an arbitrary string. The numbered registers where limited to
	 * 256 in TeX. This restriction does no longer hold for ExTeX.
	 * 
	 * @param name
	 *                 the name or the number of the register
	 * @param toks
	 *                 the new value of the register
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setToks(String name, Tokens toks, boolean global);

	/**
	 * Getter for the {@link de.dante.extex.interpreter.type.Tokens toks}
	 * register. Tokens registers are named, either with a number or an
	 * arbitrary string. The numbered registers where limited to 256 in TeX.
	 * This restriction does no longer hold for ExTeX.
	 * 
	 * @param name
	 *                 the name or number of the token register
	 * 
	 * @return the token register or <code>null</code> if it is not defined
	 */
	public abstract Tokens getToks(String name);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * @param value
	 *                 ...
	 */
	public abstract void setDimen(String name, Dimen value);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * @param value
	 *                 ...
	 */
	public abstract void setDimen(String name, long value);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * @param value
	 *                 ...
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setDimen(String name, Dimen value, boolean global);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * @param value
	 *                 ...
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setDimen(String name, long value, boolean global);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * 
	 * @return ...
	 */
	public abstract Dimen getDimen(String name);

	/**
	 * ...
	 * 
	 * @param context
	 *                 ...
	 */
	public abstract void setTypesettingContext(TypesettingContext context);

	/**
	 * ...
	 * 
	 * @param context
	 *                 ...
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setTypesettingContext(TypesettingContext context, boolean global);

	/**
	 * Getter for the typesetting context.
	 * 
	 * @return the typesetting context
	 */
	public abstract TypesettingContext getTypesettingContext();

	/**
	 * Test whether this group is the first one, which means that there is no
	 * group before and closing this group would fail.
	 * 
	 * @return <code>true</code> iff this is the first group
	 */
	public abstract boolean isGlobalGroup();

	/**
	 * Getter for the hyphenation record for a given language. The language is
	 * used to find the hyphenation table. If the language is not known an
	 * attempt is made to load it. Otherwise the default hyphenation table is
	 * returned.
	 * 
	 * @param language
	 *                 the name of the language to use
	 * 
	 * @return the hyphenation table for the requested language
	 * 
	 * @throws GeneralException
	 *                 in case of an error
	 */
	public abstract HyphenationTable getHyphenationTable(int language) throws GeneralException;

	/**
	 * Setter for the interaction in the current group. The interaction
	 * determines how verbose the actions are reported and how the interaction
	 * with the user is performed in case of an error.
	 * 
	 * @param interaction
	 *                 the new value of the interaction
	 */
	public abstract void setInteraction(Interaction interaction);

	/**
	 * Setter for the interaction in all requested groups. The interaction
	 * determines how verbose the actions are reported and how the interaction
	 * with the user is performed in case of an error.
	 * 
	 * @param interaction
	 *                 the new value of the interaction
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setInteraction(Interaction interaction, boolean global);

	/**
	 * Getter for the interaction. The interaction determines how verbose the
	 * actions are reported and how the interaction with the user is performed
	 * in case of an error.
	 * 
	 * @return the current interaction
	 */
	public abstract Interaction getInteraction();

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * @param code
	 *                 ...
	 */
	public abstract void setMacro(String name, Code code);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * @param code
	 *                 ...
	 * @param global
	 *                 the indicator for the scope; <code>true</code> means all
	 *                 groups; otherwise the current group is affected only
	 */
	public abstract void setMacro(String name, Code code, boolean global);

	/**
	 * ...
	 * 
	 * @param name
	 *                 ...
	 * 
	 * @return ...
	 */
	public abstract Code getMacro(String name);

	/**
	 * Setter for the magnification. The magnification is a global value which
	 * can be assigned at most once. It contains the magnification factor in
	 * permille. The default value is 1000. It can only take positive numbers
	 * as values. A maximal value can be enforced by an implementation.
	 * 
	 * @param mag
	 *                 the new magnification factor
	 * 
	 * @throws GeneralHelpingException
	 *                 in case that the magnification factor is not in the allowed
	 *                 range or that the magnification has been set to a different
	 *                 value earlier.
	 */
	public abstract void setMagnification(long mag) throws GeneralHelpingException;

	/**
	 * Getter for the magnification factor in permille. The default value is
	 * 1000. It can only take positive numbers as values.
	 * 
	 * @return the magnification factor
	 */
	public abstract long getMagnification();

	/**
	 * Getter for the token factory. The token factory can be used to get new
	 * tokens of some kind.
	 * 
	 * @return the token factory
	 */
	public abstract TokenFactory getTokenFactory();

	/**
	 * Getter for the tokenizer.
	 * 
	 * @return the tokenizer
	 */
	public abstract Tokenizer getTokenizer();

	/**
	 * ...
	 * 
	 * @param t
	 *                 ...
	 * 
	 * @throws GeneralException
	 *                 ...
	 */
	public abstract void afterGroup(Token t) throws GeneralException;

	/**
	 * ...
	 * 
	 * @throws GeneralException
	 *                 ...
	 */
	public abstract void closeGroup() throws GeneralException;

	/**
	 * ...
	 * 
	 * @return ...
	 * 
	 * @throws GeneralException
	 *                 ...
	 */
	public abstract boolean ifPop() throws GeneralException;

	/**
	 * ...
	 * 
	 * @param locator
	 *                 the locator of the start
	 * @param value
	 *                 the boolean value
	 */
	public abstract void ifPush(Locator locator, boolean value);

	/**
	 * This method can be used to open another group. The current group is
	 * pushed onto the stack to be reactivated when the new group will be
	 * closed.
	 * 
	 * @throws ConfigurationException
	 *                 in case of an error in the configuration, e.g. the class for
	 *                 the group can not be determined.
	 */
	public abstract void openGroup() throws ConfigurationException;

	//TODO to be completed
}
