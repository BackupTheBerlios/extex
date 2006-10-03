/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.unit;

import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This is the factory for instances of
 * {@link de.dante.extex.interpreter.unit.UnitInfo UnitInfo}.
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
 *   &lt;unit class="de.dante.extex.interpreter.unit.tex.Setup"&gt;
 *   ...
 *  </pre>
 * </p>
 * <p>
 *  The named class needs to implement the interface
 *  {@link de.dante.extex.interpreter.unit.Loader Loader}. If
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
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class UnitInfoFactory extends AbstractFactory {

    /**
     * Create a new instance of the class given by the attribute
     * <tt>class</tt> of the configuration.
     *
     * @return the Code loaded
     *
     * @throws ConfigurationException in case of an error
     */
    public UnitInfo createUnitInfo() throws ConfigurationException {

        return (UnitInfo) createInstanceForConfiguration(getConfiguration(),
                UnitInfo.class);
    }

}
