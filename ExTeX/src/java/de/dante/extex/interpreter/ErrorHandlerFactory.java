/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter;

import de.dante.extex.main.errorHandler.editHandler.EditHandler;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This is the factory for instances of
 * {@link de.dante.extex.interpreter.ErrorHandler ErrorHandler}.
 * This factory inherits its properties from the
 * {@link de.dante.util.framework.AbstractFactory AbstractFactory}. Among them
 * the support for configuration and logging.
 *
 * <h3>Configuration</h3>
 *
 * <p>
 *  Mainly the configuration needs to specify which class to use for the
 *  ErrorHandler. The configuration provides a mapping from a type name to the
 *  sub-configuration to be used. The name of the class is given as the argument
 *  <tt>class</tt> of the sub-configuration as shown below.
 *  <pre>
 *   &lt;ErrorHandler default="ExTeX"&gt;
 *     &lt;ExTeX class="de.dante.extex.main.errorHandler.ErrorHandlerImpl"&gt;
 *       &lt;EditHandler class="de.dante.extex.main.errorHandler.editHandler.EditHandlerTeXImpl"/&gt;
 *     &lt;/ExTeX&gt;
 *     &lt;TeX class="de.dante.extex.main.errorHandler.ErrorHandlerTeXImpl"&gt;
 *       &lt;EditHandler class="de.dante.extex.main.errorHandler.editHandler.EditHandlerTeXImpl"/&gt;
 *     &lt;/TeX&gt;
 *     &lt;extex class="de.dante.extex.main.errorHandler.ErrorHandlerImpl"/&gt;
 *     &lt;tex class="de.dante.extex.main.errorHandler.ErrorHandlerTeXImpl"/&gt;
 *    &lt;/ErrorHandler&gt;
 *  </pre>
 * </p>
 * <p>
 *  The named class need to implement the interface
 *  {@link de.dante.extex.interpreter.ErrorHandler ErrorHandler}. If
 *  this interface is not implemented an error is raised.
 * </p>
 * <p>
 *  The configuration is passed down to the new instance if it implements the
 *  interface {@link de.dante.util.framework.configuration.Configurable Configurable}.
 * </p>
 * <p>
 *  If the class implements the interface
 *  {@link de.dante.util.framework.logger.LogEnabled LogEnabled} then a logger
 *  is passed to the new instance. For this purpose the factory itself is
 *  log enabled to receive the logger.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class ErrorHandlerFactory extends AbstractFactory {

    /**
     * Creates a new object.
     */
    public ErrorHandlerFactory() {

        super();
    }

    /**
     * Creates a new object and configures it according to the parameter.
     *
     * @param configuration the configuration to use
     *
     * @throws ConfigurationException in case of an error during configuration
     */
    public ErrorHandlerFactory(final Configuration configuration)
            throws ConfigurationException {

        super();
        configure(configuration);
    }

    /**
     * Get an instance of an error handler.
     * This method selects one of the entries in the configuration. The
     * selection is done with the help of a type String. If the type is
     * <code>null</code> or the empty string then the default from the
     * configuration is used.
     *
     * @param type the type to use
     *
     * @return a new error handler
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public ErrorHandler newInstance(final String type)
            throws ConfigurationException {

        ErrorHandler errorHandler = (ErrorHandler) createInstance(type,
                ErrorHandler.class);
        Configuration cfg = selectConfiguration(type).findConfiguration(
                "EditHandler");
        if (cfg != null) {
            errorHandler
                    .setEditHandler((EditHandler) createInstanceForConfiguration(
                            cfg, EditHandler.class));
        }
        return errorHandler;
    }

}
