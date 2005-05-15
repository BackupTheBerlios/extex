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

package de.dante.extex.interpreter;

import java.io.IOException;
import java.io.InputStream;

import de.dante.extex.font.FontFactory;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.ErrorLimitException;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.loader.LoaderException;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.observer.Observable;

/**
 * This interface represents the outside view on an interpreter. It contains
 * everything needed to set it up and make it run.
 *
 * @see "TeX -- The Program [1029]"
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.26 $
 */
public interface Interpreter extends TokenSource, Observable {

    /**
     * Getter for the context
     *
     * @return the context
     */
    Context getContext();

    /**
     * Getter for the error handler.
     * The error handler might not be set. In this case <code>null</code> is
     * returned.
     *
     * @return the error handler currently registered
     */
    ErrorHandler getErrorHandler();

    /**
     * Getter for the interaction mode.
     *
     * @return the interaction mode
     */
    Interaction getInteraction();

    /**
     * Load the format from an external source.
     *
     * @param stream stream to read from
     * @param fmt the name of the format to be loaded
     *
     * @throws IOException in case of an IO error
     * @throws LoaderException in case of an error during loading
     */
    void loadFormat(InputStream stream, String fmt)
            throws LoaderException,
                IOException;

    /**
     * Process the current token streams by repeatedly reading a single token
     * and processing it until no token is left. The visitor pattern is used to
     * branch to the appropriate method for processing a single token.
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws ErrorLimitException in case that the error limit has been
     *  reached
     * @throws InterpreterException in case of another error
     */
    void run()
            throws ConfigurationException,
                ErrorLimitException,
                InterpreterException;

    /**
     * Add a token stream and start processing it.
     *
     * @param stream the input stream to consider
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws ErrorLimitException in case that the error limit has been
     *  reached
     * @throws InterpreterException in case of another error
     *
     * @see #run()
     */
    void run(TokenStream stream)
            throws ConfigurationException,
                ErrorLimitException,
                InterpreterException;

    /**
     * Setter for the context.
     * Use with care!
     *
     * @param context the interpreter context
     */
    void setContext(Context context);

    /**
     * Setter for the error handler.
     * The value of <code>null</code> can be used to delete the error handler
     * currently set.
     *
     * @param handler the new error handler
     */
    void setErrorHandler(ErrorHandler handler);

    /**
     * Setter for the font factory
     *
     * @param fontFactory the new font factory
     */
    void setFontFactory(FontFactory fontFactory);

    /**
     * Setter for the interaction mode.
     *
     * @param interaction the interaction mode
     *
     * @throws GeneralException in case of an error
     */
    void setInteraction(Interaction interaction) throws GeneralException;

    /**
     * Setter for the job name.
     *
     * @param jobname the new value for the job name
     *
     * @throws GeneralException in case of an error
     */
    void setJobname(String jobname) throws GeneralException;

    /**
     * Setter for the token stream factory.
     * During the processing of the input several occasions might come up
     * where new token streams are needed. In this case the factory can be used
     * to acquire them.
     *
     * @param factory the token stream factory
     *
     * @throws ConfigurationException in case of en error in the configuration
     */
    void setTokenStreamFactory(TokenStreamFactory factory)
            throws ConfigurationException;

    /**
     * Setter for the typesetter.
     *
     * @param typesetter the new typesetter
     */
    void setTypesetter(Typesetter typesetter);

}