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
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.configuration.ConfigurationInstantiationException;
import de.dante.util.file.FileFinder;

/**
 * Factory for the Interpreter.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.7 $
 */
public class InterpreterFactory {

	/** the configuration for this factory */
	private Configuration config;

	/** ... */
	private String classname;

	/**
	 * filefinder
	 */
	private FileFinder finder;

	/**
	 * Creates a new object.
	 */
	public InterpreterFactory(Configuration config, FileFinder fileFinder) throws ConfigurationException {
		super();
		this.config = config;
		classname = config.getAttribute("class");
		finder = fileFinder;
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
					.getConstructor(new Class[] { Configuration.class, FileFinder.class })
					.newInstance(new Object[] { config, finder }));
		} catch (IllegalArgumentException e) {
			throw new ConfigurationInstantiationException(e);
		} catch (SecurityException e) {
			throw new ConfigurationInstantiationException(e);
		} catch (InstantiationException e) {
			throw new ConfigurationInstantiationException(e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationInstantiationException(e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationInstantiationException(e);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationInstantiationException(e);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationInstantiationException(e);
		}

		return interpreter;
	}
}
