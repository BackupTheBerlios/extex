/*
 * Copyright (C) 2004 Michael Niedermair
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

import java.io.Serializable;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.util.GeneralException;

/**
 * Real (with a double value)
 *
 * @author <a href="mailto:m.g.sn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class Real extends AbstractComponent implements Serializable {

	/** 
	 * ZERO-Real
	 */
	public static final Real ZERO = new Real(0);

	/** 
	 * max-Real
	 */
	public static final Real MAX_VALUE = new Real(Double.MAX_VALUE);

	/** 
	 * The value
	 */
	private double value = 0.0d;

	/**
	 * Creates a new object.
	 * 
	 * @param value	init with double-value
	 */
	public Real(double value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new object.
	 * 
	 * Scan the <code>TokenSource</code> for a <code>Real</code>.
	 * 
	 * @param source	the tokensource
	 */
	public Real(Context context, TokenSource source) throws GeneralException {
		super();
		Real r = scanReal(context,source);
		value = r.getValue();
	}

	/**
	 * Creates a new object.
	 */
	public Real(Real val) {
		value = val.getValue();
	}

	/**
	 * Creates a new object.
	 */
	public Real(float val) {
		value = val;
	}

	/**
	 * Creates a new object.
	 */
	public Real(long l) {
		if (l > MAX_VALUE.getLong()) {
			l = MAX_VALUE.getLong();
		}
		value = l;
	}

	/**
	 * Creates a new object.
	 */
	public Real(int i) {
		value = i;
	}

	/**
	 * Creates a new object.<p>
	 * If a error is throws, the value is set to zero
	 */
	public Real(String s) {
		try {
			value = Double.valueOf(s).doubleValue();
		} catch (NumberFormatException e) {
			// set to zero
			value = 0.0d;
		}
	}

	/**
	 * Setter for the value.
	 *
	 * @param d the new value
	 */
	public void setValue(double d) {
		value = d;
	}

	/**
	 * Getter for the value
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * ...
	 *
	 * @param val ...
	 */
	public void add(double val) {
		value += val;
	}

	/**
	 * ...
	 *
	 * @param real ...
	 */
	public void add(Real real) {
		value += real.getValue();
	}

	/**
	 * ...
	 *
	 * @param val ...
	 *
	 * @throws GeneralHelpingException in case of a division by zero
	 */
	public void divide(double val) throws GeneralException {
		if (val == 0.0d) {
			throw new GeneralHelpingException("TTP.ArithOverflow");
		}

		value /= val;
	}

	/**
	 * ...
	 *
	 * @param val ...
	 *
	 * @throws GeneralHelpingException in case of a division by zero
	 */
	public void divide(Real val) throws GeneralException {
		divide(val.getValue());
	}

	/**
	 * ...
	 *
	 * @param val ...
	 */
	public void multiply(double val) {
		value *= val;
	}

	/**
	 * ...
	 *
	 * @param val ...
	 */
	public void multiply(Real val) {
		value *= val.getValue();
	}

	/**
	 * Return the value as long.
	 */
	public long getLong() {
		return (long) value;
	}

	/**
	 * Return the value as <code>String</code>
	 *
	 * @return the value as <code>String</code>
	 */
	public String toString() {
		return Double.toString(value);
	}
}
