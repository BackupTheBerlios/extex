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
package de.dante.extex.interpreter;

import java.lang.reflect.InvocationTargetException;

import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationClassNotFoundException;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.configuration.ConfigurationMissingAttributeException;
import de.dante.util.file.FileFinder;

/**
 * Factory for the Interpreter.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class InterpreterFactory {

	/**
	 * The constant <tt>CLASS_ATTRIBUTE</tt> ...
	 */
	private static final String CLASS_ATTRIBUTE = "class";

	/** the configuration for this factory */
	private Configuration config;

	/**
	 * The field <tt>classname</tt> ...
	 */
	private String classname;

	/**
	 * The field <tt>finder</tt> ...
	 */
	private FileFinder finder = null;

    /**
     * Creates a new object.
     * 
     * @param config ...
     * @throws ConfigurationException ...
     */
    public InterpreterFactory(Configuration config)
            throws ConfigurationException {
        super();
        this.config = config;
        classname = config.getAttribute(CLASS_ATTRIBUTE);
        if ( classname == null ) {
            throw new ConfigurationMissingAttributeException(CLASS_ATTRIBUTE,config);
        }
    }

	/**
	 * Get a instance for the interface Interpreter.
	 * 
	 * @return a new instance for the interface Interpreter
	 */
	public Interpreter newInstance() throws ConfigurationException {
		Interpreter interpreter;

		try {
			interpreter =
				(Interpreter) (Class
					.forName(classname)
					.getConstructor(new Class[] { Configuration.class })
					.newInstance(new Object[] { config }));
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
		    if (c!=null && c instanceof ConfigurationException) {
		        throw (ConfigurationException)c;
		    }
		    throw new ConfigurationInstantiationException(e);
		} catch (NoSuchMethodException e) {
		    throw new ConfigurationInstantiationException(e);
		} catch (ClassNotFoundException e) {
		    throw new ConfigurationClassNotFoundException(classname,config);
		}

		return interpreter;
	}
}
