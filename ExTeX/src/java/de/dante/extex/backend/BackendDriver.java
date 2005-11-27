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

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.exception.BackendException;
import de.dante.extex.backend.pageFilter.PagePipe;
import de.dante.extex.typesetter.type.page.Page;

/**
 * This interface describes a back-end as extension to a DocumentWriter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface BackendDriver {

    /**
     * Adder for a processor.
     *
     * @param processor the processor to append
     */
    void add(PagePipe processor);

    /**
     * This method is invoked upon the end of the processing.
     *
     * @throws DocumentWriterException in case of an error
     */
    void close() throws BackendException;

    /**
     * Getter for the document writer.
     *
     * @return the document writer
     */
    DocumentWriter getDocumentWriter();

    /**
     * Getter for the extension associated with this kind of output. For
     * instance <tt>pdf</tt> is the expected value for PDF files and
     * <tt>dvi</tt> is the expected value for DVI files.
     *
     * @return the appropriate extension for file names
     */
    String getExtension();

    /**
     * Getter for the number of pages already produced.
     *
     * @return the number of pages already shipped out
     */
    int getPages();

    /**
     * Setter for the document writer.
     *
     * @param docWriter the document writer
     */
    void setDocumentWriter(DocumentWriter docWriter);

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled. Thus all information should be present to place the
     * ink on the paper.
     *
     * @param page the page to send
     *
     * @return returns the number of pages shipped
     *
     * @throws BackendException in case of an error
     */
    int shipout(Page page) throws BackendException;

}
