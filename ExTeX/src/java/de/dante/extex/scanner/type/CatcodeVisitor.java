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

package de.dante.extex.scanner.type;

/**
 * This interface describes the capabilities for a visitor class on the category
 * codes.
 * This interface is used to implement the visitor pattern.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface CatcodeVisitor {

    /**
     * This visit method is invoked on an escape token.
     * In TeX this normally means a control sequence.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitEscape(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a left brace token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitLeftBrace(Object arg1, Object arg2, Object arg3)
            throws Exception;

    /**
     * This visit method is invoked on a right brace token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitRightBrace(Object arg1, Object arg2, Object arg3)
            throws Exception;

    /**
     * This visit method is invoked on a math shift token.
     * In TeX this normally is a $.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitMathShift(Object arg1, Object arg2, Object arg3)
            throws Exception;

    /**
     * This visit method is invoked on a tab mark token.
     * In TeX this normally is a &.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitTabMark(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a cr token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitCr(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a macro parameter token.
     * In TeX this normally is a #.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitMacroParam(Object arg1, Object arg2, Object arg3)
            throws Exception;

    /**
     * This visit method is invoked on a sup mark token.
     * In TeX this normally is a ^.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitSupMark(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a sub mark token.
     * In TeX this normally is a _.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitSubMark(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on an ignore token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitIgnore(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a space token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitSpace(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a letter token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitLetter(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on an other token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitOther(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on an active token.
     * In TeX this is e.g. ~.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitActive(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on a comment token.
     * In TeX this normally is a %.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitComment(Object arg1, Object arg2, Object arg3) throws Exception;

    /**
     * This visit method is invoked on an invalid token.
     *
     * @param arg1 the first argument to pass
     * @param arg2 the second argument to pass
     * @param arg3 the third argument to pass
     *
     * @return some value
     *
     * @throws Exception in case of an error
     */
    Object visitInvalid(Object arg1, Object arg2, Object arg3) throws Exception;

}