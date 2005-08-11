/*
 * Copyright (C) 2003-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.scanner.type.token;

import de.dante.extex.interpreter.Namespace;
import de.dante.extex.scanner.type.Catcode;
import de.dante.util.UnicodeChar;

/**
 * This class represents an active character token.
 * The active character token is characterized by its name.
 * This name is a single letter string.
 * <p>
 * This class has a protected constructor only. Use the factory
 * {@link de.dante.extex.scanner.type.token.TokenFactory TokenFactory}
 * to get an instance of this class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class ActiveCharacterToken extends AbstractToken implements CodeToken {

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
     * @param uc the string value
     * @param theNamespace the namespace
     */
    protected ActiveCharacterToken(final UnicodeChar uc,
            final String theNamespace) {

        super(uc);
        namespace = theNamespace;
    }

    /**
     * @see de.dante.extex.scanner.type.token.CodeToken#cloneInDefaultNamespace()
     */
    public CodeToken cloneInDefaultNamespace() {

        if (Namespace.DEFAULT_NAMESPACE.equals(namespace)) {
            return this;
        }
        return new ActiveCharacterToken(getChar(), Namespace.DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of the token where the namespace is the given one.
     *
     * @param theNamespace the namespace to use
     *
     * @return the new token
     *
     * @see de.dante.extex.scanner.type.token.CodeToken#cloneInNamespace(java.lang.String)
     */
    public CodeToken cloneInNamespace(final String theNamespace) {

        if (theNamespace == null || namespace.equals(theNamespace)) {
            return this;
        }
        return new ActiveCharacterToken(getChar(), theNamespace);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object other) {

        if (other == null || !(other instanceof ActiveCharacterToken)) {
            return false;
        }
        ActiveCharacterToken othertoken = (ActiveCharacterToken) other;
        return (super.equals(other) && namespace.equals(othertoken.namespace));
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#getCatcode()
     */
    public Catcode getCatcode() {

        return Catcode.ACTIVE;
    }

    /**
     * @see de.dante.extex.scanner.type.token.CodeToken#getName()
     */
    public String getName() {

        return "";
    }

    /**
     * @see de.dante.extex.scanner.type.token.CodeToken#getNamespace()
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

        return getLocalizer().format("ActiveCharacterToken.Text", getName());
    }

    /**
     * Print the token into a StringBuffer.
     *
     * @param sb the target string buffer
     *
     * @see de.dante.extex.scanner.type.token.Token#toString(java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        sb.append(getLocalizer().format("ActiveCharacterToken.Text",
                        getName()));
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#visit(
     *      de.dante.extex.scanner.type.TokenVisitor,
     *      java.lang.Object)
     */
    public Object visit(final TokenVisitor visitor, final Object arg1)
            throws Exception {

        return visitor.visitActive(this, arg1);
    }
}