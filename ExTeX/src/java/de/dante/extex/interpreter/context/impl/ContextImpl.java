/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.font.FontFactory;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.ConditionalSwitch;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.ContextInternals;
import de.dante.extex.interpreter.context.group.GroupInfo;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.context.observer.code.CodeObservable;
import de.dante.extex.interpreter.context.observer.code.CodeObserver;
import de.dante.extex.interpreter.context.observer.conditional.ConditionalObservable;
import de.dante.extex.interpreter.context.observer.conditional.ConditionalObserver;
import de.dante.extex.interpreter.context.observer.count.CountObservable;
import de.dante.extex.interpreter.context.observer.count.CountObserver;
import de.dante.extex.interpreter.context.observer.dimen.DimenObservable;
import de.dante.extex.interpreter.context.observer.dimen.DimenObserver;
import de.dante.extex.interpreter.context.observer.glue.GlueObservable;
import de.dante.extex.interpreter.context.observer.glue.GlueObserver;
import de.dante.extex.interpreter.context.observer.group.AfterGroupObserver;
import de.dante.extex.interpreter.context.observer.group.GroupObservable;
import de.dante.extex.interpreter.context.observer.group.GroupObserver;
import de.dante.extex.interpreter.context.observer.interaction.InteractionObservable;
import de.dante.extex.interpreter.context.observer.interaction.InteractionObserver;
import de.dante.extex.interpreter.context.observer.load.LoadedObservable;
import de.dante.extex.interpreter.context.observer.load.LoadedObserver;
import de.dante.extex.interpreter.context.observer.tokens.TokensObservable;
import de.dante.extex.interpreter.context.observer.tokens.TokensObserver;
import de.dante.extex.interpreter.context.tc.Direction;
import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.context.tc.TypesettingContextFactory;
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
import de.dante.extex.interpreter.type.math.MathCode;
import de.dante.extex.interpreter.type.math.MathDelimiter;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.tokens.FixedTokens;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.interpreter.unit.UnitInfo;
import de.dante.extex.language.Language;
import de.dante.extex.language.LanguageManager;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingException;
import de.dante.util.framework.configuration.exception.ConfigurationWrapperException;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a reference implementation for an interpreter context.
 *
 * The groups are implemented as a linked list of single groups. In contrast to
 * the Knuthian implementation in <logo>TeX</logo> no undo stack is used.
 * <p>
 * Several operations have to be dealt with:
 * </p>
 * <ul>
 * <li>For each new group a new instance of a
 *  {@link de.dante.extex.interpreter.context.impl.Group Group} is created
 *  with the old one as next group.</li>
 * <li>If a group is closed then the next group is used as current group and
 *  the formerly current group is discarded.</li>
 * <li>If a value has to be found in a group then the next chain has to be
 *  traced down until the value is found. <br />An implementation variant might
 *  want to insert the value found into the higher groups; all or some of them
 *  to speed up the next access. This optimization is currently not implemented.
 * </li>
 * <li>If a local value has to be stored then it can be stored in the local
 *  group only.</li>
 * <li>If a global value has to be stored then the group chain has to be
 *  traversed and the value has to be set in all appropriate groups: There are
 *  several implementation variants
 * <ul>
 * <li>Clear the value in all groups and set it in the bottommost group.</li>
 * <li>Set the value in all groups where it has a local value.</li>
 * <li>Set the value in all groups.</li>
 * </ul>
 * Here the third approach is used which is suspected to be a little more
 * efficient on the cost of slightly more memory consumption.</li>
 * </ul>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.121 $
 */
public class ContextImpl
        implements
            ContextInternals,
            CodeObservable,
            ConditionalObservable,
            CountObservable,
            DimenObservable,
            GlueObservable,
            GroupObservable,
            InteractionObservable,
            LoadedObservable,
            TokensObservable,
            Tokenizer,
            DocumentWriterOptions,
            TypesetterOptions,
            TokenStreamOptions,
            Localizable,
            LogEnabled,
            Configurable,
            Serializable {

    /**
     * The field <tt>bottommarks</tt> contains the bottom marks.
     */
    private static Map bottommarks = new HashMap();

    /**
     * The field <tt>firstmarks</tt> contains the first marks.
     */
    private static Map firstmarks = new HashMap();

    /**
     * The constant <tt>GROUP_TAG</tt> contains the name of the tag for the
     * sub-configuration for the group factory.
     */
    private static final String GROUP_TAG = "Group";

    /**
     * The constant <tt>MAGNIFICATION_MAX</tt> contains the maximal allowed
     * magnification value. This is the fallback value which can be changed in
     * the configuration.
     */
    private static final long MAGNIFICATION_MAX = 0x8000;

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060512L;

    /**
     * The field <tt>topmarks</tt> contains the top marks.
     */
    private static Map topmarks = new HashMap();

    /**
     * The constant <tt>TYPESETTING_CONTEXT_TAG</tt> contains the name of the
     * configuration tag for the typesetting context.
     */
    private static final String TYPESETTING_CONTEXT_TAG = "TypesettingContext";

    /**
     * The field <tt>afterassignment</tt> contains the token to be inserted
     * after an assignment is completed or <code>null</code>.
     */
    private Token afterassignment = null;

    /**
     * The field <tt>changeCodeObservers</tt> contains the list of observers
     * registered for change event on the code.
     */
    private transient Map changeCodeObservers;

    /**
     * The field <tt>changeCountObservers</tt> contains the list of observers
     * registered for change event on the count registers.
     */
    private transient Map changeCountObservers;

    /**
     * The field <tt>changeDimenObservers</tt> contains the list of observers
     * registered for change event on the dimen registers.
     */
    private transient Map changeDimenObservers;

    /**
     * The field <tt>dimenChangeObservers</tt> contains the list of observers
     * registered for change event on the glue (skip) registers.
     */
    private transient Map changeGlueObservers;

    /**
     * The field <tt>observersInteraction</tt> contains the observer list which
     * is used for the observers registered to receive notifications
     * when the interaction is changed. The argument is the new interaction
     * mode.
     */
    private transient List changeInteractionObservers;

    /**
     * The field <tt>toksChangeObservers</tt> contains the list of observers
     * registered for change event on the toks registers.
     */
    private transient Map changeToksObservers;

    /**
     * The field <tt>conditionalObservers</tt> contains the observer for
     * conditionals. The value <code>null</code> is treated like the empty list.
     */
    private transient List conditionalObservers = null;

    /**
     * The field <tt>conditionalStack</tt> contains the stack for conditionals.
     */
    private List conditionalStack = new ArrayList();

    /**
     * The field <tt>dirStack</tt> contains the stack of directions.
     */
    private Stack dirStack = new Stack();

    /**
     * The field <tt>errorCount</tt> contains the error counter.
     */
    private int errorCount = 0;

    /**
     * The field <tt>fontFactory</tt> contains the font factory to use.
     */
    private transient FontFactory fontFactory;

    /**
     * The field <tt>group</tt> contains the entry to the linked list of groups.
     * The current group is the first one.
     */
    private Group group = null;

    /**
     * The field <tt>groupFactory</tt> contains the factory to acquire
     * a new group.
     */
    private transient GroupFactory groupFactory;

    /**
     * The field <tt>groupObservers</tt> contains the list of observers
     * registered for change event on groups.
     */
    private transient List groupObservers;

    /**
     * The field <tt>id</tt> contains the is string.
     * The id string is the classification of the
     * original source as given in the fmt file. The id string can be
     * <code>null</code> if not known yet.
     */
    private String id = null;

    /**
     * The field <tt>interaction</tt> contains the currently active
     * interaction mode.
     */
    private Interaction interaction = Interaction.ERRORSTOPMODE;

    /**
     * The field <tt>languageManager</tt> contains the language manager.
     */
    private LanguageManager languageManager;

    /**
     * The field <tt>loadObserver</tt> contains the list of observers for the
     * load event.
     * Note that this list is stored within the format. Thus it is <i>not</i>
     * <tt>transient</tt>.
     */
    private List loadObservers = null;

    /**
     * The field <tt>localizer</tt> contains the localizer to use.
     */
    private transient Localizer localizer = null;

    /**
     * The field <tt>logger</tt> contains the logger.
     */
    private transient Logger logger = null;

    /**
     * The field <tt>magnification</tt> contains the magnification for the
     * whole document in permille. The value is always greater than 0 and
     * less or equal to <tt>magnificationMax</tt>.
     */
    private long magnification = Math.min(1000, MAGNIFICATION_MAX);

    /**
     * The field <tt>magnificationLock</tt> is used to determine whether the
     * magnification has already been set to a new value.
     * It it is <code>true</code> then it is not
     * desirable to change the value of <i>magnification</i>.
     */
    private boolean magnificationLock = false;

    /**
     * The field <tt>magnificationMax</tt> contains the maximal allowed
     * magnification value. This is initialized to MAGNIFICATION_MAX and
     * may be overwritten from within the configuration.
     */
    private long magnificationMax = MAGNIFICATION_MAX;

    /**
     * The field <tt>parshape</tt> contains the object containing the
     * dimensions of the paragraph.
     */
    private ParagraphShape parshape = null;

    /**
     * The field <tt>splitBottomMarks</tt> contains the split bottom marks.
     */
    private Map splitBottomMarks = new Hashtable();

    /**
     * The field <tt>splitFirstMarks</tt> contains the split first marks.
     */
    private Map splitFirstMarks = new Hashtable();

    /**
     * The field <tt>standardTokenStream</tt> contains the standard token
     * stream. This token stream usually is fed by the user.
     */
    private transient TokenStream standardTokenStream = null;

    /**
     * The field <tt>tokenFactory</tt> contains the token factory implementation
     * to use.
     */
    private transient TokenFactory tokenFactory;

    /**
     * The field <tt>tcFactory</tt> contains the factory to acquire new
     * instances of a TypesettingContext.
     */
    private transient TypesettingContextFactory typesettingContextFactory;

    /**
     * The field <tt>units</tt> contains the list of unit infos.
     */
    private List units = new ArrayList();

    /**
     * Creates a new object.
     */
    public ContextImpl() {

        super();
        init();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#addUnit(
     *      de.dante.extex.interpreter.unit.UnitInfo)
     */
    public void addUnit(final UnitInfo info) {

        if (info == null) {
            throw new IllegalArgumentException("addUnit()");
        }
        units.add(info);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#afterGroup(
     *      AfterGroupObserver)
     */
    public void afterGroup(final AfterGroupObserver observer) {

        group.afterGroup(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#afterGroup(
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void afterGroup(final Token t) {

        group.afterGroup(t);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#clearSplitMarks()
     */
    public void clearSplitMarks() {

        splitFirstMarks.clear();
        splitBottomMarks.clear();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#closeGroup(
     *      de.dante.extex.typesetter.Typesetter,
     *     de.dante.extex.interpreter.TokenSource)
     */
    public void closeGroup(final Typesetter typesetter, final TokenSource source)
            throws InterpreterException {

        Group next = group.getNext();

        if (next == null) {
            throw new HelpingException(localizer, "TTP.TooManyRightBraces");
        }

        group.runAfterGroup();

        Tokens toks = group.getAfterGroup();
        group = next;

        if (toks != null) {
            source.push(toks);
        }

        if (groupObservers != null) {
            int size = groupObservers.size();
            for (int i = 0; i < size; i++) {
                try {
                    ((GroupObserver) groupObservers.get(i))
                            .receiveCloseGroup(this);
                } catch (InterpreterException e) {
                    throw e;
                } catch (Exception e) {
                    throw new InterpreterException(e);
                }
            }
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration configuration)
            throws ConfigurationException {

        magnificationMax = configuration.getValueAsInteger(
                "maximalMagnification", (int) MAGNIFICATION_MAX);

        groupFactory = new GroupFactory(configuration
                .getConfiguration(GROUP_TAG));
        if (group == null) {
            try {
                openGroup(GroupType.BOTTOM_LEVEL_GROUP, null, null);
            } catch (InterpreterException e) {
                throw new ConfigurationWrapperException(e);
            }
        }

        Configuration typesettingConfig = configuration
                .getConfiguration(TYPESETTING_CONTEXT_TAG);

        if (typesettingConfig == null) {
            throw new ConfigurationMissingException(TYPESETTING_CONTEXT_TAG,
                    configuration.toString());
        }
        if (typesettingContextFactory == null) {
            typesettingContextFactory = new TypesettingContextFactory();
        }
        typesettingContextFactory.configure(typesettingConfig);
        TypesettingContext tc;
        TypesettingContext oldTc = getTypesettingContext();

        if (languageManager != null) {
            typesettingContextFactory.setLanguageManager(languageManager);
            tc = typesettingContextFactory.initial();
            tc = typesettingContextFactory.newInstance(tc, languageManager
                    .getLanguage("0"));
        } else {
            tc = typesettingContextFactory.initial();
        }
        if (oldTc != null) {
            tc = typesettingContextFactory.newInstance(oldTc);
        }
        set(tc, true);
    }

    /**
     * Setter for the localizer.
     *
     * @param theLocalizer the localizer to use
     *
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {

        this.localizer = theLocalizer;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#esc(java.lang.String)
     */
    public String esc(final String name) {

        UnicodeChar escapechar = escapechar();
        return (escapechar != null ? escapechar.toString() + name : name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#esc(
     *      de.dante.extex.scanner.type.token.Token)
     */
    public String esc(final Token token) {

        return token.toText(escapechar());
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#escapechar()
     */
    public UnicodeChar escapechar() {

        long esc = getCount("escapechar").getValue();

        return (esc >= 0 ? UnicodeChar.get((int) esc) : null);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#get(
     *      java.lang.Object,
     *      java.lang.Object)
     */
    public Object get(final Object extension, final Object key) {

        return group.get(extension, key);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getAfterassignment()
     */
    public Token getAfterassignment() {

        return afterassignment;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getBottomMark(
     *      java.lang.Object)
     */
    public Tokens getBottomMark(final Object name) {

        Tokens mark = (Tokens) bottommarks.get(name);
        if (mark == null) {
            mark = (Tokens) firstmarks.get(name);
            if (mark == null) {
                mark = (Tokens) topmarks.get(name);
            }
        }
        return mark;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getBox(java.lang.String)
     */
    public Box getBox(final String name) {

        return group.getBox(name);
    }

    /**
     * Get the {@link Catcode Catcode} for a given Unicode character.
     *
     * @param uc the Unicode character to get the catcode for.
     *
     * @return the catcode for the character
     *
     * @see de.dante.extex.interpreter.Tokenizer#getCatcode(de.dante.util.UnicodeChar)
     */
    public Catcode getCatcode(final UnicodeChar uc) {

        return group.getCatcode(uc);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#getCode(
     *      de.dante.extex.scanner.type.token.CodeToken)
     */
    public Code getCode(final CodeToken t) throws InterpreterException {

        return group.getCode(t);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getConditional()
     */
    public Conditional getConditional() {

        int size = conditionalStack.size();
        if (size <= 0) {
            return null;
        }
        return ((Conditional) conditionalStack.get(size - 1));
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getCount(
     *      java.lang.String)
     */
    public Count getCount(final String name) {

        return group.getCount(name);
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getCountOption(java.lang.String)
     */
    public FixedCount getCountOption(final String name) {

        return group.getCount(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getDelcode(
     *      de.dante.util.UnicodeChar)
     */
    public MathDelimiter getDelcode(final UnicodeChar c) {

        return group.getDelcode(c);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getDimen(
     *      java.lang.String)
     */
    public Dimen getDimen(final String name) {

        return group.getDimen(name);
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getDimenOption(java.lang.String)
     */
    public FixedDimen getDimenOption(final String name) {

        return group.getDimen(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextErrorCount#getErrorCount()
     */
    public int getErrorCount() {

        return errorCount;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getFirstMark(
     *      java.lang.Object)
     */
    public Tokens getFirstMark(final Object name) {

        Tokens mark = (Tokens) firstmarks.get(name);
        if (mark == null) {
            mark = (Tokens) topmarks.get(name);
        }
        return mark;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getFont(java.lang.String)
     */
    public Font getFont(final String name) {

        return this.group.getFont(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getFontFactory()
     */
    public FontFactory getFontFactory() {

        return fontFactory;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getGlue(java.lang.String)
     */
    public Glue getGlue(final String name) {

        return group.getSkip(name);
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#getGlueOption(java.lang.String)
     */
    public FixedGlue getGlueOption(final String name) {

        return group.getSkip(name);
    }

    /**
     * Getter for group.
     *
     * @return the group.
     */
    protected Group getGroup() {

        return group;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#getGroupInfos()
     */
    public GroupInfo[] getGroupInfos() {

        int level = (int) group.getLevel() + 1;
        GroupInfoImpl[] gi = new GroupInfoImpl[level];

        Group g = group;
        while (level-- > 0) {
            gi[level] = new GroupInfoImpl(g.getLocator(), g.getType(), g
                    .getStart());
            g = g.getNext();
        }

        return gi;
    }

    /**
     * Getter for the group level. The group level is the number of groups which
     * are currently open. Thus this number of groups can be closed.
     *
     * @return the group level
     *
     * @see de.dante.extex.interpreter.context.Context#getGroupLevel()
     */
    public long getGroupLevel() {

        return group.getLevel();
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#getGroupType()
     */
    public GroupType getGroupType() {

        return group.getType();
    }

    /**
     * Getter for the id string. The id string is the classification of the
     * original source as given in the format file. The id string can be
     * <code>null</code> if not known yet.
     *
     * @return the id string
     *
     * @see de.dante.extex.interpreter.context.Context#getId()
     */
    public String getId() {

        return id;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getIfLevel()
     */
    public long getIfLevel() {

        return conditionalStack.size();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getInFile(
     *      java.lang.String)
     */
    public InFile getInFile(final String name) {

        return group.getInFile(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getInteraction()
     */
    public Interaction getInteraction() {

        return (interaction != null ? interaction : Interaction.ERRORSTOPMODE);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getLanguage(String)
     */
    public Language getLanguage(final String language)
            throws InterpreterException {

        try {
            return languageManager.getLanguage(language);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getLanguageManager()
     */
    public LanguageManager getLanguageManager() {

        return languageManager;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getLccode(
     *      de.dante.util.UnicodeChar)
     */
    public UnicodeChar getLccode(final UnicodeChar uc) {

        return group.getLccode(uc);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMagnification()
     */
    public long getMagnification() {

        return magnification;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMathcode(
     *      de.dante.util.UnicodeChar)
     */
    public MathCode getMathcode(final UnicodeChar c) {

        return group.getMathcode(c);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getMuskip(
     *      java.lang.String)
     */
    public Muskip getMuskip(final String name) {

        return group.getMuskip(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getNamespace()
     */
    public String getNamespace() {

        return group.getNamespace();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getOutFile(
     *      java.lang.String)
     */
    public OutFile getOutFile(final String name) {

        return group.getOutFile(name);
    }

    /**
     * Getter for the parshape.
     * The parshape is a feature of the context which does not interact with
     * the grouping mechanism.
     *
     * @see de.dante.extex.interpreter.context.Context#getParshape()
     */
    public ParagraphShape getParshape() {

        return this.parshape;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getSfcode(
     *      de.dante.util.UnicodeChar)
     */
    public Count getSfcode(final UnicodeChar c) {

        return group.getSfcode(c);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getSplitBottomMark(
     *      java.lang.Object)
     */
    public Tokens getSplitBottomMark(final Object name) {

        return (Tokens) splitBottomMarks.get(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getSplitFirstMark(
     *      java.lang.Object)
     */
    public Tokens getSplitFirstMark(final Object name) {

        return (Tokens) splitFirstMarks.get(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getStandardTokenStream()
     */
    public TokenStream getStandardTokenStream() {

        return group.getStandardTokenStream();
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
     * @see de.dante.extex.backend.documentWriter.DocumentWriterOptions#getTokensOption(java.lang.String)
     */
    public Tokens getTokensOption(final String name) {

        return group.getToks(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getToks(java.lang.String)
     */
    public Tokens getToks(final String name) {

        return group.getToks(name);
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStreamOptions#getToksOption(
     *      java.lang.String)
     */
    public FixedTokens getToksOption(final String name) {

        return group.getToks(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getToksOrNull(java.lang.String)
     */
    public Tokens getToksOrNull(final String name) {

        return group.getToksOrNull(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#getTopMark(
     *      java.lang.Object)
     */
    public Tokens getTopMark(final Object name) {

        return (Tokens) topmarks.get(name);
    }

    /**
     * Getter for the typesetting context.
     *
     * @return the typesetting context
     *
     * @see de.dante.extex.typesetter.TypesetterOptions#getTypesettingContext()
     */
    public TypesettingContext getTypesettingContext() {

        return group.getTypesettingContext();
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextInternals#getTypesettingContextFactory()
     */
    public TypesettingContextFactory getTypesettingContextFactory() {

        return typesettingContextFactory;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getUccode(
     *      de.dante.util.UnicodeChar)
     */
    public UnicodeChar getUccode(final UnicodeChar lc) {

        return group.getUccode(lc);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextErrorCount#incrementErrorCount()
     */
    public int incrementErrorCount() {

        return ++errorCount;
    }

    /**
     * Initialize the internal state of the instance.
     */
    private void init() {

        changeCodeObservers = new HashMap();
        changeCountObservers = new HashMap();
        changeDimenObservers = new HashMap();
        changeGlueObservers = new HashMap();
        changeToksObservers = new HashMap();
        changeInteractionObservers = new ArrayList();
        groupObservers = null;

        LanguageObserver languageObserver = new LanguageObserver();
        registerCountObserver("language", languageObserver);
        registerTokensObserver("lang", languageObserver);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#isGlobalGroup()
     */
    public boolean isGlobalGroup() {

        return (group.getNext() == null);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextGroup#openGroup(
     *      de.dante.extex.interpreter.context.group.GroupType,
     *      de.dante.util.Locator,
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void openGroup(final GroupType type, final Locator locator,
            final Token start)
            throws ConfigurationException,
                InterpreterException {

        group = groupFactory.newInstance(group, locator, start, type);
        group.setStandardTokenStream(standardTokenStream);
        if (groupObservers != null) {
            int size = groupObservers.size();
            for (int i = 0; i < size; i++) {
                try {
                    ((GroupObserver) groupObservers.get(i))
                            .receiveOpenGroup(this);
                } catch (InterpreterException e) {
                    throw e;
                } catch (Exception e) {
                    throw new InterpreterException(e);
                }
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#popConditional()
     */
    public Conditional popConditional() {

        int len = conditionalStack.size();
        if (len <= 0) {
            return null;
        }
        Conditional cond = ((Conditional) conditionalStack.remove(len - 1));

        if (conditionalObservers != null) {
            int size = conditionalObservers.size();
            for (int i = 0; i < size; i++) {
                try {
                    ((ConditionalObserver) conditionalObservers.get(i))
                            .receiveEndConditional(this, cond);
                } catch (Exception e) {
                    logger.log(Level.INFO, "", e);
                }
            }
        }
        return cond;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#popDirection()
     */
    public Direction popDirection() {

        return (Direction) (dirStack.isEmpty() ? null : dirStack.pop());
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#pushConditional(
     *      de.dante.util.Locator,
     *      boolean,
     *      de.dante.extex.interpreter.type.Code,
     *      long,
     *      boolean)
     */
    public void pushConditional(final Locator locator,
            final boolean isIfThenElse, final Code primitive,
            final long branch, final boolean neg) {

        Conditional cond = isIfThenElse
                //
                ? new Conditional(locator, primitive, branch, neg)
                : new ConditionalSwitch(locator, primitive, branch, neg);
        conditionalStack.add(cond);

        if (conditionalObservers != null) {
            int size = conditionalObservers.size();
            for (int i = 0; i < size; i++) {
                try {
                    ((ConditionalObserver) conditionalObservers.get(i))
                            .receiveStartConditional(this, cond);
                } catch (Exception e) {
                    logger.log(Level.INFO, "", e);
                }
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#pushDirection(de.dante.extex.interpreter.context.Direction)
     */
    public void pushDirection(final Direction dir) {

        dirStack.push(dir);
    }

    /**
     * This method maps instances to their normal representations if
     * required. It is used during the deserialization.
     *
     * @return the normalized object
     *
     * @throws ObjectStreamException in case of an error
     */
    public Object readResolve() throws ObjectStreamException {

        Registrar.reconnect(this);
        init();
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.load.LoadedObservable#receiveLoad(
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void receiveLoad(final TokenSource source)
            throws InterpreterException {

        if (loadObservers != null) {
            for (int i = 0; i < loadObservers.size(); i++) {
                ((LoadedObserver) loadObservers.get(i)).receiveLoaded(this,
                        source);
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.code.CodeObservable#registerCodeChangeObserver(
     *      de.dante.extex.scanner.type.token.Token,
     *      de.dante.extex.interpreter.context.observer.code.CodeObserver)
     */
    public synchronized void registerCodeChangeObserver(final Token name,
            final CodeObserver observer) {

        List observerList = (List) changeCodeObservers.get(name);
        if (null == observerList) {
            observerList = new ArrayList();
            changeCodeObservers.put(name, observerList);
        }
        observerList.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.conditional.ConditionalObservable#registerConditionalObserver(
     *      de.dante.extex.interpreter.context.observer.conditional.ConditionalObserver)
     */
    public synchronized void registerConditionalObserver(
            final ConditionalObserver observer) {

        if (conditionalObservers == null) {
            conditionalObservers = new ArrayList();
        }
        conditionalObservers.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.count.CountObservable#registerCountObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.count.CountObserver)
     */
    public synchronized void registerCountObserver(final String name,
            final CountObserver observer) {

        List list = (List) changeCountObservers.get(name);
        if (list == null) {
            list = new ArrayList();
            changeCountObservers.put(name, list);
        }
        list.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.dimen.DimenObservable#registerDimenObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.dimen.DimenObserver)
     */
    public synchronized void registerDimenObserver(final String name,
            final DimenObserver observer) {

        List list = (List) changeDimenObservers.get(name);
        if (list == null) {
            list = new ArrayList();
            changeDimenObservers.put(name, list);
        }
        list.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.glue.GlueObservable#registerGlueObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.glue.GlueObserver)
     */
    public synchronized void registerGlueObserver(final String name,
            final GlueObserver observer) {

        List list = (List) changeGlueObservers.get(name);
        if (list == null) {
            list = new ArrayList();
            changeGlueObservers.put(name, list);
        }
        list.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.group.GroupObservable#registerGroupObserver(
     *      de.dante.extex.interpreter.context.observer.group.GroupObserver)
     */
    public synchronized void registerGroupObserver(final GroupObserver observer) {

        if (groupObservers == null) {
            groupObservers = new ArrayList();
        }
        groupObservers.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextInteraction#registerInteractionObserver(
     *      de.dante.extex.interpreter.context.observer.InteractionObserver)
     */
    public synchronized void registerInteractionObserver(
            final InteractionObserver observer) {

        changeInteractionObservers.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.load.LoadedObservable#registerLoadObserver(
     *      de.dante.extex.interpreter.context.observer.load.LoadedObserver)
     */
    public void registerLoadObserver(final LoadedObserver observer) {

        if (loadObservers == null) {
            loadObservers = new ArrayList();
        }
        loadObservers.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.tokens.TokensObservable#registerTokensObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.tokens.TokensObserver)
     */
    public synchronized void registerTokensObserver(final String name,
            final TokensObserver observer) {

        List list = (List) changeToksObservers.get(name);
        if (list == null) {
            list = new ArrayList();
            changeToksObservers.put(name, list);
        }
        list.add(observer);

    }

    /**
     * This method is able to invoke all observers for a code change event.
     *
     * @param t the code token to change the binding for
     * @param code the new code binding
     * @param observerList the list of observers
     *
     * @throws InterpreterException in case of a problem in an observer
     */
    private void runCodeObservers(final CodeToken t, final Code code,
            final List observerList) throws InterpreterException {

        try {
            int len = observerList.size();
            for (int i = 0; i < len; i++) {
                ((CodeObserver) observerList.get(i)).receiveCodeChange(this, t,
                        code);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * This method is able to invoke all observers for a count change event.
     *
     * @param name the name of the count register
     * @param count the new value
     * @param observerList the list of observers
     *
     * @throws InterpreterException in case of a problem in an observer
     */
    private void runCountObservers(final String name, final Count count,
            final List observerList) throws InterpreterException {

        try {
            int len = observerList.size();
            for (int i = 0; i < len; i++) {
                ((CountObserver) observerList.get(i)).receiveCountChange(this,
                        name, count);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * This method is able to invoke all observers for a dimen change event.
     *
     * @param name the name of the count register
     * @param dimen the new value
     * @param observerList the list of observers
     *
     * @throws InterpreterException in case of a problem in an observer
     */
    private void runDimenObservers(final String name, final Dimen dimen,
            final List observerList) throws InterpreterException {

        try {
            int len = observerList.size();
            for (int i = 0; i < len; i++) {
                ((DimenObserver) observerList.get(i)).receiveDimenChange(this,
                        name, dimen);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * This method is able to invoke all observers for a glue change event.
     *
     * @param name the name of the count register
     * @param glue the new value
     * @param observerList the list of observers
     *
     * @throws InterpreterException in case of a problem in an observer
     */
    private void runGlueObservers(final String name, final Glue glue,
            final List observerList) throws InterpreterException {

        try {
            int len = observerList.size();
            for (int i = 0; i < len; i++) {
                ((GlueObserver) observerList.get(i)).receiveGlueChange(this,
                        name, glue);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * This method is able to invoke all observers for a toks change event.
     *
     * @param name the name of the count register
     * @param toks the new value
     * @param observerList the list of observers
     *
     * @throws InterpreterException in case of a problem in an observer
     */
    private void runTokensObservers(final String name, final Tokens toks,
            final List observerList) throws InterpreterException {

        try {
            int len = observerList.size();
            for (int i = 0; i < len; i++) {
                ((TokensObserver) observerList.get(i)).receiveTokensChange(
                        this, name, toks);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(
     *      de.dante.extex.interpreter.context.Color,
     *      boolean)
     */
    public void set(final Color color, final boolean global)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), color), global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(
     *      de.dante.extex.interpreter.context.Direction,
     *      boolean)
     */
    public void set(final Direction direction, final boolean global)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), direction), global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(
     *      de.dante.extex.interpreter.type.font.Font,
     *      boolean)
     */
    public void set(final Font font, final boolean global)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), font), global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(
     *      de.dante.extex.language.Language,
     *      boolean)
     */
    public void set(final Language language, final boolean global)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), language), global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#set(
     *      java.lang.Object,
     *      java.lang.Object,
     *      java.lang.Object,
     *      boolean)
     */
    public void set(final Object extension, final Object key,
            final Object value, final boolean global) {

        group.set(extension, key, value, global);
    }

    /**
     * Setter for the typesetting context in the specified groups.
     *
     * @param context the new context to use
     * @param global if <code>true</code> then the new value is set in all
     *            groups, otherwise only in the current group.
     */
    public void set(final TypesettingContext context, final boolean global) {

        group.setTypesettingContext(context, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setAfterassignment(
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void setAfterassignment(final Token token) {

        afterassignment = token;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setBox(java.lang.String,
     *      de.dante.extex.interpreter.type.box.Box, boolean)
     */
    public void setBox(final String name, final Box value, final boolean global) {

        group.setBox(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCatcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.scanner.type.Catcode, boolean)
     */
    public void setCatcode(final UnicodeChar c, final Catcode cc,
            final boolean global) {

        group.setCatcode(c, cc, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#setCode(
     *      de.dante.extex.scanner.type.token.CodeToken,
     *      de.dante.extex.interpreter.type.Code, boolean)
     */
    public void setCode(final CodeToken t, final Code code, final boolean global)
            throws InterpreterException {

        group.setCode(t, code, global);

        List observerList = (List) changeCodeObservers.get(t);
        if (null != observerList) {
            runCodeObservers(t, code, observerList);
        }
        observerList = (List) changeCodeObservers.get(null);
        if (null != observerList) {
            runCodeObservers(t, code, observerList);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCount(
     *      java.lang.String,
     *      long, boolean)
     */
    public void setCount(final String name, final long value,
            final boolean global) throws InterpreterException {

        Count count = new Count(value);
        group.setCount(name, count, global);

        List observerList = (List) changeCountObservers.get(name);
        if (null != observerList) {
            runCountObservers(name, count, observerList);
        }
        observerList = (List) changeCountObservers.get(null);
        if (null != observerList) {
            runCountObservers(name, count, observerList);
        }
    }

    /**
     * @see de.dante.extex.typesetter.TypesetterOptions#setCountOption(
     *      java.lang.String,
     *      long)
     */
    public void setCountOption(final String name, final long value)
            throws GeneralException {

        setCount(name, value, false);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDelcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.typesetter.type.math.MathDelimiter,
     *      boolean)
     */
    public void setDelcode(final UnicodeChar c, final MathDelimiter delimiter,
            final boolean global) {

        group.setDelcode(c, delimiter, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDimen(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.dimen.Dimen,
     *      boolean)
     */
    public void setDimen(final String name, final Dimen value,
            final boolean global) throws InterpreterException {

        group.setDimen(name, value, global);

        List observerList = (List) changeDimenObservers.get(name);
        if (null != observerList) {
            runDimenObservers(name, value, observerList);
        }
        observerList = (List) changeDimenObservers.get(null);
        if (null != observerList) {
            runDimenObservers(name, value, observerList);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextDimen#setDimen(
     *      java.lang.String,
     *      long, boolean)
     */
    public void setDimen(final String name, final long value,
            final boolean global) throws InterpreterException {

        setDimen(name, new Dimen(value), global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setFont(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.font.Font,
     *      boolean)
     */
    public void setFont(final String name, final Font font, final boolean global) {

        group.setFont(name, font, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setFontFactory(
     *      de.dante.extex.font.FontFactory)
     */
    public void setFontFactory(final FontFactory factory) {

        this.fontFactory = factory;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setGlue(java.lang.String,
     *      de.dante.extex.interpreter.type.glue.Glue, boolean)
     */
    public void setGlue(final String name, final Glue value,
            final boolean global) throws InterpreterException {

        group.setSkip(name, value, global);

        List observerList = (List) changeGlueObservers.get(name);
        if (null != observerList) {
            runGlueObservers(name, value, observerList);
        }
        observerList = (List) changeGlueObservers.get(null);
        if (null != observerList) {
            runGlueObservers(name, value, observerList);
        }
    }

    /**
     * Setter for the id string. The id string is the classification of the
     * original source like given in the fmt file.
     *
     * @param theId the id string
     *
     * @see de.dante.extex.interpreter.context.Context#setId(java.lang.String)
     */
    public void setId(final String theId) {

        this.id = theId;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setInFile(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.file.InFile, boolean)
     */
    public void setInFile(final String name, final InFile file,
            final boolean global) {

        group.setInFile(name, file, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextInteraction#setInteraction(
     *      de.dante.extex.interpreter.interaction.Interaction)
     */
    public void setInteraction(final Interaction interaction)
            throws InterpreterException {

        if (this.interaction != interaction) {
            this.interaction = interaction;
            try {
                for (int i = 0; i < changeInteractionObservers.size(); i++) {
                    ((InteractionObserver) changeInteractionObservers.get(i))
                            .receiveInteractionChange(this, interaction);
                }
            } catch (Exception e) {
                throw new InterpreterException(e);
            }
        }
    }

    /**
     * @see de.dante.extex.language.LanguageManagerCarrier#setLanguageManager(
     *      de.dante.extex.language.LanguageManager)
     */
    public void setLanguageManager(final LanguageManager manager)
            throws ConfigurationException {

        if (languageManager == null) {
            set(manager.getLanguage("0"), true);
        }
        this.languageManager = manager;
        typesettingContextFactory.setLanguageManager(languageManager);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setLccode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar,
     *      boolean)
     */
    public void setLccode(final UnicodeChar uc, final UnicodeChar lc,
            final boolean global) {

        group.setLccode(uc, lc, global);
    }

    /**
     * Setter for the magnification. The magnification is a global value which
     * can be assigned at most once. It contains the magnification factor in
     * permille. The default value is 1000. It can only take positive numbers
     * as values. The maximal value is taken from the configuration option
     * <tt>maximalMaginification</tt>.
     * The default value for the maximal magnification is 32768.
     *
     * @see de.dante.extex.interpreter.context.Context#setMagnification(long, boolean)
     */
    public void setMagnification(final long mag, final boolean lock)
            throws HelpingException {

        if (magnificationLock && this.magnification != mag) {
            throw new HelpingException(localizer, "TTP.IncompatibleMag", //
                    Long.toString(mag), Long.toString(magnification));
        }

        magnificationLock |= lock;

        if (mag < 1 || mag > magnificationMax) {
            throw new HelpingException(localizer, "TTP.IllegalMag", //
                    Long.toString(mag));
        }

        magnification = mag;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMark(
     *      java.lang.Object,
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void setMark(final Object name, final Tokens mark) {

        if (firstmarks.get(name) == null) {
            firstmarks.put(name, mark);
        }
        bottommarks.put(name, mark);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMathcode(
     *      de.dante.util.UnicodeChar,
     *      MathCode, boolean)
     */
    public void setMathcode(final UnicodeChar c, final MathCode code,
            final boolean global) {

        group.setMathcode(c, code, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMuskip(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.muskip.Muskip, boolean)
     */
    public void setMuskip(final String name, final Muskip value,
            final boolean global) {

        group.setMuskip(name, value, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setNamespace(
     *      java.lang.String, boolean)
     */
    public void setNamespace(final String namespace, final boolean global) {

        group.setNamespace(namespace, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setOutFile(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.file.OutFile, boolean)
     */
    public void setOutFile(final String name, final OutFile file,
            final boolean global) {

        group.setOutFile(name, file, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setParshape(
     *      de.dante.extex.typesetter.paragraphBuilder.ParagraphShape)
     */
    public void setParshape(final ParagraphShape shape) {

        this.parshape = shape;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setSfcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setSfcode(final UnicodeChar c, final Count code,
            final boolean global) {

        group.setSfcode(c, code, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextMark#setSplitMark(
     *      java.lang.Object,
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void setSplitMark(final Object name, final Tokens mark) {

        if (splitFirstMarks.get(name) == null) {
            splitFirstMarks.put(name, mark);
        }
        splitBottomMarks.put(name, mark);
    }

    /**
     * Setter for standardTokenStream.
     *
     * @param standardTokenStream the standardTokenStream to set.
     */
    public void setStandardTokenStream(final TokenStream standardTokenStream) {

        this.standardTokenStream = standardTokenStream;
        group.setStandardTokenStream(standardTokenStream);
    }

    /**
     * Setter for the token factory
     *
     * @param factory the new value of the factory
     *
     * @see de.dante.extex.interpreter.context.Context#setTokenFactory(
     *      de.dante.extex.scanner.type.token.TokenFactory)
     */
    public void setTokenFactory(final TokenFactory factory) {

        tokenFactory = factory;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setToks(java.lang.String,
     *      de.dante.extex.interpreter.type.tokens.Tokens, boolean)
     */
    public void setToks(final String name, final Tokens toks,
            final boolean global) throws InterpreterException {

        group.setToks(name, toks, global);

        List observerList = (List) changeToksObservers.get(name);
        if (null != observerList) {
            runTokensObservers(name, toks, observerList);
        }
        observerList = (List) changeToksObservers.get(null);
        if (null != observerList) {
            runTokensObservers(name, toks, observerList);
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setUccode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar,
     *      boolean)
     */
    public void setUccode(final UnicodeChar lc, final UnicodeChar uc,
            final boolean global) {

        group.setUccode(lc, uc, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#startMarks()
     */
    public void startMarks() {

        topmarks.putAll(firstmarks);
        topmarks.putAll(bottommarks);
        firstmarks.clear();
        bottommarks.clear();
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#unitIterator()
     */
    public Iterator unitIterator() {

        return units.iterator();
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.code.CodeObservable#unregisterCodeChangeObserver(
     *      de.dante.extex.scanner.type.token.Token,
     *      de.dante.extex.interpreter.context.observer.code.CodeObserver)
     */
    public synchronized void unregisterCodeChangeObserver(final Token name,
            final CodeObserver observer) {

        List observerList = (List) changeCodeObservers.get(name);
        if (null == observerList) {
            return;
        }
        observerList.remove(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.conditional.ConditionalObservable#unregisterConditionalObserver(
     *      de.dante.extex.interpreter.context.observer.conditional.ConditionalObserver)
     */
    public synchronized void unregisterConditionalObserver(
            final ConditionalObserver observer) {

        conditionalObservers.remove(observer);
        if (conditionalObservers.size() == 0) {
            conditionalObservers = null;
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.count.CountObservable#unregisterCountObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.count.CountObserver)
     */
    public synchronized void unregisterCountObserver(final String name,
            final CountObserver observer) {

        List list = (List) changeCountObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.dimen.DimenObservable#unregisterDimenObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.dimen.DimenObserver)
     */
    public synchronized void unregisterDimenObserver(final String name,
            final DimenObserver observer) {

        List list = (List) changeDimenObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.glue.GlueObservable#unregisterGlueObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.glue.GlueObserver)
     */
    public synchronized void unregisterGlueObserver(final String name,
            final GlueObserver observer) {

        List list = (List) changeGlueObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.group.GroupObservable#unregisterGroupObserver(
     *      de.dante.extex.interpreter.context.observer.group.GroupObserver)
     */
    public synchronized void unregisterGroupObserver(
            final GroupObserver observer) {

        groupObservers.remove(observer);
        if (groupObservers.size() == 0) {
            groupObservers = null;
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.interaction.InteractionObservable#unregisterInteractionObserver(
     *      de.dante.extex.interpreter.context.observer.interaction.InteractionObserver)
     */
    public synchronized void unregisterInteractionObserver(
            final InteractionObserver observer) {

        while (changeInteractionObservers.remove(observer)) {
            // just removing the observer is enough
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.load.LoadedObservable#unregisterLoadObserver(
     *      de.dante.extex.interpreter.context.observer.load.LoadedObserver)
     */
    public void unregisterLoadObserver(final LoadedObserver observer) {

        if (loadObservers != null) {
            loadObservers.remove(observer);
            if (loadObservers.size() == 0) {
                loadObservers = null;
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.observer.tokens.TokensObservable#unregisterTokensChangeObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.tokens.TokensObserver)
     */
    public synchronized void unregisterTokensChangeObserver(final String name,
            final TokensObserver observer) {

        List list = (List) changeToksObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }

}
