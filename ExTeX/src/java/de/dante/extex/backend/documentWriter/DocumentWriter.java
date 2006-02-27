/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend.documentWriter;

import java.io.IOException;

import de.dante.extex.typesetter.type.page.Page;
import de.dante.util.exception.GeneralException;

/**
 * This is the interface to the back-end of the system. The document has to be
 * written to an output stream. Certain information can be acquired before and
 * after the production of the output.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface DocumentWriter {

    /**
     * Getter for the extension associated with this kind of output. For
     * instance <tt>pdf</tt> is the expected value for PDF files and
     * <tt>dvi</tt> is the expected value for DVI files.
     *
     * @return the appropriate extension for file names
     */
    String getExtension();

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
     * @throws GeneralException in case of a general exception<br>
     *  especially<br>
     *  DocumentWriterException in case of an error
     * @throws IOException in case of an IO exception
     */
    int shipout(Page page) throws GeneralException, IOException;

    /**
     * This method is invoked upon the end of the processing.
     *
     * @throws GeneralException in case of a general exception<br>
     *  especially<br>
     *  DocumentWriterException in case of an error
     * @throws IOException in case of an IO exception
     */
    void close() throws GeneralException, IOException;

    /**
     * Setter for a named parameter.
     * Parameters are a general mechanism to influence the behavior of the
     * document writer. Any parameter not known by the document writer has to
     * be ignored.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    void setParameter(String name, String value);

}