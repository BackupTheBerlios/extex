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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.InitializableCode;

/**
 * This class provides an implementation for the count valued primitives like
 * <code>\day</code>. It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>
 * Example
 * </p>
 *
 * <pre>
 *  \day=345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class IntegerParameter extends CountPrimitive
        implements
            InitializableCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public IntegerParameter(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.box.AbstractBox#getKey(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    protected String getKey(final Context context, final TokenSource source) {

        return getName();
    }

    /**
     * Initialize the Code with some value coming from a String.
     *
     * @param context the interpreter context
     * @param value the source of information for the initialization
     *
     * @see de.dante.extex.interpreter.type.InitializableCode#init(
     *      de.dante.extex.interpreter.context.Context, java.lang.String)
     */
    public void init(final Context context, final String value)
            throws InterpreterException {

        if (!value.equals("")) {
            try {
                long val = Long.parseLong(value);
                context.setCount(getKey(context, null), val, true);
            } catch (NumberFormatException e) {
                throw new HelpingException(getLocalizer(),
                        "NumberFormatException",
                        printableControlSequence(context), value);
            }
        }
    }

}
