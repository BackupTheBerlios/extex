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

package de.dante.extex.interpreter.max;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Logger;

import de.dante.extex.font.FontFactory;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.FlagsImpl;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextFactory;
import de.dante.extex.interpreter.context.observer.afterGroup.SwitchObserver;
import de.dante.extex.interpreter.exception.ErrorLimitException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.CantUseInException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.loader.LoaderException;
import de.dante.extex.interpreter.loader.SerialLoader;
import de.dante.extex.interpreter.observer.error.ErrorObservable;
import de.dante.extex.interpreter.observer.error.ErrorObserver;
import de.dante.extex.interpreter.observer.error.ErrorObserverList;
import de.dante.extex.interpreter.observer.expand.ExpandObservable;
import de.dante.extex.interpreter.observer.expand.ExpandObserver;
import de.dante.extex.interpreter.observer.expand.ExpandObserverList;
import de.dante.extex.interpreter.observer.expandMacro.ExpandMacroObservable;
import de.dante.extex.interpreter.observer.expandMacro.ExpandMacroObserver;
import de.dante.extex.interpreter.observer.expandMacro.ExpandMacroObserverList;
import de.dante.extex.interpreter.observer.load.LoadObservable;
import de.dante.extex.interpreter.observer.load.LoadObserver;
import de.dante.extex.interpreter.observer.load.LoadObserverList;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.PrefixCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.LanguageManagerFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.ActiveCharacterToken;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.ControlSequenceToken;
import de.dante.extex.scanner.type.CrToken;
import de.dante.extex.scanner.type.LeftBraceToken;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.MacroParamToken;
import de.dante.extex.scanner.type.MathShiftToken;
import de.dante.extex.scanner.type.OtherToken;
import de.dante.extex.scanner.type.RightBraceToken;
import de.dante.extex.scanner.type.SpaceToken;
import de.dante.extex.scanner.type.SubMarkToken;
import de.dante.extex.scanner.type.SupMarkToken;
import de.dante.extex.scanner.type.TabMarkToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.scanner.type.TokenVisitor;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.Switch;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.configuration.ConfigurationWrapperException;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a reference implementation for a <b>MA</b>cro e<b>X</b>pander. The
 * macro expander is the core engine driving <logo>ExTeX</logo>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.78 $
 */
public abstract class Max
        implements
            Interpreter,
            Localizable,
            LogEnabled,
            ExpandObservable,
            ExpandMacroObservable,
            ErrorObservable,
            LoadObservable,
            TokenVisitor {

    /**
     * The field <tt>CONTEXT_TAG</tt> contains the name of the tag for the
     * configuration of the context.
     */
    private static final String CONTEXT_TAG = "Context";

    /**
     * The field <tt>LANGUAGE_TAG</tt> contains the name of the tag for the
     * configuration of the language manager.
     */
    private static final String LANGUAGE_TAG = "Language";

    /**
     * The constant <tt>MAX_ERRORS_DEFAULT</tt> contains the default value for
     * maximal allowed number of errors after which the <logo>ExTeX</logo> run
     * is terminated automatically.
     */
    private static final int MAX_ERRORS_DEFAULT = 100;

    /**
     * The constant <tt>MINUTES_PER_HOUR</tt> contains the number of minutes
     * per hour.
     */
    private static final int MINUTES_PER_HOUR = 60;

    /**
     * The field <tt>configuration</tt> contains the configuration for this
     * interpreter.
     */
    private Configuration configuration = null;

    /**
     * The field <tt>context</tt> contains the processing context. Here nearly
     * all relevant information can be found.
     */
    private Context context = null;

    /**
     * The error handler is invoked whenever an error is detected. If none is
     * registered then the default behavior is shown.
     */
    private ErrorHandler errorHandler = null;

    /**
     * The field <tt>everyRun</tt> contains the String to be inserted at the
     * beginning of each run. It can be <code>null</code> to denote that
     * nothing should be inserted.
     */
    private String everyRun = null;

    /**
     * The field <tt>localizer</tt> contains the localizer to use.
     */
    private transient Localizer localizer = null;

    /**
     * The field <tt>logger</tt> contains the logger or
     * <code>null</code> if none has been set yet.
     */
    private Logger logger = null;

    /**
     * The field <tt>maxErrors</tt> contains the number of errors after which
     * the run is terminated. This value can be overwritten in the
     * configuration.
     */
    private int maxErrors = MAX_ERRORS_DEFAULT;

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when an error occurs. The argument is the
     * exception encountered.
     */
    private ErrorObserver observersError = null;

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when a new token is about to be expanded. The
     * argument is the token to be expanded.
     */
    private ExpandObserver observersExpand = null;

    /**
     * The field <tt>observersUndump</tt> contains the ...
     */
    private LoadObserver observersLoad = null;

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when a macro is expanded.
     */
    private ExpandMacroObserver observersMacro = null;

    /**
     * This is the prefix for the next invocations.
     */
    private Flags prefix = new FlagsImpl();

    /**
     * The field <tt>typesetter</tt> contains the typesetter for handling
     * "left-over" material.
     */
    private Typesetter typesetter = null;

    /**
     * Creates a new object.
     */
    public Max() {

        super();
    }

    /**
     * Apply the configuration options found in the given configuration object.
     *
     * @param configuration the configuration object to consider.
     *
     * @throws ConfigurationException in case of a configuration error
     */
    public void configure(final Configuration configuration)
            throws ConfigurationException {

        if (configuration == null) {
            throw new ConfigurationMissingException("Interpreter");
        }

        this.configuration = configuration;

        maxErrors = configuration.getValueAsInteger("maxErrors", maxErrors);

        TokenFactory tokenFactory = configureTokenFactory(configuration);
        makeContext(configuration);
        context.setTokenFactory(tokenFactory);
        configureHyhenation(configuration);

        Configuration everyRunConfig = configuration
                .findConfiguration("everyjob");
        if (everyRunConfig != null) {
            everyRun = everyRunConfig.getValue();
        }

    }

    /**
     * Prepare the hyphenation manager according to its configuration.
     *
     * @param configuration the configuration
     *
     * @throws ConfigurationException in case of a configuration error
     */
    private void configureHyhenation(final Configuration configuration)
            throws ConfigurationException {

        LanguageManagerFactory factory = new LanguageManagerFactory();
        factory.enableLogging(logger);
        Configuration cfg = configuration.findConfiguration(LANGUAGE_TAG);
        if (cfg == null) {
            throw new ConfigurationMissingException(LANGUAGE_TAG, configuration
                    .toString());
        }
        factory.configure(cfg);
        context.setLanguageManager(factory.newInstance(""));
    }

    /**
     * Prepare the primitives according to their configuration.
     *
     * @param configuration the configuration
     * @param tokenFactory the token factory
     * @param streamFactory the token stream factory
     *
     * @throws ConfigurationException in case of a configuration error
     */
    protected void configurePrimitives(final Configuration configuration,
            final TokenFactory tokenFactory,
            final TokenStreamFactory streamFactory)
            throws ConfigurationException {

        PrimitiveFactory primitiveFactory = new PrimitiveFactory(streamFactory);
        Iterator iterator = configuration.iterator("primitives");

        try {
            while (iterator.hasNext()) {
                primitiveFactory.define((Configuration) iterator.next(),
                        tokenFactory, context, logger, null); //TODO gene: provide OutputStreamFactory
            }
        } catch (GeneralException e) {
            throw new ConfigurationWrapperException(e);
        }
    }

    /**
     * Prepare the token factory according to its configuration.
     *
     * @param config the configuration
     *
     * @return the token factory
     *
     * @throws ConfigurationException in case of a configuration error
     */
    private TokenFactory configureTokenFactory(final Configuration config)
            throws ConfigurationException {

        TokenFactoryFactory factory = new TokenFactoryFactory(config
                .getConfiguration("TokenFactory"));
        factory.enableLogging(logger);
        return factory.createInstance();
    }

    /**
     * Setter for the getLocalizer().
     *
     * @param theLocalizer the getLocalizer() to use
     *
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {

        this.localizer = theLocalizer;
    }

    /**
     * Setter for the logger.
     *
     * @param theLogger the new logger
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *         java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * This method contains the main execution loop.
     *
     * @param onceMore switch to control the termination of the execution
     *
     * @throws InterpreterException in case of an error
     * <ul>
     *  <li> ErrorLimitException in case that the number of errors exceeds
     *   the configured error limit</li>
     * </ul>
     */
    private void execute(final Switch onceMore) throws InterpreterException {

        for (Token token = getToken(context); token != null; token = getToken(context)) {

            if (observersExpand != null) {
                observersExpand.update(token);
            }
            try {

                token.visit(this, null);

            } catch (InterpreterException e) {

                if (observersError != null) {
                    observersError.update(e);
                }

                if (context.incrementErrorCount() > maxErrors) { // cf. TTP[82]
                    throw new ErrorLimitException(maxErrors);
                } else if (errorHandler != null && //
                        !errorHandler.handleError(e, token, this, context)) {
                    throw e;
                } else {
                    throw e;
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new InterpreterException(e);
            }

            if (!onceMore.isOn()) {
                return;
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#execute(
     *      de.dante.extex.scanner.type.Token, Context, Typesetter)
     */
    public void execute(final Token token, final Context theContext,
            final Typesetter theTypesetter) throws InterpreterException {

        if (observersExpand != null) {
            observersExpand.update(token);
        }
        try {

            token.visit(this, null);

        } catch (InterpreterException e) {

            if (observersError != null) {
                observersError.update(e);
            }

            if (theContext.incrementErrorCount() > maxErrors) { // cf. TTP[82]
                throw new ErrorLimitException(maxErrors);
            } else if (errorHandler != null && //
                    errorHandler.handleError(e, token, this, theContext)) {
                return;
            }
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#executeGroup()
     */
    public void executeGroup() throws InterpreterException {

        Switch b = new Switch(true);
        context.afterGroup(new SwitchObserver(b, false));
        execute(b);
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#expand(
     *      de.dante.extex.scanner.type.Token)
     */
    protected Token expand(final Token token) throws InterpreterException {

        Token t = token;
        Code code;

        while (t instanceof CodeToken) {
            if (observersExpand != null) {
                observersExpand.update(t);
            }
            code = context.getCode((CodeToken) t);
            if (code instanceof ExpandableCode) {
                ((ExpandableCode) code).expand(prefix, context, this,
                        typesetter);
            } else {
                return t;
            }
            t = getToken(context);
        }
        return t;
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#expand(
     *      de.dante.extex.interpreter.type.tokens.Tokens,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens expand(final Tokens tokens, final Typesetter typesetter)
            throws GeneralException {

        Tokens result = new Tokens();
        TokenStream stream;
        try {
            stream = getTokenStreamFactory().newInstance("");
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }

        for (int i = tokens.length() - 1; i >= 0; i--) {
            stream.put(tokens.get(i));
        }

        TokenVisitor tv = new TokenVisitor() {

            public Object visitActive(final ActiveCharacterToken token,
                    final Object arg) throws Exception {

                Code code = context.getCode(token);
                if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context,
                            (TokenSource) arg, typesetter);
                } else if (code == null) {
                    throw new UndefinedControlSequenceException(token
                            .toString());
                } else {
                    //TODO gene: unimplemented
                    throw new RuntimeException("unimplemented");
                }

                return null;
            }

            public Object visitCr(final CrToken token, final Object arg)
                    throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitEscape(final ControlSequenceToken token,
                    final Object arg) throws Exception {

                Code code = context.getCode(token);
                if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context,
                            (TokenSource) arg, typesetter);
                } else if (code == null) {
                    throw new UndefinedControlSequenceException(token
                            .toString());
                } else {
                    //TODO gene: unimplemented
                    throw new RuntimeException("unimplemented");
                }

                return null;
            }

            public Object visitLeftBrace(final LeftBraceToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitLetter(final LetterToken token, final Object arg)
                    throws Exception {

                return token;
            }

            public Object visitMacroParam(final MacroParamToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitMathShift(final MathShiftToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitOther(final OtherToken token, final Object arg)
                    throws Exception {

                return token;
            }

            public Object visitRightBrace(final RightBraceToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitSpace(final SpaceToken token, final Object arg)
                    throws Exception {

                return token;
            }

            public Object visitSubMark(final SubMarkToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitSupMark(final SupMarkToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

            public Object visitTabMark(final TabMarkToken token,
                    final Object arg) throws Exception {

                //TODO gene: unimplemented
                throw new RuntimeException("unimplemented");
            }

        };

        try {
            for (;;) {
                Token t = stream.get(null, null);
                if (t == null) {
                    return result;
                }
                t = (Token) t.visit(tv, stream);
                if (t != null) {
                    result.add(t);
                }
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#getContext()
     */
    public Context getContext() {

        return context;
    }

    /**
     * Getter for the error handler. The error handler might not be set. In this
     * case <code>null</code> is returned.
     *
     * @return the error handler currently registered
     */
    public ErrorHandler getErrorHandler() {

        return errorHandler;
    }

    /**
     * Getter for the interaction mode.
     *
     * @return the interaction mode
     */
    public Interaction getInteraction() {

        return context.getInteraction();
    }

    /**
     * Getter for localizer.
     *
     * @return the localizer.
     */
    protected Localizer getLocalizer() {

        return this.localizer;
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#getTypesetter()
     */
    public Typesetter getTypesetter() {

        return this.typesetter;
    }

    /**
     * Initialize the date and time related primitives.
     *
     * @param calendar the time and date when <logo>ExTeX</logo> has been started
     *
     * @throws InterpreterException in case of an error
     */
    protected void initializeDate(final Calendar calendar) throws InterpreterException {

        context.setCount("day", calendar.get(Calendar.DAY_OF_MONTH), true);
        context.setCount("month", calendar.get(Calendar.MONTH), true);
        context.setCount("year", calendar.get(Calendar.YEAR), true);
        context.setCount("time", calendar.get(Calendar.HOUR_OF_DAY)
                * MINUTES_PER_HOUR + calendar.get(Calendar.MINUTE), true);
    }

    /**
     * Load the format from an external source.
     *
     * @param stream the stream to read the format information from
     * @param fmt the name of the format to be loaded
     *
     * @throws LoaderException in case that a class could not be found
     *  on the class path or a wrong class is contained in the format
     * @throws IOException in case that an IO error occurs during the reading
     *  of the format
     *
     * @see de.dante.extex.interpreter.Interpreter#loadFormat(
     *      java.io.InputStream, java.lang.String)
     */
    public void loadFormat(final InputStream stream, final String fmt)
            throws IOException,
                LoaderException {

        Context newContext;
        try {
            //Registrar.register( xxx , Code.class);
            newContext = new SerialLoader().load(stream);
            Registrar.reset();
        } catch (InvalidClassException e) {
            throw new LoaderException(getLocalizer().format(
                    "ClassLoaderIncompatibility", fmt, e.getMessage()));
        }

        try {
            initializeDate(Calendar.getInstance());
        } catch (InterpreterException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConfigurationException) {
                throw new LoaderException(cause);
            }
            throw new LoaderException(e);
        }

        if (newContext instanceof Configurable) {
            try {
                ((Configurable) newContext).configure(configuration);
            } catch (ConfigurationException e) {
                throw new LoaderException(e);
            }
        }
        newContext.setFontFactory(context.getFontFactory());
        newContext.setTokenFactory(context.getTokenFactory());
        context = newContext;

        if (observersLoad != null) {
            observersLoad.update(context);
        }
    }

    /**
     * Prepare the context according to its configuration.
     *
     * @param config the configuration
     *
     * @throws ConfigurationException in case of a configuration error
     */
    private void makeContext(final Configuration config)
            throws ConfigurationException {

        Configuration cfg = config.getConfiguration(CONTEXT_TAG);
        if (cfg == null) {
            throw new ConfigurationMissingException(CONTEXT_TAG, config
                    .toString());
        }
        ContextFactory contextFactory = new ContextFactory(cfg);
        contextFactory.enableLogging(logger);
        context = contextFactory.newInstance(null);
    }

    /**
     * Add an observer for the error event.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final ErrorObserver observer) {

        observersError = ErrorObserverList.register(observersError, observer);
    }

    /**
     * Add an observer for the expand event.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final ExpandMacroObserver observer) {

        observersMacro = ExpandMacroObserverList.register(observersMacro,
                observer);
    }

    /**
     * Add an observer for the expand event.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final ExpandObserver observer) {

        observersExpand = ExpandObserverList
                .register(observersExpand, observer);
    }

    /**
     * @see de.dante.extex.interpreter.observer.load.LoadObservable#registerObserver(
     *      de.dante.extex.interpreter.observer.undump.UndumpObserver)
     */
    public void registerObserver(final LoadObserver observer) {

        observersLoad = LoadObserverList.register(observersLoad, observer);
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#run()
     */
    public void run() throws ConfigurationException, InterpreterException {

        if (typesetter == null) {
            throw new NoTypesetterException(getClass().getName() + "#run()");
        }

        if (getTokenStreamFactory() == null) {
            throw new NoTokenStreamFactoryException(getClass().getName()
                    + "#run()");
        }

        if (everyRun != null && everyRun.length() > 0) {
            addStream(getTokenStreamFactory().newInstance(everyRun));
        }

        push(context.getToks("everyjob"));

        execute(new Switch(true));

        typesetter.finish();

        // TTP [1335]
        long groupLevel = context.getGroupLevel();
        if (groupLevel != 0) {
            Localizer localizer = getLocalizer();
            String endPrimitive = localizer.format("TTP.EndPrimitive");
            String message = localizer.format("TTP.EndGroup", context
                    .esc(endPrimitive), Long.toString(groupLevel));
            logger.warning(message);
            throw new InterpreterException(message);
        }
        Conditional cond = context.popConditional();
        if (cond != null) {
            Localizer localizer = getLocalizer();
            String endPrimitive = localizer.format("TTP.EndPrimitive");
            String message = localizer.format("TTP.EndIf", context
                    .esc(endPrimitive), cond.getPrimitive(), cond.getLocator()
                    .toString());
            logger.warning(message);
            throw new InterpreterException(message);
        }
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#run(
     *      de.dante.extex.scanner.stream.TokenStream)
     */
    public void run(final TokenStream stream)
            throws ConfigurationException,
                InterpreterException {

        addStream(stream);
        run();
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#setContext(
     *      de.dante.extex.interpreter.context.Context)
     */
    public void setContext(final Context context) {

        this.context = context;
    }

    /**
     * Setter for the error handler. The value of <code>null</code> can be
     * used to delete the error handler currently set.
     *
     * @param handler the new error handler
     */
    public void setErrorHandler(final ErrorHandler handler) {

        errorHandler = handler;
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#setFontFactory(
     *      de.dante.extex.font.FontFactory)
     */
    public void setFontFactory(final FontFactory fontFactory) {

        context.setFontFactory(fontFactory);
    }

    /**
     * Setter for the interaction mode. The interaction mode is set globally.
     *
     * @param interaction the interaction mode
     *
     * @throws GeneralException in case of an error
     */
    public void setInteraction(final Interaction interaction)
            throws GeneralException {

        context.setInteraction(interaction);
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#setJobname(java.lang.String)
     */
    public void setJobname(final String jobname) throws GeneralException {

        context.setToks("jobname", new Tokens(context, jobname), true);
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#setTypesetter(
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void setTypesetter(final Typesetter typesetter) {

        this.typesetter = typesetter;
    }

    /**
     * This visit method is invoked on an active token.
     * In <logo>TeX</logo> this is e.g. ~.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitActive(
     *      de.dante.extex.scanner.ActiveCharacterToken, java.lang.Object)
     */
    public Object visitActive(final ActiveCharacterToken token,
            final Object ignore) throws GeneralException {

        Code code = context.getCode(token);
        if (code == null) {
            throw new HelpingException(getLocalizer(), "TTP.UndefinedToken",
                    token.toString());
        }

        code.execute(prefix, context, this, typesetter);

        if (!(code instanceof PrefixCode)) {
            prefix.clear();
        }
        return null;
    }

    /**
     * This visit method is invoked on a cr token.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitCr(
     *      de.dante.extex.scanner.CrToken, java.lang.Object)
     */
    public Object visitCr(final CrToken token, final Object ignore)
            throws GeneralException {

        typesetter
                .cr(context, context.getTypesettingContext(), token.getChar());
        return null;
    }

    /**
     * This visit method is invoked on an escape token.
     * In <logo>TeX</logo> this normally means a control sequence.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitEscape(
     *      de.dante.extex.scanner.ControlSequenceToken, java.lang.Object)
     */
    public Object visitEscape(final ControlSequenceToken token,
            final Object ignore) throws GeneralException {

        Code code = context.getCode(token);
        if (observersMacro != null) {
            observersMacro.update(token, code);
        }
        if (code == null) {
            throw new HelpingException(getLocalizer(), "TTP.UndefinedToken", //
                    token.toString());
        }

        code.execute(prefix, context, this, typesetter);

        if (!(code instanceof PrefixCode)) {
            prefix.clear();
        }
        return null;
    }

    /**
     * This visit method is invoked on a left brace token.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [1063]"
     * @see de.dante.extex.scanner.type.TokenVisitor#visitLeftBrace(
     *      de.dante.extex.scanner.LeftBraceToken, java.lang.Object)
     */
    public Object visitLeftBrace(final LeftBraceToken token, final Object ignore)
            throws GeneralException {

        try {
            context.openGroup();
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }
        typesetter.leftBrace();

        return null;
    }

    /**
     * This visit method is invoked on a letter token.
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitLetter(
     *      de.dante.extex.scanner.LetterToken, java.lang.Object)
     */
    public Object visitLetter(final LetterToken token, final Object ignore)
            throws GeneralException {

        typesetter.letter(context, context.getTypesettingContext(), token
                .getChar());
        return null;
    }

    /**
     * This visit method is invoked on a macro parameter token.
     * In <logo>TeX</logo> this normally is a <tt>#</tt>.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitMacroParam(
     *      de.dante.extex.scanner.MacroParamToken, java.lang.Object)
     */
    public Object visitMacroParam(final MacroParamToken token,
            final Object ignore) throws GeneralException {

        throw new CantUseInException(token.toString(), typesetter.getMode()
                .toString());
    }

    /**
     * This visit method is invoked on a math shift token.
     * In <logo>TeX</logo> this normally is a <tt>$</tt>.
     *
     *
     *
     * <doc name="everymath" type="register">
     * <h3>The Parameter <tt>\everymath</tt></h3>
     *
     * <p>
     *
     * </p>
     * </doc>
     *
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [1137]"
     * @see de.dante.extex.scanner.type.TokenVisitor#visitMathShift(
     *      de.dante.extex.scanner.MathShiftToken, java.lang.Object)
     */
    public Object visitMathShift(final MathShiftToken token, final Object ignore)
            throws GeneralException {

        try {
            typesetter.mathShift(context, this, token);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }
        return null;
    }

    /**
     * This visit method is invoked on an other token.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitOther(
     *      de.dante.extex.scanner.OtherToken, java.lang.Object)
     */
    public Object visitOther(final OtherToken token, final Object ignore)
            throws GeneralException {

        typesetter.letter(context, //
                context.getTypesettingContext(), //
                token.getChar());
        return null;
    }

    /**
     * This visit method is invoked on a right brace token.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [1067]"
     * @see de.dante.extex.scanner.type.TokenVisitor#visitRightBrace(
     *      de.dante.extex.scanner.RightBraceToken, java.lang.Object)
     */
    public Object visitRightBrace(final RightBraceToken token,
            final Object ignore) throws GeneralException {

        context.closeGroup(typesetter, this);
        typesetter.rightBrace();
        return null;
    }

    /**
     * This visit method is invoked on a space token.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitSpace(
     *      de.dante.extex.scanner.SpaceToken, java.lang.Object)
     */
    public Object visitSpace(final SpaceToken token, final Object ignore)
            throws GeneralException {

        try {
            typesetter.addSpace(context.getTypesettingContext(), null);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }
        return null;
    }

    /**
     * This visit method is invoked on a sub mark token.
     * In <logo>TeX</logo> this normally is a <tt>_</tt>.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitSubMark(
     *      de.dante.extex.scanner.SubMarkToken, java.lang.Object)
     */
    public Object visitSubMark(final SubMarkToken token, final Object ignore)
            throws GeneralException {

        typesetter.subscriptMark(context, this, typesetter, token);
        return null;
    }

    /**
     * This visit method is invoked on a sup mark token.
     * In <logo>TeX</logo> this normally is a <tt>^</tt>.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitSupMark(
     *      de.dante.extex.scanner.SupMarkToken, java.lang.Object)
     */
    public Object visitSupMark(final SupMarkToken token, final Object ignore)
            throws GeneralException {

        typesetter.superscriptMark(context, this, typesetter, token);
        return null;
    }

    /**
     * This visit method is invoked on a tab mark token.
     * In <logo>TeX</logo> this normally is a <tt>&amp;</tt>.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.scanner.type.TokenVisitor#visitTabMark(
     *      de.dante.extex.scanner.TabMarkToken, java.lang.Object)
     */
    public Object visitTabMark(final TabMarkToken token, final Object ignore)
            throws GeneralException {

        try {
            typesetter.tab(context, this, token);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }
        return null;
    }
}