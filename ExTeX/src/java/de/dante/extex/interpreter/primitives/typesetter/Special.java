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
package de.dante.extex.interpreter.primitives.typesetter;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.node.SpecialNode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\special</code>.
 *
 * <doc name="special">
 * <h3>The Primitive <tt>\special</tt></h3>
 * <p>
 *  This primitive sends a string to the backend driver.
 *  The argument is a balanced block of text which is expanded and translated
 *  into a string.  The string is given in
 *  a {@link de.dante.extex.interpreter.type.node.SpecialNode SpecialNode} to
 *  the typesetter for passing it down.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;special&rang;
 *        &rarr; <tt>\special</tt> {@linkplain
 *           de.dante.extex.interpreter.TokenSource#scanTokens(Context)
 *           &lang;general text&rang;}  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \special{hello world}  </pre>
 *  <pre class="TeXSample">
 *    \special{ps: \abc}  </pre>
 * </p>
 * <p>
 *  For several backend drivers for TeX a quasi-standard has emerged which uses
 *  a prefix ended by a colon to indicate the backend driver the special is
 *  targeted at.
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Special extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Special(final String name) {
        super(name);
    }

    /**
     * Scan the next tokens (between braces) and send the value (as text) to the
     * typesetter for the backend driver.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String text = source.scanTokens(context).toText();
        typesetter.add(new SpecialNode(text));
    }
}
