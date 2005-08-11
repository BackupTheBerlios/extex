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

package de.dante.util.framework.configuration;

import java.lang.reflect.InvocationTargetException;

import de.dante.util.framework.configuration.exception.ConfigurationClassNotFoundException;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationInstantiationException;
import de.dante.util.framework.configuration.exception.ConfigurationInvalidNameException;
import de.dante.util.framework.configuration.impl.ConfigurationXMLImpl;


/**
 * This is the factory for configurations.
 * <p>
 * The class to be used for the configuration can be set with the
 * <tt>System.property</tt> named <tt>Util.Configuarion.class</tt>.
 * If this property is not set then the fallback class
 * {@link de.dante.util.framework.configuration.impl.ConfigurationXMLImpl ConfigurationXMLImpl}
 * is used instead.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ConfigurationFactory {

    /**
     * Creates a new object.
     */
    public ConfigurationFactory() {

        super();
    }

    /**
     * Delivers a new
     * {@link de.dante.util.framework.configuration.Configuration Configuration}
     * object which is initialized from a named source. This source is usually
     * a file name but can be anything else, like a URL or a reference to a
     * database &ndash; depending on the underlying implementation.
     *
     * @param source the source of the configuration
     *
     * @return a new Configuration object
     *
     * @throws ConfigurationException in case of an error. Especially
     * <ul>
     *  <li>ConfigurationInvalidNameException in case that the source is
     *             <code>null</code></li>
     *  <li>ConfigurationInstantiationException in case of some kind of
     *             error during instantiation</li>
     *  <li>ConfigurationClassNotFoundException in case that the class could
     *             not be found</li>
     *  <li>ConfigurationException in case that the creation of the
     *             Configuration fails</li>
     * </ul>
     */
    public Configuration newInstance(final String source)
            throws ConfigurationException {

        if (source == null) {
            throw new ConfigurationInvalidNameException(null);
        }

        String classname = System.getProperty("Util.Configuration.class");

        if (classname == null) {
            return new ConfigurationXMLImpl(source);
        }

        Configuration config = null;

        try {
            config = (Configuration) (Class.forName(classname).getConstructor(
                    new Class[]{String.class})
                    .newInstance(new Object[]{source}));
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            Throwable c = e.getCause();
            if (c != null && c instanceof ConfigurationException) {
                throw (ConfigurationException) c;
            }
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname);
        }

        return config;
    }

}
