/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.pl;

import java.io.IOException;

import de.dante.extex.font.exception.FontException;

/**
 * Interface for the PL-Format.
 *
 * <p>
 * Convert a Font (e.g. tfm) to a PL-file (property-list).
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public interface PlFormat {

    /**
     * Print all information (jn PL-format) to the writer.
     *
     * @param out  the plwriter
     * @throws IOException if an IO-error occurs.
     * @throws FontException if a font-error occurs.
     */
    void toPL(PlWriter out) throws IOException, FontException;
}
