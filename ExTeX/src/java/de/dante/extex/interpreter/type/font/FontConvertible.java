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
package de.dante.extex.interpreter.type.font;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.util.GeneralException;

/**
 * This is an interface which describes the feature to be convertibe into a
 * font.
 *
 * <doc type="syntax" name="font">
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description is the following:
 *  <pre class="syntax">
 *    &lang;font&rang;
 *        &rarr;  &lang;loaded font&rang;
 *         |   <tt>\textfont</tt> {@linkplain
 *             de.dante.extex.interpreter.TokenSource#scanNumber()
 *             &lang;8-bit&nbsp;number&rang;}
 *         |   <tt>\scriptfont</tt> {@linkplain
 *             de.dante.extex.interpreter.TokenSource#scanNumber()
 *             &lang;8-bit&nbsp;number&rang;}
 *         |   <tt>\scriptscriptfont</tt> {@linkplain
 *             de.dante.extex.interpreter.TokenSource#scanNumber()
 *             &lang;8-bit&nbsp;number&rang;}
 *         |   <tt>\font</tt>
 *  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface FontConvertible {

    /**
     * Convert some primitive value into a font.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the converted value
     *
     * @throws GeneralException in case of an error
     */
    Font convertFont(Context context, TokenSource source)
            throws GeneralException;

}
