/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.tokens.Tokens;

import de.dante.util.GeneralException;

/**
 * This is a interface to mark those classes which are able to provide a
 * description. This description is returned in form of
 * {@link de.dante.extex.interpreter.type.tokens.Tokens Tokens}.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public interface Theable {

    /**
     * This method is the getter for the description of the primitive.
     *
     * @param context the interpreter context
     * @param source the source for further tokens to qualify the request
     *
     * @return the description of the primitive as list of Tokens
     *
     * @throws GeneralException in case of an error
     */
    Tokens the(Context context, TokenSource source) throws GeneralException;

}
