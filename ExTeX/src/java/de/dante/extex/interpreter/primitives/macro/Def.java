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

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.i18n.PanicException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.LeftBraceToken;
import de.dante.extex.scanner.MacroParamToken;
import de.dante.extex.scanner.OtherToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\def</code>.
 *
 * <doc name="def">
 * <h3>The Primitive <tt>\def</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;def&rang;
 *       &rarr; &lang;prefix&rang; <tt>\def</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getControlSequence()
 *       &lang;control sequence&rang;} &lang;parameter text&rang; <tt>{</tt> &lang;replacement text&rang; <tt>}</tt>
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       | <tt>\global</tt> &lang;prefix&rang;
 *       | <tt>\long</tt> &lang;prefix&rang;
 *       | <tt>\outer</tt> &lang;prefix&rang;</pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \def#1{--#1--}  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.21 $
 */
public class Def extends AbstractAssignment {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Def(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token cs = source.getControlSequence();
        Tokens pattern = getPattern(context, source);
        Tokens body = (prefix.isExpanded() //
                ? expandedBody(context, source, typesetter)//
                : source.getTokens());

        if (cs instanceof CodeToken) {
            String name = cs.getValue();
            context.setCode(cs, new MacroCode(name, prefix, pattern, body),
                    prefix.isGlobal());
        } else if (cs == null) {
            throw new HelpingException("TTP.MissingCtrlSeq");
        } else {
            throw new PanicException("TTP.Confusion");
        }
    }

    /**
     * Parse the expanded body tokens according to the rules for \xdef.
     *
     * @param context the interpreter context
     * @param source the source to acquire tokens from
     * @param typesetter the typesetter to use as backend
     *
     * @return the tokens making up the body
     */
    private Tokens expandedBody(final Context context,
            final TokenSource source, final Typesetter typesetter) {

        //TODO
        throw new RuntimeException("unimplemented");
    }

    /**
     * Parse the pattern.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return the tokens read
     *
     * @throws GeneralException in case of an error
     */
    protected Tokens getPattern(final Context context, final TokenSource source)
            throws GeneralException {

        Tokens toks = new Tokens();
        int no = 1;
        boolean afterHash = false;

        for (Token t = source.getToken(); t != null; t = source.getToken()) {
            if (t instanceof LeftBraceToken) {
                source.push(t);
                return toks;
            }

            if (afterHash) {
                if (t instanceof OtherToken) {
                    if (t.getValue().charAt(0) != '0' + no) {
                        throw new HelpingException("TTP.NonConseqParams",
                                printableControlSequence(context));
                    }
                    no++;
                } else if (!(t instanceof MacroParamToken)) {
                    throw new HelpingException("TTP.NonConseqParams",
                            printableControlSequence(context));
                }
                afterHash = false;
            } else {
                afterHash = (t instanceof MacroParamToken);
            }
            toks.add(t);
        }

        throw new HelpingException("TTP.EOFinDef",
                printableControlSequence(context));
    }

}