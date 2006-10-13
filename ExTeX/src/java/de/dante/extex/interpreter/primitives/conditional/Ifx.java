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
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ComparableCode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\ifx</code>.
 *
 * <doc name="ifx">
 * <h3>The Primitive <tt>\ifx</tt></h3>
 * <p>
 *  The primitive <tt>\ifx</tt> compares the following two tokens. If the
 *  following tokens are no macros then the comparison succeeds if they agree
 *  in category code and character.
 * </p>
 * <p>
 *  If the argument tokens are control sequences or active characters then the
 *  assigned values are compared. If the arguments are bound to macros then
 *  the comparison succeeds if the status of <i>outer</i> and <i>long</i> are
 *  the same, the patterns are the same and the body texts are equivalent
 * </p>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
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
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \ifx\a\x ok \fi  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.28 $
 */
public class Ifx extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

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
        } else if (t1 instanceof CodeToken) {
            Code c1 = context.getCode((CodeToken) t1);

            if (c1 instanceof ComparableCode) {
                return ((ComparableCode) c1).compare(t2, context);
            }

        }
        if (t2 instanceof CodeToken) {

            Code c2 = context.getCode((CodeToken) t2);

            if (c2 instanceof ComparableCode) {
                return ((ComparableCode) c2).compare(t1, context);
            }

        }

        return t1.equals(t2);
    }

}
