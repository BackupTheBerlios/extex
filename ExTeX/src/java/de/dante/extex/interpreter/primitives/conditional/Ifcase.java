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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides an implementation for the primitive <code>\ifcase</code>.
 *
 * <doc name="ifcase">
 * <h3>The Primitive <tt>\ifcase</tt></h3>
 * <p>
 *  The primitive <tt>\ifcase</tt> provides a conditional switch on a numeric
 *  value. The next tokens are used as a number. This number determines which
 *  branch to expand. The first branch follows the number immediately. This
 *  branch is associated to the number 0.
 * </p>
 * <p>
 *  The primitive <tt>\or</tt> advances to the next branch.
 *  The primitive <tt>\else</tt> starts the else branch. The alse branch is used
 *  if no other branch fits.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;ifcase&rang;
 *     &rarr; <tt>\ifcase</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  &lang;cases&rang; <tt>\fi</tt>
 *
 *    &lang;cases&rang;
 *     &rarr;
 *      |  &lang;branch text&rang; <tt>\else</tt> &lang;else text&rang;
 *      |  &lang;branch text&rang; <tt>\or</tt> &lang;cases&rang;  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \ifcase\count0 a\or b\or c\else x\fi  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.30 $
 */
public class Ifcase extends AbstractIf {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant <tt>OR</tt> contains the value indicating an \or.
     */
    protected static final Tag OR = new Tag();

    /**
     * The constant <tt>ELSE</tt> contains the value indicating an \else.
     */
    protected static final Tag ELSE = new Tag();

    /**
     * The constant <tt>FI</tt> contains the value indicating a \fi.
     */
    protected static final Tag FI = new Tag();

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Ifcase(final String name) {

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

        long branch = source.scanInteger(context, typesetter);
        if (branch < 0) {
            if (skipToElseOrFi(context, source)) {
                context.pushConditional(source.getLocator(), true, this,
                        branch, false);
            }
            return;
        }

        while (branch > 0) {
            Tag tag = skipToOrOrElseOrFi(context, source);
            if (tag == OR) {
                branch--;
            } else if (tag == ELSE) {
                context.pushConditional(source.getLocator(), true, this,
                        branch, false);
                return;

            } else if (tag == FI) {
                return;

            } else {
                throw new ImpossibleException("impossible tag encountered");

            }
        }
        context.pushConditional(source.getLocator(), true, this, branch, false);
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
     * Skip to the next matching <tt>\fi</tt> or <tt>\or</tt> Token
     * counting the intermediate <tt>\if</tt> s and <tt>\fi</tt>s.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return <code>true</code> if a matching <tt>\or</tt> has been found;
     *  otherwise return <code>false</code> if a matching <tt>\fi</tt> has been
     *  found.
     *
     * @throws InterpreterException in case of an error
     */
    private Tag skipToOrOrElseOrFi(final Context context,
            final TokenSource source) throws InterpreterException {

        Code code;
        int n = 0;

        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {
            if (t instanceof CodeToken
                    && (code = context.getCode((CodeToken) t)) != null) {
                if (code instanceof Fi) {
                    if (--n < 0) {
                        return FI;
                    }
                } else if (code instanceof Or) {
                    if (n <= 0) {
                        return OR;
                    }
                } else if (code instanceof Else) {
                    if (n <= 0) {
                        return ELSE;
                    }
                } else if (code.isIf()) {
                    n++;
                }
            }
        }

        throw new HelpingException(LocalizerFactory
                .getLocalizer(AbstractIf.class.getName()), "TTP.EOFinSkipped");
    }

    /**
     * @see de.dante.extex.interpreter.primitives.conditional.AbstractIf#conditional(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    protected boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter) {

        return false;
    }

    /**
     * This is an internal class for type-safe values.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.30 $
     */
    protected static final class Tag {

    }

}