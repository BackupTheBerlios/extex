/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.language.ligature;

import java.io.Serializable;

import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.util.UnicodeChar;

/**
 * This interface describes the capability of a ligature builder.
 * The ligature builder inserts kerning nodes and constructs ligature nodes.
 * It might use the information from the font.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public interface LigatureBuilder extends Serializable {

    /**
     * Take a node list and transform character sequences into ligatures where
     * appropriate. The processing should extend over all characters with the
     * same font and non-character nodes. It should return the control to the
     * caller as soon as a character node with another font is found.
     *
     * @param list the node list to create ligatures for
     * @param start the index in the list to start processing
     *
     * @return the index after last node processed
     *
     * @throws HyphenationException in case of an error
     */
    int insertLigatures(NodeList list, int start) throws HyphenationException;

    /**
     * Get a single ligature of to characters.
     *
     * @param c1 the first character
     * @param c2 the second character
     * @param f the current font
     *
     * @return the ligature of c1 and c2 or <code>null</code> if none exists
     *
     * @throws HyphenationException in case of an error
     */
    UnicodeChar getLigature(UnicodeChar c1, UnicodeChar c2, Font f)
            throws HyphenationException;

}
