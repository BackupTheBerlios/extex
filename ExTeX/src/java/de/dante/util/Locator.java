/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
 *
 */
package de.dante.util;

import java.nio.CharBuffer;


/**
 * The locator is the container for the information about the name of a file
 * and the current line number.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Locator {

    /**
     * The field <tt>filename</tt> contains the name of the file.
     */
    private String filename;

    /**
     * The field <tt>lineno</tt> contains the line number.
     */
    private int lineno;

    /**
     * The field <tt>line</tt> contains the ...
     */
    private CharBuffer line;

    /**
     * The field <tt>linePointer</tt> contains the ...
     */
    private int linePointer;

    /**
     * Creates a new object.
     *
     * @param fileName the file name
     * @param lineNo the line number
     * @param line ...
     * @param linePointer ...
     */
    public Locator(final String fileName, final int lineNo,
            final CharBuffer line, final int linePointer) {

        super();
        this.filename = fileName;
        this.lineno = lineNo;
        this.line = line;
        this.linePointer = linePointer;
    }

    /**
     * Getter for line.
     *
     * @return the line.
     */
    public CharBuffer getLine() {
        return line;
    }

    /**
     * Getter for linePointer.
     *
     * @return the linePointer.
     */
    public int getLinePointer() {
        return linePointer;
    }

    /**
     * Getter for the file name.
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
