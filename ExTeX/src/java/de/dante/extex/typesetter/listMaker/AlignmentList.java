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
package de.dante.extex.typesetter.listMaker;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.util.GeneralException;


/**
 * This interface describes a list for alignments with the associated methods.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface AlignmentList {

    /**
     * The invocation of this method indicates that the pattern for the current
     * cell should not be taken from the preamble but the defaut should be used
     * instead.
     *
     * @throws GeneralException in case of an error
     */
    void omit() throws GeneralException;

    /**
     * This method is invoked when a row in the alignment is complete and the
     * cells can be integrated. If some cells are not filled jet then they
     * are treated as empty.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws GeneralException in case of an error
     */
    void cr(Context context, TokenSource source) throws GeneralException;

    /**
     * This method is invoked when a row in the alignment is complete and the
     * cells can be integrated. If some cells are not filled jet then they
     * are treated as empty.
     * In contrast to the method cr() this method is a noop when the
     * alignment is at the beginning of a row.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws GeneralException in case of an error
     */
    void crcr(Context context, TokenSource source) throws GeneralException;

    /**
     * This method is invoked when a cell is complete which should be
     * continued in the next cell.
     *
     * @param context the interpreter context
     * @param source the token source
     *
     * @throws GeneralException in case of an error
     */
    void span(Context context, TokenSource source) throws GeneralException;

}
