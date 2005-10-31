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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\countdef</code>.
 *
 * <doc name="countdef">
 * <h3>The Primitive <tt>\countdef</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;countdef&rang;
 *      &rarr; <tt>\countdef</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *        &lang;control sequence&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *        &lang;8-bit&nbsp;number&rang;}</pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \countdef\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \countdef\abc 33  </pre>
 * </p>
 * </doc>
 *
 * <pre>
 * \countdef\abc=45
 * \countdef\abc 54
 * \catcode`!=13
 * \countdef!=46
 * \countdef! 55
 * </pre>
 *
 *
 * <h3>Possible Extension</h3>
 * Allow an expandable expression instead of the number to define real named
 * counters.
 *
 * <p>Example</p>
 * <pre>
 * \countdef\abc={xyz\the\count0}
 * \countdef\abc {def}
 * </pre>
 * To protect the built-in registers one might consider to use the key
 * "#<i>name</i>" or "count#<i>name</i>".
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class Countdef extends AbstractCount {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Countdef(final String name) {

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
            throws InterpreterException {

        CodeToken cs = source.getControlSequence(context);
        source.getOptionalEquals(context);
        String key = getKey(context, source);
        context.setCode(cs, new IntegerParameter(key), prefix.isGlobal());
        prefix.clearGlobal();
    }

}
