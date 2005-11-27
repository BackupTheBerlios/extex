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

package de.dante.extex.interpreter.primitives.dynamic;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.token.ActiveCharacterToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.CrToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.MacroParamToken;
import de.dante.extex.scanner.type.token.MathShiftToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.SubMarkToken;
import de.dante.extex.scanner.type.token.SupMarkToken;
import de.dante.extex.scanner.type.token.TabMarkToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenVisitor;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class reads token until a null token
 * and return the text (toString()) for each token.
 * <p>For test cases only</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class ReadTokenUntilNull extends AbstractCode {

    /**
     * Create a new object.
     * @param codeName  the CodeName
     */
    public ReadTokenUntilNull(final String codeName) {

        super(codeName);
    }

    /**
     * initial size for the buffer
     */
    private static final int INITSIZE = 1024;

    /**
     * @see de.dante.extex.interpreter.type.AbstractCode#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        try {
            StringBuffer buf = new StringBuffer(INITSIZE);

            TokenVisitor visitor = new MyTokenVisitor();

            Token t = null;
            do {
                t = source.getToken(context);
                if (t != null) {
                    buf.append(t.visit(visitor, null));
                }

            } while (t != null);

            TokenStreamFactory factory = source.getTokenStreamFactory();

            source.addStream(factory.newInstance(buf.toString()));
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * TokenVistor
     * <p>
     * Returns the content of a token as special String.
     * </p>
     */
    private class MyTokenVisitor implements TokenVisitor {

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitActive(
         *      de.dante.extex.scanner.type.token.ActiveCharacterToken,
         *      java.lang.Object)
         */
        public Object visitActive(final ActiveCharacterToken token,
                final Object arg) throws Exception {

            return "[A:" + token.toText() + "]";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitCr(
         *      de.dante.extex.scanner.type.token.CrToken,
         *      java.lang.Object)
         */
        public Object visitCr(final CrToken token, final Object arg)
                throws Exception {

            return "[CR]";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitEscape(
         *      de.dante.extex.scanner.type.token.ControlSequenceToken,
         *      java.lang.Object)
         */
        public Object visitEscape(final ControlSequenceToken token,
                final Object arg) throws Exception {

            return "/" + token.getName() + " ";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLeftBrace(
         *      de.dante.extex.scanner.type.token.LeftBraceToken,
         *      java.lang.Object)
         */
        public Object visitLeftBrace(final LeftBraceToken token,
                final Object arg) throws Exception {

            return "(";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitLetter(
         *      de.dante.extex.scanner.type.token.LetterToken,
         *      java.lang.Object)
         */
        public Object visitLetter(final LetterToken token, final Object arg)
                throws Exception {

            return token.toText();
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMacroParam(
         *      de.dante.extex.scanner.type.token.MacroParamToken,
         *      java.lang.Object)
         */
        public Object visitMacroParam(final MacroParamToken token,
                final Object arg) throws Exception {

            return "[M:" + token.toText() + "]";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitMathShift(
         *      de.dante.extex.scanner.type.token.MathShiftToken,
         *      java.lang.Object)
         */
        public Object visitMathShift(final MathShiftToken token,
                final Object arg) throws Exception {

            return token.toText();
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitOther(
         *      de.dante.extex.scanner.type.token.OtherToken,
         *      java.lang.Object)
         */
        public Object visitOther(final OtherToken token, final Object arg)
                throws Exception {

            return token.toText();
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitRightBrace(
         *      de.dante.extex.scanner.type.token.RightBraceToken,
         *      java.lang.Object)
         */
        public Object visitRightBrace(final RightBraceToken token,
                final Object arg) throws Exception {

            return ")";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSpace(
         *      de.dante.extex.scanner.type.token.SpaceToken,
         *      java.lang.Object)
         */
        public Object visitSpace(final SpaceToken token, final Object arg)
                throws Exception {

            return " ";
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSubMark(
         *      de.dante.extex.scanner.type.token.SubMarkToken,
         *      java.lang.Object)
         */
        public Object visitSubMark(final SubMarkToken token, final Object arg)
                throws Exception {

            return token.toText();
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitSupMark(
         *      de.dante.extex.scanner.type.token.SupMarkToken,
         *      java.lang.Object)
         */
        public Object visitSupMark(final SupMarkToken token, final Object arg)
                throws Exception {

            return token.toText();
        }

        /**
         * @see de.dante.extex.scanner.type.token.TokenVisitor#visitTabMark(
         *      de.dante.extex.scanner.type.token.TabMarkToken,
         *      java.lang.Object)
         */
        public Object visitTabMark(final TabMarkToken token, final Object arg)
                throws Exception {

            return token.toText();
        }
    }
}
