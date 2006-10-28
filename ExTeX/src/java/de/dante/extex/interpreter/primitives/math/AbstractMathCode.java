/*
 * Copyright (C) 2004-2006 The ExTeX Group and individual authors listed below
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

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingMathException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.interpreter.type.math.MathClass;
import de.dante.extex.interpreter.type.math.MathClassVisitor;
import de.dante.extex.interpreter.type.math.MathCode;
import de.dante.extex.scanner.type.token.LeftBraceToken;
import de.dante.extex.scanner.type.token.RightBraceToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.ListMaker;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.math.NoadConsumer;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This is the base class for all math primitives.
 * It tries to ensure that the primitive is invoked in math mode only.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.13 $
 */
public abstract class AbstractMathCode extends AbstractCode {

    /**
     * The constant <tt>CHARACTER_MASK</tt> contains the mask for the character
     * value in the <logo>TeX</logo> encoding.
     */
    private static final int CHARACTER_8_MASK = 0xff;

    /**
     * The constant <tt>FAMILY_MASK</tt> contains the mask for the family in the
     * <logo>TeX</logo> encoding.
     */
    private static final int FAMILY_MASK = 0xf;

    /**
     * The constant <tt>FAMILY_OFFSET</tt> contains the offset for the family in
     * the <logo>TeX</logo> encoding.
     */
    private static final int FAMILY_OFFSET = 8;

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
     * Convert a {@link MathCode MathCode} to a number using the TeX encoding.
     *
     * @param mc the math code
     *
     * @return a TeX-encoded math code
     *
     * @throws InterpreterException in case of an error
     */
    public static long mathCodeToTeX(final MathCode mc) throws InterpreterException {

        MathClass mathClass = mc.getMathClass();
        if (mc == null) {
            return 0x8000;
        }
        MathGlyph mg = mc.getMathGlyph();
        int codePoint = mg.getCharacter().getCodePoint();
        if (codePoint > 0xff) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(AbstractMathCode.class),
                    "InvalidCharacterCode");
        }
        int mathFamily = mg.getFamily();
        if (mathFamily > 0xf) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(AbstractMathCode.class),
                    "InvalidFamilyCode");
        }
        return (((Integer) mathClass.visit(VISITOR, null, null)).intValue() << 12)
                | (mathFamily << 8) | codePoint;

    }

    /**
     * Parse Math code according to TeX rules and extensions.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     * @param primitive the name of the invoking primitive
     *
     * @return the MathCode
     *
     * @throws InterpreterException in case of an error
     */
    public static MathCode parseTeXMathCode(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String primitive) throws InterpreterException {

        Token t = source.getToken(context);
        if (t instanceof LeftBraceToken) {
            MathClass mc = MathClass.parse(context, source, typesetter,
                    primitive);
            long family = Count.parse(context, source, typesetter).getValue();
            UnicodeChar c = source.scanCharacterCode(context, typesetter,
                    primitive);

            t = source.getToken(context);
            if (!(t instanceof RightBraceToken)) {
                if (t == null) {
                    throw new EofException();
                }
                throw new HelpingException(LocalizerFactory
                        .getLocalizer(AbstractMathCode.class),
                        "MissingRightBrace");
            }
            return new MathCode(mc, new MathGlyph((int) family, c));
        }

        source.push(t);
        long code = Count.parse(context, source, typesetter).getValue();

        if (code < 0 || code > 0x8000) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(AbstractMathCode.class),
                    "TTP.BadMathCharCode", Long.toString(code));
        } else if (code == 0x8000) {
            return new MathCode(null, null);
        } else {
            return new MathCode(MathClass.getMathClass((int) (code >> 12)),
                    new MathGlyph((int) (code >> 8) & 0xf, //
                            UnicodeChar.get((int) (code & 0xff))));
        }

    }

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public AbstractMathCode(final String name) {

        super(name);
    }

    /**
     * Get the current list maker as Noad consumer. If the current list maker is
     * not of the proper type then an exception is thrown.
     *
     * @param context the interpreter context
     * @param typesetter the master typesetter
     *
     * @return the current list maker
     *
     * @throws MissingMathException in case that the current mode is not a
     *  math mode
     */
    protected NoadConsumer getListMaker(final Context context,
            final Typesetter typesetter) throws MissingMathException {

        ListMaker lm = typesetter.getListMaker();
        if (!(lm instanceof NoadConsumer)) {
            throw new MissingMathException(printableControlSequence(context));
        }
        return (NoadConsumer) lm;
    }

}
