/*
 * Copyright (C) 2003  Gerd Neugebauer
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


/**
 * The locator is the container for the information about the name of a file 
 * and the current line number.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Locator {
    /** the name of the file */
    private String filename;

    /** the line number */
    private int lineno;

    /**
     * Creates a new object.
     *
     * @param filename the file name
     * @param lineno the line number
     */
    public Locator(String filename, int lineno) {
        super();
        this.filename = filename;
        this.lineno   = lineno;
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
