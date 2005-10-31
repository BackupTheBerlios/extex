/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\hbox</code>.
 *
 * <doc name="hbox">
 * <h3>The Primitive <tt>\hbox</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 * <p>
 *  The contents of the toks register <tt>\everyhbox</tt> is inserted at the
 *  beginning of the horizontal material of the box.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;hbox&rang;
 *    &rarr; <tt>\hbox</tt> &lang;box specification&rang; <tt>{</tt> &lang;horizontal material&rang; <tt>}</tt>
 *
 *    &lang;box specification&rang;
 *        &rarr;
 *         | <tt>to</tt> &lang;rule dimension&rang;
 *         | <tt>spread</tt> &lang;rule dimension&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \hbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \hbox to 120pt{abc}  </pre>
 *  <pre class="TeXSample">
 *    \hbox spread 12pt{abc}  </pre>
 *
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
 * @version $Revision: 1.16 $
 */
public class Hbox extends AbstractBoxPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hbox(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.box.Boxable#getBox(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Box getBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Box box;
        try {
            if (source.getKeyword(context, "to")) {
                Dimen d = new Dimen(context, source, typesetter);
                box = acquireBox(context, source, typesetter);
                box.setWidth(d);
            } else if (source.getKeyword(context, "spread")) {
                Dimen d = new Dimen(context, source, typesetter);
                box = acquireBox(context, source, typesetter);
                box.spreadWidth(d);
            } else {
                box = acquireBox(context, source, typesetter);
            }
        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        } catch (MissingLeftBraceException e) {
            throw new MissingLeftBraceException(
                    printableControlSequence(context));
        }
        return box;
    }

    /**
     * Acquire a complete Box taking into account the tokens in
     * <tt>\afterassignment</tt> and <tt>\everyhbox</tt>.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the complete Box
     *
     * @throws InterpreterException in case of an error
     */
    private Box acquireBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Tokens toks = context.getToks("everyhbox");
        Token t = context.getAfterassignment();
        Tokens insert;

        if (t == null) {
            insert = toks;
        } else {
            insert = new Tokens(t);
            if (toks != null) {
                insert.add(toks);
            }
        }

        return new Box(context, source, typesetter, true, insert);
    }
}
