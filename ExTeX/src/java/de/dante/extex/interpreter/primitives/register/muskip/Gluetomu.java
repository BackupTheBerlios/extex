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

package de.dante.extex.interpreter.primitives.register.muskip;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.CantUseInException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.interpreter.type.muskip.Muskip;
import de.dante.extex.interpreter.type.muskip.MuskipConvertible;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\gluetomu</code>.
 *
 * <doc name="gluetomu">
 * <h3>The Primitive <tt>\gluetomu</tt></h3>
 * <p>
 *  The primitive <tt>\gluetomu</tt> converts a glue specification to a muglue
 *  specification. For this conversion 1mu=1pt is assumed. This primitive can be
 *  used wherever a muskip is expected.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;gluetomu&rang;
 *      &rarr; <tt>\gluetomu</tt> &lang;glue&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \muskip0=\gluetomu1pt  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Gluetomu extends AbstractCode implements MuskipConvertible {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060513L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Gluetomu(final String name) {

        super(name);
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

        Glue glue = Glue.parse(source, context, typesetter);

        return new Muskip(glue.getLength(), glue.getStretch(), glue.getShrink());
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

        throw new CantUseInException(printableControlSequence(context),
                typesetter.getMode().toString());
    }

}
