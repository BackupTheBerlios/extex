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

package de.dante.tex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

import de.dante.extex.ExTeX;

import junit.framework.Assert;


/**
 * Test for ExTeX.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public final class TestTeX {

    /**
     * private: no instance
     */
    private TestTeX() {

    }

    /**
     * Run ExTeX with a special File and compare the output with a output-test-file.
     * @param texfile   the tx-file
     * @param outfile   the output-test-file
     * @exception Exception iff an error occurs; iff the two files are
     *     not equals AssertionFailedError
     */
    public static void test(final String texfile, final String outfile)
        throws Exception {

        // run ExTeX
        Properties pro = System.getProperties();
        ExTeX extex = new ExTeX(pro, ".extex");
        pro.setProperty("extex.output", "text");
        pro.setProperty("extex.file", texfile);
        pro.setProperty("extex.jobname", texfile);
        extex.run();

        // compare
        BufferedReader intxt = new BufferedReader(new FileReader(texfile
            + ".txt"));
        BufferedReader intesttxt = new BufferedReader(new FileReader(
            outfile));

        String linetxt, linetesttxt;
        while ((linetxt = intxt.readLine()) != null) {
            linetesttxt = intesttxt.readLine();
            Assert.assertEquals(linetxt, linetesttxt);
        }
        intxt.close();
        intesttxt.close();
    }
}