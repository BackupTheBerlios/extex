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

package de.dante.extex.interpreter.max;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Logger;

import de.dante.extex.backend.documentWriter.OutputStreamFactory;
import de.dante.extex.font.FontFactory;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.FlagsImpl;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextFactory;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.context.observer.group.SwitchObserver;
import de.dante.extex.interpreter.context.observer.load.LoadedObservable;
import de.dante.extex.interpreter.exception.ErrorLimitException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.CantUseInException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.exception.helping.UnusedPrefixException;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.extex.interpreter.loader.LoaderException;
import de.dante.extex.interpreter.loader.SerialLoader;
import de.dante.extex.interpreter.max.util.LoadUnit;
import de.dante.extex.interpreter.observer.command.CommandObservable;
import de.dante.extex.interpreter.observer.command.CommandObserver;
import de.dante.extex.interpreter.observer.command.CommandObserverList;
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
import de.dante.extex.interpreter.observer.start.StartObservable;
import de.dante.extex.interpreter.observer.start.StartObserver;
import de.dante.extex.interpreter.observer.start.StartObserverList;
import de.dante.extex.interpreter.observer.stop.StopObservable;
import de.dante.extex.interpreter.observer.stop.StopObserver;
import de.dante.extex.interpreter.observer.stop.StopObserverList;
import de.dante.extex.interpreter.primitives.register.count.util.IntegerCode;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CodeExpander;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.PrefixCode;
import de.dante.extex.interpreter.type.ProtectedCode;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.LanguageManager;
import de.dante.extex.language.LanguageManagerFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.ActiveCharacterToken;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.CrToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.MacroParamToken;
import de.dante.extex.scanner.type.token.MathShiftToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.SubMarkToken;
import de.dante.extex.scanner.type.token.SupMarkToken;
import de.dante.extex.scanner.type.token.TabMarkToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.scanner.type.token.TokenVisitor;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.util.Switch;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.RegistrarObserver;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingException;
import de.dante.util.framework.configuration.exception.ConfigurationWrapperException;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a reference implementation for a <b>MA</b>cro e<b>X</b>pander. The
 * macro expander is the core engine driving <logo>ExTeX</logo>.
 *
 *
 * <doc name="ignorevoid" type="register">
 * <h3>The Count Parameter <tt>\ignorevoid</tt></h3>
 * <p>
 *  The count register <tt>\ignorevoid</tt> determines how an undefined
 *  active character or control sequence is encountered. If the value is
 *  greater than 0 then undefined code is ignored. Otherwise it leads to an
 *  error message.
 * </p>
 * <p>
 *  This count parameter has been introduced by <logo>ExTeX</logo>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ignorevoid&rang;
 *      &rarr; <tt>\ignorevoid</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \ignorevoid=1  </pre>
 *
 * </doc>
 *
 * <doc name="everyjob" type="register">
 * <h3>The Tokens Parameter <tt>\everyjob</tt></h3>
 * <p>
 *  The tokens register <tt>\everyjob</tt> contains the tokens to be inserted
 *  at the beginning of every job.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everyjob&rang;
 *       &rarr; <tt>\everyjob</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everyjob={\message{Hello world.}}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.112 $
 */
public abstract class Max
        implements
            Interpreter,
            Localizable,
            LogEnabled,
            CommandObservable,
            ExpandObservable,
            ExpandMacroObservable,
            ErrorObservable,
            LoadObservable,
            StartObservable,
            StopObservable,
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
    private IntegerCode maxErrors = new IntegerCode("maxErrors",
            MAX_ERRORS_DEFAULT);

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when a new token is about to be expanded. The
     * argument is the token to be executed.
     */
    private CommandObserver observersCommand = null;

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
     * The field <tt>observersLoad</tt> contains the observer list for the
     * observers which are registered to receive a notification when a format
     * is loaded.
     */
    private LoadObserver observersLoad = null;

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when a macro is expanded.
     */
    private ExpandMacroObserver observersMacro = null;

    /**
     * The field <tt>observersStart</tt> contains the observer list for the
     * observers which are registered to receive a notification when the
     * execution is started.
     */
    private StartObserver observersStart = null;

    /**
     * The field <tt>observersStop</tt> contains the observer list for the
     * observers which are registered to receive a notification when the
     * execution is finished.
     */
    private StopObserver observersStop = null;

    /**
     * This is the prefix for the next invocations.
     */
    private Flags prefix = new FlagsImpl();

    /**
     * The field <tt>tv</tt> contains the token visitor for expansion.
     */
    private TokenVisitor tv = new TokenVisitor() {

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitActive(
         *      de.dante.extex.scanner.type.token.ActiveCharacterToken,
         *      java.lang.Object)
         */
        public Object visitActive(final ActiveCharacterToken token,
                final Object arg) throws Exception {

            Code code = context.getCode(token);
            if (code instanceof ExpandableCode) {
                ((ExpandableCode) code).expand(Flags.NONE, context,
                        (TokenSource) arg, typesetter);
                return null;
            } else if (code == null) {
                throw new UndefinedControlSequenceException(AbstractCode
                        .printable(context, token));
            } else {
                return token;
            }
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitCr(
         *      de.dante.extex.scanner.type.token.CrToken, java.lang.Object)
         */
        public Object visitCr(final CrToken token, final Object arg)
                throws Exception {

            //TODO gene: unimplemented
            throw new RuntimeException("unimplemented");
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitEscape(
         *      de.dante.extex.scanner.type.token.ControlSequenceToken,
         *      java.lang.Object)
         */
        public Object visitEscape(final ControlSequenceToken token,
                final Object arg) throws Exception {

            Code code = context.getCode(token);
            if (code instanceof ExpandableCode) {
                ((ExpandableCode) code).expand(Flags.NONE, context,
                        (TokenSource) arg, typesetter);
                return null;
            } else if (code == null) {
                throw new UndefinedControlSequenceException(AbstractCode
                        .printable(context, token));
            } else {
                return token;
            }

        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLeftBrace(
         *      de.dante.extex.scanner.type.token.LeftBraceToken,
         *      java.lang.Object)
         */
        public Object visitLeftBrace(final LeftBraceToken token,
                final Object arg) throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLetter(
         *      de.dante.extex.scanner.type.token.LetterToken,
         *      java.lang.Object)
         */
        public Object visitLetter(final LetterToken token, final Object arg)
                throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMacroParam(
         *      de.dante.extex.scanner.type.token.MacroParamToken,
         *      java.lang.Object)
         */
        public Object visitMacroParam(final MacroParamToken token,
                final Object arg) throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMathShift(
         *      de.dante.extex.scanner.type.token.MathShiftToken,
         *      java.lang.Object)
         */
        public Object visitMathShift(final MathShiftToken token,
                final Object arg) throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitOther(
         *      de.dante.extex.scanner.type.token.OtherToken,
         *      java.lang.Object)
         */
        public Object visitOther(final OtherToken token, final Object arg)
                throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitRightBrace(
         *      de.dante.extex.scanner.type.token.RightBraceToken,
         *      java.lang.Object)
         */
        public Object visitRightBrace(final RightBraceToken token,
                final Object arg) throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSpace(
         *      de.dante.extex.scanner.type.token.SpaceToken,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceToken token, final Object arg)
                throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSubMark(
         *      de.dante.extex.scanner.type.token.SubMarkToken,
         *      java.lang.Object)
         */
        public Object visitSubMark(final SubMarkToken token, final Object arg)
                throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSupMark(
         *      de.dante.extex.scanner.type.token.SupMarkToken,
         *      java.lang.Object)
         */
        public Object visitSupMark(final SupMarkToken token, final Object arg)
                throws Exception {

            return token;
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitTabMark(
         *      de.dante.extex.scanner.type.token.TabMarkToken,
         *      java.lang.Object)
         */
        public Object visitTabMark(final TabMarkToken token, final Object arg)
                throws Exception {

            return token;
        }

    };

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
        registerObserver(new StartObserver() {

            /**
             * @see de.dante.extex.interpreter.observer.start.StartObserver#update(
             *      de.dante.extex.interpreter.Interpreter)
             */
            public void update(final Interpreter interpreter)
                    throws InterpreterException {

                try {
                    Context c = getContext();
                    CodeToken t = (CodeToken) c.getTokenFactory().createToken(
                            Catcode.ESCAPE, UnicodeChar.get('\\'), "maxErrors",
                            Namespace.SYSTEM_NAMESPACE);
                    Code code = c.getCode(t);
                    if (code instanceof IntegerCode) {
                        maxErrors = (IntegerCode) code;
                    }
                } catch (CatcodeException e) {
                    throw new InterpreterException(e);
                }
            }
        });
    }

    /**
     * Apply the configuration options found in the given configuration object.
     *
     * @param config the configuration object to consider.
     *
     * @throws ConfigurationException in case of a configuration error
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        if (config == null) {
            throw new ConfigurationMissingException("Interpreter");
        }

        this.configuration = config;

        TokenFactory tokenFactory = configureTokenFactory(config);
        makeContext(config);
        context.setTokenFactory(tokenFactory);
        configureHyhenation(config);

        OutputStreamFactory outputFactory = null; //TODO gene: provide OutputStreamFactory

        Context ctx = getContext();
        Typesetter ts = getTypesetter();
        Logger log = getLogger();
        try {
            ctx.setInteraction(Interaction.ERRORSTOPMODE);

            for (Iterator iterator = configuration.iterator("unit"); iterator
                    .hasNext();) {
                LoadUnit.loadUnit((Configuration) iterator.next(), ctx, this,
                        ts, log, outputFactory);
            }

            initializeDate(Calendar.getInstance());
        } catch (ConfigurationException e) {
            throw e;
        } catch (GeneralException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConfigurationException) {
                throw (ConfigurationException) cause;
            }
            throw new ConfigurationWrapperException(e);
        }

    }

    /**
     * Prepare the hyphenation manager according to its configuration.
     *
     * @param config the configuration
     *
     * @throws ConfigurationException in case of a configuration error
     */
    private void configureHyhenation(final Configuration config)
            throws ConfigurationException {

        LanguageManagerFactory factory = new LanguageManagerFactory();
        factory.enableLogging(logger);
        Configuration cfg = config.findConfiguration(LANGUAGE_TAG);
        if (cfg == null) {
            throw new ConfigurationMissingException(LANGUAGE_TAG, config
                    .toString());
        }
        factory.configure(cfg);
        context.setLanguageManager(factory.newInstance(""));
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

            if (observersCommand != null) {
                observersCommand.update(token);
            }
            try {

                token.visit(this, null);

            } catch (InterpreterException e) {
                Throwable x = e;
                while (x.getCause() instanceof InterpreterException) {
                    x = e.getCause();
                }

                handleException(token, context, (InterpreterException) x,
                        typesetter);

            } catch (RuntimeException e) {
                if (observersError != null) {
                    observersError.update(e);
                }
                throw e;

            } catch (Exception e) {
                if (observersError != null) {
                    observersError.update(e);
                }
                throw new InterpreterException(e);
            }

            if (!onceMore.isOn()) {
                return;
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#execute(
     *      de.dante.extex.scanner.type.token.Token, Context, Typesetter)
     */
    public void execute(final Token token, final Context theContext,
            final Typesetter theTypesetter) throws InterpreterException {

        if (observersCommand != null) {
            observersCommand.update(token);
        }
        try {

            token.visit(this, null);

        } catch (InterpreterException e) {

            handleException(token, theContext, e, theTypesetter);

        } catch (RuntimeException e) {
            if (observersError != null) {
                observersError.update(e);
            }
            throw e;

        } catch (Exception e) {
            if (observersError != null) {
                observersError.update(e);
            }
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
     * Take the token given and expand it as possible. If the token is
     * expandable then the expansion is performed until an un-expandable token
     * has been found. This token is returned.
     *
     * @param token the token to expand
     *
     * @return the next token
     *
     * @throws InterpreterException in case of an error
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
    public Tokens expand(final Tokens tokens, final Typesetter ts)
            throws InterpreterException {

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
     * Take the token given and expand it as possible while honoring the
     * protected code. If the token is not protected and expandable then the
     * expansion is performed until an un-expandable token has been found.
     * This token is returned.
     *
     * @param token the token to expand
     * @param tokens the token list to pass to an Expander
     *
     * @return the next token
     *
     * @throws InterpreterException in case of an error
     */
    protected Token expandUnproteced(final Token token, final Tokens tokens)
            throws InterpreterException {

        Token t = token;
        Code code;

        while (t instanceof CodeToken) {
            if (observersExpand != null) {
                observersExpand.update(t);
            }
            code = context.getCode((CodeToken) t);
            if (code instanceof ProtectedCode) {
                return t;
            } else if (code instanceof CodeExpander) {
                ((CodeExpander) code).expandCode(context, this, typesetter,
                        tokens);
            } else if (code instanceof ExpandableCode) {
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
     * Getter for logger.
     *
     * @return the logger
     */
    protected Logger getLogger() {

        return this.logger;
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#getTypesetter()
     */
    public Typesetter getTypesetter() {

        return this.typesetter;
    }

    /**
     * This method represents the error handler invocation.
     *
     * @param token the current token
     * @param cx the current context
     * @param e the current exception
     * @param ts the typesetter
     *
     * @throws InterpreterException in case of an error<br>
     *  especially<br>
     *  ErrorLimitException in case that the error limit has been exceeded.
     */
    private void handleException(final Token token, final Context cx,
            final InterpreterException e, final Typesetter ts)
            throws InterpreterException {

        if (e.isProcessed()) {
            // TODO gene: why???
            //ts.getManager().pop();
            return;
        }
        e.setProcessed(true);

        if (observersError != null) {
            observersError.update(e);
        }

        if (cx.incrementErrorCount() > maxErrors.getValue()) { // cf. TTP[82]
            throw new ErrorLimitException(maxErrors.getValue());
        } else if (errorHandler != null && //
                errorHandler.handleError(e, token, this, cx)) {
            return;
        }
        throw e;
    }

    /**
     * Initialize the date and time related primitives.
     *
     * <doc name="day" type="register">
     * <h3>The Count Parameter <tt>\day</tt></h3>
     * <p>
     *  The count parameter <tt>\day</tt> is set automatically at the start
     *  of a job to the day of the current date. Thus it always is initialized
     *  to a value in the range of 1 to 31.
     * </p>
     * <p>
     *  In the course of processing it can be used as any count register. This
     *  means that assignments, comparisons, and arithmetical operations work
     *  as for those.
     * </p>
     * <p>
     *  The value is stored when a format file is written. Note however that
     *  this value is overwritten when the format file is read back in.
     * </p>
     *
     * <h4>Syntax</h4>
     *  The formal description of this primitive is the following:
     *  <pre class="syntax">
     *    &lang;day&rang;
     *      &rarr; <tt>\day</tt> {@linkplain
     *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
     *        &lang;equals&rang;} {@linkplain
     *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
     *        &lang;number&rang;}  </pre>
     *
     * <h4>Examples</h4>
     *  <pre class="TeXSample">
     *    \the\day  </pre>
     * </doc>
     *
     * <doc name="month" type="register">
     * <h3>The Count Parameter <tt>\month</tt></h3>
     * <p>
     *  The count parameter <tt>\month</tt> is set automatically at the start
     *  of a job to the month of the current date. Thus it always is initialized
     *  to a value in the range of 1 to 12.
     * </p>
     * <p>
     *  In the course of processing it can be used as any count register. This
     *  means that assignments, comparisons, and arithmetical operations work
     *  as for those.
     * </p>
     * <p>
     *  The value is stored when a format file is written. Note however that
     *  this value is overwritten when the format file is read back in.
     * </p>
     *
     * <h4>Syntax</h4>
     *  The formal description of this primitive is the following:
     *  <pre class="syntax">
     *    &lang;month&rang;
     *      &rarr; <tt>\month</tt> {@linkplain
     *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
     *        &lang;equals&rang;} {@linkplain
     *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
     *        &lang;number&rang;}  </pre>
     *
     * <h4>Examples</h4>
     *  <pre class="TeXSample">
     *    \the\month  </pre>
     * </doc>
     *
     * <doc name="year" type="register">
     * <h3>The Count Parameter <tt>\year</tt></h3>
     * <p>
     *  The count parameter <tt>\year</tt> is set automatically at the start
     *  of a job to the year of the current date.
     * </p>
     * <p>
     *  In the course of processing it can be used as any count register. This
     *  means that assignments, comparisons, and arithmetical operations work
     *  as for those.
     * </p>
     * <p>
     *  The value is stored when a format file is written. Note however that
     *  this value is overwritten when the format file is read back in.
     * </p>
     *
     * <h4>Syntax</h4>
     *  The formal description of this primitive is the following:
     *  <pre class="syntax">
     *    &lang;year&rang;
     *      &rarr; <tt>\year</tt> {@linkplain
     *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
     *        &lang;equals&rang;} {@linkplain
     *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
     *        &lang;number&rang;}  </pre>
     *
     * <h4>Examples</h4>
     *  <pre class="TeXSample">
     *    \the\year  </pre>
     * </doc>
     *
     * <doc name="time" type="register">
     * <h3>The Count Parameter <tt>\time</tt></h3>
     * <p>
     *  The count parameter <tt>\time</tt> is set automatically at the start
     *  of a job to the time of the current date. The time is the number of
     *  minutes since 0:00. Thus you can extract the current hour by dividing
     *  it by 60 and the current minute by computing the remainder modulo 60.
     * </p>
     * <p>
     *  In the course of processing it can be used as any count register. This
     *  means that assignments, comparisons, and arithmetical operations work
     *  as for those.
     * </p>
     * <p>
     *  The value is stored when a format file is written. Note however that
     *  this value is overwritten when the format file is read back in.
     * </p>
     *
     * <h4>Syntax</h4>
     *  The formal description of this primitive is the following:
     *  <pre class="syntax">
     *    &lang;time&rang;
     *      &rarr; <tt>\time</tt> {@linkplain
     *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
     *        &lang;equals&rang;} {@linkplain
     *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
     *        &lang;number&rang;}  </pre>
     *
     * <h4>Examples</h4>
     *  <pre class="TeXSample">
     *    {\count0=\time
     *     \divide\count0 60
     *     \the\count0:<i>% here \count0 contains the hour</i>
     *     \multiply\count1 -60
     *     \advance\count0\time
     *     \the\count0<i>% here \count0 contains the minute</i>
     *    }<i>%</i>  </pre>
     * </doc>
     *
     *
     * @param calendar the time and date when <logo>ExTeX</logo> has been
     *  started
     *
     * @throws InterpreterException in case of an error
     */
    protected void initializeDate(final Calendar calendar)
            throws InterpreterException {

        context.setCount("day", calendar.get(Calendar.DAY_OF_MONTH), true);
        context.setCount("month", calendar.get(Calendar.MONTH) + 1, true);
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
        Object ref1 = Registrar.register(new RegistrarObserver() {

            /**
             * @see de.dante.util.framework.RegistrarObserver#reconnect(
             *      java.lang.Object)
             */
            public Object reconnect(final Object object) {

                ((LogEnabled) object).enableLogging(logger);
                return object;
            }

        }, LogEnabled.class);
        Object ref2 = Registrar.register(new RegistrarObserver() {

            /**
             * @see de.dante.util.framework.RegistrarObserver#reconnect(
             *      java.lang.Object)
             */
            public Object reconnect(final Object object) {

                ((Localizable) object).enableLocalization(LocalizerFactory
                        .getLocalizer(object.getClass().getName()));
                return object;
            }

        }, Localizable.class);
        try {
            newContext = new SerialLoader().load(stream);
        } catch (InvalidClassException e) {
            throw new LoaderException(getLocalizer().format(
                    "ClassLoaderIncompatibility", fmt, e.getMessage()));
        } finally {

            Registrar.unregister(ref2);
            Registrar.unregister(ref1);
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

        try {
            if (newContext instanceof Configurable) {
                //TODO gene: provide the correct configuration instead of the constant one
                ((Configurable) newContext).configure(configuration
                        .getConfiguration(CONTEXT_TAG)
                        .getConfiguration("ExTeX"));
            }

            LanguageManager languageManager = newContext.getLanguageManager();
            if (languageManager instanceof Configurable) {

                //TODO gene: provide the correct configuration instead of the constant one
                ((Configurable) languageManager).configure(configuration
                        .getConfiguration(LANGUAGE_TAG).getConfiguration(
                                "ExTeX"));
            }
        } catch (ConfigurationException e) {
            throw new LoaderException(e);
        }

        newContext.setFontFactory(context.getFontFactory());
        newContext.setTokenFactory(context.getTokenFactory());
        newContext.setStandardTokenStream(context.getStandardTokenStream());
        context = newContext;

        try {
            if (context instanceof LoadedObservable) {
                ((LoadedObservable) context).receiveLoad(this);
            }

            if (observersLoad != null) {
                observersLoad.update(context);
            }
        } catch (InterpreterException e) {
            throw new LoaderException(e);
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
     * Add an observer for the expand event.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final CommandObserver observer) {

        observersCommand = CommandObserverList.register(observersCommand,
                observer);
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
     *      de.dante.extex.interpreter.observer.load.LoadObserver)
     */
    public void registerObserver(final LoadObserver observer) {

        observersLoad = LoadObserverList.register(observersLoad, observer);
    }

    /**
     * Add an observer for the start event.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final StartObserver observer) {

        observersStart = StartObserverList.register(observersStart, observer);
    }

    /**
     * Add an observer for the stop event.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final StopObserver observer) {

        observersStop = StopObserverList.register(observersStop, observer);
    }

    /**
     * Report that a flag has not been used by a macro.
     *
     * @param token the macro which has been invoked
     *
     * @throws HelpingException with the appropriate error message
     */
    private void reportDirtyFlag(final Token token) throws HelpingException {

        String cause = "???";

        if (prefix.isGlobal()) {
            cause = "global";
        } else if (prefix.isImmediate()) {
            cause = "immediate";
        } else if (prefix.isLong()) {
            cause = "long";
        } else if (prefix.isOuter()) {
            cause = "outer";
        } else if (prefix.isExpanded()) {
            cause = "expanded";
        } else if (prefix.isProtected()) {
            cause = "protected";
        }

        prefix.clear();
        throw new UnusedPrefixException(context.esc(cause), token);
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

        if (observersStart != null) {
            observersStart.update(this);
        }

        push(context.getToks("everyjob"));

        execute(new Switch(true));

        typesetter.finish();

        // TTP [1335]
        long groupLevel = context.getGroupLevel();
        if (groupLevel != 0) {
            Localizer loc = getLocalizer();
            String endPrimitive = loc.format("TTP.EndPrimitive");
            String message = loc.format("TTP.EndGroup", context
                    .esc(endPrimitive), Long.toString(groupLevel));
            InterpreterException e = new InterpreterException(message);
            if (observersError != null) {
                observersError.update(e);
            }
            throw e;
        }
        Conditional cond = context.popConditional();
        if (cond != null) {
            Localizer loc = getLocalizer();
            String endPrimitive = loc.format("TTP.EndPrimitive");
            String message = loc.format("TTP.EndIf", context.esc(endPrimitive),
                    context.esc(cond.getPrimitiveName()), cond.getLocator()
                            .toString());
            logger.warning(message);
            InterpreterException e = new InterpreterException(message);
            if (observersError != null) {
                observersError.update(e);
            }
            throw e;
        }

        if (observersStop != null) {
            observersStop.update(this);
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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitActive(
     *      de.dante.extex.scanner.type.token.ActiveCharacterToken,
     *      java.lang.Object)
     */
    public Object visitActive(final ActiveCharacterToken token,
            final Object ignore) throws InterpreterException {

        Code code = context.getCode(token);
        if (code == null) {
            Count ignoreVoid = context.getCount("ignorevoid");
            if (ignoreVoid.le(Count.ZERO)) {
                throw new UndefinedControlSequenceException(AbstractCode
                        .printable(context, token));
            }
        } else {

            code.execute(prefix, context, this, typesetter);

            if (!(code instanceof ExpandableCode)
                    && !(code instanceof PrefixCode) && prefix.isDirty()) {
                reportDirtyFlag(token);
            }
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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitCr(
     *      de.dante.extex.scanner.type.token.CrToken, java.lang.Object)
     */
    public Object visitCr(final CrToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitEscape(
     *      de.dante.extex.scanner.type.token.ControlSequenceToken,
     *      java.lang.Object)
     */
    public Object visitEscape(final ControlSequenceToken token,
            final Object ignore) throws InterpreterException {

        Code code = context.getCode(token);
        if (observersMacro != null) {
            observersMacro.update(token, code);
        }
        if (code == null) {
            Count ignoreVoid = context.getCount("ignorevoid");
            if (ignoreVoid.le(Count.ZERO)) {
                throw new UndefinedControlSequenceException(AbstractCode
                        .printable(context, token));
            }
        } else {

            code.execute(prefix, context, this, typesetter);

            if (!(code instanceof PrefixCode) && prefix.isDirty()) {
                reportDirtyFlag(token);
            }
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
     * @throws InterpreterException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [1063]"
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLeftBrace(
     *      de.dante.extex.scanner.type.token.LeftBraceToken,
     *      java.lang.Object)
     */
    public Object visitLeftBrace(final LeftBraceToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
        try {
            context.openGroup(GroupType.SIMPLE_GROUP, getLocator(), token);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
        typesetter.leftBrace();

        return null;
    }

    /**
     * This visit method is invoked on a letter token.
     *
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLetter(
     *      de.dante.extex.scanner.type.token.LetterToken,
     *      java.lang.Object)
     */
    public Object visitLetter(final LetterToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
        if (typesetter.letter(context, context.getTypesettingContext(), token
                .getChar(), getLocator())
                && context.getCount("tracinglostchars").gt(Count.ZERO)) {
            logger.info(getLocalizer().format("TTP.MissingChar",
                    token.getChar().toString(),
                    context.getTypesettingContext().getFont().getFontName()));
        }

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
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMacroParam(
     *      de.dante.extex.scanner.type.token.MacroParamToken,
     *      java.lang.Object)
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
     * @param token the first argument to pass is the token to expand.
     * @param ignore the second argument is ignored
     *
     * @return <code>null</code>
     *
     * @throws InterpreterException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [1137]"
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMathShift(
     *      de.dante.extex.scanner.type.token.MathShiftToken,
     *      java.lang.Object)
     */
    public Object visitMathShift(final MathShiftToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
        try {
            typesetter.mathShift(context, this, token);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitOther(
     *      de.dante.extex.scanner.type.token.OtherToken,
     *      java.lang.Object)
     */
    public Object visitOther(final OtherToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
        typesetter.letter(context, //
                context.getTypesettingContext(), //
                token.getChar(), getLocator());
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
     * @throws InterpreterException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [1067]"
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitRightBrace(
     *      de.dante.extex.scanner.type.token.RightBraceToken,
     *      java.lang.Object)
     */
    public Object visitRightBrace(final RightBraceToken token,
            final Object ignore) throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
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
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSpace(
     *      de.dante.extex.scanner.type.token.SpaceToken,
     *      java.lang.Object)
     */
    public Object visitSpace(final SpaceToken token, final Object ignore)
            throws GeneralException {

        try {
            typesetter.addSpace(context.getTypesettingContext(), null);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
        if (prefix.isDirty()) {
            reportDirtyFlag(token);
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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSubMark(
     *      de.dante.extex.scanner.type.token.SubMarkToken,
     *      java.lang.Object)
     */
    public Object visitSubMark(final SubMarkToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }

        try {
            typesetter.subscriptMark(context, this, typesetter, token);
        } catch (TypesetterException e) {
            Throwable t = e.getCause();
            if (t instanceof InterpreterException) {
                throw (InterpreterException) t;
            }
            throw e;
        }

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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSupMark(
     *      de.dante.extex.scanner.type.token.SupMarkToken,
     *      java.lang.Object)
     */
    public Object visitSupMark(final SupMarkToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }

        try {
            typesetter.superscriptMark(context, this, typesetter, token);
        } catch (TypesetterException e) {
            Throwable t = e.getCause();
            if (t instanceof InterpreterException) {
                throw (InterpreterException) t;
            }
            throw e;
        }

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
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.scanner.type.token.TokenVisitor#visitTabMark(
     *      de.dante.extex.scanner.type.token.TabMarkToken,
     *      java.lang.Object)
     */
    public Object visitTabMark(final TabMarkToken token, final Object ignore)
            throws InterpreterException {

        if (prefix.isDirty()) {
            reportDirtyFlag(token);
        }
        try {
            typesetter.tab(context, this, token);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }

        return null;
    }

}
