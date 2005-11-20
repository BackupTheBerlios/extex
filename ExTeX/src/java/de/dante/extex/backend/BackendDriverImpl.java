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
 * @version $Revision: 1.2 $
 */
public class BackendDriverImpl
        implements
            BackendDriver,
            MultipleDocumentStream,
            Configurable {

    /**
     * The field <tt>pipe</tt> contains the elements of the pipe.
     */
    private NodePipe[] pipe = null;

    /**
     * The field <tt>documentWriter</tt> contains the ...
     */
    private DocumentWriter documentWriter = null;

    /**
     * Creates a new object.
     *
     * @param options
     */
    public BackendDriverImpl(final DocumentWriterOptions options) {

        super();
    }

    /**
     * The field <tt>pages</tt> contains the ...
     */
    private int pages = 0;

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#close()
     */
    public void close()
            throws DocumentWriterException,
                GeneralException,
                IOException {

        if (pipe != null) {
            for (int i = 0; i < pipe.length; i++) {
                pipe[i].close();
            }
        }
        documentWriter.close();
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return documentWriter.getExtension();
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return pages;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.backend.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory writerFactory) {

        // TODO gene: setOutputStreamFactory unimplemented
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        for (int i = 0; i < pipe.length; i++) {
            pipe[i].setParameter(name, value);
        }
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
     */
    public int shipout(final Page page)
            throws DocumentWriterException,
                GeneralException,
                IOException {

        Page p = page;

        if (pipe != null) {
            for (int i = 0; i < pipe.length; i++) {
                p = pipe[i].process(p);
                if (p == null) {
                    return 0;
                }
            }
        }
        int n = documentWriter.shipout(p);
        pages += n;
        return n;
    }

    /**
     * @see de.dante.extex.backend.BackendDriver#setDocumentWriter(
     *      de.dante.extex.backend.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter docWriter) {

        documentWriter = docWriter;
    }

}
