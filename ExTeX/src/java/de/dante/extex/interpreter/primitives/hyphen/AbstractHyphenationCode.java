/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.util.UnicodeChar;
import de.dante.util.UnicodeCharList;

/**
 * TODO gene: Javadoc
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractHyphenationCode extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractHyphenationCode(final String name) {

        super(name);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param context the interpreter context
     *
     * @return the current hyphenation table
     *
     * @throws InterpreterException in case of an error
     */
    protected HyphenationTable getHyphenationTable(final Context context)
            throws InterpreterException {

        String index;
        Tokens lang = context.getToksOrNull("lang");
        if (lang != null) {
            index = lang.toText();
        } else {
            Count language = context.getCount("language");
            index = Long.toString(language.getValue());
        }
        return context.getHyphenationTable(index);
    }

    /**
     * Transform the <code>String</code> in lowercase (use lccode)
     * @param s         the <code>String</code>
     * @param context   the context
     * @return the lowercase string
     */
    protected String makeLowercase(final String s, final Context context) {

        UnicodeCharList ucl = new UnicodeCharList(s.length());
        for (int i = 0; i < s.length(); i++) {
            UnicodeChar uc = new UnicodeChar(s, i);
            UnicodeChar lc = context.getLccode(uc);
            ucl.add(lc.getCodePoint() > 0 ? lc : uc);
        }
        return ucl.toString();
    }

}