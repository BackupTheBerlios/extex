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
package de.dante.extex.interpreter.type;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;

/**
 * This class implements the dimen value.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
 */
public class Dimen extends GlueComponent implements Serializable {

	/**
	 * The constant <tt>ZERO_PT</tt> contains the ...
	 */
	public static final Dimen ZERO_PT = new Dimen(0);

	/**
	 * The constant <tt>ONE_PT</tt> contains the ...
	 */
	public static final Dimen ONE_PT = new Dimen(1 << 16);

	/**
	 * The constant <tt>ONE</tt> contains the internal representation for 1pt.
	 * @see "TeX -- The Program [101]"
	 */
	public static final long ONE = 1 << 16;

	/**
	 * Creates a new object.
	 * The length stored in it is initialized to 0pt.
	 */
	public Dimen() {
		super();
	}

	/**
	 * Creates a new object.
	 * 
	 * @param value ...
	 */
	public Dimen(final long value) {
		super(value);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param value the value as <code>Dimen</code>
	 */
	public Dimen(final Dimen value) {
		super(value.getValue());
	}
	
	
    /**
     * Creates a new object.
     *
     * @param source ...
     * @param context ...
     *
     * @throws GeneralException in case of an error
     */
    public Dimen(final Context context, final TokenSource source)
        throws GeneralException {
        super(source, context, false);
    }

    /**
     * @see de.dante.extex.interpreter.type.GlueComponent#set(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void set(final Context context, final TokenSource source)
        throws GeneralException {
        set(source, context, false);
    }

    /**
     * ...
     * 
     * @param d the Dimen to add to
     */
    public Dimen add(final Dimen d) {
        setValue(getValue() + d.getValue());
        return new Dimen(getValue());
    }

    /**
     * ...
     * 
     * @param d the Dimen to add to
     */
    public Dimen subtract(final Dimen d) {
        setValue(getValue() - d.getValue());
        return new Dimen(getValue());
    }

    /**
     * ...
     * 
     * @param d ...
     */
    public boolean lt(final Dimen d) {
        return (getValue() < d.getValue());
    }

    /**
     * ...
     * 
     * @param d ...
     */
    public boolean le(final Dimen d) {
        return (getValue() <= d.getValue());
    }
    
	/**
	 * ...
	 *
	 * @param factory ...
	 *
	 * @return ...
	 *
	 * @throws GeneralException ...
	 * @see "TeX -- The Program [103]"
	 */
	public Tokens toToks(final TokenFactory factory) throws GeneralException {
		Tokens toks = new Tokens();
		String s    = Long.toString(getValue()/ONE);

		for (int i = 0; i < s.length(); i++) {
			toks.add(factory.newInstance(Catcode.OTHER,
										 s.substring(i, i + 1)));
		}

		//TODO: decimal places and rounding
		toks.add(factory.newInstance(Catcode.LETTER, "p"));
		toks.add(factory.newInstance(Catcode.LETTER, "t"));

		return toks;
	}
}
