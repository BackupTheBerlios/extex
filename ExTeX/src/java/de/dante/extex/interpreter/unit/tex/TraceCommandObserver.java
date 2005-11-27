/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.unit.tex;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.observer.command.CommandObserver;
import de.dante.extex.interpreter.type.PrefixCode;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;

/**
 * Observer for tracing the execution of tokens. The token is written to the
 * log file enclosed in braces.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class TraceCommandObserver implements CommandObserver {

    /**
     * The field <tt>logger</tt> contains the logger for output
     */
    private Logger logger;

    /**
     * The field <tt>context</tt> contains the interpreter context.
     */
    private Context context;

    /**
     * The field <tt>prefix</tt> contains the indicator that the last token
     * encountered has been a prefix primitive. This is used to suppress the
     * following trace output in <logo>TeX</logo> compatibility mode.
     */
    private boolean prefix = false;

    /**
     * Creates a new object.
     *
     * @param theLogger the logger for potential output
     * @param context the interpreter context for access to
     *  <tt>\tracingonline</tt>
     */
    public TraceCommandObserver(final Logger theLogger, final Context context) {

        super();
        this.logger = theLogger;
        this.context = context;
    }

    /**
     * This method is meant to be invoked just before a token is executed.
     * A token following a prefix code is ignored if \tracingcommands is
     *
     * @param token the token to be expanded
     */
    public void update(final Token token) {

        long tracing = context.getCount("tracingcommands").getValue();

        if (tracing <= 0) {
            return;
        }

        if (!prefix) {
            long online = context.getCount("tracingonline").getValue();
            logger.log((online > 0 ? Level.INFO : Level.FINE), //
                    "{" + token.toText() + "}\n");
        }

        try {
            prefix = (tracing > 2 && //
                    token instanceof CodeToken && //
            context.getCode((CodeToken) token) instanceof PrefixCode);
        } catch (InterpreterException e) {
            logger.warning(e.getMessage());
        }
    }
}
