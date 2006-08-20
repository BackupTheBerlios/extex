/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.max.util;

import java.util.Iterator;
import java.util.logging.Logger;

import de.dante.extex.backend.documentWriter.OutputStreamFactory;
import de.dante.extex.interpreter.Namespace;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.max.StringSource;
import de.dante.extex.interpreter.primitives.dynamic.util.LoaderFactory;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.InitializableCode;
import de.dante.extex.interpreter.type.OutputStreamConsumer;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;

/**
 * This is a factory to units from a configuration.
 * A unit is a configuration consisting of an optional setup class and a set of
 * primitives. When the unit is loaded the setup class is instantiated and run.
 * Then the primitives are created and registered in the context.
 *
 * <pre>
 *  &lt;unit&gt;
 *    &lt;setup class="the.setup.Class"/&gt;
 *    &lt;primitive&gt;
 *      &lt;define name="<i>name</i>" class="<i>class</i>"/&gt;
 *      &lt;define name="<i>name</i>" class="<i>class</i>"&gt;<i>value</i>&lt;/define&gt;
 *      &lt;define name="<i>name</i>" class="<i>class</i>"/&gt;
 *    &lt;/primitive&gt;
 *  &lt;/unit&gt;
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public final class LoadUnit extends AbstractFactory {

    /**
     * The field <tt>DEFINE_TAG</tt> contains the tag name used to find
     * definitions for primitives.
     */
    private static final String DEFINE_TAG = "define";

    /**
     * The constant <tt>NAME_ATTRIBUTE</tt> contains the name of the attribute
     * holding the name of the primitive to define.
     */
    private static final String NAME_ATTRIBUTE = "name";

    /**
     * The field <tt>NAMESPACE_ATTRIBUTE</tt> contains the attribute name
     * to find the name space for the new primitive.
     */
    private static final String NAMESPACE_ATTRIBUTE = "namespace";

    /**
     * Prepare the primitives according to their configuration. The given
     * configuration may contain sub-configurations with the name
     * <tt>primitives</tt> which includes the definition of primitives. Those
     * primitives are defined no matter if they are already defined or not.
     *
     * @param configuration the configuration
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param logger the logger to use
     * @param outputFactory the output stream factory
     *
     * @throws ConfigurationException in case of a configuration error
     * @throws GeneralException in case of an error
     */
    public static void loadUnit(final Configuration configuration,
            final Context context, final TokenSource source,
            final Typesetter typesetter, final Logger logger,
            final OutputStreamFactory outputFactory)
            throws ConfigurationException,
                GeneralException {

        TokenFactory tokenFactory = context.getTokenFactory();
        LoadUnit primitiveFactory = new LoadUnit();

        Configuration setup = configuration.findConfiguration("setup");
        if (setup != null) {
            LoaderFactory factory = new LoaderFactory();
            factory.enableLogging(logger);
            factory.configure(setup);
            factory.createLoad().load(context, source, typesetter);
        }

        Iterator iterator = configuration.iterator("primitives");
        while (iterator.hasNext()) {
            primitiveFactory.define((Configuration) iterator.next(),
                    tokenFactory, context, typesetter, logger, outputFactory);
        }

        iterator = configuration.iterator("import");
        while (iterator.hasNext()) {
            Configuration x = (Configuration) iterator.next();
            x.getAttribute("namespace");
            //TODO gene: do import
        }

        Configuration start = configuration.findConfiguration("start");
        if (start != null) {
            LoaderFactory factory = new LoaderFactory();
            factory.enableLogging(logger);
            factory.configure(start);
            factory.createLoad().load(context, source, typesetter);
        }
    }

    /**
     * The field <tt>stringSource</tt> contains the reused object for string
     * parsing.
     */
    private StringSource stringSource = new StringSource();

    /**
     * Creates a new object.
     */
    private LoadUnit() {

        super();
    };

    /**
     * Scan a configuration and define the primitives found.
     *
     * @param configuration the configuration to scan
     * @param tokenFactory the token factory to use
     * @param context the interpreter context to register the primitive in
     * @param typesetter the typesetter
     * @param outputLogger the logger to produce output to
     * @param outputFactory the factory for new output streams
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
            final Typesetter typesetter, final Logger outputLogger,
            final OutputStreamFactory outputFactory)
            throws GeneralException,
                ConfigurationException {

        enableLogging(outputLogger);
        UnicodeChar esc = UnicodeChar.get('\\');
        Iterator iterator = configuration.iterator(DEFINE_TAG);

        while (iterator.hasNext()) {
            Configuration cfg = (Configuration) iterator.next();

            String name = cfg.getAttribute(NAME_ATTRIBUTE);
            Code code = (Code) createInstanceForConfiguration(cfg, Code.class,
                    name);

            String namespace = cfg.getAttribute(NAMESPACE_ATTRIBUTE);
            if (namespace == null) {
                namespace = Namespace.DEFAULT_NAMESPACE;
            }

            context.setCode((CodeToken) tokenFactory.createToken(
                    Catcode.ESCAPE, esc, name, namespace), code, true);
            if (code instanceof InitializableCode) {

                stringSource.reset(cfg.getValue());
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
