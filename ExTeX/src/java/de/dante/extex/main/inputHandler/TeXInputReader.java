/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main.inputHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.interaction.Interaction;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This Reader gets the characters from <tt>System.in</tt> but presents a
 * prompt before each line of input.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class TeXInputReader extends Reader {

    /**
     * The field <tt>logger</tt> contains the logger to write output to.
     */
    private Logger logger;

    /**
     * The field <tt>prompt</tt> contains the cached prompt to be shown before
     * each line of input.
     */
    private String prompt;

    /**
     * The field <tt>reader</tt> contains the reader to do the real job.
     */
    private Reader reader;

    /**
     * The field <tt>showPrompt</tt> contains the indicator that the prompt
     * needs to be shown next time.
     */
    private boolean showPrompt = true;

    /**
     * The field <tt>interpreter</tt> contains the interpreter.
     */
    private Interpreter interpreter;

    /**
     * Creates a new object.
     *
     * @param logger the logger to write to
     * @param charset the character set to use
     * @param interpreter the interpreter
     *
     * @throws UnsupportedEncodingException in case that the encoding is not
     *  known
     */
    public TeXInputReader(final Logger logger, final String charset,
            final Interpreter interpreter) throws UnsupportedEncodingException {

        super();
        this.logger = logger;
        reader = new InputStreamReader(System.in, charset);
        Localizer localizer = LocalizerFactory
                .getLocalizer(TeXInputReader.class.getName());
        prompt = localizer.format("TTP.PromptInput");
        this.interpreter = interpreter;
    }

    /**
     * @see java.io.Reader#close()
     */
    public void close() throws IOException {

        reader.close();
    }

    /**
     * @see java.io.Reader#markSupported()
     */
    public boolean markSupported() {

        return this.reader.markSupported();
    }

    /**
     * @see java.io.Reader#read()
     */
    public int read() throws IOException {

        return this.reader.read();
    }

    /**
     * @see java.io.Reader#read(char[])
     */
    public int read(final char[] arg0) throws IOException {

        return this.reader.read(arg0);
    }

    /**
     * @see java.io.Reader#read(char[], int, int)
     */
    public int read(final char[] buffer, final int startIndex, final int arg2)
            throws IOException {

        /*
//        if (interpreter.getContext().getInteraction().getIndex() != Interaction.ERRORSTOPMODE
//                .getIndex()) {
//            return -1;
//        }
 * 
 */
        if (showPrompt) {
            logger.severe(prompt);
            showPrompt = false;
        }
        int ret = reader.read(buffer, startIndex, arg2);
        for (int i = 0; i < ret; i++) {
            char c = buffer[startIndex + i];
            logger.fine(Character.toString(c));
            if (c == '\n') {
                showPrompt = true;
            }
        }
        return ret;
    }

    /**
     * @see java.io.Reader#ready()
     */
    public boolean ready() throws IOException {

        return this.reader.ready();
    }

    /**
     * @see java.io.Reader#reset()
     */
    public void reset() throws IOException {

        this.reader.reset();
    }

    /**
     * @see java.io.Reader#skip(long)
     */
    public long skip(final long arg0) throws IOException {

        return this.reader.skip(arg0);
    }

}
