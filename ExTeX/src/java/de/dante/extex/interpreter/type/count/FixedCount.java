/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This interface describes the features of a
 * {@link de.dante.extex.interpreter.type.count.Count Count} which do not modify
 * the value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface FixedCount {

    /**
     * Compare the value for equality.
     *
     * @param count the count to compare to
     *
     * @return <code>true</code> iff count has an equal value to the current
     *  one
     */
    boolean eq(FixedCount count);

    /**
     * Compare the value for a greater or equal value.
     *
     * @param count the count to compare to
     *
     * @return <code>true</code> iff count has an value greater or equal to the
     *  current one
     */
    boolean ge(FixedCount count);

    /**
     * Getter for the value
     *
     * @return the value
     */
    long getValue();

    /**
     * Compare the value for a greater value.
     *
     * @param count the count to compare to
     *
     * @return <code>true</code> iff count has an value greater than the
     *  current one
     */
    boolean gt(FixedCount count);

    /**
     * Compare the value for a less or equal value.
     *
     * @param count the count to compare to
     *
     * @return <code>true</code> iff count has an value less or equal to the
     *  current one
     */
    boolean le(FixedCount count);

    /**
     * Compare the value for a lesser value.
     *
     * @param count the count to compare to
     *
     * @return <code>true</code> iff count has an value less than the
     *  current one
     */
    boolean lt(FixedCount count);

    /**
     * Compare the value for non-equality.
     *
     * @param count the count to compare to
     *
     * @return <code>true</code> iff count has an value not equal to the
     *  current one
     */
    boolean ne(FixedCount count);

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @return the printable representation
     *
     * @see #toString(StringBuffer)
     */
    String toString();

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @param sb the target string buffer
     *
     * @see #toString()
     */
    void toString(final StringBuffer sb);

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Count.
     *
     * @param context the interpreter context
     *
     * @return the printable representation as tokens
     *
     * @throws InterpreterException in case of an error
     */
    Tokens toToks(final Context context) throws InterpreterException;

}
