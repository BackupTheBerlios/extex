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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\or</code>.
 *
 * <doc name="or">
 * <h3>The Primitive <tt>\or</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *  <pre class="syntax">
 *    &lang;or&rang;
 *     &rarr; <tt>\ifcase</tt> ... <tt>\or</tt> ... <tt>\fi</tt> </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class Or extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

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
            throws InterpreterException {

        Conditional cond = context.popConditional();

        if (cond == null) {
            throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                    printableControlSequence(context));
        } else if (AbstractIf.skipToElseOrFi(context, source)) {
            // \else has been found; search for the \fi
            if (AbstractIf.skipToElseOrFi(context, source)) {
                // just another \else is too much
                throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                        context.esc("else"));
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Conditional cond = context.popConditional();

        if (cond == null) {
            throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                    printableControlSequence(context));
        } else if (AbstractIf.skipToElseOrFi(context, source)) {
            // \else has been found; search for the \fi
            if (AbstractIf.skipToElseOrFi(context, source)) {
                // just another \else is too much
                throw new HelpingException(getLocalizer(), "TTP.ExtraOrElseFi",
                        context.esc("else"));
            }
        }
    }

}
