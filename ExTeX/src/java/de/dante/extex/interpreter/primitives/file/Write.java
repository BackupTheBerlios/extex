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
 * <doc name="write">
 * <h3>The Primitive <tt>\write</tt></h3>
 * <p>
 *  ...
 * </p>
 * </doc>
 *
 * Example
 * <pre>
 * \write3{abc \def}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
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
    public boolean execute(final Flags prefix, final Context context,
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

        return true;
    }

    /**
     * Immediately write some tokens to a write register.
     *
     * @param no the number of the write register
     * @param toks the tokens to write
     * @param context the processing context
     * @param source the source for new tokens
     *
     * @throws GeneralException in case of an error
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
     * Write with delay.
     *
     * @param no the number of the write register
     * @param toks the tokens to write
     */
    private void write(final long no, final Tokens toks) {

        throw new RuntimeException("unimplemented");
    }

}
