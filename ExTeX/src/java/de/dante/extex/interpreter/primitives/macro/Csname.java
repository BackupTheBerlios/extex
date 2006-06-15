/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.primitives.Relax;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.SpaceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.i18n.Localizer;

/**
 * This class provides an implementation for the primitive <code>\csname</code>.
 *
 * <doc name="csname">
 * <h3>The Primitive <tt>\csname</tt></h3>
 * <p>
 *  The primitive <tt>\csname</tt> absorbs further tokens until a matching
 *  {@link de.dante.extex.interpreter.primitives.macro.Endcsname <tt>\endcsname</tt>}
 *  is found. The tokens found are expanded. Spaces are ignored. The expansion
 *  should lead to character tokens only. A new token is constructed from the
 *  characters. The escape character is the current escape character.
 * </p>
 * <p>
 *  If the meaning of the new token is currently not defined then it is defined
 *  to be equivalent to the original meaning of
 *  {@link de.dante.extex.interpreter.primitives.Relax \relax}.
 * </p>
 * <p>
 *  If a non-expandable token is encountered then an error is raised.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;csname&rang;
 *      &rarr; <tt>\csname</tt> &lang;...&rang; <tt>\endcsname</tt>  </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \csname abc\endcsname  </pre>
 *  <p>
 *   This results in the control sequence <tt>\abc</tt>.
 *  </p>
 *
 *  <pre class="TeXSample">
 *    \csname ab#de\endcsname  </pre>
 *  <p>
 *   The example is valid. It shows that even non-character tokens might be
 *   contained.
 *  </p>
 *
 *  <pre class="TeXSample">
 *    \csname \TeX\endcsname  </pre>
 *  <p>
 *   This is usually illegal since <tt>\TeX</tt> is defined in plain to contain
 *   some non-expandable primitives.
 *  </p>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.36 $
 */
public class Csname extends AbstractCode implements ExpandableCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Expand tokens and collect the result until <tt>\endcsname</tt> is found.
     * In fact the termination condition is that a Token is found which is
     * assigned to {@link Endcsname Endcsname}.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param loc the localizer
     *
     * @return the Tokens found while scanning the input tokens
     *
     * @throws InterpreterException in case of an error
     */
    public static Tokens scanToEndCsname(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final Localizer loc) throws InterpreterException {

        Tokens toks = new Tokens();
        for (Token t = source.getToken(context); t != null; t = source
                .getToken(context)) {

            if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);

                if (code instanceof Endcsname) {

                    return toks;

                } else if (code instanceof ExpandableCode) {

                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);

                } else if (code == null) {

                    throw new UndefinedControlSequenceException(printable(
                            context, t));

                } else {

                    throw new HelpingException(loc, "TTP.MissingEndcsname",
                            context.esc("endcsname"), context.esc(t));
                }

            } else if (!(t instanceof SpaceToken)) {

                toks.add(t);
            }
        }
        return toks;
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Csname(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.CsConvertible#convertCs(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Token convertCs(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Tokens toks = scanToEndCsname(context, source, typesetter,
                getLocalizer());

        try {
            return context.getTokenFactory()
                    .createToken(Catcode.ESCAPE, context.escapechar(),
                            toks.toText(), context.getNamespace());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens toks = scanToEndCsname(context, source, typesetter,
                getLocalizer());
        String s = toks.toText();

        try {
            CodeToken t = (CodeToken) context.getTokenFactory().createToken(
                    Catcode.ESCAPE, context.escapechar(), s,
                    context.getNamespace());
            Code code = context.getCode(t);
            if (code == null) {
                context.setCode(t, new Relax(s), true);
            }
            source.push(t);
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Tokens toks = scanToEndCsname(context, source, typesetter,
                getLocalizer());
        String s = toks.toText();

        try {
            CodeToken t = (CodeToken) context.getTokenFactory().createToken(
                    Catcode.ESCAPE, context.escapechar(), s,
                    context.getNamespace());
            Code code = context.getCode(t);
            if (code == null) {
                context.setCode(t, new Relax(s), true);
            }
            source.push(t);
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

}
