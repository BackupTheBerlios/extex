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

/**
 * This class provides a means to reconnect an object to a managing factory
 * after it has been disconnected. The disconnection might happen during
 * serialization and deserialization.
 *
 * <p>
 *  Whenever an object is deserialized Java tries to invoke the method
 *  {@link readResolve() readResolve()}. This method can be used to get a hand
 *  on the object which has just been reconstructed. Here the object can be
 *  replaced by another one or some other action can be applied.
 * </p>
 * <p>
 *  Any class which is serializable and wants to participate in the reconnection
 *  mechanism should implement the method <code>readResolve</code>. In this
 *  method the method <code>reconnect()</code> of the
 *  {@link Registrar Registrar} should be invoked. This is shown in the
 *  following example:
 * </p>
 *
 * <pre class="JavaSample">
 *  <b>protected</b> Object readResolve() <b>throws</b> ObjectStreamException {
 *
 *      <b>return</b> Registrar.reconnect(this);
 *  }
 * </pre>
 *
 * <p>
 *  Any factory which  wants to participate in the reconnection mechanism should
 *  implement the interface
 *  {@link de.dante.util.framework.RegistrarObserver RegistrarObserver}.
 * </p>
 * <p>
 *  Finally, before an object is deserialized, the interested parties should
 *  register an observer at the {@link Registrar Registrar}.
 * </p>
 * </p>
 *  Note that the registrar has to be implemented as a static singleton since
 *  readResolve() does not provide any means to pass a reference to some other
 *  object to it.
 * <p>
 *
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public final class Registrar {

    /**
     * This class provides a container for a pair of a class and an observer.
     *
     * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
     * @version $Revision: 1.3 $
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
     * The field <tt>observers</tt> contains the observers which are currently
     * registered.
     */
    private static List observers = new ArrayList();

    /**
     * This method registers an observer at the registrar. This observer is
     * invoked for each class which is deserialized and matches the class given.
     * The type argument can be an interface as well.
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
     * @return the object which should actually be used
     *
     * @throws RegistrarException in case of a problem with registration
     */
    public static Object reconnect(final Object object)
            throws RegistrarException {

        Object ob = object;
        int n = observers.size();
        for (int i = 0; i < n; i++) {
            Obs obs = (Obs) observers.get(i);
            if (obs.getType().isInstance(object)) {
                ob = obs.getObserver().reconnect(ob);
            }
        }
        return ob;
    }

    /**
     * Reset the list all observers which are registered.
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
