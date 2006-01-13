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
 */

package de.dante.extex.unicodeFont.format.tex.psfontmap.enc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.exception.FontEncodingIOException;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.exception.FontEncodingWrongRangeException;

/**
 * Reader for encoding-files.
 *
 * @see <a href="package-summary.html#font-enc">font encoding file</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EncReader implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 4119582362704393554L;

    /**
     * Create a new object.
     *
     * @param in    inputstream for reading
     * @throws FontException if an IO-error occured
     */
    public EncReader(final InputStream in) throws FontException {

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));

            StringBuffer buf = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!(line.startsWith("%") || line.equals(""))) {
                    buf.append(line).append(" ");
                }
            }
            reader.close();
            in.close();

            int first = buf.indexOf("[");
            int last = buf.lastIndexOf("]");
            if (first < 0 || last < 0) {
                throw new FontEncodingWrongRangeException();
            }
            String tablestring = buf.substring(first + 1, last).trim();
            table = tablestring.split("\\s");

        } catch (IOException e) {
            throw new FontEncodingIOException(e.getMessage());
        }
    }

    /**
     * encoding table.
     */
    private String[] table;

    /**
     * Returns the encoding table.
     * @return Returns the encoding table.
     */
    public String[] getTable() {

        return table;
    }
}