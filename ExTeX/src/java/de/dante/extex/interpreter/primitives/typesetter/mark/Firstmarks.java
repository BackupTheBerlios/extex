/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter.mark;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.tokens.Tokens;

/**
 * This class provides an implementation for the primitive
 * <code>\firstmarks</code>.
 *
 * <doc name="firstmarks">
 * <h3>The Primitive <tt>\firstmarks</tt></h3>
 * <p>
 *  The primitive <tt>\firstmarks</tt> expands to the first mark on the current
 *  page of the given class. If no mark has been encountered on the current page
 *  then it expands to the last mark on the previous page. If no mark has been
 *  placed ever then the primitive expands to the empty token list.
 * </p>
 * <p>
 *  See the documentation of the primitive
 *  {@link de.dante.extex.interpreter.primitives.typesetter.mark.Marks <tt>\marks</tt>}
 *  for further explanation of marks.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;firstmarks&rang;
 *      &rarr; <tt>\firstmarks</tt> {@linkplain
 *        de.dante.extex.interpreter.primitives.typesetter.mark.AbstractMarksCode#getKey(Context,TokenSource,Typesetter)
 *        &lang;mark name&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \firstmarks42  </pre>
 *  <pre class="TeXSample">
 *    \firstmarks\count0  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class Firstmarks extends AbstractMarksCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Firstmarks(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.typesetter.mark.AbstractMarksCode#getValue(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    protected Tokens getValue(final Context context, final String key)
            throws InterpreterException {

        return context.getFirstMark(key);
    }

}
