/*
 * Copyright (C) 2003-2004  Gerd Neugebauer, Michael Niedermair
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */
package de.dante.extex.interpreter;

import java.io.IOException;

import de.dante.extex.scanner.stream.TokenStream;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.Observable;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.file.FileFinder;

/**
 * ...
 *
 * @see "TeX -- The Program [1029]"
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public interface Interpreter extends TokenSource,
                                     Observable {

    /**
     * Setter for the file finder.
     * @param fileFinder    the new filefinder
     */
    void setFileFinder(FileFinder fileFinder);

    /**
     * Setter for the error handler.
     * The value of <code>null</code> can be used to delete the error handler
     * currently set.
     *
     * @param handler the new error handler
     */
    void setErrorHandler(ErrorHandler handler);

    /**
     * Getter for the error handler.
     * The error handler might not be set. In this case <code>null</code> is
     * returned.
     *
     * @return the error handler currently registered
     */
    ErrorHandler getErrorHandler();

    /**
     * Setter for the interaction mode.
     *
     * @param interaction the interaction mode
     */
    void setInteraction(Interaction interaction);

    /**
     * Getter for the interaction mode.
     *
     * @return the interaction mode
     */
    Interaction getInteraction();

    /**
     * ...
     *
     * @param jobname ...
     */
    void setJobname(String jobname);

    /**
     * Setter for the token stream factory.
     * During the processing of the input several occations might come up
     * where new token streams are needed. In this case the factory can be used
     * to acquire them.
     *
     * @param factory the token stream factory
     */
    void setTokenStreamFactory(TokenStreamFactory factory);

    /**
     * Setter for the typesetter.
     *
     * @param typesetter the new typesetter
     */
    void setTypesetter(Typesetter typesetter);

    /**
     * ...
     *
     * @param format the base name of the format file
     *
     * @throws IOException ...
     */
    void loadFormat(String format) throws IOException;

    /**
     * Process the current token streams by repeatedly reading a single token
     * and processing it until no token is left. The visitor pattern is used to
     * branch to the appropriate method for processing a single token. E.g. the
     * method
     * {@link TokenSource#visitActive(java.lang.Object,java.lang.Object) visitActive}
     * is used when the current token is an active character.
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws GeneralException in case of another error
     */
    void run() throws ConfigurationException, GeneralException;

    /**
     * Add a token stream and start processing it.
     *
     * @param stream the input stream to consider
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws GeneralException in case of another error
     *
     * @see #run()
     */
    void run(TokenStream stream) throws ConfigurationException,
            GeneralException;
}
