/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.dimen;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.primitives.Relax;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\dimenexpr</code>.
 *
 * <doc name="dimenexpr">
 * <h3>The Primitive <tt>\dimenexpr</tt></h3>
 * <p>
 *  The primitive <tt>\dimenexpr</tt> provides a means to use a inline way of
 *  writing mathematical expressions to be evaluated. Mathematical expressions
 *  can be evaluated in <logo>ExTeX</logo> using <tt>\advance</tt>,
 *  <tt>\multiply</tt>, and <tt>\divide</tt>. Nevertheless those primitives
 *  result in an assignment. This is not the case for <tt>\dimenexpr</tt>. Here
 *  the intermediate results are not stored in dimen registers but kept
 *  internally. Also the application of <tt>\afterassignment</tt> and
 *  <tt>\tracingassigns</tt> is suppressed.
 * </p>
 * <p>
 *  The mathematical expression to be evaluated can be made up of the basic
 *  operations addition (+), subtraction (-), multiplication (*) with numbers,
 *  and division(/) by numbers. The unary minus can be used. Parentheses can be
 *  used for grouping. Anything which looks like a length can be used as
 *  argument. White-space can be used freely without any harm.
 * </p>
 * <p>
 *  The expression is terminated at the first token which can not be part of
 *  an expression. For instance a letter may signal the end of the expression.
 *  If the expression should terminate without a proper token following it,
 *  the token <tt>\relax</tt> can be used to signal the end of the expression.
 *  This <tt>\relax</tt> token is silently consumed by <tt>\dimenexpr</tt>.
 * </p>
 * <p>
 *  The primitive <tt>\dimenexpr</tt> can be used in any place where a dimen is
 *  required. This includes assignments to dimen registers and comparisons.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;dimenexpr&rang;
 *      &rarr; <tt>\dimenexpr</tt> &lang;expr&rang; <tt>\relax</tt>
 *      |   <tt>\dimenexpr</tt> &lang;expr&rang;
 *
 *    &lang;expr&rang;
 *      &rarr; &lang;operand&rang;
 *      |   &lang;operand&rang; <tt>+</tt> &lang;expr&rang;
 *      |   &lang;operand&rang; <tt>-</tt> &lang;expr&rang;
 *
 *    &lang;operand&rang;
 *      &rarr; &lang;dimen&rang;
 *      |   &lang;operand&rang; <tt>*</tt> &lang;number&rang;
 *      |   &lang;number&rang; <tt>*</tt> &lang;operand&rang;
 *      |   &lang;operand&rang; <tt>/</tt> &lang;number&rang;
 *      |   <tt>-</tt> &lang;expr&rang;
 *      |   <tt>(</tt> &lang;expr&rang; <tt>)</tt>   </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   \count1=\dimenexpr 23pt \relax </pre>
 * <pre class="TeXSample">
 *   \count1=\dimenexpr 2 * 3pt \relax </pre>
 * <pre class="TeXSample">
 *   \count1=\dimenexpr 2pt*\count2  </pre>
 * <pre class="TeXSample">
 *   \count1=\dimenexpr 2*(1pt+3em)  </pre>
 * <pre class="TeXSample">
 *   \count1=\dimenexpr 2*-\dimen0  </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class Dimenexpr extends AbstractCode
        implements
            DimenConvertible,
            CountConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Dimenexpr(final String name) {

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
     * @see de.dante.extex.interpreter.type.dimen.DimenConvertible#convertDimen(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
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

        long val = evalOperand(context, source, typesetter);

        for (;;) {

            Token t = source.getNonSpace(context);
            if (t == null) {
                throw new EofException(getName());

            } else if (t.equals(Catcode.OTHER, '+')) {
                val += evalOperand(context, source, typesetter);
            } else if (t.equals(Catcode.OTHER, '-')) {
                val -= evalOperand(context, source, typesetter);

            } else {
                source.push(t);
                return val;
            }
        }
    }

    /**
     * Evaluate an operand.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the result
     *
     * @throws InterpreterException in case of an error
     */
    private long evalOperand(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        long val = evalTerminal(context, source, typesetter, source
                .getNonSpace(context));

        for (Token t = source.getNonSpace(context); t != null; t = source
                .getNonSpace(context)) {

            if (t.equals(Catcode.OTHER, '*')) {
                val *= evalOperand(context, source, typesetter);
            } else if (t.equals(Catcode.OTHER, '/')) {
                long x = evalOperand(context, source, typesetter);
                if (x == 0) {
                    throw new ArithmeticOverflowException(getName());
                }
                val /= x;
            } else {
                source.push(t);
                return val;
            }
        }
        return val;
    }

    /**
     * Evaluate a terminal symbol.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param start the first token to start with
     *
     * @return the value
     *
     * @throws InterpreterException in case of an error
     */
    private long evalTerminal(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token start)
            throws InterpreterException {

        Token t = start;
        for (;;) {
            if (t instanceof OtherToken) {
                if (t.equals(Catcode.OTHER, '(')) {
                    long val = evalExpr(context, source, typesetter);
                    t = source.getToken(context);
                    if (!t.equals(Catcode.OTHER, ')')) {
                        throw new HelpingException(getLocalizer(),
                                "MissingParenthesis", (t == null ? "null" : t
                                        .toString()));
                    }
                    return val;
                } else if (t.equals(Catcode.OTHER, '-')) {
                    return -evalOperand(context, source, typesetter);
                } else if (t.equals(Catcode.OTHER, '.')
                        || t.equals(Catcode.OTHER, ',')) {
                    source.push(t);
                    return new Dimen(context, source, typesetter).getValue();
                }
                long pre = source.scanNumber(context, t);
                t = source.getToken(context);
                if (t == null) {
                    return pre;
                } else if (t.equals(Catcode.OTHER, '.')
                        || t.equals(Catcode.OTHER, ',')) {
                    source.push(t);
                    source.push(new Tokens(context, pre));
                    return new Dimen(context, source, typesetter).getValue();
                }
                source.push(t);
                t = source.getNonSpace(context);
                if (t == null) {
                    throw new MissingNumberException();

                } else if (t instanceof LetterToken) {
                    source.push(t);
                    source.push(new Tokens(context, pre));
                    return new Dimen(context, source, typesetter).getValue();
                } else {
                    source.push(t);
                    source.push(new Tokens(context, pre));
                    return source.scanNumber(context);
                }

            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof DimenConvertible) {
                    return ((DimenConvertible) code).convertDimen(context,
                            source, typesetter);
                } else if (code instanceof CountConvertible) {
                    return ((CountConvertible) code).convertCount(context,
                            source, typesetter);
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                } else {
                    throw new MissingNumberException();
                }

            } else {
                throw new MissingNumberException();
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        try {
            //todo gene: find a solution without intermediary object
            Dimen d = new Dimen(convertDimen(context, source, typesetter));
            return d.toToks(context.getTokenFactory());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

}
