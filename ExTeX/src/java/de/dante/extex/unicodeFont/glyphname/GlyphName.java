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

package de.dante.extex.unicodeFont.glyphname;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

import de.dante.util.UnicodeChar;

/**
 * This class manage the correlation between the glyph name and
 * the Unicode value.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public final class GlyphName {

    /**
     * The file name.
     */
    private static final String FILE = "GlyphName.properties";

    /**
     * The initial size for the map.
     */
    private static final int INITSIZE = 5000;

    /**
     * hex value.
     */
    private static final int HEX = 16;

    /**
     * Create a new object.
     *
     * Read the <code>FILE</code> and parse it.
     *
     * @throws IOException if an IO-error occurred.
     */
    private GlyphName() throws IOException {

        glyphmap = new HashMap(INITSIZE);

        LineNumberReader in = new LineNumberReader(new InputStreamReader(
                getClass().getResourceAsStream(FILE)));

        String line;
        try {
            while ((line = in.readLine()) != null) {
                // ignore comment with #
                if (!line.startsWith("#") && line.trim().length() > 0) {
                    String[] tmp = line.split(";", 2);

                    // check, if more than one value
                    if (tmp[1].length() > 4) {
                        // TODO incomplete
                    } else {
                        glyphmap.put(tmp[0], new UnicodeChar(Integer.parseInt(
                                tmp[1], HEX)));
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
        in.close();
    }

    /**
     * The glyph name map.
     */
    private Map glyphmap;

    /**
     * The singleton instance.
     */
    private static GlyphName glyphname;

    /**
     * Return the instance of <code>GlyphName</code>.
     *
     * @return Return the instance of <code>GlyphName</code>.
     * @throws IOException if an IO-error occurred.
     */
    public static GlyphName getInstance() throws IOException {

        if (glyphname == null) {
            glyphname = new GlyphName();
        }
        return glyphname;
    }

    /**
     * Returns the <code>UnicodeChar</code> for the glyph name
     * or <code>null</code>, if not found.
     *
     * @param name The glyph name.
     * @return Returns the <code>UnicodeChar</code> for the glyph name
     *         or <code>null</code>, if not found.
     */
    public UnicodeChar getUnicode(final String name) {

        return (UnicodeChar) glyphmap.get(name);
    }

}
