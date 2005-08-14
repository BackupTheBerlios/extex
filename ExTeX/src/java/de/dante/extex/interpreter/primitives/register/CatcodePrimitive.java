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

package de.dante.extex.interpreter.primitives.register;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\catcode</code>.
 *
 * <doc name="catcode">
 * <h3>The Primitive <tt>\catcode</tt></h3>
 * <p>
 *  The primitive <tt>\catcode</tt> can be used to influence the tokenizer of
 *  <logo>ExTeX</logo>. This is done by assigning category codes to single
 *  characters.
 *  TODO missing documentation
 * </p>
 * <p>
 *  The assignment is controlled by the prefix macro <tt>\global</tt> and the
 *  count parameter <tt>\globaldefs</tt>. Usually the assignment is acting on
 *  the current group only. If the count parameter <tt>\globaldefs</tt> is
 *  greater than 0 or the prefix <tt>\global</tt> is given then the assignment
 *  is applied to all groups.
 * </p>
 * <p>
 *  The following table contains the category codes with their meaning and the
 *  mapping to numerical values.
 * </p>
 * <table format="lrl">
 *  <tr><td>ESCAPE</td><td>0</td><td></td></tr>
 *  <tr><td>LEFTBRACE</td><td>1</td><td></td></tr>
 *  <tr><td>RIGHTBRACE</td><td>2</td><td></td></tr>
 *  <tr><td>MATHSHIFT</td><td>3</td><td></td></tr>
 *  <tr><td>TABMARK</td><td>4</td><td></td></tr>
 *  <tr><td>CR</td><td>5</td><td></td></tr>
 *  <tr><td>MACROPARAM</td><td>6</td><td></td></tr>
 *  <tr><td>SUPMARK</td><td>7</td><td></td></tr>
 *  <tr><td>SUBMARK</td><td>8</td><td></td></tr>
 *  <tr><td>IGNORE</td><td>9</td><td></td></tr>
 *  <tr><td>SPACE</td><td>10</td><td></td></tr>
 *  <tr><td>LETTER</td><td>11</td><td></td></tr>
 *  <tr><td>OTHER</td><td>12</td><td></td></tr>
 *  <tr><td>ACTIVE</td><td>13</td><td></td></tr>
 *  <tr><td>COMMENT</td><td>14</td><td></td></tr>
 *  <tr><td>INVALID</td><td>15</td><td></td></tr>
 * </table>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;catcode&rang;
 *      &rarr; &lang;prefix&rang; <tt>\catcode</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *          &lang;8-bit&nbsp;number&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *          &lang;equals&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *          &lang;4-bit&nbsp;number&rang;}
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       |  &lang;global&rang; </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \catcode `\%=12  </pre>
 *  <pre class="TeXSample">
 *    \global\catcode `\%=11  </pre>
 *
 * <h4><tt>\catcode</tt> as a Count Value</h4>
 *
 * <p>
 *  <tt>\catcode</tt> can be used wherever a count value is required. 
 * </p>
 *
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.21 $
 */
public class CatcodePrimitive extends AbstractAssignment
        implements
            CountConvertible,
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
    public CatcodePrimitive(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void assign(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        UnicodeChar charCode = source.scanCharacterCode(context);
        source.getOptionalEquals(context);
        long ccNumber = source.scanInteger(context, typesetter);

        try {
            context.setCatcode(charCode, Catcode.toCatcode((int) ccNumber),
                    prefix.isGlobal());
        } catch (CatcodeException e) {
            throw new HelpingException(getLocalizer(), "TTP.CodeOutOfRange",
                    Long.toString(ccNumber), //
                    Integer.toString(Catcode.getCatcodeMax()));
        }

    }

    /**
     * @see de.dante.extex.interpreter.type.count.CountConvertible#convertCount(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public long convertCount(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        UnicodeChar charCode = source.scanCharacterCode(context);

        return context.getCatcode(charCode).getCode();
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        return new Tokens(context, convertCount(context, source, typesetter));
    }
}
