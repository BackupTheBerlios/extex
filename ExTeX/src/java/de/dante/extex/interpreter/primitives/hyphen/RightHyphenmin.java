/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\righthyphenmin</code>.
 *
 * The value are stored in the <code>HyphernationTable</code>.
 * Each <code>HyphernationTable</code> are based on <code>\language</code>
 * and have its own <code>\righthyphenmin</code>-value (different to original TeX).
 *
 * <p>Example:</p>
 * <pre>
 * \righthyphenmin=3
 * </pre>
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.8 $
 */
public class RightHyphenmin extends AbstractCode implements Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public RightHyphenmin(final String name) {

        super(name);
    }

    /**
     * Scan for righthyphenmin-value and stored it in the
     * <code>HyphernationTable</code> with the language-number.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Count language = context.getCount("language");
        HyphenationTable ht = context.getHyphenationTable((int) language
                .getValue());

        source.getOptionalEquals();
        int righthyphmin = (int) source.scanInteger();

        ht.setRightHyphenmin(righthyphmin);
        return true;
    }

    /**
     * Return the <code>Tokens</code> to show the content with <code>\the</code>.
     *
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Tokens the(final Context context, final TokenSource source)
            throws GeneralException {

        Count language = context.getCount("language");
        HyphenationTable ht = context.getHyphenationTable((int) language
                .getValue());
        return new Tokens(context, String.valueOf(ht.getRightHyphenmin()));
    }
}
