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

package de.dante.extex.interpreter.primitives.register.box;

import java.io.Serializable;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;

/**
 * This is the abstract base class for primitives dealing with box registers.
 * It provides a method to get the key of a box register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public abstract class AbstractBox extends AbstractCode implements Serializable {

    /**
     * Return the key (the number) for the box register.
     *
     * <doc type="syntax" name="box register name">
     * <p>
     *  A box register name determines under which key a box register can be
     *  addressed. In <logo>TeX</logo> this used to be a positive number only.
     *  This has been extended to allow also a token list in braces.
     * </p>
     *
     * <h4>Syntax</h4>
     * <pre class="syntax">
     *   &lang;box register name&rang;
     *       &rarr; {@linkplain
     *        de.dante.extex.interpreter.TokenSource#scanTokens(Context)
     *        &lang;tokens&rang;}
     *        | {@linkplain de.dante.extex.interpreter.TokenSource#scanNumber()
     *        &lang;number&rang;}  </pre>
     *
     * TODO gene: doc incomplete
     *
     * <h4>Examples</h4>
     * <pre>
     *  123
     *  {abc}
     * </pre>
     * </doc>
     *
     *
     * @param context the interpreter context to use
     * @param source the source for new tokens
     *
     * @return the key for the box register
     *
     * @throws InterpreterException in case of an error
     */
    public static String getKey(final Context context,
            final TokenSource source) throws InterpreterException {

        String name = source.scanRegisterName(context);

        if (Namespace.SUPPORT_NAMESPACE_DIMEN) {
            return context.getNamespace() + "#box#" + name;
        } else {
            return "box#" + name;
        }
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractBox(final String name) {

        super(name);
    }

}
