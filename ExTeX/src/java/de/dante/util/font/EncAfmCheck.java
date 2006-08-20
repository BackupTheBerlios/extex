/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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
 */

package de.dante.util.font;

import java.io.FileInputStream;
import java.io.IOException;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.format.afm.AfmCharMetric;
import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.extex.unicodeFont.format.tex.psfontmap.enc.EncReader;
import de.dante.extex.unicodeFont.glyphname.GlyphName;
import de.dante.util.UnicodeChar;

/**
 * Read a encoding vector and check, if the glyph names are in the afm file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 *
 */
public final class EncAfmCheck {

    /**
     * No instance.
     */
    private EncAfmCheck() {

        // no instance
    }

    /**
     * The main method.
     * 0: enc-file
     * 1: afm-file
     * @param args  The command line.
     * @throws IOException if  a IO-error occurred.
     * @throws FontException if a font-error occurred.
     */
    public static void main(final String[] args) throws IOException,
            FontException {

        if (args.length != 2) {
            System.err
                    .println("java de.dante.util.font.EncAfmCheck <file.enc> <file.afm>");
            System.exit(1);
        }

        EncReader enc = new EncReader(new FileInputStream(args[0]));

        AfmParser afm = new AfmParser(new FileInputStream(args[1]));

        GlyphName glyphname = GlyphName.getInstance();

        String[] table = enc.getTable();

        for (int i = 0; i < table.length; i++) {
            String name = table[i].replaceAll("/", "");
            System.out.print(i + "   " + name);
            if (!".notdef".equals(name)) {
                AfmCharMetric cm = afm.getAfmCharMetric(name);
                if (cm == null) {
                    System.out.print("     #   not found!");
                    UnicodeChar uc = glyphname.getUnicode(name);
                    if (uc != null) {
                        int cp = uc.getCodePoint();
                        String snr = "0000" + Integer.toHexString(cp);
                        String ucname = "uni" + snr.substring(snr.length() - 4);
                        cm = afm.getAfmCharMetric(ucname);
                        if (cm == null) {
                            System.out.print("  rename " + ucname);
                        }
                    }
                }
            }
            System.out.println();
        }

    }

}
