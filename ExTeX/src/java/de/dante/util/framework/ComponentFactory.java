/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.util.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationInvalidResourceException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.configuration.ConfigurationNotFoundException;
import de.dante.util.configuration.ConfigurationSyntaxException;
import de.dante.util.framework.component.Component;

/**
 * This is the factory for instances of
 * {@link de.dante.extex.typesetter.Typesetter Typesetter}.
 *
 * <pre>
 *  &lt;Typesetter class="the.package.TheClass"&gt;
 *  &lt;/Typesetter&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public class ComponentFactory {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the attribute
     * used to get the class name.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The constant <tt>DEFAULT_ATTRIBUTE</tt> contains the name of the
     * attribute used to get the default configuration.
     */
    private static final String DEFAULT_ATTRIBUTE = "default";

    /**
     * The field <tt>config</tt> contains the configuration for this factory.
     */
    private Configuration config = null;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration for this factory
     */
    public ComponentFactory(final Configuration configuration) {

        super();
        config = configuration;
    }

    /**
     * Get an instance of a typesetter.
     *
     * @param type ...
     *
     * @return a new typesetter
     *
     * @throws ConfigurationMissingAttributeException in case that a needed
     *  attribute was not proveided
     * @throws ConfigurationInvalidResourceException in case that the given
     *  resource name is nullor empty
     * @throws ConfigurationNotFoundException in case that the named path does
     *  not lead to a resource
     * @throws ConfigurationSyntaxException in case that the resource contains
     *  syntax errors
     * @throws ConfigurationIOException in case of an IO exception while
     *  reading the resource
     * @throws ConfigurationClassNotFoundException in case that the given
     *  class could not be found
     * @throws ConfigurationInstantiationException in cas of an exception
     *  during instantiation
     * @throws ConfigurationNoSuchMethodException in case that the expected
     *  constructor could not be found
     * @throws ConfigurationException in case of an error in the constructor
     */
    public Component createComponent(final String type)
            throws ConfigurationMissingAttributeException,
                ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException,
                ConfigurationClassNotFoundException,
                ConfigurationInstantiationException,
                ConfigurationNoSuchMethodException,
                ConfigurationException {

        Configuration cfg = config.findConfiguration(type != null ? type : "");
        if (cfg == null) {
            String fallback = config.getAttribute(DEFAULT_ATTRIBUTE);
            if (fallback == null) {
                throw new ConfigurationMissingAttributeException(
                        DEFAULT_ATTRIBUTE, config);
            }
            cfg = config.findConfiguration(fallback);
            if (cfg == null) {
                throw new ConfigurationMissingAttributeException(fallback,
                        config);
            }
        }

        String className = cfg.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    cfg);
        }

        Component component;

        try {
            Constructor constructor = Class.forName(className).getConstructor(
                    new Class[]{Configuration.class});
            component = (Component) constructor.newInstance(new Object[]{cfg});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(className + "("
                    + Configuration.class.getName() + ")");
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(className, config);
        } catch (IllegalArgumentException e) {
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
        }

        return component;

    }

}