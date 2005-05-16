/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.scanner.type;

/**
 * This interface describes the capabilities for a visitor class on the token
 * types.
 * This interface is used to implement the visitor pattern.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface TokenVisitor {

    /**
     * This visit method is invoked on an active token.
     * In <logo>TeX</logo> this is e.g. ~.
     * @param token the active token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitActive(ActiveCharacterToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a cr token.
     * @param token the cr token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitCr(CrToken token, Object arg) throws Exception;

    /**
     * This visit method is invoked on an escape token.
     * In <logo>TeX</logo> this normally means a control sequence.
     * @param token the control sequence token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitEscape(ControlSequenceToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a left brace token.
     * @param token the left brace token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitLeftBrace(LeftBraceToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a letter token.
     * @param token the letter token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitLetter(LetterToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a macro parameter token.
     * In <logo>TeX</logo> this normally is a #.
     * @param token the macro param token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitMacroParam(MacroParamToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a math shift token.
     * In <logo>TeX</logo> this normally is a $.
     * @param token the math shift token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitMathShift(MathShiftToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on an other token.
     * @param token the other token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitOther(OtherToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a right brace token.
     * @param token the right brace token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitRightBrace(RightBraceToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a space token.
     * @param token the space token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitSpace(SpaceToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a sub mark token.
     * In <logo>TeX</logo> this normally is a _.
     * @param token the sub mark token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitSubMark(SubMarkToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a sup mark token.
     * In <logo>TeX</logo> this normally is a ^.
     * @param token the sup mark token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitSupMark(SupMarkToken token, Object arg)
            throws Exception;

    /**
     * This visit method is invoked on a tab mark token.
     * In <logo>TeX</logo> this normally is a &.
     * @param token the tab mark token to visit
     * @param arg the first argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitTabMark(TabMarkToken token, Object arg)
            throws Exception;

}