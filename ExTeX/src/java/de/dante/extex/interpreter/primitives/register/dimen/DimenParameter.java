/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.dimen;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\dimen</code>.
 * It sets the named length register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \day=345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class DimenParameter extends DimenPrimitive implements InitializableCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public DimenParameter(final String name) {

        super(name);
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param context the interpreter context to use
     * @param source the source for new tokens
     *
     * @return the key for the current register
     *
     * @see de.dante.extex.interpreter.primitives.register.dimen.AbstractDimen#getKey(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    protected String getKey(final Context context, final TokenSource source) {

        return getName();
    }

    /**
     * Initialize the Code with some value coming from a Token source.
     *
     * @param context the interpreter context
     * @param source the source of information for the initialization
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     *
     * @see de.dante.extex.interpreter.type.InitializableCode#init(
     *      de.dante.extex.interpreter.context.Context, TokenSource, Typesetter)
     */
    public void init(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        if (source == null) {
            return;
        }
        Token t = source.getNonSpace(context);
        if (t == null) {
            return;
        }
        source.push(t);
        Dimen d = new Dimen(context, source, typesetter);
        context.setDimen(getKey(context, null), d, true);
    }

}
