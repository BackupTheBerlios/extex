/*
 * Copyright (C) 2004  Gerd Neugebauer
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

import java.io.File;

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.configuration.ConfigurationException;

/*
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderDirect implements FileFinder {

    private StringList ext;
    
    /**
     * Creates a new object.
     * 
     * 
     */
    public FileFinderDirect(StringList extensions) {
        super();
        this.ext = extensions;
    }
    
    /**
     * Setter for the extensions. The given string is splitted at the separator
     * <tt>:</tt>.
     * </p>
     * 
     * @param extensions the extensions to set.
     */
    public void setExtension(String extensions) {
        this.ext = new StringList(extensions, ":");
    }
    
    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String,
     *      java.lang.String)
     */
    public File findFile(String name, String type)
            throws ConfigurationException {
        File file;

        StringListIterator extIt = ext.getIterator();
        while (extIt.hasNext()) {
            String ext = extIt.next();
            file = new File(name + ext);
            if (file.canRead())
                return file;
        }

        return null;
    }
    
}
