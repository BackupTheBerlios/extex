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

package de.dante.extex.backend.documentWriter.postscript.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dante.extex.interpreter.type.font.Font;
import de.dante.util.UnicodeChar;

/**
 * The font manager keeps track of the fonts and characters used.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class FontManager {

    /**
     * The field <tt>fonts</tt> contains the registered fonts and characters.
     */
    private Map fonts = new HashMap();

    /**
     * Creates a new object.
     */
    public FontManager() {

        super();
    }

    /**
     * The field <tt>currentFont</tt> contains the font encountered most
     * recently.
     */
    private Font currentFont = null;

    /**
     * The field <tt>texdict</tt> contains the definition of font changing
     * functions.
     */
    private StringBuffer texdict = new StringBuffer("TeXDict begin\n");

    /**
     * The field <tt>fntNo</tt> contains the next number to be used for a
     * font changing macro number.
     */
    private int fntNo = 1;

    /**
     * Receive the information that a character in a certain font has been used
     * and should be remembered.
     *
     * @param font the font which is used
     * @param c the character in the font which is used
     *
     * @return <code>true</code> iff the font is not the one previously
     *  reported
     */
    public String add(final Font font, final UnicodeChar c) {

        Map map = (Map) fonts.get(font);
        if (map == null) {
            map = new HashMap();
            fonts.put(font, map);
        }
        map.put(c, c);

        if (font != currentFont) {
            currentFont = font;
            String n = Integer.toString(fntNo++);
            texdict.append("/F");
            texdict.append(n);
            texdict.append("{/Times-Roman findfont "); //TODO gene: use the correct font
            PsUnit.toPoint(font.getActualSize(), texdict, false);
            texdict.append(" scalefont setfont}def\n");

            return "F" + n + " ";
        }
        return null;
    }

    /**
     * Clear the memory and forget everything about fonts used.
     */
    public void clear() {

        fonts.clear();
        currentFont = null;
        fntNo = 1;
    }

    /**
     * Getter for the list of fonts.
     *
     * @return the list of fonts
     */
    public Font[] listFonts() {

        Font[] f = new Font[fonts.size()];
        Iterator iter = fonts.keySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            f[i++] = (Font) iter.next();
        }
        return f;
    }

    /**
     * Write all fonts to a given PostScript stream.
     *
     * @param stream the target stream
     *
     * @throws IOException in case of an IO error
     */
    public void write(final OutputStream stream) throws IOException {

        texdict.append("end\n");
        stream.write(texdict.toString().getBytes());
    }

}
