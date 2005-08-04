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

package de.dante.extex.documentWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.GeneralException;

/**
 * This is a abstract base class for DocumentWriters.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDocumentWriter
        implements
            DocumentWriter,
            MultipleDocumentStream {

    /**
     * The field <tt>pages</tt> contains the ...
     */
    private int pages = 0;

    /**
     * The field <tt>writerFactory</tt> contains the ...
     */
    private OutputStreamFactory writerFactory;

    /**
     * Creates a new object.
     */
    public AbstractDocumentWriter() {

        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return pages;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param type the type for the reference to the configuration file
     *
     * @return
     *
     * @throws DocumentWriterException in case of an error
     */
    protected OutputStream newOutputStream(final String type)
            throws DocumentWriterException {

        return writerFactory.getOutputStream(type);
    }

    /**
     * @see de.dante.extex.documentWriter.MultipleDocumentStream#setOutputStreamFactory(de.dante.extex.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory writerFactory) {

        this.writerFactory = writerFactory;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(de.dante.extex.typesetter.type.NodeList)
     */
    public final void shipout(final NodeList nodes)
            throws DocumentWriterException,
                GeneralException,
                IOException {

        Dimen width = new Dimen();
        Dimen height = new Dimen();

        if (shipout(nodes, width, height)) {
            pages++;
        }
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param nodes
     * @param width
     * @param height
     *
     * @return
     * @throws IOException
     * @throws GeneralException
     */
    protected abstract boolean shipout(final NodeList nodes, final Dimen width,
            final Dimen height) throws GeneralException, IOException;

    /**
     * The field <tt>parameter</tt> contains the map for parameters.
     */
    protected Map parameter = new HashMap();

    /**
     * Getter for a named parameter.
     *
     * @param name the name of the parameter
     *
     * @return the value of the parameter or <code>null</code> if none exists
     */
    protected String getParameter(final String name) {

        return (String) parameter.get(name);
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        parameter.put(name, value);
    }
}
