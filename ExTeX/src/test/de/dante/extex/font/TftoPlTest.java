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

package de.dante.extex.font;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;
import de.dante.extex.font.exception.FontMapNotFoundException;
import de.dante.extex.font.type.PlWriter;
import de.dante.extex.font.type.tfm.TFMFont;
import de.dante.extex.font.type.tfm.enc.EncFactory;
import de.dante.extex.font.type.tfm.psfontsmap.PSFontsMapReader;
import de.dante.util.file.random.RandomAccessInputFile;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.ConfigurationFactory;
import de.dante.util.resource.ResourceFinder;
import de.dante.util.resource.ResourceFinderFactory;

/**
 * Test the tftopl class.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.5 $
 */

public class TftoPlTest extends TestCase {

    /**
     * path
     */
    private static final String PATH = "develop/test/data/font/";

    /**
     * files
     */
    private static final String[] FILES = {"cmr12", "cmbx10", "cmcsc10",
            "cmmi9", "cmtt9", "aer12", "tyxi"};

    /**
     * test cmr12
     * @throws IOException if an IO-error occurs
     */
    public void testcmr12() throws IOException {

        fontTest(0);
    }

    /**
     * test cmbx10
     * @throws IOException if an IO-error occurs
     */
    public void testcmbx10() throws IOException {

        fontTest(1);
    }

    /**
     * test cmcsc10
     * @throws IOException if an IO-error occurs
     */
    public void testcmcsc10() throws IOException {

        fontTest(2);
    }

    /**
     * test cmmi9
     * @throws IOException if an IO-error occurs
     */
    public void testcmmi9() throws IOException {

        fontTest(3);
    }

    /**
     * test cmmi9
     * @throws IOException if an IO-error occurs
     */
    public void testcmtt9() throws IOException {

        fontTest(4);
    }

    /**
     * test aer12
     * @throws IOException if an IO-error occurs
     */
    public void testaer12() throws IOException {

        fontTest(5);
    }

    /**
     * test tyxi
     * @throws IOException if an IO-error occurs
     */
    public void testtyxi() throws IOException {

        fontTest(6);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        Configuration config = new ConfigurationFactory()
                .newInstance("config/extex.xml");
        Configuration resource = new ConfigurationFactory()
                .newInstance("config/path/fileFinder.xml");

        Configuration cfgfonts = config.getConfiguration("Fonts");

        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(".extex-test");
            prop.load(in);
        } catch (Exception e) {
            prop.setProperty("extex.fonts", "src/font");
        }

        ResourceFinder finder = (new ResourceFinderFactory())
                .createResourceFinder(resource, null, prop);

        EncFactory ef = new EncFactory(finder);

        // create pl-files
        for (int i = 0; i < FILES.length; i++) {

            // tfm-file
            InputStream tfmin = finder.findResource(FILES[i], "tfm");

            if (tfmin == null) {
                throw new FileNotFoundException(FILES[i]);
            }

            // psfonts.map
            InputStream psin = finder.findResource("psfonts.map", "");

            if (psin == null) {
                throw new FontMapNotFoundException();
            }
            PSFontsMapReader psfm = new PSFontsMapReader(psin);

            TFMFont font = new TFMFont(new RandomAccessInputStream(tfmin),
                    FILES[i]);

            font.setFontMapEncoding(psfm, ef);

            PlWriter out = new PlWriter(new BufferedOutputStream(
                    new FileOutputStream(PATH + FILES[i] + ".tfm.pl")));
            out.printZeroWidth(true);
            font.toPL(out);
            out.close();
        }

    }

    /**
     * test the Font
     * @param   nr  the font nr
     * @throws IOException if a IO-error occurs
     */
    private void fontTest(final int nr) throws IOException {

        RandomAccessR rarorg = new RandomAccessInputFile(PATH + FILES[nr]
                + ".tftopl");
        RandomAccessR rarnew = new RandomAccessInputFile(PATH + FILES[nr]
                + ".tfm.pl");

        assertEquals(readPL(rarorg, "FAMILY"), readPL(rarnew, "FAMILY"));
        assertEquals(readPL(rarorg, "DESIGNSIZE"), readPL(rarnew, "DESIGNSIZE"));

        List list = readAllPl(rarorg, "CHARACTER");
        for (int i = 0; i < list.size(); i++) {
            String cmd = (String) list.get(i);
            String l1 = readPL(rarorg, cmd);
            assertEquals(l1, readPL(rarnew, cmd));
        }

        rarorg.close();
        rarnew.close();
    }

    /**
     * read all commands
     * @param rar   the input
     * @param command   the command
     * @return the list
     * @throws IOException if an IO-error occurs
     */
    private List readAllPl(final RandomAccessR rar, final String command)
            throws IOException {

        List list = new ArrayList();
        rar.seek(0);
        String line;
        while ((line = rar.readLine()) != null) {
            if (line.trim().startsWith("(" + command)) {
                list.add(line.trim().substring(1));
            }
        }
        return list;
    }

    /**
     * read the command
     * from '(command xxx )'
     * @param rar   the input
     * @param command   the command
     * @return the line
     * @throws IOException if an IO-error occurs
     */
    private String readPL(final RandomAccessR rar, final String command)
            throws IOException {

        rar.seek(0);
        StringBuffer buf = new StringBuffer();
        String line;
        long pointer = rar.getPointer();
        while ((line = rar.readLine()) != null) {
            if (line.trim().startsWith("(" + command)) {
                rar.seek(pointer);
                int level = 0;
                boolean kf = true;
                char c;
                while (kf || level != 0) {
                    c = (char) rar.readByteAsInt();
                    if (c == '(') {
                        level++;
                        kf = false;
                    } else if (c == ')') {
                        level--;
                    }
                    if (c == '\n') {
                        c = ' ';
                    }
                    if (c != '\r') {
                        buf.append(c);
                    }
                }
                break;
            }
            pointer = rar.getPointer();
        }
        return buf.toString().trim();
    }

    /**
     * test tftopl
     * @param args  the commandline
     */
    public static void main(final String[] args) {

        junit.textui.TestRunner.run(TftoPlTest.class);
    }
}
