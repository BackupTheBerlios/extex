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

package de.dante.util;

import java.io.InputStream;

import junit.framework.TestCase;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;
import de.dante.util.resource.FileFinderLSRImpl;
import de.dante.util.resource.ResourceFinder;

/**
 * Test cases for FileFinderLSR
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class FileFinderLSRTest extends TestCase {

    /**
     * Creates a new object.
     * @param arg0 the name
     */
    public FileFinderLSRTest(final String arg0) {

        super(arg0);
    }

    /**
     * Command line interface.
     * @param args the arguments
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(FileFinderLSRTest.class);
    }

    /**
     * Test that the parsing of scaled points works.
     * A value in the middle (1234sp) is used.
     *
     * @throws Exception in case of an error
     */
    public void test1() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        Configuration cfgfonts = config.getConfiguration("Fonts");
        Configuration cfgresource = cfgfonts.getConfiguration("Resource");
        Configuration cfgfinder = cfgresource.getConfiguration("Finder");

        ResourceFinder finder = new FileFinderLSRImpl(cfgfinder);

        InputStream in = finder.findResource("cmr12", "");

        if (in != null) {
            assertTrue(true);
        } else {
            assertTrue(false);
        }
    }
}