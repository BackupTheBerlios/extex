/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * This class represents a long integer value.
 * It is used for instance as count register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class Count implements Serializable {

    /**
     * The constant <tt>ZERO</tt> contains the count register with the value 0.
     * This count register is in fact immutable.
     */
    public static final Count ZERO = new ImmutableCount(0);

    /**
     * The constant <tt>ONE</tt> contains the count register with the value 1.
     * This count register is in fact immutable.
     */
    public static final Count ONE = new ImmutableCount(1);

    /**
     * The field <tt>value</tt> contains the value of the count register.
     */
    private long value = 0;

    /**
     * Creates a new object.
     *
     * @param aValue the value
     */
    public Count(final long aValue) {

        super();
        this.value = aValue;
    }

    /**
     * Creates a new object.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @throws GeneralException in case of an error
     */
    public Count(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        value = scanCount(context, source);
    }

    /**
     * Scan the input stream for a count value.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return the value of the count
     *
     * @throws GeneralException in case of an error
     */
    public static long scanCount(final Context context,
            final TokenSource source) throws GeneralException {

        Token t = source.getNonSpace();

        if (t == null) {
            // TODO
            return 0;
        }

        Code code = context.getCode(t);
        if (code != null && code instanceof CountConvertable) {
            return ((CountConvertable) code).convertCount(context, source);
        }

        source.push(t);

        return source.scanInteger();
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
     * Getter for the value
     *
     * @return the value
     */
    public long getValue() {
        return value;
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
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final long denom) throws GeneralException {

        if (denom == 0) {
            throw new GeneralHelpingException("TTP.ArithOverflow");
        }

        value /= denom;
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
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * TeX to print the Count.
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
     * The value returned is exactely the string which would be produced by
     * TeX to print the Count.
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
     * The value returned is exactely the string which would be produced by
     * TeX to print the Count.
     *
     * @param context the interpreter context
     *
     * @throws GeneralException in case of an error
     */
    public Tokens toToks(final Context context) throws GeneralException {

        return new Tokens(context, Long.toString(value));
    }

}
