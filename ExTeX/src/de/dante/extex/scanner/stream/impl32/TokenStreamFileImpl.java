/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.scanner.stream.impl32;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;

import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.UnicodeChar;
import de.dante.util.file.InputLineDecodeStream;

/**
 * This class contains an implementation of a token stream
 * which is fed from a File.
 *
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TokenStreamFileImpl extends AbstractTokenStreamImpl
        implements
            TokenStream,
            CatcodeVisitor {

    /**
     * the source
     */
    private String source;

    /**
     * the InputLineDecodeStream
     */
    private InputLineDecodeStream in;

    /**
     * the encoding
     */
    private String encoding;

    /**
     * the charbuffer for the line
     */
    private CharBuffer buffer;

    /**
     * Creates a new object.
     * @param   filename    the filename
     * @param   enc         the encoding for the file
     * @throws IOException ...
     */
    public TokenStreamFileImpl(final File filename, final String enc)
            throws IOException {

        super();
        this.source = filename.getPath();
        in = new InputLineDecodeStream(new BufferedInputStream(
                new FileInputStream(filename)));
        encoding = enc;
        refill();
    }

    /**
     * @see de.dante.extex.scanner.stream.impl32.AbstractTokenStreamImpl#bufferLength()
     */
    protected int bufferLength() {

        return buffer.length();
    }

    /**
     * @see de.dante.extex.scanner.stream.impl32.AbstractTokenStreamImpl#getSingleChar()
     */
    protected UnicodeChar getSingleChar() {
        // TODO incomplete 2x16bit
        return new UnicodeChar(buffer.get(pointer));
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return buffer.toString();
    }

    /**
     * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
     */
    public boolean isFileStream() {

        return true;
    }

    /**
     * ...
     *
     * @return ...
     */
    protected String getSource() {

        return source;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#refill()
     */
    protected boolean refill() throws IOException {

        super.refill();
        if (in == null) {
            return false;
        }

        if ((buffer = in.readLine(encoding)) == null) {
            in.close();
            in = null;
            return false;
        }

        // TODO check, if the encoding (\inputencoding) is changend
        // a change of inputen coding is active on the next line after
        // the primitive
        return true;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#getLineno()
     */
    protected int getLineno() {

        return (in == null ? 0 : in.getLineNumber());
    }
}
