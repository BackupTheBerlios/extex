/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.impl.Manager;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.extex.typesetter.pageBuilder.PageBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.util.GeneralException;

/**
 * This interface describes the capabilities of a typesetter.
 *
 * @see "TeX -- The Program [211]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public interface Typesetter extends ListMaker {

    /**
     * Instructs the typesetter to perform any actions necessary for cleaning up
     * everything at the end of processing. This should involve a shipout of
     * any material still left unprocessed.
     *
     * @throws GeneralException in case of an error
     */
    void finish() throws GeneralException;

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
    Manager getManager();

    /**
     * Open a new list maker and put it in the top of the stack as current
     * box.
     *
     * @param listMaker the list maker
     */
    void push(ListMaker listMaker);

    /**
     * Setter for the document writer.
     * The document writer is addressed whenever a complete page has to be
     * shipped out.
     *
     * @param doc the new document writer
     */
    void setDocumentWriter(DocumentWriter doc);

    /**
     * Setter for the ligature builder.
     *
     * @param ligatureBuilder the ligature builder to set.
     */
    void setLigatureBuilder(LigatureBuilder ligatureBuilder);

    /**
     * Setter for the typesetter specific options.
     *
     * @param options the options to use
     */
    void setOptions(TypesetterOptions options);

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
     *
     * @param nodes the nodes to send to the typesetter
     *
     * @throws GeneralException in case of an error
     */
    void shipout(NodeList nodes) throws GeneralException;

}
