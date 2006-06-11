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
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a dimen value with a length which is a multiple of
 * math units (mu).
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class Mudimen implements Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060605L;

    /**
     * Scan a math unit.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the number of scaled points for the mu
     *
     * @throws InterpreterException in case of an error
     */
    protected static long scanMu(final Context context,
            final TokenSource source, final Typesetter typesetter)
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
        long value = ScaledNumber.scanFloat(context, source, typesetter, t);
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
    private GlueComponent length = new GlueComponent(0);

    /**
     * Creates a new object.
     * All components are 0.
     */
    public Mudimen() {

        super();
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
    public static Mudimen parseMudimen(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        return new Mudimen(scanMu(context, source, typesetter));
    }

    /**
     * Creates a new object.
     *
     * @param len the length
     */
    public Mudimen(final long len) {

        super();
        length.set(len);
    }

    /**
     * Add some other length to the current value.
     *
     * @param value the value to add
     */
    public void add(final long value) {

        this.length.add(new GlueComponent(value));
    }

    /**
     * Getter for length.
     *
     * @return the length
     */
    public GlueComponent getLength() {

        return this.length;
    }

    /**
     * Check for a zero value.
     *
     * @return <code>true</code> iff the length is zero
     */
    public boolean isZero() {

        return length.eq(GlueComponent.ZERO);
    }

    /**
     * Multiply the value by an integer fraction.
     * <p>
     *  <i>length</i> = <i>length</i> * <i>nom</i> / <i>denom</i>
     * </p>
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiply(final long nom, final long denom) {

        length.multiply(nom, denom);
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

        length.toString(sb, 'm', 'u');
    }

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the Mudimen. This means the result is expressed
     * in mu and properly rounded to be read back in again without loss of
     * information.
     *
     * @param toks the tokens to append to
     * @param factory the token factory to get the required tokens from
     * @param c1 the first character of the unit
     * @param c2 the second character of the unit
     *
     * @throws CatcodeException in case of an error
     */
    public void toToks(final Tokens toks, final TokenFactory factory,
            final char c1, final char c2) throws CatcodeException {

        length.toToks(toks, factory, c1, c2);
    }

}
