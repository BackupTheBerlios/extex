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
package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.Localizable;

/**
 * This class provides an implementation for the primitive <code>\if</code>.
 *
 * <doc name="unless">
 * <h3>The Primitive <tt>&#x005c;unless</tt></h3>
 * <p><strong>Copied of the eTeX reference</strong>.</p>
 * <p>
 * TeX has, by design, a rather sparse set of conditional primitives:
 * \ifeof, \ifodd, \ifvoid, etc., have no complementary
 * counterparts. Whilst this normally poses no problems since each
 * accepts both a \then (implicit) and an \else (explicit) part, they
 * fall down when used as the final \if... of a \loop ... \if
 * ... \repeat construct, since no \else is allowed after the final
 * \if.... &#x005c;unless allows the sense of all Boolean conditionals to be
 * inverted, and thus (for example) &#x005c;unless \ifeof yields true iff
 * end-of-file has not yet been reached.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 * TODO
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    &#x005c;unless\if\x\y not ok \fi  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:sebastian.waschik@gmx.de">Sebastian Waschik</a>
 * @version $Revision: 1.4 $
 */
public class Unless extends AbstractIf implements Localizable {
    /**
     * Object for localize strings messages.
     *
     */
    private Localizer localizer = null;


    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Unless(final String name) {
        super(name);
    }


    /**
     * @see de.dante.extex.interpreter.primitives.conditional.AbstractIf#conditional(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {


        Token token = source.getToken(context);
        Code code;
        AbstractIf abstractIf;

        code = context.getCode(token);

        // TODO: this does not work with \def-ifs (TE)
        if (code instanceof AbstractIf) {
            abstractIf = (AbstractIf) code;
        } else {
             // TODO: message (TE)
            throw new HelpingException(localizer, "Except If Conditional",
                                       printableControlSequence(context));
        }

        return !abstractIf.conditional(context, source, typesetter);
    }

    /**
     * Set the <code>Localizer</code> here.
     *
     * @param theLocalizer a <code>Localizer</code> value
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {
        localizer = theLocalizer;
    }

}
