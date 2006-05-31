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

package de.dante.extex.interpreter.type.muskip;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a dimen value with a length which is a multiple of
 * math unints (mu).
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Mudimen implements Serializable {

    /**
     * The constant <tt>ONE</tt> contains the internal representation for 1mu.
     * @see "<logo>TeX</logo> &ndash; The Program [101]"
     */
    private static final long ONE = 1 << 16;

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Scan a math unit.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return the number of scaled points for the mu
     *
     * @throws InterpreterException in case of an error
     */
    public static long scanMu(final Context context, final TokenSource source)
            throws InterpreterException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException("mu");
        } else if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code instanceof MudimenConvertible) {
                return ((MudimenConvertible) code).convertMudimen(context,
                        source, null);
            }
        }
        long value = ScaledNumber.scanFloat(context, source, t);
        if (!source.getKeyword(context, "mu")) {
            throw new HelpingException(//
                    LocalizerFactory.getLocalizer(Mudimen.class.getName()),
                    "TTP.IllegalMu");
        }
        return value;
    }

    /**
     * The field <tt>length</tt> contains the the natural length.
     */
    private long value;

    /**
     * Creates a new object.
     * All components are 0.
     */
    public Mudimen() {

        super();
        this.value = 0;
    }

    /**
     * Creates a new object and fills it from a token stream.
     *
     * <doc type="syntax" name="mudimen">
     * This method parses the following syntactic entity:
     *
     *  <pre class="syntax">
     *    &lang;mudimen&rang;
     *      &rarr; &lang;float&rang; <tt>mu</tt>
     *       |  &lang;mudimen variable&lang;
     * </pre>
     *  The value of &lang;mudimen&rang; is either a floating point number
     *  followed by the unit <tt>mu</tt> or a variable value resulting in a
     *  mudimen value.
     * </doc>
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @throws InterpreterException in case of an error
     */
    public Mudimen(final Context context, final TokenSource source)
            throws InterpreterException {

        super();
        this.value = scanMu(context, source);
    }

    /**
     * Getter for length.
     *
     * @return the length
     */
    public long getLength() {

        return this.value;
    }

    /**
     * Return the string representation of the instance.
     *
     * @return the string representation of this glue
     * @see "<logo>TeX</logo> &ndash; The Program [???]"
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Append the string representation of the instance to a string buffer.
     *
     * @param sb the target string buffer
     */
    public void toString(final StringBuffer sb) {

        toString(sb, 'm', 'u');
    }

    /**
     * Determine the printable representation of the object and append it to
     * the given StringBuffer.
     *
     * @param sb the output string buffer
     * @param c1 the first character for the length of order 0
     * @param c2 the second character for the length of order 0
     *
     * @see #toString(StringBuffer)
     */
    public void toString(final StringBuffer sb, final char c1, final char c2) {

        long val = value;

        if (val < 0) {
            sb.append('-');
            val = -val;
        }

        long v = val / ONE;
        if (v == 0) {
            sb.append('0');
        } else {
            long m = 1;
            while (m <= v) {
                m *= 10;
            }
            m /= 10;
            while (m > 0) {
                sb.append((char) ('0' + (v / m)));
                v = v % m;
                m /= 10;
            }
        }

        sb.append('.');

        val = 10 * (val % ONE) + 5;
        long delta = 10;
        do {
            if (delta > ONE) {
                val = val + 0100000 - 50000; // round the last digit
            }
            int i = (int) (val / ONE);
            sb.append((char) ('0' + i));
            val = 10 * (val % ONE);
            delta *= 10;
        } while (val > delta);

        sb.append(c1);
        sb.append(c2);
    }

}
