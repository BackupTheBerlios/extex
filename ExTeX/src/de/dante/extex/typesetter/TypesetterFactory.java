/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.typesetter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.dante.extex.interpreter.context.Context;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;

/**
 * This is the factory for instances of
 * {@link de.dante.extex.typesetter.Typesetter Typesetter}.
 *
 * <pre>
 *  &lt;Typesetter class="the.package.TheClass"&gt;
 *  &lt;/Typesetter&gt;
 * </pre>
 *
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class TypesetterFactory {

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
     *   newInstance}.
     */
    private Constructor constructor;

    /**
     * Creates a new object.
     *
     * @param config the configuration for this factory
     */
    public TypesetterFactory(final Configuration config)
            throws ConfigurationException {

        super();
        this.config = config;

        String classname = config.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    config);
        }

        try {
            constructor = Class.forName(classname)
                    .getConstructor(
                                    new Class[]{Configuration.class,
                                            Context.class});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname, config);
        }
    }

    /**
     * Get an instance of a typesetter.
     *
     * @return a new typesetter
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public Typesetter newInstance(final Context context)
            throws ConfigurationException {

        Typesetter typesetter;

        try {
            typesetter = (Typesetter) constructor.newInstance(new Object[]{
                    config, context});
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

        return typesetter;
    }
}
