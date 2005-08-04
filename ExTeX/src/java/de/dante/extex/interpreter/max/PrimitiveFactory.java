/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max;

import java.util.Iterator;
import java.util.logging.Logger;

import de.dante.extex.documentWriter.OutputStreamFactory;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.interpreter.type.OutputStreamConsumer;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This is a factory to deliver primitives from a configuration.
 *
 * <pre>
 *  &lt;cfg&gt;
 *    &lt;define name="<i>name</i>" class="<i>class</i>"/&gt;
 *    &lt;define name="<i>name</i>" class="<i>class</i>"&gt;<i>value</i>&lt;/define&gt;
 *    &lt;define name="<i>name</i>" class="<i>class</i>"/&gt;
 *  &lt;/cfg&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.10 $
 */
public class PrimitiveFactory extends AbstractFactory {

    /**
     * The constant <tt>NAME_ATTRIBUTE</tt> contains the name of the attribute
     * holding the name of the primitive to define.
     */
    private static final String NAME_ATTRIBUTE = "name";

    /**
     * The field <tt>factory</tt> contains the ...
     */
    private TokenStreamFactory factory;

    /**
     * Creates a new object.
     *
     * @param factory the factory for token streams
     */
    public PrimitiveFactory(final TokenStreamFactory factory) {

        super();
        this.factory = factory;
    }

    /**
     * Scan a configuration and define the primitives found.
     * @param configuration the configuration to scan
     * @param tokenFactory the token factory to use
     * @param context the interpreter context to register the primitive in
     * @param outputLogger the logger to produce output to
     * @param outputFactory TODO
     *
     * @throws GeneralException In case of an error
     * @throws ConfigurationException in case of an error
     * <ul>
     *  <li>ConfigurationMissingAttributeException
     *    in case of a missing argument</li>
     *  <li>ConfigurationInstantiationException
     *    in case of an error during instantiation</li>
     *  <li>ConfigurationClassNotFoundException
     *    in case of a missing class</li>
     *  <li>ConfigurationWrapperException
     *    in case of another error which is wrapped</li>
     * </ul>
     */
    public void define(final Configuration configuration,
            final TokenFactory tokenFactory, final Context context,
            final Logger outputLogger, final OutputStreamFactory outputFactory)
            throws GeneralException,
                ConfigurationException {

        Iterator iterator = configuration.iterator("define");
        Typesetter typesetter = null; //gene: it might be ok to have none

        while (iterator.hasNext()) {
            Configuration cfg = (Configuration) iterator.next();

            String name = cfg.getAttribute(NAME_ATTRIBUTE);
            Code code = (Code) createInstanceForConfiguration(cfg, Code.class,
                    name);

            String namespace = cfg.getAttribute("namespace");
            if (namespace == null) {
                namespace = Namespace.DEFAULT_NAMESPACE;
            }

            context.setCode((CodeToken) tokenFactory.createToken(
                    Catcode.ESCAPE, new UnicodeChar('\\'), name, namespace),
                    code, true);
            if (code instanceof LogEnabled) {
                ((LogEnabled) code).enableLogging(outputLogger);
            }
            if (code instanceof InitializableCode) {

                String value = cfg.getValue();
                StringSource stringSource = new StringSource(value);
                ((InitializableCode) code).init(context, stringSource,
                        typesetter);
            }
            if (code instanceof OutputStreamConsumer) {
                ((OutputStreamConsumer) code)
                        .setOutputStreamFactory(outputFactory);
            }
        }
    }
}