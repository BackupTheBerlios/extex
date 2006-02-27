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

package de.dante.extex.interpreter.primitives.file;

import java.io.IOException;
import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.node.WhatsItCloseNode;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive
 * <code>\closeout</code>.
 *
 * <doc name="closeout">
 * <h3>The Primitive <tt>\closeout</tt></h3>
 * <p>
 *  The primitive takes one expanded integer argument. This argument denotes a
 *  write register which will be closed if it is currently assigned to a file.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;closeout&rang;
 *       &rarr; <tt>\closeout</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#scanInteger(Context,Typesetter)
 *       &lang;number&rang;} </pre>
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \closeout5  </pre>
 *  <pre class="TeXSample">
 *    \closeout\count120  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.27 $
 */
public class Closeout extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * The field <tt>logger</tt> contains the logger to use.
     */
    private transient Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Closeout(final String name) {

        super(name);
    }

    /**
     * Setter for the logger.
     *
     * @param theLogger the new logger
     *
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
            throws InterpreterException {

        String key = AbstractFileCode.scanOutFileKey(context, source,
                typesetter);

        if (prefix.isImmediate()) {
            OutFile file = context.getOutFile(key);
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                    logger.info(e.getLocalizedMessage() + "\n");
                }
            }
            prefix.clearImmediate();
        } else {
            try {
                typesetter.add(new WhatsItCloseNode(key));
            } catch (GeneralException e) {
                throw new InterpreterException(e);
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }
    }

}
