/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.macro;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.primitives.register.CharCode;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.token.ActiveCharacterToken;
import de.dante.extex.scanner.type.token.CodeToken;
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
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\let</code>.
 *
 * <doc name="let">
 * <h3>The Primitive <tt>\let</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;let&rang;
 *      &rarr; <tt>\let</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *        &lang;control sequence&rang;} {@linkplain
 *      de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *      &lang;equals&rang;} {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken(Context)
 *       &lang;token&rang;}  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \let\a=\b  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.35 $
 */
public class Let extends AbstractAssignment {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>VISITOR</tt> contains the anonymous inner class of the
     * token visitor for the let command.
     */
    public static final TokenVisitor VISITOR = new TokenVisitor() {

        /**
         * The right hand side of a <code>\let</code> definition is an
         * active character which will be used as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public Object visitActive(final ActiveCharacterToken token,
                final Object oContext) throws GeneralException {

            return ((Context) oContext).getCode(token);
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * CR character.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitCr(final CrToken token, final Object oContext)
                throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * control sequence which will be used as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitEscape(final ControlSequenceToken token,
                final Object oContext) throws GeneralException {

            return ((Context) oContext).getCode(token);
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * left brace character which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitLeftBrace(final LeftBraceToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * letter which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitLetter(final LetterToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * macro parameter character which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitMacroParam(final MacroParamToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * math shift character which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitMathShift(final MathShiftToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is an
         * other character which will be used as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitOther(final OtherToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * right brace character which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitRightBrace(final RightBraceToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * space character. This can not happen since spaces are absorbed here.
         * Thus an error is raised.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitSpace(final SpaceToken token,
                final Object oContext) throws GeneralException {

            throw new ImpossibleException("unexpected space found");
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * subscript mark which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitSubMark(final SubMarkToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * superscript mark which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitSupMark(final SupMarkToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }

        /**
         * The right hand side of a <code>\let</code> definition is a
         * tab mark which will be used in the form of an other
         * character as new binding.
         *
         * @param token the token to find the code for
         * @param oContext the interpreter context
         *
         * @return the code associated to the token
         *
         * @throws GeneralException in case of an error
         */
        public final Object visitTabMark(final TabMarkToken token,
                final Object oContext) throws GeneralException {

            return new CharCode("", token.getChar());
        }
    };

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Let(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        CodeToken cs = source.getControlSequence(context);
        source.getOptionalEquals(context);
        Token t = source.getNonSpace(context);

        let(prefix, context, cs, t);
    }

    /**
     * Assign a new meaning to a control sequence.
     * This is the core of the primitive <code>\let</code>.
     *
     * @param prefix the flags to consider
     * @param context the processor context
     * @param cs the control sequence token to bind
     * @param t the new meaning of the control sequence token. If this
     *  parameter is <code>null</code> then an exception is thrown.
     *
     * @throws InterpreterException in case of an error
     */
    protected void let(final Flags prefix, final Context context,
            final CodeToken cs, final Token t) throws InterpreterException {

        if (t == null) {
            throw new EofException(printableControlSequence(context));
        }

        Code code;
        try {

            code = (Code) t.visit(VISITOR, context);

        } catch (InterpreterException e) {
            throw e;
        } catch (Exception e) {
            throw new InterpreterException(e);
        }
        context.setCode(cs, code, prefix.isGlobal());
        prefix.clearGlobal();
    }

}
