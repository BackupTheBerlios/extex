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

package de.dante.extex.interpreter.primitives.table;

import java.util.ArrayList;
import java.util.List;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.table.util.PreambleItem;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is the abstract base class for alignments.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public abstract class AbstractAlign extends AbstractCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive for error messages
     */
    public AbstractAlign(final String codeName) {

        super(codeName);
    }

    /**
     * Getter for the Localizer.
     *
     * @return the localizer
     */
    Localizer getMyLocalizer() {

        return LocalizerFactory.getLocalizer(AbstractAlign.class.getName());
    }

    /**
     * Get the preamble.
     * The preamble is composed of PreambleItems.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the preamble as list of PreambleItems
     *
     * @throws InterpreterException in case of an error
     */
    protected List getPreamble(final Context context, final TokenSource source)
            throws InterpreterException {

        List preamble = new ArrayList();

        while (addPreambleItem(context, source, preamble)) {
            //nothing more to do
        }
        return preamble;
    }

    /**
     * Parse an item of a preamble and add it to the given list.
     * If the item is ended by a & then <code>true</code> is returned.
     * If the item is ended by a <tt>\cr</tt> then <code>false</code> is
     * returned.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param preamble the list to add something to
     *
     * @return <code>true</code> iff the item has been ended by <tt>&</tt>.
     *
     * @throws InterpreterException in case of an error
     */
    private boolean addPreambleItem(final Context context,
            final TokenSource source, final List preamble)
            throws InterpreterException {

        Tokens pre = new Tokens();
        Tokens post = new Tokens();

        for (Token t = source.getToken(context); t != null
                && !t.isa(Catcode.MACROPARAM); t = source.getToken(context)) {

            if (t.isa(Catcode.TABMARK)) {
                throw new HelpingException(getMyLocalizer(),
                        "TTP.MissingSharp", printableControlSequence(context));
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof Cr) {
                    throw new HelpingException(getMyLocalizer(),
                            "TTP.MissingSharp",
                            printableControlSequence(context));
                } else if (code != null && code.isOuter()) {
                    throw new HelpingException(getMyLocalizer(),
                            "TTP.OuterInPreamble",
                            printableControlSequence(context));
                }
            }
            pre.add(t);
        }

        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {

            if (t.isa(Catcode.MACROPARAM)) {
                throw new HelpingException(getMyLocalizer(),
                        "TTP.SecondSharpInTab",
                        printableControlSequence(context));
            } else if (t.isa(Catcode.TABMARK)) {
                preamble.add(new PreambleItem(pre, post));
                return true;
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof Cr) {
                    preamble.add(new PreambleItem(pre, post));
                    return false;
                } else if (code != null && code.isOuter()) {
                    throw new HelpingException(getMyLocalizer(),
                            "TTP.OuterInPreamble",
                            printableControlSequence(context));
                }
            }
            post.add(t);
        }

        throw new HelpingException(getMyLocalizer(), "TTP.EOFinPreamble",
                printableControlSequence(context));
    }

}
