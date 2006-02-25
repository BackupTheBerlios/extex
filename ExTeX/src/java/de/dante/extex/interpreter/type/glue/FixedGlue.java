/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.glue;

import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.util.exception.GeneralException;

/**
 * This interface describes the features of a
 * {@link de.dante.extex.interpreter.type.glue.Glue Glue} which do not modify
 * the value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public interface FixedGlue {

    /**
     * The constant <tt>NORMAL_ORDER</tt> contains the value for the normal
     * &ndash; immutable &ndash; order.
     */
    public static final int NORMAL_ORDER = 0;

    /**
     * The constant <tt>ZERO</tt> contains the ...
     */
    public static final FixedGlue ZERO = new Glue(0);

    /**
     * Make a copy of this object.
     *
     * @return a new instance with the same internal values
     */
    Glue copy();

    /**
     * Getter for the length.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the length of the glue.
     *
     * @return the natural length
     */
    Dimen getLength();

    /**
     * Getter for shrink.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the shrink of the glue.
     *
     * @return the shrink.
     */
    FixedGlueComponent getShrink();

    /**
     * Getter for stretch.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the stretch of the glue.
     *
     * @return the stretch.
     */
    FixedGlueComponent getStretch();

    /**
     * Test that the given Glue is differnt from a given one.
     *
     * @param glue the glue to compare with
     *
     * @return <code>true</code> iff they are different
     */
    boolean ne(FixedGlue glue);

    /**
     * Provide a string representation of this instance.
     *
     * @return the string representation of this glue
     * @see "<logo>TeX</logo> &ndash; The Program [178,177]"
     */
    String toString();

    /**
     * Determine the printable representation of the object and return it as a
     * list of Tokens.
     *
     * @param factory the token factory ton get new tokens from
     *
     * @return the string representation of this glue
     *
     * @throws GeneralException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [178,177]"
     */
    Tokens toToks(TokenFactory factory)
            throws GeneralException;
}