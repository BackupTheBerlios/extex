/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.i18n.EofHelpingException;
import de.dante.extex.i18n.HelpingException;
import de.dante.extex.i18n.MissingLeftBraceHelpingException;
import de.dante.extex.i18n.PanicException;
import de.dante.extex.i18n.UndefinedControlSequenceHelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CsConvertible;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.OtherToken;
import de.dante.extex.scanner.RightBraceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
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
 * @version $Revision: 1.45 $
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

    //TODO: find a good value

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
    private ObserverList observersLogMessage = new ObserverList();

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
     * @param theStream
     *            the new stream to read from
     */
    public void addStream(final TokenStream theStream) {

        streamStack.add(this.stream);
        this.stream = theStream;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeAllStreams()
     */
    public void closeAllStreams() throws GeneralException {

        while (stream != null) {
            observersCloseStream.update(this, stream);
            int last = streamStack.size() - 1;
            stream = (last >= 0 ? (TokenStream) streamStack.remove(last) : null);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeNextFileStream()
     */
    public void closeNextFileStream() throws GeneralException {

        while (stream != null) {
            observersCloseStream.update(this, stream);
            if (stream.isFileStream()) {
                int last = streamStack.size() - 1;
                stream = (last >= 0
                        ? (TokenStream) streamStack.remove(last)
                        : null);
                return;
            }
            int last = streamStack.size() - 1;
            stream = (last >= 0 ? (TokenStream) streamStack.remove(last) : null);
        }
    }

    /**
     * The field <tt>extendedRegisterNames</tt> contains the indicator that the
     * extended definition for register names should be used. If it is
     * <code>false</code> then only numbers are permitted.
     */
    private boolean extendedRegisterNames = false;

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        Configuration cfg = config.findConfiguration("ExtendedRegisterNames");
        if (cfg != null) {
            extendedRegisterNames = Boolean.getBoolean(cfg.getValue());
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
     * Tries to expand a token. If the given token is expandable then it is
     * recursively expanded and the result is pushed. The first not-expandable
     * token is returned.
     *
     * @param token the Token to expand
     *
     * @return the next non-expandable token or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    protected abstract Token expand(final Token token) throws GeneralException;

    public abstract void execute(final Token token) throws GeneralException;

    /**
     * @see de.dante.extex.interpreter.TokenSource#getBox(
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Typesetter typesetter) throws GeneralException {

        Token t = getToken();
        if (!(t instanceof CodeToken)) {
            throw new HelpingException(localizer, "TTP.BoxExpected");
        }
        Context context = getContext();
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
    public abstract Context getContext();

    /**
     * Get the next token from the token stream and check that it is a
     * control sequence or active character.
     * At the end of all input streams the control sequence "inaccessible"
     * is insered and an exception is thrown. Thus thismethod will never return
     * <code>null</code>.
     *
     * @return the token read
     *
     * @throws GeneralException in case that the token stream is at its end or
     *   that the token read is not a control sequence token
     */
    public CodeToken getControlSequence() throws GeneralException {

        Token t = getToken();
        Context context = getContext();

        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code != null && code instanceof CsConvertible) {
                t = ((CsConvertible) code).convertCs(context, this);
            }
            return (CodeToken) t;

        }
        push(context.getTokenFactory().createToken(Catcode.ESCAPE,
                "inaccessible ", context.getNamespace()));
        push(t);
        throw new HelpingException(localizer, "TTP.MissingCtrlSeq");
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getFont()
     */
    public Font getFont() throws GeneralException {

        Token t = getToken();
        Context context = getContext();

        if (t == null) {
            throw new EofHelpingException(null);
        } else if (!(t instanceof CodeToken)) {
            throw new HelpingException(localizer, "TTP.MissingFontIdent");
        }
        Code code = context.getCode((CodeToken) t);
        if (code == null) {
            throw new UndefinedControlSequenceHelpingException(t.toString());
        } else if (!(code instanceof FontConvertible)) {
            throw new HelpingException(localizer, "TTP.MissingFontIdent");
        }

        return ((FontConvertible) code).convertFont(context, this);
    }

    /**
     * Scan the input stream for tokens making up an integer, this is a number
     * optionally preceeded by a sign (+ or -). The number can be preceeded by
     * optional whitespace. Whitespace is also ignored between the sign and the
     * number. All non-whitespace characters must have the catcode OTHER.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException
     *  in case that no number is found or the end of file has been
     *  reached before an integer could be acquired
     */
    public long getInteger() throws GeneralException {

        Token t = getNonSpace();

        if (t == null) {
            throw new HelpingException(localizer, "TTP.MissingNumber");
        } else if (t.equals(Catcode.OTHER, "-")) {
            return -scanNumber();
        } else if (!t.equals(Catcode.OTHER, "+")) {
            stream.put(t);
        }

        return scanNumber();
    }

    /**
     * Scan the expanded token stream for a sequence of letter tokens. If all
     * tokens are found then they are removed from the input stream and
     * <code>true</code> is returned. Otherwise all tokens are left in the
     * input stream and <code>false</code> is returned.
     *
     * @param s the tokens to scan
     *
     * @return <code>true</code> iff the tokens could have been successfully
     *  removed from the input stream
     *
     * @throws GeneralException
     *  in case that no number is found or the end of file has been
     *  reached before an integer could be acquired
     */
    public boolean getKeyword(final String s) throws GeneralException {

        skipSpace();
        if (getKeyword(s, 0)) {
            skipSpace();
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getKeyword(java.lang.String,
     *      boolean)
     */
    public boolean getKeyword(final String s, final boolean space)
            throws GeneralException {

        if (space) {
            skipSpace();
        }
        return getKeyword(s);
    }

    /**
     * Scans the input token stream for a given sequence of tokens. Those tokens
     * may have the catcodes <tt>LETTER</tt> or <tt>OTHER</tt>.
     *
     * @param s the string to use as reference
     * @param i the index in s to start working at
     *
     * @return <code>true</code> iff the keyword has been found
     *
     * @throws GeneralException in case of an error
     */
    private boolean getKeyword(final String s, final int i)
            throws GeneralException {

        if (i < s.length()) {
            Token t = getToken();

            if (t == null) {
                return false;
            } else if (!(t.equals(Catcode.LETTER, s.charAt(i)) || t.equals(
                    Catcode.OTHER, s.charAt(i)))
                    || !getKeyword(s, i + 1)) {
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
     * @see de.dante.extex.interpreter.TokenSource#getNonSpace()
     */
    public Token getNonSpace() throws GeneralException {

        for (Token t = getToken(); t != null; t = getToken()) {
            if (!(t.isa(Catcode.SPACE))) {
                return t;
            }
        }

        return null;
    }

    /**
     * Scan a number with a given first token.
     *
     * @param token the first token to consider
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException
     *  in case that no number is found or the end of file has been
     *  reached before an integer could be acquired
     */
    public long getNumber(final Token token) throws GeneralException {

        long n = 0;
        Token t;

        if (token != null && token.isa(Catcode.OTHER)) {
            switch (token.getValue().charAt(0)) {
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
                    n = token.getValue().charAt(0) - '0';

                    for (t = getToken(); t != null && t.isa(Catcode.OTHER)
                            && t.getValue().matches("[0-9]"); //
                    t = getToken()) {
                        n = n * 10 + t.getValue().charAt(0) - '0';
                    }

                    if (t != null) {
                        stream.put(t);
                    }
                    skipSpace();
                    return n;

                case '`':
                    t = getToken();

                    if (t != null) {
                        String val = t.getValue();
                        if (val.length() != 1) {
                            throw new HelpingException(localizer,
                                    "TTP.NonNumericToken", t.getValue());
                        }
                        return val.charAt(0);
                    }
                    // fall through to error handling
                    break;

                case '\'':
                    for (t = getToken(); t != null && t.isa(Catcode.OTHER)
                            && t.getValue().matches("[0-7]"); //
                    t = getToken()) {
                        n = n * 8 + t.getValue().charAt(0) - '0';
                    }

                    if (t != null) {
                        stream.put(t);
                    }
                    skipSpace();
                    return n;

                case '"':
                    for (t = getToken(); t != null && t.isa(Catcode.OTHER)
                            && t.getValue().matches("[0-9a-fA-F]"); //
                    t = getToken()) {
                        switch (t.getValue().charAt(0)) {
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
                                n = n * 16 + t.getValue().charAt(0) - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                n = n * 16 + t.getValue().charAt(0) - 'a' + 10;
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                n = n * 16 + t.getValue().charAt(0) - 'A' + 10;
                                break;
                            default:
                                throw new PanicException("this can't happen");
                        }
                    }

                    if (t != null) {
                        stream.put(t);
                    }
                    skipSpace();
                    return n;

                default:
            // fall through to error handling
            }
        }

        throw new HelpingException(localizer, "TTP.MissingNumber");
    }

    /**
     * Skip spaces and if the next non-space character is an equal sign skip it
     * as well and all spaces afterwards.
     *
     * @see de.dante.extex.interpreter.TokenSource#getOptionalEquals()
     */
    public void getOptionalEquals() throws GeneralException {

        Token t = getNonSpace();

        if (t == null) {
            return;
        } else if (t.equals(Catcode.OTHER, "=")) {
            stream.put(getNonSpace());
        } else {
            stream.put(t);
        }
    }

    /**
     * Get the next token from the input streams. If the current input stream is
     * at its end then the next one on the streamStack is used until a token
     * could be read. If all stream are at the end then <code>null</code> is
     * returned.
     *
     * @return the next token or <code>null</code>
     *
     * @throws GeneralException in case of an error
     */
    public Token getToken() throws GeneralException {

        Context context = getContext();
        TokenFactory factory = context.getTokenFactory();
        Tokenizer tokenizer = context.getTokenizer();
        Token t;

        while (stream != null) {
            t = stream.get(factory, tokenizer);
            if (t != null) {
                observersPop.update(this, t);
                return t;
            }
            observersCloseStream.update(this, stream);

            int last = streamStack.size() - 1;
            stream = (last >= 0 ? (TokenStream) streamStack.remove(last) : null);
        }

        observersEOF.update(this, null);
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getTokens()
     */
    public Tokens getTokens() throws GeneralException {

        Tokens toks = new Tokens();
        Token token = getToken();

        if (token == null) {
            throw new EofHelpingException(localizer.format("Tokens.Text"));
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceHelpingException("???");
        }

        int balance = 1;

        for (token = getToken(); token != null; token = getToken()) {

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
     * @throws GeneralException in case of an error
     */
    public void push(final Token token) throws GeneralException {

        observersPush.update(this, token);
        stream.put(token);
    }

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     *
     * @param tokens the tokens to push
     *
     * @throws GeneralException in case of an error
     */
    public void push(final Token[] tokens) throws GeneralException {

        for (int i = tokens.length - 1; i >= 0; i--) {

            observersPush.update(this, tokens[i]);
            stream.put(tokens[i]);
        }
    }

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     * In case that the argument is <code>null</code> then it is silently
     * ignored.
     *
     * @param tokens the tokens to push
     *
     * @throws GeneralException in case that the stream is already closed
     */
    public void push(final Tokens tokens) throws GeneralException {

        if (tokens == null) {
            return;
        }

        if (stream == null) {
            try {
                stream = getTokenStreamFactory().newInstance("");
            } catch (ConfigurationException e) {
                throw new PanicException(e);
            }
        }

        for (int i = tokens.length() - 1; i >= 0; i--) {
            observersPush.update(this, tokens.get(i));
            stream.put(tokens.get(i));
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
     *   </td>
     *  </tr>
     *  <tr>
     *   <td>push</td>
     *   <td>This observer is triggered by a push operation on the token stream.
     *   </td>
     *  </tr>
     *  <tr>
     *   <td>EOF</td>
     *   <td>This oberserver is triggered by an end of file on the token stream.
     *   </td>
     *  </tr>
     *  <tr>
     *   <td>close</td>
     *   <td>...</td>
     *  </tr>
     *  <tr>
     *   <td>log</td>
     *   <td>...</td>
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
        } else if ("log".equals(name)) {
            observersLogMessage.add(observer);
        } else {
            throw new NotObservableException(name);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanCharacterCode()
     */
    public UnicodeChar scanCharacterCode() throws GeneralException {

        long cc = scanNumber();

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
     * @return the value of the integer scanned
     *
     * @throws GeneralException
     *  in case that no number is found or the end of file has been
     *             reached before an integer could be acquired
     */
    public long scanInteger() throws GeneralException {

        Token t = scanNonSpace();

        if (t == null) {
            throw new HelpingException(localizer, "TTP.MissingNumber");
        } else if (t.equals(Catcode.OTHER, "-")) {
            return -scanNumber();
        } else if (t.equals(Catcode.OTHER, "+")) {
            return scanNumber();
        }

        stream.put(t);
        return scanNumber();
    }

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws GeneralException
     *  in case of an error in {@link #scanToken() scanToken()}
     */
    public Token scanNonSpace() throws GeneralException {

        for (Token t = scanToken(); t != null; t = scanToken()) {
            if (!(t.isa(Catcode.SPACE))) {
                return t;
            }
        }

        return null;
    }

    /**
     * Scan the input stream for tokens making up a number, this means a
     * sequence of digits with catcode OTHER. Alternative notations for a number
     * may exist. The number can be preceeded by optional whitespace.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException
     *  in case that no number is found or the end of file has been
     *  reached before an integer could be acquired
     */
    public long scanNumber() throws GeneralException {

        return scanNumber(getNonSpace());
    }

    /**
     * Scan a number with a given first token.
     *
     * @param token the first token to consider
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException
     *  in case that no number is found or the end of file has been
     *  reached before an integer could be acquired
     */
    public long scanNumber(final Token token) throws GeneralException {

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

                        for (t = scanToken(); t != null && t.isa(Catcode.OTHER)
                                && t.getValue().matches("[0-9]"); t = scanToken()) {
                            n = n * 10 + t.getValue().charAt(0) - '0';
                        }

                        if (t != null) {
                            stream.put(t);
                        }
                        skipSpace();
                        return n;

                    case '`':
                        t = getToken();

                        if (t != null) {
                            String s = t.getValue();
                            return ("".equals(s) ? 0 : s.charAt(0));
                        }
                        // fall through to error handling
                        break;

                    case '\'':
                        for (t = getToken(); t != null && t.isa(Catcode.OTHER)
                                && t.getValue().matches("[0-7]"); //
                        t = scanToken()) {
                            n = n * 8 + t.getValue().charAt(0) - '0';
                        }

                        if (t != null) {
                            stream.put(t);
                        }
                        skipSpace();
                        return n;

                    case '"':
                        for (t = scanToken(); t != null && t.isa(Catcode.OTHER)
                                && t.getValue().matches("[0-9a-fA-F]"); //
                        t = scanToken()) {
                            switch (t.getValue().charAt(0)) {
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
                                    n = n * 16 + t.getValue().charAt(0) - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    n = n * 16 + t.getValue().charAt(0) - 'a'
                                            + 10;
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    n = n * 16 + t.getValue().charAt(0) - 'A'
                                            + 10;
                                    break;
                                default:
                                    throw new PanicException(localizer,
                                            "Panic.HexNumber",
                                            "Strange character in hex number");
                            }
                        }

                        if (t != null) {
                            stream.put(t);
                        }
                        skipSpace();
                        return n;

                    default:
                        throw new HelpingException(getLocalizer(),
                                "TTP.MissingNumber");
                }
            } else if (t instanceof CodeToken) {
                Context context = getContext();
                Code code = context.getCode((CodeToken) t);
                if (code == null) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.MissingNumber");
                } else if (code instanceof CountConvertible) {
                    return ((CountConvertible) code).convertCount(context,
                            this, getTypesetter());
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, this,
                            getTypesetter());
                    t = getToken();
                } else {
                    throw new HelpingException(getLocalizer(),
                            "TTP.MissingNumber");
                }
            } else {

                throw new HelpingException(getLocalizer(), "TTP.MissingNumber");
            }
        }

        throw new HelpingException(getLocalizer(), "TTP.MissingNumber");
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanRegisterName()
     */
    public String scanRegisterName() throws GeneralException {

        Token token = getNonSpace();

        if (token == null) {
            throw new HelpingException(getLocalizer(), "TTP.MissingNumber");
        }

        if (extendedRegisterNames && token.isa(Catcode.LEFTBRACE)) {
            push(token);
            return scanTokensAsString();
        }

        return Long.toString(scanNumber(token));
    }

    /**
     * Expand tokens from the input stream until an unexpandable token is found.
     * This unexpandable token is returned.
     *
     * @return the next unexpandable token
     *
     * @throws GeneralException in case of an error
     */
    public Token scanToken() throws GeneralException {

        return expand(getToken());
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanTokens()
     */
    public Tokens scanTokens() throws GeneralException {

        Tokens toks = new Tokens();
        Token token = getNonSpace();

        if (token == null) {
            throw new EofHelpingException(null);
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceHelpingException(null);
        }

        int balance = 1;

        for (token = scanToken(); token != null; token = scanToken()) {

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
    public String scanTokensAsString() throws GeneralException {

        return scanTokens().toText();
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
    public boolean skipSpace() throws GeneralException {

        Token t = getNonSpace();
        if (t != null) {
            stream.put(t);
            return true;
        }
        return false;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#update(java.lang.String,
     *      java.lang.String)
     */
    public void update(final String name, final String text)
            throws GeneralException {

        throw new NotObservableException(name);
    }
}