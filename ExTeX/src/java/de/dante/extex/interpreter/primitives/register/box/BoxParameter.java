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

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides a Box parameter implementation.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 *
 * @version $Revision: 1.8 $
 */
public class BoxParameter extends AbstractCode implements Code, Serializable {

    /**
     * Creates a new object.
     *
     * @param name the name of the box
     */
    public BoxParameter(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source, context);
        source.getOptionalEquals(context);
        Box box = source.getBox(context, typesetter);
        context.setBox(key, box, prefix.isGlobal());
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source the source for new tokens -- if required
     * @param context the interpreter context to use
     *
     * @return the key for the box register
     *
     * @throws GeneralException in case oif an error
     */
    protected String getKey(final TokenSource source, final Context context)
            throws GeneralException {

        if (Namespace.SUPPORT_NAMESPACE_BOX) {
            return context.getNamespace() + "\b" + getName();
        } else {
            return getName();
        }
    }

}
