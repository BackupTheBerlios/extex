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

package de.dante.extex.scanner;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.Namespace;
import de.dante.util.UnicodeChar;

/**
 * This is a implementation of a token factory. This means that the factory
 * pattern is applied here. This pattern opens the possibility to cache the
 * instances for Tokens to reduce the number of objects present in the system.
 *
 * <h2>The Visitor Pattern</h2>
 * <p>
 * In addition the visitor pattern is used to select the appropriate
 * instantiation method. The visit methods are not meant to be used externally.
 * They are purely internal.
 * Despite their general definition the <tt>visit</tt>
 * methods are in fact used in the following way:
 * </p>
 *
 * <pre>
 *  Token visit<i>*</i>(String value, UnicodeChar character) throws CatcodeException
 * </pre>
 *
 * <p>
 * This means that they are expected to return the new token. The first
 * argument is the value, which is mainly meaningful for control sequence
 * tokens. The third argument contains the Unicode character for single letter
 * tokens.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class TokenFactoryImpl implements TokenFactory, CatcodeVisitor {

    /**
     * The field <tt>crToken</tt> contains the one and only cr token in the
     * system.
     */
    private static final Token CR_TOKEN = new CrToken(" ");

    /**
     * The field <tt>spaceToken</tt> contains the one and only space token in
     * the system.
     */
    private static final Token SPACE_TOKEN = new SpaceToken(" ");

    /**
     * The field <tt>activeCache</tt> contains the cache for active character
     * tokens.
     */
    private Map activeCache = new HashMap();

    /**
     * The field <tt>csCache</tt> contains the cache for control sequence
     * tokens.
     */
    private Map csCache = new HashMap();

    /**
     * The field <tt>leftBraceCache</tt> contains the cache for left brace
     * tokens.
     */
    private Map leftBraceCache = new HashMap();

    /**
     * The field <tt>letterCache</tt> contains the cache for letter tokens.
     */
    private Map letterCache = new HashMap();

    /**
     * The field <tt>macroParamCache</tt> contains the cache for macro parameter
     * tokens.
     */
    private Map macroParamCache = new HashMap();

    /**
     * The field <tt>mathShiftCache</tt> contains the cache for math shift
     * tokens.
     */
    private Map mathShiftCache = new HashMap();

    /**
     * The field <tt>otherCache</tt> contains the cache for other tokens.
     */
    private Map otherCache = new HashMap();

    /**
     * The field <tt>rightBraceCache</tt> contains the cache for right brace
     * tokens.
     */
    private Map rightBraceCache = new HashMap();

    /**
     * The field <tt>subMarkCache</tt> contains the cache for sub mark tokens.
     */
    private Map subMarkCache = new HashMap();

    /**
     * The field <tt>supMarkCache</tt> contains the cache for super mark tokens.
     */
    private Map supMarkCache = new HashMap();

    /**
     * The field <tt>tabMarkCache</tt> contains the cache for tab mark tokens.
     */
    private Map tabMarkCache = new HashMap();

    /**
     * Creates a new object.
     */
    public TokenFactoryImpl() {

        super();
    }

    /**
     * @see de.dante.extex.scanner.TokenFactory#createToken(
     *      de.dante.extex.scanner.Catcode, char, java.lang.String)
     */
    public Token createToken(final Catcode code, final char c,
            final String namespace) throws CatcodeException {

        try {
            return (Token) code
                    .visit(this, null, new UnicodeChar(c), namespace);
        } catch (CatcodeException e) {
            throw e;
        } catch (Exception e) {
            // this should not happen
            throw new CatcodeException(e);
        }
    }

    /**
     * Create a new {@link de.dante.extex.scanner.Token Token} of the
     * appropriate kind. Tokens are immutable (no setters) thus the factory
     * pattern can be applied.
     *
     * @param code the category code
     * @param value the value
     *
     * @return the new token
     *
     * @throws CatcodeException in case of an error
     *
     * @deprecated use newInstance(Catcode,String,String) instead.
     */
    public Token createToken(final Catcode code, final String value)
            throws CatcodeException {

        return createToken(code, value, Namespace.DEFAULT_NAMESPACE);
    }

    /**
     * @see de.dante.extex.scanner.TokenFactory#createToken(
     *      de.dante.extex.scanner.Catcode,
     *      java.lang.String,
     *      java.lang.String)
     */
    public Token createToken(final Catcode code, final String value,
            final String namespace) throws CatcodeException {

        try {
            return (Token) code.visit(this, value, null, namespace);
        } catch (CatcodeException e) {
            throw e;
        } catch (Exception e) {
            // this should not happen
            throw new CatcodeException(e);
        }
    }

    /**
     * @see de.dante.extex.scanner.TokenFactory#createToken(
     *      de.dante.extex.scanner.Catcode,
     *      de.dante.util.UnicodeChar)
     *
     * @deprecated use newInstance(Catcode,UnicodeChar,String) instead.
     */
    public Token createToken(final Catcode code, final UnicodeChar c)
            throws CatcodeException {

        return createToken(code, c, Namespace.DEFAULT_NAMESPACE);
    }

    /**
     * @see de.dante.extex.scanner.TokenFactory#createToken(
     *      de.dante.extex.scanner.Catcode,
     *      de.dante.util.UnicodeChar,
     *      java.lang.String)
     */
    public Token createToken(final Catcode code, final UnicodeChar c,
            final String namespace) throws CatcodeException {

        try {
            return (Token) code.visit(this, null, c, namespace);
        } catch (CatcodeException e) {
            throw e;
        } catch (Exception e) {
            // this should not happen
            throw new CatcodeException(e);
        }
    }

    /**
     * Active characters are cached. Thus a lookup in the cache preceeds the
     * creation of a new token.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitActive(final Object oValue, final Object oChar,
            final Object oNamespace) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        String namespace = (String) oNamespace;
        Map map = (Map) (activeCache.get(namespace));

        if (map == null) {
            map = new HashMap();
            activeCache.put(namespace, map);
        }

        Object token = map.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new ActiveCharacterToken(uc, (String) oNamespace);
            map.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * Comments are ignored thus <code>null</code> is returned in any case.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitComment(final Object oValue, final Object oChar,
            final Object ignore) {

        return null;
    }

    /**
     * There is only one CrToken.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitCr(final Object oValue, final Object oChar,
            final Object ignore) {

        return CR_TOKEN;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitEscape(final Object oValue, final Object oChar,
            final Object oNamespace) throws CatcodeException {

        String value;
        if (oValue != null) {
            value = (String) oValue;
        } else if (oChar != null) {
            value = ((UnicodeChar) oChar).toString();
        } else {
            throw new CatcodeVisitorException();
        }

        String namespace = (String) oNamespace;
        Map map = (Map) (csCache.get(namespace));

        if (map == null) {
            map = new HashMap();
            csCache.put(namespace, map);
        }

        Object token = map.get(value);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new ControlSequenceToken(value, (String) oNamespace);
            map.put(value, new WeakReference(token));
        }

        return token;
    }

    /**
     * Ignored characters are simply ignored;-)
     *
     * @param oValue the string value token or <code>null</code>
     * @param oChar the requested character code
     * @param ignore the third argument is ignored
     *
     * @return <code>null</code>
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitIgnore(final Object oValue, final Object oChar,
            final Object ignore) {

        return null;
    }

    /**
     * Invalid characters are ignored; even without any error message.
     *
     * @param oValue the string value token or <code>null</code>
     * @param oChar the requested character code
     * @param ignore the third argument is ignored
     *
     * @return <code>null</code>
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitInvalid(final Object oValue, final Object oChar,
            final Object ignore) {

        return null;
    }

    /**
     * A left brace token is expected to take a single character only.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(
     *      java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitLeftBrace(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = leftBraceCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new LeftBraceToken(uc);
            leftBraceCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * A letter token is expected to take a single character only.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitLetter(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = letterCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new LetterToken(uc);
            letterCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(
     *      java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitMacroParam(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = macroParamCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new MacroParamToken(uc);
            macroParamCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(
     *      java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitMathShift(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = mathShiftCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new MathShiftToken(uc);
            mathShiftCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitOther(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = otherCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new OtherToken(uc);
            otherCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(
     *      java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitRightBrace(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = rightBraceCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new RightBraceToken(uc);
            rightBraceCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * There is only one space token. It has the character code 32.
     *
     * @param oValue the string value token or <code>null</code>
     * @param oChar the requested character code
     * @param ignore the third argument is ignored
     *
     * @return the space token.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     * @see "The TeXbook [Chapter 8, p. 47]"
     */
    public Object visitSpace(final Object oValue, final Object oChar,
            final Object ignore) {

        return SPACE_TOKEN;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public Object visitSubMark(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = subMarkCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new SubMarkToken(uc);
            subMarkCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,
     *       java.lang.Object, java.lang.Object)
     */
    public Object visitSupMark(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = supMarkCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new SupMarkToken(uc);
            supMarkCache.put(uc, new WeakReference(token));
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,
     *       java.lang.Object, java.lang.Object)
     */
    public Object visitTabMark(final Object oValue, final Object oChar,
            final Object ignore) throws CatcodeException {

        UnicodeChar uc;
        if (oChar != null) {
            uc = (UnicodeChar) oChar;
        } else if (oValue != null) {
            String value = (String) oValue;
            if (value.length() != 1) {
                throw new CatcodeWrongLengthException(value);
            }
            uc = new UnicodeChar(value.charAt(0));
        } else {
            throw new CatcodeVisitorException();
        }

        Object token = tabMarkCache.get(uc);

        if (token != null) {
            token = ((WeakReference) token).get();
        }
        if (token == null) {
            token = new TabMarkToken(uc);
            tabMarkCache.put(uc, new WeakReference(token));
        }

        return token;
    }

}