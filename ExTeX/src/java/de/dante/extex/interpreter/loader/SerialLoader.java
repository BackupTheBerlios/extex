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

package de.dante.extex.interpreter.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import de.dante.extex.interpreter.context.Context;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class SerialLoader {

    /**
     * The constant <tt>FORMAT_VERSION</tt> contains the version number of the
     * format file.
     */
    private static final String FORMAT_VERSION = "ExTeX 1.0";

    /**
     * Creates a new object.
     *
     */
    public SerialLoader() {

        super();
    }

    /**
     * ...
     *
     * @param stream ...
     * @param jobname ...
     * @param context ...
     *
     * @throws IOException in case of an IO error
     */
    public void save(final OutputStream stream, final String jobname,
            final Context context) throws IOException {

        Calendar calendar = Calendar.getInstance();

        stream.write("#!extex -x\n".getBytes());
        ObjectOutputStream os = new ObjectOutputStream(stream);
        os.writeObject(FORMAT_VERSION);
        context.setId(jobname + " " + //
                calendar.get(Calendar.YEAR) + "."
                + calendar.get(Calendar.MONTH) + "."
                + calendar.get(Calendar.DAY_OF_MONTH));
        os.writeObject(context);
        //@see "TeX -- The Program [1329]"
        os.close();

    }

    /**
     * ...
     *
     * @param stream ...
     *
     * @return ...
     *
     * @throws LoaderException ...
     * @throws IOException ...
     */
    public Context load(final InputStream stream)
            throws IOException,
                LoaderException {

        for (int c = stream.read(); c != '\n'; c = stream.read()) {
            if (c < 0) {
                throw new LoaderException("EOF");
            }
        }

        ObjectInputStream in = new ObjectInputStream(stream);
        Context context;
        try {
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
        }
        return context;
    }

}