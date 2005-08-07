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

package de.dante.extex.unicodeFont.format.tex.tfm;

import java.io.IOException;
import java.io.Serializable;

import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * Class for TFM height table.
 *
 * <p>width : array [0 .. (nh-1)] of fix word</p>
 *
 * <p>
 * Information from:
 * The DVI Driver Standard, Level 0
 * The TUG DVI Driver Standards Committee
 * </p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */

public class TFMHeightArray implements XMLWriterConvertible, Serializable {

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
    public TFMHeightArray(final RandomAccessR rar, final int size)
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
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("heighttable");
        for (int i = 0; i < table.length; i++) {
            writer.writeStartElement("height");
            writer.writeAttribute("id", String.valueOf(i));
            writer.writeAttribute("value_fw", String.valueOf(table[i]
                    .getValue()));
            writer.writeAttribute("value", table[i].toStringComma());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}
