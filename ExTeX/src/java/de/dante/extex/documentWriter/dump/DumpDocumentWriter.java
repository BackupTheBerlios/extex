/*
 * Copyright (C) 2003-2004 Gerd Neugebauer
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
package de.dante.extex.documentWriter.dump;

import java.io.IOException;
import java.io.Writer;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.GeneralException;

/**
 * This is a first dummy implementation of a document writer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public class DumpDocumentWriter implements DocumentWriter {

    /**
     * The field <tt>out</tt> ...
     */
    private Writer out = null;

    /**
     * The field <tt>shippedPages</tt> ...
     */
    private int shippedPages = 0;

    /**
     * Creates a new object.
     */
    public DumpDocumentWriter() {
        super();
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {
        return shippedPages;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {
        return "out";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setWriter(java.io.Writer)
     */
    public void setWriter(final Writer outStream) {
        out = outStream;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws GeneralException,
        IOException {
        StringBuffer sb = new StringBuffer();
        nodes.toString(sb,"\n");
        out.write(sb.toString());
        out.write('\n');
        shippedPages++;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() throws GeneralException, IOException {
    }

}
