/*
 * Copyright (C) 2004 Gerd Neugebauer, Michael Niedermair
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

import de.dante.util.Locator;

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
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class TokenStreamImpl extends TokenStreamBufferImpl {
	
	/** 
	 * the decoder for the input stream 
	 */
	private CharsetDecoder decoder;

	/** 
	 * the reader to get the input from 
	 */
	private LineNumberReader reader;

	/** 
	 * the last input line as it has been read 
	 */
	private String line;

	/** 
	 * the name of the source to read from 
	 */
	private String source;

	/**
	 * indicate, if the stream is a filestrem
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
	 * @param line
	 *                 a string to read from
	 * @param encoding
	 *                 the encoding to use
	 * 
	 * @throws CharacterCodingException
	 *                 in case of an error
	 */
	public TokenStreamImpl(String line, String encoding) throws CharacterCodingException {
		super(line, encoding);
	}

	/**
	 * Creates a new object.
	 * 
	 * @param filename
	 *                 the name of the file to read
	 */
	public TokenStreamImpl(File filename, String encoding) throws FileNotFoundException, IOException {
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
	 * @param reader
	 *                 the source reader
	 * 
	 * @throws IOException
	 *                 in case of an IO error
	 */
	public TokenStreamImpl(Reader reader, String encoding) throws IOException {
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
	 * @see de.dante.extex.scanner.stream.TokenStream#getLocator()
	 */
	public Locator getLocator() {
		return new Locator(source, (reader == null ? 0 : reader.getLineNumber()));
	}

	/**
	 * @see de.dante.extex.scanner.stream.TokenStream#closeFileStream()
	 */
	public boolean closeFileStream() {
		super.closeFileStream();

		return true;
	}

	/**
	 * Get a new bunch of characters.
	 * 
	 * @return a new buffer of characters read or <code>null</code> at end of
	 *            file
	 * 
	 * @return <code>false</code> iff no more character is available
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
		
		// TODO überprüfen, ob sich das Encoding geändert hat (durch \inputencoding)
		
		// System.err.println("line [" + reader.getLineNumber() + "] :" + line + ":");

		// add '\r' for EOL 
		line = line + '\r';
		
		CharBuffer buffer = decoder.decode(ByteBuffer.wrap(line.getBytes()));
		setBuffer(buffer);
		pointer = 0;
		state=NEW_LINE;
		return true;
	}
}
