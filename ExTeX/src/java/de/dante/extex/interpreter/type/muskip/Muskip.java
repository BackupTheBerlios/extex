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

package de.dante.extex.interpreter.type.muskip;

import java.io.Serializable;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.glue.FixedGlueComponent;
import de.dante.extex.interpreter.type.glue.GlueComponent;
import de.dante.extex.interpreter.type.scaled.ScaledNumber;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.exception.GeneralException;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a skip value with a variable length of order 0.
 * The actual length is a multiple of math units (mu).
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.28 $
 */
public class Muskip extends Mudimen implements Serializable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object and fills it from a token stream.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the value parsed
     *
     * @throws InterpreterException in case of an error
     */
    public static Muskip parse(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token t;
        for (;;) {
            t = source.getNonSpace(context);
            if (t == null) {
                throw new EofException("mu");
            } else if (t instanceof CodeToken) {
                Code code = context.getCode((CodeToken) t);
                if (code instanceof MuskipConvertible) {
                    return ((MuskipConvertible) code).convertMuskip(context,
                            source, typesetter);
                } else if (code instanceof MudimenConvertible) {
                    long md = ((MudimenConvertible) code).convertMudimen(
                            context, source, typesetter);
                    return new Muskip(md);
                } else if (code instanceof ExpandableCode) {
                    ((ExpandableCode) code).expand(Flags.NONE, context, source,
                            typesetter);
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        if (t == null) {
            throw new EofException("mu");
        }
        long value = ScaledNumber.scanFloat(context, source, typesetter, t);
        if (!source.getKeyword(context, "mu")) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(Muskip.class), "TTP.IllegalMu");
        }
        Muskip ms = new Muskip(new Dimen(value));

        if (source.getKeyword(context, "plus")) {
            ms.stretch = scanMuOrFill(context, source, typesetter);
        }
        if (source.getKeyword(context, "minus")) {
            ms.shrink = scanMuOrFill(context, source, typesetter);
        }
        ms.kill = false;
        return ms;
    }

    /**
     * Scan a math unit.
     *
     * @param context the processor context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the number of scaled points for the mu
     *
     * @throws InterpreterException in case of an error
     */
    private static GlueComponent scanMuOrFill(final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException("mu");
        }
        long value = ScaledNumber.scanFloat(context, source, typesetter, t);
        if (source.getKeyword(context, "mu")) {
            return new GlueComponent(value);
        } else if (source.getKeyword(context, "fillll")) {
            return new GlueComponent(value, 5);
        } else if (source.getKeyword(context, "filll")) {
            return new GlueComponent(value, 4);
        } else if (source.getKeyword(context, "fill")) {
            return new GlueComponent(value, 3);
        } else if (source.getKeyword(context, "fil")) {
            return new GlueComponent(value, 2);
        } else if (source.getKeyword(context, "fi")) {
            return new GlueComponent(value, 1);
        }
        throw new HelpingException(LocalizerFactory.getLocalizer(Muskip.class),
                "TTP.IllegalMu");

    }

    /**
     * The field <tt>kill</tt> contains the indicator that the following glue
     * might be killed.
     */
    private boolean kill;

    /**
     * The field <tt>shrink</tt> contains the shrinkability specification.
     */
    private GlueComponent shrink = new GlueComponent(0);

    /**
     * The field <tt>stretch</tt> contains the stretchability specification.
     */
    private GlueComponent stretch = new GlueComponent(0);

    /**
     * Creates a new object.
     * All components are 0.
     */
    public Muskip() {

        super();
        kill = false;
    }

    /**
     * Creates a new object.
     * All components are 0.
     *
     * @param kill the kill indicator
     */
    public Muskip(final boolean kill) {

        super();
        this.kill = kill;
    }

    /**
     * Creates a new object.
     * Strechablity and shrinkability are 0.
     *
     * @param theLength the natural length
     */
    public Muskip(final FixedDimen theLength) {

        super(theLength.getValue());
        this.kill = false;
    }

    /**
     * Creates a new object.
     * Strechablity and shrinkability are 0.
     *
     * @param theLength the natural length
     */
    public Muskip(final long theLength) {

        super(theLength);
        this.kill = false;
    }

    /**
     * Creates a new object.
     *
     * @param theLength the natural length
     * @param theStretch the stretchability
     * @param theShrink the shrinkability
     */
    public Muskip(final FixedGlueComponent theLength,
            final FixedGlueComponent theStretch,
            final FixedGlueComponent theShrink) {

        super(theLength.getValue());
        this.stretch = theStretch.copy();
        this.shrink = theShrink.copy();
        this.kill = false;
    }

    /**
     * Creates a new object.
     *
     * @param x the other muskip
     */
    public Muskip(final Muskip x) {

        super(x.getLength().getValue());
        this.stretch = x.stretch.copy();
        this.shrink = x.shrink.copy();
        this.kill = false;
    }

    /**
     * Add another muglue to this one.
     * The addition is performed independently on the components.
     *
     * @param ms the muglue to add
     */
    public void add(final Muskip ms) {

        super.add(ms.getLength().getValue());
        this.stretch.add(ms.getStretch());
        this.shrink.add(ms.getShrink());
    }

    /**
     * Getter for shrink.
     *
     * @return the shrink
     */
    public GlueComponent getShrink() {

        return this.shrink;
    }

    /**
     * Getter for stretch.
     *
     * @return the stretch
     */
    public GlueComponent getStretch() {

        return this.stretch;
    }

    /**
     * Getter for kill.
     *
     * @return the kill
     */
    public boolean isKill() {

        return this.kill;
    }

    /**
     * Check that the muskip has natural length zero and no stretch and
     * shrink component.
     *
     * @return <code>true</code> iff the register is zero
     */
    public boolean isZero() {

        return super.isZero() && stretch.eq(GlueComponent.ZERO)
                && shrink.eq(GlueComponent.ZERO);
    }

    /**
     * Multiply all components by an integer fraction.
     *
     * @param nom nominator
     * @param denom denominator
     */
    public void multiply(final long nom, final long denom) {

        super.multiply(nom, denom);
        this.shrink.multiply(nom, denom);
        this.stretch.multiply(nom, denom);
    }

    /**
     * Setter for kill.
     *
     * @param kill the kill to set
     */
    public void setKill(final boolean kill) {

        this.kill = kill;
    }

    /**
     * Return the string representation of the instance.
     *
     * @return the string representation of this glue
     * @see "<logo>TeX</logo> &ndash; The Program [???]"
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Append the string representation of the instance to a string buffer.
     *
     * @param sb the target string buffer
     */
    public void toString(final StringBuffer sb) {

        super.toString(sb);
        if (stretch.ne(GlueComponent.ZERO)) {
            sb.append(" plus ");
            stretch.toString(sb, 'm', 'u');
        }
        if (shrink.ne(GlueComponent.ZERO)) {
            sb.append(" plus ");
            shrink.toString(sb, 'm', 'u');
        }
    }

    /**
     * Determine the printable representation of the object.
     * The value returned is exactly the string which would be produced by
     * <logo>TeX</logo> to print the muskip register.
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

        Tokens toks = new Tokens();
        super.toToks(toks, factory, 'm', 'u');

        if (stretch.getValue() != 0) {
            toks.add(factory, " plus ");
            stretch.toToks(toks, factory, 'm', 'u');
        }
        if (shrink.getValue() != 0) {
            toks.add(factory, " minus ");
            shrink.toToks(toks, factory, 'm', 'u');
        }
        return toks;
    }

}
