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
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive <code>\if</code>.
 *
 * <doc name="if">
 * <h3>The Primitive <tt>\if</tt></h3>
 * <p>
 *  The primitive expands the tokens following it until two unexpandable tokens
 *  are found. The conditional is true iff the character codes of the two tokens
 *  agree.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;if&rang;
 *     &rarr; <tt>\if</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken(Context)
 *       &lang;token<sub>1</sub>&rang;} {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken(Context)
 *       &lang;token<sub>2</sub>&rang;} &lang;true text&rang; <tt>\fi</tt>
 *     | <tt>\if</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken(Context)
 *       &lang;token<sub>1</sub>&rang;} {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken(Context)
 *       &lang;token<sub>2</sub>&rang;} &lang;true text&rang; <tt>\else</tt> &lang;false text&rang; <tt>\fi</tt> </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \if\a\x ok \fi  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public class If extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public If(final String name) {

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

        Token t1 = source.scanToken(context);
        Token t2 = source.scanToken(context);

        if (t1 == null || t2 == null) {
            throw new EofException(printableControlSequence(context));
        }

        UnicodeChar c1 = t1.getChar();
        UnicodeChar c2 = t2.getChar();
        return (c1 == null ? c2 == null : c1.equals(c2));
    }

}
