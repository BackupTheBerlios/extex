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
package de.dante.extex.typesetter.pageBuilder;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;


/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface PageBuilder {
    
    /**
     * ...
     *
     */
    void close() throws GeneralException;

    /**
     * This method is used when the page builder has received its last nodes.
     * It indicates that now the pages should be written out.
     * <p>
     * Nevertheless some shipouts might come afterwards.
     * </p>
     */
    void flush() throws GeneralException;

    /**
     * Setter for the document writer.
     * This has to be provided before the page builder can be active.
     *
     * @param docWriter the new document writer to use
     */
    void setDocumentWriter(DocumentWriter docWriter);

    /**
     * This is the entry point for the page builder. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled.
     *
     * @param nodes the nodes to send
     *
     * @throws GeneralException in case of an error
     */
    void shipout(NodeList nodes) throws GeneralException;
}
