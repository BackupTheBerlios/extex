/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font.type.tfm.psfontsmap;

/**
 * Container for a psfonts.map-line
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class PSFontEncoding {

    /**
     * Create a new object.
     */
    public PSFontEncoding() {

        super();
    }

    /**
     * filename
     */
    private String filename = "";

    /**
     * fontname
     */
    private String fontname = "";

    /**
     * encodingtxt
     */
    private String encodingtxt = "";

    /**
     * encfile
     */
    private String encfile = "";

    /**
     * pfbfile;
     */
    private String pfbfile = "";

    /**
     * @return Returns the encfile.
     */
    public String getEncfile() {

        return encfile;
    }

    /**
     * @param encf The encfile to set.
     */
    public void setEncfile(final String encf) {

        encfile = encf;
    }

    /**
     * @return Returns the encodingtxt.
     */
    public String getEncodingtxt() {

        return encodingtxt;
    }

    /**
     * Add a string to <tt>encodingtxt</tt>
     * @param s the string to add
     */
    public void addEncodingtxt(final String s) {

        encodingtxt += s + " ";
    }

    /**
     * @param enctxt The encodingtxt to set.
     */
    public void setEncodingtxt(final String enctxt) {

        encodingtxt = enctxt;
    }

    /**
     * @return Returns the filename.
     */
    public String getFilename() {

        return filename;
    }

    /**
     * @param name The filename to set.
     */
    public void setFilename(final String name) {

        filename = name;
    }

    /**
     * @return Returns the fontname.
     */
    public String getFontname() {

        return fontname;
    }

    /**
     * @param name The fontname to set.
     */
    public void setFontname(final String name) {

        fontname = name;
    }

    /**
     * @return Returns the pfbfile.
     */
    public String getPfbfile() {

        return pfbfile;
    }

    /**
     * @param file The pfbfile to set.
     */
    public void setPfbfile(final String file) {

        pfbfile = file;
    }
}