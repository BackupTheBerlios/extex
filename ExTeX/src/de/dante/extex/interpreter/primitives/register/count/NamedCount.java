/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Advanceable;
import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.Divideable;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Multiplyable;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

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
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class NamedCount extends AbstractAssignment implements ExpandableCode,
    Advanceable, Multiplyable, Divideable, Theable, CountConvertable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedCount(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Advanceable#advance(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void advance(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source);
        source.scanKeyword("by");

        long value = Count.scanCount(context, source);
        value += context.getCount(key).getValue();

        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.AbstractAssignment#assign(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source);
        source.scanOptionalEquals();

        long value = Count.scanCount(context, source);
        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String key = getKey(source);
        source.push(context.getCount(key).toToks(context));
    }

    /**
     * @see de.dante.extex.interpreter.CountConvertable#convertCount(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertCount(final Context context, final TokenSource source)
            throws GeneralException {

        String key = getKey(source);
        Count c = context.getCount(key);
        return (c != null ? c.getValue() : 0);
    }

    /**
     * @see de.dante.extex.interpreter.Divideable#divide(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void divide(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source);
        source.scanKeyword("by");

        long value = Count.scanCount(context, source);

        if (value == 0) {
            throw new GeneralHelpingException("TTP.ArithOverflow");
        }

        value = context.getCount(key).getValue() / value;
        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.Multiplyable#multiply(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void multiply(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source);
        source.scanKeyword("by");

        long value = Count.scanCount(context, source);
        value *= context.getCount(key).getValue();
        context.setCount(key, value, prefix.isGlobal());
    }

    /**
     * @see de.dante.extex.interpreter.Code#set(de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    public void set(final Context context, final String value)
            throws GeneralException {

        try {
            context.setCount(getName(), //
                             (value.equals("") ? 0 : Long.parseLong(value)));
        } catch (NumberFormatException e) {
            throw new GeneralException(e); //TODO
        }
    }

    /**
     * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        String key = getKey(source);
        String s = context.getCount(key).toString();
        Tokens toks = new Tokens(context, s);
        return toks;
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source the source for new tokens
     *
     * @return the key for the current register
     *
     * @throws GeneralException in case that a derived class need to throw an
     *             Exception this on e is declared.
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName();
    }

}
