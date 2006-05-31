/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.MacroParamToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\def</code>.
 *
 * <doc name="def">
 * <h3>The Primitive <tt>\def</tt></h3>
 * <p>
 *  TODO gene: missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;def&rang;
 *       &rarr; &lang;prefix&rang; <tt>\def</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *       &lang;control sequence&rang;} &lang;parameter text&rang; <tt>{</tt> &lang;replacement text&rang; <tt>}</tt>
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       | <tt>\global</tt> &lang;prefix&rang;
 *       | <tt>\long</tt> &lang;prefix&rang;
 *       | <tt>\outer</tt> &lang;prefix&rang;
 *       | <tt>\protected</tt> &lang;prefix&rang;</pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \def\a#1{--#1--}  </pre>
 *  <pre class="TeXSample">
 *    \def\a#1#{--#1--}  </pre>
 *  <pre class="TeXSample">
 *    \def\a#1#2{--#2--#1--}  </pre>
 *  <pre class="TeXSample">
 *    \def\a#1:#2.{--#2--#1--}  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.43 $
 */
public class Def extends AbstractAssignment {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Def(final String name) {

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
            throws InterpreterException {

        CodeToken cs = source.getControlSequence(context);
        MacroPattern pattern = getPattern(context, source);
        Tokens body = (!prefix.clearExpanded()
                ? source.getTokens(context, source, typesetter) //
                : source
                        .scanUnprotectedTokens(context, false, false, getName()));
        //TODO gene: maybe the treatment of # is incorrect

        MacroCode macroCode = (prefix.clearProtected() //
                ? new ProtectedMacroCode(cs.getName(), prefix, pattern, body) //
                : new MacroCode(cs.getName(), prefix, pattern, body));

        context.setCode(cs, macroCode, prefix.clearGlobal());
        prefix.clearLong();
        prefix.clearOuter();
    }

    /**
     * Parse the pattern.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return the tokens read
     *
     * @throws InterpreterException in case of an error
     */
    protected MacroPattern getPattern(final Context context,
            final TokenSource source) throws InterpreterException {

        MacroPattern pattern = new MacroPattern();
        int no = 1;
        boolean afterHash = false;

        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {

            if (t instanceof LeftBraceToken) {
                source.push(t);
                pattern.setArity(no);
                return pattern;
            } else if (t instanceof RightBraceToken) {
                throw new HelpingException(getLocalizer(),
                        "TTP.MissingLeftDefBrace",
                        printableControlSequence(context));
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code != null && code.isOuter()) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.OuterInDef", printableControlSequence(context));
                }
            }

            if (afterHash) {
                if (t instanceof OtherToken) {
                    if (t.getChar().getCodePoint() != '0' + no) {
                        throw new HelpingException(getLocalizer(),
                                "TTP.NonConsequtiveParams",
                                printableControlSequence(context));
                    }
                    no++;
                } else if (!(t instanceof MacroParamToken)) {
                    throw new HelpingException(getLocalizer(),
                            "TTP.NonConsequtiveParams",
                            printableControlSequence(context));
                }
                afterHash = false;
            } else {
                afterHash = (t instanceof MacroParamToken);
            }
            pattern.add(t);
        }

        throw new HelpingException(getLocalizer(), "TTP.EOFinDef",
                printableControlSequence(context));
    }

}
