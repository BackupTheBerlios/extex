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
package de.dante.util.file;

import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;

/**
 * This interface describes a factory for out files.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface OutFileFactory {

    /**
     * Create a new instance of a output stream writer.
     *
     * @param name the name of the file to open
     * @param type the type of the file
     *
     * @return the new writer or <code>null</code> if none count be found
     *
     * @throws FileNotFoundException in case that the outpout file coud not be
     *             opened
     */
    OutputStreamWriter newInstance(String name, String type)
            throws FileNotFoundException;

}
