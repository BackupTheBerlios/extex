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

import de.dante.extex.interpreter.type.dimen.FixedDimen;

/**
 * TODO gene: missing JavaDoc.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDviCode implements DviCode {

    /**
     * Creates a new object.
     */
    protected AbstractDviCode() {

        super();
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param stream the output stream to write to
     * @param value
     *
     * @throws IOException in case of an error
     */
    protected void write2(final OutputStream stream, final int value)
            throws IOException {

        stream.write(value >> 8);
        stream.write(value);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param stream the output stream to write to
     * @param value
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
     * TODO gene: missing JavaDoc
     *
     * @param stream the output stream to write to
     * @param value
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

    /**
     * TODO gene: missing JavaDoc
     *
     * @param stream
     * @param value
     * @throws IOException
     */
    protected void write4(final OutputStream stream, final FixedDimen value)
            throws IOException {

        long val = value.getValue();
        write4(stream, (int) val);
    }

    /**
     * TODO gene: missing JavaDoc
     *
     * @param baseOpcode
     * @param value
     * @param stream the output stream to write to
     *
     * @return
     *
     * @throws IOException in case of an error
     */
    protected int opcode(final int baseOpcode, final int value,
            final OutputStream stream) throws IOException {

        if (value <= 0xff) {
            stream.write(baseOpcode);
            stream.write(value);
            return 2;
        } else if (value <= 0xffff) {
            stream.write(baseOpcode + 1);
            stream.write(value >> 8);
            stream.write(value);
            return 3;
        } else if (value <= 0xffffff) {
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
     * TODO gene: missing JavaDoc
     *
     * @param baseOpcode
     * @param value
     * @param stream the output stream to write to
     *
     * @return
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
            stream.write((value >> 8) & 0xff);
            stream.write(value);
            return 3;
        } else if (-0x800000 <= value && value <= 0x7fffff) {
            stream.write(baseOpcode + 2);
            stream.write((value >> 16) & 0xff);
            stream.write((value >> 8) & 0xff);
            stream.write(value);
            return 4;
        }

        stream.write(baseOpcode + 3);
        stream.write((value >> 24) & 0xff);
        stream.write((value >> 16) & 0xff);
        stream.write((value >> 8) & 0xff);
        stream.write(value);
        return 5;
    }

}
