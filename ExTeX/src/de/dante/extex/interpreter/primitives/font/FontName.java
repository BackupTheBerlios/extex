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

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\fontname</code>.
 * <p>
 * Example:
 * <pre>
 * \fontname\myfont
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class FontName extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public FontName(final String name) {

        super(name);
    }

    /**
     * Get the next <code>ControlSequenceToken</code> with the <code>FontCode</code>
     * and put the fontname on the stack.
     *
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token tok = source.getNonSpace();

        if (tok == null || !(tok instanceof ControlSequenceToken)) {
            throw new GeneralException("no fontprimitive found!"); // TODO i18n
        }
        Code code = context.getMacro(tok.getValue());
        if (code == null || !(code instanceof FontCode)) {
            throw new GeneralException("no fontprimitive found!"); // TODO i18n
        }
        source.push(new Tokens(context, ((FontCode) code).getFontname()));

        prefix.clear();
    }
}
