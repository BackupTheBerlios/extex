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
package de.dante.extex.interpreter.max;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Iterator;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.i18n.GeneralPanicException;
import de.dante.extex.i18n.Messages;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextFactory;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.ActiveCharacterToken;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.typesetter.Mode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.NotObservableException;
import de.dante.util.Switch;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.file.FileFinder;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;
import de.dante.util.observer.SwitchObserver;

/**
 * This is a reference implementation for a <b>MA</b>cro e<b>X</b>pander.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class Max extends Moritz implements Interpreter,
        TokenSource, Observable, CatcodeVisitor {

    /**
     * The field <tt>CLASS_ATTRIBUTE</tt> contains the name of the atrtribute
     * to be used to extract the class name foprm the configuration.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The constant <tt>MAX_ERRORS_DEFAULT</tt> contains the default value
     * for maximal allowed number of errors after which the ExTeX run is
     * automatically terminated.
     */
    private static final int MAX_ERRORS_DEFAULT = 100;

    /**
     * The field <tt>calendar</tt> contains the time and date when ExTeX has
     * been started.
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     * The field <tt>context</tt> contains the processing context. Here
     * nearly all relevant information can be found.
     */
    private Context context = null;

    /**
     * The error handler is invoked whenever an error is detected. If none is
     * registered then the default behaviour is shown.
     */
    private ErrorHandler errorHandler = null;

    /**
     * This is the prefix for the next invocations.
     */
    private Flags prefix = new Flags();

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when an error occurs. The argument is the
     * exception encountered.
     */
    private ObserverList observersError = new ObserverList();

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when a new token is about to be expanded. The
     * argument is the token to be expanded.
     */
    private ObserverList observersExpand = new ObserverList();

    /**
     * This observer list is used for the observers which are registered to
     * receive a notification when a macro is expanded.
     */
    private ObserverList observersMacro = new ObserverList();

    /**
     * The field <tt>typesetter</tt> contains the typesetter for handling
     * "left-over" material.
     */
    private Typesetter typesetter = null;

    /**
     * The field <tt>errorCount</tt> contains the count for the number of
     * errors already encountered.
     */
    private int errorCount = 0;

    /**
     * The field <tt>maxErrors</tt> contains the number of errors after which
     * the run is terminated. This value can be overwritten in the
     * configuration.
     */
    private int maxErrors = MAX_ERRORS_DEFAULT;

    /**
     * Creates a new object.
     *
     * @param config the configuration object to take into account
     *
     * @throws ConfigurationException in case of an error
     * @throws GeneralException in case of another error
     */
    public Max(final Configuration config)
            throws ConfigurationException, GeneralException {
        super(config);
        //long t = System.currentTimeMillis();
        configure(config);
        //        System.err.println("init: " +
        //                           Long.toString(System.currentTimeMillis() -
        //                                         t) + "ms");
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#getContext()
     */
    public Context getContext() {
        return context;
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
     * Getter for the error handler. The error handler might not be set. In
     * this case <code>null</code> is returned.
     *
     * @return the error handler currently registered
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Setter for the file finder.
     *
     * @param fileFinder the new file finder
     */
    public void setFileFinder(final FileFinder fileFinder) {
        //finder = fileFinder;
    }

    /**
     * Setter for the interaction mode.
     *
     * @param interaction the interaction mode
     */
    public void setInteraction(final Interaction interaction) {
        context.setInteraction(interaction);
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
     * @see de.dante.extex.interpreter.Interpreter#setJobname(java.lang.String)
     */
    public void setJobname(final String jobname) throws GeneralException {
        context.setToks("jobname", new Tokens(context, jobname), true);
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#setTypesetter(de.dante.extex.typesetter.Typesetter)
     */
    public void setTypesetter(final Typesetter theTypesetter) {
        this.typesetter = theTypesetter;
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#loadFormat(java.lang.String)
     */
    public void loadFormat(final String format) {
        // TODO unimplemented
    }

    /**
     * This method can be used to register observers for certain events.
     * 
     * The following events are currently supported: <table>
     * <tr>
     * <th>Name</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>error</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>expand</td>
     * <td></td>
     * </tr>
     * <tr>
     * <td>pop</td>
     * <td>inherited from the super class</td>
     * </tr>
     * <tr>
     * <td>push</td>
     * <td>inherited from the super class</td>
     * </tr>
     * <tr>
     * <td>EOF</td>
     * <td>inherited from the super class</td>
     * </tr>
     * </table>
     *
     * @see de.dante.util.observer.Observable#registerObserver(java.lang.String,
     *      de.dante.util.Observer)
     */
    public void registerObserver(final String name, final Observer observer)
        throws NotObservableException {
        if ("expand".equals(name)) {
            observersExpand.add(observer);
        } else if ("macro".equals(name)) {
            observersExpand.add(observer);
        } else if ("error".equals(name)) {
            observersError.add(observer);
        } else {
            super.registerObserver(name, observer);
        }
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#run()
     */
    public void run() throws ConfigurationException, GeneralException {
        if (typesetter == null) {
            throw new NoTypesetterException(getClass().getName()
                                                         + "#run()");
        }

        if (getTokenStreamFactory() == null) {
            throw new NoTokenStreamFactoryException(getClass()
                .getName()
                                                                 + "#run()");
        }

        Tokens toks = context.getToks("everyjob");
        if (toks != null) {
            push(toks);
        }

        execute(new Switch(true));
        typesetter.finish(context);

        //TODO TTP[1335]
    }

    /**
     * ...
     *
     * @param onceMore ...
     *
     * @throws GeneralException in case of an error
     */
    private void execute(final Switch onceMore)
            throws GeneralException {

        for (Token current = getToken(); //
                current != null && onceMore.isOn(); //
                current = getToken()) {
            observersExpand.update(this, current);
            try {
                current.getCatcode().visit(this, current, null, null);
            } catch (GeneralPanicException e) {
                throw e; //TODO report the problem and terminate
            } catch (GeneralException e) {
                if (++errorCount > maxErrors) { // cf. TTP[82]
                    throw new GeneralPanicException("TTP.ErrorLimitReached",
                            Integer.toString(maxErrors));
                } else if (errorHandler != null) {
                    errorHandler.handleError(e, current, this, context);
                } else {
                    throw e;
                }
            } catch (Exception e) {
                throw new GeneralPanicException(e);
            }
        }
    }

    /**
     * Add a token stream and start processing it.
     *
     * @param stream the input stream to consider
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws GeneralException in case of another error
     *
     * @see #run()
     */
    public void run(final TokenStream stream) throws ConfigurationException,
            GeneralException {
        addStream(stream);
        run();
    }

    /**
     * ...
     *
     * @param config ...
     *
     * @throws ConfigurationException ...
     * @throws ConfigurationMissingAttributeException ...
     * @throws ConfigurationInstantiationException ...
     */
    private void configure(final Configuration config)
            throws ConfigurationException, GeneralException {
        if (config == null) {
            throw new ConfigurationMissingException("Interpreter");
        }

        context = new ContextFactory(config.getConfiguration("Context"))
                .newInstance();
        setContext(context);
        context.setInteraction(Interaction.ERRORSTOPMODE, true);

        maxErrors = config.getValueAsInteger("maxErrors", maxErrors);

        Iterator iterator = config.iterator("define");

        while (iterator.hasNext()) {
            Configuration cfg = (Configuration) iterator.next();
            String name = cfg.getAttribute("name");

            if (name == null || name.equals("")) {
                throw new ConfigurationMissingAttributeException("name", cfg);
            }

            String classname = cfg.getAttribute(CLASS_ATTRIBUTE);

            if (classname == null || classname.equals("")) {
                throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE, cfg);
            }

            try {
                Code code = (Code) (Class.forName(classname).getConstructor(
                        new Class[]{String.class})
                        .newInstance(new Object[]{name}));
                code.set(context, cfg.getValue());
                context.setMacro(name, code);
            } catch (IllegalArgumentException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (SecurityException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InstantiationException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (IllegalAccessException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (InvocationTargetException e) {
                Throwable c = e.getCause();
                if (c != null && c instanceof ConfigurationException) {
                    throw (ConfigurationException) c;
                }
                throw new ConfigurationInstantiationException(e);
            } catch (NoSuchMethodException e) {
                throw new ConfigurationInstantiationException(e);
            } catch (ClassNotFoundException e) {
                throw new ConfigurationClassNotFoundException(classname,
                        config);
            }
        }

        context.setCount("day", calendar.get(Calendar.DAY_OF_MONTH));
        context.setCount("month", calendar.get(Calendar.MONTH));
        context.setCount("year", calendar.get(Calendar.YEAR));
        context.setCount("time", calendar.get(Calendar.HOUR_OF_DAY) * 60
                                 + calendar.get(Calendar.MINUTE));
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#expand(de.dante.extex.scanner.Token)
     */
    protected Token expand(final Token token) throws GeneralException {
        Code code;

        for (Token t = token; t != null; t = getToken()) { //TODO ???
            if (token instanceof ControlSequenceToken) {
                observersMacro.update(this, token);
                code = context.getMacro(token.getValue());
            } else if (token instanceof ActiveCharacterToken) {
                code = context.getActive(token.getValue());
            } else {
                return token;
            }
            if (code instanceof ExpandableCode) {
                ((ExpandableCode) code).expand(prefix, context, this,
                                               typesetter);
            }
            return token;
        }
        return token;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#executeGroup()
     */
    public void executeGroup() throws GeneralException {
        Switch b = new Switch(true);
        context.afterGroup(new SwitchObserver(b, false));
        execute(b);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitActive(final Object oToken, final Object ignore, final Object ignore2)
    throws GeneralException {
        Token token = (Token) oToken;
        Code code = context.getActive(token.getValue());
        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken", //
                token.toString());
        }
        code.execute(prefix, context, this, typesetter);
        return null;
    }

    /**
     * A comment is ignored. This should never happen since comments are eaten
     * up in the scanner already.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitComment(final Object arg1, final Object ignore, final Object ignore2)
            throws GeneralException {

        throw new GeneralPanicException(Messages.format("TTP.Confusion",
                                                        getClass().getName()));
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitCr(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {
        //Token token = (Token) oToken;
        //TODO unimplemented
        throw new GeneralException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitEscape(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        Token token = (Token) oToken;
        observersMacro.update(this, token);
        Code code = context.getMacro(token.getValue());
        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken", //
                token.toString());
        }
        code.execute(prefix, context, this, typesetter);
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitIgnore(final Object oToken, final Object ignore, final Object ignore2) {
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitInvalid(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        throw new GeneralHelpingException("TTP.InvalidChar", ((Token) oToken)
                                          .getValue());
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     * @see "TeX -- The Program [1063]"
     */
    public Object visitLeftBrace(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        try {
            context.openGroup();
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitLetter(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        Token token = (Token) oToken;
        typesetter.add(context.getTypesettingContext(), token.getChar());
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitMacroParam(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        throw new GeneralHelpingException("TTP.CantUseIn", ((Token) oToken)
                .toString(), typesetter.getMode().toString());
    }

    /**
     * @see "TeX -- The Program [1137]"
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitMathShift(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        if (typesetter.getMode() == Mode.MATH) {
            typesetter.toggleMath();
            return null;
        }

        Token next = getToken();

        if (next == null) {
            // throw new GeneralException("Missing $ inserted"); //TODO i18n
        } else if (next.isa(Catcode.MATHSHIFT)) {
            typesetter.toggleDisplaymath();
        } else {
            push(next);
            typesetter.toggleMath();
        }

        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitOther(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        Token token = (Token) oToken;
        typesetter.add(context.getTypesettingContext(), token.getChar());
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(java.lang.Object, java.lang.Object)
     * @see "TeX -- The Program [1067], java.lang.Object"
     */
    public Object visitRightBrace(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        context.closeGroup(typesetter);
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitSpace(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        typesetter.addSpace(context.getTypesettingContext(), null);
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitSubMark(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        //Token token = (Token) oToken;

        //TODO unimplemented
        throw new GeneralException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitSupMark(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        //Token token = (Token) oToken;

        //TODO unimplemented
        throw new GeneralException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitTabMark(final Object oToken, final Object ignore, final Object ignore2)
            throws GeneralException {

        //Token token = (Token) oToken;

        //TODO unimplemented
        throw new GeneralException("unimplemented");
    }


}
