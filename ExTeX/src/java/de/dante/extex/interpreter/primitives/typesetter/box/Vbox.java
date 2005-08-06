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

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.box.Boxable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\vbox</code>.
 *
 * <doc name="vbox">
 * <h3>The Primitive <tt>\vbox</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The contents of the toks register <tt>\everyvbox</tt> is inserted at the
 *  beginning of the vertical material of the box.
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;vbox&rang;
 *      &rarr; <tt>\vbox</tt> &lang;box specification&rang; <tt>{</tt> &lang;vertical material&rang; <tt>{</tt>
 *
 *    &lang;box specification&rang;
 *      &rarr;
 *         | <tt>to</tt> &lang;rule dimension&rang;
 *         | <tt>spread</tt> &lang;rule dimension&rang;  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \vbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \vbox to 120pt{abc}  </pre>
 *  <pre class="TeXSample">
 *    \vbox spread 12pt{abc}  </pre>
 * </p>
 * </doc>
 *
 * <doc type="parameter" name="everyvbox">
 * <h3>The Tokens Parameter <tt>\everyvbox</tt></h3>
 * <p>
 *  The tokens parameter is used in <tt>/vbox</tt>. The tokens contained are
 *  inserted at the beginning of the vertical material of the vbox.
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Vbox extends AbstractCode implements Boxable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Vbox(final String name) {

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

        Box b = getBox(context, source, typesetter);
        try {
            typesetter.add(b.getNodes());
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
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
                box = constructBox(context, source, typesetter);
                box.setHeight(d);
            } else if (source.getKeyword(context, "spread")) {
                Dimen d = new Dimen(context, source, typesetter);
                box = constructBox(context, source, typesetter);
                box.spreadHeight(d);
            } else {
                box = constructBox(context, source, typesetter);
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
     * Acquire a Box and adjust its height and depth according to the rules
     * required.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the complete Box
     *
     * @throws InterpreterException in case of an error
     */
    protected Box constructBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Box box = acquireBox(context, source, typesetter);
        NodeList nodes = box.getNodes();
        Dimen depth = new Dimen(box.getDepth());
        depth.add(box.getHeight());
        int size = nodes.size();
        if (size > 0) {
            Node top = nodes.get(size - 1);
            Dimen height = top.getHeight();
            box.setHeight(height);
            depth.subtract(height);
            box.setDepth(depth);
        }
        return box;
    }

    /**
     * Acquire a complete Box taking into account the tokens in
     * <tt>\afterassignment</tt> and <tt>\everyvbox</tt>.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the complete Box
     *
     * @throws InterpreterException in case of an error
     */
    protected Box acquireBox(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Tokens toks = context.getToks("everyvbox");
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

        return new Box(context, source, typesetter, false, insert);
    }
}