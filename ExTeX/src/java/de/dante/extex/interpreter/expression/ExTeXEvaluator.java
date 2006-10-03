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

import de.dante.extex.interpreter.expression.term.TBooleanParser;
import de.dante.extex.interpreter.expression.term.TCountParser;
import de.dante.extex.interpreter.expression.term.TDoubleParser;
import de.dante.extex.interpreter.expression.term.TGlueParser;

/**
 * This evaluator registers the data types known in <logo>TeX</logo> and some
 * more.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ExTeXEvaluator extends Evaluator {

    //    {
    //        functions.put("abs", new Function0() {
    //
    //            /**
    //             * Compute the absolute value by eliminating the sign if present.
    //             *
    //             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
    //             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
    //             */
    //            public void apply(final Terminal accumulator)
    //                    throws InterpreterException {
    //
    //                //                if (accumulator.value < 0) {
    //                //                    accumulator.value = -accumulator.value;
    //                //                }
    //            }
    //        });
    //
    //        functions.put("max", new Function() {
    //
    //            /**
    //             * Compute the maximum of an arbitrary number of arguments.
    //             *
    //             * @param accumulator the accumulator to receive the result
    //             * @param context the interpreter context
    //             * @param source the source for new tokens
    //             * @param typesetter the typesetter
    //             *
    //             * @throws InterpreterException in case of an error
    //             */
    //            public void apply(final Terminal accumulator,
    //                    final Context context, final TokenSource source,
    //                    final Typesetter typesetter) throws InterpreterException {
    //
    //                Token t;
    //                evalExpr(accumulator, context, source, typesetter);
    //                for (t = source.getNonSpace(context); t != null
    //                        && t.equals(Catcode.OTHER, ','); t = source
    //                        .getNonSpace(context)) {
    //                    Accumulator x = new Accumulator();
    //                    evalExpr(x, context, source, typesetter);
    //                    //                    if (accumulator.sp != x.sp) {
    //                    //                        throw new HelpingException(LocalizerFactory
    //                    //                                .getLocalizer(Evaluator.class.getName()),
    //                    //                                "IncompatibleUnit", "max", accumulator
    //                    //                                        .toString(), x.toString());
    //                    //                    }
    //                    //                    if (accumulator.value < x.value) {
    //                    //                        accumulator.value = x.value;
    //                    //                    }
    //                }
    //                source.push(t);
    //            }
    //        });
    //
    //        functions.put("min", new Function() {
    //
    //            /**
    //             * Compute the minimum of an arbitrary number of arguments.
    //             *
    //             * @param accumulator the accumulator to receive the result
    //             * @param context the interpreter context
    //             * @param source the source for new tokens
    //             * @param typesetter the typesetter
    //             *
    //             * @throws InterpreterException in case of an error
    //             */
    //            public void apply(final Terminal accumulator,
    //                    final Context context, final TokenSource source,
    //                    final Typesetter typesetter) throws InterpreterException {
    //
    //                Token t;
    //                evalExpr(accumulator, context, source, typesetter);
    //                for (t = source.getNonSpace(context); t != null
    //                        && t.equals(Catcode.OTHER, ','); t = source
    //                        .getNonSpace(context)) {
    //                    Accumulator x = new Accumulator();
    //                    evalExpr(x, context, source, typesetter);
    //                    //                    if (accumulator.sp != x.sp) {
    //                    //                        throw new HelpingException(LocalizerFactory
    //                    //                                .getLocalizer(Evaluator.class.getName()),
    //                    //                                "IncompatibleUnit", "min", accumulator
    //                    //                                        .toString(), x.toString());
    //                    //                    }
    //                    //                    if (accumulator.value > x.value) {
    //                    //                        accumulator.value = x.value;
    //                    //                    }
    //                }
    //                source.push(t);
    //            }
    //        });
    //
    //        functions.put("sgn", new Function1() {
    //
    //            /**
    //             * Compute the sign i.e. return 1 for positive values, 0 for zero
    //             * values and -1 for negative values.
    //             *
    //             * @see de.dante.extex.interpreter.type.dimen.parser.Function1#apply(
    //             *      de.dante.extex.interpreter.type.dimen.parser.Accumulator)
    //             */
    //            public void apply(final Terminal accumulator)
    //                    throws InterpreterException {
    //
    //                //                if (accumulator.value > 0) {
    //                //                    accumulator.value = 1;
    //                //                } else if (accumulator.value < 0) {
    //                //                    accumulator.value = -1;
    //                //                } else {
    //                //                    accumulator.value = 0;
    //                //                }
    //                //                accumulator.sp = 0;
    //            }
    //        });

    /**
     * Creates a new object.
     *
     */
    public ExTeXEvaluator() {

        super();
        register(new TBooleanParser());
        register(new TGlueParser());
        register(new TDoubleParser());
        register(new TCountParser());
    }

}
