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

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.framework.logger.LogEnabled;
import de.dante.util.resource.RecursiveFinder;
import de.dante.util.resource.ResourceFinder;

/**
 * This ResourceFinder queries the user for the name of the file to use and
 * tries to find it via its super class.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ResourceFinderImpl
        implements
            ResourceFinder,
            RecursiveFinder,
            LogEnabled {

    /**
     * The field <tt>configuration</tt> contains the ...
     */
    private Configuration configuration;

    /**
     * The field <tt>logger</tt> contains the ...
     */
    private Logger logger = null;

    /**
     * The field <tt>parent</tt> contains the ...
     */
    private ResourceFinder parent = null;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration to use
     *
     * @throws ConfigurationException in case of an errorin the configuration
     */
    public ResourceFinderImpl(final Configuration configuration)
            throws ConfigurationException {

        super();
        this.configuration = configuration;
    }

    /**
     * ...
     *
     * @param logger
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(java.util.logging.Logger)
     */
    public void enableLogging(final Logger logger) {

        this.logger = logger;
    }

    /**
     * ...
     *
     * @param flag
     *
     * @see de.dante.util.resource.ResourceFinder#enableTrace(boolean)
     */
    public void enableTrace(final boolean flag) {

    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        String skip = configuration.findConfiguration(type)
                .getAttribute("skip");
        if (skip != null && Boolean.valueOf(skip).booleanValue()) {
            return null;
        }

        String line = name;
        Localizer localizer = LocalizerFactory
                .getLocalizer(ResourceFinderImpl.class.getName());

        for (;;) {
            if (!line.equals("")) {
                logger.severe("\n! "
                        + localizer.format("CLI.FileNotFound", name, type));
            }

            logger.severe(localizer.format("CLI.PromptFile"));
            line = readLine();

            if (line == null || line.equals("")) {
                return null;
            }
            if (line.charAt(0) == '\\') {
                //TODO make use of the line read
                throw new RuntimeException("unimplemented");
            } else {
                InputStream stream = parent.findResource(line, type);
                if (stream != null) {
                    return stream;
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
            for (int c = System.in.read(); c > 0 && c != '\n'; c = System.in
                    .read()) {

                if (c != ' ' || sb.length() > 0) {
                    sb.append((char) c);
                }
            }
        } catch (IOException e) {
            return null;
        }

        return (sb.length() > 0 ? sb.toString() : null);
    }

    /**
     * ...
     *
     * @param parent ...
     *
     * @see de.dante.util.resource.RecursiveFinder#setParent(
     *      de.dante.util.resource.ResourceFinder)
     */
    public void setParent(final ResourceFinder parent) {

        this.parent = parent;
    }

}