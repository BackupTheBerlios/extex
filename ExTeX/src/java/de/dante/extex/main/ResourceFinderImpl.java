/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.dante.extex.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import de.dante.extex.i18n.Messages;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.resource.FileFinder;

/**
 * This ResourceFinder queries the user for the nameof the file to use and tries
 * to find it via its super class.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ResourceFinderImpl extends FileFinder {

    /**
     * Creates a new object.
     *
     * @param configuration the configuration to use
     *
     * @throws ConfigurationException in case of an errorin the configuration
     */
    public ResourceFinderImpl(final Configuration configuration)
            throws ConfigurationException {

        super(configuration);
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        String line = name;
        Logger logger = getLogger();

        for (;;) {
            if (!line.equals("")) {
                logger.severe("\n! "
                        + Messages.format("CLI.FileNotFound", name));
            }

            logger.severe(Messages.format("CLI.PromptFile"));
            line = readLine();

            if (line == null) {
                return null;
            }
            if (!line.equals("")) {
                if (line.charAt(0) == '\\') {
                    //TODO make use of the line read
                    throw new RuntimeException("unimplemented");
                } else {
                    InputStream stream = super.findResource(line, type);
                    if (stream != null) {
                        return stream;
                    }
                }
            }
        }
    }

    /**
     * Read a line of characters from the standard input stream.
     * Leading spaces are ignored. At end of file <code>null</code> is returned.
     *
     * @return the line read or <code>null</code> to signal EOF
     */
    private String readLine() {

        StringBuffer sb = new StringBuffer();

        try {
            for (int c = System.in.read(); c > 0; c = System.in.read()) {
                if (c == '\n') {
                    sb.append((char) c);
                    return sb.toString();
                } else if (c != ' ' || sb.length() > 0) {
                    sb.append((char) c);
                }
            }
        } catch (IOException e) {
            return null;
        }

        return (sb.length() > 0 ? sb.toString() : null);
    }

}