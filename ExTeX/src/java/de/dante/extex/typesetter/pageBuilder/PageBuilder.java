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

package de.dante.extex.typesetter.pageBuilder;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.Interpreter;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.util.GeneralException;

/**
 * This interface describes the capabilities of a page builder.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public interface PageBuilder {

    /**
     * Close the page builder. Any material left should be processed now since
     * it can not be expected that the page builder is invoked later in any way.
     *
     * @throws GeneralException in case of an error
     */
    void close() throws GeneralException;

    /**
     * This method is used when the page builder has received its last nodes.
     * It indicates that now the pages should be written out.
     * <p>
     * Nevertheless some shipouts might come afterwards.
     * </p>
     *
     * @param nodes the nodes to send
     *
     * @throws GeneralException in case of an error
     */
    void flush(NodeList nodes) throws GeneralException;

    /**
     * This is the entry point for the page builder. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled. The nodes might be sent partially or in total. The nodes
     * sent to the document writer are removed from the NodeList.
     *
     * @param nodes the nodes to send
     *
     * @throws GeneralException in case of an error
     */
    void inspectAndBuild(VerticalListNode nodes) throws GeneralException;

    /**
     * Setter for the interpreter context
     *
     * @param context the interpreter context
     */
    void setContext(Context context);

    /**
     * Setter for the document writer.
     * This has to be provided before the page builder can be active.
     *
     * @param docWriter the new document writer to use
     */
    void setDocumentWriter(DocumentWriter docWriter);

    /**
     * Setter for options.
     *
     * @param options the options to set
     */
    void setOptions(TypesetterOptions options);

    /**
     * Setter for the interpreter.
     *
     * @param interpreter the interpreter to be used for running the
     *  output routine in
     */
    void setInterpreter(Interpreter interpreter);
}