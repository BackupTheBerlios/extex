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

package de.dante.extex.font.type.ttf.cff;

import java.io.IOException;

import de.dante.util.file.random.RandomAccessR;

/**
 * The Type 2 Chatstring format.
 *
 * @see <a href="http://partners.adobe.com/asn/developer/pdfs/tn/5177.Type2.pdf">
 *      Adobe Technical Note #5177: Type 2 Charstring Format</a>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public abstract class T2CharString {

    /**
     * escape-byte
     */
    public static final byte ESCAPE_BYTE = 12;

    /**
     * escape
     */
    public static final T2Escape ESCAPE = new T2Escape();

    /**
     * Create a new object.
     */
    protected T2CharString() {

        super();
    }

    /**
     * Check, if the objekt is an operator.
     * @return Returns <code>true</code>, if the object is an operator.
     */
    public boolean isOperator() {

        return false;
    }

    /**
     * Check, if the objekt is an escape-marker.
     * @return Returns <code>true</code>, if the object is an escape-marker.
     */
    public boolean isEscape() {

        return false;
    }

    /**
     * Check, if the objekt is a integer.
     * @return Returns <code>true</code>, if the object is a integer.
     */
    public boolean isInteger() {

        return false;
    }

    /**
     * Check, if the objekt is a double.
     * @return Returns <code>true</code>, if the object is a double.
     */
    public boolean isDouble() {

        return false;
    }

    /**
     * Check, if the objekt is a boolean.
     * @return Returns <code>true</code>, if the object is a boolean.
     */
    public boolean isBoolean() {

        return false;
    }

    /**
     * Check, if the objekt is a array.
     * @return Returns <code>true</code>, if the object is a array.
     */
    public boolean isArray() {

        return false;
    }

    /**
     * Check, if the objekt is a Top DICT operator.
     * @return Returns <code>true</code>, if the object is a Top DICT operator.
     */
    public boolean isTopDICTOperator() {

        return false;
    }

    /**
     * Returns the byte-array as short for the object.
     * @return Returns the byte-array for the object.
     */
    public abstract short[] getBytes();

    /**
     * Read a number.
     *
     * @param rar   the input
     * @return Returns the number.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Number readNumber(final RandomAccessR rar)
            throws IOException {

        return T2Number.newInstance(rar);
    }

    /**
     * Read a number.
     *
     * @param rar   the input
     * @param b0    the first byte
     * @return Returns the number.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Number readNumber(final RandomAccessR rar, final int b0)
            throws IOException {

        return T2Number.newInstance(rar, b0);
    }

    /**
     * Read a SID.
     *
     * <p>
     * SID  (0-64999) 2-byte string identifier
     * </p>
     *
     * @param rar   the input
     * @return Returns the SID.
     * @throws IOException if an IO-error occurs.
     */
    public static T2SID readSID(final RandomAccessR rar) throws IOException {

        return new T2SID(rar);
    }

    /**
     * Read a operator.
     *
     * @param rar   the input
     * @return Returns the operator.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Operator readOperator(final RandomAccessR rar)
            throws IOException {

        return T2Operator.newInstance(rar);
    }

    /**
     * Read a top DICT operator.
     *
     * @param rar       the input
     * @return  Return the Top DICT operator
     * @throws IOException if an IO-error occurs
     */
    public static T2Operator readTopDICTOperator(final RandomAccessR rar)
            throws IOException {

        return T2TopDICTOperator.newInstance(rar);
    }
}