/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.util.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationIOException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationInvalidClassException;
import de.dante.util.configuration.ConfigurationInvalidResourceException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNotFoundException;
import de.dante.util.configuration.ConfigurationSyntaxException;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.i18n.Localizable;
import de.dante.util.framework.i18n.LocalizerFactory;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is the abstract base class for factories. It contains some common
 * methods which should make it easy to create a custom factory.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public abstract class AbstractFactory implements Configurable, LogEnabled {

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
     * Configure an instance if this instance supports configuration.
     * If configuration is not supported then nothing is done.
     *
     * @param instance the instance to configure
     * @param configuration the configuration to use. If this parameter is
     *  <code>null</code> then it is not passed to the instance.
     *
     * @throws ConfigurationException in case of an error
     */
    public static void configure(final Object instance,
            final Configuration configuration) throws ConfigurationException {

        if (configuration != null && instance instanceof Configurable) {
            ((Configurable) instance).configure(configuration);
        }
    }

    /**
     * ...
     *
     * @param instance the instance to pass the localizer to
     * @param className the class name for the instance
     */
    private static void enableLocalization(final Object instance,
            final String className) {

        if (instance instanceof Localizable) {
            ((Localizable) instance).enableLocalization(LocalizerFactory
                    .getLocalizer(className));
        }
    }

    /**
     * Utility method to pass a logger to an object if it has a method to take
     * it.
     * If the logger is <code>null</code> then this method simply does nothing.
     *
     * @param instance the instance to pass the logger to
     * @param logger the logger to pass. If the logger is <code>null</code>
     *  then nothing is done.
     */
    public static void enableLogging(final Object instance, final Logger logger) {

        if (logger != null && instance instanceof LogEnabled) {
            ((LogEnabled) instance).enableLogging(logger);
        }
    }

    /**
     * The field <tt>configuration</tt> contains the configuration of the
     * factory which is also passed to the new instances.
     */
    private Configuration configuration = null;

    /**
     * The field <tt>logger</tt> contains the logger to pass to the new
     * typesetters.
     */
    private Logger logger = null;

    /**
     * Creates a new factory object.
     */
    public AbstractFactory() {

        super();
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration theConfiguration)
            throws ConfigurationException {

        configuration = theConfiguration;
    }

    /**
     * Get an instance.
     *
     * @param target the expected class or interface
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstance(final Class target)
            throws ConfigurationException {

        return createInstanceForConfiguration(configuration, target);
    }

    /**
     * Get an instance.
     * This method selects one of the entries in the configuration. The
     * selection is done with the help of a type String. If the type is
     * <code>null</code> or the empty string then the default from the
     * configuration is used.
     *
     * @param type the type to use
     * @param target the expected class or interface
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstance(final String type, final Class target)
            throws ConfigurationException {

        return createInstanceForConfiguration(selectConfiguration(type), target);
    }

    /**
     * Create a new instance for a given configuration.
     *
     * @param config the configuration to use
     * @param target the expected class or interface
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstanceForConfiguration(final Configuration config,
            final Class target) throws ConfigurationException {

        String className = config.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    config);
        }

        try {
            Class theClass = Class.forName(className);

            if (!target.isAssignableFrom(theClass)) {
                throw new ConfigurationInvalidClassException(target.getName(),
                        config);
            }

            Constructor[] constructors = theClass.getConstructors();
            Object instance = null;

            for (int i = 0; i < constructors.length; i++) {
                Class[] args = constructors[i].getParameterTypes();
                switch (args.length) {
                    case 0:
                        instance = theClass.newInstance();
                        enableLocalization(instance, className);
                        enableLogging(instance, getLogger());
                        configure(instance, config);
                        return instance;

                    case 1:
                        if (args[0].isAssignableFrom(Configuration.class)) {
                            instance = constructors[i]
                                    .newInstance(new Object[]{config});
                            enableLocalization(instance, className);
                            enableLogging(instance, getLogger());
                            return instance;
                        } else if (args[0].isAssignableFrom(Logger.class)) {
                            instance = constructors[i]
                                    .newInstance(new Object[]{logger});
                            enableLocalization(instance, className);
                            configure(instance, config);
                            return instance;
                        }
                        break;

                    case 2:
                        if (args[0].isAssignableFrom(Configuration.class)
                                && args[1].isAssignableFrom(Logger.class)) {
                            instance = constructors[i]
                                    .newInstance(new Object[]{config, logger});
                            enableLocalization(instance, className);
                            return instance;
                        } else if (args[0].isAssignableFrom(Logger.class)
                                && args[1]
                                        .isAssignableFrom(Configuration.class)) {
                            instance = constructors[i]
                                    .newInstance(new Object[]{logger, config});
                            enableLocalization(instance, className);
                            return instance;
                        }
                        break;

                    default: // Fall through to exception
                }
            }

        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(className, config);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof ConfigurationException) {
                throw (ConfigurationException) cause;
            }
            throw new ConfigurationInstantiationException(e);
        }

        throw new ConfigurationInvalidClassException(target.getName(), config);
    }


    /**
     * Create a new instance for a given configuration.
     *
     * @param config the configuration to use
     * @param target the expected class or interface
     * @param arg1 the first (String) constructor argument
     *
     * @return a new instance
     *
     * @throws ConfigurationException in case of an configuration error
     */
    protected Object createInstanceForConfiguration(final Configuration config,
            final Class target, final String arg1) throws ConfigurationException {

        String className = config.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    config);
        }

        try {
            Class theClass = Class.forName(className);

            if (!target.isAssignableFrom(theClass)) {
                throw new ConfigurationInvalidClassException(target.getName(),
                        config);
            }

            Constructor[] constructors = theClass.getConstructors();
            Object instance = null;

            for (int i = 0; i < constructors.length; i++) {
                Class[] args = constructors[i].getParameterTypes();
                switch (args.length) {
                    case 1:
                        if (args[0].isAssignableFrom(String.class)) {
                            instance = constructors[i]
                                    .newInstance(new Object[]{arg1});
                            enableLogging(instance, getLogger());
                            configure(instance, config);
                            enableLocalization(instance, className);
                            return instance;
                        }
                        break;

                    default: // Fall through to exception
                }
            }

        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(className, config);
        } catch (IllegalArgumentException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InstantiationException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof ConfigurationException) {
                throw (ConfigurationException) cause;
            }
            throw new ConfigurationInstantiationException(e);
        }

        throw new ConfigurationInvalidClassException(target.getName(), config);
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * Getter for configuration.
     *
     * @return the configuration.
     */
    public Configuration getConfiguration() {

        return this.configuration;
    }

    /**
     * Getter for logger.
     *
     * @return the logger.
     */
    public Logger getLogger() {

        return this.logger;
    }

    /**
     * Select a sub-configuration with a given name. If this does not exist
     * then the attribute <tt>default</tt> is used to find an alternative.
     *
     * @param type the tag name for the sub-configuration to find
     *
     * @return the desired sub-configuration
     *
     * @throws ConfigurationInvalidResourceException in case that the given
     *  resource name is <code>null</code> or empty.
     * @throws ConfigurationNotFoundException in case that the requested
     *  configuration does not exist.
     * @throws ConfigurationSyntaxException in case of a syntax error in the
     *  configuration.
     * @throws ConfigurationIOException in case that an IOException occurred
     *  while reading the configuration.
     * @throws ConfigurationMissingAttributeException in case that an attribute
     *  is missing.
     */
    protected Configuration selectConfiguration(final String type)
            throws ConfigurationInvalidResourceException,
                ConfigurationNotFoundException,
                ConfigurationSyntaxException,
                ConfigurationIOException,
                ConfigurationMissingAttributeException {

        Configuration config = this.configuration.findConfiguration(type);
        if (config == null) {
            String fallback = this.configuration.getAttribute(DEFAULT_ATTRIBUTE);
            if (fallback == null) {
                throw new ConfigurationMissingAttributeException(
                        DEFAULT_ATTRIBUTE, configuration);
            }
            config = configuration.findConfiguration(fallback);
            if (config == null) {
                throw new ConfigurationMissingAttributeException(fallback,
                        this.configuration);
            }
        }
        return config;
    }

}