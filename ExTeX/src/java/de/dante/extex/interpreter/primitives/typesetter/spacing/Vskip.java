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

package de.dante.extex.interpreter.primitives.typesetter.spacing;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.primitives.typesetter.AbstractVerticalCode;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\vskip</code>.
 *
 * <doc name="vskip">
 * <h3>The Primitive <tt>\vskip</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 * The formal description of this primitive is the following:
 *
 * <pre class="syntax">
 *     &lang;vskip&rang;
 *         &rarr; <tt>\vskip</tt> &lang;Glue&rang;
 * </pre>
 *
 * </p>
 * <p>
 * Examples:
 *
 * <pre class="TeXSample">
 *     \vskip 1em plus 1pt minus 1pt
 * </pre>
 *
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Vskip extends AbstractVerticalCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Vskip(final String name) {

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

        ensureVerticalMode(typesetter);
        Glue g = new Glue(source, context);
        typesetter.addGlue(g);
        return true;
    }

}