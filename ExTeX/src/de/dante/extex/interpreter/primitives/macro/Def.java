/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.MacroCode;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.LeftBraceToken;
import de.dante.extex.scanner.MacroParamToken;
import de.dante.extex.scanner.OtherToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\def</code>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class Def extends AbstractCode {
    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Def(final String name) {
        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
        final TokenSource source, final Typesetter typesetter)
        throws GeneralException {
        Token cs = source.getControlSequence();
        String name = cs.getValue();
        Tokens pattern = getPattern(context, source, name, prefix.isLong());
        Tokens body = (prefix.isExpanded() //
            ? expandedBody(source)//
            : source.getTokens());
        context.setMacro(name, new MacroCode(name, prefix, pattern, body),
                         prefix.isGlobal());
        prefix.clear();
    }

    private Tokens expandedBody(TokenSource source) {
        //TODO
        return null;
    }

    /**
     * ...
     * 
     * @param source the source for new tokens
     * 
     * @return the tokens read
     * 
     * @throws GeneralException in case of an error
     */
    protected Tokens getPattern(final Context context, final TokenSource source,
        final String name, final boolean notLong) throws GeneralException {
        Tokens toks = new Tokens();
        int no = 1;
        boolean afterHash = false;

        for (Token t = source.getToken(); t != null; t = source.getToken()) {
            if (t instanceof LeftBraceToken) {
                source.push(t);
                return toks;
            }
            /*
            if (notLong && t.equals(Catcode.ESCAPE,"par") ) {
                throw new GeneralHelpingException("");
            }
            */

            if (afterHash) {
                if (t instanceof MacroParamToken) {
                    //ok
                } else if (t instanceof OtherToken) {
                    if (t.getValue().charAt(0) != '0' + no) {
                        //TODO error
                    }
                    no++;
                } else {
                    //TODO error
                }
            }
            afterHash = (t instanceof MacroParamToken);
            toks.add(t);
        }

        char esc = (char) (context.getCount("escapechar").getValue());
        throw new GeneralHelpingException("TTP.EOFinDef", //
            Character.toString(esc) + name);
    }
    
}
