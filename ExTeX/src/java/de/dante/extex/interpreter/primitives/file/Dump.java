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

package de.dante.extex.interpreter.primitives.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.logging.Logger;

import de.dante.extex.i18n.HelpingException;
import de.dante.extex.i18n.PanicException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.loader.SerialLoader;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This class provides an implementation for the primitive <code>\dump</code>.
 *
 * <doc name="dump">
 * <h3>The Primitive <tt>\dump</tt></h3>
 * <p>
 * The primitive writes out the current state of the interpreter to an
 * format file. This format file can be read back in to restore the saved state.
 * </p>
 * <p>
 * The primitive can be used outside of any group only.
 * </p>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;dump&rang;
 *       &rarr; <tt>\dump</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \dump  </pre>
 * </p>
 * </doc>
 *
 * Example
 * <pre>
 * \dump
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.26 $
 */
public class Dump extends AbstractCode implements LogEnabled {

    /**
     * The constant <tt>FORMAT_EXTENSION</tt> contains the extension for the
     * format file.
     */
    private static final String FORMAT_EXTENSION = ".fmt";

    /**
     * The field <tt>logger</tt> contains the target channel for the message.
     */
    private Logger logger = null;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Dump(final String name) {

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
     * This method takes the first token and executes it. The result is placed
     * on the stack. This operation might have side effects. To execute a token
     * it might be necessary to consume further tokens.
     *
     * @param prefix the prefix controlling the execution
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [1303,1304]"
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        if (!context.isGlobalGroup()) {
            throw new HelpingException(getLocalizer(), "TTP.DumpInGroup");
        }

        //TODO gene: @see "TeX -- The Program [1328]"

        Tokens jobnameTokens = context.getToks("jobname");
        if (jobnameTokens == null) {
            throw new PanicException(getLocalizer(), "Dump.MissingJobname",
                    printableControlSequence(context));
        }
        String jobname = jobnameTokens.toText();
        Calendar calendar = Calendar.getInstance();

        context.setId(jobname + " " + //
                calendar.get(Calendar.YEAR) + "."
                + calendar.get(Calendar.MONTH) + "."
                + calendar.get(Calendar.DAY_OF_MONTH));

        OutputStream stream = null;
        try {
            String filename = jobname + FORMAT_EXTENSION;
            stream = new FileOutputStream(filename);
            logger.info(getLocalizer().format("TTP.Dumping", filename));
            new SerialLoader().save(stream, jobname, context);
        } catch (FileNotFoundException e) {
            throw new GeneralException(e);
        } catch (IOException e) {
            throw new GeneralException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new GeneralException(e);
                }
            }
        }
    }

}