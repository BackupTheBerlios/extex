/*
 * Copyright (C) 2003  Gerd Neugebauer
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

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Advanceable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.TokenFactory;
import de.dante.extex.typesetter.Typesetter;

import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\count</code>.
 * It sets the named count register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * Example
 * <pre>
 * \day=345
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class NamedCount extends AbstractCode implements Advanceable {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedCount(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#getThe(de.dante.extex.interpreter.context.Context)
     * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
     */
    public Tokens getThe(Context context, TokenSource source)
                  throws GeneralException {
        TokenFactory factory = context.getTokenFactory();
        String s             = context.getMacro(getName())
                                      .toString();
        Tokens toks = new Tokens();

        for (int i = 0; i < s.length(); i++) {
            toks.add(factory.newInstance(Catcode.OTHER, s.charAt(i)));
        }

        return toks;
    }

    /**
     * @see de.dante.extex.interpreter.Advanceable#advance(int, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public void advance(Flags prefix, Context context,
                        TokenSource source) throws GeneralException {
        advance(prefix, context, source, getName());
    }

    /**
     * ...
     *
     * @param prefix ...
     * @param context ...
     * @param source ...
     * @param key ...
     *
     * @throws GeneralException ...
     */
    public void advance(Flags prefix, Context context,
                        TokenSource source, String key)
                 throws GeneralException {
        source.scanKeyword("by");

        int val = source.scanInteger();

        Count count = context.getCount(key);
        count.setValue(count.getValue() + val);

        if (prefix.isGlobal()) {
            context.setCount(key, count.getValue() + val, true);
        }
    }

    /**
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context,
                       TokenSource source, Typesetter typesetter)
                throws GeneralException {
        expand(prefix, context, source, getName());
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
    public void set(Context context, String value)
             throws GeneralException {
        context.setCount(getName(),
                         (value.equals("") ? 0 : Long.parseLong(value)));
    }

    /**
     * ...
     *
     * @param prefix ...
     * @param context the interpreter context
     * @param source ...
     * @param key ...
     *
     * @throws GeneralException ...
     */
    protected void expand(Flags prefix, Context context,
                          TokenSource source, String key)
                   throws GeneralException {
        source.scanOptionalEquals();

        long value = source.scanInteger();
        context.setCount(key, value, prefix.isGlobal());
        prefix.clear();
    }
}
