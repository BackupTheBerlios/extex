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

package de.dante.extex.interpreter.primitives.info;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CodeExpander;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\the</code>.
 *
 * <doc name="the">
 * <h3>The Primitive <tt>\the</tt></h3>
 * <p>
 *  The primitive <tt>\the</tt> inserts the definition of certain primitives
 *  into the input stream. If the token following <tt>\the</tt> is not theable
 *  then an error is raised.
 * </p>
 * <p>
 *  During the expansion of arguments of macros like <tt>\edef</tt>,
 *  <tt>\xdef</tt>, <tt>\message</tt>, and others the further expansion of the
 *  tokens is inhibited.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;the&rang;
 *      &rarr; <tt>\the</tt> &lang;internal quantity&rang; </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \the\count123  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.29 $
 */
public class The extends AbstractCode implements ExpandableCode, CodeExpander {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060408L;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public The(final String name) {

        super(name);
    }

    /**
     * Get the next token (not expand) and if it <code>Theable</code>, then
     * call <code>the()</code> and put the result on the stack.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token cs = source.getToken(context);

        if (cs == null) {
            throw new EofException(printableControlSequence(context));
        }
        if (cs instanceof CodeToken) {

            Code code = context.getCode((CodeToken) cs);

            if (code instanceof Theable) {
                Tokens toks = ((Theable) code).the(context, source, typesetter);
                source.push(toks);
                return;
            }
        }

        throw new HelpingException(getLocalizer(), "TTP.CantUseAfterThe", //
                cs.toString(), printableControlSequence(context));
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        execute(prefix, context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.CodeExpander#expandCode(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter,
     *      de.dante.extex.interpreter.type.tokens.Tokens)
     */
    public void expandCode(final Context context, final TokenSource source,
            final Typesetter typesetter, final Tokens tokens)
            throws InterpreterException {

        Token cs = source.getToken(context);

        if (cs == null) {
            throw new EofException(printableControlSequence(context));
        }
        if (cs instanceof CodeToken) {

            Code code = context.getCode((CodeToken) cs);

            if (code instanceof Theable) {
                tokens.add(((Theable) code).the(context, source, typesetter));
                return;
            }
        }

        throw new HelpingException(getLocalizer(), "TTP.CantUseAfterThe", //
                cs.toString(), printableControlSequence(context));
    }
}
