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
package de.dante.extex.interpreter.primitives.file;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.dante.extex.i18n.GeneralHelpingException;
import de.dante.extex.interpreter.AbstractCode;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.scanner.SpaceToken;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;

/**
 * This class provides an implementation for the primitive <code>\input</code>.
 * It use the standardencoding (see <code>\inputencoding</code> and <code>extex.encoding</code>.
 * 
 * Example:
 * 
 * <pre>
 *  \input file.name
 * </pre>
 * 
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.6 $
 */
public class Input extends AbstractCode {

	/**
	 * Creates a new object.
	 * 
	 * @param name the name for debugging
	 */
	public Input(String name) {
		super(name);
	}

	/**
	 * Scan the filename (until a <code>SpaceToken</code>) and 
	 * put the file on the tokenizerstream.
	 *  
	 * @see de.dante.extex.interpreter.Code#execute(de.dante.extex.interpreter.Flags,
	 *      de.dante.extex.interpreter.context.Context,
	 *      de.dante.extex.interpreter.TokenSource,
	 *      de.dante.extex.typesetter.Typesetter)
	 */
	public void execute(Flags prefix, Context context, TokenSource source, Typesetter typesetter) throws GeneralException {
		String name = scanFileName(source);
		TokenStreamFactory factory = source.getTokenStreamFactory();
		String encoding = getEncoding(context);

		try {
			source.addStream(factory.newInstance(factory.findFile(name, "tex"), encoding));
		} catch (FileNotFoundException e) {
			throw new GeneralException(e);
		} catch (ConfigurationException e) {
			throw new GeneralException(e);
		} catch (IOException e) {
			throw new GeneralException(e);
		}
		prefix.clear();
	}

	/**
	 * Return the encoding for the inputfile
	 * <p>
	 * First of all, <code>\inputencoding</code> is use, if there is no
	 * value, then the property <code>extex.encoding</code> is use or <code>ISO8859-1</code>,
	 * if no entry exists.
	 * 
	 * @param context the context
	 * @return the encoding for the inputfile
	 */
	protected String getEncoding(Context context) {
		String encoding = context.getToks("inputencoding").toText().trim();
		if (encoding.length() == 0) {
			String enc = System.getProperty("extex.encoding");
			if (enc != null) {
				encoding = enc;
			} else {
				encoding = "ISO8859-1";
			}
		}
		return encoding;
	}

	/**
	 * scan the filename until a <code>SpaceToken</code>.
	 * 
	 * @param source the source for new tokens
	 * @return the file name as string
	 * @throws GeneralException in case of an error
	 */
	protected String scanFileName(TokenSource source) throws GeneralException {
		Token t = source.scanNonSpace();

		if (t == null) {
			throw new GeneralHelpingException("EOF"); //TODO
		}

		StringBuffer sb = new StringBuffer(t.getValue());

		for (t = source.scanToken(); t != null && !(t instanceof SpaceToken); t = source.scanToken()) {
			sb.append(t.getValue());
		}

		return sb.toString();
	}
}
