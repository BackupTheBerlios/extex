/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.scanner.stream.impl32.newversion;

import java.util.LinkedList;

import de.dante.extex.interpreter.Tokenizer;
import de.dante.extex.scanner.exception.ScannerException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.util.Locator;
import de.dante.util.UnicodeChar;

/**
 * This is the abstract base implementation of a token stream.
 * It has an internal stack of tokens which can be enlarged
 * with push() or reduced with pop().
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public abstract class AbstractTokenStreamImpl implements TokenStream {

    /**
     * The field <tt>stack</tt> contains the Token stack
     * for the pushback operation.
     */
    private LinkedList stack;

    /**
     * The field <tt>fileStream</tt> contains the indicator
     * whether or not this TokenStream is attached to a file.
     */
    private boolean fileStream = false;

    /**
     * Creates a new object.
     *
     * @param isFile    indicator whether or not the token stream
     *                  is assigned to a file
     */
    public AbstractTokenStreamImpl(final boolean isFile) {

        super();
        this.fileStream = isFile;
        stack = new LinkedList();
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
     */
    public abstract Locator getLocator();

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
     */
    public boolean closeFileStream() {

        stack.clear();
        return false;
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#get(
     *      de.dante.extex.scanner.TokenFactory,
     *      de.dante.extex.interpreter.Tokenizer)
     */
    public Token get(final TokenFactory factory, final Tokenizer tokenizer)
            throws ScannerException {

        return (stack.size() == 0 ? getNext(factory, tokenizer) : (Token) stack
                .removeLast());
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#put(
     *      de.dante.extex.scanner.Token)
     */
    public void put(final Token token) {

        if (token != null) {
            stack.add(token);
        }
    }

    /**
     * Get the next token when the stack is empty.
     * This method is meant to be overloaded by derived classes.
     *
     * @param factory the factory for new tokens
     * @param tokenizer the classifies for characters
     *
     * @return the next Token or <code>null</code>
     *
     * @throws ScannerException in case of an error
     */
    protected abstract Token getNext(final TokenFactory factory,
            final Tokenizer tokenizer) throws ScannerException;

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
     */
    public boolean isFileStream() {

        return fileStream;
    }

    /**
     * This is a type-safe class to represent state information.
     */
    protected static final class State {

        /**
         * Creates a new object.
         */
        public State() {

            super();
        }
    }

    /**
     * The constant <tt>BUFFERSIZE_ATTRIBUTE</tt> contains
     * the name of the attribute used to get the buffer size.
     */
    protected static final String BUFFERSIZE_ATTRIBUTE = "buffersize";

    /**
     * The constant <tt>CARET_LIMIT</tt> contains
     * the value for ...
     */
    protected static final int CARET_LIMIT = 0100; // 0100 = 64

    /**
     * The constant <tt>CR</tt> contains
     * the cr-unicodechar.
     */
    protected static final UnicodeChar CR = new UnicodeChar('\r');

    /**
     * The constant <tt>MID_LINE</tt> contains the state for the processing in
     * the middle of a line.
     */
    protected static final State MID_LINE = new State();

    /**
     * The constant <tt>NEW_LINE</tt> contains the state for the processing at
     * the beginning of a new line.
     */
    protected static final State NEW_LINE = new State();

    /**
     * The constant <tt>SKIP_BLANKS</tt> contains the state for the processing
     * when spaces are ignored.
     */
    protected static final State SKIP_BLANKS = new State();

    /**
     * Analyze a character and return its hex value, i.e. '0' to '9' are mapped
     * to 0 to 9 and 'a' to 'f' (case sensitive) are mapped to 10 to 15.
     *
     * @param c the character code to analyze
     *
     * @return Returns the integer value of a hex digit
     *         or -1 if no hex digit is given
     */
    protected int hex2int(final int c) {

        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 10;
            //} else if ('A' <= c && c <= 'F') {
            //    return c - 'A' + 10;
        } else {
            return -1;
        }
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isEof()
     */
    public boolean isEof() {

        // TODO mgn incomplete
        return false;
    }
}