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

package de.dante.extex.interpreter.primitives.file;

import java.io.IOException;
import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.interpreter.type.node.WhatsItWriteNode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\write</code>.
 *
 * <doc name="write">
 * <h3>The Primitive <tt>\write</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * </doc>
 *
 * Example
 * <pre>
 * \write3{abc \def}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.15 $
 */
public class Write extends AbstractCode implements LogEnabled {

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Write(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
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
            throws GeneralException {

        String key = AbstractFileCode.scanOutFileKey(context, source);

        if (prefix.isImmediate()) {
            Tokens toks = source.scanTokens(context);
            writeImmediate(key, toks, context, source);
        } else {
            Tokens toks = source.getTokens(context);
            typesetter.add(new WhatsItWriteNode(key, toks));
        }
    }

    /**
     * Immediately write some tokens to a write register.
     *
     * @param key the number of the write register
     * @param toks the tokens to write
     * @param context the processing context
     * @param source the source for new tokens
     *
     * @throws GeneralException in case of an error
     */
    private void writeImmediate(final String key, final Tokens toks,
            final Context context, final TokenSource source)
            throws GeneralException {

        if (key == null || key.equals("")) {
            logger.info(toks.toText());
            return;
        }

        OutFile file = context.getOutFile(key);

        if (file == null || !file.isOpen()) {
            logger.info(toks.toText());
        } else {
            try {
                file.write(toks);
            } catch (IOException e) {
                throw new GeneralException(e);
            }
        }
    }

}