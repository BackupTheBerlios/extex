/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Advanceable;
import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.Divideable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Multiplyable;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Token;
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
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class NamedCount extends AbstractCode implements 
    Advanceable, Multiplyable, Divideable, Theable, CountConvertable {

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public NamedCount(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Advanceable#advance(int,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void advance(Flags prefix, Context context, TokenSource source)
        throws GeneralException {
        String key = getKey(source);
        source.scanKeyword("by");

        long value = scanCount(context, source);

        Count count = context.getCount(key);
        count.add(value);

        if (prefix.isGlobal()) {
            value = count.getValue();
            context.setCount(key, value, true);
        }
    }

    /**
     * ...
     * 
     * @param context ...
     * @param source ...
     * 
     * @return ...
     * 
     * @throws GeneralException ...
     */
    public long convertCount(Context context, TokenSource source)
        throws GeneralException {
        String key = getKey(source);

        return context.getCount(key).getValue();
    }

    /**
     * @see de.dante.extex.interpreter.Divideable#divide(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void divide(Flags prefix, Context context, TokenSource source)
        throws GeneralException {
        String key = getKey(source);
        source.scanKeyword("by");

        long value = scanCount(context, source);

        if (value == 0) {
            throw new GeneralHelpingException("TTP.ArithOverflow");
        }

        Count count = context.getCount(key);
        count.divide(value);

        if (prefix.isGlobal()) {
            value = count.getValue();
            context.setCount(key, value, true);
        }
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(Flags prefix, Context context, TokenSource source,
        Typesetter typesetter) throws GeneralException {
        String key = getKey(source);
        source.scanOptionalEquals();

        long value = scanCount(context, source);
        context.setCount(key, value, prefix.isGlobal());
        prefix.clear();
        doAfterAssignment(context, source);
    }
    
    /**
     * @see de.dante.extex.interpreter.Multiplyable#multiply(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void multiply(Flags prefix, Context context, TokenSource source)
        throws GeneralException {
        String key = getKey(source);
        source.scanKeyword("by");

        long value = scanCount(context, source);

        Count count = context.getCount(key);
        count.setValue(count.getValue() * value);

        if (prefix.isGlobal()) {
            value = count.getValue();
            context.setCount(key, value, true);
        }
    }

    /**
     * ...
     * 
     * @param context the interpreter context
     * @param value ...
     */
    public void set(Context context, long value) {
        context.setCount(getName(), value);
    }

    /**
     * ...
     * 
     * @param context the interpreter context
     * @param value ...
     */
    public void set(Context context, String value) throws GeneralException {
        context.setCount(getName(), (value.equals("") ? 0 : Long
            .parseLong(value)));
    }

    /**
     * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(Context context, TokenSource source)
        throws GeneralException {
        String key = getKey(source);
        String s = context.getCount(key).toString();
        Tokens toks = new Tokens(context, s);
        return toks;
    }

    /**
     * Return the key (the name of the primitive) for the register.
     * 
     * @param source ...
     * 
     * @return ...
     */
    protected String getKey(TokenSource source) throws GeneralException {
        return getName();
    }

    /**
     * ...
     * 
     * @param context ...
     * @param source ...
     * 
     * @return ...
     * 
     * @throws GeneralException ...
     */
    private long scanCount(Context context, TokenSource source)
        throws GeneralException {
        Token t = source.getNonSpace();

        if (t == null) {
            // TODO
            return 0;
        } else if (t instanceof CountConvertable) {
            return ((CountConvertable) t).convertCount(context, source);
        } else {
            source.push(t);

            //TODO
        }

        return source.scanInteger();
    }
}
