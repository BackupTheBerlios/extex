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
import de.dante.util.GeneralException;

/**
 * This class implements the dimen value.
 *
 * <table border="1">
 * 	<tr><td>1 pt     </td><td>=</td><td>65536 sp</td></tr>
 * 	<tr><td>1 pc     </td><td>=</td><td>12 pt   </td></tr>
 *  <tr><td>1 in     </td><td>=</td><td>72,27 pt</td></tr>
 *  <tr><td>72 bp    </td><td>=</td><td>1 in    </td></tr>
 *  <tr><td>2,54 cm  </td><td>=</td><td>1 in    </td></tr>
 *  <tr><td>10 mm    </td><td>=</td><td>1 cm    </td></tr>
 *  <tr><td>1157 dd  </td><td>=</td><td>1238 pt </td></tr>
 *  <tr><td>1 cc     </td><td>=</td><td>12 dd   </td></tr>
 *  <tr><td>65536 sp </td><td>=</td><td>1 pt    </td></tr> 
 * </table>

 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.14 $
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
	 * Creates a new object.
	 * The length stored in it is initialized to 0pt.
	 */
	public Dimen() {
		super();
	}

	/**
	 * Creates a new object.
	 * 
	 * @param value the new dimenvalue
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
	 * Scan the <code>TokenSource</code> and create a new <code>Dimen</code>.
	 *
	 * @param source	the tokensource
	 * @param context	the context
	 * @throws GeneralException in case of an error
	 */
	public Dimen(final Context context, final TokenSource source) throws GeneralException {
		super(source, context, false);
	}

	/**
	 * Creates a new object.
	 * Scan the <code>String</code> and create a new <code>Dimen</code>.
	 *
	 * @param s			the String 
	 * @param context	the context
	 * @throws GeneralException in case of an error
	 */
	public Dimen(final Context context, final String s) throws GeneralException {
		super(s, context, false);
	}
	
	
	/**
	 * Creates a new object with a width with a possibly higher order.
	 *
	 * @param value the fixed width or the factor
	 * @param order the order
	 */
	public Dimen(final long value, final int order) {
		super(value, order);
	}

	/**
	 * @see de.dante.extex.interpreter.type.GlueComponent#set(de.dante.extex.interpreter.context.Context,
	 *      de.dante.extex.interpreter.TokenSource)
	 */
	public void set(final Context context, final TokenSource source) throws GeneralException {
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
	 * Return <code>true</code>, if the internal value is
	 * less than the dimenvalue.
	 * 
	 * @param d dimenvalue to compare
	 */
	public boolean lt(final Dimen d) {
		return (getValue() < d.getValue());
	}

	/**
	 * Return <code>true</code>, if the internal value is
	 * less than or equals the dimenvalue.
	 * 
	 * @param d dimenvalue to compare
	 */
	public boolean le(final Dimen d) {
		return (getValue() <= d.getValue());
	}
	
	/**
	 * Return the <code>Dimen</code>-value in bp
	 * @return	the value in bp
	 */
	public double toBP() {
		return ((double) getValue() * 7200) / (7227 << 16);
	}
	
}
