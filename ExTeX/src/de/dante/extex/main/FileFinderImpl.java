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
package de.dante.extex.main;

import java.io.File;
import java.io.IOException;

import de.dante.extex.i18n.Messages;
import de.dante.extex.logging.Logger;
import de.dante.util.file.FileFinder;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class FileFinderImpl implements FileFinder {

    /**
     * The field <tt>logger</tt> contains the ...
     */
    private Logger logger;

    /**
     * Creates a new object.
     */
    public FileFinderImpl(Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String, java.lang.String)
     */
    public File findFile(String name, String type) {
        File file = null;

        try {
            do {
                if (!name.equals("")) {
                    logger.severe("\n! "+Messages.format("CLI.FileNotFound",
                                                  name));
                }

                logger.severe(Messages.format("CLI.PromptFile"));
                name = readLine();

                if (name.equals("")) {
                    //TODO ???
                } else if (name.charAt(0) == '\\') {
                    //TODO make use of the line read
                    return null;
                }
            } while (file == null);
        } catch (IOException e) {
            //TODO incomplete
            file = null;
        }

        return file;
    }

    /**
     * ...
     *
     * @return the line read
     *
     * @throws IOException ...
     */
    private String readLine() throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;

        while ((c = System.in.read()) >= 0 && c == ' ') {
            // nothing to do
        }

        if (c >= 0 && c != '\n') {
            do {
                sb.append((char) c);
            } while ((c = System.in.read()) >= 0 && c != '\n');
        }

        return sb.toString();
    }
}
