/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.pageBuilder.impl;

import java.io.IOException;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.util.GeneralException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class PageBuilderImpl implements PageBuilder {

    /**
     * The field <tt>documentWriter</tt> contains the document writer to receive
     * the pages.
     */
    private DocumentWriter documentWriter = null;

    /**
     * The field <tt>vlist</tt> contains the accumulated material.
     */
    private NodeList vlist;

    /**
     * Creates a new object.
     */
    public PageBuilderImpl() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#close()
     */
    public void close() throws GeneralException {

        try {
            documentWriter.close();
        } catch (IOException e) {
            throw new GeneralException(e);
        }

    }

    /**
     * This method is used when the page builder has received its last nodes.
     * It indicates that now the pages should be written out.
     * <p>
     * Nevertheless some shipouts might come afterwards.
     * </p>
     *
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#flush()
     */
    public void flush(final NodeList nodes) throws GeneralException {

        try {
            this.documentWriter.shipout(nodes);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    /**
     * Setter for the document writer.
     * This has to be provided before the page builder can be active.
     *
     * @param docWriter the new document writer to use
     *
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#setDocumentWriter(
     *      de.dante.extex.documentWriter.DocumentWriter)
     */
    public void setDocumentWriter(final DocumentWriter docWriter) {

        this.documentWriter = docWriter;
    }

    /**
     * This is the entry point for the page builder. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled.
     *
     * @param nodes the nodes to send
     *
     * @throws GeneralException in case of an error
     *
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#inspectAndBuild(
     *      de.dante.extex.typesetter.NodeList)
     */
    public void inspectAndBuild(final NodeList nodes) throws GeneralException {

        try {
            this.documentWriter.shipout(nodes);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }
}