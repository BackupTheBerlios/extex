/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.file;

import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\write</code>.
 *
 * Example
 * <pre>
 * \write3{abc \def}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class Write extends AbstractCode {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Write(final String name) {

        super(name);
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

        long no = source.scanInteger();

        if (prefix.isImmediate()) {
            Tokens toks = source.scanTokens();
            writeImmediate(no, toks, context, source);
        } else {
            Tokens toks = source.getTokens();
            write(no, toks);
        }

        prefix.clear();
    }

    /**
     * ...
     *
     * @param no ...
     * @param toks ...
     * @param context ...
     * @param source TODO
     *
     * @throws GeneralException ...
     */
    private void writeImmediate(final long no, final Tokens toks,
            final Context context, final TokenSource source)
            throws GeneralException {

        if (no < 0) {
            source.update("log", toks.toText());
            return;
        }

        OutFile file = context.getOutFile(Long.toString(no));

        if (file == null || !file.isOpen()) {
            source.update("message", toks.toText());
        } else {
            file.write(toks);
        }
    }

    /**
     * ...
     *
     * @param no ...
     * @param toks ...
     */
    private void write(final long no, final Tokens toks) {

        throw new RuntimeException("unimplemented");
    }

}