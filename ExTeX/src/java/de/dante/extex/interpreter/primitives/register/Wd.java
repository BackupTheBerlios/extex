/*
 * Copyright (C) 2004 Gerd Neugebauer
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

import java.io.Serializable;

import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.DimenConvertable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Box;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\wd</code>.
 * 
 * Example
 * 
 * <pre>
 *  \advance\dimen12 \wd0
 * </pre>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Wd extends NumberedBox implements Serializable, Theable,
    CountConvertable, DimenConvertable {
    
    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public Wd(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
        final TokenSource source, final Typesetter typesetter)
        throws GeneralException {
        String key = getKey(source);
        source.scanOptionalEquals();
        Dimen d = new Dimen(source, context);
        Box b = context.getBox(key);

        if (b == null) {
            throw new RuntimeException("unimplemented"); //TODO unimplemented
        } else {
            b.setWidth(d);
        }

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean expand(final Flags prefix, final Context context,
        final TokenSource source, final Typesetter typesetter)
        throws GeneralException {
        source.push(the(context, source));
        return true;
    }

    /**
     * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
        throws GeneralException {
        String key = getKey(source);
        Box b = context.getBox(key);
        Dimen d;
        if (b == null) {
            d = Dimen.ZERO_PT;
        } else {
            d = b.getWidth();
        }
        return d.toToks(context.getTokenFactory());
    }

    /**
     * @see de.dante.extex.interpreter.CountConvertable#convertCount(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertCount(final Context context, final TokenSource source)
        throws GeneralException {
        return convertCount(context, source);
    }
    
    /**
     * @see de.dante.extex.interpreter.DimenConvertable#convertDimen(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public long convertDimen(final Context context, final TokenSource source)
        throws GeneralException {
        String key = getKey(source);
        Box b = context.getBox(key);
        long d;
        if (b == null) {
            d = 0;
        } else {
            d = b.getWidth().getValue();
        }
        return d;
    }

}
