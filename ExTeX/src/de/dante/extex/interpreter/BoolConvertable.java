/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Bool;
import de.dante.util.GeneralException;

/**
 * This is an interface which describes the feature to be convertibe into a bool.
 *
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public interface BoolConvertable {

    /**
     * Convert to a bool.
     *
     * @param context   the interpreter context
     * @param source    the source for new tokens
     * @return the converted value
     * @throws GeneralException in case of an error
     */
    Bool convertBoot(Context context, TokenSource source)
            throws GeneralException;
}