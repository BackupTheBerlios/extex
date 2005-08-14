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

package de.dante.extex.interpreter.primitives.dynamic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.configuration.Configuration;
import de.dante.util.framework.configuration.exception.ConfigurationException;
import de.dante.util.framework.logger.LogEnabled;

/**
 * This primitive provides a binding of a macro or active character to
 * code in some programming language.
 * This code implements the primitive <tt>\nativedef</tt>.
 *
 * <doc name="nativedef">
 * <h3>The Primitive <tt>\nativedef</tt></h3>
 * <p>
 * The primitive <tt>\nativedef</tt> assigns a definition to a macro or
 * active character. This is done in a similar way as <tt>\def</tt>
 * works. The difference is that the definition has to be provided in
 * form of a Java class which glues in native code.
 * </p>
 *
 * <h4>Syntax</h4>
 * The general form of this primitive is
 * <pre class="syntax">
 *   &lang;nativedef&rang;
 *       &rarr; <tt>\nativedef</tt> {@linkplain
 *       de.dante.extex.interpreter.TokenSource#getControlSequence(Context)
 *       &lang;control sequence&rang;} <i>&lang;name&rang;</i> </pre>
 * <p>
 * The <i>&lang;control sequence&rang;</i> is any macro or active
 * character. If this token is missing or of the wrong type then an
 * error is raised.
 * </p>
 * <p>
 * The <i>&lang;name&rang;</i> is any specification of a list of
 * tokens like a constant list enclosed in braces or a token register.
 * The value of these tokens are taken and resolved via the configuration.
 * This appropriate class is loaded if needed and instantiated. The
 * instance is bound as code to the <i>&lang;control sequence&rang;</i>.
 * </p>
 * <p>
 * The primitive <tt>\javadef</tt> is local to the enclosing group as
 * is <tt>\def</tt>. And similar to <tt>\def</tt> the modifier
 * <tt>\global</tt> can be used to make the definition in all groups
 * instead of the current group only.
 * </p>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class NativeDef extends AbstractCode
        implements
            Configurable,
            LogEnabled {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * This inner class provides access to the functionality of an abstract
     * factory. It is here to overcome the deficiency of a missing multiple
     * inheritance in Java.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.5 $
     */
    protected class Factory extends AbstractFactory {

        /**
         * Create a new instance of the class given by the attribute
         * <tt>class</tt> of the configuration.
         *
         * @return the Code loaded
         * @throws ConfigurationException in case of an error
         */
        public Definer createLoad() throws ConfigurationException {

            return (Definer) createInstanceForConfiguration(getConfiguration(),
                    Definer.class);
        }
    }

    /**
     * The field <tt>logger</tt> contains the logger to use.
     */
    private Logger logger = null;

    /**
     * The field <tt>map</tt> contains the mapping from a symbolic name to a
     * configuration.
     */
    private Map map = new HashMap();

    /**
     * Creates a new object.
     *
     * @param codeName the name of the primitive
     */
    public NativeDef(final String codeName) {

        super(codeName);
    }

    /**
     * @see de.dante.util.framework.configuration.Configurable#configure(
     *      de.dante.util.configuration.Configuration)
     */
    public void configure(final Configuration config)
            throws ConfigurationException {

        Iterator iterator = config.iterator("load");
        while (iterator.hasNext()) {
            Configuration cfg = (Configuration) iterator.next();
            map.put(cfg.getAttribute("name"), cfg);
        }
    }

    /**
     * @see de.dante.util.framework.logger.LogEnabled#enableLogging(
     *      java.util.logging.Logger)
     */
    public void enableLogging(final Logger theLogger) {

        this.logger = theLogger;
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *       de.dante.extex.interpreter.Flags,
     *       de.dante.extex.interpreter.context.Context,
     *       de.dante.extex.interpreter.TokenSource,
     *       de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        String name = source.getTokens(context).toText();
        Configuration cfg = (Configuration) map.get(name);
        if (cfg == null) {
            throw new InterpreterException(getLocalizer().format("UnknownType",
                    name, getName()));
        }

        Factory factory = new Factory();
        factory.enableLogging(logger);
        try {
            factory.configure(cfg);
            factory.createLoad().define(prefix, context, source);
        } catch (ConfigurationException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * Getter for logger.
     *
     * @return the logger
     */
    protected Logger getLogger() {

        return this.logger;
    }
}