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

package de.dante.extex.font.type.tfm;

import java.io.IOException;
import java.io.Serializable;

import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.file.random.RandomAccessR;

/**
 * Class for TFM italic table.
 *
 * <p>width : array [0 .. (ni-1)] of fix word</p>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */

public class TFMItalicArray implements XMLConvertible, Serializable {

    /**
     * the height table
     */
    private TFMFixWord[] table;

    /**
     * Create a new object
     * @param rar   the input
     * @param size  number of words in the table
     * @throws IOException if an IO-error occurs.
     */
    public TFMItalicArray(final RandomAccessR rar, final int size)
            throws IOException {

        table = new TFMFixWord[size];

        for (int i = 0; i < size; i++) {
            table[i] = new TFMFixWord(rar.readInt(),
                    TFMFixWord.FIXWORDDENOMINATOR);
        }
    }

    /**
     * Returns the table.
     * @return Returns the table.
     */
    public TFMFixWord[] getTable() {

        return table;
    }

    /**
     * @see de.dante.util.XMLConvertible#toXML()
     */
    public Element toXML() {

        Element element = new Element("italictable");
        for (int i = 0; i < table.length; i++) {
            Element e = new Element("italic");
            e.setAttribute("id", String.valueOf(i));
            e.setAttribute("value", table[i].toStringComma());
            element.addContent(e);
        }
        return element;
    }
}
