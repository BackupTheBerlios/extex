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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * EntityResolver, which get the stream from a ResourceFinder.
 * <p>Used for the DOM-Parser</p>
 *
 * <p>
 * In the first try, the resolver try to find a file name with the public-id
 * of the DTD.
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class EntityResolverRf implements EntityResolver {

    /**
     * The ResourceFinder.
     */
    private ResourceFinder finder;

    /**
     * Create a new object.
     * @param rf    The resource finder to use.
     */
    public EntityResolverRf(final ResourceFinder rf) {

        finder = rf;
    }

    /**
     * dtd: map public id to file name.
     */
    private Map dtd = null;

    /**
     * @see org.xml.sax.EntityResolver#resolveEntity(
     *      java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(final String publicId,
            final String systemId) throws SAXException, IOException {

        InputSource source = null;

        loadDtdMap();

        String file = (String) dtd.get(publicId);
        if (file != null) {
            try {
                InputStream in = finder.findResource(file, "");
                if (in != null) {
                    source = new InputSource(in);
                }
            } catch (ConfigurationException e) {
                throw new IOException(e.getMessage());
            }
        } else {

            // TODO delete after test
            System.out.println("systemid: '" + systemId + "'");
            System.out.println("publicid: '" + publicId + "'");

            // test, if system id starts with 'file:'
            if (systemId.startsWith("file:")) {
                int last = systemId.lastIndexOf(File.separator);
                if (last >= 0) {
                    file = systemId.substring(last + 1);
                    try {
                        InputStream in = finder.findResource(file, "");
                        if (in != null) {
                            source = new InputSource(in);
                        }
                    } catch (ConfigurationException e) {
                        throw new IOException(e.getMessage());
                    }

                }
            } else {
                try {
                    URI uri = new URI(systemId);
                    if (uri != null) {
                        URL url = uri.toURL();
                        if (url != null) {
                            InputStream in = url.openStream();
                            if (in != null) {
                                source = new InputSource(in);
                            }
                        }
                    }

                } catch (URISyntaxException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
        return source;
    }

    /**
     * The file name for the dtd map file.
     */
    private static final String DTD_FILE = "dtd.map";

    /**
     * Load the map for the dtd.
     * <p>1. load from the user.home-dir</p>
     * <p>2. load from the class-dir</p>
     * @throws IOException if an error occurs.
     */
    private void loadDtdMap() throws IOException {

        if (dtd == null) {
            dtd = new HashMap();

            InputStream mapin = null;

            // first, try in user.home
            File file = new File(System.getProperty("user.home")
                    + File.separator + DTD_FILE);
            if (file.canRead()) {
                mapin = new FileInputStream(file);
            } else {
                // second, try in class-dir
                mapin = this.getClass().getResourceAsStream(DTD_FILE);

            }
            if (mapin != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        mapin));

                String line;
                while ((line = in.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        int pos = line.indexOf('=');
                        if (pos >= 0) {
                            String key = line.substring(0, pos);
                            String value = line.substring(pos + 1);
                            dtd.put(key, value);
                        }

                    }
                }
                in.close();
            }
        }
    }
}
