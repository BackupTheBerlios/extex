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

package de.dante.extex.interpreter.expression.term;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.expression.EType;
import de.dante.extex.interpreter.expression.ETypeParser;
import de.dante.extex.interpreter.expression.Evaluator;
import de.dante.extex.interpreter.expression.ConstantFunction;
import de.dante.extex.interpreter.expression.UnaryFunction;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class implements the supporting functions for the date type
 * {@linkplain de.dante.extex.interpreter.expression.term.TDouble TDouble}
 * for the expression evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class TDoubleParser implements ETypeParser {

    /**
     * Creates a new object.
     */
    public TDoubleParser() {

        super();
    }

    /**
     * @see de.dante.extex.interpreter.expression.ETypeParser#convert(
     *      de.dante.extex.interpreter.type.Code,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public EType convert(final Code code, final Context context,
            final TokenSource source, final Typesetter typesetter) {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.expression.TerminalParser#parse(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public EType parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        boolean period = false;
        StringBuffer sb = new StringBuffer();
        Token t = source.getNonSpace(context);
        char c;

        while (t instanceof OtherToken) {
            c = (char) t.getChar().getCodePoint();
            switch (c) {
                case '.':
                    if (period) {
                        source.push(t);
                        t = null;
                        break;
                    }
                    period = true;
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
                    sb.append(c);
                    t = source.scanToken(context);
                    break;
                default:
                    source.push(t);
                    t = null;
            }
        }

        if (sb.length() == 0) {
            return null;
        } else if (period) {
            return new TDouble(Double.parseDouble(sb.toString()));
        } else {
            return new TCount(Long.parseLong(sb.toString()));
        }
    }

    /**
     * @see de.dante.extex.interpreter.expression.TerminalParser#registered(
     *      de.dante.extex.interpreter.expression.Evaluator)
     */
    public void registered(final Evaluator evaluator) {

        //
        //        functions.put("cos", new Function1() {
        //
        //            /**
        //             * Compute the cos value.
        //             *
        //             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
        //             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
        //             */
        //            public void apply(final Terminal accumulator)
        //                    throws InterpreterException {
        //
        //                //                if (accumulator.sp != 0) {
        //                //                    throw new HelpingException(LocalizerFactory
        //                //                            .getLocalizer(Evaluator.class.getName()),
        //                //                            "NonScalar", "cos", accumulator.toString());
        //                //                }
        //                //                double x = ((double) accumulator.value) / ScaledNumber.ONE;
        //                //                accumulator.value = (long) (ScaledNumber.ONE * Math.cos(x));
        //            }
        //        });
        //
        //        functions.put("sin", new Function1() {
        //
        //            /**
        //             * Compute the sin value.
        //             *
        //             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
        //             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
        //             */
        //            public void apply(final Terminal accumulator)
        //                    throws InterpreterException {
        //
        //                //                if (accumulator.sp != 0) {
        //                //                    throw new HelpingException(LocalizerFactory
        //                //                            .getLocalizer(Evaluator.class.getName()),
        //                //                            "NonScalar", "sin", accumulator.toString());
        //                //                }
        //                //                double x = ((double) accumulator.value) / ScaledNumber.ONE;
        //                //                accumulator.value = (long) (ScaledNumber.ONE * Math.sin(x));
        //            }
        //        });
        //
        //        functions.put("tan", new Function1() {
        //
        //            /**
        //             * Compute the tan value.
        //             *
        //             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
        //             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
        //             */
        //            public void apply(final Terminal accumulator)
        //                    throws InterpreterException {
        //
        //                //                if (accumulator.sp != 0) {
        //                //                    throw new HelpingException(LocalizerFactory
        //                //                            .getLocalizer(Evaluator.class.getName()),
        //                //                            "NonScalar", "tan", accumulator.toString());
        //                //                }
        //                //                double x = ((double) accumulator.value) / ScaledNumber.ONE;
        //                //                accumulator.value = (long) (ScaledNumber.ONE * Math.tan(x));
        //            }
        //        });
        //
        evaluator.register("pi", new ConstantFunction() {

            /**
             * Compute the value of pi.
             */
            public EType apply() {

                return new TDouble(Math.PI);
            }
        });

        evaluator.register("float", new UnaryFunction() {

            /**
             * @see de.dante.extex.interpreter.expression.Function1#apply(
             *      de.dante.extex.interpreter.expression.EType)
             */
            public EType apply(final EType accumulator)
                    throws InterpreterException {

                return new TDouble().set(accumulator);
            }
        });

    }

}
