/*
 * Copyright (C) 2004  Michael Niedermair
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
package de.dante.extex.interpreter.primitives.info;

import java.text.DecimalFormat;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.RealConvertable;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Real;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\printformat</code>.
 * It format the next primitive for the output with the given pattern and the default <code>Locale</code>.
 * 
 * <p>
 * Example:
 * <pre>
 * \the\printformat{pattern}\real7
 * </pre>
 *
 * @see java.text.DecimalFormat
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class PrintFormat extends AbstractCode implements Theable {

	/**
	 * Creates a new object.
	 *
	 * @param name the name for tracing and debugging
	 */
	public PrintFormat(String name) {
		super(name);
	}

	/**
	 * Get the next token (not expand) and print it with some format.
	 * 
	 * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
	 */
	public void execute(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
		source.push(the(context, source));
		prefix.clear();
	}

	/**
	 * @see de.dante.extex.interpreter.Theable#the(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
	 */
	public Tokens the(Context context, TokenSource source) throws GeneralException {
		// \the\printformat{pattern}\real7

		String pattern = source.scanTokensAsString();

		if (pattern == null || pattern.trim().length() == 0) {
			pattern = "0.00";
		}

		Token cs = source.getToken();

		if (!(cs instanceof ControlSequenceToken)) {
			char esc = (char) (context.getCount("escapechar").getValue());
			// TODO change char to UnicodeChar
			throw new GeneralHelpingException("TTP.CantUseAfter", cs.toString(), Character.toString(esc) + getName());
		}

		Code code = context.getMacro(cs.getValue());

		if (code == null) {
			throw new GeneralHelpingException("TTP.UndefinedToken", cs.toString());
		} else if (code instanceof RealConvertable) {
			Real val = ((RealConvertable) code).convertReal(context, source);
			DecimalFormat form = new DecimalFormat(pattern);
			Tokens toks = new Tokens(context, form.format(val.getValue()));
			return toks;
		} else {
			char esc = (char) (context.getCount("escapechar").getValue());
			throw new GeneralHelpingException("TTP.CantUseAfter", cs.toString(), Character.toString(esc) + getName());
		}
	}
}
