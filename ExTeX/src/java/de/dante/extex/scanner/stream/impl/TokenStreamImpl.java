/*
 * Copyright (C) 2003-2004 Gerd Neugebauer, Michael Niedermair
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
package de.dante.extex.scanner.stream.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * This class represents a token stream which is optionally fed from a file.
 * First of all the tokens stored locally are used. They are inherited from the
 * class TokenStreamBaseImpl. If they are used up then the file stream is used
 * to extract token by token. This action is performed in the superclass
 * TokenStreamStringImpl.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.9 $
 */
public class TokenStreamImpl extends TokenStreamBufferImpl {

    /**
     * The field <tt>decoder</tt> contains the decoder for the input stream.
     */
    private CharsetDecoder decoder;

    /**
     * The field <tt>reader</tt> contains the reader to get the input from.
     */
    private LineNumberReader reader;

    /**
     * The field <tt>line</tt> contains the last input line as it has been read.
     */
    private String line;

    /**
     * The field <tt>source</tt> contains the name of the source to read from.
     */
    private String source;

    /**
     * The field <tt>isFileStream</tt> contains the indicator whether the stream
     * is a file stream.
     */
    private boolean isFileStream = false;

     /**
      * Creates a new object.
      */
     public TokenStreamImpl() {
          super();
     }

    /**
     * Creates a new object.
     *
     * @param line a string to read from
     * @param encoding the encoding to use
     *
     * @throws CharacterCodingException in case of an error
     */
    public TokenStreamImpl(final String line, final String encoding)
            throws CharacterCodingException {
        super(line, encoding);
    }

    /**
     * Creates a new object.
     * 
     * @param filename the name of the file to read
     * @param encoding the name of the encoding to use
     */
    public TokenStreamImpl(final File filename, final String encoding)
            throws FileNotFoundException, IOException {
        super("", encoding);
        this.source = filename.getPath();
        isFileStream = true;
        reader = new LineNumberReader(new FileReader(filename));
        decoder = Charset.forName(encoding).newDecoder();
        refill();
    }

    /**
     * Creates a new object.
     *
     * @param reader the source reader
     * @param encoding the name of the encoding to use
     *
     * @throws IOException in case of an IO error
     */
    public TokenStreamImpl(final Reader reader, final String encoding)
        throws IOException {
        super("", encoding);
        this.source = reader.toString();
        this.reader = new LineNumberReader(reader);
        decoder = Charset.forName(encoding).newDecoder();
        refill();
    }

     /**
      * @see de.dante.extex.scanner.stream.TokenStream#isFileStream()
      */
     public boolean isFileStream() {
          return isFileStream;
     }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#getSource()
     */
    protected String getSource() {
        return source;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#getLineno()
     */
    protected int getLineno() {
        return (reader == null ? 0 : reader.getLineNumber());
    }

     /**
      * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
      */
     public boolean closeFileStream() {
          super.closeFileStream();

          return true;
     }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#refill()
     */
    protected boolean refill() throws IOException {
        if (reader == null) {
            return false;
        }

        if ((line = reader.readLine()) == null) {
            reader.close();
            reader = null;
            return false;
        }

        // TODO check, if the encoding (\inputencoding) is changend
        // a change of inputen coding is active on the next line after
        // the primitive

        // System.err.println("line [" + reader.getLineNumber() + "] :" + line
        // + ":");

        // add '\r' for EOL
        line = line + '\r';

        CharBuffer buffer = decoder.decode(ByteBuffer.wrap(line.getBytes()));
        setBuffer(buffer);
        return true;
    }

}
