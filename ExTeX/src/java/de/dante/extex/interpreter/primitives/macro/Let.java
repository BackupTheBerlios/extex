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
package de.dante.extex.interpreter.primitives.macro;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.ActiveCharacterToken;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.ControlSequenceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;

import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\relax</code>.
 * It does simply nothing, but as a side effect all prefixes are zeroed.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Let extends AbstractCode implements CatcodeVisitor {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Let(String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context, TokenSource source,
                      Typesetter typesetter) throws GeneralException {
        Token t = source.getNextToken();

        if (t == null) {
            throw new GeneralException(); //TODO
        } else if (t instanceof ControlSequenceToken) {
            //TODO incomplete
        } else if (t instanceof ActiveCharacterToken) {
            //TODO incomplete
        } else {
            source.push(t);
            source.push(context.getTokenFactory().newInstance(Catcode.ESCAPE,
                                                              "inaccessible "));
            throw new GeneralHelpingException("TTP.MissingCtrlSeq");
        }

        Token t2 = source.getNextNonSpace();

        if (t2 != null && t2.equals(Catcode.OTHER, '=')) {
            t2 = source.getNextNonSpace();
        }

        if (t2 == null) {
            throw new GeneralException(); //TODO
        }

        Code code = (Code) (t2.getCatcode().visit(this, t2.getValue(),
                                                  context));

        if (t instanceof ControlSequenceToken) {
            context.setMacro(t.getValue(), code,prefix.isGlobal());
        } else if (t instanceof ActiveCharacterToken) {
                context.setActive(t.getValue(), code, prefix.isGlobal());
        } else {
            throw new GeneralHelpingException("TTP.Confusion", "");
        }

        prefix.clear();
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitActive(java.lang.Object, java.lang.Object)
     */
    public Object visitActive(Object oName, Object oContext)
                       throws GeneralException {
        Context context = (Context) oContext;
        Code code       = context.getActive((String) oName);

        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken",
                                              (String) oName);
        }

        return code;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitComment(java.lang.Object, java.lang.Object)
     */
    public Object visitComment(Object oName, Object oContext)
                        throws GeneralException {
        throw new GeneralHelpingException("TTP.Confusion", "");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitCr(java.lang.Object, java.lang.Object)
     */
    public Object visitCr(Object oName, Object oContext)
                   throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitEscape(java.lang.Object, java.lang.Object)
     */
    public Object visitEscape(Object oName, Object oContext)
                       throws GeneralException {
        Context context = (Context) oContext;
        Code code       = context.getMacro((String) oName);

        if (code == null) {
            throw new GeneralHelpingException("TTP.UndefinedToken",
                                              (String) oName);
        }

        return code;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitIgnore(java.lang.Object, java.lang.Object)
     */
    public Object visitIgnore(Object oName, Object oContext)
                       throws GeneralException {
        throw new GeneralHelpingException("TTP.Confusion", "");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitInvalid(java.lang.Object, java.lang.Object)
     */
    public Object visitInvalid(Object oName, Object oContext)
                        throws GeneralException {
        throw new GeneralHelpingException("TTP.Confusion", "");
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLeftBrace(java.lang.Object, java.lang.Object)
     */
    public Object visitLeftBrace(Object oName, Object oContext)
                          throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitLetter(java.lang.Object, java.lang.Object)
     */
    public Object visitLetter(Object oName, Object oContext)
                       throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMacroParam(java.lang.Object, java.lang.Object)
     */
    public Object visitMacroParam(Object oName, Object oContext)
                           throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitMathShift(java.lang.Object, java.lang.Object)
     */
    public Object visitMathShift(Object oName, Object oContext)
                          throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitOther(java.lang.Object, java.lang.Object)
     */
    public Object visitOther(Object oName, Object oContext)
                      throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitRigthBrace(java.lang.Object, java.lang.Object)
     */
    public Object visitRightBrace(Object oName, Object oContext)
                           throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSpace(java.lang.Object, java.lang.Object)
     */
    public Object visitSpace(Object oName, Object oContext)
                      throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSubMark(java.lang.Object, java.lang.Object)
     */
    public Object visitSubMark(Object oName, Object oContext)
                        throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitSupMark(java.lang.Object, java.lang.Object)
     */
    public Object visitSupMark(Object oName, Object oContext)
                        throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.scanner.CatcodeVisitor#visitTabMark(java.lang.Object, java.lang.Object)
     */
    public Object visitTabMark(Object oName, Object oContext)
                        throws GeneralException {
        // TODO Auto-generated method stub
        return null;
    }
}
