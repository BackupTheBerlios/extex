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

package de.dante.extex.interpreter.primitives.typesetter.paragraph;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.glue.Glue;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.GlueNode;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\par</code>.
 *
 * <doc name="par">
 * <h3>The Primitive <tt>\par</tt></h3>
 * <p>
 *  The primitive <tt>\par</tt> signals the end of a paragraph. If
 *  <logo>ExTeX</logo> is in a horizontal mode then the preceding material is
 *  typeset and the paragraph is added to the vertical list. <logo>ExTeX</logo>
 *  goes into a vertical mode afterwards.
 * </p>
 * <p>
 *  If <logo>ExTeX</logo> is in a vertical mode then this primitive is simply
 *  ignored.
 * </p>
 * <p>
 *  The scanner rules of <logo>TeX</logo> determine that the macro <tt>\par</tt>
 *  is inserted for any number of subsequent empty lines. This means that in a
 *  normal text there might be a lot of invocations of <tt>\par</tt> even if
 *  none of them is written explicitly.
 * </p>
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;par&rang;
 *      &rarr; <tt>\par</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    abc \par  def </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Par extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Par(final String name) {

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

        Glue parskip = context.getGlue("parskip");
        Dimen parindent = context.getDimen("parindent");

        try {
            typesetter.par();
            typesetter.add(new GlueNode(parskip, false));
            typesetter.add(new Glue(parindent));
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

}
