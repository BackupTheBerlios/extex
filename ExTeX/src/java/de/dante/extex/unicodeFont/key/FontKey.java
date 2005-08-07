/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.key;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dante.extex.interpreter.type.dimen.Dimen;

/**
 * Class for a font key.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class FontKey implements Serializable {

    /**
     * The name of the font.
     */
    private String name;

    /**
     * Map for string values.
     */
    private Map stringMap;

    /**
     * Map for dimen values.
     */
    private Map dimenMap;

    /**
     * Map for boolean values.
     */
    private Map booleanMap;

    /**
     * Create a new object (only in the same name space!).
     *
     * @param theName   The name of the font.
     */
    protected FontKey(final String theName) {

        name = theName;
        stringMap = new HashMap();
        dimenMap = new HashMap();
        booleanMap = new HashMap();
    }

    /**
     * Create a new object (only in the same name space!).
     *
     * @param fk   The font key.
     */
    protected FontKey(final FontKey fk) {

        name = fk.getName();
        stringMap = new HashMap(fk.getStringMap());
        dimenMap = new HashMap(fk.getDimenMap());
        booleanMap = new HashMap(fk.getBooleanMap());
    }

    /**
     * Put an key values pair on the map.
     * @param key   The key.
     * @param value The value.
     */
    public void put(final String key, final String value) {

        stringMap.put(key, value);
    }

    /**
     * Put an key values pair on the map.
     * @param key   The key.
     * @param value The value.
     */
    public void put(final String key, final Dimen value) {

        dimenMap.put(key, value);
    }

    /**
     * Put an key values pair on the map.
     * @param key   The key.
     * @param value The value.
     */
    public void put(final String key, final boolean value) {

        booleanMap.put(key, new Boolean(value));
    }

    /**
     * Put an key values pair on the map.
     * @param theMap    The map.
     */
    public void put(final Map theMap) {

        Iterator it = theMap.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            Object obj = theMap.get(key);
            if (obj instanceof String) {
                put(key, (String) obj);
            } else if (obj instanceof Dimen) {
                put(key, (Dimen) obj);
            } else if (obj instanceof Boolean) {
                put(key, ((Boolean) obj).booleanValue());
            }
        }
    }

    /**
     * Returns the value for the key or <code>null</code>,
     * if no key exists in the map.
     * @param key The key.
     * @return Returns the value for the key.
     */
    public String getString(final String key) {

        return (String) stringMap.get(key);
    }

    /**
     * Returns the value for the key or <code>null</code>,
     * if no key exists in the map.
     * @param key The key.
     * @return Returns the value for the key.
     */
    public Dimen getDimen(final String key) {

        Dimen d = (Dimen) dimenMap.get(key);
        if (d != null) {
            return new Dimen(d);
        }
        return null;
    }

    /**
     * Returns the value for the key or <code>false</code>,
     * if no key exists in the map.
     * @param key The key.
     * @return Returns the value for the key.
     */
    public boolean getBoolean(final String key) {

        Boolean b = (Boolean) booleanMap.get(key);
        if (b == null) {
            return false;
        }
        return b.booleanValue();
    }

    /**
     * Returns the name.
     * @return Returns the name.
     */
    public String getName() {

        return name;
    }

    /**
     * Returns the booleanMap.
     * @return Returns the booleanMap.
     */
    protected Map getBooleanMap() {

        return booleanMap;
    }

    /**
     * Returns the dimenMap.
     * @return Returns the dimenMap.
     */
    protected Map getDimenMap() {

        return dimenMap;
    }

    /**
     * Returns the stringMap.
     * @return Returns the stringMap.
     */
    protected Map getStringMap() {

        return stringMap;
    }

}
