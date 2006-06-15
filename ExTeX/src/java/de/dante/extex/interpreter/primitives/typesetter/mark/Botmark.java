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

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\botmark</code>.
 *
 * <doc name="botmark">
 * <h3>The Primitive <tt>\botmark</tt></h3>
 * <p>
 *  The primitive <tt>\botmark</tt> expands to the last mark on the current
 *  page. If no mark has been encountered on the current page then it
 *  expands to the last mark on the previous page. If no mark has been
 *  placed ever then the primitive expands to the empty token list.
 * </p>
 * <p>
 *  See the documentation of the primitive
 *  {@link de.dante.extex.interpreter.primitives.typesetter.mark.Mark <tt>\mark</tt>}
 *  for further explanation of marks.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;botmark&rang;
 *      &rarr; <tt>\botmark</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \botmark  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Botmark extends Botmarks {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Botmark(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.typesetter.mark.AbstractMarkCode#getKey(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected String getKey(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return "0";
    }

}
