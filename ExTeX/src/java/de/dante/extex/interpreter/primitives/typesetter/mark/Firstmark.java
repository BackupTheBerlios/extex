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

package de.dante.extex.interpreter.primitives.typesetter.mark;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.primitives.macro.MacroCode;
import de.dante.extex.interpreter.primitives.macro.MacroPattern;
import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This class provides an implementation for the primitive
 * <code>\firstmark</code>.
 *
 * <doc name="firstmark">
 * <h3>The Primitive <tt>\firstmark</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\firstmark ...</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \firstmark ...  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class  Firstmark extends MacroCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Firstmark(final String name) {

        super(name, Flags.NONE, MacroPattern.EMPTY, Tokens.EMPTY);
    }

}
