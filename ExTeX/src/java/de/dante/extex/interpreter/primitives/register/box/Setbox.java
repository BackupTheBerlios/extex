/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.box;

import java.io.Serializable;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Code;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\setbox</code>.
 *
 * <doc name="setbox">
 * <h3>The Primitive <tt>\setbox</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;setbox&rang;
 *      &rarr; <tt>\setbox</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber()
 *          &lang;8-bit&nbsp;number&rang;}...</pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \setbox ...  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class Setbox extends BoxParameter implements Code, Serializable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Setbox(final String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the box register.
     *
     * @param source the source for new tokens
     * @param context the interpreter context to use
     *
     * @return the key for the box register
     *
     * @throws GeneralException in case of an error
     */
    protected String getKey(final TokenSource source, final Context context)
            throws GeneralException {

        String number = Long.toString(source.scanNumber());

        if (Namespace.SUPPORT_NAMESPACE_BOX) {
            return context.getNamespace() + "box#" + number;
        } else {
            return "box#" + number;
        }
    }

}