/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.conditional;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This is the abstract base class for all ifs.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractIf extends AbstractCode implements ExpandableCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractIf(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#isIf()
     */
    public boolean isIf() {

        return true;
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        if (conditional(context, source, typesetter) ||
                skipToElseOrFi(context, source)) {
            context.pushConditional(source.getLocator(), true);
        }

        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        if (conditional(context, source, typesetter) ||
                skipToElseOrFi(context, source)) {
            context.pushConditional(source.getLocator(), true);
        }
    }

    /**
     * This method computes the boolean value of the conditional.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the boolean value
     *
     * @throws GeneralException in case of en error
     */
    protected abstract boolean conditional(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException;

    /**
     * Skip to the next matching <tt>\fi</tt> or <tt>\else</tt> Token
     * counting the intermediate <tt>\if</tt>s and <tt>\fi</tt>s.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return <code>true</code> if a matching <tt>\else</tt> has been found;
     *         otherwise return <code>false</code> if a matching <tt>\fi</tt>
     *         has been found
     *
     * @throws GeneralException in case of en error
     */
    protected boolean skipToElseOrFi(final Context context,
            final TokenSource source) throws GeneralException {

        Code code;
        int n = 0;

        for (Token t = source.getToken(); t != null; t = source.getToken()) {

            if (t instanceof CodeToken && (code = context.getCode(t)) != null) {
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
                }
            }
        }

        throw new GeneralHelpingException("TTP.EOFinSkipped");
    }

}