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
 * Class for TFM exten array.
 *
 * <p>
 * Extensible characters are specified by an extensible_recipe,
 * which consists of four bytes called top, mid, bot, and
 * rep (in this order). These bytes are the character codes of
 * individual pieces used to build up a large symbol.
 * </p>
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

public class TFMExtenArray implements XMLWriterConvertible, Serializable {

    /**
     * the array
     */
    private TFMExtensibleRecipe[] extensiblerecipe;

    /**
     * Create a new object
     * @param rar   the input
     * @param ne    number of words in the extensible character table
     * @throws IOException if an IO-error occurs.
     */
    public TFMExtenArray(final RandomAccessR rar, final short ne)
            throws IOException {

        extensiblerecipe = new TFMExtensibleRecipe[ne];
        for (int i = 0; i < ne; i++) {
            extensiblerecipe[i] = new TFMExtensibleRecipe(rar, i);
        }
    }

    /**
     * Returns the extensiblerecipe.
     * @return Returns the extensiblerecipe.
     */
    public TFMExtensibleRecipe[] getExtensiblerecipe() {

        return extensiblerecipe;
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writer.writeStartElement("exten");
        for (int i = 0; i < extensiblerecipe.length; i++) {
            extensiblerecipe[i].writeXML(writer);
        }
        writer.writeEndElement();
    }
}
