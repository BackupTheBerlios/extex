/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.CodeToken;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;
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
 *  &lt;/cfg&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class PrimitiveFactory extends AbstractFactory {

    /**
     * The constant <tt>NAME_ATTRIBUTE</tt> contains the name of the attribute
     * holding the name of the primitive to define.
     */
    private static final String NAME_ATTRIBUTE = "name";

    /**
     * Creates a new object.
     */
    public PrimitiveFactory() {

        super();
    }

    /**
     * Scan a configuration and define the primitives found.
     *
     * @param configuration the configuration to scan
     * @param tokenFactory the token factory to use
     * @param context the interpreter context to register the primitive in
     * @param outputLogger the logger to produce output to
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
            final Logger outputLogger)
            throws GeneralException,
                ConfigurationException {

        Iterator iterator = configuration.iterator("define");

        while (iterator.hasNext()) {
            Configuration cfg = (Configuration) iterator.next();

            String name = cfg.getAttribute(NAME_ATTRIBUTE);
            Code code = (Code) createInstanceForConfiguration(cfg, Code.class,
                    name);

            context.setCode((CodeToken) tokenFactory.createToken(
                    Catcode.ESCAPE, name, Namespace.DEFAULT_NAMESPACE), code,
                    true);
            if (code instanceof LogEnabled) {
                ((LogEnabled) code).enableLogging(outputLogger);
            }
            if (code instanceof InitializableCode) {
                ((InitializableCode) code).init(context, cfg.getValue());
            }
        }
    }
}