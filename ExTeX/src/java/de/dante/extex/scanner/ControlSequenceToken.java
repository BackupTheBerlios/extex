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
import de.dante.extex.interpreter.Namespace;

/**
 * This class represents a control sequence token.
 * <p>
 * This class has a protected constructor only. Use the factory
 * {@link de.dante.extex.scanner.TokenFactory TokenFactory}
 * to get an instance of this class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public class ControlSequenceToken extends AbstractToken implements CodeToken {

    /**
     * The constant <tt>HASH_FACTOR</tt> contains the factor used to construct
     * the hash code.
     */
    private static final int HASH_FACTOR = 17;

    /**
     * The field <tt>namespace</tt> contains the namespace for this token.
     */
    private String namespace;

    /**
     * Creates a new object.
     *
     * @param value the name of the control sequence -- without the leading
     * escape character token
     * @param theNamespace the namespace
     */
    protected ControlSequenceToken(final String value, final String theNamespace) {

        super(value);
        namespace = theNamespace;
    }

    /**
     * @see de.dante.extex.scanner.CodeToken#cloneInDefaultNamespace()
     */
    public Token cloneInDefaultNamespace() {

        if (Namespace.DEFAULT_NAMESPACE.equals(namespace)) {
            return this;
        }
        return new ControlSequenceToken(getValue(), Namespace.DEFAULT_NAMESPACE);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object other) {

        if (other == null || !(other instanceof ControlSequenceToken)) {
            return false;
        }
        ControlSequenceToken othertoken = (ControlSequenceToken) other;
        return (super.equals(other) && namespace.equals(othertoken.namespace));
    }

    /**
     * @see de.dante.extex.scanner.Token#getCatcode()
     */
    public Catcode getCatcode() {

        return Catcode.ESCAPE;
    }

    /**
     * @see de.dante.extex.scanner.CodeToken#getNamespace()
     */
    public String getNamespace() {

        return namespace;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return super.hashCode() + HASH_FACTOR * namespace.hashCode();
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