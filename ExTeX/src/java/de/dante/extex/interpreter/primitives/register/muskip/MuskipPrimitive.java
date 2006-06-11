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

package de.dante.extex.interpreter.primitives.register.muskip;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\muskip</code>.
 * It sets the named muskip register to the value given,
 * and as a side effect all prefixes are zeroed.
 *
 * <p>
 * All features are inherited from
 * {@link de.dante.extex.interpreter.primitives.register.muskip.MuskipParameter
 *  MuskipParameter}.
 * Just the key has to be provided under which this Muskip has to be stored.
 * This key is constructed from the name, a hash mark and the running number.
 * </p>
 *
 * <doc name="muskip">
 * <h3>The Primitive <tt>\muskip</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;muskip&rang;
 *      &rarr; &lang;optional prefix&rang; <tt>\muskip</tt>  {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanRegisterName(Context,TokenSource,Typesetter,String)
 *        &lang;register name&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@link
 *        de.dante.extex.interpreter.type.muskip.Muskip#parse(Context,TokenSource,Typesetter)
 *        &lang;muglue&rang;}
 *
 *   &lang;optional prefix&rang;
 *     &rarr;
 *      |  <tt>\global</tt> &lang;optional prefix&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *   \muskip12=345mu plus 12mu  </pre>
 *  <pre class="TeXSample">
 *   \muskip12=0mu plus 1.2fil  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class MuskipPrimitive extends MuskipParameter {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public MuskipPrimitive(final String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the muskip register.
     *
     * @param context the interpreter context to use
     * @param source the source for the next tokens &ndash; if required
     * @param typesetter the typesetter
     *
     * @return the key for the muskip register
     *
     * @throws InterpreterException in case of an error
     */
    protected String getKey(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        String number = Long.toString(Count.scanNumber(context, source,
                typesetter));

        if (Namespace.SUPPORT_NAMESPACE_MUSKIP) {
            return context.getNamespace() + "\b" + getName() + "#" + number;
        } else {
            return getName() + "#" + number;
        }
    }

}
