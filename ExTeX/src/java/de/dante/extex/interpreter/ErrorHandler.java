/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import de.dante.extex.main.errorHandler.editHandler.EditHandler;
import de.dante.extex.scanner.Token;
import de.dante.util.GeneralException;

/**
 * This interface defines the capabilities of an error handler. The error
 * handler is invoked in the interpreter when an exception is caught. The error
 * handler can be used to clear things up such that the processing can be
 * continued.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public interface ErrorHandler {

    /**
     * This is the error handling callback.
     *
     * @param e the exception which has led to the invokation
     * @param token the token leading to the error
     * @param source the token source
     * @param context the processor context
     *
     * @return <code>true</code> iff the processing can continue
     *
     * @throws GeneralException in case of a problem
     */
    boolean handleError(GeneralException e, Token token, TokenSource source,
            Context context) throws GeneralException;

    /**
     * ...
     *
     * @param editHandler ...
     */
    void setEditHandler(EditHandler editHandler);
}
