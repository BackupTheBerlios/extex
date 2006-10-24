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

package de.dante.extex.language.ligature.impl;

import de.dante.extex.interpreter.context.tc.TypesettingContext;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.language.hyphenation.exception.HyphenationException;
import de.dante.extex.language.ligature.LigatureBuilder;
import de.dante.extex.typesetter.type.Node;
import de.dante.extex.typesetter.type.NodeList;
import de.dante.extex.typesetter.type.node.CharNode;
import de.dante.extex.typesetter.type.node.ImplicitKernNode;
import de.dante.extex.typesetter.type.node.LigatureNode;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for a ligature builder.
 * Kerning and ligatures are inserted according to the specification from the
 * font.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.11 $
 */
public class LigatureBuilderImpl implements LigatureBuilder {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     */
    public LigatureBuilderImpl() {

        super();
    }

    /**
     * @see de.dante.extex.language.ligature.LigatureBuilder#getLigature(
     *      de.dante.util.UnicodeChar,
     *      de.dante.util.UnicodeChar,
     *      de.dante.extex.interpreter.type.font.Font)
     */
    public UnicodeChar getLigature(final UnicodeChar c1, final UnicodeChar c2,
            final Font f) {

        return f.getLigature(c1, c2);
    }

    /**
     * @see de.dante.extex.language.ligature.LigatureBuilder#insertLigatures(
     *      de.dante.extex.typesetter.type.NodeList, int)
     */
    public int insertLigatures(final NodeList list, final int start)
            throws HyphenationException {

        int i = start;
        while (i < list.size() && !(list.get(i) instanceof CharNode)) {
            i++;
        }
        if (i >= list.size()) {
            return i;
        }
        CharNode lastNode = (CharNode) list.get(i);
        TypesettingContext lastTc = lastNode.getTypesettingContext();
        Font font = lastTc.getFont();
        UnicodeChar lastC = lastNode.getCharacter();

        while (++i < list.size()) {

            Node node = list.get(i);
            if (!(node instanceof CharNode)) {
                return i;
            }
            CharNode thisNode = (CharNode) node;
            if (thisNode.getTypesettingContext() != lastTc) {
                return i;
            }
            UnicodeChar thisC = thisNode.getCharacter();

            UnicodeChar lig = font.getLigature(lastC, thisC);
            if (lig != null) {
                list.remove(i--);
                list.remove(i);
                lastNode = new LigatureNode(lastTc, lig, lastNode, thisNode);
                list.add(i--, lastNode);
                lastC = lig;
            } else {
                FixedDimen kern = font.getKerning(lastC, thisC);
                if (kern != null && kern.ne(Dimen.ZERO)) {
                    list.add(i, new ImplicitKernNode(kern, true));
                    i++;
                }
                lastNode = thisNode;
                lastC = thisC;
            }
        }

        return i;
    }

}
