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

package de.dante.extex.scanner.type;

import de.dante.extex.interpreter.Namespace;
import de.dante.util.UnicodeChar;

/**
 * This class represents a control sequence token.
 * <p>
 * This class has a protected constructor only. Use the factory
 * {@link de.dante.extex.scanner.type.TokenFactory TokenFactory}
 * to get an instance of this class.
 * </p>
 *
 * <p>
 *  Note that in contrast to TeX the escape character leading to this control
 *  sequence token is stord in the character code of the abstract base class.
 * </p>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.2 $
 */
public class ControlSequenceToken extends AbstractToken implements CodeToken {

    /**
     * The constant <tt>HASH_FACTOR</tt> contains the factor used to construct
     * the hash code.
     */
    private static final int HASH_FACTOR = 17;

    /**
     * The field <tt>value</tt> contains the string value.
     */
    private String name;

    /**
     * The field <tt>namespace</tt> contains the namespace for this token.
     */
    private String namespace;

    /**
     * Creates a new object from the first character of a String.
     * If the string is empty then a space character is used instead.
     *
     * @param esc the escape character
     * @param name the name of the control sequence -- without the leading
     *  escape character token
     * @param namespace the namespace
     */
    protected ControlSequenceToken(final UnicodeChar esc, final String name,
            final String namespace) {

        super(esc);
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Create a new instance of the token where the namespace is the default
     * namespace and the other attributes are the same as for the current token.
     *
     * @return the new token
     *
     * @see de.dante.extex.scanner.type.CodeToken#cloneInDefaultNamespace()
     */
    public CodeToken cloneInDefaultNamespace() {

        if (Namespace.DEFAULT_NAMESPACE.equals(namespace)) {
            return this;
        }
        return new ControlSequenceToken(getChar(), name,
                Namespace.DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of the token where the namespace is the given one
     * and the other attributes are the same as for the current token.
     *
     * @param theNamespace the namespace to use
     *
     * @return the new token
     *
     * @see de.dante.extex.scanner.type.CodeToken#cloneInNamespace(java.lang.String)
     */
    public CodeToken cloneInNamespace(final String theNamespace) {

        if (theNamespace == null || namespace.equals(theNamespace)) {
            return this;
        }
        return new ControlSequenceToken(getChar(), name, theNamespace);
    }

    /**
     * @see de.dante.extex.scanner.type.Token#equals(
     *      de.dante.extex.scanner.Catcode, java.lang.String)
     */
    public boolean equals(final Catcode cc, final String s) {

        return getCatcode() == cc && name.equals(s);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object other) {

        if (other instanceof ControlSequenceToken) {
            ControlSequenceToken othertoken = (ControlSequenceToken) other;
            return (name.equals(othertoken.getName()) //
            && namespace.equals(othertoken.namespace));
        }
        return false;
    }

    /**
     * @see de.dante.extex.scanner.type.Token#getCatcode()
     */
    public Catcode getCatcode() {

        return Catcode.ESCAPE;
    }

    /**
     * @see de.dante.extex.scanner.type.CodeToken#getName()
     */
    public String getName() {

        return name;
    }

    /**
     * @see de.dante.extex.scanner.type.CodeToken#getNamespace()
     */
    public String getNamespace() {

        return namespace;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return name.hashCode() + HASH_FACTOR * namespace.hashCode();
    }

    /**
     * Get the string representation of this object for debugging purposes.
     *
     * @return the string representation
     */
    public String toString() {

        return getLocalizer().format("ControlSequenceToken.Text", name);
    }

    /**
     * Print the token into a StringBuffer.
     *
     * @param sb the target string buffer
     *
     * @see de.dante.extex.scanner.type.Token#toString(java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        sb.append(getLocalizer().format("ControlSequenceToken.Text", name));
    }

    /**
     * @see de.dante.extex.scanner.type.Token#toText(char)
     */
    public String toText(final char esc) {

        StringBuffer sb = new StringBuffer();
        sb.append(esc);
        sb.append(name);
        return sb.toString();
    }

    /**
     * @see de.dante.extex.scanner.type.Token#visit(
     *      de.dante.extex.scanner.TokenVisitor,
     *      java.lang.Object)
     */
    public Object visit(final TokenVisitor visitor, final Object arg1)
            throws Exception {

        return visitor.visitEscape(this, arg1);
    }

}