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

package de.dante.extex.interpreter.type.count;

import java.io.Serializable;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class represents a long integer value.
 * It is used for instance as count register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.32 $
 */
public class Count implements Serializable, FixedCount {

    /**
     * The constant <tt>ONE</tt> contains the count register with the value 1.
     * This count register is in fact immutable.
     */
    public static final Count ONE = new ImmutableCount(1);

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The constant <tt>THOUSAND</tt> contains the count register with the
     * value 1000.
     * This count register is in fact immutable.
     */
    public static final Count THOUSAND = new ImmutableCount(1000);

    /**
     * The constant <tt>ZERO</tt> contains the count register with the value 0.
     * This count register is in fact immutable.
     */
    public static final Count ZERO = new ImmutableCount(0);

    /**
     * This interface describes a binary operation on two longs.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.32 $
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
     * This operation subtracts the second argument from the first one.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.32 $
     */
    private static final class Minus implements BinOp {

        /**
         * @see de.dante.extex.interpreter.primitives.register.count.Numexpr.BinOp#apply(long, long)
         */
        public long apply(final long arg1, final long arg2) {

            return arg1 - arg2;
        }
    }

    /**
     * This operation adds the arguments.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.32 $
     */
    private static final class Plus implements BinOp {

        /**
         * @see de.dante.extex.interpreter.primitives.register.count.Numexpr.BinOp#apply(long, long)
         */
        public long apply(final long arg1, final long arg2) {

            return arg1 + arg2;
        }
    }

    /**
     * This operation ignores the first argument and returns the second one.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.32 $
     */
    private static final class Second implements BinOp {

        /**
         * @see de.dante.extex.interpreter.primitives.register.count.Numexpr.BinOp#apply(long, long)
         */
        public long apply(final long arg1, final long arg2) {

            return arg2;
        }
    }

    /**
     * The field <tt>MINUS</tt> contains the subtractor.
     */
    private static final BinOp MINUS = new Minus();

    /**
     * The field <tt>PLUS</tt> contains the adder.
     */
    private static final BinOp PLUS = new Plus();

    /**
     * The field <tt>SECOND</tt> contains the operation to select the second
     * argument.
     */
    private static final BinOp SECOND = new Second();

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
        long val = scanInteger(context, source, typesetter);

        for (;;) {

            Token t = source.getNonSpace(context);
            if (t == null) {
                throw new MissingNumberException();

            } else if (t.equals(Catcode.OTHER, '*')) {
                val *= scanInteger(context, source, typesetter);

            } else if (t.equals(Catcode.OTHER, '/')) {
                long x = scanInteger(context, source, typesetter);
                if (x == 0) {
                    throw new ArithmeticOverflowException("");
                }
                val /= x;

            } else if (t.equals(Catcode.OTHER, '+')) {
                saveVal = op.apply(saveVal, val);
                val = scanInteger(context, source, typesetter);
                op = PLUS;

            } else if (t.equals(Catcode.OTHER, '-')) {
                saveVal = op.apply(saveVal, val);
                val = scanInteger(context, source, typesetter);
                op = MINUS;

            } else {
                source.push(t);
                return op.apply(saveVal, val);
            }
        }
    }

    /**
     * Scan the input stream for tokens making up an integer, this is a number
     * optionally preceded by a sign (+ or -). The number can be preceded by
     * optional white space. White space is also ignored between the sign and
     * the number. All non-whitespace characters must have the category code
     * OTHER.
     *
     * <p>
     * This method parses the following syntactic entity:
     * </p>
     *
     * <doc type="syntax" name="integer">
     * <h3>A Number</h3>
     *
     * <pre class="syntax">
     *   &lang;number&rang; </pre>
     * <p>
     *  A number consists of a non-empty sequence of digits with category code
     *  {@link de.dante.extex.scanner.type.Catcode#OTHER OTHER}. The number is
     *  optionally preceded by white space and a sign <tt>+</tt> or <tt>-</tt>.
     * </p>
     * <p>
     *  Tokens are expanded while gathering the requested values.
     * </p>
     *
     * </doc>
     *
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter to use for conversion
     *
     * @return the value of the count
     *
     * @throws InterpreterException in case of an error
     */
    public static long scanInteger(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        for (;;) {
            Token t = source.getNonSpace(context);
            if (t == null) {
                throw new MissingNumberException();

            } else if (t.equals(Catcode.OTHER, '(')) {
                long val = evalExpr(context, source, typesetter);
                t = source.getToken(context);
                if (t.equals(Catcode.OTHER, ')')) {
                    return val;
                }

                throw new HelpingException(LocalizerFactory
                        .getLocalizer(ScaledNumber.class),
                        "MissingParenthesis", (t == null ? "null" : t
                                .toString()));

            } else if (t.equals(Catcode.OTHER, '-')) {
                return -scanInteger(context, source, typesetter);

            } else if (t.equals(Catcode.OTHER, '+')) {
                // continue

            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof CountConvertible) {
                    return ((CountConvertible) code).convertCount(context,
                            source, typesetter);

                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                } else {
                    break;
                }
            } else {
                return scanNumber(context, source, typesetter, t);
            }
        }

        throw new MissingNumberException();
    }

    /**
     * Scan the input stream for tokens making up a number, i.e. a sequence of
     * digits with category code OTHER. The number can be preceded by optional
     * white space.
     * <p>
     *  This method implements the generalization of several syntactic
     *  definitions from <logo>TeX</logo>:
     * </p>
     *
     * <doc type="syntax" name="number">
     * <h3>A Number</h3>
     *
     * <pre class="syntax">
     *   &lang;number&rang; </pre>
     * <p>
     *  A number consists of a non-empty sequence of digits with category code
     *  {@link de.dante.extex.scanner.type.Catcode#OTHER OTHER}.
     * </p>
     *
     * </doc>
     *
     *
     * Scan the input stream for tokens making up a number, this is a sequence
     * of digits with category code <tt>OTHER</tt>. The number can be preceded
     * by optional white space. Alternate representations for an integer exist.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     */
    public static long scanNumber(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        return scanNumber(context, source, typesetter, source
                .getNonSpace(context));
    }

    /**
     * Scan the input stream for tokens making up a number, this is a sequence
     * of digits with category code <tt>OTHER</tt>. The number can be preceded
     * by optional white space. Alternate representations for an integer exist.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param token the first token
     *
     * @return the value of the integer scanned
     *
     * @throws InterpreterException in case that no number is found or the
     *  end of file has been reached before an integer could be acquired
     */
    public static long scanNumber(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final Token token) throws InterpreterException {

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

                        for (t = source.getToken(context); t instanceof OtherToken
                                && t.getChar().isDigit(); t = source
                                .getToken(context)) {
                            n = n * 10 + t.getChar().getCodePoint() - '0';
                        }

                        if (t instanceof SpaceToken) {
                            source.skipSpace();
                        } else {
                            source.push(t);
                        }
                        return n;

                    case '`':
                        t = source.getToken(context);

                        if (t instanceof ControlSequenceToken) {
                            String s = ((ControlSequenceToken) t).getName();
                            return ("".equals(s) ? 0 : s.charAt(0));
                        } else if (t != null) {
                            return t.getChar().getCodePoint();
                        }
                        // fall through to error handling
                        break;

                    case '\'':
                        t = source.scanToken(context);
                        if (!(t instanceof OtherToken)) {
                            throw new MissingNumberException();
                        }
                        n = t.getChar().getCodePoint() - '0';
                        if (n < 0 || n > 7) {
                            throw new MissingNumberException();
                        }
                        for (t = source.scanToken(context); t instanceof OtherToken; //
                        t = source.scanToken(context)) {
                            no = t.getChar().getCodePoint() - '0';
                            if (no < 0 || no > 7) {
                                break;
                            }
                            n = n * 8 + no;
                        }

                        while (t instanceof SpaceToken) {
                            t = source.getToken(context);
                        }
                        source.push(t);
                        return n;

                    case '"':

                        for (t = source.scanToken(context); //
                        t instanceof OtherToken || t instanceof LetterToken; //
                        t = source.scanToken(context)) {
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
                                    source.push(t);
                                    source.skipSpace();
                                    return n;
                            }
                        }

                        while (t instanceof SpaceToken) {
                            t = source.getToken(context);
                        }
                        source.push(t);
                        return n;

                    default:
                }
                break;
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code == null) {

                    break;

                } else if (code instanceof CountConvertible) {
                    return ((CountConvertible) code).convertCount(context,
                            source, typesetter);
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                    t = source.getToken(context);
                } else {

                    break;
                }
            } else {

                break;
            }
        }

        throw new MissingNumberException();
    }

    /**
     * parse a token stream for a count value.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return a new Count instance with the value acquired
     *
     * @throws InterpreterException in case of an error
     */
    public static Count parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Count(scanInteger(context, source, typesetter));
    }

    /**
     * The field <tt>value</tt> contains the value of the count register.
     */
    private long value = 0;

    /**
     * Creates a new object.
     *
     * @param count the reference to be copied
     */
    public Count(final FixedCount count) {

        super();
        this.value = count.getValue();
    }

    /**
     * Creates a new object.
     *
     * @param value the value
     */
    public Count(final long value) {

        super();
        this.value = value;
    }

    /**
     * Add a long to the value.
     * This operation modifies the value.
     *
     * @param val the value to add to
     */
    public void add(final long val) {

        value += val;
    }

    /**
     * Divide the value by a long.
     * This operation modifies the value.
     *
     * @param denom the denominator to divide by
     *
     * @throws ArithmeticOverflowException in case of a division by zero
     */
    public void divide(final long denom) throws ArithmeticOverflowException {

        if (denom == 0) {
            throw new ArithmeticOverflowException("");
        }

        value /= denom;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#eq(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean eq(final FixedCount count) {

        return count.getValue() == value;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#ge(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean ge(final FixedCount count) {

        return value >= count.getValue();
    }

    /**
     * Getter for the localizer.
     * The localizer is initialized from the name of the Count class.
     *
     * @return the localizer
     */
    protected Localizer getLocalizer() {

        return LocalizerFactory.getLocalizer(Count.class);
    }

    /**
     * Getter for the value
     *
     * @return the value
     */
    public long getValue() {

        return value;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#gt(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean gt(final FixedCount count) {

        return value > count.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#le(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean le(final FixedCount count) {

        return value <= count.getValue();
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#lt(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean lt(final FixedCount count) {

        return value < count.getValue();
    }

    /**
     * Multiply the value with a factor.
     * This operation modifies the value.
     *
     * @param factor the factor to multiply with
     */
    public void multiply(final long factor) {

        value *= factor;
    }

    /**
     * @see de.dante.extex.interpreter.type.count.FixedCount#ne(
     *      de.dante.extex.interpreter.type.count.FixedCount)
     */
    public boolean ne(final FixedCount count) {

        return value != count.getValue();
    }

    /**
     * Setter for the value.
     *
     * @param l the new value
     *
     * @see #setValue(long)
     */
    public void set(final long l) {

        value = l;
    }

    /**
     * Setter for the value.
     *
     * @param l the new value
     *
     * @see #set(long)
     */
    public void setValue(final long l) {

        value = l;
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     */
    public String toString() {

        return Long.toString(value);
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @param sb the target string buffer
     *
     * @see #toString()
     */
    public void toString(final StringBuffer sb) {

        sb.append(value);
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @param context the interpreter context
     *
     * @return the Tokens representing this instance
     *
     * @throws InterpreterException in case of an error
     */
    public Tokens toToks(final Context context) throws InterpreterException {

        return new Tokens(context, Long.toString(value));
    }

}
