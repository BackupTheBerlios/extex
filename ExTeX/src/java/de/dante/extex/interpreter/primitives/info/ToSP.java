/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\tosp</code>.
 * It print a Dimen-value in sp.
 *
 * <p>
 * Example:
 * <pre>
 * \the\tosp\dimen7
 * \tosp\dimen8
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class ToSP extends AbstractCode implements Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public ToSP(final String name) {

        super(name);
    }

    /**
     * Get the next token (not expand) and if it a <code>Dimen</code>,  then print it in pt.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        source.push(the(context, source, typesetter));
        return true;
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Token cs = source.getToken();

        if (!(cs instanceof ControlSequenceToken)) {
            char esc = (char) (context.getCount("escapechar").getValue());
            // TODO change char to UnicodeChar
            throw new HelpingException("TTP.CantUseAfter", cs.toString(),
                    Character.toString(esc) + getName());
        }

        Code code = context.getCode(cs);

        if (code == null) {
            throw new HelpingException("TTP.UndefinedToken", cs.toString());
        } else if (code instanceof DimenConvertible) {
            Dimen val = new Dimen(((DimenConvertible) code).convertDimen(
                    context, source, typesetter));
            Tokens toks = new Tokens(context, val.toString());
            return toks;
        } else {
            char esc = (char) (context.getCount("escapechar").getValue());
            throw new HelpingException("TTP.CantUseAfter", cs.toString(),
                    Character.toString(esc) + getName());
        }
    }
}