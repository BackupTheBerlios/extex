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

package de.dante.extex.interpreter.type.scaled;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a fixed point number.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class ScaledNumber {

    /**
     * This interface describes a binary operation on two longs.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.4 $
     */
    private interface BinOp {

        /**
         * Apply the operation on the arguments.
         *
         * @param arg1 the first argument
         * @param arg2 the second argument
         *
         * @return the result
         */
        long apply(long arg1, long arg2);
    }

    /**
     * The constant <tt>FLOAT_DIGITS</tt> contains the number of digits to
     * consider when producing a string representation of this type.
     *
     * Attention: Do not change this value unless you have read and understood
     * <logo>TeX</logo> the program!
     */
    private static final int FLOAT_DIGITS = 17;

    /**
     * The field <tt>MINUS</tt> contains the subtractor.
     */
    private static final BinOp MINUS = new BinOp() {

        /**
         * @see de.dante.extex.interpreter.primitives.register.count.Numexpr.BinOp#apply(
         *      long, long)
         */
        public long apply(final long arg1, final long arg2) {

            return arg1 - arg2;
        }
    };

    /**
     * The constant <tt>ONE</tt> contains the internal representation for 1pt.
     * @see "<logo>TeX</logo> &ndash; The Program [101]"
     */
    public static final long ONE = 1 << 16;

    /**
     * The field <tt>PLUS</tt> contains the adder.
     */
    private static final BinOp PLUS = new BinOp() {

        /**
         * @see de.dante.extex.interpreter.primitives.register.count.Numexpr.BinOp#apply(
         *      long, long)
         */
        public long apply(final long arg1, final long arg2) {

            return arg1 + arg2;
        }
    };

    /**
     * The field <tt>SECOND</tt> contains the operation to select the second
     * argument.
     */
    private static final BinOp SECOND = new BinOp(){

        /**
         * @see de.dante.extex.interpreter.primitives.register.count.Numexpr.BinOp#apply(
         *      long, long)
         */
        public long apply(final long arg1, final long arg2) {

            return arg2;
        }

    };

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
    private static long evalExpr(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        long saveVal = 0;
        BinOp op = SECOND;
        long val = parse(context, source, typesetter);

        for (;;) {

            Token t = source.getNonSpace(context);
            if (t == null) {
                throw new EofException();

            } else if (t.equals(Catcode.OTHER, '*')) {
                val *= parse(context, source, typesetter);
                val /= ONE;

            } else if (t.equals(Catcode.OTHER, '/')) {
                long x = parse(context, source, typesetter);
                if (x == 0) {
                    throw new ArithmeticOverflowException("");
                }
                val *= ONE;
                val /= x;

            } else if (t.equals(Catcode.OTHER, '+')) {
                saveVal = op.apply(saveVal, val);
                val = parse(context, source, typesetter);
                op = PLUS;

            } else if (t.equals(Catcode.OTHER, '-')) {
                saveVal = op.apply(saveVal, val);
                val = parse(context, source, typesetter);
                op = MINUS;

            } else {
                source.push(t);
                return op.apply(saveVal, val);
            }
        }
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
    public static long parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        for (;;) {
            Token t = source.getNonSpace(context);
            if (t == null) {
                throw new EofException();

            } else if (t instanceof OtherToken) {
                if (t.equals(Catcode.OTHER, '(')) {
                    long val = evalExpr(context, source, typesetter);
                    t = source.getToken(context);
                    if (t.equals(Catcode.OTHER, ')')) {
                        return val;
                    }

                    throw new HelpingException(LocalizerFactory
                            .getLocalizer(ScaledNumber.class.getName()),
                            "MissingParenthesis", (t == null ? "null" : t
                                    .toString()));

                } else if (t.equals(Catcode.OTHER, '-')) {
                    return -parse(context, source, typesetter);
                } else {
                    return scanFloat(context, source, typesetter, t);
                }

            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof ScaledConvertible) {
                    return ((ScaledConvertible) code).convertScaled(context,
                            source, typesetter);

                } else if (code instanceof CountConvertible) {
                    return ((CountConvertible) code).convertCount(context,
                            source, typesetter)
                            * ONE;

                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                } else {
                    source.push(t);
                    break;
                }
            } else if (t instanceof LetterToken) {
                source.push(t);
                if (source.getKeyword(context, "min")) {
                    // TODO

                } else if (source.getKeyword(context, "max")) {
                    // TODO

                } else if (source.getKeyword(context, "sin")) {
                    // TODO

                }

                break;

            } else {
                break;
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Parses a token stream for a float and returns it as fixed point number.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param start the initial token to start with
     *
     * @return the fixed point representation of the floating number in units
     * of 2<sup>-16</sup>.
     * @throws InterpreterException in case of an error
     */
    public static long scanFloat(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final Token start) throws InterpreterException {

        boolean neg = false;
        long val = 0;
        int post = 0;
        Token t = start;
        if (t == null) {
            t = source.scanNonSpace(context);
        }

        while (t != null) {
            if (t.equals(Catcode.OTHER, '-')) {
                neg = !neg;
            } else if (!t.equals(Catcode.OTHER, '+')) {
                break;
            }
            t = source.scanNonSpace(context);
        }
        if (t != null && !t.equals(Catcode.OTHER, ".")
                && !t.equals(Catcode.OTHER, ",")) {
            val = Count.scanNumber(context, source, typesetter, t);
            t = source.getToken(context);
        }
        if (t != null
                && (t.equals(Catcode.OTHER, '.') || t
                        .equals(Catcode.OTHER, ','))) {
            // @see "TeX -- The Program [102]"
            int[] dig = new int[FLOAT_DIGITS];
            int k = 0;
            for (t = source.scanToken(context); t instanceof OtherToken
                    && t.getChar().isDigit(); t = source.scanToken(context)) {
                if (k < FLOAT_DIGITS) {
                    dig[k++] = t.getChar().getCodePoint() - '0';
                }
            }
            if (k < FLOAT_DIGITS) {
                k = FLOAT_DIGITS;
            }
            post = 0;
            while (k-- > 0) {
                post = (post + dig[k] * (1 << FLOAT_DIGITS)) / 10;
            }
            post = (post + 1) / 2;
        }
        source.push(t);
        val = val << 16 | post;
        return (neg ? -val : val);
    }

    /**
     * Parses a token stream for a float and returns it as fixed point number.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the fixed point representation of the floating point number
     * @throws InterpreterException in case of an error
     */
    public static ScaledNumber scanScaledNumber(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        return new ScaledNumber(parse(context, source, typesetter));
    }

    /**
     * Determine the printable representation of the object and append it to
     * the given StringBuffer.
     *
     * @param sb the output string buffer
     * @param value the internal value in multiples of ONE
     */
    public static void toString(final StringBuffer sb, final long value) {

        long val = value;

        if (val < 0) {
            sb.append('-');
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            sb.append('0');
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                sb.append((char) ('0' + (v / m)));
                v = v % m;
                m /= 10;
            }
        }

        sb.append('.');

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            sb.append((char) ('0' + i));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);
    }

    /**
     * The field <tt>value</tt> contains the value.
     */
    private long value;

    /**
     * Creates a new object.
     *
     */
    public ScaledNumber() {

        super();
        this.value = 0;
    }

    /**
     * Creates a new object.
     *
     * @param value the initial value
     */
    private ScaledNumber(final long value) {

        super();
        this.value = value;
    }

    /**
     * Add a number to the current one.
     *
     * @param scaled the number to add
     */
    public void add(final long scaled) {

        this.value += scaled;
    }

    /**
     * Add a number to the current one.
     *
     * @param scaled the number to add
     */
    public void add(final ScaledNumber scaled) {

        this.value += scaled.value;
    }

    /**
     * Divide the scaled value by a number.
     *
     * @param scaled the divisor
     */
    public void divide(final long scaled) {

        value = value / scaled * ONE;
    }

    /**
     * Compares the current instance with another ScaledNumber for equality.
     *
     * @param d the other ScaledNumber to compare to. If this parameter is
     * <code>null</code> then the comparison fails.
     *
     * @return <code>true</code> iff <i>|this| == |d|</i>
     */
    public boolean eq(final ScaledNumber d) {

        return (d != null && value == d.value);
    }

    /**
     * Compares the current instance with another ScaledNumber.
     *
     * @param d the other ScaledNumber to compare to
     *
     * @return <code>true</code> iff this is greater or equal to d
     */
    public boolean ge(final ScaledNumber d) {

        return (value >= d.value);
    }

    /**
     * Getter for the value.
     *
     * @return the value
     */
    public long getValue() {

        return value;
    }

    /**
     * Compares the current instance with another ScaledNumber.
     *
     * @param d the other ScaledNumber to compare to
     *
     * @return <code>true</code> iff this is less or equal to d
     */
    public boolean le(final ScaledNumber d) {

        return (value <= d.value);
    }

    /**
     * Compares the current instance with another ScaledNumber.
     *
     * @param d the other ScaledNumber to compare to
     *
     * @return <code>true</code> iff |this| &lt; |d|</i>
     */
    public boolean lt(final ScaledNumber d) {

        return (value < d.value);
    }

    /**
     * Multiply the current value by a scaled number.
     *
     * @param scaled the multiplicant
     */
    public void multiply(final long scaled) {

        value = value * scaled / ONE;
    }

    /**
     * Multiply the value by an integer fraction.
     * <p>
     *  <i>length</i> = <i>length</i> * <i>nom</i> / <i>denom</i>
     * </p>
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiply(final long nom, final long denom) {

        this.value = this.value * nom / denom;
    }

    /**
     * Negate the current value.
     */
    public void negate() {

        this.value = -this.value;
    }

    /**
     * Set the value to a new one
     *
     * @param scaled the new value
     */
    public void set(final long scaled) {

        value = scaled;
    }

    /**
     * Setter for the value
     *
     * @param scaled the new value
     */
    public void set(final ScaledNumber scaled) {

        value = scaled.value;
    }

    /**
     * Subtract a number from the current one.
     *
     * @param scaled the number to subtract
     */
    public void subtract(final ScaledNumber scaled) {

        this.value -= scaled.value;
    }

    /**
     * Determine the printable representation of the object.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     * @see #toToks(TokenFactory)
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb, this.value);
        return sb.toString();
    }

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Dimen. This means the result is expressed
     * in pt and properly rounded to be read back in again without loss of
     * information.
     *
     * @param toks the tokens to append to
     * @param factory the token factory to get the required tokens from
     *
     * @throws CatcodeException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [103]"
     */
    public void toToks(final Tokens toks, final TokenFactory factory)
            throws CatcodeException {

        long val = this.value;

        if (val < 0) {
            toks.add(factory.createToken(Catcode.OTHER, '-',
                    Namespace.DEFAULT_NAMESPACE));
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            toks.add(factory.createToken(Catcode.OTHER, '0',
                    Namespace.DEFAULT_NAMESPACE));
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                toks.add(factory.createToken(Catcode.OTHER,
                        (char) ('0' + (v / m)), Namespace.DEFAULT_NAMESPACE));
                v = v % m;
                m /= 10;
            }
        }

        toks.add(factory.createToken(Catcode.OTHER, '.',
                Namespace.DEFAULT_NAMESPACE));

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            toks.add(factory.createToken(Catcode.OTHER, (char) ('0' + i),
                    Namespace.DEFAULT_NAMESPACE));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);
    }

}
