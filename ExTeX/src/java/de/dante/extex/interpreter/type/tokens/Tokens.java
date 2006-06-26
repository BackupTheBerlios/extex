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

package de.dante.extex.interpreter.type.tokens;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.MacroParamToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.UnicodeChar;

/**
 * This class is a container for a list of
 * {@link de.dante.extex.scanner.type.token.Token Token}s.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.23 $
 */
public class Tokens implements Serializable, FixedTokens {

    /**
     * This constant is the empty token register.
     */
    public static final Tokens EMPTY = new ImmutableTokens();

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060415L;

    /**
     * The internal list of tokens
     */
    private List tokens = new ArrayList();

    /**
     * Creates a new object which does not contain any elements.
     */
    public Tokens() {

        super();
    }

    /**
     * Creates a new object
     * <p>
     * Each character is converted into a <code>OtherToken</code>
     * and added to the internal list.
     *
     * @param context the interpreter context
     * @param num the number to add
     *
     * @throws InterpreterException in case of an error
     */
    public Tokens(final Context context, final long num)
            throws InterpreterException {

        this(context, Long.toString(num));
    }

    /**
     * Creates a new object
     * <p>
     * Each character of the string is converted into a <code>OtherToken</code>
     * and added to the internal list. An exception is made for spaces which
     * are converted into a <code>SpaceToken</code>.
     *
     * @param context the interpreter context
     * @param s the <code>String</code> to add
     *
     * @throws InterpreterException in case of an error
     */
    public Tokens(final Context context, final String s)
            throws InterpreterException {

        this();
        try {
            add(context.getTokenFactory(), s);
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * Creates a new object.
     *
     * @param t the initial token
     */
    public Tokens(final Token t) {

        super();
        tokens.add(t);
    }

    /**
     * Add another token to the end of the Tokens.
     *
     * @param t The token to add
     */
    public void add(final Token t) {

        tokens.add(t);
    }

    /**
     * Add all characters from the given String to the list of Tokens.
     * If the string is <code>null</code> then it is ignored: i.e. it is treated
     * like the empty string.
     * The Tokens all have the catcode OTHER with exception of spaces which
     * have the catcode SPACE.
     *
     * @param factory the TokenFactory to acquire new Tokens from
     * @param s the String to add
     *
     * @throws CatcodeException in case of an error
     */
    public void add(final TokenFactory factory, final String s)
            throws CatcodeException {

        if (s == null) {
            return;
        }

        char c;
        int len = s.length();

        for (int i = 0; i < len; i++) {
            c = s.charAt(i);
            tokens.add(factory.createToken((c == ' '
                    ? Catcode.SPACE
                    : Catcode.OTHER), c, Namespace.DEFAULT_NAMESPACE));
        }

    }

    /**
     * Add another token list to the end of the Tokens.
     *
     * @param toks the tokens to add
     */
    public void add(final Tokens toks) {

        int len = toks.length();
        for (int i = 0; i < len; i++) {
            tokens.add(toks.get(i));
        }
    }

    /**
     * This method removes all elements from the tokens list. Afterwards the
     * list is empty.
     */
    public void clear() {

        tokens.clear();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object object) {

        if (!(object instanceof Tokens)) {
            return false;
        }
        Tokens toks = (Tokens) object;
        if (toks.length() != length()) {
            return false;
        }
        for (int i = 0; i < length(); i++) {
            if (!get(i).equals(toks.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get a specified token from the toks register.
     *
     * @param i the index for the token to get
     *
     * @return the i<sup>th</sup> token or <code>null</code> if i is out of
     *  bounds
     */
    public Token get(final int i) {

        return (i >= 0 && i < tokens.size() ? (Token) (tokens.get(i)) : null);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        int hash = length();
        for (int i = 0; i < length(); i++) {
            hash += get(i).getChar().getCodePoint();
        }
        return hash;
    }

    /**
     * Add a token to the list at a certain position.
     *
     * @param index the index to add the token to
     * @param t the token to add
     */
    public void insert(final int index, final Token t) {

        tokens.add(index, t);
    }

    /**
     * Getter for the length of the token register, this is the number of
     * elements contained.
     *
     * @return the number of elements in the token register
     */
    public int length() {

        return tokens.size();
    }

    /**
     * Remove the last token from the list and return it. If the list is empty
     * then <code>null</code> is returned.
     *
     * @return the last token or <code>null</code>
     */
    public Token removeLast() {

        if (tokens.size() == 0) {
            return null;
        }
        return (Token) tokens.remove(tokens.size() - 1);
    }

    /**
     * @see de.dante.extex.interpreter.type.tokens.FixedTokens#show(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void show(final Context context, final Tokens toks)
            throws CatcodeException {

        TokenFactory factory = context.getTokenFactory();
        Token t;

        for (int i = 0; i < tokens.size(); i++) {
            t = (Token) (tokens.get(i));
            if (t instanceof ControlSequenceToken) {
                long esc = context.getCount("escapechar").getValue();
                if (esc >= 0) {
                    toks.add(factory.createToken(Catcode.OTHER, (char) (esc),
                            Namespace.DEFAULT_NAMESPACE));
                }
                toks.add(factory, t.toString());
            } else if (t instanceof MacroParamToken) {
                //toks.add(factory.createToken(Catcode.OTHER, '#',
                //        Namespace.DEFAULT_NAMESPACE));
                toks.add(factory.createToken(Catcode.OTHER, t.getChar(),
                        Namespace.DEFAULT_NAMESPACE));
            } else {
                toks.add(factory.createToken(Catcode.OTHER, t.getChar(),
                        Namespace.DEFAULT_NAMESPACE));
            }
        }
    }

    /**
     * Return a String, which show all tokens in the list.
     *
     * @return a String, which show all tokens in the list
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Print the token into a StringBuffer.
     *
     * @param sb the target string buffer
     */
    public void toString(final StringBuffer sb) {

        for (int i = 0; i < tokens.size(); i++) {
            ((Token) tokens.get(i)).toString(sb);
            sb.append("\n  ");
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.tokens.FixedTokens#toText()
     */
    public String toText() {

        StringBuffer sb = new StringBuffer();

        int size = tokens.size();
        for (int i = 0; i < size; i++) {
            Token t = (Token) tokens.get(i);
            sb.append(t.toText());
            if (t instanceof ControlSequenceToken && i != size - 1) {
                sb.append(' ');
            }
        }

        return sb.toString();
    }

    /**
     * @see de.dante.extex.interpreter.type.tokens.FixedTokens#toText(UnicodeChar)
     */
    public String toText(final UnicodeChar esc) {

        StringBuffer sb = new StringBuffer();

        int size = tokens.size();
        for (int i = 0; i < size; i++) {
            Token t = (Token) tokens.get(i);
            sb.append(t.toText(esc));
            if (t instanceof ControlSequenceToken && i != size - 1) {
                sb.append(' ');
            }
        }

        return sb.toString();
    }

}
