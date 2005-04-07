/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.listMaker.ListManager;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNodeFactory;

/**
 * This interface describes the capabilities of a typesetter.
 * The typesetter is a contaner for a stack of list makers which perform the
 * task of assembling node lists of the appropriate type and structure.
 * The typesetter acts as proxy. Most requests are simply forwarded to the
 * current list maker.
 *
 * @see "TeX -- The Program [211]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.24 $
 */
public interface Typesetter extends ListMaker {

    /**
     * Clear the internal state about shipouts.
     * The shipout mark is reset to <code>false</code>.
     *
     * @see #isShipoutMark()
     */
    void clearShipoutMark();

    /**
     * Instructs the typesetter to perform any actions necessary for cleaning up
     * everything at the end of processing. This should involve a shipout of
     * any material still left unprocessed.
     *
     * @throws TypesetterException in case of an error
     */
    void finish() throws TypesetterException;

    /**
     * Getter for the CharNodeFactory.
     *
     * @return the character node factory
     */
    CharNodeFactory getCharNodeFactory();

    /**
     * Getter for the current list maker..
     *
     * @return the top list maker or <code>null</code> if the stack is empty
     */
    ListMaker getListMaker();

    /**
     * Getter for the manager of the list maker stack.
     *
     * @return the manager
     */
    ListManager getManager();

    /**
     * Query the shipout mark.
     * The shipout mark is an internal state which records whether or not the
     * shipout method has been called recently. This method can be used to
     * get the current state.
     * The method {@link #clearShipoutMark() clearShipoutMark()} can be used to
     * reset the shipout mark to <code>false</code>.
     * Initially the shipout mark is <code>false</code>.
     *
     * @return <code>true</code> iff there has been an invocation to the method
     *  {@link #shipout(NodeList) shipout()} since the last clearing
     * @see #clearShipoutMark()
     */
    boolean isShipoutMark();

    /**
     * Open a new list maker and put it in the top of the stack as current
     * box.
     *
     * @param listMaker the list maker
     * @throws TypesetterException TODO
     */
    void push(ListMaker listMaker) throws TypesetterException;

    /**
     * Setter for the document writer.
     * The document writer is addressed whenever a complete page has to be
     * shipped out.
     *
     * @param doc the new document writer
     */
    void setDocumentWriter(DocumentWriter doc);

    /**
     * Setter for the typesetter specific options.
     *
     * @param options the options to use
     */
    void setOptions(TypesetterOptions options);

    /**
     * Setter for the output routine.
     *
     * @param output the output routine
     */
    void setOutputRoutine(OutputRoutine output);

    /**
     * Setter for the page builder.
     *
     * @param pageBuilder the page builder to set.
     */
    void setPageBuilder(PageBuilder pageBuilder);

    /**
     * Setter for the paragraph builder.
     *
     * @param paragraphBuilder the paragraph builder to set.
     */
    void setParagraphBuilder(ParagraphBuilder paragraphBuilder);

    /**
     * Send a list of nodes to the document writer.
     * As a side effect the shipout mark is set.
     *
     * @param nodes the nodes to send to the typesetter
     *
     * @throws TypesetterException in case of an error
     *
     * @see #clearShipoutMark()
     */
    void shipout(NodeList nodes) throws TypesetterException;
}