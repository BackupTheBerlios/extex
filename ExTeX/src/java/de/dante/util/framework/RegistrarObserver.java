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


/**
 * This interface describes an observer which is meant to reconnect a object
 * which has just been reconstructed.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface RegistrarObserver {

    /**
     * Reconnect an object.
     * It should return the object which should actually be used. This is
     * normally the object which is passed in as argument. Nevertheless the
     * as a side effect the object can be attached to an internal list in a
     * factory or augmented with additional information by invoking some of its
     * methods.
     *
     * @param object the object to reconnect
     *
     * @return the object to be actually used
     *
     * @throws RegistrarException in case of an error during configuration
     */
    Object reconnect(Object object) throws RegistrarException;
}
