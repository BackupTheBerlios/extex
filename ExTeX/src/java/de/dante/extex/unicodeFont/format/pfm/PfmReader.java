/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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
 */

package de.dante.extex.unicodeFont.format.pfm;

import java.io.IOException;
import java.io.InputStream;

import de.dante.extex.unicodeFont.format.afm.AfmParser;
import de.dante.util.file.random.RandomAccessInputStream;
import de.dante.util.file.random.RandomAccessR;

/**
 * Reader for a PFM-Font file.
 * TODO: ...
 *
 * <p>School project</p>
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @author <a href="mailto:...">...</a>
 * @author <a href="mailto:...">...</a>
 * @version $Revision: 1.1 $
 *
 */
public class PfmReader {

    /**
     * Create a new object.
     *
     * @param afm   The afm parser.
     */
    public PfmReader(final AfmParser afm) {

    }

    /**
     * Create a new object.
     *
     * @param rar    The input.
     * @throws IOException if an IO-error occurred.
     */
    public PfmReader(final RandomAccessR rar) throws IOException {

    }

    /**
     * Create a new object.
     *
     * @param in    The input stream.
     * @throws IOException if an IO-error occurred.
     */
    public PfmReader(final InputStream in) throws IOException {

        this(new RandomAccessInputStream(in));
    }

    /**
     * The pfm information.
     */
    private Pfm pfm;

    /**
     * Create a new object.
     *
     * @param apfm   The pfm object.
     */
    public PfmReader(final Pfm apfm) {

        pfm = apfm;
    }

    /**
     * Returns the pfm.
     * @return Returns the pfm.
     */
    public Pfm getPfm() {

        return pfm;
    }

}
