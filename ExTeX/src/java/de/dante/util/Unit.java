/*
 * Copyright (C) 2004 The ExTeX Group
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
 */
package de.dante.util;

import de.dante.extex.interpreter.type.Dimen;

/**
 * This class implements a converter e.g for dimen values.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class Unit {

	/**
	 * Return the <code>Dimen</code>-value as BP (big point)
	 * @param value		the <code>Dimen</code>
	 * @return the <code>Dimen</code>-value as BP
	 */
	public static double getDimenAsBP(final Dimen value) {
		return ((double) value.getValue() * 7200) / (7227 << 16);
	}

	/**
	 * Return the <code>Dimen</code>-value as PT (point)
	 * @param value		the <code>Dimen</code>
	 * @return the <code>Dimen</code>-value as PT
	 */
	public static double getDimenAsPT(final Dimen value) {
		return ((double) value.getValue()) / Dimen.ONE;
	}

	/**
	 * Return the <code>Dimen</code>-value as MM (milimeter)
	 * @param value		the <code>Dimen</code>
	 * @return the <code>Dimen</code>-value as MM
	 */
	public static double getDimenAsMM(final Dimen value) {
		return ((double) value.getValue() * 2540) / (7227 << 16);
	}

	/**
	 * Return the <code>Dimen</code>-value as CM (centimeter)
	 * @param value		the <code>Dimen</code>
	 * @return the <code>Dimen</code>-value as CM
	 */
	public static double getDimenAsCM(final Dimen value) {
		return ((double) value.getValue() * 254) / (7227 << 16);
	}

	/**
	 * Return the <code>Dimen</code>-value as IN (inch)
	 * @param value		the <code>Dimen</code>
	 * @return the <code>Dimen</code>-value as IN
	 */
	public static double getDimenAsIN(final Dimen value) {
		return ((double) value.getValue() * 100) / (7227 << 16);
	}

	/**
	 * Round the double-value to a number of decimals.
	 * @param value		the double-value
	 * @param round		the number of decimals to round (not round: negative value) 
	 * @return the rounded double-value
	 */
	public static double round(final double value, final int round) {
		if (round < 0) {
			return value;
		} else {
			return Math.round(value * Math.pow(10, round)) / Math.pow(10, round);
		}
	}

	// TODO kill after test
	public static void main(String[] args) {
		Dimen test = new Dimen(Dimen.ONE);
		Dimen test2 = new Dimen(Dimen.ONE + 10);

		System.out.println("dimen: 1pt = " + test.toString());
		System.out.println(" = " + getDimenAsBP(test) + "bp");
		System.out.println(" = " + getDimenAsPT(test) + "pt");
		System.out.println("dimen: = " + test2.toString());
		System.out.println(" = " + getDimenAsPT(test2) + "pt");
		System.out.println("-1: = " + round(getDimenAsPT(test2), -1) + "pt");
		System.out.println(" 1: = " + round(getDimenAsPT(test2), 1) + "pt");
		System.out.println(" 2: = " + round(getDimenAsPT(test2), 2) + "pt");
		System.out.println(" 3: = " + round(getDimenAsPT(test2), 3) + "pt");
		System.out.println(" 4: = " + round(getDimenAsPT(test2), 4) + "pt");
		System.out.println(" 5: = " + round(getDimenAsPT(test2), 5) + "pt");
		System.out.println(" 6: = " + round(getDimenAsPT(test2), 6) + "pt");
		System.out.println(" 7: = " + round(getDimenAsPT(test2), 7) + "pt");
		System.out.println(" 8: = " + round(getDimenAsPT(test2), 8) + "pt");
	}
}
