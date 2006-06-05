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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\endcsname</code>.
 *
 * <doc name="endcsname">
 * <h3>The Primitive <tt>\endcsname</tt></h3>
 * <p>
 *  The macro <tt>\endcsname</tt> is used in combination with the macro
 *  {@link de.dante.extex.interpreter.primitives.macro.Csname \csname} only.
 *  Whenever a <tt>\endcsname</tt> is seen alone it must
 *  be an error. Thus thus primitive produces an error message in any case.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;endcsname&rang;
 *      &rarr; <tt>\endscsname</tt>  </pre>
 *
 * <h4>Examples</h4>
 * <p>
 *  The following example shows a complicated way to invoke the macro
 *  <tt>abc</tt>. Here the primitive <tt>\endcsname</tt> is legal. It is
 *  consumed by the primitive <tt>\csname</tt> and not expanded on its own.
 * </p>
 * <pre class="TeXSample">
 *   \csname abc\endcsname  </pre>
 * </doc>
 *
 *
 * @see "TTP [1134]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class Endcsname extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Endcsname(final String name) {

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

        throw new HelpingException(getLocalizer(), "TTP.ExtraEndcsname",
                printableControlSequence(context));
    }

}
