/*
 * Copyright (C) 2003  Gerd Neugebauer
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
package de.dante.extex.interpreter.primitives.file;

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

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class provides an implementation for the primitive <code>\input</code>.
 *
 * Example
 * <pre>
 * \input file.name
 * \input {file.name}
 * </pre>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
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
     * @see de.dante.extex.interpreter.Code#expand(de.dante.extex.interpreter.Flags, de.dante.extex.interpreter.context.Context, de.dante.extex.interpreter.TokenSource, de.dante.extex.typesetter.Typesetter)
     */
    public void expand(Flags prefix, Context context,
                       TokenSource source, Typesetter typesetter)
                throws GeneralException {
        String name                = scanFileName(source);
        TokenStreamFactory factory = source.getTokenStreamFactory();

        try {
            source.addStream(factory.newInstance(factory.findFile(name,
                                                                  "tex"),
                                                 "ISO-8859-1"));//TODO encoding
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
     * ...
     *
     * @param source the source for new tokens
     *
     * @return the file name as string
     *
     * @throws GeneralException in case of an error
     */
    private String scanFileName(TokenSource source)
                         throws GeneralException {
        Token t = source.scanNextNonSpace();

        if (t == null) {
            throw new GeneralHelpingException("EOF"); //TODO
        }

        StringBuffer sb = new StringBuffer(t.getValue());

        for (t = source.scanNextToken();
                 t != null && !(t instanceof SpaceToken);
                 t = source.scanNextToken()) {
            sb.append(t.getValue());
        }

        return sb.toString();
    }
}
