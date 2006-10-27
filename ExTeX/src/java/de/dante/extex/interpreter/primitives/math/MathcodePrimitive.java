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

package de.dante.extex.interpreter.primitives.math;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.count.CountConvertible;
import de.dante.extex.interpreter.type.math.MathClass;
import de.dante.extex.interpreter.type.math.MathClassVisitor;
import de.dante.extex.interpreter.type.math.MathCode;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\mathcode</code>.
 *
 * <doc name="mathcode">
 * <h3>The Math Primitive <tt>\mathcode</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;mathcode&rang;
 *      &rarr; <tt>\mathcode</tt> {@link
 *        de.dante.extex.interpreter.TokenSource#scanCharacterCode(Context,Typesetter,String)
 *        &lang;character code&rang;} {@linkplain
 *        de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *        &lang;equals&rang;} ...
 *        </pre>
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \mathcode`.="41  </pre>
 *
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.1 $
 */
public class MathcodePrimitive extends AbstractAssignment
        implements
            CountConvertible,
            Theable {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    protected static final long serialVersionUID = 2006L;

    /**
     * The field <tt>VISITOR</tt> contains the ...
     */
    private static final MathClassVisitor VISITOR = new MathClassVisitor() {

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitBinary(java.lang.Object, java.lang.Object)
         */
        public Object visitBinary(final Object arg, final Object arg2) {

            return new Integer(2);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitClosing(java.lang.Object, java.lang.Object)
         */
        public Object visitClosing(final Object arg, final Object arg2) {

            return new Integer(5);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitLarge(java.lang.Object, java.lang.Object)
         */
        public Object visitLarge(final Object arg, final Object arg2) {

            return new Integer(1);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitOpening(java.lang.Object, java.lang.Object)
         */
        public Object visitOpening(final Object arg, final Object arg2) {

            return new Integer(4);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitOrdinary(java.lang.Object, java.lang.Object)
         */
        public Object visitOrdinary(final Object arg, final Object arg2) {

            return new Integer(0);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitPunctation(java.lang.Object, java.lang.Object)
         */
        public Object visitPunctation(final Object arg, final Object arg2) {

            return new Integer(6);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitRelation(java.lang.Object, java.lang.Object)
         */
        public Object visitRelation(final Object arg, final Object arg2) {

            return new Integer(3);
        }

        /**
         * @see de.dante.extex.interpreter.type.math.MathClassVisitor#visitVariable(java.lang.Object, java.lang.Object)
         */
        public Object visitVariable(final Object arg, final Object arg2) {

            return new Integer(7);
        }
    };

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public MathcodePrimitive(final String name) {

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

        UnicodeChar charCode = source.scanCharacterCode(context, typesetter,
                getName());
        source.getOptionalEquals(context);
        Count mathCode = Count.parse(context, source, typesetter);

        if (mathCode.getValue() == 0x8000) {
            context.setMathcode(charCode, new MathCode(null, 0, null), //
                    prefix.clearGlobal());
        } else {
            context.setMathcode(charCode, new MathCode(mathCode.getValue()), //
                    prefix.clearGlobal());
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

        UnicodeChar charCode = source.scanCharacterCode(context, typesetter,
                getName());
        MathCode mc = context.getMathcode(charCode);

        int codePoint = mc.getMathChar().getCodePoint();
        if (codePoint > 0xff) {
            //TODO gene: unimplemented
            throw new RuntimeException("unimplemented");
        }
        int mathFamily = mc.getMathFamily();
        if (mathFamily > 0xf) {
            //TODO gene: unimplemented
            throw new RuntimeException("unimplemented");
        }
        MathClass mathClass = mc.getMathClass();
        if (mc == null) {
            return 0x8000;
        }
        return (((Integer) mathClass.visit(VISITOR, null, null)).intValue() << 12)
                | (mathFamily << 8) | codePoint;
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
