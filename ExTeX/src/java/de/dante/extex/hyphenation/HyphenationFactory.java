/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.hyphenation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.dante.extex.hyphenation.liang.LiangsHyphenationTable;
import de.dante.util.framework.AbstractFactory;

/**
 * This class managed the <code>HyphenationTable</code>.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class HyphenationFactory extends AbstractFactory implements Serializable {

    /**
     * The field <tt>tables</tt> contains the mapping from index to
     * hyphenation table.
     */
    private Map tables = new HashMap();

    /**
     * Creates a new object.
     */
    public HyphenationFactory() {

        super();
    }

    /**
     * Return the <code>HyphernationTable</code>.
     * <p>
     * If there is no table present with the given index then
     * a new one is created.
     * </p>
     * <p>
     * The index in TeX is the language number as <code>String</code>. This
     * implementation does not have this restriction.
     * </p>
     *
     * @see de.dante.extex.hyphenation.HyphenationFactory#getHyphenationTable(java.lang.String)
     */
    public HyphenationTable getHyphenationTable(final String index) {

        HyphenationTable table = (HyphenationTable) (tables.get(index));
        if (table == null) {
            table = new LiangsHyphenationTable();
            tables.put(index, table);
        }
        return table;
    }
}