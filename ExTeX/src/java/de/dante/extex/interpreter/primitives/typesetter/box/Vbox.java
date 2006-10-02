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
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.box.Box;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This class provides an implementation for the primitive <code>\vbox</code>.
 *
 * <doc name="vbox">
 * <h3>The Primitive <tt>\vbox</tt></h3>
 * <p>
 *  The primitive <tt>\vbox</tt> constructs a box of vertical material. Any
 *  boxes added to the vertical box are put below the boxes already contained.
 *  The reference point of the last box contained is used for the whole
 *  vertical box. The height may be adjusted if requested.
 * </p>
 * <p>
 *  The contents of the toks register <tt>\everyvbox</tt> is inserted at the
 *  beginning of the vertical material of the box.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;vbox&rang;
 *      &rarr; <tt>\vbox</tt> &lang;box specification&rang; <tt>{</tt> &lang;vertical material&rang; <tt>}</tt>
 *
 *    &lang;box specification&rang;
 *      &rarr;
 *         | <tt>to</tt> {@linkplain
 *           de.dante.extex.interpreter.type.dimen.Dimen#parse(Context,TokenSource,Typesetter)
 *           &lang;rule dimension&rang;}
 *         | <tt>spread</tt> {@linkplain
 *           de.dante.extex.interpreter.type.dimen.Dimen#parse(Context,TokenSource,Typesetter)
 *           &lang;rule dimension&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \vbox{abc}  </pre>
 *  <pre class="TeXSample">
 *    \vbox to 120pt{abc}  </pre>
 *  <pre class="TeXSample">
 *    \vbox spread 12pt{abc}  </pre>
 *
 * </doc>
 *
 * <doc type="parameter" name="everyvbox">
 * <h3>The Tokens Parameter <tt>\everyvbox</tt></h3>
 * <p>
 *  The tokens parameter is used in <tt>\vbox</tt>. The tokens contained are
 *  inserted at the beginning of the vertical material of the vbox.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;everyvbox&rang;
 *      &rarr; <tt>\everyvbox</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \everyvbox{\message{Hi there}}  </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.27 $
 */
public class Vbox extends AbstractBoxPrimitive {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Vbox(final String name) {

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

        Token startToken = source.getLastToken();
        Box box;
        try {
            if (source.getKeyword(context, "to")) {
                Dimen d = Dimen.parse(context, source, typesetter);
                box = constructBox(context, source, typesetter, startToken);
                box.setHeight(d);
            } else if (source.getKeyword(context, "spread")) {
                Dimen d = Dimen.parse(context, source, typesetter);
                box = constructBox(context, source, typesetter, startToken);
                box.spreadHeight(d);
            } else {
                box = constructBox(context, source, typesetter, startToken);
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
     * <p>
     *  For a <tt>\vbox</tt> the alignment takes the last box contained and
     *  uses its reference point. The depth is preserved and the height is
     *  adjusted if required.
     * </p>
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param startToken the token which started the group
     *
     * @return the complete Box
     *
     * @throws InterpreterException in case of an error
     */
    protected Box constructBox(final Context context, final TokenSource source,
            final Typesetter typesetter, final Token startToken)
            throws InterpreterException {

        Box box = acquireBox(context, source, typesetter, GroupType.VBOX_GROUP,
                startToken);
        NodeList nodes = box.getNodes();
        Dimen depth = new Dimen(box.getDepth());
        depth.add(box.getHeight());
        int size = nodes.size();
        if (size > 0) {
            Node top = nodes.get(size - 1);
            FixedDimen height = top.getHeight();
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
     * @param groupType the group type
     * @param startToken the token which started the group
     *
     * @return the complete Box
     *
     * @throws InterpreterException in case of an error
     */
    protected Box acquireBox(final Context context, final TokenSource source,
            final Typesetter typesetter, final GroupType groupType,
            final Token startToken) throws InterpreterException {

        Tokens everyvbox = context.getToks("everyvbox");
        Token afterassignment = context.getAfterassignment();
        Tokens insert;

        if (afterassignment == null) {
            insert = everyvbox;
        } else {
            insert = new Tokens(afterassignment);
            if (everyvbox != null) {
                insert.add(everyvbox);
            }
        }

        return new Box(context, source, typesetter, false, insert, groupType,
                startToken);
    }

}
