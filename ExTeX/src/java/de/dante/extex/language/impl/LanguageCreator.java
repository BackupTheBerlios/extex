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

package de.dante.extex.language.impl;

import de.dante.extex.language.Language;
import de.dante.extex.language.hyphenation.exception.HyphenationException;

/**
 * This interface describes the features of an object which is able to provide
 * a language instance either by loading it from an external source or by
 * creating a new one.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface LanguageCreator {

    /**
     * Create a new instance for the given index if required.
     *
     * @param index the symbolic name of the language
     *
     * @return the new instance
     *
     * @throws HyphenationException in case of an error
     */
    Language createLanguageInstance(String index) throws HyphenationException;

    /**
     * Load or create a new instance for the given index if required.
     *
     * @param index the symbolic name of the language
     *
     * @return the new instance
     *
     * @throws HyphenationException in case of an error
     */
    Language loadLanguageInstance(String index) throws HyphenationException;

}