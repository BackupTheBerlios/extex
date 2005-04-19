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

package de.dante.extex.typesetter;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.GeneralException;

/**
 * This interface describes the functionality provided by an output routine.
 * A output routne is not necessarily implemented by a program in the macro
 * language. Instead might as well be mplemented in Java or some other extension
 * language.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface OutputRoutine {

    /**
     * The output function is invoked to process a vertical list and put the
     * material on the page. In fact it should find its way to the document
     * writer &ndash; either immediately or later on.
     *
     * @param vlist the nodes to put onto the page
     * @param documentWriter the document writer to target the nodes to
     *
     * @throws GeneralException in case of an error
     */
    void output(NodeList vlist, DocumentWriter documentWriter)
            throws GeneralException;
}