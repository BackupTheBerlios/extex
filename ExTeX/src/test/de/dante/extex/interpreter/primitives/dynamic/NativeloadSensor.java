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

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.primitives.dynamic.java.Loadable;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class NativeloadSensor implements Loadable {

    /**
     * The field <tt>kilroy</tt> contains the ...
     */
    private static boolean kilroy = false;

    /**
     * Getter for kilroy.
     *
     * @return the kilroy
     */
    public static boolean isKilroy() {

        return kilroy;
    }

    /**
     * Setter for kilroy.
     *
     * @param kilroy the kilroy to set
     */
    public static void setKilroy(boolean kilroy) {

        NativeloadSensor.kilroy = kilroy;
    }

    /**
     * Creates a new object.
     *
     */
    public NativeloadSensor() {

        super();
        kilroy = false;
    }

    /**
     * @see de.dante.extex.interpreter.primitives.dynamic.java.Loadable#init(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void init(final Context context, final Typesetter typesetter)
            throws GeneralException {

        kilroy = true;
    }

}
