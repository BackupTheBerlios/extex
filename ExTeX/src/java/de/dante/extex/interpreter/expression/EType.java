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

import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.expression.exception.CastException;
import de.dante.extex.interpreter.expression.exception.UnsupportedException;
import de.dante.extex.interpreter.expression.term.TBoolean;

/**
 * This interface describes the data type of an expression type.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface EType {

    /**
     * Apply the operation + on the current instance and an additional argument.
     *
     * @param t the terminal to add
     *
     * @return the result of the operation
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType add(EType t) throws CastException, UnsupportedException;

    /**
     * Apply the operation && on the current instance and an additional argument.
     *
     * @param t the terminal to build the conjunction with
     *
     * @return the result of the operation
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType and(EType t) throws CastException, UnsupportedException;

    /**
     * Apply the operation + on the current instance and an additional argument.
     *
     * @param t the terminal to divide by
     *
     * @return the result of the operation
     *
     * @throws ArithmeticOverflowException in case of a division by zero
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType divide(EType t)
            throws ArithmeticOverflowException,
                CastException,
                UnsupportedException;

    /**
     * Compare the current instance with an another value for equality.
     *
     * @param t the terminal to compare to
     *
     * @return the result of the comparison
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    TBoolean eq(EType t) throws CastException, UnsupportedException;

    /**
     * Compare the current instance with an another value for greater or equal.
     *
     * @param t the terminal to compare to
     *
     * @return the result of the comparison
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    TBoolean ge(EType t) throws CastException, UnsupportedException;

    /**
     * Compare the current instance with an another value for greater than.
     *
     * @param t the terminal to compare to
     *
     * @return the result of the comparison
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    TBoolean gt(EType t) throws CastException, UnsupportedException;

    /**
     * Compare the current instance with an another value for less or equal.
     *
     * @param t the terminal to compare to
     *
     * @return the result of the comparison
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    TBoolean le(EType t) throws CastException, UnsupportedException;

    /**
     * Compare the current instance with an another value for less.
     *
     * @param t the terminal to compare to
     *
     * @return the result of the comparison
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    TBoolean lt(EType t) throws CastException, UnsupportedException;

    /**
     * Apply the operation * on the current instance and an additional argument.
     *
     * @param t the terminal to multiply
     *
     * @return the result of the operation
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType multiply(EType t) throws CastException, UnsupportedException;

    /**
     * Compare the current instance with an another value for not equal.
     *
     * @param t the terminal to compare to
     *
     * @return the result of the comparison
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    TBoolean ne(EType t) throws CastException, UnsupportedException;

    /**
     * Apply the unary minus - on the current instance.
     *
     * @return the result of the operation
     *
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType negate() throws UnsupportedException;

    /**
     * Apply the unary negation ! on the current instance.
     *
     * @return the result of the operation
     *
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType not() throws UnsupportedException;

    /**
     * Apply the operation || on the current instance and an additional argument.
     *
     * @param t the terminal to build the disjunction with
     *
     * @return the result of the operation
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType or(EType t) throws CastException, UnsupportedException;

    /**
     * Assign a new value from an additional argument. The argument is casted
     * into the proper type.
     *
     * @param t the terminal to build the conjunction with
     *
     * @return the result of the operation
     *
     * @throws CastException in case of an error in casing the argument
     */
    EType set(EType t) throws CastException;

    /**
     * Apply the operation - on the current instance and an additional argument.
     *
     * @param t the terminal to subtract
     *
     * @return the result of the operation
     *
     * @throws CastException in case of an error in casing the argument
     * @throws UnsupportedException in case of an unsupported operation
     */
    EType subtract(EType t) throws CastException, UnsupportedException;

}
