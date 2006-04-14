/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.count;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.RegisterMaxObserver;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the count register
 * <code>max.register</code>.
 *
 * <doc name="max.register">
 * <h3>The Count Register <tt>\max.register</tt></h3>
 * <p>
 *  The count register <tt>\max.register</tt> controls the scanning of
 *  register names. The following interpretation for the values is given:
 * </p>
 * <ul>
 *  <li>
 *   If the value is positive than the register name must be a number in the
 *   range from 0 to the value given.
 *  </li>
 *  <li>
 *   If the value is zero then the register name must be a number. The number is
 *   not restricted any further.
 *  </li>
 *  <li>
 *   If the value is less then zero then the register name can be a number or
 *   a token list (in braces).
 *  </li>
 * </ul>
 *
 * <p>
 *  The count register <tt>\max.register</tt> is not affected by grouping. This
 *  means that any assignment is always global.
 * </p>
 * <p>
 *  Note that the name of the register contains a period symbol. This symbol has
 *  usually the catcode other. Thus it can normally not be part of a control
 *  sequence. You can either redefine the catcode locally in a group or use
 *  the primitive <tt>\csname</tt> can be use as shown in the examples below.
 * </p>
 *
 * <h4>Examples</h4>
 * <p>
 * </p>
 * <pre class="TeXSample">
 *   \csname max.register\endcsname=1000  </pre>
 * </doc>
 *
 *
 * <p>
 *  This class avoids the count observer for performance reasons and because an
 *  implementation might not support it.
 * </p>
 *
 * <h3>Note</h3>
 * <p>
 *  The implementation might change in favor of the use of name spaces for
 *  count registers.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MaxRegister extends IntegerParameter {

    /**
     * The field <tt>serialVersionUID</tt> contains the version number for
     * serialization.
     */
    protected static final long serialVersionUID = 20060413L;

    /**
     * Creates a new object.
     *
     * @param name the name of the primitive
     */
    public MaxRegister(final String name) {

        super(name);
    }

    /**
     * Propagate the value change to the token source if it wants to receive it.
     *
     * @param context the interpreter context
     * @param source the source to inform about changes
     */
    protected void propagateValue(final Context context,
            final TokenSource source) {

        if (source instanceof RegisterMaxObserver) {
            ((RegisterMaxObserver) source).setRegisterMax(context.getCount(
                    getName()).getValue());
        }
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.CountPrimitive#advance(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void advance(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        prefix.setGlobal();
        super.advance(prefix, context, source, typesetter);
        propagateValue(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.CountPrimitive#assign(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        prefix.setGlobal();
        super.assign(prefix, context, source, typesetter);
        propagateValue(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.CountPrimitive#divide(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void divide(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        prefix.setGlobal();
        super.divide(prefix, context, source, typesetter);
        propagateValue(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.CountPrimitive#multiply(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void multiply(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        prefix.setGlobal();
        super.multiply(prefix, context, source, typesetter);
        propagateValue(context, source);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.register.count.IntegerParameter#init(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void init(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        super.init(context, source, typesetter);
        propagateValue(context, source);
    }

}
