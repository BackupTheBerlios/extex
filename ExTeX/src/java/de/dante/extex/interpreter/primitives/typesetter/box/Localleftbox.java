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

package de.dante.extex.interpreter.primitives.typesetter.box;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.context.group.GroupType;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\localleftbox</code>.
 *
 * <doc name="localleftbox">
 * <h3>The Primitive <tt>\localleftbox</tt></h3>
 * <p>
 *  The primitive <tt>\localleftbox</tt> takes an argument enclosed in braces
 *  and typesets this contents in horizontal mode. 
 * </p>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;localleftbox&rang;
 *      &rarr; <tt>\localleftbox</tt> <tt>{</tt> &lang;horizontal material&rang; <tt>}</tt> </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \localleftbox{abc}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Localleftbox extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Localleftbox(final String name) {

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

        Token startToken = source.getLastToken();
        Flags flags = prefix.copy();
        prefix.clear();
        Token t = context.getAfterassignment();
        Tokens insert;

        if (t == null) {
            insert = new Tokens();
        } else {
            insert = new Tokens(t);
        }
        Box box = new Box(context, source, typesetter, true, insert,
                GroupType.HBOX_GROUP, startToken);

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");

        //prefix.set(flags);
    }

}
