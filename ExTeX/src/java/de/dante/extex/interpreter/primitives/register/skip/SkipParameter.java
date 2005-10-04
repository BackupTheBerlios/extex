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

package de.dante.extex.interpreter.primitives.register.skip;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\skip</code>.
 * It sets the named skip register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>
 * All features are inherited from
 * {@link de.dante.extex.interpreter.primitives.register.skip.SkipParameter SkipParameter}.
 * Just the key has to be provided under which this Glue has to be stored.
 * This key is constructed from the name, a hash mark and the running number.
 * </p>
 *
 * <p>Example</p>
 * <pre>
 * \skip12=345pt plus 12em
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class SkipParameter extends SkipPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public SkipParameter(final String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the skip register.
     *
     * @param source the source for the next tokens &ndash; if required
     * @param context the interpreter context to use
     *
     * @return the key for the skip register
     *
     * @throws GeneralException in case oif an error
     */
    protected String getKey(final Context context, final TokenSource source) {

        if (Namespace.SUPPORT_NAMESPACE_SKIP) {
            return context.getNamespace() + "\b" + getName();
        } else {
            return getName();
        }
    }

}