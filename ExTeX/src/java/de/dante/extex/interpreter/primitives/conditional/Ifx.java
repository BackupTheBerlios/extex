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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\ifx</code>.
 *
 * <doc name="ifx">
 * <h3>The Primitive <tt>\ifx</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ifx&rang;
 *      &rarr; <tt>\ifx</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getToken(Context)
 *        &lang;token<sub>1</sub>&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getToken(Context)
 *        &lang;token<sub>2</sub>&rang;}; &lang;true text&rang; <tt>\fi</tt>
 *      | <tt>\ifx</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getToken(Context)
 *        &lang;token<sub>1</sub>&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getToken(Context)
 *        &lang;token<sub>2</sub>&rang;} &lang;true text&rang; <tt>\else</tt> &lang;false text&rang; <tt>\fi</tt> </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \ifx\a\x ok \fi  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.23 $
 */
public class Ifx extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifx(final String name) {

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

        Token t1 = source.getToken(context);
        Token t2 = source.getToken(context);

        if (t1 == null || t2 == null) {
            throw new EofException(printableControlSequence(context));
        }

        return t1.equals(t2);
    }
}