/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * URIResolver, which get the stream from a ResourceFinder.
 * <p>Used for the TransformerFactory</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class UriResolverRf implements URIResolver {

    /**
     * The ResourceFinder
     */
    private ResourceFinder finder;

    /**
     * Create a new object.
     * @param rf    The resource finder to use.
     */
    public UriResolverRf(final ResourceFinder rf) {

        finder = rf;
    }

    /**
     * @see javax.xml.transform.URIResolver#resolve(
     *      java.lang.String, java.lang.String)
     */
    public Source resolve(final String href, final String base)
            throws TransformerException {

        System.out.print("href = '" + href + "'");
        System.out.println("   base = '" + base + "'");

        StreamSource source = null;
        try {
            InputStream in = finder.findResource(href, "");
            if (in != null) {
                source = new StreamSource(in);
            }
        } catch (ConfigurationException e) {
            throw new TransformerException(e);
        }
        return source;
    }

}
