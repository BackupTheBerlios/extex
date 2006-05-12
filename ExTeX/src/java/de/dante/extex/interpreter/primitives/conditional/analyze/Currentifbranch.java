/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.conditional.analyze;

import de.dante.extex.interpreter.Conditional;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\currentifbranch</code>.
 *
 * <doc name="currentifbranch">
 * <h3>The Primitive <tt>\currentifbranch</tt></h3>
 * <p>
 *  The primitive <tt>\currentifbranch</tt> is an integer quantity which
 *  provides the information in which branch of the enclosing conditional. The
 *  value is determined by the following rules:
 * </p>
 * <ul>
 *  <li>
 *   If the then branch of the enclosing is active then the value is <tt>1</tt>.
 *  </li>
 *  <li>
 *   If the else branch of the enclosing is active then the value is <tt>-1</tt>.
 *  </li>
 *  <li>
 *   If the enclosing conditional is <tt>\ifcase</tt> then the value is the
 *   number selecting the current case for normal cases and <tt>-1</tt> for the
 *   else case.
 *  </li>
 *  <li>
 *   If there is no enclosing conditional then the value is <tt>0</tt>.
 *  </li>
 * </ul>
 * <p>
 *  The primitive <tt>\currentifbranch</tt> is a read-only quantity. an attempt
 *  to use this primitive in a horizontal or vertical mode results in an error.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;currentifbranch&rang;
 *     &rarr; <tt>\currentifbranch</tt> </pre>
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \count0=\currentifbranch  </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Currentifbranch extends AbstractCode
        implements
            CountConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Currentifbranch(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Conditional conditional = context.getConditional();
        return (conditional == null ? 0 : conditional.getBranch());
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, convertCount(context, source, typesetter));
    }

}
