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
package de.dante.extex.interpreter;

import java.util.Calendar;
import java.util.Iterator;

import de.dante.extex.font.FontFactory;
import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.i18n.GeneralPanicException;
import de.dante.extex.i18n.GeneralTerminateException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextFactory;
import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.context.TypesettingContextImpl;
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
import de.dante.util.Observable;
import de.dante.util.Observer;
import de.dante.util.ObserverList;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationMissingException;
import de.dante.util.file.FileFinder;

/**
 * This is a reference implementation for a <b>MA</b>cro e<b>X</b>pander.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
 */
public class Max extends Moritz implements CatcodeVisitor, Interpreter, TokenSource, Observable {
	/**
	 * This calender instance contains the time and date when ExTeX has been
	 * started.
	 */
	private Calendar calendar = Calendar.getInstance();

	/**
	 * This is the processing context. Here nearly all relevant information can
	 * be found.
	 */
	private Context context = null;

	/**
	 * The error handler is invoked whenever an error is detected. If none is
	 * registered then the default behaviour is shown.
	 */
	private ErrorHandler errorHandler = null;

	/**
	 * This is the prefix for the next invocations
	 */
	private Flags prefix = new Flags();

	/** 
	 * fontfactory
	 */
	private FontFactory fontFactory = null;

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
	 * This is the typesetter for handling "left-over" material.
	 */
	private Typesetter typesetter = null;

	/**
	 * The count for the number of errors already encountered
	 */
	private int errorCount = 0;

	/**
	 * The number of errors after which the run is terminated. This value might
	 * be overwritten in the configuration.
	 */
	private int maxErrors = 100;

	/**
	 * Creates a new object.
	 *
	 * @param config
	 *                 the configuration object to take into account
	 *
	 * @throws ConfigurationException
	 *                 in case of an error
	 */
	public Max(Configuration config, FileFinder fileFinder) throws ConfigurationException, GeneralException {
		super(config);
		finder = fileFinder;
		configure(config);
	}

	/**
	 * filefinder
	 */
	private FileFinder finder = null;
	
	/**
	 * Setter for the file finder.
	 * @param fileFinder	the new filefinder
	 */
	public void setFileFinder(FileFinder fileFinder) {
		finder = fileFinder;
	}
	
	/**
	 * Setter for the error handler. The value of <code>null</code> can be
	 * used to delete the error handler currently set.
	 *
	 * @param handler the new error handler
	 */
	public void setErrorHandler(ErrorHandler handler) {
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
	 * Setter for the interaction mode.
	 *
	 * @param interaction the interaction mode
	 */
	public void setInteraction(Interaction interaction) {
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
	public void setJobname(String jobname) {
		// TODO unimplemented
	}

	/**
	 * @see de.dante.extex.interpreter.Interpreter#setTypesetter(de.dante.extex.typesetter.Typesetter)
	 */
	public void setTypesetter(Typesetter typesetter) {
		this.typesetter = typesetter;
	}

	/**
	 * @see de.dante.extex.interpreter.Interpreter#loadFormat(java.lang.String)
	 */
	public void loadFormat(String format) {
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
	 * @see de.dante.util.Observable#registerObserver(java.lang.String,
	 *         de.dante.util.Observer)
	 */
	public void registerObserver(String name, Observer observer) throws NotObservableException {
		if ("expand".equals(name)) {
			observersExpand.add(observer);
		} else if ("error".equals(name)) {
			observersError.add(observer);
		} else {
			super.registerObserver(name, observer);
		}
	}

	/**
	 * Process the current token streams by repeatedly reading a single token
	 * and processing it until no token is left. The visitor pattern is used to
	 * branch to the appropriate method for processing a single token. E.g. the
	 * method
	 * {@link #visitActive(java.lang.Object,java.lang.Object) visitActive}is
	 * used when the current token is an active character.
	 *
	 * @throws ConfigurationException
	 *                 in case of a configuration error
	 */
	public void run() throws ConfigurationException, GeneralException {
		if (typesetter == null) {
			throw new ConfigurationNoTypesetterException(this.getClass().getName());
		}

		if (getTokenStreamFactory() == null) {
			throw new ConfigurationException(this.getClass().getName());

			//TODO: use new exception
		}

		for (Token current = getToken(); current != null; current = getToken()) {
			observersExpand.update(this, current);

			try {
				current.getCatcode().visit(this, current, null);
			} catch (GeneralPanicException e) {
				throw e; //TODO report the problem and terminate
			} catch (GeneralTerminateException e) {
				break;
			} catch (GeneralException e) {
				if (++errorCount > maxErrors) { // cf. TTP[82]
					throw new GeneralPanicException("TTP.ErrorLimitReached", Integer.toString(maxErrors));
				} else if (errorHandler != null) {
					errorHandler.handleError(e, current, this, context);
				} else {
					throw e;
				}
			} catch (Exception e) {
				throw new GeneralException(e);
			}
		}

		typesetter.finish();

		//TODO TTP[1335]
	}

	/**
	 * Add a token stream and start processing it.
	 *
	 * @param stream the input stream to consider
	 *
	 * @throws ConfigurationException
	 *                 in case of a configuration error
	 *
	 * @see #run()
	 */
	public void run(TokenStream stream) throws ConfigurationException, GeneralException {
		addStream(stream);
		run();
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,java.lang.Object)
	 */
	public Object visitActive(Object oToken, Object arg2) throws GeneralException {
		Token token = (Token) oToken;
		return execute(token, context.getActive(token.getValue()));
	}

	/**
	 * A comment is ignored. This should never happen since comments are eaten
	 * up in the scanner already.
	 *
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,java.lang.Object)
	 */
	public Object visitComment(Object arg1, Object ignore) throws GeneralException {
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,java.lang.Object)
	 */
	public Object visitCr(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;
		//TODO unimplemented
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,java.lang.Object)
	 */
	public Object visitEscape(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;
		return execute(token, context.getMacro(token.getValue()));
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,java.lang.Object)
	 */
	public Object visitIgnore(Object oToken, Object ignore) throws GeneralException {
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,java.lang.Object)
	 */
	public Object visitInvalid(Object oToken, Object ignore) throws GeneralException {
		throw new GeneralHelpingException("TTP.InvalidChar", ((Token) oToken).getValue());
	}

	/**
	 * @see "TeX -- The Program [1063]"
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,java.lang.Object)
	 */
	public Object visitLeftBrace(Object oToken, Object ignore) throws GeneralException {
		try {
			context.openGroup();
		} catch (ConfigurationException e) {
			throw new GeneralException(e);
		}

		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,java.lang.Object)
	 */
	public Object visitLetter(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;
		typesetter.add(context.getTypesettingContext(), token.getChar());
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,java.lang.Object)
	 */
	public Object visitMacroParam(Object oToken, Object ignore) throws GeneralException {
		throw new GeneralHelpingException("TTP.CantUseIn", ((Token) oToken).toString(), typesetter.getMode().toString());
	}

	/**
	 * @see "TeX -- The Program [1137]"
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
	 */
	public Object visitMathShift(Object oToken, Object ignore) throws GeneralException {
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
			this.push(next);
			typesetter.toggleMath();
		}

		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
	 */
	public Object visitOther(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;
		typesetter.add(context.getTypesettingContext(), token.getChar());
		return null;
	}

	/**
	 * @see "TeX -- The Program [1067]"
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitRigthBrace(java.lang.Object,java.lang.Object)
	 */
	public Object visitRightBrace(Object oToken, Object ignore) throws GeneralException {
		//TODO
		context.closeGroup();
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
	 */
	public Object visitSpace(Object oToken, Object ignore) throws GeneralException {
		typesetter.addSpace(context.getTypesettingContext(), null);
		return null;
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
	 */
	public Object visitSubMark(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;

		//TODO unimplemented
		throw new GeneralException("unimplemented");
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
	 */
	public Object visitSupMark(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;

		//TODO unimplemented
		throw new GeneralException("unimplemented");
	}

	/**
	 * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
	 */
	public Object visitTabMark(Object oToken, Object ignore) throws GeneralException {
		Token token = (Token) oToken;

		//TODO unimplemented
		throw new GeneralException("unimplemented");
	}

	/**
	 * ...
	 *
	 * @param config
	 *                 ...
	 *
	 * @throws ConfigurationException
	 *                 ...
	 * @throws ConfigurationMissingAttributeException
	 *                 ...
	 * @throws ConfigurationInstantiationException
	 *                 ...
	 */
	private void configure(Configuration config) throws ConfigurationException, GeneralException {
		long t = System.currentTimeMillis();
		if (config == null) {
			throw new ConfigurationMissingException("Interpreter");
		}

		context = new ContextFactory(config.getConfiguration("Context")).newInstance();
		setContext(context);
		context.setInteraction(Interaction.ERRORSTOPMODE);

		// FontFactroy
		Configuration fontcfg = config.getConfiguration("Font");
		String fontclass = fontcfg.getAttribute("class");

		if (fontclass == null || fontclass.equals("")) {
			throw new ConfigurationMissingAttributeException("classname");
		}

		try {
			fontFactory = (FontFactory) (Class.forName(fontclass).getConstructor(new Class[] { FileFinder.class }).newInstance(new Object[] { finder }));
		} catch (Exception e) {
			throw new ConfigurationInstantiationException(e);
		}

		TypesettingContext typesettingContext = new TypesettingContextImpl();
		typesettingContext.setFont(fontFactory.getInstance(config.getValue("Font")));

		//typesettingContext.setLanguage(config.getValue("Language"));
		context.setTypesettingContext(typesettingContext);

		maxErrors = config.getValueAsInteger("maxErrors", 100);

		Iterator iterator = config.iterator("define");

		while (iterator.hasNext()) {
			Configuration cfg = (Configuration) iterator.next();
			String name = cfg.getAttribute("name");

			if (name == null || name.equals("")) {
				throw new ConfigurationMissingAttributeException("name");
			}

			String classname = cfg.getAttribute("class");

			if (classname == null || classname.equals("")) {
				throw new ConfigurationMissingAttributeException("classname");
			}

			try {
				Code code = (Code) (Class.forName(classname).getConstructor(new Class[] { String.class }).newInstance(new Object[] { name }));
				code.set(context, cfg.getValue());
				context.setMacro(name, code);
			} catch (Exception e) {
				throw new ConfigurationInstantiationException(e);
			}
		}

		context.setCount("day", calendar.get(Calendar.DAY_OF_MONTH));
		context.setCount("month", calendar.get(Calendar.MONTH));
		context.setCount("year", calendar.get(Calendar.YEAR));
		context.setCount("time", calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE));

		//        System.err.println("init: " +
		//                           Long.toString(System.currentTimeMillis() -
		//                                         t) + "ms");
	}

	/**
	 * ...
	 *
	 * @param token the token which is about to be expanded (for error reporting
	 *                 purposes)
	 * @param code the definition to expand
	 *
	 * @return <code>null</code>
	 */
	private Object execute(Token token, Code code) throws GeneralException {
		if (code == null) {
			throw new GeneralHelpingException("TTP.UndefinedToken", token.toString());
		}

		code.execute(prefix, context, this, typesetter);

		return null;
	}

	/**
	 * @see de.dante.extex.interpreter.Moritz#expand(de.dante.extex.scanner.Token)
	 */
	protected Token expand(Token token) throws GeneralException {
		Code code;

		while (token == null) {
			if (token instanceof ControlSequenceToken) {
				code = context.getMacro(token.getValue());
			} else if (token instanceof ActiveCharacterToken) {
				code = context.getMacro(token.getValue());
			} else {
				return token;
			}
			if (!code.expand(prefix, context, this, typesetter)) {
				return token;
			}
			token = getToken();
		}
		return token;
	}

}
