/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.font;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.TestCase;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Test the font-system
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class FontTest extends TestCase {

    /**
     * main
     * @param args ...
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(FontTest.class);

    }

    /**
     * Dimen 12 pt
     */
    private static final Dimen DIM12 = new Dimen(GlueComponent.ONE * 12);

    /**
     * Make a <code>FontFactory</code>.
     *
     * @return a <code>FontFactory</code>
     * @exception Exception if an error occurs
     */
    private FontFactory makeFontFactory() throws Exception {

        FontFactory fontFactory;
        ResourceFinder fontFinder;

        Properties properties = System.getProperties();

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");

        Configuration fontConfig = config.getConfiguration("Fonts");
        Configuration resource = new ConfigurationFactory()
                .newInstance("config/path/fileFinder.xml");

        String fontClass = fontConfig.getAttribute("class");

        // load user properties
        properties.load(new FileInputStream(".extex-test"));

        // test configuration
        assertNotNull(fontClass);
        assertTrue(!fontClass.equals(""));

        fontFinder = (new ResourceFinderFactory()).createResourceFinder(
                resource, Logger.global, properties);

        fontFactory = (FontFactory) (Class.forName(fontClass).getConstructor(
                new Class[]{Configuration.class, ResourceFinder.class})
                .newInstance(new Object[]{fontConfig, fontFinder}));

        return fontFactory;
    }

    /**
     * test 01
     * @throws Exception ...
     */
    public void test01() throws Exception {

        FontFactory factory = makeFontFactory();

        Font font = factory.getInstance("tfmcmr12", DIM12);

        assertEquals("tfmcmr12", font.getFontName());
        assertTrue(DIM12.eq(font.getEm()));
    }

}
