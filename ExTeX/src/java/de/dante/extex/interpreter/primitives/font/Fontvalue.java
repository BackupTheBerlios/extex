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

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Theable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\fontvalue</code>.
 *
 *
 * <p>
 * Example:
 * <pre>
 * \fontvalue\ff{key}=5pt
 * \the\fontvalue\ff{key}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.3 $
 */
public class Fontvalue extends AbstractAssignment implements Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Fontvalue(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        // \fontvalue\ff{key}=5pt
        source.skipSpace();
        Font font = source.getFont();
        String key = source.scanTokensAsString();
        if (key == null || key.trim().length() == 0) {
            throw new GeneralHelpingException("FONT.fontkeynotfound");
        }

        source.getOptionalEquals();
        Dimen size = new Dimen(context, source);
        font.setFontDimen(key, size);
        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        source.skipSpace();
        Font font = source.getFont();
        String key = source.scanTokensAsString();
        if (key == null || key.trim().length() == 0) {
            throw new GeneralHelpingException("FONT.fontkeynotfound");
        }
        Dimen size = font.getFontDimen(key);

        return size.toToks(context.getTokenFactory());
    }

}
