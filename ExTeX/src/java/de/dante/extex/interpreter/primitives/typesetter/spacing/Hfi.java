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

package de.dante.extex.interpreter.primitives.typesetter.spacing;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.typesetter.AbstractHorizontalCode;
import de.dante.extex.interpreter.type.glue.FixedGlue;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\hfi</code>.
 *
 * <doc name="hfi">
 * <h3>The Primitive <tt>\hfi</tt></h3>
 * <p>
 *  The primitive <tt>\hfi</tt> inserts a new infinite glue into the output.
 *  The value of <tt>\hfi</tt> is an infinite quantity which is less than
 *  <tt>\hfil</tt>. This means that <tt>\hfil</tt> or a larger value
 *  suppress this glue. On the other side if no greater value is present then
 *  this value suppresses any finite value.
 * </p>
 * <p>
 *  This quantity has been introduced by <logo>Omega</logo>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hfi&rang;
 *        &rarr; <tt>\hfi</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \hfi  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Hfi extends AbstractHorizontalCode implements HorizontalSkip {

    /**
     * The field <tt>FIL</tt> contains the glue to insert for this primitive.
     */
    private static final Glue FI = new Glue(GlueComponent.ZERO,
            GlueComponent.ONE_FI, GlueComponent.ZERO);

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hfi(final String name) {

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

        switchToHorizontalMode(typesetter);
        typesetter.add(FI);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.typesetter.spacing.HorizontalSkip#getGlue(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public FixedGlue getGlue(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return FI;
    }

}
