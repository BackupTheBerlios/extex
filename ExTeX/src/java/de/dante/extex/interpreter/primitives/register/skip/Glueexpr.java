/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.skip;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.muskip.MuskipConvertible;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\glueexpr</code>.
 *
 * <doc name="glueexpr">
 * <h3>The Primitive <tt>\glueexpr</tt></h3>
 * <p>
 *  The primitive <tt>\glueexpr</tt> ...
 * </p>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;glueexpr&rang;
 *      &rarr; <tt>\glueexpr</tt> </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \glueexpr\skip0\relax  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Glueexpr extends AbstractCode implements MuskipConvertible {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    private static final long serialVersionUID = 2006L;

    /**
     * Creates a new object.
     *
     * @param codeName the name
     */
    public Glueexpr(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.extex.interpreter.type.muskip.MuskipConvertible#convertMuskip(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Muskip convertMuskip(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

}
