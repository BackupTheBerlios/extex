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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Advanceable;
import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.DimenConvertable;
import de.dante.extex.interpreter.Divideable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Multiplyable;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\dimen</code>.
 * It sets the named dimen register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>Example</p>
 * <pre>
 * \day=345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class NamedDimen extends AbstractAssignment implements Advanceable,
        CountConvertable, DimenConvertable, Multiplyable, Divideable, Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedDimen(final String name) {
        super(name);
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source the source for new tokens
     *
     * @return the key for the current register
     *
     * @throws GeneralException in case that a derived class need to throw an
     *             Exception this one is declared.
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName();
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

        Dimen d = new Dimen(context, source);
        d.add(context.getDimen(key));
        context.setDimen(key, d, prefix.isGlobal());

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
        final TokenSource source, final Typesetter typesetter)
        throws GeneralException {

        String key = getKey(source);
        source.scanOptionalEquals();

        Dimen dimen = new Dimen(context, source);
        context.setDimen(key, dimen, prefix.isGlobal());
    }

    /**
     * ...
     *
     * @param context the interpreter context
     * @param value ...
     *
     * @throws GeneralException in case of an error
     */
    public void set(final Context context, final String value)
             throws GeneralException {
                 //TODO
//        context.setDimen(getName(),
//                         (value.equals("") ? 0 : ...));
    }

    /**
     * @see de.dante.extex.interpreter.CountConvertable#convertCount(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertCount(final Context context, final TokenSource source)
            throws GeneralException {

        String key = getKey(source);
        Dimen d = context.getDimen(key);
        return (d != null ? d.getValue() : 0);
    }

    /**
     * @see de.dante.extex.interpreter.DimenConvertable#convertDimen(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertDimen(final Context context, final TokenSource source)
            throws GeneralException {

        return convertCount(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.Multiplyable#multiply(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public void multiply(final Flags prefix, final Context context,
            final TokenSource source) throws GeneralException {

        String key = getKey(source);
        source.scanKeyword("by");
        long value = Count.scanCount(context, source);
        Dimen d = new Dimen(context.getDimen(key).getValue() * value);
        context.setDimen(key, d, prefix.isGlobal());

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.Divideable#divide(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context,
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

        Dimen d = new Dimen(context.getDimen(key).getValue() / value);
        context.setDimen(key, d, prefix.isGlobal());

        prefix.clear();
    }
    /**
     * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        String key = getKey(source);
        return context.getDimen(key).toToks(context.getTokenFactory());
    }

}
