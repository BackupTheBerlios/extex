/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.i18n.MathHelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.NoadConsumer;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;

/**
 * This is the base class for all math primitives.
 * It tries to ensure that the primitive is invoked in math mode only.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
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
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        if (!typesetter.getMode().isMath()) {

            throw new MathHelpingException(printableControlSequence(context));
        }
        return true;
    }

    /**
     * Scan some Noads.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    protected Noad scanNoad(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        return getListMaker(typesetter).scanNoad(context, source);
    }

    /**
     * Get the current list maker as Noad consumer. If the current list maker is
     * not of the proper type then an exception is thrown.
     *
     * @param typesetter the master typesetter
     *
     * @return the current list maker
     *
     * @throws GeneralException in case of an error
     */
    protected NoadConsumer getListMaker(final Typesetter typesetter)
            throws GeneralException {

        ListMaker lm = typesetter.getListMaker();
        if (!(lm instanceof NoadConsumer)) {
            //TODO error unimplemented
            throw new RuntimeException("unimplemented");
        }
        return (NoadConsumer) lm;
    }
}