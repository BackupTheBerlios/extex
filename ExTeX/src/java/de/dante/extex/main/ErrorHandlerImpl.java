/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main;

import java.io.IOException;
import java.util.logging.Logger;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.i18n.Messages;
import de.dante.extex.interpreter.ErrorHandler;
import de.dante.extex.interpreter.Interaction;
import de.dante.extex.interpreter.InteractionVisitor;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;
import de.dante.util.Locator;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;

/**
 * This is a simple implementation of the error handler interacting with the
 * user on the command line like TeX does.
 * <p>
 * The GeneralHelpingException is capable of carrying a name and two arguments
 * for the error message. This class can be queried to provide additional help
 * concerning the error at hand.
 * See {@link GeneralHelpingException GeneralHelpingException} for details.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class ErrorHandlerImpl implements ErrorHandler, InteractionVisitor {

    /**
     * The constant <tt>NL</tt> contains the String with the newline character,
     * since it is needed several times.
     */
    private static final String NL = "\n";

    /**
     * The field <tt>logger</tt> contains the logger to write a protocol of
     * the interaction to. Note that the error has already been logged when
     * this handler is invoked. Thus only additional logging output should be
     * produced in this class.
     */
    private Logger logger;

    /**
     * Creates a new object.
     *
     * @param configuration ...
     * @param theLogger the logger for the interaction logging
     */
    public ErrorHandlerImpl(final Configuration configuration,
            final Logger theLogger) {

        super();
        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.ErrorHandler#handleError(
     *      de.dante.util.GeneralException,
     *      de.dante.extex.scanner.Token,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.interpreter.context.Context)
     */
    public boolean handleError(final GeneralException exception, final Token t,
            final TokenSource source, final Context context)
            throws GeneralException {

        return context.getInteraction().visit(this, source, context, exception);
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
     * Interact with the user in case of an error
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
        showErrorLine(logger, ex.getMessage(), source.getLocator());

        try {
            boolean firstHelp = true;

            for (;;) {
                logger.severe(Messages.format("ErrorHandler.Prompt"));

                String line = readLine();
                if (line == null) {
                    throw new GeneralHelpingException("TTP.EOFonTerm");
                }
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
                                source.getToken();
                            }
                            firstHelp = false;
                            break;
                        case 'd':
                        case 'D':
                            //TODO: support debug? TTP[84] TTP[1338]
                            break;
                        case 'e':
                        case 'E':
                            //TODO: support edit? TTP[84]
                            break;
                        case 'i':
                        case 'I':
                            source.addStream(source.getTokenStreamFactory()
                                    .newInstance(line.substring(1)));
                            break;
                        case 'h':
                        case 'H':

                            String help;

                            if (!firstHelp) {
                                help = Messages
                                        .format("ErrorHandler.noMoreHelp");
                            } else if ((help = ex.getHelp()) == null) {
                                help = Messages.format("ErrorHandler.noHelp");
                            }

                            firstHelp = false;
                            logger.severe(help + NL);
                            break;
                        case 'q':
                        case 'Q':
                            context.setInteraction(Interaction.BATCHMODE, true);
                            logger.info(Messages
                                    .format("ErrorHandler.batchmode")
                                        + NL);
                            return true;
                        case 'r':
                        case 'R':
                            context.setInteraction(Interaction.NONSTOPMODE,
                                                   true);
                            logger.info(Messages
                                    .format("ErrorHandler.nonstopmode")
                                        + NL);
                            return true;
                        case 's':
                        case 'S':
                            context
                                    .setInteraction(Interaction.SCROLLMODE,
                                                    true);
                            logger.info(Messages
                                    .format("ErrorHandler.scrollmode")
                                        + NL);
                            return true;
                        case 'x':
                        case 'X':
                            return false;
                        default:
                            logger.severe(Messages.format("ErrorHandler.help")
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

    /**
     * Read a line of characters from the standard input stream.
     * Leading spaces are ignored. At end of file <code>null</code> is returned.
     *
     * @return the line read or <code>null</code> to signal EOF
     *
     * @throws IOException in case of an error during IO. This is rather
     * unlikely
     */
    private String readLine() throws IOException {

        StringBuffer sb = new StringBuffer();

        for (int c = System.in.read(); c > 0; c = System.in.read()) {
            if (c == '\n') {
                sb.append((char) c);
                return sb.toString();
            } else if (c != ' ' || sb.length() > 0) {
                sb.append((char) c);
            }
        }

        return (sb.length() > 0 ? sb.toString() : null);
    }

    /**
     * This method is invoked to present the current line causing the error.
     *
     * @param aLogger the logger to use for output
     * @param message the error message
     * @param locator the locator for the error position
     */
    protected void showErrorLine(final Logger aLogger, final String message,
            final Locator locator) {

        String file = locator.getFilename();
        StringBuffer sb = new StringBuffer();

        for (int i = locator.getLinePointer(); i > 0; i--) {
            sb.append('_');
        }
        aLogger.severe(NL + NL + (file == null ? "" : file) + ":"
                      + Integer.toString(locator.getLineno()) + ": " + message
                      + NL + NL + locator.getLine() + NL + sb.toString() + "^"
                      + NL);
    }

}