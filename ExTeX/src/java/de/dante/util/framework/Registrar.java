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

package de.dante.util.framework;

import java.util.ArrayList;
import java.util.List;

import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides a means to reconnect an object to a managing factory
 * after it has been disconnected. The disconnection might happen during
 * serialization and deserialization.
 *
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public final class Registrar {

    /**
     * This class provides a container for a pair of a class and an observer.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.1 $
     */
    private static final class Obs {

        /**
         * The field <tt>type</tt> contains the class.
         */
        private Class type;

        /**
         * The field <tt>observer</tt> contains the observer.
         */
        private RegistrarObserver observer;

        /**
         * Creates a new object.
         *
         * @param observer the observer
         * @param type the interface or class to be observed
         */
        public Obs(final RegistrarObserver observer, final Class type) {

            super();
            this.type = type;
            this.observer = observer;
        }

        /**
         * Getter for observer.
         *
         * @return the observer
         */
        public RegistrarObserver getObserver() {

            return this.observer;
        }

        /**
         * Getter for type.
         *
         * @return the type
         */
        public Class getType() {

            return this.type;
        }
    }

    /**
     * The field <tt>observers</tt> contains the ...
     */
    private static List observers = new ArrayList();

    /**
     * TODO gene: missing JavaDoc
     *
     * @param observer the observer
     * @param type the interface or class to be observed
     */
    public static void register(final RegistrarObserver observer,
            final Class type) {

        observers.add(new Obs(observer, type));
    }

    /**
     * Find anyone interested in an object and let the object be integrated into
     * their views of the world.
     *
     * @param object the object to reconnect
     *
     * @throws ConfigurationException in case of a problem during configuration
     * @throws RegistrarException in case of a problem with registration
     */
    public static void reconnect(final Object object)
            throws RegistrarException,
                ConfigurationException {

        int n = observers.size();
        for (int i = 0; i < n; i++) {
            Obs obs = (Obs) observers.get(i);
            if (obs.getType().isInstance(object)) {
                obs.getObserver().reconnect(object);
            }
        }
    }

    /**
     * Reset all information which might be
     */
    public static void reset() {

        observers = new ArrayList();
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private Registrar() {

    }
}
