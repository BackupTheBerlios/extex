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

package de.dante.extex.interpreter.primitives.register.bool;

import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.BoolConvertible;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextExtension;
import de.dante.extex.interpreter.type.Bool;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.main.MainExTeXExtensionException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the bool valued primitives.
 * It sets the named bool register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \debug=true
 * \debug=false
 * \debug=on
 * \debug=off
 * \debug=0
 * \debug=7
 * </pre>
 *
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class NamedBool extends AbstractAssignment
        implements
            Theable,
            BoolConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedBool(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.AbstractAssignment#assign(
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
            source.scanOptionalEquals();
            Bool value = new Bool(context, source);
            contextextex.setBool(key, value, prefix.isGlobal());

        } else {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * set the value
     *
     * @param context    the interpreter context
     * @param value      the new value
     * @throws GeneralException ...
     */
    public void set(final Context context, final Bool value)
            throws GeneralException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            contextextex.setBool(getName(), value);
        } else {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * Set the value
     *
     * @param context    the interpreter context
     * @param value      the new value as String
     * @throws GeneralException ...
     */
    public void set(final Context context, final String value)
            throws GeneralException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            contextextex.setBool(getName(), new Bool(value));
        } else {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * @see de.dante.extex.interpreter.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            String key = getKey(source);
            String s = contextextex.getBool(key).toString();
            return new Tokens(context, s);
        } else {
            throw new MainExTeXExtensionException();
        }
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source ...
     * @return the key
     * @throws GeneralException ...
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName();
    }

    /**
     * @see de.dante.extex.interpreter.BoolConvertable#convertBoot(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Bool convertBoot(final Context context, final TokenSource source)
            throws GeneralException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;

            String key = getKey(source);
            Bool value = contextextex.getBool(key);
            return (value != null ? value : new Bool());

        } else {
            throw new MainExTeXExtensionException();
        }
    }
}