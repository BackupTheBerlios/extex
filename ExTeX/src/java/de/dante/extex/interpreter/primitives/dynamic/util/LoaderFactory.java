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

package de.dante.extex.interpreter.primitives.dynamic.util;

import de.dante.extex.interpreter.unit.Loader;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LoaderFactory extends AbstractFactory {

    /**
     * Create a new instance of the class given by the attribute
     * <tt>class</tt> of the configuration.
     *
     * @return the Code loaded
     *
     * @throws ConfigurationException in case of an error
     */
    public Loader createLoad() throws ConfigurationException {

        return (Loader) createInstanceForConfiguration(getConfiguration(),
                Loader.class);
    }

}
