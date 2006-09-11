/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.backend.BackendDriver;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.OutputRoutine;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.page.PageFactory;

/**
 * This interface describes the capabilities of a page builder.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.16 $
 */
public interface PageBuilder {

    /**
     * Close the page builder. Any material left should be processed now since
     * it can not be expected that the page builder is invoked later in any way.
     *
     * @throws TypesetterException in case of an error
     */
    void close() throws TypesetterException;

    /**
     * This method is used when the page builder has received its last nodes.
     * It indicates that now the pages should be written out.
     * <p>
     * Nevertheless some ship-outs might come afterwards.
     * </p>
     *
     * @param nodes the nodes to send
     * @param typesetter the typesetter
     *
     * @throws TypesetterException in case of an error
     */
    void flush(NodeList nodes, Typesetter typesetter)
            throws TypesetterException;

    /**
     * This method is used when the page builder has received its last nodes.
     * It indicates that now the pages should be written out.
     * <p>
     * Nevertheless some ship-outs might come afterwards.
     * </p>
     *
     * @param nodes the nodes to send
     * @param typesetter the typesetter
     *
     * @throws TypesetterException in case of an error
     */
    void shipout(NodeList nodes, Typesetter typesetter)
            throws TypesetterException;

    /**
     * This is the entry point for the page builder. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled. The nodes might be sent partially or in total. The nodes
     * sent to the document writer are removed from the NodeList.
     *
     * @param nodes the nodes to send
     * @param typesetter the typesetter
     *
     * @throws TypesetterException in case of an error
     */
    void inspectAndBuild(VerticalListNode nodes, Typesetter typesetter)
            throws TypesetterException;

    /**
     * Setter for the back-end.
     * This has to be provided before the page builder can be active.
     *
     * @param backend the new document writer to use
     */
    void setBackend(BackendDriver backend);

    /**
     * Setter for the interpreter context
     *
     * @param context the interpreter context
     *
     * @throws TypesetterException in case of an error
     */
    void setContext(Context context) throws TypesetterException;

    /**
     * Setter for options.
     *
     * @param options the options to set
     */
    void setOptions(TypesetterOptions options);

    /**
     * Setter for the output routine.
     *
     * @param output the output routine
     */
    void setOutputRoutine(OutputRoutine output);

    /**
     * Setter for the page factory.
     *
     * @param factory the page factory
     */
    void setPageFactory(PageFactory factory);

}
