/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractIf;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\else</code>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Else extends AbstractIf {

    /**
     * Creates a new object.
     * 
     * @param name the name for debugging
     */
    public Else(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context, TokenSource source, Typesetter typesetter)
               throws GeneralException {
        if ( context.ifPop() ) {
            if ( skipToElseOrFi(context, source) ) {
                throw new GeneralHelpingException("TTP.ExtraOrElseFi","\\else");
            }
        }

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.AbstractIf#conditional(de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource)
     */
    protected boolean conditional(Context context, TokenSource source, Typesetter typesetter) {
        return false;
    }

    /**
     * @see de.dante.extex.interpreter.Code#isIf()
     */
    public boolean isIf() {
        return false;
    }

}
