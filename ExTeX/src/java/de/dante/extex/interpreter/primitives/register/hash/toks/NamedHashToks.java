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

package de.dante.extex.interpreter.primitives.register.hash.toks;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextExtension;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.hash.toks.HashToks;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.main.MainExTeXExtensionException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the hash-toks valued primitives.
 * It sets the named pair register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \myhash{ {key1}{value1} {key2}{value2} {key3}{value3} ...}
 * \myhash={ {key1}{value1} {key2}{value2} {key3}{value3} ...}
 * </pre>
 *
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class NamedHashToks extends AbstractAssignment implements Theable {

    /**
     * Creates a new object.
     * @param name the name for debugging
     */
    public NamedHashToks(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        if (context instanceof ContextExtension) {

            ContextExtension contextextex = (ContextExtension) context;

            String key = getKey(source);
            source.getOptionalEquals();

            HashToks value = new HashToks(context, source);
            contextextex.setHashToks(key, value, prefix.isGlobal());

        } else {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            String key = getKey(source);
            String s = contextextex.getHashToks(key).toString();
            return new Tokens(context, s);
        } else {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source  the source
     * @return the key
     * @throws GeneralException ...
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName();
    }

}