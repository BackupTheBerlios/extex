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

package de.dante.extex.language.word;

import java.util.List;

import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.typesetter.type.NodeList;

/**
 * This interface describes the contract for a tokenizer which is abke to split
 * a list of nodes into words.
 * This kind of tokenizer might be language specific.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface WordTokenizer {

    /**
     * Analyze a node list to find the start of the next word after a given
     * position in the list.
     *
     * @param list the list of nodes to analyze
     * @param start the starting position in the list
     *
     * @return the index of the first node of the word or an index past
     *  the end of the node list if no word follows
     *
     * @throws HyphenationException in case of an error
     */
    //int skipNonWord(NodeList list, int start) throws HyphenationException;
    /**
     * Analyze a list of nodes and find the end of the word starting at a
     * given position.
     *
     * @param list the list of nodes to analyze
     * @param start the starting position in the list
     *
     * @return the index of the first non-word node or the smallest index past
     *  the end of the node list if the word is at the end of the list
     *
     * @throws HyphenationException in case of an error
     */
    //int skipWord(NodeList list, int start) throws HyphenationException;

    /**
     * Extract a word from a node list.
     *
     * @param nodes the nodes to extract the word from
     * @param start the start index
     * @param word the target list for the letters of the word
     *
     * @return the index of the first node beyond the word
     *
     * @throws HyphenationException in case of an error
     */
    int findWord(NodeList nodes, int start, List word)
            throws HyphenationException;
}
