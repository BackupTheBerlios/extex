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

package de.dante.extex.typesetter.impl;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.context.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class HAlignListMaker extends RestrictedHorizontalListMaker
        implements
            AlignmentList {

    /**
     * The field <tt>col</tt> contains the indicator for the current column.
     */
    private int col = 0;

    /**
     * The field <tt>line</tt> contains the ...
     */
    private Object[] line;

    /**
     * The field <tt>preamble</tt> contains the ...
     */
    private List preamble;

    /**
     * The field <tt>rows</tt> contains the ...
     */
    private List rows = new ArrayList();

    /**
     * Creates a new object.
     *
     * @param manager the manager
     * @param thePreamble the list of preamble items
     * @param width the target width or <code>null</code> if the natural width
     *  should be used
     */
    public HAlignListMaker(final Manager manager, final List thePreamble,
            final Dimen width) {

        super(manager);
        preamble = thePreamble;
        line = new Object[preamble.size()];
    }

    /**
     * Clear all entries of the current line.
     */
    private void clearLine() {

        for (int i = 0; i < line.length; i++) {
            line[i] = null;
        }
        col = 0;
    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#close()
     */
    public NodeList close() throws GeneralException {

        // TODO unimplemented
        return null;
    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#cr()
     */
    public void cr() throws GeneralException {

        // TODO unimplemented
        clearLine();
    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#crcr()
     */
    public void crcr() throws GeneralException {

        // TODO unimplemented
        clearLine();
    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#omit()
     */
    public void omit() throws GeneralException {

        // TODO unimplemented

    }

    /**
     * @see de.dante.extex.typesetter.impl.AlignmentList#span()
     */
    public void span() throws GeneralException {

        // TODO unimplemented
        col++;

    }

    /**
     * @see de.dante.extex.typesetter.ListMaker#treatTabMark(
     *      de.dante.extex.interpreter.context.TypesettingContext,
     *      de.dante.extex.scanner.Token)
     */
    public void treatTabMark(final TypesettingContext context, final Token token)
            throws GeneralException {

        // TODO unimplemented
    }
}