/*
 * Copyright (C) 2006 The ExTeX Group and individual authors listed below
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

package de.dante.dviware.type;

import java.io.IOException;
import java.io.OutputStream;

import de.dante.dviware.Dvi;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public interface DviCode {

    /**
     * The field <tt>POP</tt> contains the ...
     */
    public static final DviCode POP = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.POP);
            return 1;
        }

    };

    /**
     * The field <tt>PUSH</tt> contains the ...
     */
    public static final DviCode PUSH = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.PUSH);
            return 1;
        }

    };

    /**
     * The field <tt>W0</tt> contains the ...
     */
    public static final DviCode W0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.W0);
            return 1;
        }

    };

    /**
     * The field <tt>X0</tt> contains the ...
     */
    public static final DviCode X0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.X0);
            return 1;
        }

    };

    /**
     * The field <tt>X0</tt> contains the ...
     */
    public static final DviCode Y0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.Y0);
            return 1;
        }

    };

    /**
     * The field <tt>Z0</tt> contains the ...
     */
    public static final DviCode Z0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.Z0);
            return 1;
        }

    };

    /**
     * Write the code to the output stream.
     *
     * @param stream the target stream
     *
     * @throws IOException in case of an error
     */
    int write(OutputStream stream) throws IOException;

}
