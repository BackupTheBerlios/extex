/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.scanner.stream.impl;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;

import de.dante.extex.scanner.stream.TokenStream;
import de.dante.util.UnicodeChar;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de"> Gerd Neugebauer </a>
 *
 * @version $Revision: 1.5 $
 */
public class TokenStreamBuffersImpl extends AbstractTokenStreamImpl implements
        TokenStream {

	/**
	 * the line
	 */
	private String line;
	

	/**
	 * Creates a new object.
	 * @param	line	the line for the tokenizer 
	 */
	public TokenStreamBuffersImpl(final String line) {
		super();
		if (line != null) {
			this.line = line;
		} else {
			this.line="";
		}
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
     * The field <tt>nextLine</tt> ...
     */
    private int nextLine = 1;

    /**
     * The field <tt>lines</tt> ...
     */
    private String[] lines = null;

    /**
     * The field <tt>encoding</tt> contains the ...
     */
    // private String encoding; mgn: changed

    /**
     * Creates a new object.
     *
     * @param lines the array of lines to consider
     * @throws CharacterCodingException in cas of an error
     */
    public TokenStreamBuffersImpl(final String[] lines)
            throws CharacterCodingException {
        line = lines[0];
        this.lines = lines;
    }

    /**
     * @see de.dante.extex.scanner.stream.impl.TokenStreamBufferImpl#refill()
     */
    protected boolean refill() throws IOException {
    	super.refill();
        if (lines == null || nextLine >= lines.length) {
            return false;
        }
        line = lines[nextLine++];
        return true;
    }

}
