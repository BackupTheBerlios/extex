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

package de.dante.extex.backend.pageFilter;

import de.dante.extex.backend.exception.BackendException;
import de.dante.extex.typesetter.type.page.Page;

/**
 * A page pipe describes the ability to process a node list &ndash; resulting
 * in a new node list.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface PagePipe {

    /**
     * This method is invoked upon the end of the processing.
     *
     * @throws DocumentWriterException in case of an error
     * @throws BackendException in case of a back-end error
     * @throws IOException in case of an IO error
     */
    void close() throws BackendException;

    /**
     * This is the entry point for the document writer. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled. Thus all information should be present to place the
     * ink on the paper.
     *
     * @param nodes the nodes to send
     *
     * @throws BackendException in case of an error
     */
    void shipout(Page nodes) throws BackendException;

    /**
     * Setter for the output node pipe.
     *
     * @param out the output node pipe
     */
    void setOutput(PagePipe out);

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
