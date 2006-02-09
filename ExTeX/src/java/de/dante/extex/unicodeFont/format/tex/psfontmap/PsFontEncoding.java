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

package de.dante.extex.unicodeFont.format.tex.psfontmap;

import java.io.Serializable;

/**
 * Container for a psfonts.map-line.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class PsFontEncoding implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -8945435561945139483L;

    /**
     * Create a new object.
     */
    public PsFontEncoding() {

        super();
    }

    /**
     * The filename.
     */
    private String filename = "";

    /**
     * The fontname.
     */
    private String fontname = "";

    /**
     * The encodingtxt.
     */
    private String encodingtxt = "";

    /**
     * The encfile.
     */
    private String encfile = "";

    /**
     * The font file, e.g. xxx.pfb, xxx.ttf.
     */
    private String fontfile = "";

    /**
     * Returns the encfile.
     * @return Returns the encfile.
     */
    public String getEncfile() {

        return encfile;
    }

    /**
     * Set the encfile.
     * @param encf The encfile to set.
     */
    public void setEncfile(final String encf) {

        encfile = encf;
    }

    /**
     * Returns the encodingtxt.
     * @return Returns the encodingtxt.
     */
    public String getEncodingtxt() {

        return encodingtxt.trim();
    }

    /**
     * Add a string to <tt>encodingtxt</tt>.
     * @param s the string to add
     */
    public void addEncodingtxt(final String s) {

        encodingtxt += s + " ";
    }

    /**
     * Set the encodingtxt.
     * @param enctxt The encodingtxt to set.
     */
    public void setEncodingtxt(final String enctxt) {

        encodingtxt = enctxt;
    }

    /**
     * Returns the filename.
     * @return Returns the filename.
     */
    public String getFilename() {

        return filename;
    }

    /**
     * Set the filename.
     * @param name The filename to set.
     */
    public void setFilename(final String name) {

        filename = name;
    }

    /**
     * Returns the fontname.
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * Set the font name.
     * @param name The fontname to set.
     */
    public void setFontname(final String name) {

        fontname = name;
    }

    /**
     * Returns the font file.
     * @return Returns the font file.
     */
    public String getFontfile() {

        return fontfile;
    }

    /**
     * Set the font file.
     * @param file The font file to set.
     */
    public void setFontfile(final String file) {

        fontfile = file;
    }

    /**
     * Returns the font file extension, e.g. pfb, ttf
     * @return Returns the font file extension, e.g. pfb, ttf
     */
    public String getFontfileExtension() {

        int pos = fontfile.lastIndexOf('.');
        if (pos >= 0) {
            return fontfile.substring(pos + 1);
        }
        return "";
    }
}
