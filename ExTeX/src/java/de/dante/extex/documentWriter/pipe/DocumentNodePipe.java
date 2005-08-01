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

package de.dante.extex.documentWriter.pipe;

import java.io.IOException;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.documentWriter.MultipleDocumentStream;
import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.extex.documentWriter.exception.DocumentWriterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.configuration.Configurable;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class DocumentNodePipe
        implements
            DocumentWriter,
            MultipleDocumentStream,
            Configurable {

    private NodePipe[] pipe;

    /**
     * Creates a new object.
     *
     * @param options
     */
    public DocumentNodePipe(final DocumentWriterOptions options) {

        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close()
            throws DocumentWriterException,
                GeneralException,
                IOException {

        // TODO gene: close unimplemented

    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        // TODO gene: configure unimplemented

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        // TODO gene: getExtension unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        // TODO gene: getPages unimplemented
        return 0;
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

        // TODO gene: setParameter unimplemented

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.NodeList)
     */
    public void shipout(NodeList nodes)
            throws DocumentWriterException,
                GeneralException,
                IOException {

        // TODO gene: shipout unimplemented

    }

}
