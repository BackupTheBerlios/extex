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

package de.dante.extex.interpreter.max;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.scanner.Token;
import de.dante.extex.scanner.stream.TokenStreamFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;
import de.dante.util.configuration.ConfigurationException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class StringSource extends Moritz implements TokenSource {

    /**
     * Creates a new object.
     *
     * @param factory ...
     * @param s ...
     *
     * @throws ConfigurationException ...
     */
    public StringSource(final TokenStreamFactory factory, final String s)
            throws ConfigurationException {

        super(null);
        addStream(factory.newInstance(s));
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#getTypesetter()
     */
    public Typesetter getTypesetter() {

        return null;
    }

    /**
     * @see de.dante.extex.interpreter.max.Moritz#expand(de.dante.extex.scanner.Token)
     */
    protected Token expand(final Token token) throws GeneralException {

        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see de.dante.extex.interpreter.TokenSource#executeGroup()
     */
    public void executeGroup() throws GeneralException {

        // TODO Auto-generated method stub

    }

}