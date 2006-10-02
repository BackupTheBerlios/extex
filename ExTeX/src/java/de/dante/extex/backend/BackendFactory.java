/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.backend;

import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import de.dante.extex.backend.documentWriter.DocumentWriter;
import de.dante.extex.backend.documentWriter.DocumentWriterFactory;
import de.dante.extex.backend.documentWriter.DocumentWriterOptions;
import de.dante.extex.backend.documentWriter.MultipleDocumentStream;
import de.dante.extex.backend.documentWriter.exception.DocumentWriterException;
import de.dante.extex.backend.outputStream.OutputStreamFactory;
import de.dante.extex.color.ColorAware;
import de.dante.extex.color.ColorConverter;
import de.dante.extex.font.FontFactory;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

/**
 * This class provides a factory for the back-end.
 *
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
 *  <dt>{@link de.dante.util.resource.PropertyConfigurable PropertyConfigurable}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link java.util.Properties Properties} instance
 *   is passed in with the interface method.
 *  </dd>
 *  <dt>{@link de.dante.extex.color.ColorAware ColorAware}</dt>
 *  <dd>
 *   If this interface is implemented then a
 *   {@link de.dante.extex.color.ColorConverter ColorConverter} instance
 *   is passed in with the interface method.
 *  </dd>
 * </dl>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class BackendFactory extends AbstractFactory {

    /**
     * Creates a new object.
     *
     * @param config the configuration
     * @param logger the logger
     *
     * @throws ConfigurationException in case of an configuration error
     */
    public BackendFactory(final Configuration config, final Logger logger)
            throws ConfigurationException {

        super();
        enableLogging(logger);
        configure(config);
    }

    /**
     * Acquire an instance of a back-end driver.
     *
     * @param type ...
     * @param options ...
     * @param outFactory the output stream factory
     * @param finder the resource finder
     * @param properties the properties
     * @param creator the creator string
     * @param colorConverter the color converter
     *
     * @return the new instance
     *
     * @throws DocumentWriterException in case of an error
     * @throws ConfigurationException in case of an error in the configuration
     */
    public BackendDriver newInstance(final String type,
            final DocumentWriterOptions options,
            final OutputStreamFactory outFactory, final ResourceFinder finder,
            final Properties properties, final String creator,
            final ColorConverter colorConverter)
            throws DocumentWriterException,
                ConfigurationException {

        BackendDriver backend = (BackendDriver) createInstanceForConfiguration(
                getConfiguration(), BackendDriver.class);

        DocumentWriterFactory factory = new DocumentWriterFactory(
                getConfiguration().getConfiguration("DocumentWriter"),
                getLogger());
        DocumentWriter docWriter = factory.newInstance(//
                type, //
                options, //
                outFactory);
        if (docWriter instanceof PropertyConfigurable) {
            ((PropertyConfigurable) docWriter).setProperties(properties);
        }
        if (docWriter instanceof ColorAware) {
            ((ColorAware) docWriter).setColorConverter(colorConverter);
        }
        if (docWriter instanceof ResourceConsumer) {
            ((ResourceConsumer) docWriter).setResourceFinder(finder);
        }
        if (docWriter instanceof FontFactoryConsumer) {
            FontFactory fontFactory = null; //TODO gene: provide factory
            ((FontFactoryConsumer) docWriter).setFontFactory(fontFactory );
        }
        docWriter.setParameter("Creator", creator);

        Iterator iterator = getConfiguration().iterator("parameter");
        while (iterator.hasNext()) {
            Configuration p = (Configuration) iterator.next();
            String name = p.getAttribute("name");
            String s = p.getAttribute("value");
            if (s != null) {
                docWriter.setParameter(name, s);
            } else {
                s = p.getAttribute("property");
                if (s != null) {
                    docWriter.setParameter(name, properties.getProperty(s));
                } else {
                    throw new ConfigurationMissingAttributeException("value", p);
                }
            }
        }

        backend.setDocumentWriter(docWriter);

        if (backend instanceof MultipleDocumentStream) {
            ((MultipleDocumentStream) backend)
                    .setOutputStreamFactory(outFactory);
        }

        return backend;
    }

}
