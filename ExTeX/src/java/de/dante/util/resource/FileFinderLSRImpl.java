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

package de.dante.util.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.dante.util.StringList;
import de.dante.util.StringListIterator;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;

/**
 * This file finder search a file in a ls-R file.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class FileFinderLSRImpl implements ResourceFinder {

    /**
     * use obj-hashmap in file
     */
    private static final boolean USEOBJ = false;

    /**
     * The field <tt>path</tt> contains the directory where the ls-R file is stored
     */
    private File path;

    /**
     * filename for the ls-R file
     */
    private static final String LSR = "ls-R";

    /**
     * The field <tt>ext</tt> the list of extensions to consider.
     */
    private StringList extensionList;

    /**
     * Map for the ls-R entries
     */
    private Map lsr = null;

    /**
     * Creates a new object.
     *
     * @param apath          directory with ls-R file
     * @param extensions    list of extensions to consider
     */
    public FileFinderLSRImpl(final String apath, final StringList extensions) {

        super();
        path = new File(apath);
        extensionList = extensions;
    }

    /**
     * Setter for the extensions. The given string is splitted at the separator
     * <tt>:</tt>.
     *
     * @param extensions the extensions to set.
     */
    public void setExtension(final String extensions) {

        extensionList = new StringList(extensions, ":");
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        if (lsr == null) {
            File lsrobj = new File("ls-R.obj");
            // TODO incomplete ....

            if (USEOBJ && lsrobj.canRead()) {
                //long start = System.currentTimeMillis();
                try {
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(lsrobj));

                    lsr = (HashMap) ois.readObject();
                    ois.close();

                } catch (Exception e) {
                    throw new ConfigurationIOException("error", e);
                }
                //System.out.println("\nload ls-R.obj "
                //        + (System.currentTimeMillis() - start) + " ms");
            } else {
                //long start = System.currentTimeMillis();
                loadLSR();
                //System.out.println("\nload ls-R "
                //        + (System.currentTimeMillis() - start) + " ms   size=" + lsr.size());
                if (USEOBJ) {
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(
                                new FileOutputStream(lsrobj));

                        oos.writeObject(lsr);
                        oos.close();

                    } catch (Exception e) {
                        throw new ConfigurationIOException("error", e);
                    }
                }
            }
        }

        File file;

        StringListIterator extIt = extensionList.getIterator();
        while (extIt.hasNext()) {
            String ext = extIt.next();
            file = (File) lsr.get(name + (ext.equals("") ? "" : ".") + ext);
            if (file != null && file.canRead()) {
                try {
                    //System.out.println("\nFILE " + file);
                    InputStream stream = new FileInputStream(file);
                    return stream;
                } catch (FileNotFoundException e) {
                    // ignore unreadable files
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * Load the ls-R file
     * 
     * @throws ConfigurationException if an error occured
     */
    private void loadLSR() throws ConfigurationException {

        lsr = new HashMap();

        try {

            BufferedReader in = new BufferedReader(new FileReader(new File(
                    path, LSR)));

            String line;
            String rpath = "";
            while ((line = in.readLine()) != null) {
                if (line.startsWith("%") || line.trim().equals("")) {
                    continue;
                }
                line = line.trim();
                if (line.endsWith(":")) {
                    // directory
                    rpath = line.substring(0, line.length() - 1);
                    if (rpath.startsWith("./")) {
                        rpath = rpath.substring(2);
                    }
                } else {
                    // file
                    File abspath = new File(path, rpath);
                    File file = new File(abspath, line);
                    // only readable files!
                    if (!file.isDirectory() && file.canRead()) {
                        lsr.put(line, file);
                    }
                }
            }
            in.close();

        } catch (FileNotFoundException e) {
            throw new ConfigurationIOException("FileNotFound", e);
        } catch (IOException e) {
            throw new ConfigurationIOException("IO", e);
        }
    }
}