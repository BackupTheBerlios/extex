/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.interpreter;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This is the interface for all expandable or executable classes.
 *
 * <p>
 * Each primitive has a name which is used for debugging purposes. Since an
 * arbitrary sequence of \let and \def operations might have taken place it is
 * in general not possible to determine the current name under which the
 * primitive has been called. Thus an initial value is stored in it for the
 * purpose.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.7 $
 */
public interface Code {

    /**
     * This simple little method distinguishes the conditionals from the other
     * primitives. This is necessary for the processing of all \if* primitives.
     *
     * @return <code>true</code> iff this is some sort if \if
     */
    boolean isIf();

    /**
     * Setter for the name of this primitive.
     *
     * @param name the name
     */
    void setName(String name);

    /**
     * Getter for the name.
     *
     * @return the name
     */
    String getName();

    /**
     * This method takes the first token and executes it. The result is placed
     * on the stack. This operation might have side effects. To execute a token
     * it might be necessary to consume further tokens.
     * <p>
     * For expandable primitives the execution is identical to expansion.
     * </p>
     *
     * @param prefix the prefix controlling the execution
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @throws GeneralException in case of an error
     */
    void execute(Flags prefix, Context context,
            TokenSource source, Typesetter typesetter) throws GeneralException;

    /**
     * ...
     *
     * @param context the interpreter context
     * @param value ...
     *
     * @throws GeneralException in case of an error
     */
    void set(Context context, String value)
            throws GeneralException;
}
