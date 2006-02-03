/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.interpreter.type.font;

import java.util.logging.Logger;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * Font utility methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class FontUtil {

    /**
     * Create a new Object
     */
    private FontUtil() {

    }

    /**
     * The constant <tt>LOCALIZER</tt> contains the localizer for this utility
     * class.
     */
    private static final Localizer LOCALIZER = LocalizerFactory
            .getLocalizer(FontUtil.class.getName());

    /**
     * This method produces a log entry for lost characters if the count
     * register <tt>tracinglostchars</tt> is greater than zero.
     *
     * @param logger the logger to write to
     * @param context the interpreter context
     * @param font the font queried
     * @param c the character not found
     *
     * @see "TTP [581]"
     */
    public static void charWarning(final Logger logger, final Context context,
            final Font font, final UnicodeChar c) {

        if (context.getCount("tracinglostchars").gt(Count.ZERO)) {
            logger.info(LOCALIZER.format("TTP.MissingChar", c.toString(), font
                    .getFontName()));
        }
    }
}
