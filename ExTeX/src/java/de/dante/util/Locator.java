/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.util;

import java.io.Serializable;

/**
 * The locator is the container for the information about the name of a file
 * and the current line number.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public class Locator implements Serializable {

    /**
     * The field <tt>filename</tt> contains the name of the file.
     */
    private String filename;

    /**
     * The field <tt>lineno</tt> contains the line number.
     */
    private int lineno;

    /**
     * The field <tt>line</tt> contains the current line of input.
     */
    private String line;

    /**
     * The field <tt>linePointer</tt> contains the positin in the line the
     * locator refers to.
     */
    private int linePointer;

    /**
     * Creates a new object.
     *
     * @param fileName the file name
     * @param lineNo the line number
     * @param currentLine the current line of input
     * @param currentLinePointer the current position in the line of input
     */
    public Locator(final String fileName, final int lineNo,
            final String currentLine, final int currentLinePointer) {

        super();
        this.filename = fileName;
        this.lineno = lineNo;
        this.line = currentLine;
        this.linePointer = currentLinePointer;
    }

    /**
     * Getter for the line.
     *
     * @return the line.
     */
    public String getLine() {

        return line;
    }

    /**
     * Getter for the line pointer.
     * The line ponter is the position within the line at which something
     * happend.
     *
     * @return the line pointer.
     */
    public int getLinePointer() {

        return linePointer;
    }

    /**
     * Getter for the file name.
     * The file name can be unset. in ths case <code>null</code> is returned.
     *
     * @return the line file name or <code>null</code>
     */
    public String getFilename() {

        return filename;
    }

    /**
     * Getter for the line number.
     *
     * @return the line number
     */
    public int getLineno() {

        return lineno;
    }

}