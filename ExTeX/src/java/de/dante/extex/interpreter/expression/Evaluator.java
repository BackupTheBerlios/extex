/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.expression.exception.CastException;
import de.dante.extex.interpreter.expression.exception.UnsupportedException;
import de.dante.extex.interpreter.expression.term.Accumulator;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides some static methods to parse an expression and return its
 * value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Evaluator {

    /**
     * The field <tt>ASSIGN</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction ASSIGN = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg2;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return ":=";
        }

    };

    /**
     * The field <tt>EQ</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction EQ = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.eq(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "==";
        }

    };

    /**
     * The field <tt>GE</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction GE = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.ge(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return ">=";
        }

    };

    /**
     * The field <tt>GT</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction GT = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.gt(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return ">";
        }

    };

    /**
     * The field <tt>LAND</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction LAND = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.and(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "&&";
        }

    };

    /**
     * The field <tt>LE</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction LE = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.le(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "<=";
        }

    };

    /**
     * The field <tt>LOR</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction LOR = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.or(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "||";
        }

    };

    /**
     * The field <tt>LT</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction LT = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.lt(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "<";
        }

    };

    /**
     * The field <tt>MINUS</tt> contains the subtractor.
     */
    private static final BinaryFunction MINUS = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws HelpingException {

            return arg1.subtract(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "-";
        }

    };

    /**
     * The field <tt>LAND</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final BinaryFunction NE = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws CastException,
                    UnsupportedException {

            return arg1.ne(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "!=";
        }

    };

    /**
     * The field <tt>PLUS</tt> contains the adder.
     */
    private static final BinaryFunction PLUS = new BinaryFunction() {

        /**
         * @see de.dante.extex.interpreter.expression.Function2#apply(
         *      de.dante.extex.interpreter.expression.EType,
         *      de.dante.extex.interpreter.expression.EType)
         */
        public EType apply(final EType arg1, final EType arg2)
                throws HelpingException {

            return arg1.add(arg2);
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {

            return "+";
        }

    };

    /**
     * The field <tt>UC_AND</tt> contains the Unicode code point for the logical
     * and.
     */
    private static final int UC_AND = 0x2227;

    /**
     * The field <tt>UC_GE</tt> contains the Unicode code point for the greater
     * or equal sign.
     */
    private static final int UC_GE = 0x2265;

    /**
     * The field <tt>UC_LE</tt> contains the Unicode code point for the less
     * or equal sign.
     */
    private static final int UC_LE = 0x2264;

    /**
     * The field <tt>UC_NE</tt> contains the Unicode code point for the not
     * equal sign.
     */
    private static final int UC_NE = 0x2260;

    /**
     * The field <tt>UC_NOT</tt> contains the Unicode code point for the
     * logical not sign.
     */
    private static final int UC_NOT = 0xac;

    /**
     * The field <tt>UC_OR</tt> contains the Unicode code point for the logical
     * or.
     */
    private static final int UC_OR = 0x2228;

    /**
     * Find the next comma after any white-space and discard it and the
     * white-space afterwards.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @throws InterpreterException in case of an error
     */
    protected static void skipComma(final Context context,
            final TokenSource source) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException();
        } else if (!t.equals(Catcode.OTHER, ',')) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(Evaluator.class), "MissingComma", t
                    .toString());
        }
        source.skipSpace();
    }

    /**
     * The field <tt>functions</tt> contains the function object attached to a
     * function name.
     */
    private Map functions = new HashMap();

    /**
     * The field <tt>parsers</tt> contains the list of registered parsers.
     */
    private List parsers = new ArrayList();

    /**
     * Creates a new object.
     */
    public Evaluator() {

        super();
    }

    /**
     * Creates a new object from a token stream.
     *
     * @param term the terminal to store the result in
     * @param context the interpreter context
     * @param source the source for next tokens
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    public void eval(final EType term, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        term.set(evalLogicExpressionOrFunctionalExpression(context, source,
                typesetter));
        source.skipSpace();
        return;
    }

    /**
     * Evaluate an expression.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalExpression(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        return evalExpression(evalTerm(context, source, typesetter), //
                context, source, typesetter);
    }

    /**
     * Evaluate an expression.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalExpression(final EType start, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        EType accumulator = null;
        EType savedValue = start;
        BinaryFunction op = ASSIGN;

        for (Token t = source.getNonSpace(context); t != null; t = source
                .getNonSpace(context)) {

            int c = (t instanceof OtherToken //
                    ? t.getChar().getCodePoint()//
                    : '\0');

            switch (c) {
                case '+':
                    accumulator = op.apply(accumulator, savedValue);
                    savedValue = evalTerm(context, source, typesetter);
                    op = PLUS;
                    break;
                case '-':
                    accumulator = op.apply(accumulator, savedValue);
                    savedValue = evalTerm(context, source, typesetter);
                    op = MINUS;
                    break;
                case '*':
                    savedValue = savedValue.multiply(evalTerm(context, source,
                            typesetter));
                    break;
                case '/':
                    savedValue = savedValue.divide(evalTerm(context, source,
                            typesetter));
                    break;
                default:
                    source.push(t);
                    return op.apply(accumulator, savedValue);
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Evaluate some expression in parentheses.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalGroup(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        EType a = evalTerm(context, source, typesetter);
        Token t = source.getNonSpace(context);
        if (t != null && t.equals(Catcode.OTHER, ')')) {
            return a;
        }
        source.push(t);
        if (t instanceof OtherToken) {
            int c = t.getChar().getCodePoint();
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                a = evalExpression(a, context, source, typesetter);
                t = source.getNonSpace(context);
                if (t != null && t.equals(Catcode.OTHER, ')')) {
                    return a;
                }
            }
            BinaryFunction op = getOp(context, source);
            if (op != null) {
                a = op.apply(a, evalExpression(context, source, typesetter));
                t = source.getNonSpace(context);
                if (t != null && t.equals(Catcode.OTHER, ')')) {
                    return a;
                }
                source.push(t);
            }
            op = getJunctor(context, source);
            if (op != null) {
                a = evalJunction(a, op, context, source, typesetter);
                t = source.getNonSpace(context);
                if (t != null && t.equals(Catcode.OTHER, ')')) {
                    return a;
                }
                source.push(t);
            }
        }

        throw new HelpingException(LocalizerFactory
                .getLocalizer(Evaluator.class), "MissingParenthesis", //
                (t == null ? "null" : t.toString()));
    }

    /**
     * Evaluate a logical junction expression.
     *
     * @param start the value of the first junction element
     * @param junctor the junctor, i.e. && or ||
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalJunction(final EType start, final BinaryFunction junctor,
            final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        EType a = start;

        for (;;) {
            a = junctor.apply(a, evalLogicTerm(context, source, typesetter));
            if (!getJunctor(junctor, context, source)) {
                return a;
            }
        }
    }

    /**
     * Evaluate a logical expression.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalLogicTerm(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new MissingNumberException();

        } else if (t instanceof OtherToken) {
            switch (t.getChar().getCodePoint()) {
                case '(':
                    //TODO gene: unimplemented
                    throw new RuntimeException("unimplemented");
                case '!':
                case UC_NOT:
                    EType ac = evalLogicExpressionOrFunctionalExpression(
                            context, source, typesetter);
                    ac.not();
                    return ac;
                default:
            // ...
            }
        }

        source.push(t);
        EType accumulator = evalTerm(context, source, typesetter);

        BinaryFunction op = getOp(context, source);
        if (op != null) {
            return op.apply(accumulator, evalTerm(context, source, typesetter));
        }
        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

    /**
     * Evaluate a logical expression.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalLogicExpressionOrFunctionalExpression(
            final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new MissingNumberException();

        } else if (t instanceof OtherToken) {
            switch (t.getChar().getCodePoint()) {
                case '!':
                case UC_NOT:
                    EType ac = evalLogicExpressionOrFunctionalExpression(
                            context, source, typesetter);
                    ac.not();
                    return ac;
                default:
            // ...
            }
        }

        source.push(t);
        EType accumulator = evalTerm(context, source, typesetter);

        BinaryFunction op = getOp(context, source);
        if (op != null) {
            return op.apply(accumulator, evalTerm(context, source, typesetter));
        }
        op = getJunctor(context, source);
        if (op != null) {
            return op.apply(accumulator,
                    evalLogicExpressionOrFunctionalExpression(context, source,
                            typesetter));
        }
        return accumulator;
    }

    /**
     * Evaluate a terminal.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private EType evalTerm(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        for (Token t = source.getNonSpace(context); t != null; t = source
                .getNonSpace(context)) {

            if (t instanceof OtherToken) {
                switch (t.getChar().getCodePoint()) {
                    case '+':
                        // continue
                        break;
                    case '-':
                        EType accumulator = evalTerm(context, source,
                                typesetter);
                        accumulator = accumulator.negate();
                        return accumulator;
                    case '(':
                        return evalGroup(context, source, typesetter);

                    default:
                        source.push(t);
                        for (int i = 0; i < parsers.size(); i++) {
                            EType term = ((ETypeParser) parsers.get(i)).parse(
                                    context, source, typesetter);
                            if (term != null) {
                                return term;
                            }
                        }
                }
                break;

            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);

                for (int i = 0; i < parsers.size(); i++) {
                    EType term = ((ETypeParser) parsers.get(i)).convert(code,
                            context, source, typesetter);
                    if (term != null) {
                        return term;
                    }
                }

                if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                } else {
                    break;
                }
            } else if (t instanceof LetterToken) {
                Tokens tokens = new Tokens(t);
                for (t = source.getToken(context); t != null
                        && t instanceof LetterToken; t = source
                        .getToken(context)) {
                    tokens.add(t);
                }
                source.push(t);
                String name = tokens.toText();

                Object f = functions.get(name);
                if (f == null) {
                    source.push(tokens);
                    break;
                }
                if (f instanceof ConstantFunction) {
                    return ((ConstantFunction) f).apply();
                }
                if (f instanceof UnaryFunction) {
                    return ((UnaryFunction) f).apply(evalTerm(context, source,
                            typesetter));
                }
                t = source.getNonSpace(context);
                if (t == null) {
                    throw new EofException();
                } else if (!t.equals(Catcode.OTHER, '(')) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(Evaluator.class),
                            "MissingOpenParenthesis", name, t.toString());
                }
                EType accumulator = new Accumulator();
                if (f instanceof BinaryFunction) {
                    EType arg1 = evalExpression(context, source, typesetter);
                    skipComma(context, source);
                    EType arg2 = evalExpression(context, source, typesetter);
                    accumulator = ((BinaryFunction) f).apply(arg1, arg2);
                } else if (f instanceof ParsingFunction) {
                    accumulator = ((ParsingFunction) f).apply(context, source,
                            typesetter);
                } else {
                    break;
                }

                t = source.getNonSpace(context);
                if (t == null) {
                    throw new EofException();
                } else if (!t.equals(Catcode.OTHER, ')')) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(Evaluator.class),
                            "MissingParenthesis", t.toString());
                }
                source.skipSpace();
                return accumulator;

            } else {
                break;
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Get an logical junction operator.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the function constant associated to the operator found or
     *  <code>null</code> if none was found
     *
     * @throws InterpreterException in case of an error
     */
    private BinaryFunction getJunctor(final Context context,
            final TokenSource source) throws InterpreterException {

        Token t = source.getNonSpace(context);
        Token t2;

        if (t instanceof OtherToken) {
            switch (t.getChar().getCodePoint()) {
                case UC_AND:
                    return LAND;
                case UC_OR:
                    return LOR;
                case '&':
                    t2 = source.getToken(context);
                    if (t2 != null && t2.equals(Catcode.OTHER, '&')) {
                        return LAND;
                    }
                    source.push(t2);
                    break;
                case '|':
                    t2 = source.getToken(context);
                    if (t2 == null || t2.equals(Catcode.OTHER, '|')) {
                        return LOR;
                    }
                    source.push(t2);
                    break;
                default:
            // fall-through to report nothing
            }
        }
        source.push(t);
        return null;
    }

    /**
     * get a certain junctor from the token stream.
     *
     * @param junctor the junctor to look for
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return <code>true</code> iff the junctor has been found
     *
     * @throws InterpreterException in case of an error
     */
    private boolean getJunctor(final BinaryFunction junctor,
            final Context context, final TokenSource source)
            throws InterpreterException {

        Token t = source.getNonSpace(context);

        if (junctor == LAND) {

            if (t instanceof OtherToken) {
                switch (t.getChar().getCodePoint()) {
                    case UC_AND:
                        return true;
                    case '&':
                        Token t2 = source.getToken(context);
                        if (t2 != null && t2.equals(Catcode.OTHER, '&')) {
                            return true;
                        }
                        source.push(t2);
                        break;
                    default:
                // fall-through to report nothing
                }
            }
            source.push(t);

        } else if (junctor == LOR) {

            if (t instanceof OtherToken) {
                switch (t.getChar().getCodePoint()) {
                    case UC_OR:
                        return true;
                    case '|':
                        Token t2 = source.getToken(context);
                        if (t2 != null && t2.equals(Catcode.OTHER, '|')) {
                            return true;
                        }
                        source.push(t2);
                        break;
                    default:
                // fall-through to report nothing
                }
            }
            source.push(t);

        } else {
            //TODO gene: unimplemented
            throw new RuntimeException("unimplemented");
        }

        return false;
    }

    /**
     * Get an comparison operator.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the function constant associated to the operator found or
     *  <code>null</code> if none was found
     *
     * @throws InterpreterException in case of an error
     */
    private BinaryFunction getOp(final Context context, final TokenSource source)
            throws InterpreterException {

        Token t = source.getNonSpace(context);

        if (t instanceof OtherToken) {
            switch (t.getChar().getCodePoint()) {
                case UC_LE:
                    return LE;
                case UC_GE:
                    return GE;
                case UC_NE:
                    return NE;
                case '!':
                    t = source.getToken(context);
                    if (t != null && t.equals(Catcode.OTHER, '=')) {
                        return NE;
                    }
                    break;
                case '=':
                    t = source.getToken(context);
                    if (t == null || !t.equals(Catcode.OTHER, '=')) {
                        source.push(t);
                    }
                    return EQ;
                case '<':
                    t = source.getToken(context);
                    if (t == null || !t.equals(Catcode.OTHER, '=')) {
                        source.push(t);
                        return LT;
                    }
                    return LE;
                case '>':
                    t = source.getToken(context);
                    if (t == null || !t.equals(Catcode.OTHER, '=')) {
                        source.push(t);
                        return GT;
                    }
                    return GE;
                default:
            // fall-through to report nothing
            }
        }
        source.push(t);
        return null;
    }

    /**
     * Register a Terminal for usage.
     * the registered instance is used to access the parser and converter
     * methods.
     *
     * @param parser the terminal parser instance
     */
    public void register(final ETypeParser parser) {

        parsers.add(parser);
        parser.registered(this);
    }

    /**
     * Register a function in the evaluator.
     *
     * @param name the name of the function in the expression
     * @param function the function object
     */
    public void register(final String name, final ParsingFunction function) {

        functions.put(name, function);
    }

    /**
     * Register a constant in the evaluator.
     *
     * @param name the name of the function in the expression
     * @param function the function object
     */
    public void register(final String name, final ConstantFunction function) {

        functions.put(name, function);
    }

    /**
     * Register a unary function in the evaluator.
     *
     * @param name the name of the function in the expression
     * @param function the function object
     */
    public void register(final String name, final UnaryFunction function) {

        functions.put(name, function);
    }

    /**
     * Register a binary function in the evaluator.
     *
     * @param name the name of the function in the expression
     * @param function the function object
     */
    public void register(final String name, final BinaryFunction function) {

        functions.put(name, function);
    }

}
