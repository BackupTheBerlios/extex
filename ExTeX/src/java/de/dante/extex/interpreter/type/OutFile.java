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
package de.dante.extex.interpreter.type;

import java.io.File;
import java.io.Serializable;

import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class OutFile implements Serializable {

    /**
     * The constant <tt>MAX_FILE_NO</tt> contains the ...
     */
    public static final int MAX_FILE_NO = 15;

    /**
     * The field <tt>file</tt> contains the ...
     */
    private File file = null;

    /**
     * Creates a new object.
     *
     * @param name the file to write to
     */
    public OutFile(final File name) {
        super();
        file = name;
    }

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    public void open() throws GeneralException {
        //TODO
    }

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    public void close() throws GeneralException {
        //TODO
    }

    public boolean isOpened() {
        return false; // TODO
    }

    /**
     * ...
     *
     * @param t ...
     *
     * @throws GeneralException in case of an error
     */
    public void write(final Tokens t) throws GeneralException {
        //TODO
    }

}
