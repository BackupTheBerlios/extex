/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;

/**
 * This is the abstract base class for all hyphenation related primitives.
 * It provides common methods.
 *
 * <h2>Determining the Current Language</h2>
 *
 * <p>
 *  In <logo>TeX</logo> the language is determined by the count register named
 *  <tt>language</tt>. This has the disadvantage that the language is named
 *  anonymously by an integer.
 * </p>
 * <p>
 *  This base class implements an extension to this scheme. First the toks
 *  register <tt>lang</tt> is sought. If this register is defined and not
 *  empty then the contents is used as name of the current language. Otherwise
 *  the count register <tt>language</tt> is used for this purpose.
 * </p>
 *
 *
 * <doc name="lang" type="register">
 * <h3>The Tokens Register <tt>\lang</tt></h3>
 * <p>
 *  The tokens register <tt>\lang</tt> is the primary source of information to
 *  determine the current language. If this register is not defined or has the
 *  empty value then the count register <tt>\language</tt> is used instead.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;lang&rang;
 *      &rarr; <tt>\lang</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getTokens(Context,TokenSource,Typesetter)
 *        &lang;tokens&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \lang={de}  </pre>
 *
 * </doc>
 *
 *
 * <doc name="language" type="register">
 * <h3>The Count Register <tt>\language</tt></h3>
 * <p>
 *  The count register <tt>\language</tt> is the secondary source of information
 *  to determine the current language. If this tokens register <tt>\lang</tt> is
 *  not defined or has the empty value then this is used instead.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;language&rang;
 *      &rarr; <tt>\language</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *        &lang;number&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \language=1  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public abstract class AbstractHyphenationCode extends AbstractCode {

    /**
     * The field <tt>LANGUAGE_COUNT</tt> contains the name of the count register
     * to determine the language.
     */
    private static final String LANGUAGE_COUNT = "language";

    /**
     * The field <tt>LANGUAGE_TOKS</tt> contains the name of the tokens register
     * to determine the language.
     */
    private static final String LANGUAGE_TOKS = "lang";

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public AbstractHyphenationCode(final String name) {

        super(name);
    }

    /**
     * Getter for the current hyphenation table.
     *
     * @param context the interpreter context
     *
     * @return the current hyphenation table
     *
     * @throws InterpreterException in case of an error
     */
    protected Language getHyphenationTable(final Context context)
            throws InterpreterException {

        Tokens lang = context.getToksOrNull(LANGUAGE_TOKS);
        String name = (lang != null //
                ? lang.toText() //
                : Long.toString(context.getCount(LANGUAGE_COUNT).getValue()));

        return context.getLanguage(name);
    }

}
