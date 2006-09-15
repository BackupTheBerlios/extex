/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.loader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import de.dante.extex.interpreter.context.Context;

/**
 * This class writes and reads format files.
 * <p>
 * Format files contains the information needed to continue processing some
 * input. This means that essentially the context is stored.
 * </p>
 * <h3>The Format File</h3>
 * <p>
 * The format file contains certain information in a fixed sequence:
 * <dl>
 *  <dd>The magic line</dd>
 *  <dt>This line is ended by a newline character and is used on Unix to
 *   make the format executable.
 *  </dt>
 *  <dd>The format identifier</dd>
 *  <dt>This string is stored as Java object and is used to distinguish
 *   formats from other types of files.
 *  </dt>
 *  <dd>The format version</dd>
 *  <dt>This Java String contains the version number of the file format.
 *   It is used to detect incompatible implementations of formats.
 *  </dt>
 *  <dd>The Context</dd>
 *  <dt>This Java Object contains the interpreter context with all status
 *   information.
 *  </dt>
 * </dl>
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class SerialLoader {

    /**
     * The field <tt>MAGIC_LINE</tt> contains the first line of a format file.
     */
    private static final String MAGIC_LINE = "#!extex -run-format\n";

    /**
     * The constant <tt>FORMAT_VERSION</tt> contains the version number of the
     * format file.
     */
    private static final String FORMAT_VERSION = "1.0";

    /**
     * The field <tt>FORMAT_ID</tt> contains the id string stored in the
     * format file.
     */
    private static final String FORMAT_ID = "ExTeX format";

    /**
     * Creates a new object.
     */
    public SerialLoader() {

        super();
    }

    /**
     * Load the interpreter context from an input stream.
     *
     * @param stream the input stream to read the context from
     *
     * @return the context read
     *
     * @throws LoaderException in case of an format error during the read
     * @throws IOException in case of an IO error
     */
    public Context load(final InputStream stream)
            throws IOException,
                LoaderException {

        InputStream inStream = new BufferedInputStream(stream);

        for (int c = inStream.read(); c != '\n'; c = inStream.read()) {
            if (c < 0) {
                throw new LoaderException("EOF");
            }
        }

        ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(
                inStream));
        Context context;
        try {
            String id = (String) in.readObject();
            if (!id.equals(FORMAT_ID)) {
                throw new LoaderException(id);
            }

            String version = (String) in.readObject();
            if (!version.equals(FORMAT_VERSION)) {
                throw new LoaderVersionException(version);
            }
            context = (Context) in.readObject();
        } catch (ClassCastException e) {
            throw new LoaderClassCastException(e);
        } catch (ClassNotFoundException e) {
            throw new LoaderClassNotFoundException(e);
        } finally {
            in.close();
            inStream.close();
        }
        return context;
    }

    /**
     * Write the format information to an output stream.
     *
     * @param stream the output stream to write the context to
     * @param jobname the name of the job currently processed
     * @param context the interpreter context to store
     *
     * @throws IOException in case of an IO error
     */
    public void save(final OutputStream stream, final String jobname,
            final Context context) throws IOException {

        OutputStream outStream = new BufferedOutputStream(stream);

        outStream.write(MAGIC_LINE.getBytes());

        ObjectOutputStream os = new ObjectOutputStream(new GZIPOutputStream(
                outStream));
        os.writeObject(FORMAT_ID);
        os.writeObject(FORMAT_VERSION);
        os.writeObject(context);
        //@see "TeX -- The Program [1329]"
        os.close();
        outStream.close();

    }

}