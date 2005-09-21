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
package de.dante.extex.interpreter.primitives.dynamic.java;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;


/**
 * This interface describes the initialization method which is invoked from
 * {@link JavaLoad JavaLoad} after the requested class has been instantiated.
 * This interface can be used to write a class that performs many
 * initializations after it has been loaded dynamically.
 *
 * <p>
 *  Note that no provisions are made to avoid that a class is loaded more than
 *  once.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public interface Loadable {

    /**
     * Perform any initializations desirable after the component has been
     * loaded.
     *
     * @param context the processor context
     * @param typesetter the current typesetter
     *
     * @throws GeneralException in case of an error
     */
    void init(Context context, Typesetter typesetter) throws GeneralException;
}
