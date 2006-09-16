/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.outputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;

/**
 * The trivial output stream factory is not configurable. It just creates files
 * in the current directory.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class TrivialOutputFactory implements OutputStreamFactory {

    /**
     * The field <tt>extension</tt> contains the default extension used when
     * type is <code>null</code>.
     */
    private String extension = null;

    /**
     * The field <tt>observers</tt> contains the list of registered observers.
     */
    private List observers = null;

    /**
     * Creates a new object.
     */
    public TrivialOutputFactory() {

        super();
    }

    /**
     * @see de.dante.extex.backend.outputStream.OutputStreamFactory#getOutputStream(
     *      java.lang.String, java.lang.String)
     */
    public OutputStream getOutputStream(final String name, final String type)
            throws DocumentWriterException {

        String ext = (type != null ? type : extension);
        File file = new File(".", name + "." + ext);
        NamedOutputStream stream = null;
        try {
            stream = new NamedOutputStream(file.toString(),
                    new BufferedOutputStream(new FileOutputStream(file)));
        } catch (FileNotFoundException e) {
            throw new DocumentWriterException(e);
        }
        if (stream != null && observers != null) {
            int size = observers.size();
            for (int i = 0; i < size; i++) {
                ((OutputStreamObserver) observers.get(i)).update(name, type,
                        stream);
            }
        }
        return stream;
    }

    /**
     * @see de.dante.extex.backend.outputStream.OutputStreamFactory#register(
     *      de.dante.extex.backend.outputStream.OutputStreamObserver)
     */
    public void register(final OutputStreamObserver observer) {

        if (observers == null) {
            observers = new ArrayList();
        }
        observers.add(observer);
    }

    /**
     * @see de.dante.extex.backend.outputStream.OutputStreamFactory#setExtension(
     *      java.lang.String)
     */
    public void setExtension(final String extension) {

        this.extension = extension;
    }

}
