/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This is the interface for all expandable or executable classes.
 *
 * <p>
 * Each primitive has a name which is used for debugging purposes. Since an
 * arbitrary sequence of <tt>\let</tt> and <tt>\def</tt> operations might have
 * taken place it is in general not possible to determine the current name
 * under which the primitive has been called. Thus an initial value is stored
 * in it for this purpose.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public interface Code {

    /**
     * This simple little method distinguishes the conditionals from the other
     * primitives. This is necessary for the processing of all \if* primitives.
     *
     * @return <code>true</code> iff this is some sort if <tt>\if</tt>.
     */
    boolean isIf();

    /**
     * Getter for the outer flag.
     *
     * @return <code>true</code> iff the code is defined outer.
     */
    boolean isOuter();

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
     *
     * @param prefix the prefix controlling the execution
     * @param context the interpreter context
     * @param source the token source
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    void execute(Flags prefix, Context context, TokenSource source,
            Typesetter typesetter) throws InterpreterException;

}