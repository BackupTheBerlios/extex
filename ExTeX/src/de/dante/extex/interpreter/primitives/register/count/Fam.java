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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.TokenSource;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the count valued primitives like
 * <code>\fam</code>.
 *
 * <doc name="fam">
 * <h3>The Primitive <tt>\fam</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\fam</tt> ...  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    ...  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class Fam extends CountPrimitive {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Fam(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.IntegerParameter#getKey(
     *      de.dante.extex.interpreter.TokenSource, String)
     */
    protected String getKey(final TokenSource source, final String namespace)
            throws GeneralException {

        return "fam#" + Long.toString(source.scanNumber());
    }

}