/*
 * Copyright (C) 2003 Gerd Neugebauer, Michael Niedermair
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
import java.util.Stack;

import de.dante.extex.hyphenation.HyphenationManager;
import de.dante.extex.hyphenation.HyphenationManagerImpl;
import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.TokenFactoryImpl;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This is a reference implementation for an interpreter context.
 * 
 * The groups are implemented as a linked list of single groups. In contrast to
 * the Knuthian implementation in TeX no undo stack is used.
 * <p>
 * Several operations have to be dealt with:
 * </p>
 * <ul>
 * <li>For each new group a new instance of a {@link Group Group}is created
 * with the old one as next group.</li>
 * <li>If a group is closed then the next group is used as current group and
 * the formerly current group is discarted.</li>
 * <li>If a value has to be found in a group then the next chain has to be
 * traced down until the value is found. <br />An implementation variant might
 * want to insert the value found into the higher groups; all or some of them
 * to speed up the next access. This optimization is currently not implemented.
 * </li>
 * <li>If a local value has to be stored then it can be stored in the local
 * group only.</li>
 * <li>If a global value has to be stored then the group chain has to be
 * traversed and the value has to be set in all approrpiate groups: There are
 * several implementation variants
 * <ul>
 * <li>Clear the value in all groups and set it in the bottommost group.</li>
 * <li>Set the value in all groups where it has a local value.</li>
 * <li>Set teh value in all groups.</li>
 * </ul>
 * Here the third approach is used which is suspected to be a little more
 * efficient on the cost of slightly more memory consumption.</li>
 * </ul>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class ContextImpl implements Context, Serializable {

	/**
	 * The saved configuration
	 */
	private Configuration config = null;

	/**
	 * This is the entry to the linked list of groups. The current group is the
	 * first one.
	 */
	private Group group = null;

	/**
	 * The factory to acquire a new group
	 */
	private transient GroupFactory groupFactory;

	/**
	 * ...
	 */
	private HyphenationManager hyphenationManager = new HyphenationManagerImpl();

	/**
	 * The interaction mode to use
	 */
	private Interaction interaction = null;

	/**
	 * The stack for conditionals
	 */
	private Stack ifStack = new Stack();

	/**
	 * The token factory implementation to use
	 */
	private TokenFactory tokenFactory = new TokenFactoryImpl();

	/**
	 * This boolean is used to determine whether the magnification has already
	 * been set to a new value. It it is <code>true</code> then it is not
	 * desirable to change the value of <i>magnification</i>.
	 */
	private boolean magnificationLock = false;

	/**
	 * the magnification for the whole document in permille
	 */
	private long magnification = 1000;

	/**
	 * Creates a new object.
	 */
	public ContextImpl() {
		super();
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setActive(java.lang.String,
	 *         de.dante.extex.interpreter.Code)
	 */
	public void setActive(String name, Code code) {
		group.setActive(name, code);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setActive(java.lang.String,
	 *         de.dante.extex.interpreter.Code, boolean)
	 */
	public void setActive(String name, Code code, boolean global) {
		group.setActive(name, code, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getActive(java.lang.String)
	 */
	public Code getActive(String name) {
		return group.getActive(name);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setCatcode(char,
	 *         de.dante.extex.scanner.Catcode)
	 */
	public void setCatcode(char c, Catcode cc) throws GeneralHelpingException {
		group.setCatcode(c, cc);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setCatcode(java.lang.String,
	 *         de.dante.extex.scanner.Catcode, boolean)
	 */
	public void setCatcode(char c, Catcode cc, boolean global) throws GeneralHelpingException {
		group.setCatcode(c, cc, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setCount(java.lang.String,
	 *         long)
	 */
	public void setCount(String name, long value) {
		Count count = new Count(value);
		group.setCount(name, count);

		//TODO: use existing Register instead of making a new one
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setCount(java.lang.String,
	 *         long, boolean)
	 */
	public void setCount(String name, long value, boolean global) {
		Count count = new Count(value);
		group.setCount(name, count, global);

		//TODO: use existing Register instead of making a new one
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getCount(java.lang.String)
	 */
	public Count getCount(String name) {
		return group.getCount(name);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
	 *         long)
	 */
	public void setDimen(String name, long value) {
		Dimen dimen = new Dimen(value);
		group.setDimen(name, dimen);

		//TODO: use existing Register instead of making a new one
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
	 *         de.dante.extex.interpreter.type.Dimen)
	 */
	public void setDimen(String name, Dimen value) {
		group.setDimen(name, value);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
	 *         de.dante.extex.interpreter.type.Dimen, boolean)
	 */
	public void setDimen(String name, Dimen value, boolean global) {
		group.setDimen(name, value, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
	 *         long, boolean)
	 */
	public void setDimen(String name, long value, boolean global) {
		Dimen dimen = new Dimen(value);
		group.setDimen(name, dimen, global);

		//TODO: use existing Register instead of making a new one
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getDimen(java.lang.String)
	 */
	public Dimen getDimen(String name) {
		return group.getDimen(name);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#isGlobalGroup()
	 */
	public boolean isGlobalGroup() {
		return (group.getNext() == null);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getHyphenation(int)
	 */
	public HyphenationTable getHyphenationTable(int language) throws GeneralException {
		return hyphenationManager.getHyphenationTable(Integer.toString(language));
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setInteraction(de.dante.extex.interpreter.Interaction)
	 */
	public void setInteraction(Interaction interaction) {
		group.setInteraction(interaction);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setInteraction(de.dante.extex.interpreter.Interaction,
	 *         boolean)
	 */
	public void setInteraction(Interaction interaction, boolean global) {
		group.setInteraction(interaction, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getInteraction()
	 */
	public Interaction getInteraction() {
		return interaction;
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setMacro(java.lang.String,
	 *         de.dante.extex.interpreter.Code)
	 */
	public void setMacro(String name, Code code) {
		group.setMacro(name, code);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setMacro(java.lang.String,
	 *         de.dante.extex.interpreter.Code, boolean)
	 */
	public void setMacro(String name, Code code, boolean global) {
		group.setMacro(name, code, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getMacro(java.lang.String)
	 */
	public Code getMacro(String name) {
		return group.getMacro(name);
	}

	/**
	 * Setter for the magnification. The magnification is a global value which
	 * can be assigned at most once. It contains the magnification factor in
	 * permille. The default value is 1000. It can only take positive numbers
	 * as values. The maximal value is taken from the configuration option <tt>maximalMaginification</tt>.
	 * The default value for the maximal magnification is 32768.
	 * 
	 * @see de.dante.extex.interpreter.context.Context#setMagnification(long)
	 */
	public void setMagnification(long mag) throws GeneralHelpingException {
		if (magnificationLock && this.magnification != mag) {
			throw new GeneralHelpingException("TTP.IncompatMag", Long.toString(mag));
		}

		magnificationLock = true;

		long maximalMagnification;

		try {
			maximalMagnification = config.getValueAsInteger("maximalMagnification", 32768);
		} catch (ConfigurationException e) {
			throw new GeneralHelpingException(e);
		}

		if (mag < 1 || mag > maximalMagnification) {
			throw new GeneralHelpingException("TTP.IllegalMag", Long.toString(mag));
		}

		magnification = mag;
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getMagnification()
	 */
	public long getMagnification() {
		return magnification;
	}

	/**
	 * Getter for the token factory.
	 * 
	 * @return the token factory
	 */
	public TokenFactory getTokenFactory() {
		return tokenFactory;
	}

	/**
	 * Getter for the tokenizer.
	 * 
	 * @return the tokenizer
	 */
	public Tokenizer getTokenizer() {
		return (Tokenizer) group;
	}

	/**
	 * Setter for the typesetting context in the current group.
	 * 
	 * @param context
	 *                 the new context to use
	 */
	public void setTypesettingContext(TypesettingContext context) {
		group.setTypesettingContext(context);
	}

	/**
	 * Setter for the typesetting context in the specified groups.
	 * 
	 * @param context
	 *                 the new context to use
	 * @param global
	 *                 if <code>true</code> then the new value is set in all
	 *                 groups, otherwise only in the current group.
	 */
	public void setTypesettingContext(TypesettingContext context, boolean global) {
		group.setTypesettingContext(context, global);
	}

	/**
	 * Getter for the typesetting context.
	 * 
	 * @return the typesetting context
	 */
	public TypesettingContext getTypesettingContext() {
		return group.getTypesettingContext();
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#afterGroup(de.dante.extex.scanner.Token)
	 */
	public void afterGroup(Token t) throws GeneralException {
		group.afterGroup(t);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#closeGroup()
	 */
	public void closeGroup() throws GeneralException {
		Group next = group.getNext();

		if (next == null) {
			throw new GeneralHelpingException("TTP.TooManyRightBraces");
		} else {
			Tokens toks = group.getAfterGroup();
			group = next;

			if (toks != null) {
				//TODO execute aftergroup
			}
		}
	}

	/**
	 * @see de.dante.util.configuration.Configurable#configure(de.dante.util.configuration.Configuration)
	 */
	public void configure(Configuration config) throws ConfigurationException {
		this.config = config;
		groupFactory = new GroupFactory(config.getConfiguration("Group"));
		group = groupFactory.newInstance(group);
	}

	/**
	 * ...
	 * 
	 * @return ...
	 */
	public boolean ifPop() {
		return ((Conditional) ifStack.pop()).isValue();
	}

	/**
	 * Put a boolean value onto the if stack.
	 * 
	 * @param value
	 *                 the value to push
	 */
	public void ifPush(Locator locator, boolean value) {
		ifStack.add(new Conditional(locator, value));
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#openGroup()
	 */
	public void openGroup() throws ConfigurationException {
		group = groupFactory.newInstance(group);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#getToks(java.lang.String)
	 */
	public Tokens getToks(String name) {
		return group.getToks(name);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setToks(java.lang.String,
	 *         de.dante.extex.interpreter.type.Tokens, boolean)
	 */
	public void setToks(String name, Tokens toks, boolean global) {
		group.setToks(name, toks, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setToks(java.lang.String,
	 *         de.dante.extex.interpreter.type.Tokens)
	 */
	public void setToks(String name, Tokens toks) {
		group.setToks(name, toks);
	}
	
	
	
	/**
	 * @see de.dante.extex.interpreter.context.Context#getReal(java.lang.String)
	 */
	public Real getReal(String name) {
		return group.getReal(name);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setReal(java.lang.String, de.dante.extex.interpreter.type.Real, boolean)
	 */
	public void setReal(String name, Real value, boolean global) {
		group.setReal(name, value, global);
	}

	/**
	 * @see de.dante.extex.interpreter.context.Context#setReal(java.lang.String, de.dante.extex.interpreter.type.Real)
	 */
	public void setReal(String name, Real value) {
		group.setReal(name, value);
	}

}
