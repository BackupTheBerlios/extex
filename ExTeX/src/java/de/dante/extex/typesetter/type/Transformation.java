/*
 * Copyright (C) 2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type;

/**
 * TODO gene: missing JavaDoc.
 *
 *
 * <table>
 *  <tr><td>a</td>  <td>b</td>  <td>0</td> </tr>
 *  <tr><td>c</td>  <td>d</td>  <td>0</td> </tr>
 *  <tr><td>tx</td> <td>ty</td> <td>1</td> </tr>
 * </table>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class Transformation {

    /**
     * The field <tt>a</tt> contains the ...
     */
    private float a;

    /**
     * The field <tt>b</tt> contains the ...
     */
    private float b;

    /**
     * The field <tt>c</tt> contains the ...
     */
    private float c;

    /**
     * The field <tt>d</tt> contains the ...
     */
    private float d;

    /**
     * The field <tt>tx</tt> contains the ...
     */
    private float tx;

    /**
     * The field <tt>ty</tt> contains the ...
     */
    private float ty;

    /**
     * Creates a new object.
     */
    public Transformation() {

        super();
    }

    /**
     * Getter for a.
     *
     * @return the a
     */
    public String getA() {

        return Float.toString(this.a);
    }

    /**
     * Getter for b.
     *
     * @return the b
     */
    public String getB() {

        return Float.toString(this.b);
    }

    /**
     * Getter for c.
     *
     * @return the c
     */
    public String getC() {

        return Float.toString(this.c);
    }

    /**
     * Getter for d.
     *
     * @return the d
     */
    public String getD() {

        return Float.toString(this.d);
    }

    /**
     * Getter for tx.
     *
     * @return the tx
     */
    public String getTx() {

        return Float.toString(this.tx);
    }

    /**
     * Getter for ty.
     *
     * @return the ty
     */
    public String getTy() {

        return Float.toString(this.ty);
    }
}
