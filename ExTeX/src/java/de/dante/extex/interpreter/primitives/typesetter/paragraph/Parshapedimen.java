/*
 * Copyright (C) 2005-2006 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.typesetter.paragraph;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.dimen.DimenConvertible;
import de.dante.extex.interpreter.type.dimen.FixedDimen;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.paragraphBuilder.ParagraphShape;
import de.dante.util.exception.GeneralException;

/**
 * This class provides an implementation for the primitive <code>\relax</code>.
 *
 * <doc name="parshapedimen">
 * <h3>The Primitive <tt>\parshapedimen</tt></h3>
 * <p>
 *  The primitive <tt>\parshapedimen</tt> gives access to the settings for the
 *  current paragraph shape. The primitive takes a number as parameter. If
 *  this number is odd then the indentation of the line denoted by the parameter
 *  is returned. If the number is even then the length of the line is returned.
 *  The line numbering starts with 1. If the argument is less than 1 then 0 is
 *  returned.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;parshapedimen&rang;
 *        &rarr; <tt>\parshapedimen</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *        &lang;8-bit&nbsp;number&rang;} </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \dimen2=\parshapedimen 3  </pre>
 *  <pre class="TeXSample">
 *    \dimen2=\parshapedimen -3  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class Parshapedimen extends AbstractCode
        implements
            CountConvertible,
            DimenConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2005L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Parshapedimen(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return convertDimen(context, source, typesetter);
    }

    /**
     * @see de.dante.extex.interpreter.type.count.DimenConvertible#convertDimen(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public long convertDimen(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        int n = (int) Count.scanInteger(context, source, typesetter);
        ParagraphShape parshape = context.getParshape();
        if (parshape == null || n < 0) {
            return 0;
        }
        return ((n & 1) == 0 //
                ? parshape.getIndent(n / 2).getValue() //
                : parshape.getLength(n / 2).getValue());
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        int n = (int) Count.scanInteger(context, source, typesetter);
        ParagraphShape parshape = context.getParshape();
        FixedDimen d = (parshape == null || n < 0
                ? Dimen.ZERO_PT
                : ((n & 1) == 0 //
                        ? parshape.getIndent(n / 2) //
                        : parshape.getLength(n / 2)));

        try {
            return d.toToks(context.getTokenFactory());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        } catch (InterpreterException e) {
            throw e;
        } catch (GeneralException e) {
            throw new InterpreterException(e);
        }
    }

}
