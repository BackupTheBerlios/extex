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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * TFMIndexMultimap can store and retrieve <code>int</code>
 * values associated to particular <code>int</code> key. There can be
 * more values associated to the same key.
 * TFtoPL[63]
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TFMIndexMultimap implements Serializable {

    /**
     * Create a new object
     */
    public TFMIndexMultimap() {

        super();
    }

    /**
     * Internal storage of (key, value) pairs
     */
    private ArrayList data = new ArrayList();

    /**
     * The number of (key, value) pairs kept.
     *
     * @return Returns the number of stored pairs.
     */
    public int size() {

        return data.size();
    }

    /**
     * (key, value) pair at given position.
     *
     * @param i the position of pair to be examined.
     * @return Return the pair at given position.
     */
    public TFMKeyInt at(final int i) {

        return (TFMKeyInt) data.get(i);
    }

    /**
     * Insert a (key, value) pair at the given position.
     *
     * @param p the pair to be inserted.
     * @param i the position to be inserted to.
     */
    public void insert(final TFMKeyInt p, final int i) {

        data.add(i, p);
    }

    /**
     * Gives the position where a (key, value) pair with given key is
     * stored or where it should be stored if there is no such pair.
     *
     * @param key the key searched for.
     * @return Return the position.
     */
    public int search(final int key) {

        int beg = 0;
        int end = size();
        while (beg < end) {
            int med = (beg + end) / 2;
            TFMKeyInt p = at(med);
            if (key < p.getKey()) {
                end = med;
            } else if (key > p.getKey()) {
                beg = med + 1;
            } else {
                return med;
            }
        }
        return beg;
    }

    /**
     * Adds a new (key, value) pair.
     *
     * @param key the key of the new pair.
     * @param val the value of the new pair.
     */
    public void add(final int key, final int val) {

        synchronized (data) {
            int pos = search(key);
            while (pos < size() && at(pos).getKey() == key) {
                pos++;
            }
            insert(new TFMKeyInt(key, val), pos);
        }
    }

    /**
     * Gives the sequence of all keys associated to the given key.
     *
     * @param key the given key.
     * @return Return the object representing the sequence of associated values.
     */
    public Enum forKey(final int key) {

        return new Enum(key);
    }

    /**
     * Class <code>Enum</code> provides the sequence of all values
     * associated to particular key.
     */
    public final class Enum implements Serializable {

        /**
         * Create a new object.
         *
         * @param k the key for which the values are required.
         */
        private Enum(final int k) {

            super();
            synchronized (data) {
                key = k;
                pos = search(key);
                while (pos > 0 && at(pos - 1).getKey() == key) {
                    pos--;
                }
            }
        }

        /**
         * the current position in the sequence of pairs
         */
        private int pos;

        /**
         * the key for which the values are required
         */
        private final int key;

        /**
         * Tests if there is another associated value.
         *
         * @return Return <code>true</code> if next value is available,
         *         otherwise <code>false</code>.
         */
        public boolean hasMore() {

            return (pos < size() && at(pos).getKey() == key);
        }

        /**
         * Gives the next value from the sequence of associated values.
         *
         * @return Return the next value.
         */
        public int next() {

            return at(pos++).getVal();
        }
    }
}