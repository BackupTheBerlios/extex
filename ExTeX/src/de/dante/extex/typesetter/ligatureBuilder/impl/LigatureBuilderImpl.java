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

package de.dante.extex.typesetter.ligatureBuilder.impl;

import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.font.Glyph;
import de.dante.extex.interpreter.type.node.CharNode;
import de.dante.extex.interpreter.type.node.LigatureNode;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;
import de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder;
import de.dante.util.UnicodeChar;
import de.dante.util.configuration.Configuration;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class LigatureBuilderImpl implements LigatureBuilder {

    /**
     * Creates a new object.
     */
    public LigatureBuilderImpl(final Configuration configuration) {

        super();
    }

    /**
     * @see de.dante.extex.typesetter.ligatureBuilder.LigatureBuilder#insertLigatures(
     *      de.dante.extex.typesetter.NodeList)
     */
    public void insertLigatures(final NodeList list) {

        int size = list.size();
        int i = 0;
        Node node = null;

        while (i < size) {
            node = list.get(i);
            if (node instanceof CharNode) {
                i = processCharNodes(list, size, i, (CharNode) node);
                size = list.size();
            } else {
                i++;
            }
        }
    }

    /**
     * ...
     *
     * @param list the list of nodes to process
     * @param size the size of <code>list</code>
     * @param i the index to start with
     * @param node the node at index <code>i</code>
     *
     * @return the index of the next non-CharNode or the size of
     *   <code>list</code> if none is found
     */
    private int processCharNodes(final NodeList list, int size,
            int i, final CharNode node) {

        UnicodeChar uc1 = node.getCharacter();
        UnicodeChar uc2;
        Font font1 = node.getTypesettingContext().getFont();
        Font font2;
        Node n1 = node;
        Node n2;

        for (i++; i < size; i++) {
            n2 = list.get(i);
            if (!(n2 instanceof CharNode)) {
                return i;
            }
            font2 = ((CharNode) n2).getTypesettingContext().getFont();
            uc2 = ((CharNode) n2).getCharacter();

            if (font2 == font1) {

                //UnicodeChar lig = font1.getLigature(uc1, uc2);
                Glyph g = font1.getGlyph(uc1);
                if (g==null) {
                    return i; //TODO: DIRTY?
                }
                UnicodeChar lig = g.getLigature(uc2);
                if (lig != null) {
                    Node ligNode = new LigatureNode(node
                            .getTypesettingContext(), lig, n1, n2);
                    list.remove(i);
                    list.remove(--i);
                    list.add(i, ligNode);
                    uc1 = lig;
                    size--;
                } else {
                    n1 = n2;
                    font1 = font2;
                    uc1 = uc2;
                }
            } else {
                n1 = n2;
                font1 = font2;
                uc1 = uc2;
            }
        }

        return i;
    }

}
