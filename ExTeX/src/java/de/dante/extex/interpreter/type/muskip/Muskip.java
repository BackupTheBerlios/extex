/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.type.muskip;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Muskip implements Serializable {

    /** ... */
    private GlueComponent length = new GlueComponent(0);

    /** ... */
    private GlueComponent shrink = new GlueComponent(0);

    /** ... */
    private GlueComponent stretch = new GlueComponent(0);

    /**
     * Creates a new object.
     */
    public Muskip() {

        super();
    }

    /**
     * Creates a new object.
     *
     * @param theLength ...
     * @param theStretch ...
     * @param theShrink ...
     */
    public Muskip(final GlueComponent theLength,
            final GlueComponent theStretch, final GlueComponent theShrink) {

        super();
        this.length = theLength;
        this.stretch = theStretch;
        this.shrink = theShrink;
    }

    /**
     * Creates a new object.
     *
     * @param theLength ...
     */
    public Muskip(final Dimen theLength) {

        super();
        this.length = theLength;
    }

    /**
     * Creates a new object.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @throws GeneralException in case of an error
     */
    public Muskip(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        this.length = new Dimen(getMu(context, source));
        if (source.scanKeyword("plus")) {
            this.stretch = new GlueComponent(getMu(context, source));
        }
        if (source.scanKeyword("minus")) {
            this.shrink = new GlueComponent(getMu(context, source));
        }
    }

    /**
     * ...
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    private long getMu(final Context context, final TokenSource source)
            throws GeneralException {

        Token t = source.getToken();
        if (t == null) {
            throw new RuntimeException("unimplemented");
        }
        long value = GlueComponent.scanFloat(source, t);
        if (!source.scanKeyword("mu")) {
            throw new RuntimeException("unimplemented");
        }
        // TODO: use the math family fonts instead
        Dimen em = context.getTypesettingContext().getFont().getEm();
        value = value * em.getValue() / GlueComponent.ONE;
        return value;
    }

    /**
     * Getter for the natural length of this muskip.
     * The object returned is a copy which is not related to the internal
     * value. Thus it can be used for any computations necessary.
     *
     * @return the natural length
     */
    public Dimen getLength() {

        return new Dimen(length.getValue());
    }

    /**
     * ...
     *
     * @return the string representation of this glue
     * @see "TeX -- The Program [???]"
     */
    public String toString() {

        return length.toString() + " "; //TODO incomplete
    }

}