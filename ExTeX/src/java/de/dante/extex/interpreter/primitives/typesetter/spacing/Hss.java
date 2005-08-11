/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter.spacing;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.typesetter.AbstractHorizontalCode;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\hss</code>.
 *
 * <doc name="hss">
 * <h3>The Primitive <tt>\hss</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hss&rang;
 *        &rarr; <tt>\hss</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \hss  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Hss extends AbstractHorizontalCode {

    /**
     * The field <tt>HSS</tt> contains the glue to insert for this primitive.
     */
    private static final Glue HSS = new Glue(GlueComponent.ZERO,
            GlueComponent.ONE_FIL, GlueComponent.ONE_FIL);

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hss(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        ensureHorizontalMode(typesetter);
        try {
            typesetter.addGlue(HSS);
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
