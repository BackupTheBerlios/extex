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

package de.dante.extex.hyphenation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class manages the <code>HyphenationTable</code>s. It is a container
 * which can be asked to provide an appropriate instance. This instance is
 * either taken from existing instances or a new instance is created.
 *
 * <h2>Configuration</h2>
 *
 * This instance is configurable. The configuration is used to select the
 * appropriate classs and optional parameters for a requested instance. In this
 * respect this class makes best use of the infrastructure of the
 * {@link de.dante.util.framework.AbstractFactory AbstractFactory}.
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class HyphenationManagerImpl extends BaseHyphenationManager
        implements
            Serializable {

    /**
     * TODO gene: missing JavaDoc
     *
     * @param in the stream to read from
     *
     * @throws IOException in case of an IO error
     * @throws ClassNotFoundException in case of a non existing class
     *  definition
     */
    private void readObject(final ObjectInputStream in)
            throws IOException,
                ClassNotFoundException {

        Map tables = getTables();
        Map map = (Map) in.readObject();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            String key = (String) e.getKey();
            Object value = e.getValue();
            tables.put(key, value);
        }
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param key the name of the table
     * @param value the table itself
     *
     * @return <code>true</code> iff the table has been saved
     */
    private boolean saveTable(final String key, final Object value) {

        if (key.matches("\\d+")) {
            return false;
        }
        //TODO gene: dump on new file

        return true;
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param out
     * @throws IOException
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {

        Map map = new HashMap();
        Iterator iter = getTables().entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry) iter.next();
            String key = (String) e.getKey();
            Object value = e.getValue();
            if (!saveTable(key, value)) {
                map.put(key, value);
            }
        }
        out.writeObject(map);
    }

}