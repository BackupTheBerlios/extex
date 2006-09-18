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

package de.dante.extex.interpreter;

import java.util.Properties;
import java.util.logging.Logger;

import de.dante.extex.backend.outputStream.OutputStreamFactory;
import de.dante.extex.interpreter.type.OutputStreamConsumer;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.PropertyConfigurable;

/**
 * This class provides a factory for
 * {@link de.dante.extex.interpreter.Interpreter Interpreter}s.
 * The configuration and the logger are passed to the new instance if they are
 * present and required.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.22 $
 */
public class InterpreterFactory extends AbstractFactory {

    /**
     * Creates a new factory object.
     *
     * @param configuration the configuration for this factory
     * @param logger the logger
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    public InterpreterFactory(final Configuration configuration,
            final Logger logger) throws ConfigurationException {

        super();
        enableLogging(logger);
        configure(configuration);
    }

    /**
     * Get a instance for the interface
     * <tt>{@link de.dante.extex.interpreter.Interpreter Interpreter}</tt>.
     *
     * @param properties the properties
     * @param outFactory the output stream factory
     *
     * @return a new instance for the interface Interpreter
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public Interpreter newInstance(final Properties properties,
            final OutputStreamFactory outFactory) throws ConfigurationException {

        Interpreter interpreter = (Interpreter) createInstance(Interpreter.class);

        if (interpreter instanceof PropertyConfigurable) {
            ((PropertyConfigurable) interpreter).setProperties(properties);
        }
        if (interpreter instanceof OutputStreamConsumer) {
            ((OutputStreamConsumer) interpreter)
                    .setOutputStreamFactory(outFactory);
        }

        return interpreter;
    }

}
