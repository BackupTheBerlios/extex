/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;
import de.dante.util.framework.AbstractFactory;

/**
 * This class provides a factory for a
 * {@link de.dante.extex.interpreter.context.Context Context}.
 *
 *
 * <pre>
 *  &lt;Context class="the.package.TheClass"&gt;
 *  &lt;/Context&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class ContextFactory extends AbstractFactory {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the attribute
     * used to get the class name.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * Creates a new object.
     *
     * @param configuration the configuration for this factory
     *
     * @throws ConfigurationException in case of an error in the configuration.
     */
    public ContextFactory(final Configuration configuration)
            throws ConfigurationException {

        super();
        configure(configuration);
    }

    /**
     * Get an instance of a context.
     * This method selects one of the entries in the configuration. The
     * selection is done with the help of a type String. If the type is
     * <code>null</code> or the empty string then the default from the
     * configuration is used.
     *
     * @param type the type to use
     *
     * @return a new context
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public Context newInstance(final String type) throws ConfigurationException {

        Configuration cfg = selectConfiguration(type);
        String className = cfg.getAttribute(CLASS_ATTRIBUTE);

        if (className == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    cfg);
        }

        Context context;

        try {
            Constructor constructor = Class.forName(className).getConstructor(
                    new Class[]{Configuration.class});
            context = (Context) constructor.newInstance(new Object[]{cfg});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(className + "("
                    + Configuration.class.getName() + ")");
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(className,
                    getConfiguration());
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

        enableLogging(context, getLogger());

        return context;
    }

}