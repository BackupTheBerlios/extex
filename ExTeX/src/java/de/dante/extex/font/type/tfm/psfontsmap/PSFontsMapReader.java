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

package de.dante.extex.font.type.tfm.psfontsmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.type.tfm.psfontsmap.exception.FontMapIOException;
import de.dante.extex.font.type.tfm.psfontsmap.exception.FontMapNoFileNameException;
import de.dante.extex.font.type.tfm.psfontsmap.exception.FontMapNoFontNameException;

/**
 * Reader for a psfonts.map-file.
 *
 * @see <a href="package-summary.html#psfontsmap">psfonts.map</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */

public class PSFontsMapReader implements Serializable {

    /**
     * Create a new object.
     *
     * @param in    inputstream for reading
     * @throws FontException if an font-error occured
     */
    public PSFontsMapReader(final InputStream in) throws FontException {

        try {
            data = new HashMap();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("%") || line.equals("")) {
                    continue;
                }
                PSFontEncoding psfe = new PSFontEncoding();
                StringTokenizer st = new StringTokenizer(line);

                // filename
                if (st.hasMoreTokens()) {
                    psfe.setFilename(st.nextToken());
                } else {
                    throw new FontMapNoFileNameException(line);
                }
                // fontname
                if (st.hasMoreTokens()) {
                    psfe.setFontname(st.nextToken());
                } else {
                    throw new FontMapNoFontNameException(line);
                }
                String tmp;
                boolean encodingtextfound = false;
                while (st.hasMoreTokens()) {
                    tmp = st.nextToken();
                    if (tmp.startsWith("\"")) {
                        encodingtextfound = !encodingtextfound;
                    } else if (encodingtextfound) {
                        psfe.addEncodingtxt(tmp);
                    } else {
                        if (tmp.endsWith("enc")) {
                            psfe.setEncfile(tmp.substring(1));
                        } else {
                            psfe.setPfbfile(tmp.substring(1));
                            break;
                        }
                    }
                }
                data.put(psfe.getFilename(), psfe);
            }
            reader.close();
        } catch (IOException e) {
            throw new FontMapIOException(e.getMessage());
        }
    }

    /**
     * Map for the font-data
     */
    private Map data;

    /**
     * Returns the encoding object for a font.
     * @param filename  the font-filename
     * @return Returns the encoding for a font.
     */
    public PSFontEncoding getPSFontEncoding(final String filename) {

        return (PSFontEncoding) data.get(filename);
    }
}