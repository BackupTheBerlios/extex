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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.font.FontFactory;
import de.dante.extex.font.type.other.NullFont;
import de.dante.extex.hyphenation.HyphenationManager;
import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.ConditionalSwitch;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Color;
import de.dante.extex.interpreter.context.ContextInternals;
import de.dante.extex.interpreter.context.Direction;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextFactory;
import de.dante.extex.interpreter.context.observer.CodeObserver;
import de.dante.extex.interpreter.context.observer.CountObserver;
import de.dante.extex.interpreter.context.observer.DimenObserver;
import de.dante.extex.interpreter.context.observer.InteractionObserver;
import de.dante.extex.interpreter.context.observer.TokensObserver;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
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
import de.dante.extex.interpreter.type.tokens.FixedTokens;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamOptions;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;

/**
 * This is a reference implementation for an interpreter context.
 *
 * The groups are implemented as a linked list of single groups. In contrast to
 * the Knuthian implementation in TeX no undo stack is used.
 * <p>
 * Several operations have to be dealt with:
 * </p>
 * <ul>
 * <li>For each new group a new instance of a {@link Group Group} is created
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
 * <li>Set the value in all groups.</li>
 * </ul>
 * Here the third approach is used which is suspected to be a little more
 * efficient on the cost of slightly more memory consumption.</li>
 * </ul>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.73 $
 */
public class ContextImpl
        implements
            ContextInternals,
            Tokenizer,
            DocumentWriterOptions,
            TypesetterOptions,
            TokenStreamOptions,
            Observable,
            Localizable,
            Configurable,
            Serializable {

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
     * The constant <tt>TYPESETTING_CONTEXT_TAG</tt> contains the name of the
     * configuration tag for the typesetting context.
     */
    private static final String TYPESETTING_CONTEXT_TAG = "TypesettingContext";

    /**
     * The field <tt>afterassignment</tt> contains the token to be inserted
     * after a assignemnt is completed or <code>null</code>.
     */
    private Token afterassignment = null;

    /**
     * The field <tt>codeChangeObservers</tt> contains the list of observers
     * registered for change event on the code.
     */
    private transient Map changeCodeObservers;

    /**
     * The field <tt>countChangeObservers</tt> contains the list of observers
     * registered for change event on the count registers.
     */
    private transient Map changeCountObservers;

    /**
     * The field <tt>dimenChangeObservers</tt> contains the list of observers
     * registered for change event on the count registers.
     */
    private transient Map changeDimenObservers;

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
     * The field <tt>conditionalStack</tt> contains the stack for conditionals.
     */
    private List conditionalStack = new ArrayList();

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
     * The field <tt>HYPHENATION_MANAGER_TAG</tt> contains the tag of the
     * configuration to select the sub-configuration for the hyphenation
     * manager.
     */
    private final String HYPHENATION_MANAGER_TAG = "Hyphenation";

    /**
     * The field <tt>hyphenationManager</tt> contains the hyphenation manager.
     */
    private HyphenationManager hyphenationManager;

    /**
     * The field <tt>id</tt> contains the is string.
     * The id string is the classification of the
     * original source as given in the fmt file. The id string can be
     * <code>null</code> if not known yet.
     */
    private String id = null;

    /**
     * The field <tt>localizer</tt> contains the localizer to use.
     */
    private transient Localizer localizer = null;

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
     * maginification value. This is initialized to MAGNIFICATION_MAX and
     * may be overwritten from within the configuration.
     */
    private long magnificationMax = MAGNIFICATION_MAX;

    /**
     * The field <tt>parshape</tt> contains the object containing the
     * dimensions of the paragraph.
     */
    private ParagraphShape parshape = null;

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
     * Creates a new object.
     */
    public ContextImpl() {

        super();
        init();
    }

    /**
     * Creates a new object.
     *
     * @param configuration the configuration to use
     *
     * @throws ConfigurationException in case of an configuration error
     * @throws GeneralException in case of an execution error
     *
     * @deprecated Use the interface Configurable instead
     */
    protected ContextImpl(final Configuration configuration)
            throws ConfigurationException,
                GeneralException {

        this();
        configure(configuration);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#afterGroup(
     *      de.dante.util.observer.Observer)
     */
    public void afterGroup(final Observer observer) {

        group.afterGroup(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#afterGroup(
     *      de.dante.extex.scanner.Token)
     */
    public void afterGroup(final Token t) {

        group.afterGroup(t);
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

        Interaction interaction = next.getInteraction();
        if (group.getInteraction() != interaction) {
            try {
                for (int i = 0; i < changeInteractionObservers.size(); i++) {
                    ((InteractionObserver) changeInteractionObservers.get(i))
                            .receiveInteractionChange(this, interaction);
                }
            } catch (Exception e) {
                throw new InterpreterException(e);
            }
        }

        group.runAfterGroup(this, typesetter);

        Tokens toks = group.getAfterGroup();
        group = next;

        if (toks != null) {
            source.push(toks);
        }

    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration configuration)
            throws ConfigurationException {

        groupFactory = new GroupFactory(configuration
                .getConfiguration(GROUP_TAG));
        openGroup();

        Configuration typesettingConfig = configuration
                .getConfiguration(TYPESETTING_CONTEXT_TAG);

        if (hyphenationManager instanceof Configurable) {
            Configuration config = configuration
                    .getConfiguration(HYPHENATION_MANAGER_TAG);

            if (config == null) {
                throw new ConfigurationMissingException(
                        HYPHENATION_MANAGER_TAG, configuration.toString());
            }
            ((Configurable) hyphenationManager).configure(config);
        }

        if (typesettingConfig == null) {
            throw new ConfigurationMissingException(TYPESETTING_CONTEXT_TAG,
                    configuration.toString());
        }

        typesettingContextFactory = new TypesettingContextFactory();
        typesettingContextFactory.configure(typesettingConfig);
        typesettingContextFactory.setHyphenationManager(hyphenationManager);
        TypesettingContext typesettingContext = typesettingContextFactory
                .newInstance();

        typesettingContext.setFont(new NullFont());
        setTypesettingContext(typesettingContext);

        magnificationMax = configuration.getValueAsInteger(
                "maximalMagnification", (int) MAGNIFICATION_MAX);
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
     * @see de.dante.extex.interpreter.context.Context#esc(java.lang.String)
     */
    public String esc(final String name) {

        char escapechar = escapechar();
        return (escapechar != '\0' ? escapechar + name : name);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#esc(
     *      de.dante.extex.scanner.Token)
     */
    public String esc(final Token token) {

        return token.toText(escapechar());
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#escapechar()
     */
    public char escapechar() {

        long esc = getCount("escapechar").getValue();

        return (esc >= 0 ? (char) esc : '\0');
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#expand(
     *      de.dante.extex.interpreter.type.tokens.Tokens, Typesetter)
     */
    public Tokens expand(final Tokens tokens, final Typesetter typesetter)
            throws GeneralException {

        /*
         Tokens result = new Tokens();
         //TODO gene: use interface instead of implementation
         TokenStreamBaseImpl stream = new TokenStreamBaseImpl(false, tokens);

         while (!stream.isEof()) {
         Token t = stream.get(null, null);

         while (t instanceof CodeToken) {
         Code code = getCode((CodeToken) t);
         if (code instanceof ExpandableCode) {
         ((ExpandableCode) code).expand(Flags.NONE, (Context) this,
         (TokenStream) stream, typesetter);
         t = stream.get(null, null);
         }
         }

         }

         return result;
         */
        // TODO gene: expand() unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#getAfterassignment()
     */
    public Token getAfterassignment() {

        return afterassignment;
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
     * @see de.dante.extex.interpreter.context.Context#getCode(
     *      de.dante.extex.scanner.CodeToken)
     */
    public Code getCode(final CodeToken t) throws InterpreterException {

        return group.getCode(t);
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
    public Count getDelcode(final UnicodeChar c) {

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
     * @see de.dante.extex.interpreter.context.Context#getHyphenationTable(String)
     */
    public HyphenationTable getHyphenationTable(final String language)
            throws InterpreterException {

        try {
            return hyphenationManager.getHyphenationTable(language);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * Getter for the id string. The id string is the classification of the
     * original source as given in the fmt file. The id string can be
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

        return group.getInteraction();
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
    public Count getMathcode(final UnicodeChar c) {

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
     * @see de.dante.extex.interpreter.context.Context#getParshape()
     */
    public ParagraphShape getParshape() {

        //TODO gene: How does \parshape interact with groups?
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
        changeToksObservers = new HashMap();
        changeInteractionObservers = new ArrayList();

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
     * @see de.dante.extex.interpreter.context.Context#openGroup()
     */
    public void openGroup() throws ConfigurationException {

        group = groupFactory.newInstance(group);
        group.setStandardTokenStream(standardTokenStream);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#popConditional()
     */
    public Conditional popConditional() {

        int size = conditionalStack.size();
        if (size <= 0) {
            return null;
        }
        return ((Conditional) conditionalStack.remove(size - 1));
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#pushConditional(
     *      de.dante.util.Locator, boolean)
     */
    public void pushConditional(final Locator locator,
            final boolean isIfThenElse) {

        conditionalStack.add(isIfThenElse
                ? new Conditional(locator)
                : new ConditionalSwitch(locator));
    }

    /**
     * This method is mapps instances to their normal representations if
     * required. It is used during the deserialization.
     *
     * @return the normalized object
     *
     * @throws ObjectStreamException in case of an error
     */
    public Object readResolve() throws ObjectStreamException {

        init();
        return this;
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#registerCodeChangeObserver(
     *      de.dante.extex.scanner.type.Token,
     *      de.dante.extex.interpreter.context.observer.CodeChangeObserver)
     */
    public void registerCodeChangeObserver(final Token name,
            final CodeObserver observer) {

        List observerList = (List) changeCodeObservers.get(name);
        if (null == observerList) {
            observerList = new ArrayList();
            changeCodeObservers.put(name, observerList);
        }
        observerList.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCount#registerCountObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.CountChangeObserver)
     */
    public void registerCountObserver(final String name,
            final CountObserver observer) {

        List list = (List) changeCountObservers.get(name);
        if (list == null) {
            list = new ArrayList();
            changeCountObservers.put(name, list);
        }
        list.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextDimen#registerDimenObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.DimenChangeObserver)
     */
    public void registerDimenObserver(final String name,
            final DimenObserver observer) {

        List list = (List) changeDimenObservers.get(name);
        if (list == null) {
            list = new ArrayList();
            changeDimenObservers.put(name, list);
        }
        list.add(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextInteraction#registerInteractionObserver(
     *      de.dante.extex.interpreter.context.observer.InteractionObserver)
     */
    public void registerInteractionObserver(final InteractionObserver observer) {

        changeInteractionObservers.add(observer);
    }

    /**
     * @see de.dante.util.observer.Observable#registerObserver(
     *      java.lang.String,
     *      de.dante.util.observer.Observer)
     */
    public void registerObserver(final String name, final Observer observer)
            throws NotObservableException {

        throw new NotObservableException(name);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextTokens#registerTokensObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.TokensObserver)
     */
    public void registerTokensObserver(final String name,
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
     * @see de.dante.extex.interpreter.context.Context#setAfterassignment(
     *      de.dante.extex.scanner.Token)
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
     *      de.dante.extex.scanner.Catcode, boolean)
     */
    public void setCatcode(final UnicodeChar c, final Catcode cc,
            final boolean global) {

        group.setCatcode(c, cc, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setCode(
     *      de.dante.extex.scanner.CodeToken,
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
     * @see de.dante.extex.interpreter.context.Context#setDelcode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.type.count.Count, boolean)
     */
    public void setDelcode(final UnicodeChar c, final Count code,
            final boolean global) {

        group.setDelcode(c, code, global);
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setDimen(
     *      java.lang.String,
     *      de.dante.extex.interpreter.type.dimen.Dimen, boolean)
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
            final boolean global) {

        group.setSkip(name, value, global);
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationManagerCarrier#setHyphenationManager(
     *      de.dante.extex.hyphenation.HyphenationFactory)
     */
    public void setHyphenationManager(final HyphenationManager manager) {

        this.hyphenationManager = manager;
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
     * @see de.dante.extex.interpreter.context.Context#setInteraction(
     *      de.dante.extex.interpreter.Interaction,
     *      boolean)
     */
    public void setInteraction(final Interaction interaction,
            final boolean global) throws InterpreterException {

        group.setInteraction(interaction, global);

        if (group.getInteraction() != interaction) {
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
     * @see de.dante.extex.interpreter.context.Context#setLccode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public void setLccode(final UnicodeChar uc, final UnicodeChar lc) {

        group.setLccode(uc, lc);
    }

    /**
     * Setter for the magnification. The magnification is a global value which
     * can be assigned at most once. It contains the magnification factor in
     * permille. The default value is 1000. It can only take positive numbers
     * as values. The maximal value is taken from the configuration option
     * <tt>maximalMaginification</tt>.
     * The default value for the maximal magnification is 32768.
     *
     * @see de.dante.extex.interpreter.context.Context#setMagnification(long)
     */
    public void setMagnification(final long mag) throws HelpingException {

        if (magnificationLock && this.magnification != mag) {
            throw new HelpingException(localizer, "TTP.IncompatibleMag", //
                    Long.toString(mag));
        }

        magnificationLock = true;

        if (mag < 1 || mag > magnificationMax) {
            throw new HelpingException(localizer, "TTP.IllegalMag", //
                    Long.toString(mag));
        }

        magnification = mag;
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setMathcode(
     *      de.dante.util.UnicodeChar,
     *      Count, boolean)
     */
    public void setMathcode(final UnicodeChar c, final Count code,
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
     *      de.dante.extex.scanner.TokenFactory)
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
     * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(
     *      de.dante.extex.interpreter.context.Color)
     */
    public void setTypesettingContext(final Color color)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), color));
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(
     *      de.dante.extex.interpreter.context.Direction)
     */
    public void setTypesettingContext(final Direction direction)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), direction));
    }

    /**
     * @see de.dante.extex.interpreter.context.Context#setTypesettingContext(
     *      de.dante.extex.interpreter.type.font.Font)
     */
    public void setTypesettingContext(final Font font)
            throws ConfigurationException {

        group.setTypesettingContext(typesettingContextFactory.newInstance(group
                .getTypesettingContext(), font));
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
     * @see de.dante.extex.interpreter.context.Context#setUccode(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar)
     */
    public void setUccode(final UnicodeChar lc, final UnicodeChar uc) {

        group.setUccode(lc, uc);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCode#unregisterCodeChangeObserver(
     *      de.dante.extex.scanner.type.Token,
     *      de.dante.extex.interpreter.context.observer.CodeChangeObserver)
     */
    public void unregisterCodeChangeObserver(final Token name,
            final CodeObserver observer) {

        List observerList = (List) changeCodeObservers.get(name);
        if (null == observerList) {
            return;
        }
        observerList.remove(observer);
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextCount#unregisterCountObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.CountChangeObserver)
     */
    public void unregisterCountObserver(final String name,
            final CountObserver observer) {

        List list = (List) changeCountObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextDimen#unregisterDimenObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.DimenChangeObserver)
     */
    public void unregisterDimenObserver(final String name,
            final DimenObserver observer) {

        List list = (List) changeDimenObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextInteraction#unregisterInteractionObserver(
     *      de.dante.extex.interpreter.context.observer.InteractionObserver)
     */
    public void unregisterInteractionObserver(final InteractionObserver observer) {

        while (changeInteractionObservers.remove(observer)) {
            // just removing the observer is enough
        }
    }

    /**
     * @see de.dante.extex.interpreter.context.ContextTokens#unregisterTokensChangeObserver(
     *      java.lang.String,
     *      de.dante.extex.interpreter.context.observer.TokensObserver)
     */
    public void unregisterTokensChangeObserver(final String name,
            final TokensObserver observer) {

        List list = (List) changeToksObservers.get(name);
        if (list != null) {
            while (list.remove(observer)) {
                // just removing the observer is enough
            }
        }
    }
}