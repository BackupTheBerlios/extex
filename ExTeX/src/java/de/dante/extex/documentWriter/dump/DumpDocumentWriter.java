/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
import java.io.OutputStream;

import de.dante.extex.documentWriter.DocumentWriter;
import de.dante.extex.documentWriter.DocumentWriterOptions;
import de.dante.extex.typesetter.NodeList;
import de.dante.util.configuration.Configuration;

/**
 * This is a first dummy implementation of a document writer.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.14 $
 */
public class DumpDocumentWriter implements DocumentWriter {

    /**
     * The field <tt>out</tt> contains the outut stream to use.
     */
    private OutputStream out = null;

    /**
     * The field <tt>shippedPages</tt> contains the number of pages already
     * shipped out.
     */
    private int shippedPages = 0;

    /**
     * The field <tt>options</tt> contains the ...
     */
    private DocumentWriterOptions options;

    /**
     * Creates a new object.
     *
     * @param cfg the configuration object
     * @param opts the dynamic access to the context
     */
    public DumpDocumentWriter(final Configuration cfg,
            final DocumentWriterOptions opts) {

        super();
        options = opts;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#close()
     */
    public void close() {

        // nothing to do
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getExtension()
     */
    public String getExtension() {

        return "out";
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#getPages()
     */
    public int getPages() {

        return shippedPages;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setOutputStream(
     *      java.io.OutputStream)
     */
    public void setOutputStream(final OutputStream outStream) {

        out = outStream;
    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#setParameter(
     *      java.lang.String, java.lang.String)
     */
    public void setParameter(final String name, final String value) {

    }

    /**
     * @see de.dante.extex.documentWriter.DocumentWriter#shipout(
     *      de.dante.extex.typesetter.NodeList)
     */
    public void shipout(final NodeList nodes) throws IOException {

        StringBuffer sb = new StringBuffer();
        nodes.toString(sb, "\n");
        out.write(sb.toString().getBytes());
        out.write('\n');
        shippedPages++;
    }

}