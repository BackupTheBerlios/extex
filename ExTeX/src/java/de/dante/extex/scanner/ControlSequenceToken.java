/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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
package de.dante.extex.scanner;

import de.dante.extex.i18n.Messages;


/**
 * This class represents a control sequence token.
 * <p>
 * This class has a protected constructor only. Use the factory
 * {@link de.dante.extex.scanner.TokenFactory TokenFactory}
 * to get an instance of this class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class ControlSequenceToken extends AbstractToken implements CodeToken {

    /**
     * Creates a new object.
     *
     * @param value the name of the control sequence -- without the leading
     * escape character token
     */
    protected ControlSequenceToken(final String value) {
        super(value);
    }

    /**
     * @see de.dante.extex.scanner.Token#getCatcode()
     */
    public Catcode getCatcode() {
        return Catcode.ESCAPE;
    }

    /**
     * Get the string representation of this object for debugging purposes.
     *
     * @return the string representation
     */
    public String toString() {
        return Messages.format("ControlSequenceToken.Text", getValue());
    }

}
