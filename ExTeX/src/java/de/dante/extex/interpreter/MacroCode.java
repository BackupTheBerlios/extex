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
package de.dante.extex.interpreter;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Tokens;
import de.dante.extex.scanner.LeftBraceToken;
import de.dante.extex.scanner.MacroParamToken;
import de.dante.extex.scanner.OtherToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class MacroCode extends AbstractCode implements Code {
    /**
     * The field <tt>body</tt> contains the tokens of the macro expansion text.
     */
    private Tokens body;

    /**
     * The field <tt>pattern</tt> contains the specification for the argument
     * matching.
     */
    private Tokens pattern;

    /**
     * The field <tt>longP</tt> contains the ...
     */
    private boolean longP;

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
        longP = flags.isLong(); 
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
        Tokens[] args = getArgs(context, source);
        int len = body.length();
        Tokens toks = new Tokens();
        int no = 1;

        for (int i = 0; i < len; i++) {
            Token t = body.get(i);

            if (t instanceof MacroParamToken) {
                t = body.get(++i);
                if (t == null) {
                    //TODO error
                } else if (t instanceof MacroParamToken) {
                    toks.add(t);
                } else if (t instanceof OtherToken
                           && t.getValue().matches("[1-9]")) {
                    no = t.getValue().charAt(0) - '0';
                    if ( args[no] == null ) {
                        //TODO: error
                    }
                    toks.add(args[no]);
                } else {
                    //TODO: error
                }
            } else {
                toks.add(t);
            }
        }

        source.push(toks);
        prefix.clear();
    }

    /**
     * ...
     * 
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return ...
     *
     * @throws GeneralException ...
     */
    private Tokens[] getArgs(final Context context, final TokenSource source)
        throws GeneralException {
        Tokens[] ta = new Tokens[9];
        Token ti;
        Token t;
        //int i = 1;
        int len = pattern.length();

        for (int pi = 0; pi < len; pi++) {
            ti = pattern.get(pi);
            if (ti instanceof MacroParamToken) {
                if (++pi >= len) {
                    throw new GeneralHelpingException("TTP.UseDoesntMatch",
                        "\\" + getName()); //TODO; maybe another error text?
                }
                ti = pattern.get(pi);
                if (ti instanceof MacroParamToken) {
                    t = source.getToken();
                    if (!ti.equals(t)) {
                        char esc = (char) (context.getCount("escapechar")
                            .getValue());
                        throw new GeneralHelpingException("TTP.UseDoesntMatch",
                            Character.toString(esc) + getName());
                    }
                } else if (ti instanceof OtherToken
                           && ti.getValue().matches("[1-9]")) {
                    int no = ti.getValue().charAt(0) - '0';

                    if (pi >= len
                        || pattern.get(pi + 1) instanceof MacroParamToken
                        //TODO #1##
                    ) {
                        ta[no] = scanToken(context, source);
                    } else {
                        ta[no] = scanTo(context, source, pattern.get(pi + 1));
                    }
                } else {
                    throw new GeneralHelpingException("TTP.UseDoesntMatch",
                        "\\" + getName()); //TODO; maybe another error text?
                }

            } else {
                t = source.getToken();
                if (!t.equals(ti)) {
                    char esc = (char) (context.getCount("escapechar")
                        .getValue());
                    throw new GeneralHelpingException("TTP.UseDoesntMatch",
                        Character.toString(esc) + getName());
                }
            }
        }

        return ta;
    }

    /**
     * ...
     * 
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return ...
     *
     * @throws GeneralException ...
     */
    private Tokens scanToken(final Context context, final TokenSource source)
        throws GeneralException {
        Tokens toks;
        Token t = source.getToken();

        if (t == null) {
            char esc = (char) (context.getCount("escapechar").getValue());
            throw new GeneralHelpingException("TTP.EOFinMatch", //
                Character.toString(esc) + getName());
        } else if (t instanceof LeftBraceToken) {
            source.push(t);
            toks = source.getTokens();
            if (toks == null) {
                char esc = (char) (context.getCount("escapechar").getValue());
                throw new GeneralHelpingException("TTP.EOFinMatch", //
                    Character.toString(esc) + getName());
            }
        } else {
            toks = new Tokens(t);
        }

        return toks;
    }

    /**
     * ...
     * 
     * @param context
     * @param source
     * @param to
     * @return @throws GeneralException
     */
    private Tokens scanTo(final Context context, final TokenSource source,
        final Token to) throws GeneralException {
        Tokens toks = new Tokens();
        Token t;

        for (t = source.getToken(); t != null; t = source.getToken()) {
            if (t.equals(to)) {
                return toks;
            }
            toks.add(t);
        }

        char esc = (char) (context.getCount("escapechar").getValue());
        throw new GeneralHelpingException("TTP.EOFinMatch", //
            Character.toString(esc) + getName());
    }

}
