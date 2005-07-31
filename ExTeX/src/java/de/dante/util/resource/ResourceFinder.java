/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.util.resource;

import java.io.InputStream;

import de.dante.util.configuration.ConfigurationException;

/**
 * This interface describes a class which is able to find files or other
 * resources of different kinds for reading.
 * It is not determined how the search is performed.
 * Searching for the given file name, augmenting extension and path, or using
 * an external library &ndash; like kpathsea &ndash; are left to possible
 * implementations. Even an interaction with the user can be envisioned.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public interface ResourceFinder {

    /**
     * Find a resource which can be used for reading. If the search fails then
     * <code>null</code> is returned.
     *
     * @param name the base name of the resource
     * @param type the type, i.e. the extension
     *
     * @return the file or <code>null</code> if none could be found
     *
     * @throws ConfigurationException in case of an exception
     */
    InputStream findResource(String name, String type)
            throws ConfigurationException;

    /**
     * Enable or disable the tracing. The argument indicates whether tracing
     * should be enabled or disabled.
     * The resource finder can decide on its own how to perform tracing. The
     * preferred way is to write tracing records to a logger.
     *
     * @param flag indicator whether tracing should be turned on or off.
     */
    void enableTracing(boolean flag);

}
