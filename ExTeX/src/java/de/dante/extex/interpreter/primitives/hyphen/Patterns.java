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
import de.dante.extex.hyphenation.exception.DuplicateHyphenationException;
import de.dante.extex.hyphenation.exception.IllegalTokenHyphenationException;
import de.dante.extex.hyphenation.exception.IllegalValueHyphenationException;
import de.dante.extex.hyphenation.exception.ImmutableHyphenationException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.MissingLeftBraceException;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.ActiveCharacterToken;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.ControlSequenceToken;
import de.dante.extex.scanner.type.CrToken;
import de.dante.extex.scanner.type.LeftBraceToken;
import de.dante.extex.scanner.type.LetterToken;
import de.dante.extex.scanner.type.MacroParamToken;
import de.dante.extex.scanner.type.MathShiftToken;
import de.dante.extex.scanner.type.OtherToken;
import de.dante.extex.scanner.type.RightBraceToken;
import de.dante.extex.scanner.type.SpaceToken;
import de.dante.extex.scanner.type.SubMarkToken;
import de.dante.extex.scanner.type.SupMarkToken;
import de.dante.extex.scanner.type.TabMarkToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.scanner.type.TokenVisitor;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive <code>\patterns</code>.
 *
 * <p>Example:</p>
 * <pre>
 * \patterns{.ach4 .ad4der .af1t}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.13 $
 */
public class Patterns extends AbstractHyphenationCode {

    /**
     * This class provides the token visitor which processes all tokens in the
     * argument of the <tt>\pattern</tt> macro.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.13 $
     */
    private class TV implements TokenVisitor {

        /**
         * The field <tt>table</tt> contains the ...
         */
        private HyphenationTable table;

        /**
         * The field <tt>toks</tt> contains the the container for the tokens.
         */
        private Tokens tokens = new Tokens();

        /**
         * The field <tt>context</tt> contains the interpreter context to use.
         */
        private Context context;

        /**
         * The field <tt>zero</tt> contains the OtherToken 0.
         */
        private Token zero;

        /**
         * The field <tt>letter</tt> contains the indicator that the character
         * read recently has been a letter.
         */
        private boolean letter = true;

        /**
         * Creates a new object.
         *
         * @param context the interpreter context
         * @param table the hyphenation table to feed
         *
         * @throws CatcodeException in case of a problem when creating the zero
         *  token.
         */
        public TV(final Context context, final HyphenationTable table)
                throws CatcodeException {

            super();
            this.table = table;
            this.context = context;
            zero = context.getTokenFactory().createToken(Catcode.OTHER, '0',
                    Namespace.DEFAULT_NAMESPACE);
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitActive(
         *      de.dante.extex.scanner.type.ActiveCharacterToken,
         *      java.lang.Object)
         */
        public Object visitActive(final ActiveCharacterToken token,
                final Object arg) throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitCr(
         *      de.dante.extex.scanner.type.CrToken,
         *      java.lang.Object)
         */
        public Object visitCr(final CrToken token, final Object arg)
                throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitEscape(
         *      de.dante.extex.scanner.type.ControlSequenceToken,
         *      java.lang.Object)
         */
        public Object visitEscape(final ControlSequenceToken token,
                final Object arg) throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitLeftBrace(
         *      de.dante.extex.scanner.type.LeftBraceToken,
         *      java.lang.Object)
         */
        public Object visitLeftBrace(final LeftBraceToken token,
                final Object arg) throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitLetter(
         *      de.dante.extex.scanner.type.LetterToken,
         *      java.lang.Object)
         */
        public Object visitLetter(final LetterToken token, final Object arg)
                throws Exception {

            if (letter) {
                tokens.add(zero);
            }

            UnicodeChar c = token.getChar();
            UnicodeChar lc = context.getLccode(c);
            if (lc == null) {
                throw new InterpreterException(getLocalizer().format(
                        "TTP.NonLetterInHyph", token.toString()));
            } else if (c.equals(lc)) {
                tokens.add(token);
            } else {
                tokens.add(context.getTokenFactory().createToken(
                        Catcode.LETTER, lc, Namespace.DEFAULT_NAMESPACE));
            }
            letter = true;
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitMacroParam(
         *      de.dante.extex.scanner.type.MacroParamToken,
         *      java.lang.Object)
         */
        public Object visitMacroParam(final MacroParamToken token,
                final Object arg) throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitMathShift(
         *      de.dante.extex.scanner.type.MathShiftToken,
         *      java.lang.Object)
         */
        public Object visitMathShift(final MathShiftToken token,
                final Object arg) throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitOther(
         *      de.dante.extex.scanner.type.OtherToken,
         *      java.lang.Object)
         */
        public Object visitOther(final OtherToken token, final Object arg)
                throws Exception {

            UnicodeChar c = token.getChar();

            if (c.getCodePoint() == '.') {
                if (letter) {
                    tokens.add(zero);
                }
                tokens.add(context.getTokenFactory().createToken(
                        Catcode.LETTER, '.', Namespace.DEFAULT_NAMESPACE));
                letter = true;
            } else if (letter) {
                if (c.isDigit()) {
                    tokens.add(token);
                    letter = false;
                } else {
                    throw new InterpreterException(getLocalizer().format(
                            "TTP.NonLetter", token.toString()));
                }
            } else {
                UnicodeChar lc = context.getLccode(c);
                if (lc == null) {
                    throw new InterpreterException(getLocalizer().format(
                            "TTP.NonLetterInHyph", token.toString()));
                } else {
                    tokens.add(context.getTokenFactory().createToken(
                            Catcode.LETTER, c.equals(lc) ? c : lc,
                            Namespace.DEFAULT_NAMESPACE));
                }
                letter = true;
            }

            return null;
        }

        /**
         * This method returns a non-null value to indicate the end of
         * processing.
         *
         * @see de.dante.extex.scanner.type.TokenVisitor#visitRightBrace(
         *      de.dante.extex.scanner.type.RightBraceToken,
         *      java.lang.Object)
         */
        public Object visitRightBrace(final RightBraceToken token,
                final Object arg) throws Exception {

            if (tokens.length() > 0) {
                table.addPattern(tokens);
                tokens.clear();
            }
            letter = true;
            return this;
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitSpace(
         *      de.dante.extex.scanner.type.SpaceToken,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceToken token, final Object arg)
                throws Exception {

            if (tokens.length() > 0) {
                table.addPattern(tokens);
                tokens.clear();
            }
            letter = true;
            return null;
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitSubMark(
         *      de.dante.extex.scanner.type.SubMarkToken,
         *      java.lang.Object)
         */
        public Object visitSubMark(final SubMarkToken token, final Object arg)
                throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitSupMark(
         *      de.dante.extex.scanner.type.SupMarkToken,
         *      java.lang.Object)
         */
        public Object visitSupMark(final SupMarkToken token, final Object arg)
                throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }

        /**
         * @see de.dante.extex.scanner.type.TokenVisitor#visitTabMark(
         *      de.dante.extex.scanner.type.TabMarkToken,
         *      java.lang.Object)
         */
        public Object visitTabMark(final TabMarkToken token, final Object arg)
                throws Exception {

            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter", token.toString()));
        }
    }

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
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(printableControlSequence(context));
        } else if (!t.isa(Catcode.LEFTBRACE)) {
            throw new MissingLeftBraceException("???");
        }

        try {
            TV tv = new TV(context, getHyphenationTable(context));

            do {
                t = source.getToken(context);
            } while (t != null && t.visit(tv, null) == null);

        } catch (InterpreterException e) {
            throw e;
        } catch (DuplicateHyphenationException e) {
            throw new InterpreterException(getLocalizer().format(
                    "TTP.DuplicatePattern"));
        } catch (IllegalTokenHyphenationException e) {
            throw new InterpreterException(getLocalizer().format(
                    "TTP.NonLetter"));
        } catch (IllegalValueHyphenationException e) {
            throw new InterpreterException(getLocalizer().format(
                    "TTP.BadPatterns"));
        } catch (ImmutableHyphenationException e) {
            throw new InterpreterException(getLocalizer().format(
                    "TTP.LatePatterns"));
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

}