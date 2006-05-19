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
 * <code>\topmarks</code>.
 *
 * <doc name="topmarks">
 * <h3>The Primitive <tt>\topmarks</tt></h3>
 * <p>
 *  The primitive <tt>\topmarks</tt> provides access to the topmost mark of the
 *  given class encountered on the current page &ndash; when processing the page
 *  in the output routine.
 * </p>
 * <p>
 *  The primitive is a tokens register. It can be used wherever a tokens value
 *  is expected. The value is empty when no top mark of the class is available
 *  yet.
 * </p>
 * <p>
 *  The primitive <tt>\topmark</tt> is an abbreviation for <tt>\topmarks0</tt>.
 * </p>
 * <p>
 *  The class of the top mark is determined when the <tt>\marks</tt> primitive
 *  is used to insert a mark into the current list. In <logo>TeX</logo> the
 *  class used to be a number in the range 0&ndash;255; the limit has been
 *  raised in <logo>eTeX</logo> to 32767 and in <logo>ExTeX</logo> to allow
 *  any number or tokens value.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;topmarks&rang;
 *      &rarr; <tt>\topmarks</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanRegisterName(Context,String)
 *        &lang;mark name&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \topmarks42  </pre>
 *  <pre class="TeXSample">
 *    \topmarks{abc}  </pre>
 *  <pre class="TeXSample">
 *    \topmarks\count0  </pre>
 *  <pre class="TeXSample">
 *    \toks0=\topmark3  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class Topmarks extends AbstractMarksCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060416L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Topmarks(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.typesetter.mark.AbstractMarksCode#getValue(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    protected Tokens getValue(final Context context, final String key)
            throws InterpreterException {

        return context.getTopMark(key);
    }

}
