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

import java.util.Stack;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.RightBraceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.NotObservableException;
import de.dante.util.Observable;
import de.dante.util.Observer;
import de.dante.util.ObserverList;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;

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
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.18 $
 */
public abstract class Moritz implements TokenSource, Observable {
    /**
     * The constant <tt>MAX_CHAR_CODE</tt> contains the maximum value for a
     * character code. In original TeX this value would be 255.
     */
    private static final long MAX_CHAR_CODE = Integer.MAX_VALUE; //TODO: find a good value

    /**
     * The field <tt>context</tt> contains the interpreter context. well, the
     * two of them (Max and Moritz) are more closely linked than I like it.
     */
    private Context context;

    /**
     * The field <tt>observersCloseStream</tt> contains the observer list is
     * used for the observers which are registered to receive notifications
     * when a stream is closed.
     */
    private ObserverList observersCloseStream = new ObserverList();

    /**
     * The field <tt>observersEOF</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when all
     * streams are at their end. The argument is always <code>null</code>.
     */
    private ObserverList observersEOF = new ObserverList();

    /**
     * The field <tt>observersMessage</tt> contains the observer list is used
     * for the observers which are registered to receive notifications when a
     * message is send from another component. This message shuld be made
     * accessible to the user in some way, e.g. on the terminal or in the log
     * file.
     */
    private ObserverList observersMessage = new ObserverList();

    /**
     * The field <tt>observersPop</tt> contains the observer list is used for
     * the observers which are registered to receive a notification when a new
     * token is about to be delivered. The argument is the token to be
     * delivered.
     */
    private ObserverList observersPop = new ObserverList();

    /**
     * The field <tt>observersPush</tt> contains the observer list is used
     * for the observers which are registered to receive a notification when a
     * new token is pushed back to the input stream. The argument is the token
     * to be pushed.
     */
    private ObserverList observersPush = new ObserverList();

    /**
     * The field <tt>streamStack</tt> contains the stack of streams to read
     * from except of the current one which is stored in the variable
     * <code>stream</code>.
     */
    private Stack streamStack = new Stack();

    /**
     * The field <tt>stream</tt> contains the current stream to read tokens
     * from. For efficiency this stream is kept in a variable instead of
     * accessing the streamStack each time it is needed.
     */
    private TokenStream stream = null;

    /**
     * The field <tt>tokenStreamFactory</tt> contains the factory for new
     * token streams.
     */
    private TokenStreamFactory tokenStreamFactory = null;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration to use
     */
    public Moritz(final Configuration configuration) {
        super();
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getLocator()
     */
    public Locator getLocator() {
        return (stream == null ? null : stream.getLocator());
    }

    /**
     * Get the next token which has not the catcode SPACE.
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws GeneralException in case of an error
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
     * Get the next token form the input streams. If the current input stream
     * is at its end then the next one on the streamStack is used until a token
     * could be read. If all stream are at the end then <code>null</code> is
     * returned.
     *
     * @return the next token or <code>null</code>
     *
     * @throws GeneralException in case of another error
     */
    public Token getToken() throws GeneralException {
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

            stream = (TokenStream) streamStack.pop();
        }

        observersEOF.update(this, null);
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#getTokens()
     */
    public Tokens getTokens() throws GeneralException {
        Tokens toks = new Tokens();
        Token token = getNonSpace();

        if (token == null) {
            throw new GeneralHelpingException("EOF");
            //TODO: handle EOF
        } else if ( ! token.isa(Catcode.LEFTBRACE)) {
            throw new GeneralHelpingException("TTP.MissingLeftBrace");
            //TODO call the error handler
        }

        int balance = 1;

        for (token = getToken(); token != null; token = getToken()) {

            if (token.isa(Catcode.LEFTBRACE)) {
                ++balance;
            } else if (token instanceof RightBraceToken && --balance <= 0) {
                break;
            }

            toks.add(token);
        }
        
        return toks;
    }

    /**
     * ...
     *
     * @return ...
     * 
     * @throws GeneralException
     */
    public Token getControlSequence()
        throws GeneralException {
        Token t = getToken();
        
        if ( t == null ) {
            throw new GeneralHelpingException("xxx"); //TODO EOF
        } else if ( ! (t instanceof ControlSequenceToken) ) {
            throw new GeneralHelpingException("xxx"); //TODO error
        } else if ( ! t.getValue().equals("csname") ) {
            return t;
        }

        Tokens toks = new Tokens();
        //scanToEndcsname();
        
        
        //TODO incomplete
        return null;
    }
    
    /**
     * Setter for the token stream factory.
     * 
     * @param factory the token stream factory
     */
    public void setTokenStreamFactory(final TokenStreamFactory factory) {
        tokenStreamFactory = factory;
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
     * Put a given stream on top of the stream stack. The reading occurs on
     * this new stream before resorting to the previous streams.
     * 
     * @param stream the new stream to read from
     */
    public void addStream(final TokenStream stream) {
        streamStack.push(this.stream);
        this.stream = stream;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeAllStreams()
     */
    public void closeAllStreams() {

        while (stream != null) {
            observersCloseStream.update(this, stream);
            stream = (TokenStream) streamStack.pop();
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#closeNextFileStream()
     */
    public void closeNextFileStream() {

        while (stream != null) {
            observersCloseStream.update(this, stream);
            if (stream.isFileStream()) {
                stream = (TokenStream) streamStack.pop();
                return;
            }
            stream = (TokenStream) streamStack.pop();
        }
    }

    /**
     * Push back a token onto the input stream for subsequent reading.
     * 
     * @param token the token to push
     */
    public void push(final Token token) {
        observersPush.update(this, token);
        stream.put(token);
    }

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     * 
     * @param tokens the tokens to push
     */
    public void push(final Token[] tokens) {
        for (int i = tokens.length - 1; i >= 0; i--) {
            observersPush.update(this, tokens[i]);
            stream.put(tokens[i]);
        }
    }

    /**
     * Push back a list of tokens onto the input stream for subsequent reading.
     * 
     * @param tokens the tokens to push
     */
    public void push(final Tokens tokens) {
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
     *   <td>...</td>
     *  </tr>
     *  <tr>
     *   <td>push</td>
     *   <td>...</td>
     *  </tr>
     *  <tr>
     *   <td>EOF</td>
     *   <td>...</td>
     *  </tr>
     *  <tr>
     *   <td>close</td>
     *   <td>...</td>
     *  </tr>
     *  <tr>
     *   <td>message</td>
     *   <td>...</td>
     *  </tr>
     * </table>
     *
     * @see de.dante.util.Observable#registerObserver(java.lang.String,
     *         de.dante.util.Observer)
     */
    public void registerObserver(String name, Observer observer)
            throws NotObservableException {
        if ("push".equals(name)) {
            observersPush.add(observer);
        } else if ("pop".equals(name)) {
            observersPop.add(observer);
        } else if ("EOF".equals(name)) {
            observersEOF.add(observer);
        } else if ("close".equals(name)) {
            observersCloseStream.add(observer);
        } else if ("message".equals(name)) {
            observersMessage.add(observer);
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
            throw new GeneralHelpingException("TTP.BadCharCode", Long
                    .toString(cc), "0", Long.toString(MAX_CHAR_CODE));
        }

        return new UnicodeChar((int) cc);
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanFloat()
     */
    //TODO: gene: this method should be moved out of here
    public long scanFloat() throws GeneralException {
        long value = 0;
        boolean neg = false;
        int post = 0;
        Token t = scanNonSpace();

        if (t == null) {
            //TODO: treat EOF
        } else if (t.equals(Catcode.OTHER, "-")) {
            neg = true;
            t = scanNonSpace();
        } else if (t.equals(Catcode.OTHER, "+")) {
            t = scanNonSpace();
        }

        if (t != null && !t.equals(Catcode.OTHER, ".")
            && !t.equals(Catcode.OTHER, ",")) {
            value = scanNumber(t);
            t = getToken();
        }

        if (t != null
            && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
            // @see "TeX -- The Program [102]"
            int[] dig = new int[17];
            int k = 0;

            for (t = getToken(); t != null && t.isa(Catcode.OTHER)
                                 && t.getValue().matches("[0-9]"); 
                 t = scanToken()) {
                if (k < 17) {
                    dig[k++] = t.getValue().charAt(0) - '0';
                }
            }

            if (k < 17) {
                k = 17;
            }

            post = 0;

            while (k-- > 0) {
                post = (post + dig[k] * (1 << 17)) / 10;
            }

            post = (post + 1) / 2;
        }

        push(t);

        value = value << 16 | post;

        return (neg ? -value : value);
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#scanReal()
     */
    //TODO: gene: this method should be moved out of here
    public Real scanReal() throws GeneralException {
        StringBuffer sb = new StringBuffer(32);
        long value = 0;
        boolean neg = false;
        Token t = scanNonSpace();

        if (t == null) {
            // TODO: to be completed
        } else if (t.equals(Catcode.OTHER, "-")) {
            neg = true;
            t = scanNonSpace();
        } else if (t.equals(Catcode.OTHER, "+")) {
            t = scanNonSpace();
        }

        if (neg) {
            sb.append('-');
        }

        if (t != null && !t.equals(Catcode.OTHER, ".")
            && !t.equals(Catcode.OTHER, ",")) {
            value = scanNumber(t);
            t = getToken();
        }

        sb.append(Long.toString(value));
        sb.append('.');
        value = 0;

        if (t != null
            && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
            value = scanNumber();
        }
        sb.append(Long.toString(value));

        return new Real(sb.toString());
    }

    /**
     * Scan the input stream for tokens making up an integer, this is a number
     * optionally preceeded by a sign (+ or -). The number can be preceeded by
     * optional whitespace. Whitespace is also ignored between the sign and the
     * number. All non-whitespace characters must have the catcode OTHER.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case that no number is found or the end of
     *             file has been reached before an integer could be acquired
     */
    public long scanInteger() throws GeneralException {
        Token t = scanNonSpace();

        if (t == null) {
            throw new GeneralHelpingException("TTP.MissingNumber");
        } else if (t.equals(Catcode.OTHER, "-")) {
            return -scanNumber();
        } else if (t.equals(Catcode.OTHER, "+")) {
            return scanNumber();
        }

        stream.put(t);
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
     *         removed from the input stream
     *
     * @throws GeneralException in case that no number is found or the end of
     *             file has been reached before an integer could be acquired
     */
    public boolean scanKeyword(final String s) throws GeneralException {
        return scanKeyword(s, 0);
    }

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     *
     * @return the next non-space token or <code>null</code> at EOF
     *
     * @throws GeneralException in case of an error in
     *             {@link #scanToken() scanToken()}
     */
    public Token scanNonSpace() throws GeneralException {
        return scanNonSpace(scanToken());
    }

    /**
     * Scan the input for the next token which has not the catcode SPACE.
     * 
     * @param token the first token to consider
     * 
     * @return the next non-space token or <code>null</code> at EOF
     * 
     * @throws GeneralException in case of an error in
     *             {@link #scanToken() scanToken()}
     */
    public Token scanNonSpace(final Token token) throws GeneralException {
        for (Token t=token; t != null; t = scanToken()) {
            if (!(t.isa(Catcode.SPACE))) {
                return t;
            }
        }

        return null;
    }

    /**
     * Expand tokens from the input stream until an unexpandable token is
     * found. This unexpandable token is returned.
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
            //TODO: handle EOF
        } else if ( ! token.isa(Catcode.LEFTBRACE)) {
            throw new GeneralHelpingException("TTP.MissingLeftBrace");
            //TODO call the error handler
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
     * @see de.dante.extex.interpreter.TokenSource#scanNextTokensAsString()
     */
    public String scanTokensAsString() throws GeneralException {
        return scanTokens().toText();
    }

    /**
     * Scan the input stream for tokens making up a number, this means a
     * sequence of digits with catcode OTHER. Alternative notations for a
     * number may exist. The number can be preceeded by optional whitespace.
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case that no number is found or the end of
     *             file has been reached before an integer could be acquired
     */
    public long scanNumber() throws GeneralException {
        return scanNumber(scanNonSpace());
    }

    /**
     * Scan a number with a given first token.
     *
     * @param token the first token to consider
     *
     * @return the value of the integer scanned
     *
     * @throws GeneralException in case that no number is found or the end of
     *             file has been reached before an integer could be acquired
     */
    public long scanNumber(final Token token) throws GeneralException {
        long n = 0;
        Token t = token;

        if (t == null) {
            throw new GeneralHelpingException("TTP.MissingNumber");
        } else if (t.isa(Catcode.OTHER) && t.getValue().matches("[0-9]")) {
            do {
                n = n * 10 + t.getValue().charAt(0) - '0';
                t = scanToken();
            } while (t != null && t.isa(Catcode.OTHER)
                     && t.getValue().matches("[0-9]"));

            stream.put(t);
        } else if (t.isa(Catcode.OTHER) && t.getValue().equals("'")) {
            while (t != null && t.isa(Catcode.OTHER)
                   && t.getValue().matches("[0-7]")) {
                n = n * 8 + t.getValue().charAt(0) - '0';
                t = scanToken();
            }

            stream.put(t);
        } else if (t.isa(Catcode.OTHER) && t.getValue().equals("`")) {
            t = getToken();

            if (t == null) {
                throw new GeneralHelpingException("TTP.MissingNumber");
            }

            n = t.getValue().charAt(0);
        } else if (t.isa(Catcode.OTHER) && t.getValue().equals("\"")) {
            //TODO parse hex numbers
        } else {
            stream.put(t);
            throw new GeneralHelpingException("TTP.MissingNumber");
        }

        return n;
    }

    /**
     * Skip spaces and if the next non-space character is an equal sign skip it
     * as well and all spaces afterwards.
     *
     * @throws GeneralException in case of an error
     */
    public void scanOptionalEquals() throws GeneralException {
        Token t = scanNonSpace();

        if (t == null) {
            throw new GeneralHelpingException("TTP.MissingNumber");
        } else if (t.equals(Catcode.OTHER, "=")) {
            stream.put(scanNonSpace());
        } else {
            stream.put(t);
        }
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#update(java.lang.String,
     *      java.lang.String)
     */
    public void update(final String name, final String text)
            throws NotObservableException {
        if ("message".equals(name)) {
            observersMessage.update(this, text);
        } else {
            throw new NotObservableException(name);
        }
        //gene: this method can be specialized if only a single name is ever
        // used.
    }

    /**
     * ...
     *
     * @param token
     * @return
     * @throws GeneralException
     */
    protected abstract Token expand(final Token token)
	        throws GeneralException;

    /**
     * Setter for the context.
     * 
     * @param theContext the context to use
     */
    protected void setContext(final Context theContext) {
        context = theContext;
    }

    /**
     * Scans the input token stream for a given sequence of tokens. Those
     * tokens may have the catcodes <tt>LETTER</tt> or <tt>OTHER</tt>.
     *
     * @param s the string to use as reference
     * @param i the index in s to start working at
     *
     * @return <code>true</code> iff the keyword has been found
     *
     * @throws GeneralException in case of an error
     */
    private boolean scanKeyword(final String s, final int i)
            throws GeneralException {
        if (i < s.length()) {
            Token t = scanToken();

            if (t == null) {
                return false;
            } else if (!(t.equals(Catcode.LETTER, s.charAt(i)) || t
                    .equals(Catcode.OTHER, s.charAt(i)))
                       || !scanKeyword(s, i + 1)) {
                stream.put(t);
                return false;
            }
        }

        return true;
    }

}
