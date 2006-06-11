/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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
 *  The primitive <tt>\countdef</tt> can be used to define a control sequence as
 *  alias for a count register. The control sequence can be used wherever a
 *  count register is expected afterwards.
 * </p>
 * <p>
 *  The primitive <tt>\countdef</tt>  is an assignment. Thus the settings of
 *  <tt>\afterassignment</tt> and <tt>\globaldefs</tt> are applied.
 * </p>
 * <p>
 *  The prefix <tt>\global</tt> can be used to make the assignment to the new
 *  control sequence global instead of the group-local assignment which is the
 *  default.
 * </p>
 *
 * <h4>Syntax</h4>
 * The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;countdef&rang;
 *      &rarr; &lang;modifier&rang; <tt>\countdef</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *        &lang;control sequence&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanRegisterName(Context,TokenSource,Typesetter,String)
 *        &lang;register name&rang;}
 *
 *    &lang;modifier&rang;
 *      &rarr;
 *       |  <tt>\global</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \countdef\abc=45  </pre>
 *  <pre class="TeXSample">
 *    \countdef\abc 33  </pre>
 *  <pre class="TeXSample">
 *    \countdef\abc={xyz}  </pre>
 *  <pre class="TeXSample">
 *    \countdef\abc={xyz\the\count0}  </pre>
 *
 * <h4>Differences to <logo>TeX</logo> and Friends</h4>
 * <p>
 *  In <logo>TeX</logo> the register name could consist of an integer in the
 *  range from 0 to 255. In <logo>Omega</logo> this restriction has been relaxed
 *  to allow integers from 0 to 32767. In <logo>ExTeX</logo> the restriction to
 *  integers has been relaxed. The register name can either be a number &ndash;
 *  positive or not and of any value &ndash; or alternatively any token
 *  sequence enclosed in braces.
 * </p>
 * <p>
 *  Note that the extended register names and the maximal number acceptable as
 *  register names are a feature of <logo>ExTeX</logo>
 *  which is configurable via the count register <tt>\max.register</tt>.
 *  This means that the feature can be disabled in the compatibility modes.
 * </p>
 * </doc>
 *
 *
 *
 * To protect the built-in registers one might consider to use the key
 * "#<i>name</i>" or "count#<i>name</i>".
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.24 $
 */
public class Countdef extends AbstractCount {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

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
        String key = getKey(context, source, typesetter);
        context.setCode(cs, new IntegerParameter(key), prefix.clearGlobal());
    }

}
