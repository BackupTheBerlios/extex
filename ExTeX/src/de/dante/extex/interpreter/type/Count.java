/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public class Count implements Serializable {
    /**
     * The constant <tt>ZERO</tt> contains the ...
     */
    public static final Count ZERO = new ImmutableCount(0);

    /**
     * The field <tt>ONE</tt> contains the ...
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
     * Increment the value.
     *
     * @param val the value to add to
     */
    public void add(final long val) {
        value += val;
    }

    /**
     * ...
     *
     * @param val ...
     *
     * @throws GeneralException in case of a division by zero
     */
    public void divide(final long val) throws GeneralException {
        if (val == 0) {
            throw new GeneralHelpingException("TTP.ArithOverflow");
        }

        value /= val;
    }

    /**
     * ...
     *
     * @param val ...
     */
    public void multiply(final long val) {
        value *= val;
    }

    /**
     * Return the value as <code>String</code>
     *
     * @return the value as <code>String</code>
     */
    public String toString() {
        return Long.toString(value);
    }
}
