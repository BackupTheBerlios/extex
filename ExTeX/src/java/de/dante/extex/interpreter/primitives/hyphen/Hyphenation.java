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

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.primitives.register.CharCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.LeftBraceToken;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.OtherToken;
import de.dante.extex.scanner.type.RightBraceToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\hyphenation</code>.
 *
 * <doc name="hyphenation">
 * <h3>The Primitive <tt>\hyphenation</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  <pre class="syntax">
 *    &lang;hyphenation&rang;
 *     &rarr; <tt>\hyphenation</tt> {...} </pre>
 *
 * <h4>Example:</h4>
 * <pre>
 * \hyphenation{as-so-ciate as-so-ciates}  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.12 $
 */
public class Hyphenation extends AbstractHyphenationCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Hyphenation(final String name) {

        super(name);
    }

    /**
     * Create the name for the <code>HyphenationTable</code>.
     *
     * @param pattern the pattern
     * @param context the interpreter context
     *
     * @return the name
     *
     * @throws CatcodeException in case of an error
     */
    private Tokens createHyphenation(final Tokens pattern, final Context context)
            throws CatcodeException {

        Tokens ret = new Tokens();
        TokenFactory tokenFactory = context.getTokenFactory();
        Token t;

        for (int i = 0; i < pattern.length(); i++) {
            t = pattern.get(i);
            if (!t.equals(Catcode.OTHER, '-')) {

                UnicodeChar uc = t.getChar();
                UnicodeChar lc = context.getLccode(uc);
                ret.add(tokenFactory.createToken(Catcode.OTHER, //
                        lc == null ? uc : lc, ""));
            }
        }

        return ret;
    }

    /**
     * Scan for hyphenation values and store these values in the
     * <code>HyphernationTable</code>.
     * The index for the <code>HyphernationTable</code> is based on the
     * value from the count register <code>\language</code>.
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

        Language table = getHyphenationTable(context);

        Token t = source.getNonSpace(context);
        if (!(t instanceof LeftBraceToken)) {
            throw new MissingLeftBraceException(
                    printableControlSequence(context));
        }

        try {
            for (t = source.getNonSpace(context); !(t instanceof RightBraceToken); t = source
                    .getNonSpace(context)) {
                if (!isWordConstituent(t, context)) {
                    throw new InterpreterException(getLocalizer().format(
                            "TTP.ImproperHyphen",
                            printableControlSequence(context)));
                }
                Tokens word = new Tokens();
                do {
                    word.add(t);
                    t = source.getToken(context);
                } while (isWordConstituent(t, context));

                table.addHyphenation(createHyphenation(word, context),
                        (TypesetterOptions) context);
            }
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        } catch (HyphenationException e) {
            throw new InterpreterException(e);
        }

    }

    /**
     * This method checks that the given token is a word constituent.
     * This means that the token is either
     * <ul>
     * <li>a letter token, or</li>
     * <li>a other token, or</li>
     * <li>a code token defined with <tt>\chardef</tt>.</li>
     * </ul>
     *
     * @param t the token to analyze
     * @param context the interpreter context
     *
     * @return <code>true</code> iff the token is
     *
     * @throws InterpreterException in case of an error
     */
    private boolean isWordConstituent(final Token t, final Context context)
            throws InterpreterException {

        if (t == null) {
            return false;
        } else if (t instanceof LetterToken || t instanceof OtherToken) {
            return true;
        } else if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            return (code instanceof CharCode);
        }

        return false;
    }

}