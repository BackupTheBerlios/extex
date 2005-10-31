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
import de.dante.util.exception.GeneralException;

/**
 * This is a abstract base class for DocumentWriters.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public abstract class AbstractDocumentWriter
        implements
            DocumentWriter,
            MultipleDocumentStream {

    /**
     * The field <tt>pages</tt> contains the number of pages already written.
     */
    private int pages = 0;

    /**
     * The field <tt>parameter</tt> contains the map for parameters.
     */
    protected Map parameter = new HashMap();

    /**
     * The field <tt>writerFactory</tt> contains the output stream factory.
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
     * Acquire a new output stream.
     *
     * @param type the type for the reference to the configuration file
     *
     * @return the new output stream
     *
     * @throws DocumentWriterException in case of an error
     */
    protected OutputStream newOutputStream(final String type)
            throws DocumentWriterException {

        return writerFactory.getOutputStream(type);
    }

    /**
     * @see de.dante.extex.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory factory) {

        this.writerFactory = factory;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        parameter.put(name, value);
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public final void shipout(final NodeList nodes)
            throws GeneralException,
                IOException {

        Dimen width = new Dimen(Dimen.ONE_INCH);
        width.multiply(2100, 254); // A4 paper
        Dimen height = new Dimen(Dimen.ONE_INCH);
        height.multiply(2970, 254); // A4 paper

        pages += shipout(nodes, width, height);
    }

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled. Thus all information should be present to place the
     * ink on the paper.
     *
     * @param nodes the nodes to put on the paper
     * @param width the width of the page
     * @param height the height of the page
     *
     * @return the number of pages shipped out in this step. This is usually 1.
     *  But it can also be 0 if the page is skipped or a greater number is the
     *  page is split.
     *
     * @throws IOException in case of an IO error
     * @throws GeneralException in case of another error
     */
    protected abstract int shipout(final NodeList nodes, final Dimen width,
            final Dimen height) throws GeneralException, IOException;
}
