/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter.primitives.box;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\hbox</code>.
 *
 * <doc name="hbox">
 * <h3>The Primitive <tt>\hbox</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The contents of the toks register <tt>\everyhbox</tt> is inserted at the
 *  beginning of the horizontal material of the box.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hbox&rang;
 *    := <tt>\hbox</tt> &lang;box specification&rang; <tt>{</tt> &lang;horizontal material&rang; <tt>{</tt>
 *
 *    &lang;box specification&rang;
 *        :=
 *         | <tt>to</tt> &lang;rule dimension&rang;
 *         | <tt>spread</tt> &lang;rule dimension&rang;  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \hbox to 120pt{abc}  </pre>
 *  <pre class="TeXSample">
 *    \hbox spread 12pt{abc}  </pre>
 * </p>
 * </doc>
 *
 * <doc type="parameter" name="everyhbox">
 * <h3>The Tokens Parameter <tt>\everyhbox</tt></h3>
 * <p>
 *  The tokens parameter is used in <tt>/hbox</tt>. The tokens contained are
 *  inserted at the beginnig of the horizontal material of the hbox.
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class Hbox extends AbstractCode implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hbox(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Box b = getBox(context, source, typesetter);
        typesetter.add(b.getNodes());

        return true;
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        Tokens toks = context.getToks("everyhbox");
        if (toks != null) {
            source.push(toks);
        }

        Box box;
        if (source.getKeyword("to")) {
            Dimen d = new Dimen(context, source);
            box = new Box(context, source, typesetter, true);
            box.setWidth(d);
        } else if (source.getKeyword("spread")) {
            Dimen d = new Dimen(context, source);
            box = new Box(context, source, typesetter, true);
            box.getWidth().add(d);
        } else {
            box = new Box(context, source, typesetter, true);
        }
        return box;
    }

}
