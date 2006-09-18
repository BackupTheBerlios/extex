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

import java.util.logging.Logger;

import de.dante.extex.backend.outputStream.OutputStreamFactory;
import de.dante.extex.interpreter.type.OutputStreamConsumer;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

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
 * <p>
 *  The class to be instantiated can implements one or more interfaces which
 *  trigger special actions:
 * </p>
 * <dl>
 *  <dt>{@link de.dante.util.framework.configuration.Configurable Configurable}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link de.dante.util.framework.configuration.Configuration Configuration}
 *   is passed in with the interface method.
 *  </dd>
 *  <dt>{@link de.dante.util.framework.logger.LogEnabled LogEnabled}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link java.util.logging.Logger Logger}
 *   is passed in with the interface method.
 *  </dd>
 *  <dt>{@link de.dante.util.framework.i18n.Localizable Localizable}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link de.dante.util.framework.i18n.Localizer Localizer}
 *   is passed in with the interface method.
 *  </dd>
 *  <dt>{@link de.dante.util.resource.ResourceConsumer ResourceConsumer}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link de.dante.util.resource.ResourceFinder ResourceFinder}
 *   is passed in with the interface method.
 *  </dd>
 *  <dt>{@link de.dante.extex.interpreter.type.OutputStreamConsumer OutputStreamConsumer}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link de.dante.extex.backend.outputStream.OutputStreamFactory OutputStreamFactory}
 *   is passed in with the interface method.
 *  </dd>
 * </dl>
 *
 *
 *
 * @see de.dante.util.framework.AbstractFactory
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class LanguageManagerFactory extends AbstractFactory {

    /**
     * Creates a new object.
     *
     * @param config the configuration
     * @param logger the logger
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public LanguageManagerFactory(final Configuration config,
            final Logger logger) throws ConfigurationException {

        super();
        enableLogging(logger);
        configure(config);
    }

    /**
     * Get an instance of a
     * {@link de.dante.extex.language.LanguageManager LanguageManager}.
     * This method selects one of the entries in the configuration. The
     * selection is done with the help of a type String. If the type is
     * <code>null</code> or the empty string then the default from the
     * configuration is used.
     *
     * @param type the type to use
     * @param outFactory the output stream factory to pass in
     * @param finder the resource finder to pass in
     *
     * @return a new context
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public LanguageManager newInstance(final String type,
            final OutputStreamFactory outFactory, final ResourceFinder finder)
            throws ConfigurationException {

        LanguageManager manager = (LanguageManager) createInstance(type,
                LanguageManager.class);
        if (manager instanceof OutputStreamConsumer) {
            ((OutputStreamConsumer) manager).setOutputStreamFactory(outFactory);
        }
        if (manager instanceof ResourceConsumer) {
            ((ResourceConsumer) manager).setResourceFinder(finder);
        }

        return manager;
    }

}
