/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.math;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.NoadConsumer;
import de.dante.extex.typesetter.type.MathGlyph;
import de.dante.extex.typesetter.type.noad.AccentNoad;
import de.dante.extex.typesetter.type.noad.Noad;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\mathaccent</code>.
 *
 * <doc name="mathaccent">
 * <h3>The Primitive <tt>\mathaccent</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;mathaccent&rang;
 *       &rarr; <tt>\mathaccent</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \mathaccent  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class Mathaccent extends AbstractMathCode {

    /**
     * The field <tt>CHARCODE_MAX</tt> contains the maximum of the character
     * code. If this value is exceeded then an error should be raised.
     */
    private static final int CHARCODE_MAX = 0xfff;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Mathaccent(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        NoadConsumer nc = getListMaker(context, typesetter);
        long accent = source.scanNumber();
        if (accent > CHARCODE_MAX) {
            throw new HelpingException(getLocalizer(), "TTP.BadMathCharCode",
                    Long.toHexString(accent));
        }
        Noad noad = nc.scanNoad(context, source);
        nc.add(new AccentNoad(new MathGlyph((int) accent), noad));
        return true;
    }

}