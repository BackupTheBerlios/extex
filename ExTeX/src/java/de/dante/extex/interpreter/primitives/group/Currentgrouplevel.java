/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.group;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;


/*
 * TODO:
 * - getCounter does not work???
 * (TE)
 *


/**
 * This class provides an implementation for the primitive
 * <code>\currentgrouplevel</code>.
 *
 * <doc name="begingroup">
 * <h3>The Primitive <tt>\currentgrouplevel</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;currentgrouplevel&rang;
 *      &rarr; <tt>\currentgrouplevel</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \the\currentgrouplevel  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.3 $
 */
public class Currentgrouplevel
    extends AbstractCode
    implements CountConvertible, Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Currentgrouplevel(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        throw new HelpingException("TTP.CantUseIn",
                printableControlSequence(context), typesetter.getMode()
                        .toString());
    }


    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        return context.getGroupLevel();
    }


    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
                      final Typesetter typesetter)
        throws GeneralException {

        String level = Long.toString(context.getGroupLevel());
        return new Tokens(context, level);
    }
}
