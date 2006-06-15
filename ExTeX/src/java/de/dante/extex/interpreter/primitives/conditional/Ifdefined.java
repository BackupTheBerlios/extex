/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\if</code>.
 *
 * <doc name="ifdefined">
 * <h3>The Primitive <tt>&#x005c;unless</tt></h3>
 *
 * <p><strong>Copied from the <logo>eTeX</logo> reference</strong>:
 * </p>
 * <p><i>
 *  similar in effect to <tt>&#x005c;unless</tt> <tt>\ifx</tt>
 *  <tt>&#x005c;undefined</tt>, but does not require <tt>&#x005c;undefined</tt>
 *  to actually be undefined, since no explicit comparison is made with any
 *  particular control sequence.
 * </i></p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ifdefined&rang;
 *      &rarr; <tt>\ifdefined</tt> ...<tt>\else</tt>...<tt>\fi</tt>
 *       |  <tt>\ifdefined</tt> ...<tt>\else</tt>...<tt>\fi</tt>  </pre>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 *   &#x005c;ifdefined\TESTNAME\else not\fi defined  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.14 $
 */
public class Ifdefined extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

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
            throws InterpreterException {

        Token token = source.getToken(context);

        return (token instanceof CodeToken
                && context.getCode((CodeToken) token) != null);
    }

}
