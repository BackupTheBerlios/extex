/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.namespace;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.primitives.macro.Let;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive <code>\import</code>.
 *
 * <doc name="import">
 * <h3>The Primitive <tt>\import</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;import&rang;
 *      &rarr; <tt>\import</tt> {@linkplain
 *      de.dante.extex.interpreter.TokenSource#getTokens()
 *      &lang;replacement text&rang;}  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \import{de.dante.dtk}  </pre>
 * </p>
 * </doc>
 *
 * @see de.dante.extex.interpreter.primitives.namespace.Export
 * @see de.dante.extex.interpreter.primitives.namespace.Namespace
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Import extends Let {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Import(final String name) {

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

        String ns = source.getTokens(context).toText();
        Tokens export = context.getToks(ns + "\bexport");
        String namespace = context.getNamespace();
        int length = export.length();

        for (int i = 0; i < length; i++) {
            Token t = export.get(i);
            if (t instanceof CodeToken) {
                if (context.getCode((CodeToken) t) == null) {
                    throw new HelpingException(getLocalizer(),
                            "Namespace.Import.undef", t.toString());
                } else {
                    let(prefix, context, //
                            ((CodeToken) t).cloneInNamespace(namespace), t);
                }
            }
        }
    }

}
