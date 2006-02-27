/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter.spacing;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.ExplicitKernNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\kern</code>.
  *
 * <doc name="kern">
 * <h3>The Primitive <tt>\kern</tt></h3>
 * <p>
 *  This primitive produces a horizontal or vertical kerning. This is a (minor)
 *  adjustment of the position. The meaning depends on the current mode of the
 *  typesetter. In vertical modes it means a vertival adjustment. Otherwise it
 *  means a horizontal adjustment.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;kern&rang;
 *      &rarr; <tt>\kern</tt> {@linkplain
 *      de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *      &lang;dimen&rang;}   </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \kern 12pt  </pre>
 *  <pre class="TeXSample">
 *    \kern -3mm  </pre>
 *  <pre class="TeXSample">
 *    \kern -\dimen123  </pre>
 *
 * </doc>
 *
 * <p>
 *  The effect of the primitive is that a
 *  {@link de.dante.extex.typesetter.type.node.KernNode KernNode} is is sent to
 *  the typesetter.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Kern extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Kern(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Dimen kern = new Dimen();
        try {
            kern.set(context, source, typesetter);
            typesetter.add(new ExplicitKernNode(kern));
        } catch (InterpreterException e) {
            throw e;
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

}
