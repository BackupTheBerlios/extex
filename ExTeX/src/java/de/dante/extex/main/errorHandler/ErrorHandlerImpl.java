/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
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

package de.dante.extex.main.errorHandler;

import java.io.IOException;
import java.util.logging.Logger;

import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.InteractionVisitor;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.ImpossibleException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.main.errorHandler.editHandler.EditHandler;
import de.dante.extex.scanner.type.Token;
import de.dante.util.Locator;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a simple implementation of the error handler interacting with the
 * user on the command line like <logo>TeX</logo> does.
 * <p>
 * The {@link HelpingException HelpingException} is capable of carrying a name
 * and two arguments for the error message. This class can be queried to
 * provide additional help concerning the error at hand.
 * See {@link HelpingException HelpingException} for details.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class ErrorHandlerImpl
        implements
            ErrorHandler,
            LogEnabled,
            Localizable,
            InteractionVisitor {

    /**
     * The constant <tt>NL</tt> contains the String with the newline character,
     * since it is needed several times.
     */
    protected static final String NL = "\n";

    /**
     * The field <tt>editHandler</tt> contains the handler to be invoked upon a
     * request to edit a file.
     */
    private EditHandler editHandler = null;

    /**
     * The field <tt>localizer</tt> contains the localizer.
     */
    private Localizer localizer;

    /**
     * The field <tt>logger</tt> contains the logger to write a protocol of
     * the interaction to. Note that the error has already been logged when
     * this handler is invoked. Thus only additional logging output should be
     * produced in this class.
     */
    private Logger logger = null;

    /**
     * Creates a new object.
     */
    public ErrorHandlerImpl() {

        super();
    }

    /**
     * Setter for the localizer.
     *
     * @param theLocalizer the new localizer
     *
     * @see de.dante.util.framework.i18n.Localizable#enableLocalization(
     *      de.dante.util.framework.i18n.Localizer)
     */
    public void enableLocalization(final Localizer theLocalizer) {

        this.localizer = theLocalizer;
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * Special treatment of the debug case.
     *
     * @throws IOException in case of an error during IO. This is rather
     *  unlikely.
     * @throws HelpingException in case of EOF on terminal
     *
     * @see "TTP[1338]"
     */
    private void handleDebug() throws HelpingException, IOException {

        for (;;) {
            String line = promptAndReadLine(localizer
                    .format("ErrorHandler.Prompt"));
            logger.config(line);

            if ("1".equals(line)) {

            } else if ("2".equals(line)) {
            } else if ("3".equals(line)) {
            } else if ("4".equals(line)) {
            } else if ("5".equals(line)) {
            } else if ("6".equals(line)) {
            } else if ("7".equals(line)) {
            } else if ("8".equals(line)) {
            } else if ("9".equals(line)) {
            } else if ("10".equals(line)) {
            } else if ("11".equals(line)) {
            } else if ("12".equals(line)) {
            } else if ("13".equals(line)) {
            } else if ("14".equals(line)) {
            } else if ("15".equals(line)) {
            } else if ("16".equals(line)) {
            } else if (line.startsWith("-")) {
                return;
            } else {
                logger.config(localizer.format("ErrorHandler.DebugElsePrompt"));
            }
        }
        // TODO gene: handleDebug unimplemented
    }

    /**
     * @see de.dante.extex.interpreter.ErrorHandler#handleError(
     *      de.dante.util.GeneralException,
     *      de.dante.extex.scanner.type.Token,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.interpreter.context.Context)
     */
    public boolean handleError(final GeneralException exception, final Token t,
            final TokenSource source, final Context context)
            throws InterpreterException {

        try {
            return context.getInteraction().visit(this, source, context,
                    exception);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new ImpossibleException(e);
        }
    }

    /**
     * Read a line of characters from the standard input stream after a prompt
     * has been shown.
     * Leading spaces are ignored. At end of file an exception is thrown.
     *
     * @param prompt the prompt to display
     *
     * @return the line read or <code>null</code> to signal EOF
     *
     * @throws IOException in case of an error during IO. This is rather
     *  unlikely.
     * @throws HelpingException in case of EOF on terminal
     */
    protected String promptAndReadLine(final String prompt)
            throws IOException,
                HelpingException {

        logger.severe(prompt);

        StringBuffer sb = new StringBuffer();

        for (int c = System.in.read(); c > 0; c = System.in.read()) {
            if (c == '\n') {
                sb.append((char) c);
                return sb.toString();
            } else if (c != ' ' || sb.length() > 0) {
                sb.append((char) c);
            }
        }

        if (sb.length() > 0) {
            return sb.toString();
        }

        throw new HelpingException(localizer, "TTP.EOFonTerm");
    }

    /**
     * @see de.dante.extex.interpreter.ErrorHandler#setEditHandler(
     *      de.dante.extex.main.errorHandler.editHandler.EditHandler)
     */
    public void setEditHandler(final EditHandler editHandler) {

        this.editHandler = editHandler;
    }

    /**
     * This method is invoked to present the current line causing the error.
     *
     * @param theLogger the logger to use for output
     * @param message the error message
     * @param locator the locator for the error position
     */
    protected void showErrorLine(final Logger theLogger, final String message,
            final Locator locator) {

        if (locator == null) {
            return;
        }
        String file = locator.getResourceName();
        StringBuffer sb = new StringBuffer();

        for (int i = locator.getLinePointer(); i > 0; i--) {
            sb.append('_');
        }
        theLogger.severe(NL + NL + (file == null ? "" : file) + ":"
                + Integer.toString(locator.getLineno()) + ": " + message + NL
                + NL + locator.getLine() + NL + sb.toString() + "^" + NL);
    }

    /**
     * @see de.dante.extex.interpreter.InteractionVisitor#visitBatchmode(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public boolean visitBatchmode(final Object arg1, final Object arg2,
            final Object arg3) throws GeneralException {

        return true;
    }

    /**
     * Interact with the user in case of an error.
     *
     * @see de.dante.extex.interpreter.InteractionVisitor#visitErrorstopmode(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public boolean visitErrorstopmode(final Object oSource,
            final Object oContext, final Object oException)
            throws GeneralException {

        final TokenSource source = (TokenSource) oSource;
        final Context context = (Context) oContext;
        GeneralException ex = (GeneralException) oException;
        showErrorLine(logger, ex.getLocalizedMessage(), source.getLocator());

        try {
            boolean firstHelp = true;

            for (;;) {
                String line = promptAndReadLine(localizer
                        .format("ErrorHandler.Prompt"));
                logger.config(line);

                if (line.equals("")) {
                    return true;
                } else {
                    switch (line.charAt(0)) {
                        case '0':
                        case '9':
                        case '8':
                        case '7':
                        case '6':
                        case '5':
                        case '4':
                        case '3':
                        case '2':
                        case '1':
                            int count = line.charAt(0) - '0';
                            if (line.length() > 1
                                    && Character.isDigit(line.charAt(1))) {
                                count = count * 10 + line.charAt(1) - '0';
                            }
                            while (count-- > 0) {
                                source.getToken(context);
                            }
                            firstHelp = false;
                            break;
                        case 'd':
                        case 'D':
                            // TTP[84] TTP[1338]
                            handleDebug();
                            break;
                        case 'e':
                        case 'E':
                            // TTP[84]
                            if (editHandler != null
                                    && editHandler.edit(localizer, //
                                            source.getLocator())) {

                                context.setInteraction(Interaction.SCROLLMODE);
                                logger.info(localizer
                                        .format("ErrorHandler.scrollmode")
                                        + NL);
                            }
                            return true;
                        case 'i':
                        case 'I':
                            source.addStream(source.getTokenStreamFactory()
                                    .newInstance(line.substring(1)));
                            break;
                        case 'h':
                        case 'H':

                            String help;

                            if (!firstHelp) {
                                help = localizer
                                        .format("ErrorHandler.noMoreHelp");
                            } else if ((help = ex.getHelp()) == null) {
                                help = localizer.format("ErrorHandler.noHelp");
                            }

                            firstHelp = false;
                            logger.severe(help + NL);
                            break;
                        case 'q':
                        case 'Q':
                            context.setInteraction(Interaction.BATCHMODE);
                            logger.info(localizer
                                    .format("ErrorHandler.batchmode")
                                    + NL);
                            return true;
                        case 'r':
                        case 'R':
                            context.setInteraction(Interaction.NONSTOPMODE);
                            logger.info(localizer
                                    .format("ErrorHandler.nonstopmode")
                                    + NL);
                            return true;
                        case 's':
                        case 'S':
                            context
                                    .setInteraction(Interaction.SCROLLMODE);
                            logger.info(localizer
                                    .format("ErrorHandler.scrollmode")
                                    + NL);
                            return true;
                        case 'x':
                        case 'X':
                            return false;
                        default:
                            logger.severe(localizer.format("ErrorHandler.help")
                                    + NL);
                    }
                }
            }
        } catch (IOException e) {
            throw new GeneralException(e);
        } catch (ConfigurationException e) {
            throw new GeneralException(e);
        } catch (GeneralException e) {
            throw e;
        }
    }

    /**
     * @see de.dante.extex.interpreter.InteractionVisitor#visitNonstopmode(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public boolean visitNonstopmode(final Object arg1, final Object arg2,
            final Object arg3) throws GeneralException {

        return true;
    }

    /**
     * @see de.dante.extex.interpreter.InteractionVisitor#visitScrollmode(
     *      java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public boolean visitScrollmode(final Object oSource, final Object oContext,
            final Object oException) throws GeneralException {

        return false;
    }

}