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
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Code;
import de.dante.extex.interpreter.ExpandableCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.Showable;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.Catcode;
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
 * @version $Revision: 1.2 $
 */
public class MacroCode extends AbstractCode implements Code, ExpandableCode, Showable {

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
     * The field <tt>numberOfParams</tt> contains the number of parameters used.
     */
    private int numberOfParams = 10;

    /**
     * The field <tt>notLong</tt> contains the negated <tt>\long</tt> flag.
     * This field indicates that no macros <tt>\par</tt> are allowed in macro
     * parameter values.
     */
    private boolean notLong;

    /**
     * The field <tt>outerP</tt> contains the ...
     */
    private boolean outerP;

    /**
     * Creates a new object.
     *
     * @param name the initial name of the macro
     * @param flags the flags controlling the behavior of the macro
     * @param thePattern the pattern for the acquiring of the arguments
     * @param theBody the expansion text
     */
    public MacroCode(final String name, final Flags flags,
            final Tokens thePattern, final Tokens theBody) {

        super(name);
        this.body = theBody;
        this.pattern = thePattern;
        this.notLong = !flags.isLong();
        this.outerP = flags.isOuter(); //TODO: use the flag outer
    }

    /**
     * @see de.dante.extex.interpreter.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Tokens[] args = matchPattern(context, source);
        int len = body.length();
        Tokens toks = new Tokens();
        int no = 1;

        for (int i = 0; i < len; i++) {
            Token t = body.get(i);

            if (t instanceof MacroParamToken) {
                t = body.get(++i);
                if (t == null) {
                    throw new GeneralHelpingException("TTP.EOFinMatch",
                            printableControlSequence(context));
                } else if (t instanceof MacroParamToken) {
                    toks.add(t);
                } else if (t instanceof OtherToken && t.getChar().isDigit()) {
                    no = t.getChar().getCodePoint() - '0';
                    if (args[no] == null) {
                        throw new GeneralException("internal error");
                    }
                    toks.add(args[no]);
                } else {
                    throw new GeneralException("internal error");
                }
            } else {
                toks.add(t);
            }
        }

        source.push(toks);
        prefix.clear();
    }

    /**
     * @see de.dante.extex.interpreter.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        execute(prefix, context, source, typesetter);
    }

    /**
     * Match the pattern of this macro with the next tokens of the token
     * source. As a result the matching arguments are stored in an array of
     * {@link de.dante.extex.interpreter.type.tokens.Tokens Tokens}. This array
     * is returned.
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return a new array of parameters which have been found during the
     *         matching. Note that some of th elements of the array might be
     *         <code>null</code>.
     *
     * @throws GeneralException in case of an error
     */
    private Tokens[] matchPattern(final Context context,
            final TokenSource source) throws GeneralException {

        Tokens[] args = new Tokens[numberOfParams];
        Token ti;
        Token t;
        int len = pattern.length();

        for (int pi = 0; pi < len; pi++) {
            ti = pattern.get(pi);
            if (ti instanceof MacroParamToken) {
                pi = matchParameter(context, source, args, len, pi);
            } else if (notLong && ti.equals(Catcode.ESCAPE, "par")) {
                throw new GeneralHelpingException("TTP.RunawayArg",
                        printableControlSequence(context));
            } else {
                t = source.getToken();
                if (!t.equals(ti)) {
                    throw new GeneralHelpingException("TTP.UseDoesntMatch",
                            printableControlSequence(context));
                }
            }
        }

        return args;
    }

    /**
     * ...
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param args the array of Tokens to fill
     * @param len the length of the patterns
     * @param i the starting index
     *
     * @return ...
     *
     * @throws GeneralException in case of an error
     */
    private int matchParameter(final Context context, final TokenSource source,
            final Tokens[] args, final int len, final int i)
            throws GeneralException {

        if (i + 1 >= len) {
            throw new GeneralHelpingException("TTP.UseDoesntMatch",
                    printableControlSequence(context));
            //TODO; maybe another error text?
        }
        int pi = i + 1;
        Token ti = pattern.get(pi);
        if (ti instanceof MacroParamToken) {
            Token t = source.getToken();
            if (!ti.equals(t)) {
                throw new GeneralHelpingException("TTP.UseDoesntMatch",
                        printableControlSequence(context));
            }
            return pi;
        } else if (ti instanceof OtherToken && ti.getChar().isDigit()) {
            int no = ti.getChar().getCodePoint() - '0';
            ++pi;
            if (pi >= len) {
                args[no] = getTokenOrBlock(context, source);
            } else if (pattern.get(pi) instanceof MacroParamToken
            //TODO #1##
            ) {
                args[no] = getTokenOrBlock(context, source);
            } else {
                args[no] = scanTo(context, source, pattern.get(pi));
            }
            return pi - 1;
        }

        throw new GeneralHelpingException("TTP.UseDoesntMatch",
                printableControlSequence(context));
        //TODO maybe another error text?
    }

    /**
     * ...
     *
     * @param context the processor context
     * @param source the source for new tokens
     *
     * @return the tokens accumulated
     *
     * @throws GeneralException in case of an error
     */
    private Tokens getTokenOrBlock(final Context context,
            final TokenSource source) throws GeneralException {

        Token t = source.getToken();

        if (t == null) {
            throw new GeneralHelpingException("TTP.EOFinMatch",
                    printableControlSequence(context));
        } else if (t instanceof LeftBraceToken) {
            source.push(t);
            Tokens toks = source.getTokens();
            if (toks == null) {
                throw new GeneralHelpingException("TTP.EOFinMatch",
                        printableControlSequence(context));
            }
            return toks;
        }

        return new Tokens(t);
    }

    /**
     * ...
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param to the terminating token
     *
     * @return the tokens accumulated in between
     *
     * @throws GeneralException in case of an error
     */
    private Tokens scanTo(final Context context, final TokenSource source,
            final Token to) throws GeneralException {

        Tokens toks = new Tokens();

        for (Token t = source.getToken(); t != null; t = source.getToken()) {
            if (t.equals(to)) {
                return toks;
            }
            toks.add(t);
        }

        throw new GeneralHelpingException("TTP.EOFinMatch",
                printableControlSequence(context));
    }

    /**
     * @see de.dante.extex.interpreter.Showable#show(
     *      de.dante.extex.interpreter.context.Context)
     */
    public Tokens show(final Context context) throws GeneralException {

        Tokens toks = new Tokens(context, "macro:\n");

        pattern.show(context, toks);
        toks.add(context.getTokenFactory(), " ->");
        body.show(context, toks);
        return toks;
    }
}