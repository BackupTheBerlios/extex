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

package de.dante.extex.interpreter.primitives.file;

import de.dante.extex.i18n.BadFileNumberHelpingException;
import de.dante.extex.interpreter.type.file.InFile;
import de.dante.extex.interpreter.type.file.OutFile;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class FileKeyValidator {

    /**
     * ...
     *
     * @param no ...
     * @param key ...
     *
     * @throws BadFileNumberHelpingException in case of a failure
     */
    public static final void validateInFile(final long no, final String key)
            throws BadFileNumberHelpingException {

        if (no < 0 || no > InFile.MAX_FILE_NO) {
            throw new BadFileNumberHelpingException(key, //
                    "0", Integer.toString(InFile.MAX_FILE_NO));
        }
    }

    /**
     * ...
     *
     * @param no ...
     * @param key ...
     *
     * @throws BadFileNumberHelpingException in case of a failure
     */
    public static final void validateOutFile(final long no, final String key)
            throws BadFileNumberHelpingException {

        if (no < 0 || no > OutFile.MAX_FILE_NO) {
            throw new BadFileNumberHelpingException(key, //
                    "0", Integer.toString(OutFile.MAX_FILE_NO));
        }
    }
}