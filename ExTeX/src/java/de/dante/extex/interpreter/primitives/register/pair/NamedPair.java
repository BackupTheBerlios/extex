/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.pair;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.ContextExtension;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.InterpreterExtensionException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.pair.Pair;
import de.dante.extex.interpreter.type.pair.PairConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the pair valued primitives.
 * It sets the named pair register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \xy=1.234 3.45
 * </pre>
 *
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class NamedPair extends AbstractAssignment
        implements
            Theable,
            PairConvertible {

    /**
     * Creates a new object.
     * @param name the name for debugging
     */
    public NamedPair(final String name) {

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
            throws InterpreterException {

        if (context instanceof ContextExtension) {

            ContextExtension contextextex = (ContextExtension) context;

            String key = getKey(context, source);
            source.getOptionalEquals(context);
            Pair value = new Pair(context, source);
            contextextex.setPair(key, value, prefix.isGlobal());
            prefix.clearGlobal();

        } else {
            throw new InterpreterExtensionException();
        }
    }

    /**
     * set the value
     *
     * @param context    the interpreter context
     * @param value      the new value
     * @throws InterpreterException if no extension is configured.
     */
    public void set(final Context context, final Pair value)
            throws InterpreterException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            contextextex.setPair(getName(), value);
        } else {
            throw new InterpreterExtensionException();
        }
    }

    /**
     * Set the value
     *
     * @param context    the interpreter context
     * @param value      the new value as String
     * @throws InterpreterException if no extension is configured
     */
    public void set(final Context context, final String value)
            throws InterpreterException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            contextextex.setPair(getName(), new Pair(value));
        } else {
            throw new InterpreterExtensionException();
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            String key = getKey(context, source);
            String s = contextextex.getPair(key).toString();
            return new Tokens(context, s);
        }
        throw new InterpreterExtensionException();
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param context the context
     * @param source  the source
     * @return the key
     * @throws InterpreterException in case of an error.
     */
    protected String getKey(final Context context, final TokenSource source)
            throws InterpreterException {

        return getName();
    }

    /**
     * @see de.dante.extex.interpreter.type.pair.PairConvertible#convertPair(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Pair convertPair(final Context context, final TokenSource source)
            throws InterpreterException {

        if (context instanceof ContextExtension) {
            ContextExtension contextextex = (ContextExtension) context;
            String key = getKey(context, source);
            Pair val = contextextex.getPair(key);
            return (val != null ? val : new Pair());
        }
        throw new InterpreterExtensionException();
    }
}