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
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
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
     * @param length ...
     * @param stretch ...
     * @param shrink ...
     */
    public Muskip(final GlueComponent length, final GlueComponent stretch,
            final GlueComponent shrink) {
        super();
        this.length = length;
        this.stretch = stretch;
        this.shrink = shrink;
    }

    /**
     * Creates a new object.
     * 
     * @param length ...
     */
    public Muskip(final Dimen length) {
        super();
        this.length = length;
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
        this.length = new Dimen(context, source);
        if (source.scanKeyword("plus")) {
            this.stretch = new GlueComponent(context, source, true);
        }
        if (source.scanKeyword("minus")) {
            this.shrink = new GlueComponent(context, source, true);
        }
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
        return length.toString()+" "; //TODO incomplete
    }

}
