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

import java.util.StringTokenizer;

import de.dante.extex.hyphenation.HyphenationTable;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Count;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive <code>\patterns</code>.
 * <p>
 * Example:
 *
 * <pre>
 * \patterns{.ach4 .ad4der .af1t}
 * </pre>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class Patterns extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Patterns(final String name) {

        super(name);
    }

    /**
     * Scan the patterns for hyphenation and store this values
     * in the <code>HyphernationTable</code>.
     * The <code>HyphernationTable</code> are based on the
     * value from <code>\language</code>.
     *
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        String pattern = source.scanTokensAsString().trim();
        Count language = context.getCount("language");
        HyphenationTable ht = context.getHyphenationTable((int) language
                .getValue());

        StringTokenizer st = new StringTokenizer(pattern);
        while (st.hasMoreTokens()) {
            String pat = st.nextToken().trim();
            ht.addPattern(createPatternName(pat, context), createPatternEntry(
                    pat, context));
        }
    }

    /**
     * Cretate the entry for the <code>HyphenationTable</code>.
     * <p>
     * Each character must have before and after a digit. If no digit
     * is present, the value are zero.
     * <p>
     * A dot at the beginning shows, that the pattern is at the begin
     * of a word, the dot at the end, that the pattern is at the
     * end on the word.
     *
     * @param pattern   the pattern
     * @param context   the context
     * @return  the entry
     */
    private String createPatternEntry(final String pattern,
            final Context context) {

        StringBuffer sb = new StringBuffer();
        String ls = makeLowercase(pattern, context);
        if (ls.length() > 0) {
            boolean number = true;
            for (int i = 0; i < pattern.length(); i++) {
                UnicodeChar uc = new UnicodeChar(ls, i);
                if (uc.toString().equals(".")) {
                    sb.append('.');
                    number = true;
                    continue;
                }
                if (number) {
                    if (uc.isDigit()) {
                        sb.append(uc.toString());
                        number = false;
                    } else {
                        sb.append('0');
                        sb.append(uc.toString());
                        number = true;
                    }
                } else {
                    if (uc.isDigit()) {
                        System.err.println("Fehler");
                    } else {
                        sb.append(uc.toString());
                        number = true;
                    }
                }
            }
            UnicodeChar uc = new UnicodeChar(sb.toString(), sb.length() - 1);
            if (!uc.isDigit()) {
                if (uc.toString().equals(".")) {
                    uc = new UnicodeChar(sb.toString(), sb.length() - 2);
                    if (!uc.isDigit()) {
                        sb.insert(sb.length() - 1, '0');
                    }
                } else {
                    sb.append('0');
                }
            }
        }
        return sb.toString();
    }

    /**
     * Cretate the name for the <code>HyphenationTable</code>.
     * <p>
     * All digits from the <code>String</code> are removed.
     *
     * @param pattern   the pattern
     * @param context   the context
     * @return  the name
     */
    private String createPatternName(final String pattern, final Context context) {

        String rt = "";
        if (pattern != null) {
            rt = makeLowercase(pattern.replaceAll("[0-9]", ""), context);
        }
        return rt;
    }

    /**
     * Transform the <code>String</code> in lowercase (use lccode).
     *
     * @param s         the <code>String</code>
     * @param context   the context
     * @return the lowercase string
     */
    private String makeLowercase(final String s, final Context context) {

        // for (int i=0; i< s.length(); i++) {
        // UnicodeChar uc = new UnicodeChar(s,i);
        // int lc = context.get
        // }
        return s.toLowerCase();
        //TODO change toLowerCase to lccode
    }
}
