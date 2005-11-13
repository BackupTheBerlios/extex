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

package de.dante.extex.backend;

import java.io.IOException;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.MultipleDocumentStream;
import de.dante.extex.backend.documentWriter.OutputStreamFactory;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.backend.nodeFilter.NodePipe;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This document writer can be used to combine several components.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class BackendDriver
        implements
            DocumentWriter,
            MultipleDocumentStream,
            Configurable {

    /**
     * The field <tt>pipe</tt> contains the elements of the pipe.
     */
    private NodePipe[] pipe;

    /**
     * The field <tt>documentWriter</tt> contains the ...
     */
    private DocumentWriter documentWriter;

    /**
     * Creates a new object.
     *
     * @param options
     */
    public BackendDriver(final DocumentWriterOptions options) {

        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close()
            throws DocumentWriterException,
                GeneralException,
                IOException {

        for (int i = 0; i < pipe.length; i++) {
            pipe[i].close();
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return pipe[pipe.length - 1].getExtension();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return pipe[pipe.length - 1].getPages();
    }

    /**
     * @see de.dante.extex.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory writerFactory) {

        // TODO gene: setOutputStreamFactory unimplemented
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        for (int i = 0; i < pipe.length; i++) {
            pipe[i].setParameter(name, value);
        }
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public int shipout(Page page)
            throws DocumentWriterException,
                GeneralException,
                IOException {

        // TODO gene: shipout unimplemented
        return 1;
    }

}
