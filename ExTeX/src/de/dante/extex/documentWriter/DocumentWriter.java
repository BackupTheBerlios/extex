/*
 * Copyright (C) 2003 Gerd Neugebauer
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
import java.io.Writer;

import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;

/**
 * This is the interface to the backend of the system. The document has to be
 * written to a output stream. Certain information can be acquired before and
 * after the production of the output.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface DocumentWriter {
    /**
     * Getter for the extension associated with this kind of output. For
     * instance <tt>.pdf</tt> is the expected value for PDF files and
     * <tt>.dvi</tt> is the expected value for DVI files.
     *
     * @return the appropriate filename extension
     */
    String getExtension();

    /**
     * Setter for the output stream.
     *
     * @param os the output stream
     */
    void setWriter(Writer writer);

    /**
     * Getter for the number of pages already produced.
     *
     * @return the number of pages already shipped out
     */
    int getPages();

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled. Thus all information should be present to place the
     * ink on the paper.
     *
     * @param nodes the nodes to send
     *
     * @throws GeneralException iun case of an error
     * @throws IOException in cayse that a writing operation fails
     */
    void shipout(NodeList nodes) throws GeneralException, IOException;

    /**
     * This method is invoked upon the end of the processing.
     *
     * @throws GeneralException in case of an error
     * @throws IOException in cayse that a writing operation fails
     */
    void close() throws GeneralException, IOException;
}
