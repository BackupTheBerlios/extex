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

package de.dante.extex.interpreter.primitives.prefix;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.PrefixCode;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\global</code>.
 * It does simply nothing, but as a side effect the prefix <i>GLOBAL</i> is
 * added to the prefixes.
 *
 * <doc name="global">
 * <h3>The Prefix Primitive <tt>\global</tt></h3>
 * <p>
 *  The primitive <tt>\global</tt> is a prefix macro. It does not do anything
 *  by its own but works in combination with a following primitive token only.
 *  If the following token constitutes an assignment then the assignment is not
 *  restricted to the current group but acts globallay in all groups.
 * </p>
 * <p>
 *  If the following command token does not happen to be an operation for
 *  which the global modifier is applicable then a warning might be raised.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;global&rang;
 *      &rarr; <tt>\global</tt> &lang;...&rang; </pre>
 * </p>
 * <h4>Examples</h4>
 * <p>
 *  The following example shows that two macros defined in a group. The first
 *  macro falls back to its previous binding when the group is closed.
 *  The second macro has the same binding in all groups.
 *  defined.
 * </p>
 * <pre class="TeXSample">
 *   \begingroup
 *     \def\a{123}
 *     \global\def\b{123}
 *   \endgroup  </pre>
 * <p>
 *  The following example shows that two count registers are set in a group.
 *  The first count register keeps its value untile the group is closed and
 *  falls back to the value it had when the group has been entered. The second
 *  count register keeps its value even when the group is closed.
 * </p>
 * <pre class="TeXSample">
 *   \begingroup
 *     \count1=123
 *     \global\count2=45
 *   \endgroup  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.17 $
 */
public class Global extends AbstractCode implements PrefixCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Global(final String name) {

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
            final TokenSource source, final Typesetter typesetter) throws InterpreterException {

        prefix.setGlobal();
    }
}