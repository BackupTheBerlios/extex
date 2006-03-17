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
 * {@link de.dante.extex.language.LanguageManager LanguageManager}.
 *
 *
 * <pre>
 * &lt;Language default="ExTeX"&gt;
 *
 *   &lt;TeX class="de.dante.extex.language.impl.BaseLanguageManager"
 *        default="default"&gt;
 *     &lt;default
 *       class="de.dante.extex.language.hyphenation.liang.LiangsHyphenationTable"&gt;
 *       &lt;LigatureBuilder
 *         class="de.dante.extex.language.ligature.impl.LigatureBuilderImpl"/&gt;
 *       &lt;WordTokenizer
 *         class="de.dante.extex.language.word.impl.TeXWords"/&gt;
 *     &lt;/default&gt;
 *   &lt;/TeX&gt;
 *
 *   &lt;ExTeX class="de.dante.extex.language.impl.LoadingLanguageManager"
 *          default="default"&gt;
 *     &lt;default
 *       class="de.dante.extex.language.hyphenation.liang.LiangsHyphenationTable"&gt;
 *       &lt;LigatureBuilder
 *         class="de.dante.extex.language.ligature.impl.LigatureBuilderImpl"/&gt;
 *       &lt;WordTokenizer
 *         class="de.dante.extex.language.word.impl.ExTeXWords"/&gt;
 *     &lt;/default&gt;
 *   &lt;/ExTeX&gt;
 *
 * &lt;/Language&gt;
 * </pre>
 *
 * @see de.dante.util.framework.AbstractFactory
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class LanguageManagerFactory extends AbstractFactory {

    /**
     * Get an instance of a
     * {@link de.dante.extex.language.LanguageManager LanguageManager}.
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
