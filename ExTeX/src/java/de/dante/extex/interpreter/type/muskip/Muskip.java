/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.scanner.type.Token;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a skip value with a variable length of order 0.
 * The actual length is a multiple of math unints (mu).
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Muskip implements Serializable {

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
        }
        long value = GlueComponent.scanFloat(context, source, t);
        if (!source.getKeyword(context, "mu")) {
            throw new HelpingException(//
                    LocalizerFactory.getLocalizer(Muskip.class.getName()),
                    "TTP.IllegalMu");
        }
        return value;
    }

    /**
     * The field <tt>kill</tt> contains the indicator that the following glue
     * might be killed.
     */
    private boolean kill;

    /**
     * The field <tt>length</tt> contains the the natural length.
     */
    private GlueComponent length = new GlueComponent(0);

    /**
     * The field <tt>shrink</tt> contains the shrinkability specification.
     */
    private GlueComponent shrink = new GlueComponent(0);

    /**
     * The field <tt>stretch</tt> contains the stretchability specification.
     */
    private GlueComponent stretch = new GlueComponent(0);

    /**
     * Creates a new object.
     * All components are 0.
     */
    public Muskip() {

        super();
    }

    /**
     * Creates a new object and fills it from a token stream.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @throws InterpreterException in case of an error
     */
    public Muskip(final Context context, final TokenSource source)
            throws InterpreterException {

        super();
        this.length = new Dimen(scanMu(context, source));
        if (source.getKeyword(context, "plus")) {
            this.stretch = new GlueComponent(scanMu(context, source));
        }
        if (source.getKeyword(context, "minus")) {
            this.shrink = new GlueComponent(scanMu(context, source));
        }
    }

    /**
     * Creates a new object.
     * Strechablity and shrinkability are 0.
     *
     * @param theLength the natural length
     */
    public Muskip(final Dimen theLength) {

        super();
        this.length = theLength;
    }

    /**
     * Creates a new object.
     *
     * @param theLength the natural length
     * @param theStretch the stretchability
     * @param theShrink the shrinkability
     */
    public Muskip(final GlueComponent theLength,
            final GlueComponent theStretch, final GlueComponent theShrink) {

        super();
        this.length = theLength;
        this.stretch = theStretch;
        this.shrink = theShrink;
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
     * Getter for shrink.
     *
     * @return the shrink
     */
    public GlueComponent getShrink() {

        return this.shrink;
    }
    /**
     * Getter for stretch.
     *
     * @return the stretch
     */
    public GlueComponent getStretch() {

        return this.stretch;
    }

    /**
     * Getter for the natural length of this muskip.
     * The object returned is a copy which is not related to the internal
     * value. Thus it can be used for any computations necessary.
     *
     * @return the natural length
     */
    public Dimen getxxLength() {

        return new Dimen(length.getValue());
    }

    /**
     * Getter for kill.
     *
     * @return the kill
     */
    public boolean isKill() {

        return this.kill;
    }

    /**
     * Setter for kill.
     *
     * @param kill the kill to set
     */
    public void setKill(final boolean kill) {

        this.kill = kill;
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

        length.toString(sb, 'm', 'u');
        if (stretch.ne(GlueComponent.ZERO)) {
            sb.append(" plus ");
            stretch.toString(sb, 'm', 'u');
        }
        if (shrink.ne(GlueComponent.ZERO)) {
            sb.append(" plus ");
            shrink.toString(sb, 'm', 'u');
        }
    }
}