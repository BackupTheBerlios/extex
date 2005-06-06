/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.Relax;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\numexpr</code>.
 *
 * <doc name="numexpr">
 * <h3>The Primitive <tt>\numexpr</tt></h3>
 * <p>
 *  The primitive <tt>\numexpr</tt> provides a means to use a inline way of
 *  writing mathematical expressions to be evaluated. Mathematical expressions
 *  can be evaluated in <logo>ExTeX</logo> using <tt>\advance</tt>,
 *  <tt>\multiply</tt>, and <tt>\divide</tt>. Nevertheless those primitives
 *  result in an assignment. This is not the case for <tt>\numexpr</tt>. Here
 *  the intermediate results are not stored in count registers but kept
 *  internally. Also the application of <tt>\afterassignment</tt> and
 *  <tt>\tracingassigns</tt> is suppressed.
 * </p>
 * <p>
 *  The mathematical expression to be evaluated can be made up of the basic
 *  operations addition (+), subtraction (-), multiplication (*), and
 *  division(/). The unary minus can be used. Parentheses can be used for
 *  grouping. Anything which looks like a number can be used as argument.
 *  White-space can be used freely without any harm.
 * </p>
 * <p>
 *  The expression is terminated at the first token which can not be part of
 *  an expression. For instance a letter may signal the end of the expression.
 *  If the expression should terminate without a proper token following it,
 *  the token <tt>\relax</tt> can be used to signal the end of the expression.
 *  This <tt>\relax</tt> token is silently consumed by <tt>\numexpr</tt>.
 * </p>
 * <p>
 *  The primitive <tt>\numexpr</tt> can be used in any place where a number is
 *  required. This includes assignments to count registers and comparisons.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;numexpr&rang;
 *      &rarr; <tt>\numexpr</tt> &lang;expr&rang; <tt>\relax</tt>
 *      |   <tt>\numexpr</tt> &lang;expr&rang;
 *
 *    &lang;expr&rang;
 *      &rarr; &lang;number&rang;
 *      |   &lang;operand&rang;
 *      |   &lang;operand&rang; <tt>+</tt> &lang;operand&rang;
 *      |   &lang;operand&rang; <tt>-</tt> &lang;operand&rang;
 *      |   &lang;operand&rang; <tt>*</tt> &lang;operand&rang;
 *      |   &lang;operand&rang; <tt>/</tt> &lang;operand&rang;
 *
 *    &lang;operand&rang;
 *      &rarr; &lang;number&rang;
 *      |   <tt>-</tt> &lang;expr&rang;
 *      |   <tt>(</tt> &lang;expr&rang; <tt>)</tt>   </pre>
 * </p>
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \count1=\numexpr 23 \relax </pre>
 * <pre class="TeXSample">
 *   \count1=\numexpr 2 * 3 \relax </pre>
 * <pre class="TeXSample">
 *   \count1=\numexpr 2*\count2  </pre>
 * <pre class="TeXSample">
 *   \count1=\numexpr 2*(1+3)  </pre>
 * <pre class="TeXSample">
 *   \count1=\numexpr 2*-\count0  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Numexpr extends AbstractCode implements CountConvertible, Theable {

    /**
     * This interface describes a binary operation on two longs.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
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
     * This operation ignores the first argument and returns the second one.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
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
     * This operation adds the arguments.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
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
     * This operation subtracts the second argument from the first one.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
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
     * The field <tt>SECOND</tt> contains the operation to select the second
     * argument.
     */
    private static final BinOp SECOND = new Second();

    /**
     * The field <tt>PLUS</tt> contains the adder.
     */
    private static final BinOp PLUS = new Plus();

    /**
     * The field <tt>MINUS</tt> contains the subtractor.
     */
    private static final BinOp MINUS = new Minus();

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Numexpr(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        long result = evalExpr(context, source, typesetter);
        Token t = source.getToken(context);
        if (!(t instanceof CodeToken)
                || !(context.getCode((CodeToken) t) instanceof Relax)) {
            source.push(t);
        }
        return result;
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
    private long evalExpr(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        long saveVal = 0;
        BinOp op = SECOND;
        long val = evalOperant(context, source, typesetter);

        for (;;) {

            Token t = source.getNonSpace(context);
            if (t == null) {
                throw new EofException(getName());

            } else if (t.equals(Catcode.OTHER, '*')) {
                val *= evalOperant(context, source, typesetter);

            } else if (t.equals(Catcode.OTHER, '/')) {
                long x = evalOperant(context, source, typesetter);
                if (x == 0) {
                    throw new ArithmeticOverflowException(getName());
                }
                val /= x;

            } else if (t.equals(Catcode.OTHER, '+')) {
                saveVal = op.apply(saveVal, val);
                val = evalOperant(context, source, typesetter);
                op = PLUS;

            } else if (t.equals(Catcode.OTHER, '-')) {
                saveVal = op.apply(saveVal, val);
                val = evalOperant(context, source, typesetter);
                op = MINUS;

            } else {
                source.push(t);
                return op.apply(saveVal, val);
            }
        }
    }

    /**
     * Evaluate an operant.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    public long evalOperant(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(getName());

        } else if (t.equals(Catcode.OTHER, '(')) {
            long val = evalExpr(context, source, typesetter);
            t = source.getToken(context);
            if (t.equals(Catcode.OTHER, ')')) {
                return val;
            }

            throw new HelpingException(getLocalizer(), "MissingParenthesis",
                    (t == null ? "null" : t.toString()));

        } else if (t.equals(Catcode.OTHER, '-')) {
            long val = evalOperant(context, source, typesetter);
            return -val;

        }

        source.push(t);
        return source.scanNumber(context);
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, Long.toString(convertCount(context, source,
                typesetter)));
    }

}
