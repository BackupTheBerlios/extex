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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Multiplyable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\multiply</code>.
 * The real work is done by the object implementing the Advanceable interface.
 *
 * Example
 * <pre>
 * \multiply\count12 345
 * \multiply\count12 by -345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class Multiply extends AbstractAssignment {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Multiply(final String name) {
        super(name);
    }

/**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.scanToken();

        if (!(cs instanceof ControlSequenceToken)) {
            throw new GeneralHelpingException("TTP.CantUseAfter",
                    cs.toString(), printableControlSequence(context));
        }

        Code code = context.getMacro(cs.getValue());

        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken", cs
                    .toString());
        } else if (code instanceof Multiplyable) {
            ((Multiplyable) code).multiply(prefix, context, source);
        } else {
            throw new GeneralHelpingException("TTP.CantUseAfter",
                    cs.toString(), printableControlSequence(context));
        }
    }
}
