/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.color;

import de.dante.extex.interpreter.context.Color;

/**
 * This class provides some utility functions for colors.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class ColorUtil {

    /**
     * Creates a new object.
     */
    private ColorUtil() {

    }

    /**
     * Format a single color component: the color value from 0 to 0xffff
     * is translated to a representation in the range 0.0 to 1.0.
     *
     * @param sb the target string buffer
     * @param cc the color component
     */
    public static void formatComponent(final StringBuffer sb, final long cc) {

        sb.append((float) cc / Color.MAX_VALUE);
    }

    /**
     * Format the alpha value. If it is the default value then nothing is added
     * to the target string buffer. Otherwise the value in the range from 0.0
     * to 1.0 preceded by <tt>alpha</tt> is added.
     *
     * @param sb the target string buffer
     * @param alpha the alpha value
     */
    public static void formatAlpha(final StringBuffer sb, final long alpha) {

        sb.append(alpha == 0 ? "" : "alpha " + (float) alpha / Color.MAX_VALUE
                + " ");
    }

}
