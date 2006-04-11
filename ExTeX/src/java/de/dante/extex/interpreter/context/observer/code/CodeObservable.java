/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.context.observer.code;

import de.dante.extex.scanner.type.token.Token;

/**
 * This interface describes the possibility to register an observer for an
 * code change event.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface CodeObservable {

    /**
     * Register an observer for code change events.
     * Code change events are triggered when the assignment of a macro or
     * active character changes. In this case the appropriate method in the
     * observer is invoked.
     *
     * @param token the token to be observed. This should be a macro or
     * active character token.
     * @param observer the observer to receive the events
     */
    void registerCodeChangeObserver(Token token, CodeObserver observer);

    /**
     * Remove a registered observer for code change events.
     * Code change events are triggered when the assignment of a macro or
     * active character changes. In this case the appropriate method in the
     * observer is invoked.
     *
     * @param name the token to be observed. This should be a macro or
     * active character token.
     * @param observer the observer to receive the events
     */
    void unregisterCodeChangeObserver(Token name, CodeObserver observer);

}
