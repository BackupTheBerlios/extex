/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context;

import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This interface describes the container for marks of an interpreter
 * context.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface ContextMark {

    /**
     * This method clears all split marks.
     */
    void clearSplitMarks();

    /**
     * Getter for the bottom mark.
     *
     * @param name the name of the mark
     *
     * @return the bottom mark
     */
    Tokens getBottomMark(Object name);

    /**
     * Getter for the first mark.
     *
     * @param name the name of the mark
     *
     * @return the first mark
     */
    Tokens getFirstMark(Object name);

    /**
     * Getter for the split bottom mark.
     *
     * @param name the name of the mark
     *
     * @return the split bottom mark
     */
    Tokens getSplitBottomMark(Object name);

    /**
     * Getter for the split first mark.
     *
     * @param name the name of the mark
     *
     * @return the split first mark
     */
    Tokens getSplitFirstMark(Object name);

    /**
     * Getter for the top mark.
     *
     * @param name the name of the mark
     *
     * @return the top mark
     */
    Tokens getTopMark(Object name);

    /**
     * Setter for a mark.
     * The information for first mark and top mark are updated if necessary.
     *
     * @param name the name of the mark
     * @param mark the vale of the mark
     */
    void setMark(Object name, Tokens mark);

    /**
     * Setter for a split mark.
     * The information for first mark and top mark are updated is necessary.
     *
     * @param name the name of the mark
     * @param mark the vale of the mark
     */
    void setSplitMark(Object name, Tokens mark);

    /**
     * This method indicated that a new page is started.
     * The values of first mark, bottom mark, and top mark should be updated
     * properly.
     */
    void startMarks();

}
