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

package de.dante.extex.interpreter.primitives.hyphen;

import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\hyphenactive</code>.
 * <p>
 * The value are stored in the <code>HyphernationTable</code>.
 * Each <code>HyphernationTable</code> are based on <code>\language</code>
 * and have its own <code>\hyphenactive</code>-value.
 *
 * <p>Example:</p>
 * <pre>
 * \hyphenactive=0  % yes
 * \hyphenactive=1  % no
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class HyphenActive extends AbstractCode implements Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public HyphenActive(final String name) {

        super(name);
    }

    /**
     * Scan for hyphenactive-value and stored it in the
     * <code>HyphernationTable</code> with the language-number.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Count language = context.getCount("language");
        HyphenationTable ht = context.getHyphenationTable((int) language
                .getValue());

        source.getOptionalEquals(context);
        boolean active = true;
        if (source.scanInteger(context) != 0) {
            active = false;
        }
        ht.setHyphenActive(active);
    }

    /**
     * Return the <code>Tokens</code> to show the content with <code>\the</code>.
     *
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        try {
            Count language = context.getCount("language");
            HyphenationTable ht = context.getHyphenationTable((int) language
                    .getValue());
            String value = "0";
            if (!ht.isHyphenActive()) {
                value = "1";
            }
            return new Tokens(context, value);
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }
}