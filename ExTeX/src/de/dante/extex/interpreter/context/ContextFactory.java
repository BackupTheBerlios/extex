/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.interpreter.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;

/**
 * ...
 *
 *
 * <pre>
 *  &lt;Context class="the.package.TheClass"&gt;
 *  &lt;/Context&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class ContextFactory {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> ...
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>config</tt> contains the configuration for this factory.
     */
    private Configuration config = null;

    /**
     * The field <tt>constructor</tt> contains the constructor of the class to
     * instantiate. It is kept here to speed up the method
     * {@link #newInstance(de.dante.extex.interpreter.context.impl.Group)
     *  newInstance}.
     */
    private Constructor constructor;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration for this factory
     *
     * @throws ConfigurationException ...
     */
    public ContextFactory(final Configuration configuration)
            throws ConfigurationException {
        super();
        config = configuration;

        String classname = config.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    config);
        }

        try {
            constructor = Class.forName(classname).getConstructor(
                    new Class[]{Configuration.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname, config);
        }
    }

    /**
     * Get a instance for the interface Context.
     *
     * @return a new instance for the interface Context
     *
     * @throws ConfigurationException ...
     */
    public Context newInstance() throws ConfigurationException {
        Context context;

        try {
            context = (Context) (constructor.newInstance(new Object[]{config}));
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

        return context;
    }
}
