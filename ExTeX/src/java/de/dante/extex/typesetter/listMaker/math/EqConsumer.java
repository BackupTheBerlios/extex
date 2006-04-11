/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.listMaker.math;

import de.dante.extex.interpreter.exception.helping.CantUseInException;

/**
 * This interface describes the functionality to activate the typesetting of an
 * equation number.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface EqConsumer {

    /**
     * This method switches the collection of material to the target "equation
     * number".
     *
     * @param left the indicator on which side to produce the equation number.
     *  A value <code>true</code> indicates that the left side should be used.
     *
     * @throws CantUseInException in case that the EqConsumer is in a mode
     *  where a switching to the number is not possible
     */
    void switchToNumber(boolean left) throws CantUseInException;

}
