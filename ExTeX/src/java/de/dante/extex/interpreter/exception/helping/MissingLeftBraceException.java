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

import de.dante.extex.i18n.HelpingException;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This exception is raised when an arithmetic overflow has been encoutered.
 * <p>
 *  The localization format is taken from the Localizer under the key
 *  <tt>TTP.ArithmeticOverflow</tt>.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MissingLeftBraceException extends HelpingException {

    /**
     * Creates a new object.
     *
     * @param macro the name of the macro in which the eof has been encoutered
     */
    public MissingLeftBraceException(final String macro) {

        super(LocalizerFactory.getLocalizer(//
                MissingLeftBraceException.class.getName()),
                "TTP.MissingLeftBrace");
    }

}