/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.test;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.logging.LogFormatter;
import de.dante.extex.main.errorHandler.editHandler.EditHandler;
import de.dante.extex.main.exception.MainException;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * This base class for test cases handles all the nitty gritty details of
 * running an instance of ExTeX.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class ExTeXLauncher extends TestCase {

    /**
     * Inner class for the error handler.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.14 $
     */
    private class EHandler implements ErrorHandler {

        /**
         * The field <tt>logger</tt> contains the target logger.
         */
        private Logger logger;

        /**
         * Creates a new object.
         *
         * @param theLogger the target logger
         */
        public EHandler(final Logger theLogger) {

            super();
            this.logger = theLogger;
        }

        /**
         * @see de.dante.extex.interpreter.ErrorHandler#handleError(
         *      de.dante.util.GeneralException,
         *      de.dante.extex.scanner.Token,
         *      de.dante.extex.interpreter.TokenSource,
         *      de.dante.extex.interpreter.context.Context)
         */
        public boolean handleError(final GeneralException e, final Token token,
                final TokenSource source, final Context context)
                throws GeneralException {

            logger.log(Level.SEVERE, e.getLocalizedMessage());
            return false;
        }

        /**
         * @see de.dante.extex.interpreter.ErrorHandler#setEditHandler(
         *       de.dante.extex.main.errorHandler.editHandler.EditHandler)
         */
        public void setEditHandler(final EditHandler editHandler) {

        }
    }

    /**
     * Set some properties to default values. The properties set are:
     * <dl>
     * <dt><tt>extex.output</tt></dt><dd>Preset to <tt>text</tt></dd>
     * <dt><tt>extex.interaction</tt></dt><dd>Preset to <tt>batchmode</tt></dd>
     * <dt><tt>extex.fonts</tt></dt><dd>Preset to <tt>src/fonts</tt></dd>
     * </dl>
     *
     * @param properties to properties to adapt
     */
    private static void prepareProperties(final Properties properties) {

        provide(properties, "extex.output", "out");
        provide(properties, "extex.interaction", "batchmode");
        provide(properties, "extex.fonts", "src/font");
    }

    /**
     * Set a property if it has not been set yet.
     *
     * @param properties th properties o modify
     * @param name the name of the property
     * @param value the new value
     */
    private static void provide(final Properties properties, final String name,
            final String value) {

        if (properties.getProperty(name) == null) {
            properties.setProperty(name, value);
        }
    }

    /**
     * Creates a new object.
     *
     * @param arg the name
     */
    public ExTeXLauncher(final String arg) {

        super(arg);
    }

    /**
     * Run some code through ExTeX.
     *
     * @param properties the properties to start with
     * @param code the code to expand
     * @param log the expected output on the log stream
     * @param expect the expected output on the output stream
     *
     * @throws MainException in case of an error
     */
    public void runCode(final Properties properties, final String code,
            final String log, final String expect) throws MainException {

        prepareProperties(properties);
        properties.setProperty("extex.code", code);
        properties.setProperty("extex.file", "");

        ExTeX main = new ExTeX(properties);

        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.WARNING);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(byteStream, new LogFormatter());
        handler.setLevel(Level.WARNING);
        logger.addHandler(handler);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        main.setOutStream(stream);
        main.setErrorHandler(new EHandler(logger));
        main.setLogger(logger);

        try {
            main.run();
        } catch (MainException e) {
            // TODO gene: error handling unimplemented
        }

        handler.close();
        logger.removeHandler(handler);
        if (log != null) {
            assertEquals(log, byteStream.toString());
        }
        if (expect != null) {
            assertEquals(expect, stream.toString());
        }
    }

    /**
     * Run some code through ExTeX.
     *
     * @param code the code to expand
     * @param log the expected output on the log stream
     * @param expect the expected output on the output stream
     *
     * @throws Exception in case of an error
     */
    public void runCode(final String code, final String log, final String expect)
            throws Exception {

        runCode(System.getProperties(), code, log, expect);
    }

    /**
     * Run ExTeX on a file.
     *
     * @param file the name of the file to read from
     *
     * @return the contents of the log file
     *
     * @throws Exception in case of an error
     */
    public String runFile(final String file) throws Exception {

        Properties properties = System.getProperties();
        prepareProperties(properties);
        properties.setProperty("extex.code", "");
        properties.setProperty("extex.file", file);
        properties.setProperty("extex.jobname", file);

        ExTeX main = new ExTeX(properties);

        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(bytes, new LogFormatter());
        handler.setLevel(Level.WARNING);
        logger.addHandler(handler);
        main.setLogger(logger);
        main.setErrorHandler(new EHandler(logger));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        main.setOutStream(stream);

        main.run();

        handler.close();
        logger.removeHandler(handler);
        return bytes.toString();
    }

}