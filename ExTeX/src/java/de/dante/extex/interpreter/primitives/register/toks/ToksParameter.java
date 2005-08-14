/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.register.toks;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\toks</code>.
 * It sets the numbered toks register to the value given, and as a side effect
 * all prefixes are zeroed.
 *
 * Example:
 * <pre>
 *  \toks12{123}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:mgn@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.10 $
 */
public class ToksParameter extends ToksPrimitive implements InitializableCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Return the key for a named toks register.
     *
     * @param name the name of the register
     * @param context the interpreter context to use
     *
     * @return the key for the toks register
     */
    public static String getKey(final String name, final Context context) {

        if (Namespace.SUPPORT_NAMESPACE_TOKS) {
            return context.getNamespace() + "\b" + name;
        } else {
            return name;
        }
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public ToksParameter(final String name) {

        super(name);
    }

    /**
     * Return the key (the number) for the tokens register.
     *
     * @param source the source for the next tokens &ndash; if required
     * @param context the interpreter context to use
     *
     * @return the key for the tokens register
     */
    protected String getKey(final TokenSource source, final Context context) {

        if (Namespace.SUPPORT_NAMESPACE_TOKS) {
            return context.getNamespace() + "\b" + getName();
        } else {
            return getName();
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.InitializableCode#init(
     *      de.dante.extex.interpreter.context.Context,
     *      TokenSource, Typesetter)
     */
    public void init(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        if (source != null) {
            Tokens toks = new Tokens();
            for (Token t = source.getToken(context); t != null; t = source
                    .getToken(context)) {
                toks.add(t);
            }
            context.setToks(getKey((TokenSource) null, context), toks, true);
        }
    }
}