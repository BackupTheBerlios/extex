/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.util.GeneralException;


/**
 * This class provides objects of type Dimen where all setters are redefined
 * to produce an exception. Thus the object is in fact immutable.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ImmutableDimen extends Dimen implements Serializable {

    /**
     * Creates a new object.
     *
     * @param value ...
     */
    public ImmutableDimen(final long value) {

        super(value);
    }

    /**
     * Creates a new object.
     *
     * @param value
     */
    public ImmutableDimen(final Dimen value) {

        super(value);
    }

    /**
     * @see de.dante.extex.interpreter.type.Dimen#add(de.dante.extex.interpreter.type.Dimen)
     */
    public void add(final Dimen d) {

        throw new RuntimeException("Unable to add to an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.Dimen#divide(long)
     */
    public void divide(final long denom) throws GeneralHelpingException {

        throw new RuntimeException("Unable to divide an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.Dimen#max(de.dante.extex.interpreter.type.Dimen)
     */
    public void max(final Dimen d) {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.Dimen#multiply(long)
     */
    public void multiply(final long factor) {

        throw new RuntimeException("Unable to multiply an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.GlueComponent#set(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */

    public void set(final Context context, final TokenSource source)
            throws GeneralException {

        throw new RuntimeException("Unable to set an immutable object");
    }

    /**
     * @see de.dante.extex.interpreter.type.Dimen#subtract(de.dante.extex.interpreter.type.Dimen)
     */
    public void subtract(final Dimen d) {

        throw new RuntimeException("Unable to subtract from an immutable object");
    }

}
