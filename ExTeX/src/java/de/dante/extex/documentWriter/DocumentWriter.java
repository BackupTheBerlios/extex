/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.documentWriter;

import java.io.OutputStream;

/**
 * This is the interface to the backend of the system. The document has to be written to
 * a output stream. Certain information can be acquired before and after the production
 * of the output.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface DocumentWriter {
    /**
     * Getter for the extension associated with this kind of output.
     * E.g. <tt>.pdf</tt> is the expected value for PDF files and <tt>.dvi</tt> is the
     * expected value for DVI files.
     *
     * @return the appropriate filename extension
     */
    public abstract String getExtension();

    /**
     * Setter for the output stream.
     *
     * @param os the output stream
     */
    public abstract void setOutputStream(OutputStream os);

    /**
     * Getter for the number of pages already produced.
     *
     * @return the number of pages
     */
    public abstract int getPages();
}
