/*
 * Copyright (C) 2004 Michael Niedermair, Gerd Neugebauer
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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\toks</code>.
 * It sets the named toks register to the value given, and as a side effect all
 * prefixes are zeroed.
 *
 * Example
 *
 * <pre>
 *  \encoding{UTF-8}
 * </pre>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class NamedToks extends AbstractAssignment implements Theable {

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public NamedToks(final String name) {
        super(name);
    }

    /**
     * Return the register value as <code>Tokens</code> for <code>\the</code>.
     *
     * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        return context.getToks(getKey(source));
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
        Tokens toks = source.getTokens();
        context.setToks(getKey(source), toks, prefix.isGlobal());
    }

    /**
     * Set the value for the register...
     * 
     * @param context the interpreter context
     * @param value the value for the tokens
     */
    public void set(final Context context, final String value)
            throws GeneralException {

        context.setToks(getKey(null), new Tokens(context, value)); //TODO
    }

    /**
     * Expand
     * <p>
     * Scan the tokens between <code>{</code> and <code>}</code> and store
     * it.
     * 
     * @param prefix the prefix flags
     * @param context the interpreter context
     * @param source the tokensource
     * @param key the key
     * 
     * @throws GeneralException ...
     */
    protected void expand(final Flags prefix, final Context context,
        final TokenSource source, final String key) throws GeneralException {
        Tokens toks = source.getTokens();
        context.setToks(key, toks, prefix.isGlobal());
        prefix.clear();
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
