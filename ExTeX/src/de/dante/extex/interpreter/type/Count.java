/*
 * Copyright (C) 2003 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter.type;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;

import de.dante.util.GeneralException;

import java.io.Serializable;

/**
 * The <code>Count</code>-component.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */
public class Count extends AbstractComponent implements Serializable {

	/**
	 * ZERO
	 */
	public static final Count ZERO = new Count(0);

	/** 
	  * the value
	  */
	private long value = 0;

	/**
	 * Creates a new object.
	 */
	public Count(long value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new object.
	 * Scant the tokensource and create a new <code>Count</code>     
	 */
	public Count(final Context context, final TokenSource source) throws GeneralException {
		super();
		value = scanCountAsLong(context, source);
	}

	/**
	 * Setter for the value.
	 *
	 * @param l the new value
	 */
	public void setValue(long l) {
		value = l;
	}

	/**
	 * Getter for the value
	 *
	 * @return the value
	 */
	public long getValue() {
		return value;
	}

	/**
	 * Add a value to the count
	 * @param val 	the value to add
	 * @return	the new value as <code>Count</code> (copy) 
	 */
	public Count add(long val) {
		value += val;
		return new Count(value);
	}

	/**
	 * Devide a value to the count
	 * @param val 	the value to divide
	 * @return	the new value as <code>Count</code> (copy) 
	 * @throws GeneralHelpingException in case of a division by zero
	 */
	public Count divide(long val) throws GeneralException {
		if (val == 0) {
			throw new GeneralHelpingException("TTP.ArithOverflow");
		}

		value /= val;
		return new Count(value);
	}

	/**
	 * Multiply a value to the count
	 * @param val 	the value to multiply
	 * @return	the new value as <code>Count</code> (copy) 
	 */
	public Count multiply(long val) {
		value *= val;
		return new Count(value);
	}

	/**
	 * Return the value as <code>String</code>
	 *
	 * @return the value as <code>String</code>
	 */
	public String toString() {
		return Long.toString(value);
	}
}
