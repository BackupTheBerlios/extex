/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\ifscaled</code>.
 *
 * <doc name="ifscaled">
 * <h3>The Primitive <tt>\ifscaled</tt></h3>
 * <p>
 *  The primitive <tt>\ifscaled</tt> provides a conditional which compares two
 *  numerical values. The comparison for equality, greater, and less are
 *  possible.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ifscaled&rang;
 *      &rarr; <tt>\ifscaled</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;} &lang;op&rang; {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;} &lang;true text&rang; <tt>\fi</tt>
 *      | <tt>\ifscaled</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;} &lang;op&rang; {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;} &lang;true text&rang; <tt>\else</tt> &lang;false text&rang; <tt>\fi</tt>
 *
 *    &lang;op&rang;
 *      &rarr; [&lt;]
 *      | [=]
 *      | [&gt;]  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \ifscaled\count0&gt;42 abc \fi  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Ifscaled extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifscaled(final String name) {

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

        long value = ScaledNumber.parse(context, source, typesetter);
        Token rel = source.getToken(context);
        if (rel == null) {
            throw new EofException(printableControlSequence(context));
        }
        if (rel.getCatcode() == Catcode.OTHER) {
            switch (rel.getChar().getCodePoint()) {
                case '<':
                    return (value < ScaledNumber.parse(context, source,
                            typesetter));
                case '=':
                    return (value == ScaledNumber.parse(context, source,
                            typesetter));
                case '>':
                    return (value > ScaledNumber.parse(context, source,
                            typesetter));
                default:
            // fall-through
            }
        }

        throw new HelpingException(getLocalizer(), "IllegalOp");
    }

}
