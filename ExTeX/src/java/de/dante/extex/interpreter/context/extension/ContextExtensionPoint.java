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

package de.dante.extex.interpreter.context.extension;

import de.dante.extex.interpreter.exception.InterpreterException;

/**
 * This class provides an extension mechanism for contexts.
 * <p>
 *  The general idea is that the extensions can be triggered from several points
 *  in the life cycle of <logo>ExTeX</logo>. The simplest case the extension
 *  is requested during the registration of a primitive. Here the implementation
 *  has the possibility to implement the interface
 *  {@link de.dante.extex.interpreter.type.InitializableCode InitializableCode}.
 *  Here it is possible to acquire an extension point for a given class.
 * </p>
 * TODO gene: missing JavaDoc (incomplete).
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public interface ContextExtensionPoint {

    /**
     * Getter for the extension point for a certain class.
     * The extension point is a instance of the index class. This instance is
     * kept and delivered whenever the extension is requested. If the
     * extension point has not been requested before then a new instance of the
     * given class is created, registered, and returned.
     *
     * @param c the class to be used as reference for the extension. This class
     *  needs to implement the interface {@link ExtensionPoint ExtensionPoint}.
     *
     * @return the extension; either an existing or a new one.
     */
    ExtensionPoint getExtension(Class c) throws InterpreterException;
}
