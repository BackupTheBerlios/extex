/*
 * Copyright (C) 2004 Gerd Neugebauer
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
package de.dante.test;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import junit.framework.TestCase;
import de.dante.extex.ExTeX;
import de.dante.extex.logging.LogFormatter;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class ExTeXLauncher extends TestCase {

    /**
     * Creates a new object.
     *
     * @param arg ...
     */
    public ExTeXLauncher(final String arg) {
        super(arg);
    }

    /**
     * ...
     *
     * @param code ...
     *
     * @throws Exception in case of an error
     */
    public void runCode(final String code, final String log, final String out)
            throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.code", code);
        properties.setProperty("extex.output", "text");
        properties.setProperty("extex.file", "");
        properties.setProperty("extex.interaction", "batchmode");

        ExTeX main = new ExTeX(properties);

        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(bytes, new LogFormatter());
        logger.addHandler(handler);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        main.setOutStream(stream);

        main.run();

        handler.close();
        logger.removeHandler(handler);
        if (log != null) {
            assertEquals(log, bytes.toString());
        }
        if (out != null) {
            assertEquals(out, stream.toString());
        }
    }

    /**
     * ...
     *
     * @param file the name of the file to read from
     *
     * @return the contents of the log file
     *
     * @throws Exception in case of an error
     */
    public String runFile(final String file) throws Exception {

        Properties properties = System.getProperties();
        properties.setProperty("extex.code", "");
        properties.setProperty("extex.output", "text");
        properties.setProperty("extex.file", file);
        properties.setProperty("extex.jobname", file);
        properties.setProperty("extex.interaction", "batchmode");

        ExTeX main = new ExTeX(properties);

        Logger logger = Logger.getLogger("test");
        logger.setUseParentHandlers(false);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Handler handler = new StreamHandler(bytes, new LogFormatter());
        logger.addHandler(handler);
        main.setLogger(logger);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        main.setOutStream(stream);

        main.run();

        handler.close();
        logger.removeHandler(handler);
        return bytes.toString();
   }

}