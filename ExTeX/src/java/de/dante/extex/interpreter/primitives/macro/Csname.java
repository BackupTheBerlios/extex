/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.CsConvertible;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.ActiveCharacterToken;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\csname</code>.
 *
 * <doc name="csname">
 * <h3>The Primitive <tt>\csname</tt></h3>
 * <p>
 *  ...
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\csname ...\endcsname</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \csname abc\endcsname  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class Csname extends AbstractCode implements ExpandableCode,
        CsConvertible {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Csname(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        Token t = convertCs(context, source);
        source.push(t);
        return false;
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
            throws GeneralException {

        Token t = convertCs(context, source);
        source.push(t);
        //gene: this night not be correct
    }

    /**
     * @see de.dante.extex.interpreter.type.CsConvertible#convertCs(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public Token convertCs(final Context context, final TokenSource source)
            throws GeneralException {

        Token t = source.getToken();

        if (t == null) {

            throw new HelpingException("TTP.MissingCtrlSeq");
        } else if (t instanceof CodeToken) {

            if (t.getValue().equals("csname")) {
                Tokens toks = scanToEndCsname(context, source);
                t = context.getTokenFactory()
                        .createToken(Catcode.ESCAPE, toks.toString(),
                                     context.getNamespace());
            }

        } else if (!(t instanceof ActiveCharacterToken)) {

            throw new HelpingException("TTP.MissingCtrlSeq");
        }

        return t;
    }

    /**
     * Expand tokens and collect the result until <tt>\endcsname</tt> is found.
     * In fact the termination condition is that a Token is found which is
     * assigned to {@link Endcsname Endcsname}.
     *
     * @param context the interpreter context
     * @param source the source fot new tokens
     *
     * @return the Tokens found while scanning the input tokens
     *
     * @throws GeneralException in case of an error
     */
    private Tokens scanToEndCsname(final Context context,
            final TokenSource source) throws GeneralException {

        Tokens toks = new Tokens();
        for (Token t = source.getToken(); t != null; t = source.getToken()) {

            if (t instanceof CodeToken) {
                Code code = context.getCode(t);

                if (code == null) {
                    //TODO handle EOF
                    throw new RuntimeException("unimplemented");
                } else if (code instanceof Endcsname) {
                    return toks;
                } else if (code instanceof ExpandableCode) {
                    //((ExpandableCode) code).expand(Flags.NONE, getContext(),
                    //                               this, getTypesetter());
                    //TODO handle expansion
                    throw new RuntimeException("unimplemented");
                }

            } else if (!(t instanceof SpaceToken)) {
                toks.add(t);
            }
        }
        return toks;
    }

}
