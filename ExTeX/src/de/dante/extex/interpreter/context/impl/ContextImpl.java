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
import java.util.Stack;

import de.dante.extex.font.FontFactory;
import de.dante.extex.hyphenation.HyphenationManager;
import de.dante.extex.hyphenation.impl.HyphenationManagerImpl;
import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextFactory;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.TokenFactoryImpl;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.StringList;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.file.FileFinder;
import de.dante.util.file.FileFinderConfigImpl;
import de.dante.util.file.FileFinderDirect;
import de.dante.util.file.FileFinderList;

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
 * @version $Revision: 1.18 $
 */
public class ContextImpl implements Context, Serializable {

    private static final String TYPESETTING_CONTEXT_TAG = "TypesettingContext";
	/**
     * The field <tt>FILE_TAG</tt> ...
     */
    private static final String FILE_TAG = "File";

    /**
     * The constant <tt>DEFAULT_ATTRIBUTE</tt> ...
     */
    private static final String DEFAULT_ATTRIBUTE = "default";

	/**
	 * The constant <tt>DEFAULT_SIZE</tt> ...
	 */
	private static final String DEFAULT_SIZE = "size";

    /**
     * The constant <tt>FONT_TAG</tt> ...
     */
    private static final String FONT_TAG = "Font";

    /**
     * The constant <tt>GROUP_TAG</tt> ...
     */
    private static final String GROUP_TAG = "Group";

    /**
     * The constant <tt>MAGNIFICATION_MAX</tt> ...
     */
    private static final int MAGNIFICATION_MAX = 32768;

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> ...
     */
    private static final String CLASS_ATTRIBUTE = "class";

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
     * The magnification for the whole document in permille
     */
    private long magnification = 1000;

    /**
     * The field <tt>tcFactory</tt> ...
     */
    private TypesettingContextFactory tcFactory;

    /**
     * The field <tt>fontFactory</tt> ...
     */
    private FontFactory fontFactory;

    /**
     * Creates a new object.
     *
     * @param configuration ...
     * @throws ConfigurationException ...
     * @throws GeneralException ...
     */
    public ContextImpl(final Configuration configuration)
            throws ConfigurationException, GeneralException {
        super();
        this.config = configuration;
        groupFactory = new GroupFactory(config.getConfiguration(GROUP_TAG));
        group = groupFactory.newInstance(group);

        Configuration fontConfiguration = config.getConfiguration(FONT_TAG);
        String fontClass = fontConfiguration.getAttribute(CLASS_ATTRIBUTE);

        if (fontClass == null || fontClass.equals("")) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    fontConfiguration);
        }

        try {
            FileFinderList finder = new FileFinderList(new FileFinderDirect(
                    new StringList("", ":")));
            finder.add(new FileFinderConfigImpl(fontConfiguration
                    .getConfiguration(FILE_TAG)));
            fontFactory = (FontFactory) (Class.forName(fontClass)
                    .getConstructor(new Class[]{FileFinder.class})
                    .newInstance(new Object[]{finder}));
        } catch (Exception e) {
            throw new ConfigurationInstantiationException(e);
        }

        // default font and size
		String defaultFont = fontConfiguration.getAttribute(DEFAULT_ATTRIBUTE);
        String size = fontConfiguration.getAttribute(DEFAULT_SIZE);

        if (defaultFont == null || defaultFont.equals("")) {
            throw new ConfigurationMissingAttributeException(DEFAULT_ATTRIBUTE,
                    fontConfiguration);
        }

        Configuration typesettingConfig = config
                .getConfiguration(TYPESETTING_CONTEXT_TAG);

        if (typesettingConfig == null) {
            throw new ConfigurationMissingException(TYPESETTING_CONTEXT_TAG,
                    config.toString());
        }
        
        Dimen fontsize = null;
        try {
        	float f = Float.parseFloat(size);
        	fontsize = new Dimen((long)(Dimen.ONE * f));
        } catch (NumberFormatException e) {
			fontsize = new Dimen(Dimen.ONE * 12);
		}

        tcFactory = new TypesettingContextFactory(typesettingConfig);
        TypesettingContext typesettingContext = tcFactory.newInstance();
        typesettingContext.setFont(fontFactory.getInstance(defaultFont,fontsize));
        //typesettingContext.setLanguage(config.getValue("Language"));
        setTypesettingContext(typesettingContext);

    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setActive(java.lang.String,
     *         de.dante.extex.interpreter.Code)
     */
    public void setActive(final String name, final Code code) {
        group.setActive(name, code);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setActive(java.lang.String,
     *      de.dante.extex.interpreter.Code, boolean)
     */
    public void setActive(final String name, final Code code,
            final boolean global) {
        group.setActive(name, code, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getActive(java.lang.String)
     */
    public Code getActive(final String name) {
        return group.getActive(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCatcode(de.dante.util.UnicodeChar,
     *      de.dante.extex.scanner.Catcode)
     */
    public void setCatcode(final UnicodeChar c, final Catcode cc) {
        group.setCatcode(c, cc);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCatcode(de.dante.util.UnicodeChar,
     *      de.dante.extex.scanner.Catcode, boolean)
     */
    public void setCatcode(final UnicodeChar c, final Catcode cc,
            final boolean global) {
        group.setCatcode(c, cc, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setBox(java.lang.String,
     *         de.dante.extex.interpreter.type.Box)
     */
    public void setBox(final String name, final Box value) {
        group.setBox(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setBox(java.lang.String,
     *      de.dante.extex.interpreter.type.Box, boolean)
     */
    public void setBox(final String name, final Box value,
            final boolean global) {
        group.setBox(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getBox(java.lang.String)
     */
    public Box getBox(final String name) {
        return group.getBox(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCount(java.lang.String,
     *         long)
     */
    public void setCount(final String name, final long value) {
        Count count = new Count(value);
        group.setCount(name, count);

        //TODO: use existing Register instead of making a new one
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCount(java.lang.String,
     *         long, boolean)
     */
    public void setCount(final String name, final long value,
            final boolean global) {
        Count count = new Count(value);
        group.setCount(name, count, global);

        //TODO: use existing Register instead of making a new one
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getCount(java.lang.String)
     */
    public Count getCount(final String name) {
        return group.getCount(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
     *         long)
     */
    public void setDimen(final String name, final long value) {
        Dimen dimen = new Dimen(value);
        group.setDimen(name, dimen);

        //TODO: use existing Register instead of making a new one
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
     *         de.dante.extex.interpreter.type.Dimen)
     */
    public void setDimen(final String name, final Dimen value) {
        group.setDimen(name, value);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
     *         de.dante.extex.interpreter.type.Dimen, boolean)
     */
    public void setDimen(final String name, final Dimen value,
            final boolean global) {
        group.setDimen(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDimen(java.lang.String,
     *         long, boolean)
     */
    public void setDimen(final String name, final long value,
            final boolean global) {
        Dimen dimen = new Dimen(value);
        group.setDimen(name, dimen, global);

        //TODO: use existing Register instead of making a new one
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getDimen(java.lang.String)
     */
    public Dimen getDimen(final String name) {
        return group.getDimen(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getFontFactory()
     */
    public FontFactory getFontFactory() {
        return fontFactory;
    }
    /**
     * @see de.dante.extex.interpreter.context.Context#setFontFactory(de.dante.extex.font.FontFactory)
     */
    public void setFontFactory(final FontFactory factory) {
        this.fontFactory = factory;
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
    public HyphenationTable getHyphenationTable(final int language) {
        return hyphenationManager.getHyphenationTable(Integer
                .toString(language));
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setInteraction(de.dante.extex.interpreter.Interaction)
     */
    public void setInteraction(final Interaction interaction) {
        group.setInteraction(interaction);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setInteraction(de.dante.extex.interpreter.Interaction,
     *      boolean)
     */
    public void setInteraction(final Interaction interaction,
            final boolean global) {
        group.setInteraction(interaction, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getInteraction()
     */
    public Interaction getInteraction() {
        return group.getInteraction();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMacro(java.lang.String,
     *         de.dante.extex.interpreter.Code)
     */
    public void setMacro(final String name, final Code code) {
        group.setMacro(name, code);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMacro(java.lang.String,
     *      de.dante.extex.interpreter.Code, boolean)
     */
    public void setMacro(final String name, final Code code,
            final boolean global) {
        group.setMacro(name, code, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMacro(java.lang.String)
     */
    public Code getMacro(final String name) {
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
    public void setMagnification(final long mag) throws GeneralHelpingException {
        if (magnificationLock && this.magnification != mag) {
            throw new GeneralHelpingException("TTP.IncompatMag", Long
                    .toString(mag));
        }

        magnificationLock = true;

        long maximalMagnification;

        try {
            maximalMagnification = config
                    .getValueAsInteger("maximalMagnification",
                                        MAGNIFICATION_MAX);
        } catch (ConfigurationException e) {
            throw new GeneralHelpingException(e);
        }

        if (mag < 1 || mag > maximalMagnification) {
            throw new GeneralHelpingException("TTP.IllegalMag", Long
                    .toString(mag));
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
	 * @param context the new context to use
	 */
	public void setTypesettingContext(final TypesettingContext context) {
		group.setTypesettingContext(context);
	}

    /**
     * Setter for the typesetting context in the specified groups.
     *
     * @param context the new context to use
     * @param global if <code>true</code> then the new value is set in all
     *            groups, otherwise only in the current group.
     */
    public void setTypesettingContext(final TypesettingContext context,
            final boolean global) {
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
    public void afterGroup(final Token t) {
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
     * ...
     *
     * @return ...
     */
    public long ifPop() {
        return ((Conditional) ifStack.pop()).getValue();
    }

    /**
     * Put a boolean value onto the if stack.
     *
     * @param value
     *                 the value to push
     */
    public void ifPush(final Locator locator, final long value) {
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
    public Tokens getToks(final String name) {
        return group.getToks(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setToks(java.lang.String,
     *      de.dante.extex.interpreter.type.Tokens, boolean)
     */
    public void setToks(final String name, final Tokens toks,
            final boolean global) {
        group.setToks(name, toks, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setToks(java.lang.String,
     *      de.dante.extex.interpreter.type.Tokens)
     */
    public void setToks(final String name, final Tokens toks) {
        group.setToks(name, toks);
    }

    /**
     * Getter for group.
     *
     * @return the group.
     */
    protected Group getGroup() {
        return group;
    }
}
