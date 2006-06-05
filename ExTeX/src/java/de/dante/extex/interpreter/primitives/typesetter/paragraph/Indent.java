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

package de.dante.extex.interpreter.primitives.typesetter.paragraph;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.primitives.register.box.AbstractBox;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.HorizontalListNode;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\indent</code>.
 *
 * <doc name="indent">
 * <h3>The Primitive <tt>\indent</tt></h3>
 * <p>
 *  The primitive <tt>\indent</tt> ensures that the further processing appears
 *  in horizontal mode and inserts horizontal spacing in the width of
 *  the dimen register <tt>\parindent</tt>.
 * </p>
 * <p>
 *  Note that the spacing is inserted in any case. Thus several successive
 *  invocations lead to more spacing. This can even happen in the middle of a
 *  paragraph.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;indent&rang;
 *       &rarr; <tt>\indent</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \indent  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class Indent extends AbstractBox {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060402L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Indent(final String name) {

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

        Dimen parindent = context.getDimen("parindent");
        typesetter.ensureHorizontalMode(source.getLocator());
        try {
            typesetter.add(new HorizontalListNode(parindent));
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

}
