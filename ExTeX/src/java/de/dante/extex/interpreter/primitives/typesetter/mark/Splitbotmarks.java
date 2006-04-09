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

package de.dante.extex.interpreter.primitives.typesetter.mark;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This class provides an implementation for the primitive
 * <code>\splitbotmarks</code>.
 *
 * <doc name="splitbotmarks">
 * <h3>The Primitive <tt>\splitbotmarks</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\splitbotmarks</tt> ...  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \splitbotmarks42  </pre>
 *  <pre class="TeXSample">
 *    \splitbotmarks\count0  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Splitbotmarks extends AbstractMarksCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Splitbotmarks(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.typesetter.mark.AbstractMarksCode#getValue(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    protected Tokens getValue(final Context context, final String key)
            throws InterpreterException {

        return context.getSplitBottomMark(key);
    }

}
