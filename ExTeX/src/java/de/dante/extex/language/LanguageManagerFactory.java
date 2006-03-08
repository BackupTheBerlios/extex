/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language;

import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This class provides a factory for a
 * {@link de.dante.extex.LanguageManager LanguageManager}.
 *
 *
 * <pre>
 *  &lt;Context class="the.package.TheClass"&gt;
 *  &lt;/Context&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class LanguageManagerFactory extends AbstractFactory {

    /**
     * Get an instance of a HyphenationManager.
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
    public LanguageManager newInstance(final String type)
            throws ConfigurationException {

        return (LanguageManager) createInstance(type, LanguageManager.class);
    }

}
