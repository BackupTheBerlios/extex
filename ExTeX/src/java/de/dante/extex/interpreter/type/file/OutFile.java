/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import de.dante.extex.i18n.PanicException;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.util.GeneralException;

/**
 * This class holds an output file onto which tokens can be wrtitten.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class OutFile implements Serializable {

    /**
     * The field <tt>file</tt> contains the file assigned to this instance.
     * If the value is <code>null</code> then it can never be opened.
     */
    private File file;

    /**
     * The field <tt>writer</tt> contains the real writer assigned to this
     * instance.
     */
    private transient Writer writer = null;

    /**
     * Creates a new object.
     *
     * @param name the file to write to
     */
    public OutFile(final File name) {

        super();
        this.file = name;
    }

    /**
     * Open the current file.
     *
     * @throws GeneralException in case of an error
     */
    public void open() throws GeneralException {

        if (file != null) {
            try {
                writer = new BufferedWriter(new FileWriter(file));
            } catch (FileNotFoundException e) {
                // ignored on purpose
            } catch (IOException e) {
                // ignored on purpose
            }
        }
    }

    /**
     * Close the current file.
     *
     * @throws IOException in case of an error
     */
    public void close() throws IOException {

        if (writer != null) {
            try {
                writer.close();
            } finally {
                writer = null;
            }
        }
    }

    /**
     * Check whether the output file is open.
     *
     * @return <code>true</code> iff the instance is open
     */
    public boolean isOpen() {

        return (null != writer);
    }

    /**
     * Write some tokens to the output writer.
     *
     * @param toks tokens to write
     *
     * @throws IOException in case of an error
     */
    public void write(final Tokens toks) throws IOException {

        if (writer == null) {
            return;
        }
        int len = toks.length();

        for (int i = 0; i < len; i++) {
            writer.write(toks.get(i).getValue());
        }
    }

}