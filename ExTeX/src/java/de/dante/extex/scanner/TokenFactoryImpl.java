/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.scanner;

import de.dante.util.GeneralException;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a implementation of a token factory.
 * This means that the factory pattern is applied here. This pattern opens the
 * possibility to cache the instances for Tokens to reduce the number of
 * objects present in the system.
 * <p>
 * In addition the visitor pattern is used to select the appropriate
 * instanciation method.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TokenFactoryImpl implements TokenFactory,
                                         CatcodeVisitor {
    /** cache for active character tokens */
    private Map activeCache = new HashMap();

    /** ... */
    private Map crCache = new HashMap();

    /** cache for control sequence tokens */
    private Map csCache = new HashMap();

    /** cache for left brace tokens */
    private Map leftBraceCache = new HashMap();

    /** cache for letter tokens */
    private Map letterCache = new HashMap();

    /** cache for macro parameter tokens */
    private Map macroParamCache = new HashMap();

    /** cache for other tokens */
    private Map otherCache = new HashMap();

    /** ... */
    private Map rightBraceCache = new HashMap();

    /** ... */
    private Map spaceCache   = new HashMap();

    /** ... */
    private Map subMarkCache = new HashMap();

    /** ... */
    private Map supMarkCache = new HashMap();

    /** ... */
    private Map tabMarkCache = new HashMap();

    /**
     * Creates a new object.
     */
    public TokenFactoryImpl() {
        super();
    }

    /**
     * Create a new {@link de.dante.extex.scanner.Token Token} of the
     * appropriate kind.
     * Tokens are immutable (no setters) thus the factory pattern can be
     * applied.
     *
     * @param code the category code
     * @param value the value
     *
     * @return the new token
     *
     * @throws GeneralException in case of an error
     */
    public Token newInstance(Catcode code, String value)
                      throws GeneralException {
        return (Token) code.visit((CatcodeVisitor) this, value, null);
    }

    /**
     * Create a new {@link de.dante.extex.scanner.Token Token} of the
     * appropriate kind.
     * Tokens are immutable (no setters) thus the factory pattern can be
     * applied.
     *
     * @param code the category code
     * @param c the character value
     *
     * @return the new token
     *
     * @throws GeneralException in case of an error
     */
    public Token newInstance(Catcode code, char c)
                      throws GeneralException {
        return (Token) code.visit((CatcodeVisitor) this,
                                  Character.toString(c), null);
    }

    /**
     * Active characters are cached. Thus a lookup in the cache preceeds the
     * creation of a new token
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,java.lang.Object)
     */
    public Object visitActive(Object value, Object ignore) {
        Object token = activeCache.get(value);

        if (token == null) {
            token = new ActiveCharacterToken((String) value);
            activeCache.put(value, token);
        }

        return token;
    }

    /**
     * Comments are ignored thus <code>null</code> is returned in any case.
     *
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,java.lang.Object)
     */
    public Object visitComment(Object value, Object ignore) {
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,java.lang.Object)
     */
    public Object visitCr(Object value, Object ignore) {
        Object token = crCache.get(value);

        if (token == null) {
            token = new CrToken((String) value);
            crCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,java.lang.Object)
     */
    public Object visitEscape(Object value, Object ignore) {
        Object token = csCache.get(value);

        if (token == null) {
            token = new ControlSequenceToken((String) value);
            csCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,java.lang.Object)
     */
    public Object visitIgnore(Object value, Object ignore) {
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,java.lang.Object)
     */
    public Object visitInvalid(Object value, Object ignore) {
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitLeftBrace(Object value, Object ignore) {
        Object token = leftBraceCache.get(value);

        if (token == null) {
            token = new LeftBraceToken((String) value);
            leftBraceCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,java.lang.Object)
     */
    public Object visitLetter(Object value, Object ignore) {
        Object token = letterCache.get(value);

        if (token == null) {
            token = new LetterToken((String) value);
            letterCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,java.lang.Object)
     */
    public Object visitMacroParam(Object value, Object ignore) {
        Object token = macroParamCache.get(value);

        if (token == null) {
            token = new MacroParamToken((String) value);
            macroParamCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object,java.lang.Object)
     */
    public Object visitMathShift(Object value, Object ignore) {
        return new MathShiftToken((String) value);
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,java.lang.Object)
     */
    public Object visitOther(Object value, Object ignore) {
        Object token = otherCache.get(value);

        if (token == null) {
            token = new OtherToken((String) value);
            otherCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(java.lang.Object,java.lang.Object)
     */
    public Object visitRightBrace(Object value, Object ignore) {
        Object token = rightBraceCache.get(value);

        if (token == null) {
            token = new RightBraceToken((String) value);
            rightBraceCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,java.lang.Object)
     */
    public Object visitSpace(Object value, Object ignore) {
        Object token = spaceCache.get(value);

        if (token == null) {
            token = new SpaceToken((String) value);
            spaceCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSubMark(Object value, Object ignore) {
        Object token = subMarkCache.get(value);

        if (token == null) {
            token = new SubMarkToken((String) value);
            subMarkCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,java.lang.Object)
     */
    public Object visitSupMark(Object value, Object ignore) {
        Object token = supMarkCache.get(value);

        if (token == null) {
            token = new SupMarkToken((String) value);
            supMarkCache.put(value, token);
        }

        return token;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,java.lang.Object)
     */
    public Object visitTabMark(Object value, Object ignore) {
        Object token = tabMarkCache.get(value);

        if (token == null) {
            token = new TabMarkToken((String) value);
            tabMarkCache.put(value, token);
        }

        return token;
    }
}
