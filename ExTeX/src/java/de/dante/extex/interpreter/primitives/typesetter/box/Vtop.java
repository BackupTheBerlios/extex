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

package de.dante.extex.interpreter.primitives.typesetter.box;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This class provides an implementation for the primitive <code>\vtop</code>.
 *
 * <doc name="vtop">
 * <h3>The Primitive <tt>\vtop</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The contents of the toks register <tt>\everyvbox</tt> is inserted at the
 *  beginning of the vertical material of the box.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;vtop&rang;
 *      &rarr; <tt>\vtop</tt> &lang;box specification&rang; <tt>{</tt> &lang;vertical material&rang; <tt>}</tt>
 *
 *    &lang;box specification&rang;
 *      &rarr;
 *         | <tt>to</tt> &lang;rule dimension&rang;
 *         | <tt>spread</tt> &lang;rule dimension&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \vtop{abc}  </pre>
 *  <pre class="TeXSample">
 *    \vtop to 120pt{abc}  </pre>
 *  <pre class="TeXSample">
 *    \vtop spread 12pt{abc}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.12 $
 */
public class Vtop extends Vbox {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060617L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Vtop(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.primitives.typesetter.box.Vbox#constructBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.scanner.type.token.Token)
     */
    protected Box constructBox(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token startToken)
            throws InterpreterException {

        Box box = acquireBox(context, source, typesetter, GroupType.VTOP_GROUP,
                startToken);
        NodeList nodes = box.getNodes();
        Dimen depth = new Dimen(box.getDepth());
        depth.add(box.getHeight());
        if (nodes.size() > 0) {
            Node top = nodes.get(0);
            FixedDimen height = top.getHeight();
            box.setHeight(height);
            depth.subtract(height);
            box.setDepth(depth);
        }
        return box;
    }

}
