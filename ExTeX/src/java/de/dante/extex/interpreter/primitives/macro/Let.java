/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.primitives.macro;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractAssignment;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\let</code>.
 *
 * <doc>
 * <h3>The Primitive <tt>\let</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\let</tt> &lang;control sequence&rang; ... </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \let\a=\b  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class Let extends AbstractAssignment implements CatcodeVisitor {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Let(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.getControlSequence();
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
            final Token cs, final Token t) throws GeneralException {

        if (t == null) {
            throw new GeneralHelpingException("UnexpectedEOF",
                    printableControlSequence(context));
        }

        Code code;
        try {
            code = (Code) (t.getCatcode().visit(this, t, context,
                                                null));
        } catch (Exception e) {
            throw new GeneralException(e);
        }
        if (cs instanceof CodeToken) {
            context.setCode(cs, code, prefix.isGlobal());
        } else {
            throw new GeneralHelpingException("TTP.Confusion", //
                    getClass().getName() + "#assign()");
        }
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object,
     *       java.lang.Object, java.lang.Object)
     */
    public Object visitActive(final Object oToken, final Object oContext,
            final Object ignore) throws GeneralException {

        Context context = (Context) oContext;
        Code code = context.getCode((Token) oToken);

        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken",
                    ((Token) oToken).toString());
        }

        return code;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitComment(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new GeneralHelpingException("TTP.Confusion", "");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitCr(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitEscape(final Object oToken, final Object oContext,
            final Object ignore) throws GeneralException {

        Context context = (Context) oContext;
        Code code = context.getCode((Token) oToken);

        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken",
                    ((Token) oToken).toString());
        }

        return code;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitIgnore(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new GeneralHelpingException("TTP.Confusion", "");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitInvalid(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new GeneralHelpingException("TTP.Confusion", "");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitLeftBrace(final Object oName,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitLetter(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitMacroParam(final Object oName,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(
     *     java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public final Object visitMathShift(final Object oName,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitOther(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRightBrace(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public final Object visitRightBrace(final Object oName,
            final Object oContext, final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitSpace(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitSubMark(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitSupMark(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object,
     *      java.lang.Object, java.lang.Object)
     */
    public final Object visitTabMark(final Object oName, final Object oContext,
            final Object ignore) throws GeneralException {

        throw new RuntimeException("unimplemented");
    }

}