/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.ActiveCharacterToken;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.CrToken;
import de.dante.extex.scanner.LeftBraceToken;
import de.dante.extex.scanner.LetterToken;
import de.dante.extex.scanner.MacroParamToken;
import de.dante.extex.scanner.MathShiftToken;
import de.dante.extex.scanner.OtherToken;
import de.dante.extex.scanner.RightBraceToken;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.SubMarkToken;
import de.dante.extex.scanner.SupMarkToken;
import de.dante.extex.scanner.TabMarkToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.TokenVisitor;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\let</code>.
 *
 * <doc name="let">
 * <h3>The Primitive <tt>\let</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;let&rang;
 *      &rarr; <tt>\let</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getControlSequence()
 *        &lang;control sequence&rang;} {@linkplain
 *      de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *      &lang;equals&rang;} {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getToken()
 *       &lang;token&rang;}  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \let\a=\b  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Let extends AbstractAssignment implements TokenVisitor {

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
            throws GeneralException {

        CodeToken cs = source.getControlSequence();
        source.getOptionalEquals();
        Token t = source.getNonSpace();

        let(prefix, context, cs, t);
    }

    /**
     * ...
     *
     * @param prefix the flags to consider
     * @param context the processor context
     * @param cs the control sequence token to bind
     * @param t the new meaning of the control sequence token. If this
     *  parameter is <code>null</code> then an exception is thrown.
     *
     * @throws GeneralException in case of an error
     */
    protected void let(final Flags prefix, final Context context,
            final CodeToken cs, final Token t) throws GeneralException {

        if (t == null) {
            throw new HelpingException("UnexpectedEOF",
                    printableControlSequence(context));
        }

        Code code;
        try {
            code = (Code) t.visit(this, context, null);
        } catch (GeneralException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
        context.setCode(cs, code, prefix.isGlobal());
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public Object visitActive(final ActiveCharacterToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        Context context = (Context) oContext;
        Code code = context.getCode(token);

        if (code == null) {
            throw new HelpingException("TTP.UndefinedToken", token.toString());
        }

        return code;
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitCr(final CrToken token, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitEscape(final ControlSequenceToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        Context context = (Context) oContext;
        Code code = context.getCode(token);

        if (code == null) {
            throw new HelpingException("TTP.UndefinedToken", (token).toString());
        }

        return code;
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitLeftBrace(final LeftBraceToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitLetter(final LetterToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitMacroParam(final MacroParamToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitMathShift(final MathShiftToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitOther(final OtherToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitRightBrace(final RightBraceToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitSpace(final SpaceToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitSubMark(final SubMarkToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitSupMark(final SupMarkToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * ...
     *
     * @param token the token to find the code for
     * @param oContext the interpreter context
     * @param ignore this parameter is ignored
     * @return the code associated to the token
     * @throws GeneralException in case of an error
     */
    public final Object visitTabMark(final TabMarkToken token,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

}