/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf.cff;

import java.io.IOException;
import java.util.List;

import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * Operator.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public abstract class T2Operator extends T2CharString implements XMLWriterConvertible{

    /**
     * rmoveto
     */
    public static final int RMOVETO = 21;

    /**
     * Create a new object.
     */
    protected T2Operator() {

        super();
    }

    /**
     * Create a new instance.
     *
     * @param rar       the input
     * @return Returns the new T2Operatorr object.
     * @throws IOException if an IO-error occurs.
     */
    public static T2Operator newInstance(final RandomAccessR rar)
            throws IOException {

        int b0 = rar.readUnsignedByte();

        if (b0 >= 0 && b0 <= 31) {

            switch (b0) {
                case 0 :
                    break;
                case 1 :
                    break;
                case 2 :
                    break;
                case 3 :
                    break;
                case 4 :
                    break;
                case 5 :
                    break;
                case 6 :
                    break;
                case 7 :
                    break;
                case 8 :
                    break;
                case 9 :
                    break;
                case 10 :
                    break;
                case 11 :
                    break;
                case 12 :
                    break;
                case 13 :
                    break;
                case 14 :
                    break;
                case 15 :
                    break;
                case 16 :
                    break;
                case 17 :
                    break;
                case 18 :
                    break;
                case 19 :
                    break;
                case 20 :
                    break;
                case RMOVETO :
                    return new T2RMOVETO();
                case 22 :
                    break;
                case 23 :
                    break;
                case 24 :
                    break;
                case 25 :
                    break;
                case 26 :
                    break;
                case 27 :
                    break;
                case 29 :
                    break;
                case 30 :
                    break;
                case 31 :
                    break;

            }

        }

        throw new T2NotAOperatorException();
    }

    /**
     * @see de.dante.extex.font.type.ttf.cff.T2CharString#isOperator()
     */
    public boolean isOperator() {

        return true;
    }

    /**
     * Return the name of the operator.
     * @return Return the name of the operator.
     */
    public abstract String getName();

    /**
     * Convert a stack (a List) into an array and add at the top the id-array.
     * @param stack     the stack
     * @param id        the id-array
     * @return Return the byte-array
     */
    protected short[] convertStackaddID(final List stack, final short[] id) {

        // calculate size
        int size = id.length;

        for (int i = 0; i < stack.size(); i++) {
            T2CharString obj = (T2CharString) stack.get(i);
            short[] tmp = obj.getBytes();
            if (tmp != null) {
                size += tmp.length;
            }
        }

        // copy elements
        short[] bytes = new short[size];
        System.arraycopy(id, 0, bytes, 0, id.length);

        int pos = id.length;
        for (int i = 0; i < stack.size(); i++) {
            T2CharString obj = (T2CharString) stack.get(i);
            short[] tmp = obj.getBytes();
            if (tmp != null) {
                System.arraycopy(obj.getBytes(), 0, bytes, pos, tmp.length);
                pos += tmp.length;
            }
        }
        return bytes;
    }
}