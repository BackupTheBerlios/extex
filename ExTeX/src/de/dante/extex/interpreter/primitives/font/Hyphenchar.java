/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\hyphenchar</code>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Hyphenchar extends AbstractCode implements ExpandableCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hyphenchar(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Font font = source.getFont();
        source.getOptionalEquals();
        UnicodeChar c = source.scanCharacterCode();
        font.setHyphenChar(c);
        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Font font = source.getFont();
        Token t = context.getTokenFactory().newInstance(Catcode.OTHER,
                                                        font.getHyphenChar());
        source.push(t);
    }
}