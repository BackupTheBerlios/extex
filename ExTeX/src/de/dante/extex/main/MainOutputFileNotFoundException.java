/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.main;

import de.dante.extex.i18n.Messages;

/**
 * This exception is thrown when the main program tries to open an output file
 * and is not able to perform this operation.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class MainOutputFileNotFoundException extends MainException {
    /**
     * Creates a new object.
     *
     * @param filename the name of the file to open
     */
    public MainOutputFileNotFoundException(final String filename) {
        super(-15, filename);
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return Messages.format("MainOutputFileNotFoundException.Message",
                               super.getMessage());
    }
}
