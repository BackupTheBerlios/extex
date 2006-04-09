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
import de.dante.extex.backend.exception.BackendException;
import de.dante.extex.backend.pageFilter.PagePipe;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This document writer can be used to combine several components.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class BackendDriverImpl
        implements
            BackendDriver,
            MultipleDocumentStream,
            Configurable {

    /**
     * This internal class acts as page counter as last element in the node pipe.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    private class Counter implements PagePipe {

        /**
         * @see de.dante.extex.backend.nodeFilter.NodePipe#close()
         */
        public void close() throws BackendException {

            try {
                documentWriter.close();
            } catch (GeneralException e) {
                new BackendException(e);
            } catch (IOException e) {
                new BackendException(e);
            }
            documentWriter = null;
        }

        /**
         * @see de.dante.extex.backend.nodeFilter.NodePipe#setNext(
         *      de.dante.extex.backend.nodeFilter.NodePipe)
         */
        public void setOutput(final PagePipe next) {

            throw new RuntimeException("this should not happen");
        }

        /**
         * @see de.dante.extex.backend.nodeFilter.NodePipe#setParameter(
         *      java.lang.String,
         *      java.lang.String)
         */
        public void setParameter(final String name, final String value) {

        }

        /**
         * @see de.dante.extex.backend.nodeFilter.NodePipe#process(
         *      de.dante.extex.typesetter.type.page.Page)
         */
        public void shipout(final Page nodes) throws BackendException {

            try {
                documentWriter.shipout(nodes);
            } catch (DocumentWriterException e) {
                throw e;
            } catch (GeneralException e) {
                throw new DocumentWriterException(e);
            } catch (IOException e) {
                throw new DocumentWriterException(e);
            }
            pages++;
        }

    }

    /**
     * The field <tt>counter</tt> contains the counter page pipe  which will
     * always be placed at the end of the of the pipe.
     */
    private PagePipe counter = new Counter();

    /**
     * The field <tt>documentWriter</tt> contains the document writer.
     */
    private DocumentWriter documentWriter = null;

    /**
     * The field <tt>pages</tt> contains the number of pages already sent to the
     * document writer.
     */
    private int pages = 0;

    /**
     * The field <tt>pipeFirst</tt> contains the elements of the pipe.
     */
    private PagePipe pipeFirst = counter;

    /**
     * The field <tt>pipeLast</tt> contains the last item in the pipe. Initially
     * it is the counter. If the pipe is longer this value is the last item
     * before the counter.
     */
    private PagePipe pipeLast = counter;

    /**
     * Creates a new object.
     *
     * @param options the options
     */
    public BackendDriverImpl(final DocumentWriterOptions options) {

        super();
    }

    /**
     * @see de.dante.extex.backend.BackendDriver#add(
     *      de.dante.extex.backend.pageFilter.PagePipe)
     */
    public void add(final PagePipe processor) {

        processor.setOutput(counter);

        if (pipeFirst == counter) {
            pipeFirst = processor;
        } else {
            pipeLast.setOutput(processor);
        }

        pipeLast = processor;
    }

    /**
     * @see de.dante.extex.backend.BackendDriver#close()
     */
    public void close() throws BackendException {

        pipeFirst.close();

        if (documentWriter != null) {
            try {
                documentWriter.close();
            } catch (GeneralException e) {
                throw new BackendException(e);
            } catch (IOException e) {
                throw new BackendException(e);
            }
            documentWriter = null;
        }
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.framework.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

    }

    /**
     * @see de.dante.extex.backend.BackendDriver#getDocumentWriter()
     */
    public DocumentWriter getDocumentWriter() {

        return documentWriter;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return documentWriter.getExtension();
    }

    /**
     * @see de.dante.extex.backend.BackendDriver#getPages()
     */
    public int getPages() {

        return pages;
    }

    /**
     * @see de.dante.extex.backend.BackendDriver#setDocumentWriter(
     *      de.dante.extex.backend.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter docWriter) {

        documentWriter = docWriter;
    }

    /**
     * @see de.dante.extex.backend.documentWriter.MultipleDocumentStream#setOutputStreamFactory(
     *      de.dante.extex.backend.documentWriter.OutputStreamFactory)
     */
    public void setOutputStreamFactory(final OutputStreamFactory writerFactory) {

        if (documentWriter instanceof MultipleDocumentStream) {
            ((MultipleDocumentStream) documentWriter)
                    .setOutputStreamFactory(writerFactory);
        }
    }

    /**
     * @see de.dante.extex.backend.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(final String name, final String value) {

        // ignored
    }

    /**
     * @see de.dante.extex.backend.BackendDriver#shipout(
     *      de.dante.extex.typesetter.type.page.Page)
     */
    public int shipout(final Page page) throws BackendException {

        int n = pages;
        pipeFirst.shipout(page);
        return pages - n;
    }

}
