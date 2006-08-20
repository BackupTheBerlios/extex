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

package de.dante.extex.interpreter.primitives.namespace;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\export</code>.
 *
 * <doc name="export">
 * <h3>The Primitive <tt>\export</tt></h3>
 * <p>
 *  The primitive <tt>\export</tt> takes a list of tokens and saves them away
 *  for an associated <tt>\import</tt>. The tokens in the list are either
 *  control sequence tokens or active characters. All other tokens are ignored.
 * </p>
 * <p>
 *  The expansion text is empty. The primitive is an assignment. Thus
 *  <tt>\afterassignment</tt> interacts with the primitive in the expected way.
 * </p>
 * <p>
 *  The definitions are usually performed local to the current group. If the
 *  prefix <tt>\global</tt> is given or the count register <tt>\globaldefs</tt>
 *  has a positive value then the definition is made globally.
 *  Usually you want to define the export as global. This is the case if the
 *  <tt>\export</tt> primitive is invoked at group level 0. Interesting special
 *  effects can be achieved when using the export statement in groups and
 *  together with a local scope definition.
 * </p>
 * <p>
 *  This primitive is one building block for the use of name spaces in
 *  <logo>ExTeX</logo>. The central primitive for this purpose is
 *  <tt>\namespace</tt>.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;export&rang;
 *      &rarr; &lang;prefix&rang; <tt>\export</tt> {@linkplain
 *      de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *      &lang;replacement text&rang;}
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       |  <tt>\global</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \export{\a\b}  </pre>
 *
 * </doc>
 *
 *
 * @see de.dante.extex.interpreter.primitives.namespace.Namespace
 * @see de.dante.extex.interpreter.primitives.namespace.Import
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class Export extends AbstractAssignment {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Export(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens export = source.getTokens(context, source, typesetter);
        context.setToks(context.getNamespace() + "\bexport", export, true);
    }

}
