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

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\skewchar</code>.
 *
 * <doc name="skewchar">
 * <h3>The Primitive <tt>\skewchar</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\skewchar</tt> &lang;font&rang; {@linkplain
 *    de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *    &lang;equals&rang;} {@linkplain
 *    de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *    &lang;8-bit&nbsp;number&rang;} </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \skewchar\font=123  </pre>
 * </p>
 *
 * <h4>Incompatibility</h4>
 * <p>
 *  The TeXbook gives no indication ow the primitive should react for negative
 *  values &ndash; except -1. The implementation of <logo>TeX</logo> allows to store
 *  and retrieve arbirary negative values. This behaviour of <logo>TeX</logo>
 *  is not preserved in <logo>ExTeX</logo>.
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.19 $
 */
public class Skewchar extends AbstractAssignment
        implements
            CountConvertible,
            ExpandableCode,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Skewchar(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
     *       de.dante.extex.interpreter.Flags,
     *       de.dante.extex.interpreter.context.Context,
     *       de.dante.extex.interpreter.TokenSource,
     *       de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        try {
            Font font = source.getFont(context, getName());
            source.getOptionalEquals(context);
            long c = source.scanInteger(context, typesetter);
            if (c < 0) {
                font.setSkewChar(null);
            } else {
                font.setSkewChar(new UnicodeChar((int) c));
            }
        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *       de.dante.extex.interpreter.context.Context,
     *       de.dante.extex.interpreter.TokenSource,
     *       de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        try {
            Font font = source.getFont(context, getName());
            UnicodeChar uc = font.getSkewChar();
            if (uc == null) {
                return -1;
            } else {
                return uc.getCodePoint();
            }
        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

    /**
     * @see de.dante.extex.interpreter.type.ExpandableCode#expand(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void expand(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.push(the(context, source, typesetter));
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        try {
            Font font = source.getFont(context, getName());
            UnicodeChar uc = font.getSkewChar();
            if (uc == null) {
                return new Tokens(context, "-1");
            } else {
                return new Tokens(context, //
                        Integer.toString(uc.getCodePoint()));
            }
        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}