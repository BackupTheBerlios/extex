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
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Dimen;
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
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class NamedDimen extends AbstractCode implements Advanceable {

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
     * @param source	the tokensource
     * @return the key for the current register
     * @throws GeneralException in case that a derived class need to throw an Exception this one is declared.
     */
    protected String getKey(final TokenSource source) throws GeneralException {
        return getName();
    }

    /**
     * @see de.dante.extex.interpreter.Advanceable#advance(int,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void advance(final Flags prefix, final Context context,
        final TokenSource source) throws GeneralException {
        String key = getKey(source);
        source.scanKeyword("by",true);
        Dimen add = new Dimen(context,source);
        Dimen dimen = context.getDimen(key);
        add.add(dimen);
        dimen.set(add);
        if (prefix.isGlobal()) {
            context.setDimen(key, add, true);
        }
        prefix.clear();
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
        Dimen dimen = new Dimen(context, source);
        context.setDimen(key, dimen, prefix.isGlobal());
        prefix.clear();
        doAfterAssignment(context, source);
    }

    /**
     * Set the new value (String)
     * @param context 	the interpreter context
     * @param value		the new value
     * @throws GeneralException in case of an error
     */
    public void set(final Context context, final String value)
             throws GeneralException {
    	try {
    		context.setDimen(getName(), (value.equals("") ? 0 : new Dimen(context,value).getValue()));
    	} catch (NumberFormatException e) {
    		throw new GeneralHelpingException("TTP.NumberFormatError", value);
    	}
    }
}
