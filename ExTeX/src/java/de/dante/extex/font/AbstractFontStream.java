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

package de.dante.extex.font;

import java.io.IOException;
import java.io.InputStream;

import de.dante.extex.font.exception.FontException;
import de.dante.extex.font.exception.FontIOException;
import de.dante.extex.font.exception.FontNoExternalFileException;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;

/**
 * Abstract class for a FontStream.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractFontStream implements FontStream {

    /**
     * Create a new object
     * @param in  the external stream
     */
    AbstractFontStream(final InputStream in) {

        super();
        externalstream = in;
    }

    /**
     * the external stream
     */
    private InputStream externalstream;

    /**
     * Return the <code>InputStream</code> for the external font.
     * @return  Return the <code>InputStream</code> for the external font.
     */
    public InputStream getStream() {

        return externalstream;
    }

    /**
     * Set the inputstream.
     * @param in    the external stream to set.
     */
    public void setStream(final InputStream in) {

        externalstream = in;
    }

    /**
     * Returns the String for the class
     * @return Returns the string for the class
     */
    public abstract String toString();

    /**
     * the input as rar
     */
    private RandomAccessR rar;

    /**
     * @see de.dante.extex.font.FontStream#getRandomAccessRead()
     */
    public RandomAccessR getRandomAccessRead() throws FontException {

        if (rar == null) {
            if (externalstream == null) {
                throw new FontNoExternalFileException();
            }
            try {
                rar = new RandomAccessInputStream(getStream());
            } catch (IOException e) {
                throw new FontIOException(e.getMessage());
            }
        }
        return rar;
    }
}