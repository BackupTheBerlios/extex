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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\or</code>.
 *
 * <doc name="or">
 * <h3>The Primitive <tt>\or</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Or extends AbstractIf {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Or(final String name) {

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
            throws GeneralException {

        Conditional cond = context.popConditional();

        if (cond == null) {
            throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                    printableControlSequence(context));
        } else if (skipToElseOrFi(context, source)) {
            throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                    context.esc("else"));
        }
    }

    /**
     * This method is overwritten here since <tt>\or</tt> does not count as
     * an opening conditional even so it is derived from
     * {@link de.dante.extex.interpreter.primitives.conditional.AbstractIf
     *  AbstractIf}.
     *
     * @see de.dante.extex.interpreter.type.Code#isIf()
     */
    public boolean isIf() {

        return false;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.conditional.AbstractIf#conditional(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        return false;
    }
}
