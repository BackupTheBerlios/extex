/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.util.GeneralException;

/**
 * This is the abstarct base class for primitives dealing with box registers.
 * It provides a method to get the key of a box register.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public abstract class AbstractBox extends AbstractCode implements Serializable {

    /**
     * Return the key (the number) for the box register.
     *
     * @param name the name (number) of the register
     * @param context the interpreter context to use
     * @param source the source for new tokens
     *
     * @return the key for the box register
     *
     * @throws GeneralException in case of an error
     */
    public static String getKey(final String name, final Context context,
            final TokenSource source) throws GeneralException {

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

    /**
     * Return the key (the number) for the box register.
     *
     * @param context the interpreter context to use
     * @param source the source for new tokens
     *
     * @return the key for the box register
     *
     * @throws GeneralException in case of an error
     */
    public String getKey(final Context context, final TokenSource source)
            throws GeneralException {

        String name = source.scanRegisterName(context);

        if (Namespace.SUPPORT_NAMESPACE_DIMEN) {
            return context.getNamespace() + "#box#" + name;
        } else {
            return "box#" + name;
        }
    }

}