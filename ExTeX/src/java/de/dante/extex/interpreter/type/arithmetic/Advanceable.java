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
package de.dante.extex.interpreter.type.arithmetic;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.typesetter.Typesetter;

/**
 * This is a interface to mark those Classes which are able to advance
 * something.
 *
 *
 * <doc type="howto" name="advance">
 * <h3>Extending <tt>\advance</tt></h3>
 * <p>
 *  The primitive <tt>\advance</tt> is designed to be expanded. It is fairly
 *  simple to write an advancable primitive. The associated code simply has to
 *  implement the interface <tt>Advanceable</tt>. Whenever <tt>\advance</tt>
 *  is encountered immediately followed by a token which has the proper code
 *  associated, the method <tt>advance</tt> is invoked. It is up to this
 *  method to gather further arguments and perform the functionality.
 * </p>
 * <p>
 *  With this interface the functionality is in fact tied to the implementing
 *  code and not to the primitive <tt>\advance</tt>. Each primitive can be
 *  made aware for advancing without touchíng the code for
 *  <tt>\advance</tt>.
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public interface Advanceable {
    /**
     * This method is called when the macro <tt>\advance</tt> has been seen.
     * It performs the remaining tasks for the expansion.
     *
     * @param prefix the prefix for the command
     * @param context the processor context
     * @param source the token source to parse
     *
     * @throws InterpreterException in case of an error
     */
    void advance(Flags prefix, Context context, TokenSource source, Typesetter typesetter)
            throws InterpreterException;
}
