/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.interpreter;

import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.configuration.ConfigurationNoSuchMethodException;

/**
 * Factory for the Interpreter.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.15 $
 */
public class InterpreterFactory {

    /**
     * The constant <tt>CLASS_ATTRIBUTE</tt> contains the name of the attribute
     * for the class name to be used.
     */
    private static final String CLASS_ATTRIBUTE = "class";

    /**
     * The field <tt>config</tt> contains the configuration for this factory
     */
    private Configuration config;

    /**
     * The field <tt>classname</tt> contains the name of the class to
     * instantiate.
     */
    private String classname;

    /**
     * Creates a new object.
     *
     * @param configuration the configuration object to use
     *
     * @throws ConfigurationException in case that the attribute
     *             <tt>classname</tt> is missing
     */
    public InterpreterFactory(final Configuration configuration)
            throws ConfigurationException {
        super();
        this.config = configuration;
        classname = config.getAttribute(CLASS_ATTRIBUTE);
        if (classname == null) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,
                    config);
        }
    }

    /**
     * Get a instance for the interface
     * <tt>{@link de.dante.extex.interpreter.Interpreter Interpreter}</tt>.
     *
     * @return a new instance for the interface Interpreter
     *
     * @throws ConfigurationException in case that the instantiation failes
     */
    public Interpreter newInstance() throws ConfigurationException {

        Interpreter interpreter;

        try {
            interpreter = (Interpreter) (Class.forName(classname)
                    .getConstructor(new Class[]{Configuration.class})
                    .newInstance(new Object[]{config}));
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
            throw new ConfigurationNoSuchMethodException(classname
                                                         + "("
                                                         + Configuration.class
                                                                 .getName()
                                                         + ")");
        } catch (ClassNotFoundException e) {
            throw new ConfigurationClassNotFoundException(classname, config);
        }

        return interpreter;
    }
}
