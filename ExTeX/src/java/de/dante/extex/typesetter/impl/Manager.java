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
package de.dante.extex.typesetter.impl;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.interpreter.type.node.CharNodeFactory;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphBuilder;
import de.dante.util.GeneralException;

/**
 * Interface for the Manager of a typesetter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.11 $
 */
public interface Manager {

    /**
     * Discart to top of the stack of list makers.
     *
     * @throws GeneralException in case of an error
     */
    void pop() throws GeneralException;

    /**
     * Push a new element to the stack of list makers.
     *
     * @param listMaker the new element to push
     *
     * @throws GeneralException in case of an error
     */
    void push(ListMaker listMaker) throws GeneralException;

    /**
     * ...
     *
     * @throws GeneralException in case of an error
     */
    void endParagraph() throws GeneralException;

    /**
     * Getter for the char node factory.
     *
     * @return the char node factory
     */
    CharNodeFactory getCharNodeFactory();

    /**
     * Getter for the doument writer.
     *
     * @return the document writer
     */
    DocumentWriter getDocumentWriter();

    /**
     * Getter for the options object.
     *
     * @return the options
     */
    TypesetterOptions getOptions();

    /**
     * Getter for the ligature builder.
     *
     * @return the current ligature builder
     */
    LigatureBuilder getLigatureBuilder();

    /**
     * Getter for the current paragraph builder.
     *
     * @return the current paragraph builder
     */
    ParagraphBuilder getParagraphBuilder();
}
