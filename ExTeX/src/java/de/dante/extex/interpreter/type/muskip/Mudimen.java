/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.scanner.type.Token;
import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a dimen value with a length which is a multiple of
 * math unints (mu).
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Mudimen implements Serializable {

    /**
     * Scan a math unit.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return the number of scaled points for the mu
     *
     * @throws GeneralException in case of an error
     */
    public static long scanMu(final Context context, final TokenSource source)
            throws GeneralException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException("mu");
        }
        long value = GlueComponent.scanFloat(context, source, t);
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
    private long length;

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
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @throws GeneralException in case of an error
     */
    public Mudimen(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        this.length = scanMu(context, source);
    }

    /**
     * Getter for length.
     *
     * @return the length
     */
    public long getLength() {

        return this.length;
    }

    /**
     * Return the string representation of the instance.
     *
     * @return the string representation of this glue
     * @see "TeX -- The Program [???]"
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

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

}