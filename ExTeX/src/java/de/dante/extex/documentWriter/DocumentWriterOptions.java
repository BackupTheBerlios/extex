/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.type.count.FixedCount;
import de.dante.extex.interpreter.type.dimen.FixedDimen;


/**
 * This interface describes the possibilities of the DocumentWriter to access
 * its options. These options are dynamically linked to the Context of the
 * processor. Only read access is provided.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface DocumentWriterOptions {

    /**
     * Getter for a count register.
     *
     * @param name the name of the register
     *
     * @return the content of the count register
     */
    FixedCount getCountOption(String name);

    /**
     * Getter for a dimen register.
     *
     * @param name the name of the register
     *
     * @return the content of the dimen register
     */
    FixedDimen getDimenOption(String name);

    /**
     * Getter for the magnification of the document.
     *
     * @return the magnifactikon in sp.
     */
    long getMagnification();

}
