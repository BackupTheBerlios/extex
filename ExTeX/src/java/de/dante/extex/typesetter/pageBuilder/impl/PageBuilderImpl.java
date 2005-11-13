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

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.OutputRoutine;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.VerticalListNode;
import de.dante.extex.typesetter.type.page.Page;
import de.dante.extex.typesetter.type.page.PageFactory;
import de.dante.util.exception.GeneralException;

/**
 * This is a first reference implementation of a page builder.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.21 $
 */
public class PageBuilderImpl implements PageBuilder {

    /**
     * The field <tt>context</tt> contains the interpreter context.
     */
    private Context context = null;

    /**
     * The field <tt>documentWriter</tt> contains the document writer to receive
     * the pages.
     */
    private DocumentWriter documentWriter = null;

    /**
     * The field <tt>options</tt> contains the options to control the behaviour.
     */
    private TypesetterOptions options = null;

    /**
     * The field <tt>output</tt> contains the output routine.
     */
    private OutputRoutine outputRoutine = null;

    /**
     * The field <tt>pageFactory</tt> contains the ...
     */
    private PageFactory pageFactory = new PageFactory();

    /**
     * Creates a new object.
     */
    public PageBuilderImpl() {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#close()
     */
    public void close() throws TypesetterException {

        try {
            documentWriter.close();
        } catch (IOException e) {
            throw new TypesetterException(e);
        } catch (GeneralException e) {
            throw new TypesetterException(e);
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
    public void flush(final NodeList nodes) throws TypesetterException {

        if (nodes.size() > 0) {
            Page page = pageFactory.newInstance(nodes, context);
            try {
                if (this.outputRoutine != null) {
                    this.outputRoutine.output(page, documentWriter);
                } else {
                    documentWriter.shipout(page);
                }
                nodes.clear();
            } catch (IOException e) {
                throw new TypesetterException(e);
            } catch (GeneralException e) {
                throw new TypesetterException(e);
            }
        }
    }

    /**
     * This is the entry point for the page builder. Here it receives a
     * complete node list to be sent to the output writer. It can be assumed
     * that all values for width, height, and depth of the node lists are
     * properly filled.
     *
     * @param nodes the nodes to send
     *
     * @throws TypesetterException in case of an error
     *
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#inspectAndBuild(
     *      VerticalNodeList)
     */
    public void inspectAndBuild(final VerticalListNode nodes)
            throws TypesetterException {

        Dimen d = nodes.getVerticalSize();
        if (d.ge(options.getDimenOption("vsize"))) {

            flush(nodes);
        }
    }

    /**
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#setContext(
     *      de.dante.extex.interpreter.context.Context)
     */
    public void setContext(final Context context) {

        this.context = context;
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
     * Setter for options.
     *
     * @param options the options to set
     */
    public void setOptions(final TypesetterOptions options) {

        this.options = options;
    }

    /**
     * @see de.dante.extex.typesetter.pageBuilder.PageBuilder#setOutputRoutine(
     *      de.dante.extex.typesetter.OutputRoutine)
     */
    public void setOutputRoutine(final OutputRoutine output) {

        this.outputRoutine = output;
    }

}
