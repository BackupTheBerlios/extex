/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.expression.term;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.expression.EType;
import de.dante.extex.interpreter.expression.ETypeParser;
import de.dante.extex.interpreter.expression.Evaluator;
import de.dante.extex.interpreter.expression.UnaryFunction;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.ControlSequenceToken;
import de.dante.extex.scanner.type.token.LetterToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class implements the supporting functions for the date type
 * {@linkplain de.dante.extex.interpreter.expression.term.TCount TCount}
 * for the expression evaluator.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class TCountParser implements ETypeParser {

    /**
     * Creates a new object.
     */
    public TCountParser() {

        super();
    }

    /**
     * @see de.dante.extex.interpreter.expression.EType#convert(
     *      de.dante.extex.interpreter.type.Code,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public EType convert(final Code code, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        if (code instanceof CountConvertible) {
            return new TCount(((CountConvertible) code).convertCount(context,
                    source, typesetter));
        }
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.expression.ETypeParser#parse(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public EType parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        long n = 0;
        Token t = source.getNonSpace(context);
        Tokens save = new Tokens();
        int no;

        while (t != null) {

            save.add(t);

            if (t instanceof OtherToken) {
                int c = t.getChar().getCodePoint();
                switch (c) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        n = c - '0';

                        for (t = source.getToken(context); t instanceof OtherToken
                                && t.getChar().isDigit(); t = source
                                .getToken(context)) {
                            n = n * 10 + t.getChar().getCodePoint() - '0';
                            save.add(t);
                        }

                        if (t instanceof SpaceToken) {
                            source.skipSpace();
                        } else {
                            source.push(t);
                        }
                        return new TCount(n);

                    case '`':
                        t = source.getToken(context);
                        save.add(t);

                        if (t instanceof ControlSequenceToken) {
                            String s = ((ControlSequenceToken) t).getName();
                            n = ("".equals(s) ? 0 : s.charAt(0));
                            return new TCount(n);
                        } else if (t != null) {
                            n = t.getChar().getCodePoint();
                            return new TCount(n);
                        }
                        // fall through to error handling
                        break;

                    case '\'':
                        t = source.scanToken(context);
                        if (!(t instanceof OtherToken)) {
                            t = null;
                            break;
                        }
                        save.add(t);
                        n = t.getChar().getCodePoint() - '0';
                        if (n < 0 || n > 7) {
                            t = null;
                            break;
                        }
                        for (t = source.scanToken(context); t instanceof OtherToken; //
                        t = source.scanToken(context)) {
                            save.add(t);
                            no = t.getChar().getCodePoint() - '0';
                            if (no < 0 || no > 7) {
                                break;
                            }
                            n = n * 8 + no;
                        }

                        while (t instanceof SpaceToken) {
                            t = source.getToken(context);
                        }
                        source.push(t);
                        return new TCount(n);

                    case '"':

                        for (t = source.scanToken(context); //
                        t instanceof OtherToken || t instanceof LetterToken; //
                        t = source.scanToken(context)) {
                            save.add(t);
                            no = t.getChar().getCodePoint();
                            switch (no) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    n = n * 16 + no - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    n = n * 16 + no - 'a' + 10;
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    n = n * 16 + no - 'A' + 10;
                                    break;
                                default:
                                    source.push(t);
                                    source.skipSpace();
                                    return new TCount(n);
                            }
                        }

                        while (t instanceof SpaceToken) {
                            t = source.getToken(context);
                        }
                        source.push(t);
                        return new TCount(n);

                    default:
                }
                break;
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code == null) {
                    break;

                } else if (code instanceof CountConvertible) {
                    n = ((CountConvertible) code).convertCount(context, source,
                            typesetter);
                    return new TCount(n);
                } else if (code instanceof ExpandableCode) {
                    save.removeLast();
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                    t = source.getToken(context);
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        source.push(save);
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.expression.ETypeParser#registered(
     *      de.dante.extex.interpreter.expression.Evaluator)
     */
    public void registered(final Evaluator evaluator) {

        evaluator.register("int", new UnaryFunction() {

            /**
             * @see de.dante.extex.interpreter.expression.Function1#apply(
             *      de.dante.extex.interpreter.expression.EType)
             */
            public EType apply(final EType accumulator)
                    throws InterpreterException {

                return new TCount().set(accumulator);
            }
        });
    }

}
