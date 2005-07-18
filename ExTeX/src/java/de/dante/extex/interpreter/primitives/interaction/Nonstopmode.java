/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.interaction;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\nonstopmode</code>. It does simply nothing, but as a side
 * effect all prefixes are zeroed and the interaction mode is set to
 * <tt>nonstopmode</tt>.
 *
 * <doc name="nonstopmode">
 * <h3>The Primitive <tt>\nonstopmode</tt></h3>
 * <p>
 *  This primitive sets the interaction mode to batch mode.
 *  In batch mode the processing is terminated if the program needs input from
 *  the terminal or n error occurs.
 * </p>
 * <p>
 *  The setting of the interaction mode is an assignment. The mode is always
 *  processed globally. This means it does not interact with the group concept.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;nonstopmode&rang;
 *      &rarr; <tt>\nonstopmode</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \nonstopmode  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Nonstopmode extends AbstractAssignment {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Nonstopmode(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        context.setInteraction(Interaction.NONSTOPMODE);
    }
}
