/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type;

import java.util.ArrayList;

/**
 * Class for a <code>FontFile</code>List.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class FontFileList {

    /**
     * The list
     */
    private ArrayList list;

    /**
     * Create a new object.
     */
    public FontFileList() {

        super();
        list = new ArrayList();
    }

    /**
     * Create a new object.
     * @param initsize  the initsize for the list
     */
    public FontFileList(final int initsize) {

        super();
        list = new ArrayList(initsize);
    }

    /**
     * Return the size of the list
     * @return  the size of the list
     */
    public int size() {

        return list.size();
    }

    /**
     * Add a <code>FontFile</code>
     * @param fontfile  the fontfile to add
     */
    public void add(final FontFile fontfile) {

        list.add(fontfile);
    }

    /**
     * Return the fontfile at position idx.
     * @param idx   the position
     * @return  the fontfile at position idx
     */
    public FontFile getFontFile(final int idx) {

        return (FontFile) list.get(idx);
    }

}
