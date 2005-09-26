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

package de.dante.extex.interpreter.interaction;

import de.dante.util.exception.GeneralException;

/**
 * This interface describes a visitor for interaction modes.
 * With the help of this interface the visitor pattern can be implemented.
 * <p>
 * The user of the visitor pattern has to provide an implementation of
 * this interface. Then the <tt>visit</tt> method is invoked and the caller
 * is forwarded to the appropriate <tt>visit</tt> method in the visitor.
 * </p>
 * <p>
 * Consider we have a mode constant at hand which is in fact a batchmode and
 * we invokes
 * <pre>
 *   boolean boo = mode.visit(visitor, a, b, c);
 * </pre>
 * then the following method in the object <tt>visitor</tt> is invoked:
 * <pre>
 *   visitBatchmode(visitor, a, b, c)
 * </pre>
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface InteractionVisitor {

    /**
     * Invoke the method in case of a batchmode interaction.
     *
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @param arg3 the third argument
     *
     * @return a boolean indicator
     *
     * @throws GeneralException in case of an error
     */
    boolean visitBatchmode(Object arg1, Object arg2, Object arg3)
            throws GeneralException;

    /**
     * Invoke the method in case of a non-stop mode interaction.
     *
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @param arg3 the third argument
     *
     * @return a boolean indicator
     *
     * @throws GeneralException in case of an error
     */
    boolean visitNonstopmode(Object arg1, Object arg2, Object arg3)
            throws GeneralException;

    /**
     * Invoke the method in case of a scroll mode interaction.
     *
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @param arg3 the third argument
     *
     * @return a boolean indicator
     *
     * @throws GeneralException in case of an error
     */
    boolean visitScrollmode(Object arg1, Object arg2, Object arg3)
            throws GeneralException;

    /**
     * Invoke the method in case of a error-stop mode interaction.
     *
     * @param arg1 the first argument
     * @param arg2 the second argument
     * @param arg3 the third argument
     *
     * @return a boolean indicator
     *
     * @throws GeneralException in case of an error
     */
    boolean visitErrorstopmode(Object arg1, Object arg2, Object arg3)
            throws GeneralException;

}