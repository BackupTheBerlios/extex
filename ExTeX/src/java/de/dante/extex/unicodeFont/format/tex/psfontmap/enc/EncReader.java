/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.unicodeFont.exception.FontIOException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * Reader for encoding-files.
 *
 * @see <a href="package-summary.html#font-enc">font encoding file</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */

public class EncReader implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 4119582362704393554L;

    /**
     * The field <tt>localizer</tt> contains the localizer. It is initiated
     * with a localizer for the name of this class.
     */
    private transient Localizer localizer = LocalizerFactory
            .getLocalizer(EncReader.class.getName());

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
                    // ignore all after '%'
                    int pos = line.indexOf('%');
                    if (pos >= 0) {
                        line = line.substring(0, pos - 1).trim();
                    }
                    buf.append(line).append(" ");
                }
            }
            reader.close();
            in.close();

            int fs = buf.indexOf("/");
            int first = buf.indexOf("[");
            int last = buf.lastIndexOf("]");
            if (fs < 0 || first < 0 || last < 0) {
                throw new FontException(localizer
                        .format("EncReader.WrongRange"));

            }
            String tablestring = buf.substring(first + 1, last).trim();
            table = tablestring.split("\\s");
            encname = buf.substring(fs + 1, first).trim();

        } catch (IOException e) {
            throw new FontIOException(e.getMessage());
        }
    }

    /**
     * The encoding name.
     */
    private String encname = "";

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

    /**
     * Returns the encname.
     * @return Returns the encname.
     */
    public String getEncname() {

        return encname;
    }
}
