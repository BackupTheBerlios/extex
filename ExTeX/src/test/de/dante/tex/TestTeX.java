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

package de.dante.tex;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import junit.framework.Assert;
import de.dante.extex.ExTeX;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.InterpreterFactory;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.main.errorHandler.editHandler.EditHandler;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationFactory;

/**
 * Test for ExTeX.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.10 $
 */
public final class TestTeX {

    private static class AssertFailErrorHandler implements ErrorHandler {

        public boolean handleError(final GeneralException e, final Token token,
                final TokenSource source, final Context context) {

            Assert.fail("error in tex document");
            return false; // not reached
        }

        public void setEditHandler(final EditHandler editHandler) {

        }
    }

    private static ErrorHandler errorHandler = new AssertFailErrorHandler();

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
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Properties pro = System.getProperties();
        ExTeX extex = new ExTeX(pro, ".extex");
        pro.setProperty("extex.output", "dump");
        pro.setProperty("extex.file", texfile);
        pro.setProperty("extex.jobname", texfile);
        // BATCHMODE
        // TODO: handle errors??? (TE)
        pro.setProperty("extex.interaction", "0");
        extex.setErrorHandler(errorHandler);
        extex.setOutStream(output);

        extex.run();

        // compare

        BufferedReader intesttxt = new BufferedReader(new FileReader(outfile));
        Reader stringReader = new StringReader(output.toString());
        BufferedReader intxt = new BufferedReader(stringReader);

        String linetxt, linetesttxt;
        while ((linetxt = intxt.readLine()) != null) {
            linetesttxt = intesttxt.readLine().toString();

            Assert.assertEquals(linetesttxt, linetxt);
        }
        Assert.assertTrue(!intesttxt.ready());
        intxt.close();
        intesttxt.close();
    }

    public static void test(final String basename) throws Exception {

        test(basename, "src/test/data/" + basename + ".testtxt");
    }

    /**
     * Make an <code>Interpreter</code>.
     *
     * @param configurationFile configuration file for ExTeX
     * @return an <code>Interpreter</code>
     * @exception Exception if an error occurs
     */
    public static Interpreter makeInterpreter(String configurationFile)
            throws Exception {

        Configuration config = new ConfigurationFactory().newInstance("config/"
                + configurationFile);
        Configuration intcfg = config.getConfiguration("Interpreter");
        InterpreterFactory intf = new InterpreterFactory();

        intf.configure(intcfg);

        return intf.newInstance();
    }

    /**
     * Make an <code>Interpreter</code>.
     *
     * @return an <code>Interpreter</code>
     * @exception Exception if an error occurs
     */
    public static Interpreter makeInterpreter() throws Exception {

        return makeInterpreter("extex.xml");
    }
}