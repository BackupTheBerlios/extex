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

package de.dante.extex.interpreter.type.dimen.parser;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.interpreter.type.scaled.ScaledConvertible;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
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
public final class LengthParser {

    /**
     * The field <tt>ASSIGN</tt> contains the operation to assign the second
     * argument to the first one.
     */
    private static final Function2 ASSIGN = new Function2() {

        /**
         * @see de.dante.extex.interpreter.type.dimen.parser.Function2#apply(
         *      de.dante.extex.interpreter.type.dimen.parser.Accumulator,
         *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
         */
        public void apply(final Accumulator arg1, final Accumulator arg2) {

            arg1.value = arg2.value;
            arg1.sp = arg2.sp;
        }
    };
    /**
     * The field <tt>functions</tt> contains the function object attached to a
     * function name.
     */
    private static Map functions = new HashMap();

    /**
     * The field <tt>MINUS</tt> contains the subtractor.
     */
    private static final Function2 MINUS = new Function2() {

        /**
         * @see de.dante.extex.interpreter.type.dimen.parser.Function2#apply(
         *      de.dante.extex.interpreter.type.dimen.parser.Accumulator,
         *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
         */
        public void apply(final Accumulator arg1, final Accumulator arg2)
                throws HelpingException {

            if (arg1.sp != arg2.sp) {
                throw new HelpingException(LocalizerFactory
                        .getLocalizer(LengthParser.class.getName()),
                        "IncompatibleUnit", "-", "sp^"
                                + Integer.toString(arg1.sp), "sp^"
                                + Integer.toString(arg2.sp));
            }
            arg1.value -= arg2.value;
        }
    };

    /**
     * The field <tt>PLUS</tt> contains the adder.
     */
    private static final Function2 PLUS = new Function2() {

        /**
         * @see de.dante.extex.interpreter.type.dimen.parser.Function2#apply(
         *      de.dante.extex.interpreter.type.dimen.parser.Accumulator,
         *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
         */
        public void apply(final Accumulator arg1, final Accumulator arg2)
                throws HelpingException {

            if (arg1.sp != arg2.sp) {
                throw new HelpingException(LocalizerFactory
                        .getLocalizer(LengthParser.class.getName()),
                        "IncompatibleUnit", "+", "sp^"
                                + Integer.toString(arg1.sp), "sp^"
                                + Integer.toString(arg2.sp));
            }
            arg1.value += arg2.value;
        }
    };

    static {
        functions.put("abs", new Function0() {

            /**
             * Compute the absolute value by eliminating the sign if present.
             *
             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
             */
            public void apply(final Accumulator accumulator)
                    throws InterpreterException {

                if (accumulator.value < 0) {
                    accumulator.value = -accumulator.value;
                }
            }
        });

        functions.put("max", new Function() {

            /**
             * Compute the maximum of an arbitrary number of arguments.
             *
             * @param accumulator the accumulator to receive the result
             * @param context the interpreter context
             * @param source the source for new tokens
             * @param typesetter the typesetter
             *
             * @throws InterpreterException in case of an error
             */
            public void apply(final Accumulator accumulator,
                    final Context context, final TokenSource source,
                    final Typesetter typesetter) throws InterpreterException {

                Token t;
                evalExpr(accumulator, context, source, typesetter);
                for (t = source.getNonSpace(context); t != null
                        && t.equals(Catcode.OTHER, ','); t = source
                        .getNonSpace(context)) {
                    Accumulator x = new Accumulator();
                    evalExpr(x, context, source, typesetter);
                    if (accumulator.sp != x.sp) {
                        throw new HelpingException(LocalizerFactory
                                .getLocalizer(LengthParser.class.getName()),
                                "IncompatibleUnit", "max", "sp^"
                                        + Integer.toString(accumulator.sp),
                                "sp^" + Integer.toString(x.sp));
                    }
                    if (accumulator.value < x.value) {
                        accumulator.value = x.value;
                    }
                }
                source.push(t);
            }
        });

        functions.put("min", new Function() {

            /**
             * Compute the minimum of an arbitrary number of arguments.
             *
             * @param accumulator the accumulator to receive the result
             * @param context the interpreter context
             * @param source the source for new tokens
             * @param typesetter the typesetter
             *
             * @throws InterpreterException in case of an error
             */
            public void apply(final Accumulator accumulator,
                    final Context context, final TokenSource source,
                    final Typesetter typesetter) throws InterpreterException {

                Token t;
                evalExpr(accumulator, context, source, typesetter);
                for (t = source.getNonSpace(context); t != null
                        && t.equals(Catcode.OTHER, ','); t = source
                        .getNonSpace(context)) {
                    Accumulator x = new Accumulator();
                    evalExpr(x, context, source, typesetter);
                    if (accumulator.sp != x.sp) {
                        throw new HelpingException(LocalizerFactory
                                .getLocalizer(LengthParser.class.getName()),
                                "IncompatibleUnit", "min", "sp^"
                                        + Integer.toString(accumulator.sp),
                                "sp^" + Integer.toString(x.sp));
                    }
                    if (accumulator.value > x.value) {
                        accumulator.value = x.value;
                    }
                }
                source.push(t);
            }
        });

        functions.put("sgn", new Function1() {

            /**
             * Compute the sign i.e. return 1 for positive values, 0 for zero
             * values and -1 for negative values.
             *
             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
             */
            public void apply(final Accumulator accumulator)
                    throws InterpreterException {

                if (accumulator.value > 0) {
                    accumulator.value = 1;
                } else if (accumulator.value < 0) {
                    accumulator.value = -1;
                } else {
                    accumulator.value = 0;
                }
                accumulator.sp = 0;
            }
        });

        functions.put("cos", new Function1() {

            /**
             * Compute the cos value.
             *
             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
             */
            public void apply(final Accumulator accumulator)
                    throws InterpreterException {

                if (accumulator.sp != 0) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(LengthParser.class.getName()),
                            "NonScalar", "cos", accumulator.toString());
                }
                double x = ((double) accumulator.value) / ScaledNumber.ONE;
                accumulator.value = (long) (ScaledNumber.ONE * Math.cos(x));
            }
        });

        functions.put("sin", new Function1() {

            /**
             * Compute the sin value.
             *
             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
             */
            public void apply(final Accumulator accumulator)
                    throws InterpreterException {

                if (accumulator.sp != 0) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(LengthParser.class.getName()),
                            "NonScalar", "sin", accumulator.toString());
                }
                double x = ((double) accumulator.value) / ScaledNumber.ONE;
                accumulator.value = (long) (ScaledNumber.ONE * Math.sin(x));
            }
        });

        functions.put("tan", new Function1() {

            /**
             * Compute the tan value.
             *
             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
             */
            public void apply(final Accumulator accumulator)
                    throws InterpreterException {

                if (accumulator.sp != 0) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(LengthParser.class.getName()),
                            "NonScalar", "tan", accumulator.toString());
                }
                double x = ((double) accumulator.value) / ScaledNumber.ONE;
                accumulator.value = (long) (ScaledNumber.ONE * Math.tan(x));
            }
        });

        functions.put("pi", new Function0() {

            /**
             * Compute the value of pi.
             *
             * @param accumulator the accumulator to receive the result
             */
            public void apply(final Accumulator accumulator) {

                accumulator.sp = 0;
                accumulator.value = 205887;
            }
        });
    }

    /**
     * Evaluate an expression.
     *
     * @param accumulator the accumulator to receive the result
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    private static void evalExpr(final Accumulator accumulator,
            final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token t;
        Function2 op = ASSIGN;
        Accumulator val = new Accumulator();
        evalTerm(val, context, source, typesetter);

        for (t = source.getNonSpace(context); t != null; t = source
                .getNonSpace(context)) {

            if (t.equals(Catcode.OTHER, '*')) {
                Accumulator x = new Accumulator();
                evalTerm(x, context, source, typesetter);
                val.value *= x.value;
                if (x.sp == 0) {
                    val.value /= ScaledNumber.ONE;
                } else {
                    val.value /= Dimen.ONE;
                    val.sp += x.sp;
                }

            } else if (t.equals(Catcode.OTHER, '/')) {
                Accumulator x = new Accumulator();
                evalTerm(x, context, source, typesetter);
                if (x.value == 0) {
                    throw new ArithmeticOverflowException("");
                }
                if (x.sp == 0) {
                    val.value *= ScaledNumber.ONE;
                } else {
                    val.value *= Dimen.ONE;
                    val.sp -= x.sp;
                }
                val.value /= x.value;

            } else if (t.equals(Catcode.OTHER, '+')) {
                op.apply(accumulator, val);
                evalTerm(val, context, source, typesetter);
                op = PLUS;

            } else if (t.equals(Catcode.OTHER, '-')) {
                op.apply(accumulator, val);
                evalTerm(val, context, source, typesetter);
                op = MINUS;

            } else {
                source.push(t);
                op.apply(accumulator, val);
                return;
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Evaluate a terminal.
     *
     * @param accumulator the accumulator to receive the result
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    public static void evalTerm(final Accumulator accumulator,
            final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        for (Token t = source.getNonSpace(context); t != null; t = source
                .getNonSpace(context)) {

            if (t instanceof OtherToken) {
                if (t.equals(Catcode.OTHER, '(')) {
                    evalExpr(accumulator, context, source, typesetter);
                    t = source.getNonSpace(context);
                    if (t.equals(Catcode.OTHER, ')')) {
                        return;
                    }

                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(LengthParser.class.getName()),
                            "MissingParenthesis", (t == null ? "null" : t
                                    .toString()));

                } else if (t.equals(Catcode.OTHER, '-')) {
                    evalTerm(accumulator, context, source, typesetter);
                    accumulator.value = -accumulator.value;
                    return;

                } else if (t.equals(Catcode.OTHER, '+')) {
                    // continue
                } else {

                    long value = ScaledNumber.scanFloat(context, source,
                            typesetter, t);

                    GlueComponent gc = Dimen.attachUnit(value, context, source,
                            typesetter, false);
                    if (gc == null) {
                        accumulator.value = value;
                        accumulator.sp = 0;
                    } else {
                        accumulator.value = gc.getValue();
                        accumulator.sp = 1;
                    }

                    return;
                }
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof DimenConvertible) {
                    accumulator.value = ((DimenConvertible) code).convertDimen(
                            context, source, typesetter);
                    accumulator.sp = 1;
                    return;

                } else if (code instanceof ScaledConvertible) {
                    accumulator.value = ((ScaledConvertible) code)
                            .convertScaled(context, source, typesetter);
                    accumulator.sp = 0;
                    return;

                } else if (code instanceof CountConvertible) {
                    accumulator.value = ((CountConvertible) code).convertCount(
                            context, source, typesetter)
                            * ScaledNumber.ONE;
                    accumulator.sp = 0;
                    return;

                } else if (code instanceof ExpandableCode) {
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
                if (f instanceof Function0) {
                    ((Function0) f).apply(accumulator);
                    return;
                }
                if (f instanceof Function1) {
                    evalTerm(accumulator, context, source, typesetter);
                    ((Function1) f).apply(accumulator);
                    return;
                }
                t = source.getNonSpace(context);
                if (t == null) {
                    throw new EofException();
                } else if (!t.equals(Catcode.OTHER, '(')) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(LengthParser.class.getName()),
                            "MissingOpenParenthesis", name, t.toString());
                }
                if (f instanceof Function2) {
                    Accumulator arg2 = new Accumulator();
                    evalExpr(accumulator, context, source, typesetter);
                    skipComma(context, source);
                    evalExpr(arg2, context, source, typesetter);
                    ((Function) f).apply(accumulator, context, source,
                            typesetter);
                } else if (f instanceof Function) {
                    ((Function) f).apply(accumulator, context, source,
                            typesetter);
                } else {
                    break;
                }

                t = source.getNonSpace(context);
                if (t == null) {
                    throw new EofException();
                } else if (!t.equals(Catcode.OTHER, ')')) {
                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(LengthParser.class.getName()),
                            "MissingParenthesis", t.toString());
                }
                source.skipSpace();
                return;

            } else {
                break;
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Creates a new object from a token stream.
     *
     * @param context the interpreter context
     * @param source the source for next tokens
     * @param typesetter the typesetter
     *
     * @return a new instance with the value acquired
     *
     * @throws InterpreterException in case of an error
     */
    public static Dimen parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Accumulator accumulator = new Accumulator();
        evalTerm(accumulator, context, source, typesetter);
        if (accumulator.sp != 1) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(LengthParser.class.getName()),
                    (accumulator.sp == 0 ? "MissingUnit" : "IllegalUnit"),
                    "sp^" + Integer.toString(accumulator.sp));
        }
        source.skipSpace();
        return new Dimen(accumulator.value);
    }

    public static void register(final String name, final Function function) {

        functions.put(name, function);
    }

    public static void register(final String name, final Function0 function) {

        functions.put(name, function);
    }

    public static void register(final String name, final Function1 function) {

        functions.put(name, function);
    }

    public static void register(final String name, final Function2 function) {

        functions.put(name, function);
    }

    /**
     * Find the next comma after any white-space and discard it and the
     * white-space afterwards.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @throws InterpreterException in case of an error
     */
    private static void skipComma(final Context context,
            final TokenSource source) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException();
        } else if (!t.equals(Catcode.OTHER, ',')) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(LengthParser.class.getName()),
                    "MissingComma", t.toString());
        }
        source.skipSpace();
    }

    /**
     * Creates a new object.
     */
    private LengthParser() {

        super();
    }

}
