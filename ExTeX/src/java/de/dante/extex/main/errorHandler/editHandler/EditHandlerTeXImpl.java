/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.main.errorHandler.editHandler;

import de.dante.util.Locator;
import de.dante.util.framework.i18n.Localizer;

/**
 * This is a dummy implementation for an EditHandler which just prints the
 * location to the error stream.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class EditHandlerTeXImpl implements EditHandler {

    /**
     * Creates a new object.
     */
    public EditHandlerTeXImpl() {

        super();
    }

    /**
     * @see de.dante.extex.main.errorHandler.editHandler.EditHandler#edit(
     *      de.dante.util.framework.i18n.Localizer,
     *      de.dante.util.Locator)
     */
    public boolean edit(final Localizer localizer, final Locator locator) {

        System.err.println(localizer.format("EditHandler.edit", locator
                .getFilename(), Integer.toString(locator.getLineno())));
        return false;
    }
}