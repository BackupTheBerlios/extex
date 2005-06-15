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
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.interpreter.type.TokensWriter;
import de.dante.extex.interpreter.type.file.ExecuteFile;
import de.dante.extex.interpreter.type.file.LogFile;
import de.dante.extex.interpreter.type.file.OutFile;
import de.dante.extex.interpreter.type.file.UserAndLogFile;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.exception.TypesetterException;
import de.dante.extex.typesetter.type.node.WhatsItWriteNode;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\write</code>.
 *
 * <doc name="write">
 * <h3>The Primitive <tt>\write</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <h4>Syntax</h4>
 *
 * <h4>Examples</h4>
 * <pre class="TeXSample">
 * \immediate\openout3= abc.def
 * \write3{Hi there!}
 * \closeout3 </pre>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.24 $
 */
public class Write extends AbstractCode
        implements
            TokensWriter,
            LogEnabled,
            Configurable,
            InitializableCode {

    /**
     * The constant <tt>LOG_FILE</tt> contains the key for the log file.
     */
    private static final String LOG_FILE = "-1";

    /**
     * The field <tt>SYSTEM</tt> contains the key for the system execute
     * (\write18).
     */
    private static final String SYSTEM = "18";

    /**
     * The field <tt>USER_AND_LOG</tt> contains the key for the user trace and
     * log file.
     */
    private static final String USER_AND_LOG = "17";

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private transient Logger logger = null;

    /**
     * The field <tt>write18</tt> contains the indicator that the ancient
     * write18 feature of <logo>TeX</logo> should be enabled.
     */
    private boolean write18 = false;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Write(final String name) {

        super(name);
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        write18 = Boolean.valueOf(config.getAttribute("write18"))
                .booleanValue();
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
            throws InterpreterException {

        String key = AbstractFileCode.scanOutFileKey(context, source, typesetter);

        if (prefix.isImmediate()) {

            Tokens toks = source.scanTokens(context);
            write(key, toks, context);

        } else {

            Tokens toks = source.getTokens(context);

            try {
                typesetter.add(new WhatsItWriteNode(key, toks, source, this));
            } catch (TypesetterException e) {
                throw new InterpreterException(e);
            } catch (ConfigurationException e) {
                throw new InterpreterException(e);
            }
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.InitializableCode#init(
     *      de.dante.extex.interpreter.context.Context,
     *      java.lang.String)
     */
    public void init(final Context context, final String value)
            throws InterpreterException {

        context.setOutFile(LOG_FILE, new LogFile(logger), true);
        context.setOutFile(USER_AND_LOG, new UserAndLogFile(logger), true);
        if (write18) {
            context.setOutFile(SYSTEM, new ExecuteFile(logger), true);
        }
    }

    /**
     * Write some tokens to a write register.
     *
     * @param key the name (number) of the write register
     * @param toks the tokens to write
     * @param context the processing context
     *
     * @throws InterpreterException in case of an error
     */
    public void write(final String key, final Tokens toks, final Context context)
            throws InterpreterException {

        OutFile file = context.getOutFile(key);

        if (file == null || !file.isOpen()) {

            if (key == null || "".equals(key) || key.charAt(0) == '-') {
                file = context.getOutFile(LOG_FILE);
            } else {
                file = context.getOutFile(USER_AND_LOG);
            }
        }

        try {
            file.write(toks);
        } catch (IOException e) {
            throw new InterpreterException(e);
        }
    }

}