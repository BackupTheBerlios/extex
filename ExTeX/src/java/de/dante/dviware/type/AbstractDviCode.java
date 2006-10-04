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

/**
 * This is an abstract base class for DVI instructions.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.5 $
 */
public abstract class AbstractDviCode implements DviCode {

    /**
     * The constant <tt>ONE_BYTE_MASK</tt> contains the bit mask for a single
     * byte.
     */
    private static final int ONE_BYTE_MASK = 0xff;

    /**
     * The constant <tt>ONE_BYTE_MASK</tt> contains the bit mask for a double
     * byle.
     */
    private static final int TWO_BYTE_MASK = 0xffff;

    /**
     * The constant <tt>ONE_BYTE_MASK</tt> contains the bit mask for a triple
     * byle.
     */
    private static final int THREE_BYTE_MASK = 0xffffff;

    /**
     * Write two bytes to the output stream.
     *
     * @param stream the output stream to write to
     * @param value the value
     *
     * @throws IOException in case of an error
     */
    protected static void write2(final OutputStream stream, final int value)
            throws IOException {

        stream.write(value >> 8);
        stream.write(value);
    }

    /**
     * Creates a new object.
     */
    protected AbstractDviCode() {

        super();
    }

    /**
     * Write an opcode and some unsigned value to the output stream.
     * The value determines the exact opcode. If the value fits in one byte then
     * the base opcode is used. If two bytes are needed then 1 is added.
     * If two bytes are needed then 2 is added. Finally if all four bytes are
     * needed then 3 is added to the base opcode.
     *
     * @param baseOpcode the opcode
     * @param value the value
     * @param stream the output stream to write to
     *
     * @return the number of bytes written
     *
     * @throws IOException in case of an error
     */
    protected int opcode(final int baseOpcode, final int value,
            final OutputStream stream) throws IOException {

        if (value <= ONE_BYTE_MASK) {
            stream.write(baseOpcode);
            stream.write(value);
            return 2;
        } else if (value <= TWO_BYTE_MASK) {
            stream.write(baseOpcode + 1);
            stream.write(value >> 8);
            stream.write(value);
            return 3;
        } else if (value <= THREE_BYTE_MASK) {
            stream.write(baseOpcode + 2);
            stream.write(value >> 16);
            stream.write(value >> 8);
            stream.write(value);
            return 4;
        }

        stream.write(baseOpcode + 3);
        stream.write(value >> 24);
        stream.write(value >> 16);
        stream.write(value >> 8);
        stream.write(value);
        return 5;
    }

    /**
     * Write an opcode and some signed value to the output stream.
     * The value determines the exact opcode. If the value fits in one byte then
     * the base opcode is used. If two bytes are needed then 1 is added.
     * If two bytes are needed then 2 is added. Finally if all four bytes are
     * needed then 3 is added to the base opcode.
     *
     * @param baseOpcode the opcode
     * @param value the value
     * @param stream the output stream to write to
     *
     * @return the number of bytes written
     *
     * @throws IOException in case of an error
     */
    protected int opcodeSigned(final int baseOpcode, final int value,
            final OutputStream stream) throws IOException {

        if (-0x80 <= value && value < 0x7f) {
            stream.write(baseOpcode);
            stream.write(value);
            return 2;
        } else if (-0x8000 <= value && value <= 0x7fff) {
            stream.write(baseOpcode + 1);
            stream.write((value >> 8) & ONE_BYTE_MASK);
            stream.write(value);
            return 3;
        } else if (-0x800000 <= value && value <= 0x7fffff) {
            stream.write(baseOpcode + 2);
            stream.write((value >> 16) & ONE_BYTE_MASK);
            stream.write((value >> 8) & ONE_BYTE_MASK);
            stream.write(value);
            return 4;
        }

        stream.write(baseOpcode + 3);
        stream.write((value >> 24) & ONE_BYTE_MASK);
        stream.write((value >> 16) & ONE_BYTE_MASK);
        stream.write((value >> 8) & ONE_BYTE_MASK);
        stream.write(value);
        return 5;
    }

    /**
     * Determine which variant of a DVI instruction is needed for the operand.
     *
     * @param value the unsigned value
     *
     * @return the variant
     */
    protected String variant(final int value) {

        if (value <= ONE_BYTE_MASK) {
            return "1";
        } else if (value <= TWO_BYTE_MASK) {
            return "2";
        } else if (value <= THREE_BYTE_MASK) {
            return "3";
        }

        return "4";
    }

    /**
     * Write three bytes to the output stream.
     *
     * @param stream the output stream to write to
     * @param value the value
     *
     * @throws IOException in case of an error
     */
    protected void write3(final OutputStream stream, final int value)
            throws IOException {

        stream.write(value >> 16);
        stream.write(value >> 8);
        stream.write(value);
    }

    /**
     * Write four bytes to the output stream.
     *
     * @param stream the output stream to write to
     * @param value the value
     *
     * @throws IOException in case of an error
     */
    protected void write4(final OutputStream stream, final int value)
            throws IOException {

        stream.write(value >> 24);
        stream.write(value >> 16);
        stream.write(value >> 8);
        stream.write(value);
    }

}
