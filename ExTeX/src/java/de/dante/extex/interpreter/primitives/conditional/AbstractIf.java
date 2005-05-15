/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is the abstract base class for all ifs.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public abstract class AbstractIf extends AbstractCode implements ExpandableCode {

    /**
     * Getter for the localizer.
     *
     * @return the localizer
     */
    protected static Localizer getMyLocalizer() {

        return LocalizerFactory.getLocalizer(AbstractIf.class.getName());
    }

    /**
     * Skip to the next matching <tt>\fi</tt> or <tt>\else</tt> Token
     * counting the intermediate <tt>\if</tt>s and <tt>\fi</tt>s.
     *
     * <p>
     *  This method implements to the absorbtion of tokens at high speed.
     * </p>
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return <code>true</code> if a matching <tt>\else</tt> has been found;
     *         otherwise return <code>false</code> if a matching <tt>\fi</tt>
     *         has been found
     *
     * @throws InterpreterException in case of en error
     */
    protected static boolean skipToElseOrFi(final Context context,
            final TokenSource source) throws InterpreterException {

        Code code;
        int n = 0;

        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {

            if (t instanceof CodeToken
                    && (code = context.getCode((CodeToken) t)) != null) {
                if (code instanceof Fi) {
                    if (--n < 0) {
                        return false;
                    }
                } else if (code instanceof Else) {
                    if (n <= 0) {
                        return true;
                    }
                } else if (code.isIf()) {
                    n++;
                } else if (code.isOuter()) {
                    throw new HelpingException(getMyLocalizer(),
                            "TTP.OuterInSkipped");
                }
            }
        }

        throw new HelpingException(getMyLocalizer(), "TTP.EOFinSkipped");
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractIf(final String name) {

        super(name);
    }

    /**
     * This method computes the boolean value of the conditional.
     * If the result is <code>true</code> then the then branch is expanded and
     * the else branch is skipped. Otherwise the then branch is skipped and the
     * else branch is expanded.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the boolean value
     *
     * @throws InterpreterException in case of en error
     */
    protected abstract boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException;

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

        if (conditional(context, source, typesetter)
                || skipToElseOrFi(context, source)) {
            context.pushConditional(source.getLocator(), true,
                    printableControlSequence(context));
        }
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

        if (conditional(context, source, typesetter)
                || skipToElseOrFi(context, source)) {
            context.pushConditional(source.getLocator(), true,
                    printableControlSequence(context));
        }
    }

    /**
     * The ifs are characterized by the return value <code>true</code> of this
     * method. Thus the overwritten method returning the constant is provided
     * in this abstract base class.
     *
     * @see de.dante.extex.interpreter.type.Code#isIf()
     */
    public boolean isIf() {

        return true;
    }
}