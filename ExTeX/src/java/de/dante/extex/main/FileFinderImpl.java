/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import java.util.logging.Logger;

import de.dante.extex.i18n.Messages;
import de.dante.util.file.FileFinder;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class FileFinderImpl implements FileFinder {

    /**
     * The field <tt>logger</tt> contains the logger to write output to.
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param aLogger ...
     */
    public FileFinderImpl(final Logger aLogger) {
        super();
        this.logger = aLogger;
    }

    /**
     * @see de.dante.util.file.FileFinder#findFile(java.lang.String,
     *      java.lang.String)
     */
    public File findFile(final String name, final String type) {

        File file = null;
        String line = name;

        try {
            do {
                if (!line.equals("")) {
                    logger.severe("\n! "
                                  + Messages.format("CLI.FileNotFound", name));
                }

                logger.severe(Messages.format("CLI.PromptFile"));
                line = readLine();

                if (!line.equals("")) {
                    if (line.charAt(0) == '\\') {
                        //TODO make use of the line read
                        throw new RuntimeException("unimplemented");
                    } else {
                        file = new File(line);
                        if (!file.canRead()) {
                            file = null;
                        }
                    }
                }
            } while (file == null);
        } catch (IOException e) {
            //TODO incomplete
            file = null;
        }

        return file;
    }

    /**
     * Read a line of characters from the standard input stream.
     * Leading spaces are ignored. At end of file <code>null</code> is returned.
     *
     * @return the line read or <code>null</code> to signal EOF
     *
     * @throws IOException in case of an error during IO. This is rather
     * unlikely
     */
    private String readLine() throws IOException {

        StringBuffer sb = new StringBuffer();

        for (int c = System.in.read(); c > 0; c = System.in.read()) {
            if (c == '\n') {
                sb.append((char) c);
                return sb.toString();
            } else if (c != ' ' || sb.length() > 0) {
                sb.append((char) c);
            }
        }

        return (sb.length() > 0 ? sb.toString() : null);
    }

}
