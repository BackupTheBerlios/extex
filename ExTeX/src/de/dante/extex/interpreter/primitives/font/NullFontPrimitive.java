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

import de.dante.extex.font.NullFont;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.FontConvertable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Font;
import de.dante.util.GeneralException;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class NullFontPrimitive extends AbstractCode
        implements FontConvertable {

    /**
     * The field <tt>theFont</tt> contains the ...
     */
    private Font theFont = new NullFont();

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive
     */
    public NullFontPrimitive(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.extex.interpreter.FontConvertable#convertFont(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Font convertFont(final Context context, final TokenSource source)
            throws GeneralException {

        return theFont;
    }

}
