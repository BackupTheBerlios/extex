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
package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\hbox</code>.
 * <p>
 * Examples
 * </p>
 * <pre>
 *  \hbox{abc}
 *  \hbox to 120pt {abc}
 *  \hbox spread 12pt {abc}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class Hbox extends AbstractCode implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hbox(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Box b = getBox(context, source, typesetter);
        typesetter.add(b.getNodes());

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Tokens toks = context.getToks("everyhbox");
        if (toks != null) {
            source.push(toks);
        }

        Box box;
        if (source.scanKeyword("to")) {
            Dimen d = new Dimen(context, source);
            box = new Box(context, source, typesetter, true);
            box.setWidth(d);
        } else if (source.scanKeyword("spread")) {
            Dimen d = new Dimen(context, source);
            box = new Box(context, source, typesetter, true);
            box.getWidth().add(d);
        } else {
            box = new Box(context, source, typesetter, true);
        }
        return box;
    }

}
