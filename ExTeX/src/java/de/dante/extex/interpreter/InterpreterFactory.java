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

import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a factory for
 * {@link de.dante.extex.interpreter.Interpreter Interpreter}s.
 * The configuration and the logger are passed to the new instance if they are
 * present and required.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class InterpreterFactory extends AbstractFactory {

    /**
     * Creates a new factory object.
     */
    public InterpreterFactory() {

        super();
    }

    /**
     * Creates a new object.
     * Usually the newly created instance should be configured (see
     * {@link AbstractFactory#configure(Configuration) configure}) and
     * a logger should be attached
     * (see @link AbstractFactory#enableLogging(Logger)).
     *
     * @param configuration the configuration object to use
     *
     * @throws ConfigurationException in case that the attribute
     *             <tt>classname</tt> is missing
     * @deprecated use the constructor without arguments and configure()
     */
    public InterpreterFactory(final Configuration configuration)
            throws ConfigurationException {

        super();
        configure(configuration);
    }

    /**
     * Get a instance for the interface
     * <tt>{@link de.dante.extex.interpreter.Interpreter Interpreter}</tt>.
     *
     * @return a new instance for the interface Interpreter
     *
     * @throws ConfigurationException in case of an error in the configuration
     */
    public Interpreter newInstance() throws ConfigurationException {

        Interpreter interpreter = (Interpreter) createInstance(Interpreter.class);

        return interpreter;
    }
}