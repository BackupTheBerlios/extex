/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import de.dante.extex.unicodeFont.exception.FontException;
import de.dante.extex.unicodeFont.exception.FontNoFileExtensionFoundException;
import de.dante.extex.unicodeFont.exception.FontNotFoundException;
import de.dante.extex.unicodeFont.key.FontKey;
import de.dante.extex.unicodeFont.key.FontKeyConfigurable;
import de.dante.extex.unicodeFont.type.FontInit;
import de.dante.extex.unicodeFont.type.InputStreamConfigurable;
import de.dante.extex.unicodeFont.type.TexFont;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.Registrar;
import de.dante.util.framework.RegistrarException;
import de.dante.util.framework.RegistrarObserver;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;
import de.dante.util.resource.PropertyConfigurable;
import de.dante.util.resource.ResourceConsumer;
import de.dante.util.resource.ResourceFinder;

/**
 * Factory for the font system.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class FontFactory extends AbstractFactory
        implements
            ResourceConsumer,
            RegistrarObserver,
            PropertyConfigurable {

    /**
     * Create a new object.
     *
     * @param config    The configuration object.
     * @param theFinder The resource finder.
     * @throws ConfigurationException from the configuration system.
     */
    public FontFactory(final Configuration config,
            final ResourceFinder theFinder) throws ConfigurationException {

        super();

        finder = theFinder;
        configure(config);

        Registrar.register(this, TexFont.class);

    }

    // MGN cache!!!

    /**
     * Returns a new Instance of the font.
     *
     * @param key   The font key.
     * @return Returns a new Instance of the font.
     * @throws ConfigurationException From the configuration system.
     * @throws FontException if an font-error occurred.
     */
    public TexFont newInstance(final FontKey key)
            throws ConfigurationException, FontException {

        Configuration config = getConfiguration();

        Iterator it = config.iterator("font");
        while (it.hasNext()) {
            Configuration cfg = (Configuration) it.next();

            String fileextension = cfg.getAttribute("fileextension");
            if (fileextension == null) {
                throw new FontNoFileExtensionFoundException();
            }

            InputStream in = finder.findResource(key.getName(), fileextension);

            if (in != null) {
                TexFont texFont = (TexFont) createInstanceForConfiguration(cfg,
                        TexFont.class);

                //                if (texFont instanceof LogEnabled) {
                //                    ((LogEnabled) texFont).enableLogging(getLogger());
                //                }
                if (texFont instanceof PropertyConfigurable) {
                    ((PropertyConfigurable) texFont).setProperties(properties);
                }
                if (texFont instanceof InputStreamConfigurable) {
                    ((InputStreamConfigurable) texFont).setInputStream(in);
                }
                if (texFont instanceof FontKeyConfigurable) {
                    ((FontKeyConfigurable) texFont).setFontKey(key);
                }
                // init
                if (texFont instanceof FontInit) {
                    ((FontInit) texFont).init();
                }
                return texFont;
            }
        }

        throw new FontNotFoundException(key.getName());
    }

    /**
     * The resource finder.
     */
    private ResourceFinder finder;

    /**
     * @see de.dante.util.resource.ResourceConsumer#setResourceFinder(
     *      de.dante.util.resource.ResourceFinder)
     */
    public void setResourceFinder(final ResourceFinder theFinder) {

        finder = theFinder;
    }

    /**
     * @see de.dante.util.framework.AbstractFactory#reconnect(java.lang.Object)
     */
    public Object reconnect(final Object instance) throws RegistrarException {

        Object object = super.reconnect(instance);

        if (object instanceof TexFont) {
            TexFont f = (TexFont) object;
            try {
                return newInstance(f.getFontKey());
            } catch (ConfigurationException e) {
                throw new RegistrarException(e);
            } catch (FontException e) {
                throw new RegistrarException(e);
            }
        }
        // TODO resourceBundle
        throw new RegistrarException("no TexFont");
    }

    /**
     * the properties
     */
    private Properties properties;

    /**
     * @see de.dante.util.resource.PropertyConfigurable#setProperties(
     *      java.util.Properties)
     */
    public void setProperties(final Properties theProperties) {

        properties = theProperties;
    }
}
