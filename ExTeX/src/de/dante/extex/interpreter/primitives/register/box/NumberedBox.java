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

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\box</code>.
 *
 * Example
 *
 * <pre>
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class NumberedBox extends AbstractCode implements Boxable, Serializable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NumberedBox(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source, context.getNamespace());
        Box box = context.getBox(key);
        context.setBox(key, box, prefix.isGlobal());
        prefix.clear();
    }

    /**
     * Return the key (the number) for the box register.
     *
     * @param source the source for new tokens
     * @param namespace the namespace to use
     *
     * @return the key of the box register
     *
     * @throws GeneralException in case of an error
     */
    protected String getKey(final TokenSource source, final String namespace)
            throws GeneralException {

        return namespace + "box#" + Long.toString(source.scanNumber());
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *       de.dante.extex.interpreter.context.Context,
     *       de.dante.extex.interpreter.TokenSource,
     *       de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        String key = getKey(source, context.getNamespace());
        return context.getBox(key);
    }

}