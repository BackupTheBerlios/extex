/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.util.resource;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import de.dante.util.StringListIterator;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.configuration.exception.ConfigurationMissingAttributeException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This resource finder utilizes the Java class finder to search in the class
 * path. Thus it is possible to find resources inside a jar archive.
 *
 * <h2>Configuration</h2>
 * The resource finder can be configured to influence its actions.
 * The following example shows a configuration for a resource finder:
 *
 * <pre>
 * &lt;Finder class="de.dante.util.resource.ClasspathFinder"
 *         trace="false"
 *         default="default"&gt;
 *   &lt;tex&gt;
 *     &lt;extension&gt;&lt;/extension&gt;
 *     &lt;extension&gt;.tex&lt;/extension&gt;
 *   &lt;/tex&gt;
 *   &lt;fmt&gt;
 *     &lt;extension&gt;&lt;/extension&gt;
 *     &lt;extension&gt;.fmt&lt;/extension&gt;
 *   &lt;/fmt&gt;
 *   &lt;default&gt;
 *     &lt;extension&gt;&lt;/extension&gt;
 *   &lt;/default&gt;
 * &lt;/Finder&gt;
 * </pre>
 *
 * <p>
 *  Whenever a resource is sought its type is used to find the appropriate
 *  parameters for the search. If the sub-configuration with the name of the
 *  type exists then this sub-configuration is used. For instance if the
 *  resource <tt>tex</tt> with the type <tt>fmt</tt> is sought then the
 *  sub-configuration <tt>fmt</tt> determines how to find this file.
 * </p>
 * <p>
 *  If no sub-configuration of the given type is present then the attribute
 *  <tt>default</tt> is used to find the default sub-configuration. In the
 *  example given above this default configuration is called <tt>default</tt>.
 *  Nevertheless it would also be possible to point the default configuration
 *  to another existing configuration. The attribute <tt>default</tt> is
 *  mandatory.
 * </p>
 * <p>
 *  Each sub-configuration takes the tag <tt>extension</tt> in arbitrary number.
 *  <tt>extension</tt> contains the extension appended after the resource name.
 * </p>
 * <p>
 *  All combinations of resource name and extension are tried in turn.
 *  If one combination leads to a readable input stream then it is used.
 * </p>
 * <p>
 *  The attribute <tt>trace</tt> can be used to force a tracing of the actions
 *  in the log file. The tracing is performed only if a logger is present when
 *  needed. The tracing flag can be overwritten at run-time.
 *  The attribute <tt>trace</tt> is optional.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class ClasspathFinder implements LogEnabled, ResourceFinder {

    /**
     * The field <tt>bundle</tt> contains the resource bundle for messages.
     */
    private transient ResourceBundle bundle = null;

    /**
     * The field <tt>configuration</tt> contains the configuration object on
     * which this resource finder is based.
     */
    private Configuration configuration;

    /**
     * The field <tt>logger</tt> contains the logger to be used for tracing.
     */
    private Logger logger = null;

    /**
     * The field <tt>trace</tt> contains the indicator that tracing is required.
     * This field is set to <code>true</code> according to the configuration.
     */
    private boolean trace = false;

    /**
     * Creates a new object.
     *
     * @param configuration the encapsulated configuration object
     */
    public ClasspathFinder(final Configuration configuration) {

        super();
        this.configuration = configuration;
        String t = configuration.getAttribute("trace");
        if (t != null && Boolean.valueOf(t).booleanValue()) {
            trace = true;
        }
    }

    /**
     * Setter for the logger.
     *
     * @param theLogger the logger to set.
     *
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#enableTracing(boolean)
     */
    public void enableTracing(final boolean flag) {

        trace = flag;
    }

    /**
     * @see de.dante.util.resource.ResourceFinder#findResource(
     *      java.lang.String,
     *      java.lang.String)
     */
    public InputStream findResource(final String name, final String type)
            throws ConfigurationException {

        if (trace && bundle == null) {
            bundle = ResourceBundle.getBundle(ClasspathFinder.class.getName());
        }

        ClassLoader classLoader = this.getClass().getClassLoader();

        Configuration cfg = configuration.findConfiguration(type);
        if (cfg == null) {
            String t = configuration.getAttribute("default");
            if (t == null) {
                throw new ConfigurationMissingAttributeException("default",
                        configuration);
            }
            cfg = configuration.getConfiguration(t);
            if (cfg == null) {
                return null;
            }
        }
        String t = cfg.getAttribute("skip");
        if (t != null && Boolean.valueOf(t).booleanValue()) {

            if (trace) {
                trace("Skipped", type, null);
            }
            return null;
        }
        String prefix = cfg.getAttribute("prefix");
        if (prefix == null) {
            prefix = "";
        }

        StringListIterator extIt = cfg.getValues("extension").getIterator();

        while (extIt.hasNext()) {
            String fullName = prefix + name + extIt.next();
            fullName = fullName.replaceAll("\\{type\\}", type);
            if (trace) {
                trace("Try", fullName, null);
            }
            InputStream stream = classLoader.getResourceAsStream(fullName);

            if (stream != null) {
                if (trace) {
                    trace("Found", fullName, null);
                }
                return stream;
            }
            if (trace) {
                trace("NotFound", fullName, null);
            }
        }
        return null;
    }

    /**
     * Produce an internationalized trace message.
     *
     * @param key the resource key for the message format
     * @param arg the first argument to insert
     * @param arg2 the second argument to insert
     */
    private void trace(final String key, final String arg, final String arg2) {

        logger.fine(MessageFormat.format(bundle.getString(key), //
                new Object[]{arg, arg2}));
    }

}
