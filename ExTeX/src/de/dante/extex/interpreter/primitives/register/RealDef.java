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
package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\realdef</code>.
 * 
 * <p>
 * Example
 * </p>
 * 
 * <pre>
 *  \realdef\hugo=7
 * </pre>
 * 
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class RealDef extends AbstractCode {

	/**
	 * Creates a new object.
	 * 
	 * @param name the name for debugging
	 */
	public RealDef(final String name) {
		super(name);
	}

	/**
	 * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
	 *      de.dante.extex.interpreter.context.Context,
	 *      de.dante.extex.interpreter.TokenSource,
	 *      de.dante.extex.typesetter.Typesetter)
	 */
	public void execute(final Flags prefix, final Context context, final TokenSource source, Typesetter typesetter) throws GeneralException {
		//  \realdef\hugo=7
		Token tok = source.scanNonSpace();
		if (!(tok instanceof ControlSequenceToken)) {
			throw new GeneralHelpingException("TTP.MissingCtrlSeq");
		}
		source.scanOptionalEquals();
		long value = new Count(context, source).getValue();

		// create new primitive
		Code code = context.getMacro(tok.getValue());
		if (code != null) {
			throw new GeneralHelpingException("TTP.AlreadyDefinedToken", code.getName());
		}
		code = new DefinedReal(tok.getValue(), "real", value);
		context.setMacro(tok.getValue(), code);
		prefix.clear();
		doAfterAssignment(context, source);
	}
}
