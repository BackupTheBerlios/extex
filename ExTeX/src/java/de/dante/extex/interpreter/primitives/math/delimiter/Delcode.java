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

package de.dante.extex.interpreter.primitives.math.delimiter;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.ArithmeticOverflowException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.arithmetic.Advanceable;
import de.dante.extex.interpreter.type.arithmetic.Divideable;
import de.dante.extex.interpreter.type.arithmetic.Multiplyable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\delcode</code>.
 *
 * <doc name="delcode">
 * <h3>The Math Primitive <tt>\delcode</tt></h3>
 * <p>
 *  The primitive <tt>\delcode</tt> can be used to assign and query the
 *  delimiter code for a character. The delimiter code determines, how a
 *  character is typeset in math mode.
 * </p>
 * <p>
 * The <logo>TeX</logo> encoding interprets the number as 27 bit hex number:
 * <tt>"csyylxx</tt>. Here the digits have the following meaning:
 * <dl>
 *  <dt>c</dt>
 *  <dd>the math class of this delimiter. It has a range from 0 to 7.</dd>
 *  <dt>l</dt>
 *  <dd>the family for the large character. It has a range from 0 to 15.</dd>
 *  <dt>xx</dt>
 *  <dd>the character code of the large character.</dd>
 *  <dt>s</dt>
 *  <dd>the family for the small character. It has a range from 0 to 15.</dd>
 *  <dt>yy</dt>
 *  <dd>the character code of the small character.</dd>
 * </dl>
 * </p>
 * <p>
 *  The assigning a new value to a delimiter code acts in a group restricted way
 *  unless declared differently. If the prefix <tt>\global</tt> is given then
 *  the assignment is performed globally. The same effect can be achieved when
 *  the count register <tt>\globaldefs</tt> is greater than 0.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;delcode&rang;
 *      &rarr; &lang;prefix&rang; <tt>\delcode</tt> {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber()
 *        &lang;8-bit&nbsp;number&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#scanNumber()
 *        &lang;8-bit&nbsp;number&rang;}
 *
 *    &lang;prefix&rang;
 *      &rarr;
 *       |  &lang;global&rang; </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \delcode`x="123456  </pre>
 *  <pre class="TeXSample">
 *    \global\delcode`x="123456  </pre>
 *
 * <h4>Using as Count Register</h4>
 * <p>
 *  The primitive <tt>\delcode</tt> can be used like a count register. This
 *  means you can use it wherever a number is expected. In addition the value
 *  can be advanced, multiplied, and divided. In any case the delimiter code
 *  is translated according to the <logo>TeX</logo> encoding and processed as
 *  number.
 * </p>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \count1=\delcode`x  </pre>
 *  <pre class="TeXSample">
 *    \advance\delcode`x by 42  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.3 $
 */
public class Delcode extends AbstractAssignment
        implements
            CountConvertible,
            Advanceable,
            Divideable,
            Multiplyable,
            Theable {

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Delcode(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Advanceable#advance(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void advance(final Flags prefix, final Context context,
            final TokenSource source) throws InterpreterException {

        UnicodeChar charCode = source.scanCharacterCode(context);
        source.getKeyword(context, "by");

        long value = Count.scanCount(context, source, null);
        MathDelimiter delcode = context.getDelcode(charCode);
        value += AbstractTeXDelimter.toTeX(delcode);

        assign(prefix, context, source, charCode, value);
    }

    /**
     * @see de.dante.extex.interpreter.type.AbstractAssignment#assign(
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
        MathDelimiter del = AbstractTeXDelimter.parseDelimiter(context, source);
        context.setDelcode(charCode, del, prefix.isGlobal());
    }

    /**
     * Perform an assignment of a delimiter code.
     *
     * @param prefix the prefix indicator
     * @param context the interpreter context
     * @param source the token source
     * @param charCode the character to assign the delimiter code to
     * @param value the delimiter code in <logo>TeX</logo> encoding
     *
     * @throws InterpreterException in case of an error
     */
    private void assign(final Flags prefix, final Context context,
            final TokenSource source, final UnicodeChar charCode,
            final long value) throws InterpreterException {

        long globaldef = context.getCount("globaldefs").getValue();
        if (globaldef != 0) {
            prefix.setGlobal((globaldef > 0));
        }

        context.setDelcode(charCode, AbstractTeXDelimter
                .newMathDelimiter(value), //
                prefix.isGlobal());

        Token afterassignment = context.getAfterassignment();
        if (afterassignment != null) {
            context.setAfterassignment(null);
            source.push(afterassignment);
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
        MathDelimiter delcode = context.getDelcode(charCode);
        return AbstractTeXDelimter.toTeX(delcode);
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Divideable#divide(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void divide(final Flags prefix, final Context context,
            final TokenSource source) throws InterpreterException {

        UnicodeChar charCode = source.scanCharacterCode(context);
        source.getKeyword(context, "by");

        long value = Count.scanCount(context, source, null);
        MathDelimiter delcode = context.getDelcode(charCode);
        if (value == 0) {
            throw new ArithmeticOverflowException(
                    printableControlSequence(context));
        }

        value = AbstractTeXDelimter.toTeX(delcode) / value;
        assign(prefix, context, source, charCode, value);
    }

    /**
     * @see de.dante.extex.interpreter.type.arithmetic.Multiplyable#multiply(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource)
     */
    public void multiply(final Flags prefix, final Context context,
            final TokenSource source) throws InterpreterException {

        UnicodeChar charCode = source.scanCharacterCode(context);
        source.getKeyword(context, "by");

        long value = Count.scanCount(context, source, null);
        MathDelimiter delcode = context.getDelcode(charCode);
        value *= AbstractTeXDelimter.toTeX(delcode);
        assign(prefix, context, source, charCode, value);
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        UnicodeChar charCode = source.scanCharacterCode(context);
        MathDelimiter delcode = context.getDelcode(charCode);
        long value = AbstractTeXDelimter.toTeX(delcode);
        return new Tokens(context, value);
    }

}
