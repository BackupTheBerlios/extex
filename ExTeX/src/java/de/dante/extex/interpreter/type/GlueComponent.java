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
 * This class provides a means to store floating numbers with an order.
 *
 * <p>Examples</p>
 * <pre>
 * 123 pt
 * -123 pt
 * 123.456 pt
 * 123.pt
 * .465 pt
 * -.456pt
 * +456pt
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class GlueComponent extends AbstractComponent implements Serializable {

	/**
	 * The field <tt>order</tt> contains the ...
	 */
	private int order = 0;

	/**
	 * The field <tt>value</tt> contains the integer representatation of the
	 * dimen register.
	 */
	private long value = 0;

	/**
	 * Creates a new object.
	 */
	public GlueComponent() {
		super();
	}

	/**
	 * Creates a new object with a fixed width.
	 *
	 * @param value the fixed value
	 */
	public GlueComponent(final long value) {
		super();
		this.value = value;
	}

	/**
	 * Creates a new object with a width with a possibly higher order.
	 *
	 * @param value the fixed width or the factor
	 * @param order the order
	 */
	public GlueComponent(final long value, final int order) {
		super();
		this.value = value;
		this.order = order;
	}

	/**
	 * Creates a new object from a TokenStream.
	 *
	 * @param source the source for new tokens
	 * @param context the interpreter context
	 *
	 * @throws GeneralException in case of an error
	 */
	public GlueComponent(final TokenSource source, final Context context) throws GeneralException {
		this(source, context, false);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param source the source for the tokens to be read
	 * @param context the interpreter context
	 * @param fixed if <code>true</code> then no glue order is allowed
	 * 
	 * @throws GeneralException in case of an error
	 * @throws GeneralHelpingException ...
	 */
	public GlueComponent(final TokenSource source, final Context context, final boolean fixed) throws GeneralException {
		super();
		set(source, context, fixed);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param s			the String
	 * @param context	the interpreter context
	 * @param fixed if <code>true</code> then no glue order is allowed
	 * @throws GeneralException in case of an error
	 */
	public GlueComponent(final String s, final Context context, final boolean fixed) throws GeneralException {
		super();
		Dimen dv = scanDimen(context, s, fixed);
		value = dv.getValue();
		order = dv.getOrder();
	}

	/**
	 * Getter for the value in scaled points (sp).
	 *
	 * @return the value in internal units of scaled points (sp)
	 */
	public long getValue() {
		return value;
	}

	/**
	 * @return Returns the order.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Setter for the value.
	 *
	 * @param val the new value
	 */
	public void setValue(final long val) {
		value = val;
	}

	/**
	 * Copy the <code>GlueComponent</code>.
	 * @return Return a copy of the <code>GlueComponent</code>
	 */
	public GlueComponent copy() {
		return new GlueComponent(value, order);
	}

	/**
	 * Setter for the value in terms of the internal representation.
	 *
	 * @param l 	the new value
	 */
	public void set(final long l) {
		value = l;
	}

	/**
	 * Setter for the value.
	 *
	 * @param d the new value
	 */
	public void set(final Dimen d) {
		value = d.getValue();
		order = 0;
	}

	/**
	 * Set the value by scanning the tokensource.
	 *
	 * @param source 	the tokensource
	 * @param context 	the context
	 * @throws GeneralException in case of an error
	 */
	public void set(final Context context, final TokenSource source) throws GeneralException {
		set(source, context, true);
	}

	/**
	 * Set the value by scanning the tokensource.
	 * 
	 * @param source 	the tokensource
	 * @param context 	the context
	 * @param fixed 		fixed
	 * @throws GeneralException in case of an error
	 */
	public void set(final TokenSource source, final Context context, final boolean fixed) throws GeneralException {
		Dimen dv = scanDimen(source, context, fixed);
		value = dv.getValue();
		order = dv.getOrder();
	}

	/**
	 * Return a String with the value in pt 
	 * @return a String with the value in pt
	 */
	public String toPT() {
		return String.valueOf(round((double) getValue() / ONE)) + "pt";
	}

	/**
	 * Add the value as <code>String</code> to a <code>StringBuffer</code>.
	 * @param sb	the <code>StringBuffer</code>
	 */
	public void toString(final StringBuffer sb) {
		sb.append(Long.toString(getValue()));
		sb.append("sp");
	}

	/**
	 * Return the value as <code>String</code>
	 * @return the value as <code>String</code>
	 */
	public String toString() {
		return Long.toString(getValue()) + "sp";
	}
}
