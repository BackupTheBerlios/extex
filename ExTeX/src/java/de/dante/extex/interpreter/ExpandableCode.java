/*
 * Copyright (C) 2004 Gerd Neugebauer
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
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface ExpandableCode {

    /**
     * This method takes the first token and expands it. The result is placed
     * on the stack. To expand a token it might be necessary to consume further
     * tokens. In this case <code>true</code> is returned.
     * <p>
     * This means that expandable code does one step of expansion, puts the
     * result on the stack, and returns <code>true</code>.
     * Non-expandable code simply return <code>false</code>.
     * </p>
     *
     * @param prefix the prefix flags controlling the expansion
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an error
     */
    void expand(Flags prefix, Context context,
        TokenSource source, Typesetter typesetter) throws GeneralException;

}
