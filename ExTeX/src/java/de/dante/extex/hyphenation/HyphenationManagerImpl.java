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
package de.dante.extex.hyphenation;

import java.util.HashMap;
import java.util.Map;


/**
 * ...
 *
 * @author <a href="gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class HyphenationManagerImpl implements HyphenationManager {

    /**
     * The field <tt>tables</tt> ...
     */
    private Map tables = new HashMap();

    /**
     * Creates a new object.
     */
    public HyphenationManagerImpl() {
        super();
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationManager#createHyphenationTable(java.lang.String)
     */
    public HyphenationTable createHyphenationTable(final String index) {
        HyphenationTable table = (HyphenationTable) (tables.get(index));
        if (table == null) {
            table = new HyphenationTableImpl();
            tables.put(index, table);
        }
        return table;
    }

    /**
     * @see de.dante.extex.hyphenation.HyphenationManager#getHyphenationTable(java.lang.String)
     */
    public HyphenationTable getHyphenationTable(final String index) {
        return (HyphenationTable)(tables.get(index));
    }

}
