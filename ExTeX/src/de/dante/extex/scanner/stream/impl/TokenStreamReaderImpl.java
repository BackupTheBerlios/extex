/*
 * Copyright (C) 2003-2004 The ExTeX Group
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

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import de.dante.extex.scanner.CatcodeVisitor;
import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.UnicodeChar;

/**
 * This class contains an implementation of a token stream which is fed from a
 * Reader.
 * <p>
 * The class ignore the encoding in <tt>\inputencoding</tt>!
 * 
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.1 $
 */
public class TokenStreamReaderImpl extends AbstractTokenStreamImpl implements TokenStream, CatcodeVisitor {
	
	/**
	 * the source
	 */
	private String source;
	
	/**
	 * the reader
	 */
	private LineNumberReader in;

	/**
	 * the line
	 */
	private String line;

	/**
	 * Creates a new object.
	 * @param	reader	the reader 
	 */
	public TokenStreamReaderImpl(final Reader reader) throws IOException {
		super();
		in = new LineNumberReader(reader);
		this.source = reader.toString();
		refill();
	}

	/**
	 * @see de.dante.extex.scanner.stream.impl.AbstractTokenStreamImpl#bufferLength()
	 */
	protected int bufferLength() {
		return line.length();
	}

	/**
	 * @see de.dante.extex.scanner.stream.impl.AbstractTokenStreamImpl#getSingleChar()
	 */
	protected UnicodeChar getSingleChar() {
		return new UnicodeChar(line,pointer);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return line;
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
		if ((line = in.readLine()) == null) {
			in.close();
			in = null;
			return false;
		}
		return true;
	}
	
	/**
	 * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#getLineno()
	 */
	protected int getLineno() {
		return (in == null ? 0 : in.getLineNumber());
	}		
}
