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

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.typesetter.Typesetter;

/**
 * This interface describes a parser which can be registered in the evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface ETypeParser {

    /**
     * Try to convert some code into a proper data type.
     *
     * @param code the code to convert
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the converted value or <code>null</code> if the conversion
     *  could not be performed
     *
     * @throws InterpreterException in case of an error
     */
    EType convert(Code code, Context context, TokenSource source,
            Typesetter typesetter) throws InterpreterException;

    /**
     * Try to parse a proper value from the token source.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the element inquired or <code>null</code> if none could be
     *  parsed
     *
     * @throws InterpreterException in case of an error
     */
    EType parse(Context context, TokenSource source, Typesetter typesetter)
            throws InterpreterException;

    /**
     * Inform the parser that it has been registered in an evaluator.
     * In this case the parser can register some functions in the evaluator.
     *
     * @param evaluator the evaluator where the parser has been registered
     */
    void registered(Evaluator evaluator);

}
