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

package de.dante.extex.interpreter.primitives.math;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.MissingMathException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.math.NoadConsumer;

/**
 * This is the base class for all math primitives.
 * It tries to ensure that the primitive is invoked in math mode only.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public abstract class AbstractMathCode extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public AbstractMathCode(final String name) {

        super(name);
    }

    /**
     * Get the current list maker as Noad consumer. If the current list maker is
     * not of the proper type then an exception is thrown.
     *
     * @param context the interpreter context
     * @param typesetter the master typesetter
     *
     * @return the current list maker
     *
     * @throws InterpreterException in case of an error
     */
    protected NoadConsumer getListMaker(final Context context,
            final Typesetter typesetter) throws InterpreterException {

        ListMaker lm = typesetter.getListMaker();
        if (!(lm instanceof NoadConsumer)) {
            throw new MissingMathException(printableControlSequence(context));
        }
        return (NoadConsumer) lm;
    }
}