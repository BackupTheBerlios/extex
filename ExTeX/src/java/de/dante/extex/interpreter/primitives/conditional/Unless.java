/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;

/**
 * This class provides an implementation for the primitive <code>\if</code>.
 *
 * <doc name="unless">
 * <h3>The Primitive <tt>&#x005c;unless</tt></h3>
 * <p><strong>Copied of the <logo>eTeX</logo> reference</strong>.</p>
 * <p>
 *  <logo>TeX</logo> has, by design, a rather sparse set of conditional
 *  primitives:
 *  <tt>\ifeof</tt>, <tt>\ifodd</tt>, <tt>\ifvoid</tt>, etc., have no complementary
 *  counterparts. Whilst this normally poses no problems since each
 *  accepts both a <tt>\then</tt> (implicit) and an <tt>\else</tt> (explicit) part, they
 *  fall down when used as the final <tt>\if</tt>... of a <tt>\loop</tt> ... <tt>\if</tt>
 *  ... <tt>\repeat</tt> construct, since no <tt>\else</tt> is allowed after the final
 *  <tt>\if</tt>.... <tt>&#x005c;unless</tt> allows the sense of all Boolean conditionals to be
 *  inverted, and thus (for example) <tt>&#x005c;unless</tt> <tt>\ifeof</tt> yields true iff
 *  end-of-file has not yet been reached.
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
 * @version $Revision: 1.10 $
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
            throws InterpreterException {


        CodeToken token = source.getControlSequence(context);
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
