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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dante.util.configuration.ConfigurationException;

/*
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderList implements FileFinder {

    private List list = new ArrayList();
    
    /**
     * Creates a new object.
     * 
     * 
     */
    public FileFinderList() {
        super();
    }
    
    public FileFinderList(FileFinder finder) {
        super();
        add(finder);
    }
    
    public FileFinderList(FileFinder finder1, FileFinder finder2) {
        super();
        add(finder1);
        add(finder2);
    }
    
    public FileFinderList(FileFinder finder1, FileFinder finder2, FileFinder finder3) {
        super();
        add(finder1);
        add(finder2);
        add(finder3);
    }
    
    public void add(FileFinder finder) {
        list.add(finder);
    }
    
    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String,
     *      java.lang.String)
     */
    public File findFile(String name, String type)
            throws ConfigurationException {

        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            File f = ((FileFinder) iter.next()).findFile(name, type);
            if (f != null) {
                return f;
            }
        }

        return null;
    }

}
