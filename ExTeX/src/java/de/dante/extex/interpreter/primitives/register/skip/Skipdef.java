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

package de.dante.extex.interpreter.primitives.register.skip;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\skipdef</code>.
 *
 * <doc name="skipdef">
 * <h3>The Primitive <tt>\skipdef</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\skipdef</tt> {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getControlSequence()
 *    &lang;control sequence&rang;} {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *    &lang;equals&rang;} {@linkplain
 *      de.dante.extex.interpreter.TokenSource#scanNumber()
 *      &lang;8-bit&nbsp;number&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \skipdef\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \skipdef\abc 33  </pre>
 * </p>
 * </doc>
 *
 *
 * <h3>Possible Extension</h3>
 * Allow an expandable expression instead of the number to define real named
 * counters.
 *
 * <p>Example</p>
 * <pre>
 * \skipdef\abc={xyz\the\count0}
 * \skipdef\abc {def}
 * </pre>
 * To protect the buildin registers one might consider to use the key
 * "#<i>name</i>" or "skip#<i>name</i>".
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Skipdef extends AbstractSkip {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Skipdef(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.getControlSequence();
        source.getOptionalEquals();
        String key = getKey(source, context);
        context.setCode(cs, new SkipParameter(key), prefix.isGlobal());

    }
}