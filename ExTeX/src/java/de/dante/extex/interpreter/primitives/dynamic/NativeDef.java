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
import de.dante.util.configuration.Configuration;
import de.dante.util.configuration.ConfigurationException;
import de.dante.util.framework.AbstractFactory;
import de.dante.util.framework.configuration.Configurable;
import de.dante.util.framework.logger.LogEnabled;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class NativeDef extends AbstractCode
        implements
            Configurable,
            LogEnabled {

    /**
     * This inner class provides access to the functionality of an abstract
     * factory. It is here to overcome the deficiency of a missing multiple
     * inheritance in Java.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
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
     * The field <tt>logger</tt> contains the ...
     */
    private Logger logger = null;

    /**
     * The field <tt>map</tt> contains the ...
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
    public void enableLogging(final Logger logger) {

        this.logger = logger;
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