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
package de.dante.extex.documentWriter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;

/**
 * This is the factory to provide an instance of a document writer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.6 $
 */
public class DocumentWriterFactory {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> ...
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>config</tt> contains the configuration to use.
     */
    private Configuration config;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration to use
     */
    public DocumentWriterFactory(final Configuration configuration) {

        super();
        this.config = configuration;
    }

    /**
     * Provide a new instance of a document writer.
     *
     * @param type ...
     *
     * @return the new instance
     *
     * @throws ConfigurationException in case of an error
     */
    public DocumentWriter newInstance(final String type)
            throws ConfigurationException {

        Configuration cfg = config.findConfiguration(type != null ? type : "");
        if (cfg == null) {
            String fallback = config.getAttribute("default");
            if (fallback == null) {
                throw new ConfigurationMissingAttributeException("default",
                        config);
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

        DocumentWriter docWriter;

        try {
            Constructor constructor = Class.forName(className)
                    .getConstructor(new Class[]{Configuration.class});
            docWriter = (DocumentWriter) constructor
                    .newInstance(new Object[]{config});
        } catch (SecurityException e) {
            throw new ConfigurationInstantiationException(e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationNoSuchMethodException(className
                                                         + "("
                                                         + Configuration.class
                                                                 .getName()
                                                         + ")");
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

        return docWriter;

    }
}
