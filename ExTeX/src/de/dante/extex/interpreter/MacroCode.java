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
package de.dante.extex.interpreter;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.MacroParamToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class MacroCode extends AbstractCode implements Code {
    /** The tokens the macro expands to */
    private Tokens body;

    /** the specification for the argument matching */
    private Tokens pattern;

    /** the flags contain additional information about the behaviour of the
     *  macro
     */
    private Flags flags = new Flags();

    /**
     * Creates a new object.
     *
     * @param name ...
     * @param flags ...
     * @param pattern ...
     * @param body ...
     */
    public MacroCode(final String name, final Flags flags,
            final Tokens pattern, final Tokens body) {
        super(name);
        this.pattern = pattern;
        this.body = body;
        this.flags = flags;
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter) {
        Tokens[] args = getArgs(source); //TODO

        for (int i = body.length() - 1; i >= 0; i--) {
            Token t = body.get(i);

            if (t instanceof MacroParamToken) {
                //if () {
                //}
            } else {
                source.push(body.get(i));
            }
        }

        prefix.clear();
    }

    /**
     * ...
     *
     * @param source ...
     *
     * @return ...
     */
    private Tokens[] getArgs(final TokenSource source) {
        Tokens[] ta = new Tokens[2];
        Tokens t    = new Tokens();
        //TODO
        return ta;
    }
}
