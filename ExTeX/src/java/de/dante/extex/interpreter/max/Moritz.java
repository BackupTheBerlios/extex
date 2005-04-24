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

import java.util.ArrayList;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.ErrorLimitException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.primitives.register.toks.ToksParameter;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CsConvertible;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.ControlSequenceToken;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.OtherToken;
import de.dante.extex.scanner.type.RightBraceToken;
import de.dante.extex.scanner.type.SpaceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.observer.NotObservableException;
import de.dante.util.observer.Observable;
import de.dante.util.observer.Observer;
import de.dante.util.observer.ObserverList;

/**
 * This class provides the layer above the input streams and the tokenizer. It
 * has additional methods for parsing. The details of the token streams are
 * mostly hidden.
 *
 * <p>
 * This class is the companion to Max. (The name is a joke for friends of
 * Wilhelm Busch)
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.62 $
 */
public abstract class Moritz
        implements
            TokenSource,
            Configurable,
            Localizable,
            Observable {

    /**
     * The constant <tt>MAX_CHAR_CODE</tt> contains the maximum value for a
     * character code. In original TeX this value would be 255.
     */
    private static final long MAX_CHAR_CODE = Long.MAX_VALUE;

    /**
     * The field <tt>extendedRegisterNames</tt> contains the indicator that the
     * extended definition for register names should be used. If it is
     * <code>false</code> then only numbers are permitted.
     */
    private boolean extendedRegisterNames = false;

    //TODO gene: find a good value

    /**
     * The field <tt>localizer</tt> contains the localizer to use.
     */
    private transient Localizer localizer = null;

    /**
     * The field <tt>observersCloseStream</tt> contains the observer list is
     * used for the observers which are registered to receive notifications when
     * a stream is closed.
     */
    private ObserverList observersCloseStream = new ObserverList();

    /**
     * The field <tt>observersEOF</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when all
     * streams are at their end. The argument is always <code>null</code>.
     */
    private ObserverList observersEOF = new ObserverList();

    /**
     * The field <tt>observersLogMessage</tt> contains the observer list is
     * used for the observers which are registered to receive notifications when
     * a log message is send from another component. This message shuld be made
     * accessible to the user in some way, e.g. in the log file.
     */
    //private ObserverList observersLogMessage = new ObserverList();
    /**
     * The field <tt>observersPop</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when a new
     * token is about to be delivered. The argument is the token to be
     * delivered.
     */
    private ObserverList observersPop = new ObserverList();

    /**
     * The field <tt>observersPush</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when a new
     * token is pushed back to the input stream. The argument is the token to be
     * pushed.
     */
    private ObserverList observersPush = new ObserverList();

    /**
     * The field <tt>skipSpaces</tt> contains the indicator that space tokens
     * should be discarted before the next token is delivered.
     */
    private boolean skipSpaces = false;

    /**
     * The field <tt>stream</tt> contains the current stream to read tokens
     * from. For efficiency this stream is kept in a variable instead of
     * accessing the streamStack each time it is needed.
     */
    private TokenStream stream = null;

    /**
     * The field <tt>streamStack</tt> contains the stack of streams to read
     * from except of the current one which is stored in the variable
     * <code>stream</code>.
     */
    private ArrayList streamStack = new ArrayList();

    /**
     * The field <tt>tokenStreamFactory</tt> contains the factory for new
     * token streams.
     */
    private TokenStreamFactory tokenStreamFactory = null;

    /**
     * Creates a new object.
     */
    public Moritz() {

        super();
    }

    /**
     * Put a given stream on top of the stream stack. The reading occurs on this
     * new stream before resorting to the previous streams.
     *
     * @param theStream the new stream to read from
     */
    public void addStream(final TokenStream theStream) {

        streamStack.add(this.stream);
        this.stream = theStream;
    }

    /**
     * Close the topmost stream and pop another one to the top if one is left.
     * If the closed stream has been a file stream then the tokens from the
     * toks register <tt>everyeof</tt> is inserted into the token stream.
     *
     * @param context the interpreter context
     *
     * @return <code>true</code> iff the closed file has been a file stream
     *
     * @throws InterpreterException in case of an error
     */
    private boolean closeStream(final Context context)
            throws InterpreterException {

        try {
            observersCloseStream.update(this, stream);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
        boolean isFile = stream.isFileStream();
        int last = streamStack.size() - 1;
        stream = (last >= 0 ? (TokenStream) streamStack.remove(last) : null);
        if (isFile) {
            push(context.getToks(ToksParameter.getKey("everyeof", context)));
            return true;
        }
        return false;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeAllStreams(Context)
     */
    public void closeAllStreams(final Context context)
            throws InterpreterException {

        while (stream != null) {
            closeStream(context);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeNextFileStream(Context)
     */
    public void closeNextFileStream(final Context context)
            throws InterpreterException {

        while (stream != null) {
            if (closeStream(context)) {
                return;
            }
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        Configuration cfg = config.findConfiguration("ExtendedRegisterNames");
        if (cfg != null) {
            extendedRegisterNames = Boolean.valueOf(cfg.getValue())
                    .booleanValue();
        }

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
     * @see de.dante.extex.interpreter.TokenSource#execute(
     *      de.dante.extex.scanner.type.Token, Context, Typesetter)
     */
    public abstract void execute(final Token token, final Context context,
            final Typesetter typesetter)
            throws InterpreterException,
                ErrorLimitException;

    /**
     * Tries to expand a token. If the given token is expandable then it is
     * recursively expanded and the result is pushed. The first not-expandable
     * token is returned.
     *
     * @param token the Token to expand
     *
     * @return the next non-expandable token or <code>null</code>
     *
     * @throws InterpreterException in case of an error
     */
    protected abstract Token expand(final Token token)
            throws InterpreterException;

    /**
     * @see de.dante.extex.interpreter.TokenSource#getBox(
     *      Context, de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final Typesetter typesetter)
            throws InterpreterException {

        Token t = getToken(context);
        if (!(t instanceof CodeToken)) {
            throw new HelpingException(localizer, "TTP.BoxExpected");
        }
        Code code = context.getCode((CodeToken) t);
        if (!(code instanceof Boxable)) {
            throw new HelpingException(localizer, "TTP.BoxExpected");
        }
        Box box = ((Boxable) code).getBox(context, this, typesetter);

        return box;
    }

    /**
     * @see de.dante.extex.interpreter.Interpreter#getContext()
     */
    protected abstract Context getContext();

    /**
     * Get the next token from the token stream and check that it is a
     * control sequence or active character.
     * At the end of all input streams the control sequence "inaccessible"
     * is insered and an exception is thrown. Thus thismethod will never return
     * <code>null</code>.
     *
     * @param context the interpreter context
     *
     * @return the token read
     *
     * @throws InterpreterException in case that the token stream is at its
     *  end or that the token read is not a control sequence token
     */
    public CodeToken getControlSequence(final Context context)
            throws InterpreterException {

        Token t = getToken(context);

        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code instanceof CsConvertible) {
                t = ((CsConvertible) code).convertCs(context, this);
            }
            return (CodeToken) t;

        }
        try {
            push(context.getTokenFactory().createToken(Catcode.ESCAPE,
                    new UnicodeChar(context.esc("")), "inaccessible ",
                    context.getNamespace()));
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
        push(t);
        throw new HelpingException(localizer, "TTP.MissingCtrlSeq");
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getFont(Context)
     */
    public Font getFont(final Context context) throws InterpreterException {

        Token t = getToken(context);

        if (t == null) {
            throw new EofException(null);
        } else if (!(t instanceof CodeToken)) {
            throw new HelpingException(localizer, "TTP.MissingFontIdent");
        }
        Code code = context.getCode((CodeToken) t);
        if (code == null) {
            throw new UndefinedControlSequenceException(t.toString());
        } else if (!(code instanceof FontConvertible)) {
            throw new HelpingException(localizer, "TTP.MissingFontIdent");
        }

        return ((FontConvertible) code).convertFont(context, this);
    }

    /**
     * Scan the expanded token stream for a sequence of letter tokens. If all
     * tokens are found then they are removed from the input stream and
     * <code>true</code> is returned. Otherwise all tokens are left in the
     * input stream and <code>false</code> is returned.
     *
     * @param context the interpreter context
     * @param s the tokens to scan
     *
     * @return <code>true</code> iff the tokens could have been successfully
     *  removed from the input stream
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     */
    public boolean getKeyword(final Context context, final String s)
            throws InterpreterException {

        skipSpaces = true;
        if (getKeyword(context, s, 0)) {
            skipSpaces = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Scans the input token stream for a given sequence of tokens. Those tokens
     * may have the catcodes <tt>LETTER</tt> or <tt>OTHER</tt>.
     *
     * @param context the interprter context
     * @param s the string to use as reference
     * @param i the index in s to start working at
     *
     * @return <code>true</code> iff the keyword has been found
     *
     * @throws InterpreterException in case of an error
     */
    private boolean getKeyword(final Context context, final String s,
            final int i) throws InterpreterException {

        if (i < s.length()) {
            Token t = getToken(context);

            if (t == null) {
                return false;
            } else if (!(t.equals(Catcode.LETTER, s.charAt(i)) //
                    || t.equals(Catcode.OTHER, s.charAt(i)))
                    || !getKeyword(context, s, i + 1)) {
                stream.put(t);
                return false;
            }

        }
        return true;
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
     * @see de.dante.extex.interpreter.TokenSource#getLocator()
     */
    public Locator getLocator() {

        return (stream == null ? null : stream.getLocator());
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getNonSpace(
     *      de.dante.extex.interpreter.context.Context)
     */
    public Token getNonSpace(final Context context) throws InterpreterException {

        skipSpaces = true;
        return getToken(context);
    }

    /**
     * Scan a number with a given first token.
     *
     * @param token the first token to consider
     *
     * @return the value of the integer scanned
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     * @throws MissingNumberException 
     *  in case that no number is found or the end of file has been
     *  reached before an integer could be acquired
     */
    public long getNumber(final Token token)
            throws InterpreterException,
                MissingNumberException {

        Context context = getContext();
        long n = 0;
        Token t;

        if (token != null && token.isa(Catcode.OTHER)) {
            switch (token.getChar().getCodePoint()) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    n = token.getChar().getCodePoint() - '0';

                    for (t = getToken(context); t != null
                            && t.isa(Catcode.OTHER) && t.getChar().isDigit(); //
                    t = getToken(context)) {
                        n = n * 10 + t.getChar().getCodePoint() - '0';
                    }

                    if (t != null) {
                        stream.put(t);
                    }
                    skipSpaces = true;
                    return n;

                case '`':
                    t = getToken(context);

                    if (t instanceof ControlSequenceToken) {
                        String val = ((ControlSequenceToken) t).getName();
                        if (val.length() != 1) {
                            throw new HelpingException(localizer,
                                    "TTP.NonNumericToken", t.toString());
                        }
                        return val.charAt(0);
                    } else if (t != null) {
                        return t.getChar().getCodePoint();
                    }
                    // fall through to error handling
                    break;

                case '\'':
                    for (t = getToken(context); t instanceof OtherToken; //
                    t = getToken(context)) {
                        int no = t.getChar().getCodePoint() - '0';
                        if (no < 0 || no >= 7) {
                            break;
                        }
                        n = n * 8 + no;
                    }

                    stream.put(t);
                    skipSpaces = true;
                    return n;

                case '"':

                    for (t = getToken(context); //
                    t instanceof OtherToken || t instanceof LetterToken; //
                    t = getToken(context)) {
                        int no = t.getChar().getCodePoint();
                        switch (no) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                n = n * 16 + no - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                n = n * 16 + no - 'a' + 10;
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                n = n * 16 + no - 'A' + 10;
                                break;
                            default:
                                stream.put(t);
                                skipSpaces = true;
                                return n;
                        }
                    }

                    stream.put(t);
                    skipSpaces = true;
                    return n;

                default:
            // fall through to error handling
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Skip spaces and if the next non-space character is an equal sign skip it
     * as well and all spaces afterwards.
     *
     * @param context the interpreter context
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
     */
    public void getOptionalEquals(final Context context)
            throws InterpreterException {

        skipSpaces = true;
        Token t = getToken(context);

        if (t == null) {
            return;
        } else if (t.equals(Catcode.OTHER, '=')) {
            skipSpaces = true;
        } else {
            stream.put(t);
        }
    }

    /**
     * Get the next token from the input streams. If the current input stream is
     * at its end then the next one on the streamStack is used until a token
     * could be read. If all stream are at the end then <code>null</code> is
     * returned.
     * <p>
     * Whenever a file stream is closed then the tokens from the
     * </p>
     *
     * @param context the interpreter context
     *
     * @return the next token or <code>null</code>
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#getToken(de.dante.extex.interpreter.context.Context)
     */
    public Token getToken(final Context context) throws InterpreterException {

        TokenFactory factory = context.getTokenFactory();
        Tokenizer tokenizer = context.getTokenizer();
        Token t;

        if (skipSpaces) {

            skipSpaces = false;

            try {
                while (stream != null) {
                    do {
                        t = stream.get(factory, tokenizer);
                        if (t != null) {
                            observersPop.update(this, t);
                        }
                    } while (t != null && t instanceof SpaceToken);

                    if (t != null) {
                        return t;
                    }

                    closeStream(context);
                }
            } catch (InterpreterException e) {
                throw e;
            } catch (GeneralException e) {
                throw new InterpreterException(e);
            }

        } else {

            try {
                while (stream != null) {
                    t = stream.get(factory, tokenizer);
                    if (t != null) {
                        observersPop.update(this, t);
                        return t;
                    }
                    closeStream(context);
                }
            } catch (InterpreterException e) {
                throw e;
            } catch (GeneralException e) {
                throw new InterpreterException(e);
            }
        }

        try {
            observersEOF.update(this, null);
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getTokens()
     */
    public Tokens getTokens(final Context context) throws InterpreterException {

        Tokens toks = new Tokens();
        Token token = getToken(context);

        if (token == null) {
            throw new EofException(localizer.format("Tokens.Text"));
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceException("???");
        }

        int balance = 1;

        for (token = getToken(context); token != null; token = getToken(context)) {

            if (token.isa(Catcode.LEFTBRACE)) {
                ++balance;
            } else if (token instanceof RightBraceToken && --balance <= 0) {
                return toks;
            }

            toks.add(token);
        }

        return toks;
    }

    /**
     * Getter for the token stream factory.
     *
     * @return the token stream factory
     */
    public TokenStreamFactory getTokenStreamFactory() {

        return tokenStreamFactory;
    }

    /**
     * Getter for the typesetter.
     *
     * @return the typesetter
     */
    public abstract Typesetter getTypesetter();

    /**
     * Push back a token onto the input stream for subsequent reading.
     *
     * @param token the token to push
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#push(
     *      de.dante.extex.scanner.type.Token)
     */
    public void push(final Token token) throws InterpreterException {

        try {
            observersPush.update(this, token);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }

        if (stream == null) {
            try {
                stream = getTokenStreamFactory().newInstance("");
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }

        stream.put(token);
    }

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     *
     * @param tokens the tokens to push
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#push(
     *      de.dante.extex.scanner.type.Token[])
     */
    public void push(final Token[] tokens) throws InterpreterException {

        try {
            for (int i = tokens.length - 1; i >= 0; i--) {

                observersPush.update(this, tokens[i]);
                stream.put(tokens[i]);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     * In case that the argument is <code>null</code> then it is silently
     * ignored.
     * <p>
     *  If the current stream is <code>null</code> then all streams are at their
     *  end. In this case a new Token stream is created to hold the tokens.
     * </p>
     *
     * @param tokens the tokens to push
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#push(
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void push(final Tokens tokens) throws InterpreterException {

        if (tokens == null || tokens.length() == 0) {
            return;
        }

        if (stream == null) {
            try {
                stream = getTokenStreamFactory().newInstance("");
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }

        try {
            for (int i = tokens.length() - 1; i >= 0; i--) {
                Token t = tokens.get(i);
                observersPush.update(this, t);
                stream.put(t);
            }
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * This method can be used to register observers for certain events.
     *
     * The following events are currently supported:
     * <table>
     *  <tr>
     *   <th>Name</th>
     *   <th>Description</th>
     *  </tr>
     *  <tr>
     *   <td>pop</td>
     *   <td>This observer is triggered by a pop operation on the token stream.
     *    This means that a token has been extracted.
     *   </td>
     *  </tr>
     *  <tr>
     *   <td>push</td>
     *   <td>This observer is triggered by a push operation on the token stream.
     *    This means that a token has been placed on the current stream for
     *    subsequent reading.
     *   </td>
     *  </tr>
     *  <tr>
     *   <td>EOF</td>
     *   <td>This oberserver is triggered by an end of file on the token stream.
     *    This means that all tokens have been processed and all stream are at
     *    their end.
     *   </td>
     *  </tr>
     *  <tr>
     *   <td>close</td>
     *   <td>This oberserver is triggered by a close on the token stream stack.
     *   </td>
     *  </tr>
     * </table>
     *
     * @see de.dante.util.observer.Observable#registerObserver(java.lang.String,
     *      de.dante.util.observer.Observer)
     */
    public void registerObserver(final String name, final Observer observer)
            throws NotObservableException {

        if ("push".equals(name)) {
            observersPush.add(observer);
        } else if ("pop".equals(name)) {
            observersPop.add(observer);
        } else if ("EOF".equals(name)) {
            observersEOF.add(observer);
        } else if ("close".equals(name)) {
            observersCloseStream.add(observer);
        } else {
            throw new NotObservableException(name);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanCharacterCode(Context)
     */
    public UnicodeChar scanCharacterCode(final Context context)
            throws InterpreterException {

        long cc = scanNumber(context);

        if (cc < 0 || cc > MAX_CHAR_CODE) {
            throw new HelpingException(getLocalizer(), "TTP.BadCharCode", //
                    Long.toString(cc), "0", Long.toString(MAX_CHAR_CODE));
        }

        return new UnicodeChar((int) cc);
    }

    /**
     * Scan the input stream for tokens making up an integer, this is a number
     * optionally preceeded by a sign (+ or -). The number can be preceeded by
     * optional whitespace. Whitespace is also ignored between the sign and the
     * number. All non-whitespace characters must have the catcode OTHER.
     *
     * @param context the interpreter context
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that the end of file has been
     *  reached before an integer could be acquired
     * @throws MissingNumberException in case of a missing number
     *
     * @see de.dante.extex.interpreter.TokenSource#scanInteger(
     *      de.dante.extex.interpreter.context.Context)
     */
    public long scanInteger(final Context context)
            throws InterpreterException,
                MissingNumberException {

        Token t = getNonSpace(context);

        if (t == null) {

            throw new MissingNumberException();

        } else if (t.equals(Catcode.OTHER, '-')) {

            return -scanNumber(context);

        } else if (t.equals(Catcode.OTHER, '+')) {

            return scanNumber(context);

        }

        return scanNumber(context, t);
    }

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     *
     * @param context the interpreter contex
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws InterpreterException
     *  in case of an error in {@link #scanToken(Context) scanToken()}
     */
    public Token scanNonSpace(final Context context)
            throws InterpreterException {

        for (Token t = scanToken(context); t != null; t = scanToken(context)) {

            if (!(t.isa(Catcode.SPACE))) {
                return t;
            }
        }

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanNumber()
     * @deprecated use scanNumber(Context) instead
     */
    public long scanNumber() throws InterpreterException {

        return scanNumber(getContext(), getNonSpace(getContext()));
    }

    /**
     * Scan the input stream for tokens making up a number, this means a
     * sequence of digits with catcode OTHER. Alternative notations for a number
     * may exist. The number can be preceeded by optional whitespace.
     *
     * @param context the interprester contex
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     *
     * @see de.dante.extex.interpreter.TokenSource#scanNumber()
     */
    public long scanNumber(final Context context) throws InterpreterException {

        return scanNumber(context, getNonSpace(context));
    }

    /**
     * Scan a number with a given first token.
     *
     * @param context the interpreter contex
     * @param token the first token to consider
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     *
     * @see de.dante.extex.interpreter.TokenSource#scanNumber(
     *      de.dante.extex.scanner.type.Token)
     */
    public long scanNumber(final Context context, final Token token)
            throws InterpreterException,
                MissingNumberException {

        long n = 0;
        Token t = token;

        while (t != null) {

            if (t instanceof OtherToken) {
                int c = t.getChar().getCodePoint();
                switch (c) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        n = c - '0';

                        for (t = scanToken(context); t instanceof OtherToken
                                && t.getChar().isDigit(); t = scanToken(context)) {
                            n = n * 10 + t.getChar().getCodePoint() - '0';
                        }

                        if (t != null) {
                            stream.put(t);
                        }
                        skipSpaces = true;
                        return n;

                    case '`':
                        t = getToken(context);

                        if (t instanceof ControlSequenceToken) {
                            String s = ((ControlSequenceToken) t).getName();
                            return ("".equals(s) ? 0 : s.charAt(0));
                        } else if (t != null) {
                            return t.getChar().getCodePoint();
                        }
                        // fall through to error handling
                        break;

                    case '\'':
                        for (t = scanToken(context); t instanceof OtherToken; //
                        t = scanToken(context)) {
                            int no = t.getChar().getCodePoint() - '0';
                            if (no < 0 || no > 7) {
                                break;
                            }
                            n = n * 8 + no;
                        }

                        if (t != null) {
                            stream.put(t);
                        }
                        skipSpaces = true;
                        return n;

                    case '"':

                        for (t = scanToken(context); //
                        t instanceof OtherToken || t instanceof LetterToken; //
                        t = scanToken(context)) {
                            int no = t.getChar().getCodePoint();
                            switch (no) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    n = n * 16 + no - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    n = n * 16 + no - 'a' + 10;
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    n = n * 16 + no - 'A' + 10;
                                    break;
                                default:
                                    stream.put(t);
                                    skipSpaces = true;
                                    return n;
                            }
                        }

                        stream.put(t);
                        skipSpaces = true;
                        return n;

                    default:
                        throw new MissingNumberException();
                }
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code == null) {

                    throw new MissingNumberException();

                } else if (code instanceof CountConvertible) {
                    return ((CountConvertible) code).convertCount(context,
                            this, getTypesetter());
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, this,
                            getTypesetter());
                    t = getToken(context);
                } else {

                    throw new MissingNumberException();
                }
            } else {

                throw new MissingNumberException();
            }
        }

        throw new MissingNumberException();
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanRegisterName(Context)
     */
    public String scanRegisterName(final Context context)
            throws InterpreterException {

        skipSpaces = true;
        Token token = getToken(context);

        if (token == null) {

            throw new MissingNumberException();
        }

        if (extendedRegisterNames && token.isa(Catcode.LEFTBRACE)) {
            push(token);
            return scanTokensAsString(context);
        }

        return Long.toString(scanNumber(context, token));
    }

    /**
     * Expand tokens from the input stream until an unexpandable token is found.
     * This unexpandable token is returned.
     *
     * @param context the interpreter contex
     *
     * @return the next unexpandable token
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#scanToken(
     *      de.dante.extex.interpreter.context.Context)
     */
    public Token scanToken(final Context context) throws InterpreterException {

        return expand(getToken(context));
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanTokens(Context)
     */
    public Tokens scanTokens(final Context context) throws InterpreterException {

        Tokens toks = new Tokens();
        skipSpaces = true;
        Token token = getToken(context);

        if (token == null) {
            throw new EofException(null);
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceException(null);
        }

        int balance = 1;

        for (token = scanToken(context); token != null; token = scanToken(context)) {

            if (token.isa(Catcode.LEFTBRACE)) {
                ++balance;
            } else if (token.isa(Catcode.RIGHTBRACE) && --balance <= 0) {
                break;
            }

            toks.add(token);
        }

        return toks;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanTokensAsString()
     */
    public String scanTokensAsString(final Context context)
            throws InterpreterException {

        return scanTokens(context).toText();
    }

    /**
     * Setter for the token stream factory.
     *
     * @param factory the token stream factory
     *
     * @throws ConfigurationException this exception is never thrown. It is
     *  defined here to provide an exit for derived classes
     */
    public void setTokenStreamFactory(final TokenStreamFactory factory)
            throws ConfigurationException {

        tokenStreamFactory = factory;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#skipSpace()
     */
    public void skipSpace() {

        skipSpaces = true;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#update(java.lang.String,
     *      java.lang.String)
     */
    public void update(final String name, final String text)
            throws NotObservableException {

        throw new NotObservableException(name);
    }
}