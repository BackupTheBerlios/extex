/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.font;

import java.io.File;

/**
 * Abstract class for a Font-file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractFontFile implements FontFile {

    /**
     * Create a new object
     * @param file  the external file
     */
    AbstractFontFile(final File file) {

        super();
        externalfile = file;
    }

    /**
     * the external file
     */
    private File externalfile;

    /**
     * Return the <code>File</code> for the external fontfile
     * @return  the external font-file
     */
    public File getFile() {

        return externalfile;
    }

    /**
     * @param file The externalfile to set.
     */
    public void setFile(final File file) {

        externalfile = file;
    }

    /**
     * Return the String for the class
     * @return the string for the class
     */
    public abstract String toString();

}
