/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter;

import de.dante.extex.interpreter.context.Context;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.AbstractFactory;

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
 * @version $Revision: 1.11 $
 */
public class TypesetterFactory extends AbstractFactory {

    /**
     * Creates a new object.
     *
     * @param configuration the configuration for this factory
     *
     * @throws ConfigurationException ...
     */
    public TypesetterFactory(final Configuration configuration)
            throws ConfigurationException {

        super();
        configure(configuration);
    }

    /**
     * Get an instance of a typesetter.
     *
     * @param type ...
     * @param context the interpreter context
     *
     * @return a new typesetter
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public Typesetter newInstance(final String type, final Context context)
            throws ConfigurationException {

        Typesetter typesetter = (Typesetter) createInstance(type,
                Typesetter.class);
        typesetter.setOptions((TypesetterOptions) context);

        return typesetter;

    }

}