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
import de.dante.extex.interpreter.expression.UnaryFunction;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class implements the supporting functions for the date type
 * {@linkplain de.dante.extex.interpreter.expression.term.TBoolean TBoolean}
 * for the expression evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class TBooleanParser implements ETypeParser {

    /**
     * Creates a new object.
     */
    public TBooleanParser() {

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

        if (source.getKeyword(context, "true")) {
            return new TBoolean(true);
        } else if (source.getKeyword(context, "false")) {
            return new TBoolean(false);
        }

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.expression.TerminalParser#registered(
     *      de.dante.extex.interpreter.expression.Evaluator)
     */
    public void registered(final Evaluator evaluator) {

        evaluator.register("boolean", new UnaryFunction() {

            /**
             * @see de.dante.extex.interpreter.expression.Function1#apply(
             *      de.dante.extex.interpreter.expression.EType)
             */
            public EType apply(final EType accumulator)
                    throws InterpreterException {

                return new TBoolean().set(accumulator);
            }
        });

    }

}
