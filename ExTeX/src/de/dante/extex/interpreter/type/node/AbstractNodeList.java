/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.interpreter.type.node;

import de.dante.extex.interpreter.type.Dimen;
import de.dante.extex.interpreter.type.Glue;
import de.dante.extex.typesetter.Node;
import de.dante.extex.typesetter.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractNodeList extends AbstractNode
    implements NodeList {
    /** ... */
    private Dimen shift = new Dimen(0);

    /** ... */
    private List list = new ArrayList();

    /**
     * Creates a new object.
     */
    public AbstractNodeList() {
        super();
    }

    /**
     * @see de.dante.extex.typesetter.NodeList#add(de.dante.extex.typesetter.Node)
     */
    public void add(Node node) {
        list.add(node);
    }

    /**
     * @see de.dante.extex.typesetter.NodeList#addGlyph(de.dante.extex.interpreter.type.node.CharNode)
     */
    public void addGlyph(CharNode node) {
        list.add(node);
    }

    /**
     * @see de.dante.extex.typesetter.NodeList#addSkip(de.dante.extex.interpreter.type.Glue)
     */
    public void addSkip(Glue glue) {
        list.add(glue);
    }

    /**
     * ...
     *
     * @return the String representation of the object
     * @see "TeX -- The Program [182]"
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * ...
     *
     * @param sb the target StringBuffer
     */
    public abstract void toString(StringBuffer sb);

    /**
     * ...
     *
     * @param sb the target StringBuffer
     * @param prefix ...
     */
    protected void toString(final StringBuffer sb, final String prefix) {
        sb.append("(");
        sb.append(prefix);

        for (int i = 0; i < list.size(); i++) {
            ((Node) list.get(i)).toString(sb);
        }

        sb.append(")");
    }
}
