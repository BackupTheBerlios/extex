/*
 * Copyright (C) 2003-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.type.glue;

import java.io.Serializable;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.UndefinedControlSequenceException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;

/**
 * This class provides the basic data type of a stretchable and shrinkable
 * quantity of length.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.27 $
 */
public class Glue implements Serializable, FixedGlue {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

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
     * Creates a new object with a fixed length.
     *
     * @param theLength the natural length
     */
    public Glue(final FixedDimen theLength) {

        super();
        this.length = theLength.copy();
    }

    /**
     * Creates a new object as copy of another glue.
     *
     * @param glue the glue to clone
     */
    public Glue(final FixedGlue glue) {

        super();
        this.length = glue.getLength().copy();
        this.stretch = glue.getStretch().copy();
        this.shrink = glue.getShrink().copy();
    }

    /**
     * Creates a new object from the three components.
     *
     * @param theLength the natural length
     * @param theStretch the stretch specification
     * @param theShrink the shrink specification
     */
    public Glue(final FixedGlueComponent theLength,
            final FixedGlueComponent theStretch,
            final FixedGlueComponent theShrink) {

        super();
        this.length = theLength.copy();
        this.stretch = theStretch.copy();
        this.shrink = theShrink.copy();
    }

    /**
     * Creates a new object from a fixed length.
     *
     * @param theLength the natural length in scaled point
     */
    public Glue(final long theLength) {

        super();
        this.length = new GlueComponent(theLength);
    }

    /**
     * Creates a new object by parsing a token source.
     *
     * @param source the source to read new tokens from
     * @param context the processing context
     * @param typesetter the typesetter
     *
     * @throws InterpreterException in case of an error
     */
    public Glue(final TokenSource source, final Context context,
            final Typesetter typesetter) throws InterpreterException {

        super();
        Token t = source.getToken(context);
        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code instanceof GlueConvertible) {
                Glue g = ((GlueConvertible) code).convertGlue(context, source,
                        null);
                this.length = g.getLength().copy();
                this.shrink = g.getShrink().copy();
                this.stretch = g.getStretch().copy();
                return;
            } else if (code == null) {
                throw new UndefinedControlSequenceException(AbstractCode
                        .printable(context, t));
            }
        }
        source.push(t);
        this.length = new GlueComponent(context, source, typesetter, false);
        if (source.getKeyword(context, "plus")) {
            this.stretch = new GlueComponent(context, source, typesetter, true);
        }
        if (source.getKeyword(context, "minus")) {
            this.shrink = new GlueComponent(context, source, typesetter, true);
        }
        return;
    }

    /**
     * Add another glue to this one.
     * The addition is performed independently on the components.
     *
     * @param g the glue to add
     */
    public void add(final FixedGlue g) {

        this.length.add(g.getLength());
        this.stretch.add(g.getStretch());
        this.shrink.add(g.getShrink());
    }

    /**
     * Add a dimen to this one glue.
     * The addition is performed independently on the components.
     *
     * @param g the glue to add
     */
    public void add(final FixedGlueComponent g) {

        this.length.add(g);
    }

    /**
     * Make a copy of this object.
     *
     * @return a new instance with the same internal values
     */
    public Glue copy() {

        return new Glue(length.copy(), stretch.copy(), shrink.copy());
    }

    /**
     * Test that the given Glue is equal to a given one.
     *
     * @param glue the glue to compare with
     *
     * @return <code>true</code> iff they are different
     *
     * @see de.dante.extex.interpreter.type.glue.FixedGlue#eq(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public boolean eq(final FixedGlue glue) {

        return length.eq(glue.getLength()) && stretch.eq(glue.getStretch())
                && shrink.eq(glue.getShrink());
    }

    /**
     * Compare this value with a given glue and return <code>true</code> iff
     * the current length is greater or equal than the given length.
     *
     * @param x the value to compare to
     *
     * @return <code>true</code> iff the current length is greater or equal
     *  than the given one
     */
    public boolean ge(final FixedGlueComponent x) {

        return this.length.ge(x);
    }

    /**
     * Getter for the length.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the length of the glue.
     *
     * @return the natural length
     *
     * @see de.dante.extex.interpreter.type.glue.FixedGlue#getLength()
     */
    public FixedDimen getLength() {

        return new Dimen(length.getValue());
    }

    /**
     * Getter for shrink.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the shrink of the glue.
     *
     * @return the shrink.
     *
     * @see de.dante.extex.interpreter.type.glue.FixedGlue#getShrink()
     */
    public FixedGlueComponent getShrink() {

        return (FixedGlueComponent) shrink;
    }

    /**
     * Getter for stretch.
     * Note that the value returned is independent from the original object.
     * Changing its value does not affect the stretch of the glue.
     *
     * @return the stretch.
     *
     * @see de.dante.extex.interpreter.type.glue.FixedGlue#getStretch()
     */
    public FixedGlueComponent getStretch() {

        return (FixedGlueComponent) stretch;
    }

    /**
     * Compare this value with a given glue and return <code>true</code> iff
     * the current length is greater than the given length.
     *
     * @param x the value to compare to
     *
     * @return <code>true</code> iff the current length is greater than the
     *  given one
     */
    public boolean gt(final FixedGlueComponent x) {

        return this.length.gt(x);
    }

    /**
     * Compare this value with a given glue and return <code>true</code> iff
     * the current length is less or equal than the given length.
     *
     * @param x the value to compare to
     *
     * @return <code>true</code> iff the current length is less or equal
     *  than the given one
     */
    public boolean le(final FixedGlueComponent x) {

        return this.length.le(x);
    }

    /**
     * Compare this value with a given glue and return <code>true</code> iff
     * the current length is less than the given length.
     *
     * @param x the value to compare to
     *
     * @return <code>true</code> iff the current length is less than the
     *  given one
     */
    public boolean lt(final FixedGlueComponent x) {

        return this.length.lt(x);
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
     * Multiply all components by an integer fraction.
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiplyAll(final long nom, final long denom) {

        this.length.multiply(nom, denom);
        this.shrink.multiply(nom, denom);
        this.stretch.multiply(nom, denom);
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

        this.shrink.multiply(nom, denom);
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

        this.stretch.multiply(nom, denom);
    }

    /**
     * @see de.dante.extex.interpreter.type.glue.FixedGlue#ne(
     *      de.dante.extex.interpreter.type.glue.FixedGlue)
     */
    public boolean ne(final FixedGlue glue) {

        return length.ne(glue.getLength()) || stretch.ne(glue.getStretch())
                || shrink.ne(glue.getShrink());
    }

    /**
     * Set the glue value to a non-stretchable and non-shrinkable length.
     *
     * @param theLength the new length
     */
    public void set(final FixedDimen theLength) {

        this.length.set(theLength);
        this.shrink.set(0);
        this.stretch.set(0);
    }

    /**
     * Set the glue value.
     *
     * @param theLength the new length
     */
    public void set(final FixedGlue theLength) {

        this.length.set(theLength.getLength());
        this.shrink.set(theLength.getShrink());
        this.stretch.set(theLength.getStretch());
    }

    /**
     * Subtract a Glue component from this glue.
     * The subtraction is performed on the length only.
     *
     * @param g the glue to subtract
     */
    public void subtract(final FixedGlueComponent g) {

        this.length.subtract(g);
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the skip register.
     *
     * @return the string representation of this glue
     * @see "<logo>TeX</logo> &ndash; The Program [178,177]"
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
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the skip register.
     *
     * @param factory the factory to get the tokens from
     *
     * @return the string representation of this glue
     *
     * @throws GeneralException in case of an error
     *
     * @see "<logo>TeX</logo> &ndash; The Program [178,177]"
     */
    public Tokens toToks(final TokenFactory factory) throws GeneralException {

        Tokens toks = length.toToks(factory);

        if (stretch.getValue() != 0) {
            toks.add(factory, " plus ");
            stretch.toToks(toks, factory, 'p', 't');
        }
        if (shrink.getValue() != 0) {
            toks.add(factory, " minus ");
            shrink.toToks(toks, factory, 'p', 't');
        }
        return toks;
    }

}
