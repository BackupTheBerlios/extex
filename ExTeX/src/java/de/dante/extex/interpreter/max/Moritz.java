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
import java.util.Calendar;
import java.util.Iterator;

import com.ibm.icu.lang.UCharacter;

import de.dante.extex.backend.documentWriter.OutputStreamFactory;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.IllegalRegisterException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.InvalidCharacterException;
import de.dante.extex.interpreter.exception.helping.InvalidCharacterNameException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.extex.interpreter.max.util.LoadUnit;
import de.dante.extex.interpreter.observer.eof.EofObservable;
import de.dante.extex.interpreter.observer.eof.EofObserver;
import de.dante.extex.interpreter.observer.eof.EofObserverList;
import de.dante.extex.interpreter.observer.pop.PopObservable;
import de.dante.extex.interpreter.observer.pop.PopObserver;
import de.dante.extex.interpreter.observer.pop.PopObserverList;
import de.dante.extex.interpreter.observer.push.PushObservable;
import de.dante.extex.interpreter.observer.push.PushObserver;
import de.dante.extex.interpreter.observer.push.PushObserverList;
import de.dante.extex.interpreter.observer.streamClose.StreamCloseObservable;
import de.dante.extex.interpreter.observer.streamClose.StreamCloseObserver;
import de.dante.extex.interpreter.observer.streamClose.StreamCloseObserverList;
import de.dante.extex.interpreter.primitives.register.toks.ToksParameter;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CsConvertible;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.FontConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.interpreter.type.tokens.TokensConvertible;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.stream.observer.file.OpenFileObservable;
import de.dante.extex.scanner.stream.observer.file.OpenFileObserver;
import de.dante.extex.scanner.stream.observer.reader.OpenReaderObservable;
import de.dante.extex.scanner.stream.observer.reader.OpenReaderObserver;
import de.dante.extex.scanner.stream.observer.string.OpenStringObservable;
import de.dante.extex.scanner.stream.observer.string.OpenStringObserver;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationWrapperException;
import de.dante.util.observer.NotObservableException;

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
 * @version $Revision: 1.87 $
 */
public class Moritz extends Max
        implements
            TokenSource,
            Configurable,
            StreamCloseObservable,
            PopObservable,
            PushObservable,
            EofObservable,
            OpenFileObservable,
            OpenStringObservable,
            OpenReaderObservable {

    /**
     * The constant <tt>MAX_CHAR_CODE</tt> contains the maximum value for a
     * character code. In original <logo>TeX</logo> this value would be 255.
     */
    private static final long MAX_CHAR_CODE = Long.MAX_VALUE;

    /**
     * The field <tt>extendedRegisterNames</tt> contains the indicator that the
     * extended definition for register names should be used. If it is
     * <code>false</code> then only numbers are permitted.
     */
    private boolean extendedRegisterNames = false;

    /**
     * The field <tt>observersCloseStream</tt> contains the observer list is
     * used for the observers which are registered to receive notifications when
     * a stream is closed.
     */
    private StreamCloseObserverList observersCloseStream = null;

    /**
     * The field <tt>observersEOF</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when all
     * streams are at their end. The argument is always <code>null</code>.
     */
    private EofObserver observersEOF = null;

    /**
     * The field <tt>observersPop</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when a new
     * token is about to be delivered. The argument is the token to be
     * delivered.
     */
    private PopObserver observersPop = null;

    /**
     * The field <tt>observersPush</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when a new
     * token is pushed back to the input stream. The argument is the token to be
     * pushed.
     */
    private PushObserver observersPush = null;

    /**
     * The field <tt>registerMaxIndex</tt> contains the maximal number for a
     * register if they are  numeric. A negative value indicates that no
     * restriction is imposed.
     */
    private long registerMaxIndex = -1;

    /**
     * The field <tt>skipSpaces</tt> contains the indicator that space tokens
     * should be discarded before the next token is delivered.
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
     * from &ndash; except of the current one which is stored in the variable
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
     *
     * @see de.dante.extex.interpreter.TokenSource#addStream(
     *      de.dante.extex.scanner.stream.TokenStream)
     */
    public void addStream(final TokenStream theStream) {

        streamStack.add(this.stream);
        this.stream = theStream;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeAllStreams(
     *      de.dante.extex.interpreter.context.Context)
     */
    public void closeAllStreams(final Context context)
            throws InterpreterException {

        while (stream != null) {
            closeStream(context);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeNextFileStream(
     *      de.dante.extex.interpreter.context.Context)
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
     * Close the topmost stream and pop another one to the top if one is left.
     * If the closed stream has been a file stream then the tokens from the
     * tokens register <tt>everyeof</tt> is inserted into the token stream.
     *
     * @param context the interpreter context
     *
     * @return <code>true</code> iff the closed file has been a file stream
     *
     * @throws InterpreterException in case of an error
     */
    private boolean closeStream(final Context context)
            throws InterpreterException {

        if (observersCloseStream != null) {
            observersCloseStream.update(stream);
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
     * This method adapts the configurable settings according to the values from
     * the given configuration.
     * The following items are considered:
     * <dl>
     *  <dt><tt>ExtendedRegisterNames</tt></dt>
     *  <dd>The value of this configuration contains a boolean value. If it is
     *   <code>true</code> then extended register names are allowed. This means
     *   that arbitrary strings can be used instead of the simple numbers as
     *   defined by <logo>TeX</logo>. This configuration is optional.
     *  </dd>
     *  <dt><tt>RegisterMax</tt></dt>
     *  <dd>The value of this configuration contains a number. This number is
     *   the highest register number. Attempts to use a higher number results
     *   in an exception.
     *  </dd>
     * </dl>
     *
     * <p>Example</p>
     * <pre>
     *  &lt;interpreter&gt;
     *    &lt;ExtendedRegisterNames&gt;true&lt;/ExtendedRegisterNames&gt;
     *    &lt;RegisterMax&gt;255&lt;/RegisterMax&gt;
     *  &lt;/interpreter&gt;
     * </pre>
     *
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration configuration)
            throws ConfigurationException {

        super.configure(configuration);
        OutputStreamFactory outputFactory = null; //TODO gene: provide OutputStreamFactory

        try {
            getContext().setInteraction(Interaction.ERRORSTOPMODE);

            Iterator iterator = configuration.iterator("unit");

            while (iterator.hasNext()) {
                LoadUnit.loadUnit((Configuration) iterator.next(),
                        getContext(), this, getTypesetter(), getLogger(),
                        outputFactory);
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

        Configuration cfg = configuration
                .findConfiguration("ExtendedRegisterNames");
        if (cfg != null) {
            extendedRegisterNames = Boolean.valueOf(cfg.getValue())
                    .booleanValue();
        }

        cfg = configuration.findConfiguration("RegisterMax");
        if (cfg != null) {
            registerMaxIndex = Long.valueOf(cfg.getValue()).longValue();
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getBox(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Flags flags, final Context context,
            final Typesetter typesetter) throws InterpreterException {

        Flags f = null;
        if (flags != null) {
            f = flags.copy();
            flags.clear();
        }
        Token t = getToken(context);
        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code instanceof Boxable) {
                if (flags != null) {
                    flags.set(f);
                }
                return ((Boxable) code).getBox(context, this, typesetter);
            }
        }
        throw new HelpingException(getLocalizer(), "TTP.BoxExpected");
    }

    /**
     * Get the next token from the token stream and check that it is a
     * control sequence or active character.
     * At the end of all input streams the control sequence "inaccessible"
     * is inserted and an exception is thrown. Thus this method will never
     * return <code>null</code>.
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
        throw new HelpingException(getLocalizer(), "TTP.MissingCtrlSeq");
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getFont(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    public Font getFont(final Context context, final String primitive)
            throws InterpreterException {

        Token t = getToken(context);

        if (t == null) {
            throw new EofException(primitive);

        } else if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code == null) {
                throw new UndefinedControlSequenceException(AbstractCode
                        .printable(context, t));

            } else if (code instanceof FontConvertible) {
                return ((FontConvertible) code).convertFont(context, this);
            }

        }
        throw new HelpingException(getLocalizer(), "TTP.MissingFontIdent");

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
     * may have the category codes <tt>LETTER</tt> or <tt>OTHER</tt>.
     *
     * @param context the interpreter context
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
                put(t);
                return false;
            }

        }
        return true;
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
     * @throws MissingNumberException in case that no number is found or
     *  the end of file has been
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
                        put(t);
                    }
                    skipSpaces = true;
                    return n;

                case '`':
                    t = getToken(context);

                    if (t instanceof ControlSequenceToken) {
                        String val = ((ControlSequenceToken) t).getName();
                        if (val.length() != 1) {
                            throw new HelpingException(getLocalizer(),
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

                    put(t);
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
                                put(t);
                                skipSpaces = true;
                                return n;
                        }
                    }

                    put(t);
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

        if (t != null && t.equals(Catcode.OTHER, '=')) {
            skipSpaces = true;
        } else {
            put(t);
        }
    }

    /**
     * Get the next token from the input streams. If the current input stream is
     * at its end then the next one on the streamStack is used until a token
     * could be read. If all stream are at the end then <code>null</code> is
     * returned.
     * <p>
     * Whenever a file stream is closed then the tokens from the ...
     * </p>
     *
     * @param context the interpreter context
     *
     * @return the next token or <code>null</code>
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#getToken(
     *      de.dante.extex.interpreter.context.Context)
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
                        if (t != null && observersPop != null) {
                            observersPop.update(t);
                        }
                    } while (t != null && t instanceof SpaceToken);

                    if (t != null) {
                        return t;
                    }

                    closeStream(context);
                }
            } catch (ScannerException e) {
                throw new InterpreterException(e);
            }

        } else {

            try {
                while (stream != null) {
                    t = stream.get(factory, tokenizer);
                    if (t != null) {
                        if (observersPop != null) {
                            observersPop.update(t);
                        }
                        return t;
                    }
                    closeStream(context);
                }
            } catch (ScannerException e) {
                throw new InterpreterException(e);
            }
        }
        if (observersEOF != null) {
            observersEOF.update();
        }
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getTokens(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens getTokens(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Tokens toks = new Tokens();
        Token token = getToken(context);

        if (token instanceof LeftBraceToken) {
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

        } else if (token instanceof CodeToken) {
            Code code = context.getCode((CodeToken) token);
            if (code instanceof TokensConvertible) {
                return ((TokensConvertible) code).convertTokens(context,
                        source, typesetter);
            }

        } else if (token == null) {
            throw new EofException(getLocalizer().format("Tokens.Text"));
        }

        throw new MissingLeftBraceException("???");
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
     * Push back a token onto the input stream for subsequent reading.
     *
     * @param token the token to push
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.TokenSource#push(
     *      de.dante.extex.scanner.type.token.Token)
     */
    public void push(final Token token) throws InterpreterException {

        if (token == null) {
            return;
        }
        if (observersPush != null) {
            observersPush.update(token);
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
     *      de.dante.extex.scanner.type.token.Token[])
     */
    public void push(final Token[] tokens) throws InterpreterException {

        if (stream == null) {
            try {
                stream = getTokenStreamFactory().newInstance("");
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }

        for (int i = tokens.length - 1; i >= 0; i--) {

            if (observersPush != null) {
                observersPush.update(tokens[i]);
            }

            stream.put(tokens[i]);
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

        for (int i = tokens.length() - 1; i >= 0; i--) {
            Token t = tokens.get(i);
            if (observersPush != null) {
                observersPush.update(t);
            }
            stream.put(t);
        }
    }

    /**
     * Push a token back to the input stream.
     *
     * @param t the token to push
     *
     * @throws InterpreterException in case of a configuration problem
     */
    private void put(final Token t) throws InterpreterException {

        if (t == null) {
            return;
        }
        if (stream == null) {
            try {
                stream = getTokenStreamFactory().newInstance("");
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }
        stream.put(t);

    }

    /**
     * Add an observer for the eof event.
     * This observer is triggered by an end of file on the token stream.
     * This means that all tokens have been processed and all stream are at
     * their end.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final EofObserver observer) {

        observersEOF = EofObserverList.register(observersEOF, observer);
    }

    /**
     * @see de.dante.extex.scanner.stream.observer.file.OpenFileObservable#registerObserver(de.dante.extex.scanner.stream.observer.file.OpenFileObserver)
     */
    public void registerObserver(final OpenFileObserver observer) {

        if (tokenStreamFactory instanceof OpenFileObservable) {
            ((OpenFileObservable) tokenStreamFactory)
                    .registerObserver(observer);
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.observer.reader.OpenReaderObservable#registerObserver(de.dante.extex.scanner.stream.observer.reader.OpenReaderObserver)
     */
    public void registerObserver(final OpenReaderObserver observer) {

        if (tokenStreamFactory instanceof OpenReaderObservable) {
            ((OpenReaderObservable) tokenStreamFactory)
                    .registerObserver(observer);
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.observer.string.OpenStringObservable#registerObserver(de.dante.extex.scanner.stream.observer.string.OpenStringObserver)
     */
    public void registerObserver(final OpenStringObserver observer) {

        if (tokenStreamFactory instanceof OpenStringObservable) {
            ((OpenStringObservable) tokenStreamFactory)
                    .registerObserver(observer);
        }
    }

    /**
     * Add an observer for the pop event.
     * This observer is triggered by a pop operation on the token stream.
     * This means that a token has been extracted.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final PopObserver observer) {

        observersPop = PopObserverList.register(observersPop, observer);
    }

    /**
     * Add an observer for the push event.
     * This observer is triggered by a push operation on the token stream.
     * This means that a token has been placed on the current stream for
     * subsequent reading.
     *
     * @param observer the observer to add
     */
    public void registerObserver(final PushObserver observer) {

        observersPush = PushObserverList.register(observersPush, observer);
    }

    /**
     * @see de.dante.extex.interpreter.observer.streamClose.StreamCloseObservable#registerObserver(
     *      de.dante.extex.interpreter.observer.streamClose.StreamCloseObserver)
     */
    public void registerObserver(final StreamCloseObserver observer) {

        observersCloseStream = StreamCloseObserverList.register(
                observersCloseStream, //
                observer);
    }

    /**
     * This method scans a character code.
     * <doc type="extension">
     *  The character code is either a number &ndash; after expansion &ndash; or
     *  the name of a Unicode character in braces.
     * </doc>
     *
     * @see de.dante.extex.interpreter.TokenSource#scanCharacterCode(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter,
     *      java.lang.String)
     */
    public UnicodeChar scanCharacterCode(final Context context,
            final Typesetter typesetter, final String primitive)
            throws InterpreterException {

        long cc;

        Token t = getNonSpace(context);
        if (t instanceof LeftBraceToken) {
            push(t);
            String name = scanTokensAsString(context, primitive);
            if (name == null) {
                throw new InvalidCharacterNameException("");
            }
            cc = UCharacter.getCharFromName(name);

            if (cc < 0 || cc > MAX_CHAR_CODE) {
                throw new InvalidCharacterNameException(name);
            }
        } else {

            push(t);
            cc = scanInteger(context, typesetter);

            if (cc < 0 || cc > MAX_CHAR_CODE) {
                throw new InvalidCharacterException(Long.toString(cc));
            }
        }

        return new UnicodeChar((int) cc);
    }

    /**
     * Scan the input stream for tokens making up an integer, this is a number
     * optionally preceded by a sign (+ or -). The number can be preceded by
     * optional white space. White space is also ignored between the sign and
     * the number. All non-whitespace characters must have the category code
     * OTHER.
     *
     * @param context the interpreter context
     * @param typesetter the typesetter
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that the end of file has been
     *  reached before an integer could be acquired
     * @throws MissingNumberException in case of a missing number
     *
     * @see de.dante.extex.interpreter.TokenSource#scanInteger(
     *      de.dante.extex.interpreter.context.Context, Typesetter)
     */
    public long scanInteger(final Context context, final Typesetter typesetter)
            throws InterpreterException,
                MissingNumberException {

        boolean neg = false;
        Token t;

        for (t = getNonSpace(context); t != null; t = getNonSpace(context)) {

            if (t.equals(Catcode.OTHER, '-')) {
                neg = !neg;

            } else if (t.equals(Catcode.OTHER, '+')) {
                // + is absorbed

            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);

                if (code instanceof CountConvertible) {

                    return ((CountConvertible) code).convertCount(context,
                            this, typesetter);

                } else if (code instanceof ExpandableCode) {

                    ((ExpandableCode) code).expand(Flags.NONE, context, this,
                            typesetter);

                } else {
                    break;
                }
            } else {
                return (neg ? -scanNumber(context, t) : scanNumber(context, t));
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     *
     * @param context the interpreter context
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws InterpreterException
     *  in case of an error in {@link #scanToken(Context) scanToken()}
     */
    public Token scanNonSpace(final Context context)
            throws InterpreterException {

        Token t;
        do {
            skipSpaces = true;
            t = scanToken(context);
        } while (t != null && t.isa(Catcode.SPACE));

        return t;
    }

    /**
     * Scan the input stream for tokens making up a number, this means a
     * sequence of digits with category code OTHER. Alternative notations for
     * a number may exist. The number can be preceded by optional white space.
     *
     * @param context the interpreter context
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     *
     * @see de.dante.extex.interpreter.TokenSource#scanNumber(Context)
     */
    public long scanNumber(final Context context) throws InterpreterException {

        return scanNumber(context, getNonSpace(context));
    }

    /**
     * Scan a number with a given first token.
     *
     * @param context the interpreter context
     * @param token the first token to consider
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     *
     * @see de.dante.extex.interpreter.TokenSource#scanNumber(
     *      de.dante.extex.scanner.type.token.Token)
     */
    public long scanNumber(final Context context, final Token token)
            throws InterpreterException,
                MissingNumberException {

        long n = 0;
        Token t = token;
        int no;

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

                        for (t = getToken(context); t instanceof OtherToken
                                && t.getChar().isDigit(); t = getToken(context)) {
                            n = n * 10 + t.getChar().getCodePoint() - '0';
                        }

                        /*
                         while (t instanceof SpaceToken) {
                         t = getToken(context);
                         }
                         */
                        if (t instanceof SpaceToken) {
                            skipSpaces = true;
                        } else {
                            put(t);
                        }
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
                        t = scanToken(context);
                        if (!(t instanceof OtherToken)) {
                            throw new MissingNumberException();
                        }
                        n = t.getChar().getCodePoint() - '0';
                        if (n < 0 || n > 7) {
                            throw new MissingNumberException();
                        }
                        for (t = scanToken(context); t instanceof OtherToken; //
                        t = scanToken(context)) {
                            no = t.getChar().getCodePoint() - '0';
                            if (no < 0 || no > 7) {
                                break;
                            }
                            n = n * 8 + no;
                        }

                        while (t instanceof SpaceToken) {
                            t = getToken(context);
                        }
                        put(t);
                        return n;

                    case '"':

                        for (t = scanToken(context); //
                        t instanceof OtherToken || t instanceof LetterToken; //
                        t = scanToken(context)) {
                            no = t.getChar().getCodePoint();
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
                                    put(t);
                                    skipSpaces = true;
                                    return n;
                            }
                        }

                        while (t instanceof SpaceToken) {
                            t = getToken(context);
                        }
                        put(t);
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
     * @see de.dante.extex.interpreter.TokenSource#scanRegisterName(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    public String scanRegisterName(final Context context, final String primitive)
            throws InterpreterException {

        skipSpaces = true;
        Token token = getToken(context);

        if (token == null) {

            throw new MissingNumberException();
        }

        if (extendedRegisterNames && token.isa(Catcode.LEFTBRACE)) {
            push(token);
            return scanTokensAsString(context, primitive);
        }

        long registerNumber = scanNumber(context, token);
        if (registerMaxIndex >= 0 && registerNumber > registerMaxIndex) {
            throw new IllegalRegisterException(Long.toString(registerNumber));
        }
        return Long.toString(registerNumber);
    }

    /**
     * Expand tokens from the input stream until an not expandable token is
     * found. This not expandable token is returned.
     *
     * @param context the interpreter context
     *
     * @return the next not expandable token
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
     * @see de.dante.extex.interpreter.TokenSource#scanTokens(
     *      de.dante.extex.interpreter.context.Context,
     *      boolean,
     *      boolean,
     *      java.lang.String)
     */
    public Tokens scanTokens(final Context context,
            final boolean reportUndefined, final boolean ignoreUndefined,
            final String primitive) throws InterpreterException {

        Tokens toks = new Tokens();
        skipSpaces = true;
        Token token = getToken(context);

        if (token == null) {
            throw new EofException(primitive);
        } else if (!token.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceException(primitive);
        }

        int balance = 1;

        for (token = scanToken(context); token != null; token = scanToken(context)) {

            if (token instanceof LeftBraceToken) {
                toks.add(token);
                ++balance;
            } else if (token instanceof RightBraceToken && --balance <= 0) {
                break;
            } else if (token instanceof CodeToken) {
                if (ignoreUndefined) {

                } else if (reportUndefined
                        && context.getCode((CodeToken) token) == null) {
                    throw new UndefinedControlSequenceException(token
                            .toString());
                } else {
                    toks.add(token);
                }
            } else {
                toks.add(token);
            }

        }

        return toks;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanTokensAsString(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    public String scanTokensAsString(final Context context,
            final String primitive) throws InterpreterException {

        return scanTokens(context, false, false, primitive).toText();
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