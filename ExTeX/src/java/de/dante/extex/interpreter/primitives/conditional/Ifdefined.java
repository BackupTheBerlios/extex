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
package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;


/**
 * This class provides an implementation for the primitive <code>\if</code>.
 *
 * <doc name="unless">
 * <h3>The Primitive <tt>&#x005c;unless</tt></h3>
 * <p><strong>Copied of the eTeX reference</strong>.</p>
 * <p>
 * similar in effect to &#x005c;unless \ifx &#x005c;undefined, but
 * does not require &#x005c;undefined to actually be undefined, since
 * no explicit comparison is made with any particular control
 * sequence.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 * TODO
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    &#x005c;ifdefined\TESTNAME\else not\fi defined</pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.4 $
 */
public class Ifdefined extends AbstractIf {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifdefined(final String name) {
        super(name);
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


        CodeToken token = source.getControlSequence(context);
        Code code = context.getCode(token);

        // TODO: is this correct? (TE)
        return code != null;
    }

}
