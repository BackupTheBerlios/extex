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

package de.dante.extex.interpreter.exception.helping;

import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This exception is raised when a illegal file reference has been encoutered.
 * <p>
 *  The localization format is taken from the Localizer under the key
 *  <tt>TTP.BadFileNumber</tt>.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class BadFileNumberException extends HelpingException {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param value the illegal file reference
     * @param min the minimum for numerical values
     * @param max the maximum for numerical values
     */
    public BadFileNumberException(final String value, final String min,
            final String max) {

        super(LocalizerFactory.getLocalizer(//
                BadFileNumberException.class.getName()), "TTP.BadFileNumber",
                value, min, max);
    }

}