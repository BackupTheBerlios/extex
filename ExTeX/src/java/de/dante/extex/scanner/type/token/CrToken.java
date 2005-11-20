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

import de.dante.extex.scanner.type.Catcode;
import de.dante.util.UnicodeChar;

/**
 * This class represents a cr token.
 * <p>
 * This class has a protected constructor only. Use the factory
 * {@link de.dante.extex.scanner.type.token.TokenFactory TokenFactory}
 * to get an instance of this class.
 * </p>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.4 $
 */
public class CrToken extends AbstractToken implements Token {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param value the string value
     */
    protected CrToken(final UnicodeChar value) {

        super(value);
    }

    /**
     * @see de.dante.extex.scanner.type.token.AbstractToken#equals(de.dante.extex.scanner.type.Catcode, char)
     */
    public boolean equals(final Catcode cc, final char c) {

        return cc == getCatcode();
    }

    /**
     * @see de.dante.extex.scanner.type.token.AbstractToken#equals(de.dante.extex.scanner.type.Catcode, java.lang.String)
     */
    public boolean equals(final Catcode cc, final String s) {

        return cc == getCatcode();
    }

    /**
     * @see de.dante.extex.scanner.type.token.AbstractToken#equals(char)
     */
    public boolean equals(char c) {

        return false;
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#getCatcode()
     */
    public Catcode getCatcode() {

        return Catcode.CR;
    }

    /**
     * Get the string representation of this object for debugging purposes.
     *
     * @return the string representation
     *
     * @see "<logo>TeX</logo> &ndash; The Program [298]"
     */
    public String toString() {

        UnicodeChar c = getChar();
        return getLocalizer().format("CrToken.Text",
                (c != null ? c.toString() : ""));
    }

    /**
     * Print the token into a StringBuffer.
     *
     * @param sb the target string buffer
     *
     * @see de.dante.extex.scanner.type.token.Token#toString(java.lang.StringBuffer)
     */
    public void toString(final StringBuffer sb) {

        UnicodeChar c = getChar();
        sb.append(getLocalizer().format("CrToken.Text",
                (c != null ? c.toString() : "")));
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#toText()
     */
    public String toText() {

        return "[]";
    }

    /**
     * @see de.dante.extex.scanner.type.token.Token#visit(
     *      de.dante.extex.scanner.type.token.TokenVisitor,
     *      java.lang.Object)
     */
    public Object visit(final TokenVisitor visitor, final Object arg1)
            throws Exception {

        return visitor.visitCr(this, arg1);
    }

}