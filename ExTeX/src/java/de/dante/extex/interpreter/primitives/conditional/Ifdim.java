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
package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractIf;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\ifdim</code>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Ifdim extends AbstractIf {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifdim(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.AbstractIf#conditional(de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
        final TokenSource source, final Typesetter typesetter)
        throws GeneralException {

        long x = new Dimen(context, source).getValue();
        Token rel = source.getToken();
        if (rel == null) {
            throw new GeneralHelpingException("UnexpectedEOF",
                    printableControlSequence(context));
        }
        if (rel.getCatcode() == Catcode.OTHER) {
            switch (rel.getChar().getCodePoint()) {
            case '<':
                return (x < new Dimen(context, source).getValue());
            case '=':
                return (x == new Dimen(context, source).getValue());
            case '>':
                return (x > new Dimen(context, source).getValue());
            default:
            // fall-through
            }
        }
        //TODO pushback the tokens read
        throw new GeneralHelpingException("TTP.IllegalIfnumOp");
    }
}
