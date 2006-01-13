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

package de.dante.extex.unicodeFont.format.tex.psfontmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.unicodeFont.exception.FontException;

/**
 * Reader for a psfonts.map-file.
 *
 * @see <a href="package-summary.html#psfontsmap">psfonts.map</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class PSFontsMapReader implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -1865154820965971624L;

    /**
     * Create a new object.
     *
     * @param in    The input for reading.
     * @throws FontException if an font-error occurred.
     */
    public PSFontsMapReader(final InputStream in) throws FontException {

        try {
            data = new HashMap();

            if (in != null) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));

                // samples:
                //  antpb AntykwaPoltawskiego-Bold "encantp ReEncodeFont"
                //      <antp.enc <antpb.pfb
                //  antpbi AntykwaPoltawskiego-BoldItalic "encantp ReEncodeFont"
                //      <antp.enc <antpbi.pfb
                //  antpr AntykwaPoltawskiego-Regular "encantp ReEncodeFont"
                //      <antp.enc <antpr.pfb
                //  cmr10 CMR10 <cmr10.pfb
                //  cmr12 CMR12 <cmr12.pfb

                String line;
                while ((line = reader.readLine()) != null) {

                    line = line.trim();

                    // ignore comment
                    if (line.startsWith("%") || line.equals("")) {
                        continue;
                    }
                    PSFontEncoding psfe = new PSFontEncoding();

                    // file name
                    int pos = line.indexOf(' ');
                    if (pos >= 0) {
                        psfe.setFilename(line.substring(0, pos));
                        line = line.substring(pos).trim();
                    }

                    // font name
                    if (line.length() > 0) {
                        pos = line.indexOf(' ');
                        if (pos >= 0) {
                            psfe.setFontname(line.substring(0, pos));
                            line = line.substring(pos).trim();
                        } else {
                            psfe.setFontname(line);
                            line = "";
                        }
                    }

                    // encoding text
                    if (line.length() > 0 && line.startsWith("\"")) {
                        pos = line.indexOf('"', 1);
                        if (pos >= 0) {
                            psfe.setEncodingtxt(line.substring(1, pos));
                            line = line.substring(pos + 1).trim();
                        }
                    }

                    // encoding or font file
                    if (line.length() > 0 && line.startsWith("<")) {
                        pos = line.indexOf(' ', 1);
                        if (pos >= 0) {
                            String n = line.substring(1, pos);
                            if (n.endsWith("enc")) {
                                psfe.setEncfile(n);
                            } else {
                                psfe.setFontfile(n);
                            }
                            line = line.substring(pos).trim();
                        }
                    }

                    // font file
                    if (line.length() > 0 && line.startsWith("<")) {
                        String n = line.substring(1);
                        psfe.setFontfile(n);
                    }

                    data.put(psfe.getFilename(), psfe);
                }
                reader.close();
            }
        } catch (IOException e) {
            throw new FontException(e.getMessage());
        }
    }

    /**
     * Map for the font-data.
     */
    private Map data;

    /**
     * Returns the encoding object for a font.
     * @param filename      The font-filename.
     * @return Returns the encoding for a font.
     */
    public PSFontEncoding getPSFontEncoding(final String filename) {

        return (PSFontEncoding) data.get(filename);
    }
}