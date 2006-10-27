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

package de.dante.extex.interpreter.primitives.info;

import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive
 * <code>\message</code>.
 *
 * <doc name="message">
 * <h3>The Primitive <tt>\message</tt></h3>
 * <p>
 *  The primitive <tt>\message</tt> takes as argument a list of tokens enclosed
 *  in braces and writes them to output stream and into the log file.
 * </p>
 * <p>
 *  If the keywords <tt>to log</tt> are given then the message is written to the
 *  log file only. This is an extension not present in <logo>TeX</logo> and
 *  friends.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;message&rang;
 *      &rarr; <tt>\message</tt> <tt>{</tt> &lang;unprotected tokens&rang; <tt>}</tt>
 *       |   <tt>\message</tt> <tt>to</tt> <tt>log</tt> <tt>{</tt> &lang;unprotected tokens&rang; <tt>}</tt>
 *       </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \message{Hello World!}  </pre>
 *  <pre class="TeXSample">
 *    \message to log {Hello World!}  </pre>
 *
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.24 $
 */
public class Message extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 20060406L;

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public Message(final String name) {

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
     * Scan the next tokens (between braces) and put the value (as text) on the
     * log. In fact only the source is informed that there is something to
     * write out. This is done using the observer pattern.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        boolean log = false;

        if (source.getKeyword(context, "to")) {

            if (source.getKeyword(context, "log")) {
                log = true;
            } else {
                throw new HelpingException(getLocalizer(), "logMissing");
            }
        }

        Tokens toks = source.scanUnprotectedTokens(context, true, false,
                getName());
        if (log) {
            logger.fine(" " + toks.toText());
        } else {
            logger.severe(" " + toks.toText());
        }
    }

}
