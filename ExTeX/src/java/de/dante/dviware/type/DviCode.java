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
 * This interface describes DVI code. It must be written to the output stream
 * at the end.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public interface DviCode {

    /**
     * The constant <tt>POP</tt> contains the DviCode for the <tt>pop</tt>
     * instruction. This instruction doe not carry any parameters. Thus a
     * singleton can be used.
     */
    DviCode POP = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#getName()
         */
        public String getName() {

            return "pop";
        }

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.POP);
            return 1;
        }

    };

    /**
     * The constant <tt>PUSH</tt> contains the DviCode for the <tt>push</tt>
     * instruction. This instruction doe not carry any parameters. Thus a
     * singleton can be used.
     */
    DviCode PUSH = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#getName()
         */
        public String getName() {

            return "push";
        }

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.PUSH);
            return 1;
        }

    };

    /**
     * The constant <tt>W0</tt> contains the DviCode for the <tt>w0</tt>
     * instruction. This instruction doe not carry any parameters. Thus a
     * singleton can be used.
     */
    DviCode W0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#getName()
         */
        public String getName() {

            return "w0";
        }

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.W0);
            return 1;
        }

    };

    /**
     * The constant <tt>X0</tt> contains the DviCode for the <tt>x0</tt>
     * instruction. This instruction doe not carry any parameters. Thus a
     * singleton can be used.
     */
    DviCode X0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#getName()
         */
        public String getName() {

            return "x0";
        }

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.X0);
            return 1;
        }

    };

    /**
     * The constant <tt>X0</tt> contains the DviCode for the <tt>y0</tt>
     * instruction. This instruction doe not carry any parameters. Thus a
     * singleton can be used.
     */
    DviCode Y0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#getName()
         */
        public String getName() {

            return "y0";
        }

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.Y0);
            return 1;
        }

    };

    /**
     * The constant <tt>Z0</tt> contains the DviCode for the <tt>z0</tt>
     * instruction. This instruction doe not carry any parameters. Thus a
     * singleton can be used.
     */
    DviCode Z0 = new DviCode() {

        /**
         * @see de.dante.dviware.type.DviCode#getName()
         */
        public String getName() {

            return "z0";
        }

        /**
         * @see de.dante.dviware.type.DviCode#write(java.io.OutputStream)
         */
        public int write(final OutputStream stream) throws IOException {

            stream.write(Dvi.Z0);
            return 1;
        }

    };

    /**
     * Getter for the name f the DVI instruction.
     *
     * @return the name of the DVI instruction
     */
    String getName();

    /**
     * Write the code to the output stream.
     *
     * @param stream the target stream
     *
     * @return the number of bytes actually written
     *
     * @throws IOException in case of an error
     */
    int write(OutputStream stream) throws IOException;

}
