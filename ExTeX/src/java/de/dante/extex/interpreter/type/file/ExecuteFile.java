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

package de.dante.extex.interpreter.type.file;

import java.io.IOException;
import java.util.logging.Logger;

import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is a class presenting itself as an OutFile which sends the tokens
 * written to it to the underlying operating system for the execution in a
 * shell.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ExecuteFile extends OutFile {

    /**
     * The field <tt>logger</tt> contains the logger for tracing and error
     * messages.
     */
    private transient Logger logger;

    /**
     * Creates a new object.
     *
     * @param logger the target Logger
     */
    public ExecuteFile(final Logger logger) {

        super(null);
        this.logger = logger;
    }

    /**
     * @see de.dante.extex.interpreter.type.file.OutFile#close()
     */
    public void close() throws IOException {

    }

    /**
     * @see de.dante.extex.interpreter.type.file.OutFile#isOpen()
     */
    public boolean isOpen() {

        return true;
    }

    /**
     * @see de.dante.extex.interpreter.type.file.OutFile#open()
     */
    public void open() {

    }

    /**
     * @see de.dante.extex.interpreter.type.file.OutFile#write(
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void write(final Tokens toks) throws IOException {

        Localizer localizer = LocalizerFactory.getLocalizer(ExecuteFile.class
                .getName());
        String command = toks.toText();
        logger.fine(localizer.format("Start.Message", command));
        Process process = Runtime.getRuntime().exec(command);
        try {
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.fine(localizer.format("End.Message", command));
            } else {
                logger.fine(localizer.format("Failed.Message", command, //
                        Integer.toString(exitCode)));
            }
        } catch (InterruptedException e) {
            logger.fine(localizer.format("Interrupted.Message", command));
        }
    }
}