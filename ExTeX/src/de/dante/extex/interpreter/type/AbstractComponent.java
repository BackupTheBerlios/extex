/*
 * Copyright (C) 2004 Michael Niedermair
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

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.CountConvertable;
import de.dante.extex.interpreter.DimenConvertable;
import de.dante.extex.interpreter.RealConvertable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * Abstract class for all components e.g. <code>Count</code>, <code>Dimen</code>, ...
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractComponent implements Serializable {

	/**
	 * The constant <tt>ONE</tt> contains the internal representation for 1pt.
	 * @see "TeX -- The Program [101]"
	 */
	public static final long ONE = 1 << 16;

	/**
	 * Creates a new object.
	 */
	public AbstractComponent() {
		super();
	}

	/**
	 * Scan the input stream for tokens making up an integer, this is a number
	 * optionally preceeded by a sign (+ or -). The number can be preceeded by
	 * optional whitespace. Whitespace is also ignored between the sign and the
	 * number. All non-whitespace characters must have the catcode OTHER.
	 *
	 * @return the value of the integer scanned
	 *
	 * @throws GeneralException in case that no number is found or the end of
	 *             file has been reached before an integer could be acquired
	 */
	public long scanCountAsLong(Context context, TokenSource source) throws GeneralException {
		Token t = source.scanNonSpace();
		if (t == null) {
			throw new GeneralHelpingException("TTP.MissingNumber");
		} else if (t.equals(Catcode.OTHER, "-")) {
			return -source.scanNumber();
		} else if (t.equals(Catcode.OTHER, "+")) {
			return source.scanNumber();
		} else if (t instanceof ControlSequenceToken){
			Code code = context.getMacro(t.getValue());
			if (code != null && code instanceof CountConvertable) {
				return ((CountConvertable)code).convertCount(context, source);
			}
		}
		source.push(t);
		return source.scanNumber();
	}

	/**
	 * Scan the input stream for tokens making up an <code>Count</code>.
	 *
	 * @return the value of the integer scanned as <code>Count</code>.
	 *
	 * @throws GeneralException in case that no number is found or the end of
	 *             file has been reached before an integer could be acquired
	 */
	public Count scanCount(Context context, TokenSource source) throws GeneralException {
		return new Count(scanCountAsLong(context, source));
	}

	/**
	 * Scan the input stream for tokens making up an float, this is a number
	 * optionally preceeded by a sign (+ or -), followed by a dot (. or ,) and a number. 
	 * The number can be preceeded by
	 * optional whitespace. Whitespace is also ignored between the sign and the
	 * number. All non-whitespace characters must have the catcode OTHER.
	 * 
	 * @return the value of the float scanned as long.
	 *
	 * @throws GeneralException in case that no number is found or the end of
	 *             file has been reached before an integer could be acquired
	 */
	public long scanFloatAsLong(TokenSource source) throws GeneralException {
		long value = 0;
		boolean neg = false;
		int post = 0;
		Token t = source.scanNonSpace();

		if (t == null) {
			throw new GeneralHelpingException("TTP.EOFinNumber");
		} else if (t.equals(Catcode.OTHER, "-")) {
			neg = true;
			t = source.scanNonSpace();
		} else if (t.equals(Catcode.OTHER, "+")) {
			t = source.scanNonSpace();
		}

		if (t != null && !t.equals(Catcode.OTHER, ".") && !t.equals(Catcode.OTHER, ",")) {
			value = source.scanNumber(t);
			t = source.getToken();
		}

		if (t != null && (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
			// @see "TeX -- The Program [102]"
			int[] dig = new int[17];
			int k = 0;

			for (t = source.getToken(); t != null && t.isa(Catcode.OTHER) && t.getValue().matches("[0-9]"); t = source.scanToken()) {
				if (k < 17) {
					dig[k++] = t.getValue().charAt(0) - '0';
				}
			}

			if (k < 17) {
				k = 17;
			}

			post = 0;

			while (k-- > 0) {
				post = (post + dig[k] * (1 << 17)) / 10;
			}

			post = (post + 1) / 2;
		}

		source.push(t);

		value = value << 16 | post;

		return (neg ? -value : value);
	}

	/**
	 * Scan the String for making up an float.
	 * 
	 * @return the value of the float scanned as long.
	 * @throws GeneralException in case that no number is found
	 */
	public long scanFloatAsLong(final String s) throws GeneralException {
		String svalue = s.trim().replaceAll("," , ".").replaceAll(" ", "");
		long value=0;
		try {
			value = (long)Double.parseDouble(svalue)*ONE;
		} catch (NumberFormatException e) {
			throw new GeneralHelpingException("TTP.NumberFormatError",svalue);
		}
		return value;
	}
	
	/**
	 * Scan the input stream for tokens making up an <code>Dimen</code>.
	 * 
	 * @return the <code>Dimen</code>.
	 *
	 * @throws GeneralException in case that no number is found or the end of
	 *             file has been reached before an integer could be acquired
	 */
	public Dimen scanDimen(final TokenSource source, final Context context, final boolean fixed) throws GeneralException {
		long value = scanFloatAsLong(source);
		int order = 0;

		Token t = source.scanNonSpace();
		if (t == null) {
			throw new GeneralHelpingException("TTP.GlueUnitnotFound");
		}	else if (t instanceof CountConvertable) {// TODO incomplete, see getCount
			return new Dimen(((DimenConvertable) t).convertDimen(context, source));
		}
		source.push(t);

		long mag = 1000;

		if (source.scanKeyword("true")) { // cf. TTP[453], TTP[457]
			mag = context.getMagnification();
			source.push(source.scanNonSpace());
		}

		// cf. TTP[458]
		if (source.scanKeyword("sp")) {
			value = value / ONE;
		} else if (source.scanKeyword("pt")) {
			// nothing to do
		} else if (source.scanKeyword("mm")) {
			value = value * 7227 / 2540;
		} else if (source.scanKeyword("cm")) {
			value = value * 7227 / 254;
		} else if (source.scanKeyword("in")) {
			value = value * 7227 / 100;
		} else if (source.scanKeyword("pc")) {
			value = value * 12;
		} else if (source.scanKeyword("bp")) {
			value = value * 7227 / 7200;
		} else if (source.scanKeyword("dd")) {
			value = value * 1238 / 1157;
		} else if (source.scanKeyword("cc")) {
			value = value * 14856 / 1157;
		} else if (source.scanKeyword("ex")) {
			//TODO ex unimplemented
		} else if (source.scanKeyword("em")) {
			//TODO em unimplemented
		} else if (fixed && source.scanKeyword("fil")) {
			order = 1;
			for (t = source.getToken();(t != null && (t.equals('l') || t.equals('L'))); t = source.getToken()) {
				order++;
			}
			source.push(t);
		} else { // cf. TTP [459]
			throw new GeneralHelpingException("TTP.IllegalUnit");
		}

		if (mag != 1000) {
			value = value * mag / 1000;
		}
		return new Dimen(value, order);
	}

	/**
	 * Scan the String for making up an <code>Dimen</code>.
	 * @return the <code>Dimen</code>.
	 * @throws GeneralException in case that no number is found
	 */
	public Dimen scanDimen(Context context, final String s, final boolean fixed) throws GeneralException {
		
		StringBuffer sb = new StringBuffer();
		int idx=0;
		while (idx <s.length()) {
			char c = s.charAt(idx);
			if (c=='-' || c =='+' || Character.isDigit(c) || c == ' ' || c =='.' || c==',') {
				sb.append(c);
				idx++;
			} else {
				break;
			}
		}
		long value = scanFloatAsLong(sb.toString());
		int order = 0;
		String unit = s.substring(idx).trim();
		
		if (unit.length() == 0) {
			throw new GeneralHelpingException("TTP.GlueUnitnotFound");
		}

		long mag = 1000;

		if (unit.startsWith("true")) { 
			mag = context.getMagnification();
			unit = unit.substring(4).trim();
		}

		if (unit.startsWith("sp")) {
			value = value / ONE;
		} else if (unit.startsWith("pt")) {
			// nothing to do
		} else if (unit.startsWith("mm")) {
			value = value * 7227 / 2540;
		} else if (unit.startsWith("cm")) {
			value = value * 7227 / 254;
		} else if (unit.startsWith("in")) {
			value = value * 7227 / 100;
		} else if (unit.startsWith("pc")) {
			value = value * 12;
		} else if (unit.startsWith("bp")) {
			value = value * 7227 / 7200;
		} else if (unit.startsWith("dd")) {
			value = value * 1238 / 1157;
		} else if (unit.startsWith("cc")) {
			value = value * 14856 / 1157;
		} else if (unit.startsWith("ex")) {
			//TODO ex unimplemented
		} else if (unit.startsWith("em")) {
			//TODO em unimplemented
		} else if (fixed && unit.startsWith("fil")) { // fil, fill, filll, ...
			order = 1;
			for (int i = 3; i < unit.length(); i++) {
				if (unit.charAt(i)== 'l' || unit.charAt(i)== 'L') {
					order++;
				}
			}
		} else { // cf. TTP [459]
			throw new GeneralHelpingException("TTP.IllegalUnit");
		}

		if (mag != 1000) {
			value = value * mag / 1000;
		}
		return new Dimen(value, order);
	}
	
	
	/** 
	 * Rounds a floating-point number to nearest whole number.
	 * It uses exactly the same algorithm as web2c implementation of TeX.
	 * @param	d	number to be rounded
	 * @return	rounded value
	 */
	protected long round(double d) {
		return (long) ((d >= 0.0) ? d + 0.5 : d - 0.5);
	}

	/**
	 * Scan the input stream for tokens making up a <code>Glue</code>.
	 * 
	 * @return the <code>Glue</code>.
	 *
	 * @throws GeneralException in case that no number is found or the end of
	 *             file has been reached before an integer could be acquired
	 */
	public Glue scanGlue(TokenSource source, Context context) throws GeneralException {
		Dimen length = new Dimen(context, source);
		GlueComponent stretch = Dimen.ZERO_PT;
		GlueComponent shrink = Dimen.ZERO_PT;

		if (source.scanKeyword("plus")) {
			stretch = new GlueComponent(source, context, true);
		}
		if (source.scanKeyword("minus")) {
			shrink = new GlueComponent(source, context, true);
		}
		return new Glue(length, stretch, shrink);
	}

	/**
	 * Scan the input stream for tokens making up a <code>Real</code>.
	 * @return	the <code>Real</code>-value
	 * @throws GeneralException, in case of an error
	 */
	public Real scanReal(Context context,TokenSource source) throws GeneralException {
		StringBuffer sb = new StringBuffer(32);
		long value = 0;
		boolean neg = false;
		Token t = source.scanNonSpace();

		if (t == null) {
			throw new GeneralHelpingException("TTP.MissingNumber");
		} else if (t instanceof RealConvertable) {// TODO incomplete, see getCount
			return ((RealConvertable) t).convertReal(context, source);
		} else if (t.equals(Catcode.OTHER, "-")) {
			neg = true;
			t = source.scanNonSpace();
		} else if (t.equals(Catcode.OTHER, "+")) {
			t = source.scanNonSpace();
		}

		if (neg) {
			sb.append('-');
		}

		if (t != null && !t.equals(Catcode.OTHER, ".")
				&& !t.equals(Catcode.OTHER, ",")) {
			value = source.scanNumber(t);
			t = source.getToken();
		}

		sb.append(Long.toString(value));
		sb.append('.');
		value = 0;

		if (t != null
				&& (t.equals(Catcode.OTHER, ".") || t.equals(Catcode.OTHER, ","))) {
			value = source.scanNumber();
		}
		sb.append(Long.toString(value));

		return new Real(sb.toString());
	}
}
