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
package de.dante.extex.interpreter.primitives.register.dimen;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.scanner.ActiveCharacterToken;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\dimendef</code>.
 *
 * <p>Example</p>
 * <pre>
 * \dimendef\abc=45
 * \dimendef\abc 54
 * </pre>
 *
 *
 * <h3>Possible Extension</h3>
 * Allow an expandable expression instead of the number to defined real named
 * counters.
 *
 * <p>Example</p>
 * <pre>
 * \dimendef\abc={xyz\the\count0}
 * \dimendef\abc {def}
 * </pre>
 * To protect the buildin registers one might consider to use the key
 * "#<i>name</i>" or "dimen#<i>name</i>".
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Dimendef extends AbstractAssignment {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Dimendef(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.AbstractAssignment#assign(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.scanToken();

        if (cs instanceof ControlSequenceToken) {
            source.scanOptionalEquals();
            //todo: unfortunately we have to know the internal format of the key:-(
            String key = "dimen#" + Long.toString(Count.scanCount(context, source));
            context.setMacro(cs.getValue(), new NamedDimen(key), prefix.isGlobal());
            return;

        } else if (cs instanceof ActiveCharacterToken) {
            source.scanOptionalEquals();
            //todo: unfortunately we have to know the internal format of the key:-(
            String key = "dimen#" + Long.toString(Count.scanCount(context, source));
            context.setActive(cs.getValue(), new NamedDimen(key), prefix.isGlobal());
            return;

        }

        throw new GeneralHelpingException("TTP.MissingCtrlSeq");
    }

}
