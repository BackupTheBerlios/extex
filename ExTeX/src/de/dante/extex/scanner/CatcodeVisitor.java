/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.scanner;

import de.dante.util.GeneralException;

/**
 * This interface describes the capabilities for a visitor class on the category
 * codes.
 * This interface is used to implement the visitor pattern.
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface CatcodeVisitor {

    public abstract Object visitEscape(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitLeftBrace(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitRightBrace(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitMathShift(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitTabMark(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitCr(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitMacroParam(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitSupMark(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitSubMark(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitIgnore(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitSpace(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitLetter(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitOther(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitActive(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitComment(Object arg1, Object arg2) throws GeneralException;

    public abstract Object visitInvalid(Object arg1, Object arg2) throws GeneralException;

}
