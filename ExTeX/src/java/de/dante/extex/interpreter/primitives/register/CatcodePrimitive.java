/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\catcode</code>.
 *
 * <doc name="catcode">
 * <h3>The Primitive <tt>\catcode</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The assignment is controlled by the modifier <tt>\global</tt> and the
 *  parameter <tt>\globaldefs</tt>. Usually the assignment is acting on the
 *  current group only. if the integer parameter <tt>\globaldefs</tt> is not
 *  0 or the modifier <tt>\global</tt> is given then the assignment is applied
 *  to all groups.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;catcode&rang;
 *      &rarr; <tt>\catcode</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber()
 *          &lang;8-bit&nbsp;number&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *          &lang;equals&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber()
 *          &lang;4-bit&nbsp;number&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \catcode ...  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class CatcodePrimitive extends AbstractAssignment {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public CatcodePrimitive(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        UnicodeChar charCode = source.scanCharacterCode();
        source.getOptionalEquals();
        long ccNumber = source.scanNumber();

        try {
            context.setCatcode(charCode, Catcode.toCatcode((int) ccNumber),
                    prefix.isGlobal());
        } catch (CatcodeException e) {
            throw new HelpingException(getLocalizer(), "TTP.CodeOutOfRange",
                    Long.toString(ccNumber), Integer.toString(Catcode
                            .getCatcodeMax()));
        }

    }
}