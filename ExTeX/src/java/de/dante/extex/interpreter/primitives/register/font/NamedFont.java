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

package de.dante.extex.interpreter.primitives.register.font;

import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.FontConvertible;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Font;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class NamedFont extends AbstractAssignment implements FontConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public NamedFont(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.AbstractAssignment#assign(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        // TODO Auto-generated method stub
        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.interpreter.FontConvertible#convertFont(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    public Font convertFont(final Context context, final TokenSource source)
            throws GeneralException {

        return context.getFont(getKey(source));
    }

    /**
     * Return the key (the name of the primitive) for the register.
     *
     * @param source the source for new tokens
     *
     * @return the key for the current register
     *
     * @throws GeneralException in case that a derived class need to throw an
     *             Exception this one is declared.
     */
    protected String getKey(final TokenSource source) throws GeneralException {

        return getName();
    }

}