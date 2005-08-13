/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.unicodeFont.format.xtf;

import java.io.IOException;

import org.jdom.Comment;
import org.jdom.Element;

import de.dante.util.XMLConvertible;
import de.dante.util.XMLWriterConvertible;
import de.dante.util.file.random.RandomAccessR;
import de.dante.util.xml.XMLStreamWriter;

/**
 * The 'FMTX' ... TODO incomplete
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class OtfTableFMTX extends AbstractXtfTable
        implements
            XtfTable,
            XMLWriterConvertible{

    /**
     * Create a new object
     *
     * @param tablemap  the tablemap
     * @param de        entry
     * @param rar       input
     * @throws IOException if an IO-error occurs
     */
    OtfTableFMTX(final XtfTableMap tablemap, final XtfTableDirectory.Entry de,
            final RandomAccessR rar) throws IOException {

        super(tablemap);
        rar.seek(de.getOffset());

        // incomplete
    }

    /**
     * Get the table type, as a table directory value.
     * @return Returns the table type
     */
    public int getType() {

        return XtfReader.FMTX;
    }
    /**
     * @see de.dante.extex.unicodeFont.format.xtf.XtfTable#getShortcur()
     */
    public String getShortcut() {

        return "fmtx";
    }

    /**
     * @see de.dante.util.XMLWriterConvertible#writeXML(
     *      de.dante.util.xml.XMLStreamWriter)
     */
    public void writeXML(final XMLStreamWriter writer) throws IOException {

        writeStartElement(writer);
        writer.writeComment("incomplete");
        writer.writeEndElement();
    }
}