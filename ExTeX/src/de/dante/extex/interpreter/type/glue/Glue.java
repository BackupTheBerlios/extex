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
package de.dante.extex.interpreter.type.glue;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.TokenFactory;
import de.dante.util.GeneralException;

/**
 * ...
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.4 $
 */
public class Glue implements Serializable, FixedGlue {

    /**
     * The field <tt>length</tt> contains the natural length of the glue.
     */
    private GlueComponent length = new GlueComponent(0);

    /**
     * The field <tt>shrink</tt> contains the shrink specification.
     */
    private GlueComponent shrink = new GlueComponent(0);

    /**
     * The field <tt>stretch</tt> contains the stretch specification.
     */
    private GlueComponent stretch = new GlueComponent(0);

    /**
     * Creates a new object.
     *
     * @param theLength the naturallength in scaled point
     */
    public Glue(final long theLength) {

        super();
        this.length = new GlueComponent(theLength);
    }

    /**
     * Creates a new object.
     *
     * @param theLength the natural length
     * @param theStretch the stretch specification
     * @param theShrink the shrink specification
     */
    public Glue(final GlueComponent theLength, final GlueComponent theStretch,
            final GlueComponent theShrink) {

        super();
        this.length = theLength.copy();
        this.stretch = theStretch.copy();
        this.shrink = theShrink.copy();
    }

    /**
     * Creates a new object.
     *
     * @param theLength the natural length
     */
    public Glue(final Dimen theLength) {

        super();
        this.length = theLength.copy();
    }

    /**
     * Creates a new object.
     *
     * @param source the source to read new tokens from
     * @param context the processing context
     *
     * @throws GeneralException in case of an error
     */
    public Glue(final TokenSource source, final Context context)
            throws GeneralException {

        super();
        this.length = new GlueComponent(context, source, false);
        if (source.getKeyword("plus")) {
            this.stretch = new GlueComponent(context, source, true);
        }
        if (source.getKeyword("minus")) {
            this.shrink = new GlueComponent(context, source, true);
        }
    }

    /**
     * Getter for the length.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the length of the glue.
     *
     * @return the natural length
     */
    public Dimen getLength() {
        return new Dimen(length.getValue());
    }

    /**
     * Getter for shrink.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the shrink of the glue.
     *
     * @return the shrink.
     */
    public Dimen getShrink() {

        return new Dimen(shrink.getValue());
    }

    /**
     * Getter for stretch.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the stretch of the glue.
     *
     * @return the stretch.
     */
    public GlueComponent getStretch() {

        return new Dimen(stretch.getValue());
    }

    /**
     * Make a copy of this object.
     *
     * @return a new instance withe the same internal values
     */
    public Glue copy() {

        return new Glue(length.copy(), stretch.copy(), shrink.copy());
    }

    /**
     * Add another glue to this one.
     * The addition is perfomred indepentently on the components.
     *
     * @param g the glue to add
     */
    public void add(final Glue g) {

        this.length.add(g.getLength());
        this.stretch.add(g.getStretch());
        this.shrink.add(g.getShrink());
    }

    /**
     * Multiply the normal size by an integer fraction.
     * <p>
     *  <i>length</i> = <i>length</i> * <i>nom</i> / <i>denom</i>
     * </p>
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiply(final long nom, final long denom) {

        this.length.multiply(nom, denom);
    }

    /**
     * Multiply the stretch component by an integer fraction.
     * <p>
     *  <i>stretch</i> = <i>stretch</i> * <i>nom</i> / <i>denom</i>
     * </p>
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiplyStretch(final long nom, final long denom) {

        this.length.multiply(nom, denom);
    }

    /**
     * Multiply the shrink component by an integer fraction.
     * <p>
     *  <i>shrink</i> = <i>shrink</i> * <i>nom</i> / <i>denom</i>
     * </p>
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiplyShrink(final long nom, final long denom) {

        this.length.multiply(nom, denom);
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * TeX to print the skip register.
     *
     * @return the string representation of this glue
     * @see "TeX -- The Program [178,177]"
     */
    public String toString() {

        StringBuffer sb = new StringBuffer(length.toString());
        if (stretch.getValue() != 0) {
            sb.append(" plus ");
            sb.append(stretch.toString());
        }
        if (shrink.getValue() != 0) {
            sb.append(" minus ");
            sb.append(shrink.toString());
        }
        return sb.toString();
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactely the string which would be produced by
     * TeX to print the skip register.
     *
     * @param factory the factory to get the tokens from
     *
     * @return the string representation of this glue
     *
     * @throws GeneralException in case of an error
     *
     * @see "TeX -- The Program [178,177]"
     */
    public Tokens toToks(final TokenFactory factory) throws GeneralException {

        Tokens toks = length.toToks(factory);

        if (stretch.getValue() != 0) {
            toks.add(factory, " plus ");
            stretch.toToks(toks, factory);
        }
        if (shrink.getValue() != 0) {
            toks.add(factory, " minus ");
            shrink.toToks(toks, factory);
        }
        return toks;
    }

}
