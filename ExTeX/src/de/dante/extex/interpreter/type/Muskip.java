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

package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
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
     * @param source ...
     * @param context ...
     * @throws GeneralException in case of an error
     */
    public Muskip(final TokenSource source, final Context context)
            throws GeneralException {

        super();
        this.length = new Dimen(getMu(source, context));
        if (source.scanKeyword("plus")) {
            this.stretch = new GlueComponent(getMu(source, context));
        }
        if (source.scanKeyword("minus")) {
            this.shrink = new GlueComponent(getMu(source, context));
        }
    }

    /**
     * ...
     *
     * @param source
     * @param context
     * @return
     * @throws GeneralException
     */
    private long getMu(final TokenSource source, final Context context)
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
     * ...
     *
     * @return ...
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