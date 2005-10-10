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

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.primitives.math.AbstractMathCode;
import de.dante.extex.interpreter.type.Code;
import de.dante.extex.scanner.type.token.CodeToken;
import de.dante.extex.scanner.type.token.OtherToken;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.type.math.MathClass;
import de.dante.extex.typesetter.type.math.MathClassVisitor;
import de.dante.extex.typesetter.type.math.MathDelimiter;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.Localizer;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This abstract class adds the ability to translate
 * {@link de.dante.extex.typesetter.type.math.MathDelimiter MathDelimiter}s
 * to and from their <logo>TeX</logo> encoding as numbers to abstract math code.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public abstract class AbstractTeXDelimter extends AbstractMathCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant <tt>CHAR_MASK</tt> contains the character mask.
     */
    private static final int CHAR_MASK = 0xff;

    /**
     * The field <tt>CHAR_MAX</tt> contains the maximal number for a character.
     */
    private static final int CHAR_MAX = 255;

    /**
     * The field <tt>CHAR_MIN</tt> contains the minimal number for a character.
     */
    private static final int CHAR_MIN = 0;

    /**
     * The field <tt>CLASS_MASK</tt> contains the mask for the class. This
     * implies a maximal value.
     */
    private static final int CLASS_MASK = 0xf;

    /**
     * The constant <tt>CLASS_MAX</tt> contains the maximum number for a
     * math class.
     */
    private static final int CLASS_MAX = CLASS_MASK;

    /**
     * The field <tt>CLASS_SHIFT</tt> contains the number of bits to shift the
     * class rightwards in the <logo>TeX</logo> encoding of delimiters.
     */
    private static final int CLASS_SHIFT = 24;

    /**
     * The field <tt>FAM_MAX</tt> contains the maximum of the family number.
     */
    private static final int FAM_MAX = 15;

    /**
     * The field <tt>FAM_MIN</tt> contains the minimum of the family number.
     */
    private static final int FAM_MIN = 0;

    /**
     * The field <tt>LARGE_CLASS_OFFSET</tt> contains the offset for large
     * character's class.
     */
    private static final int LARGE_CLASS_OFFSET = 8;

    /**
     * The field <tt>MCV</tt> contains the visitor to map a math class to
     * numbers.
     */
    private static final MathClassVisitor MCV = new MathClassVisitor() {

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitBinary(
         *      java.lang.Object)
         */
        public Object visitBinary(final Object ignore) {

            return new Integer(2);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitClosing(
         *      java.lang.Object)
         */
        public Object visitClosing(final Object ignore) {

            return new Integer(5);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitLarge(
         *      java.lang.Object)
         */
        public Object visitLarge(final Object ignore) {

            return new Integer(1);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitOpening(
         *      java.lang.Object)
         */
        public Object visitOpening(final Object ignore) {

            return new Integer(4);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitOrdinary(
         *      java.lang.Object)
         */
        public Object visitOrdinary(final Object ignore) {

            return new Integer(0);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitPunctation(
         *      java.lang.Object)
         */
        public Object visitPunctation(final Object ignore) {

            return new Integer(6);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitRelation(
         *      java.lang.Object)
         */
        public Object visitRelation(final Object ignore) {

            return new Integer(3);
        }

        /**
         * @see de.dante.extex.typesetter.type.math.MathClassVisitor#visitVariable(
         *      java.lang.Object)
         */
        public Object visitVariable(final Object ignore) {

            return new Integer(7);
        }
    };

    /**
     * The field <tt>SMALL_CHAR_OFFSET</tt> contains the offset for the small
     * characters.
     */
    private static final int SMALL_CHAR_OFFSET = 12;

    /**
     * The field <tt>SMALL_CLASS_OFFSET</tt> contains the offset for the small
     * character's class.
     */
    private static final int SMALL_CLASS_OFFSET = 20;

    /**
     * Create a localizer for this class.
     *
     * @return the localizer
     */
    protected static Localizer getMyLocalizer() {

        return LocalizerFactory.getLocalizer(AbstractTeXDelimter.class
                .getName());
    }

    /**
     * Creates a new MathDelimiter object from the <logo>TeX</logo> encoding.
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
     *
     * @param delcode the <logo>TeX</logo> encoding for the delimiter
     *
     * @return a new MathDelimiter
     *
     * @throws InterpreterException in case of a parameter out of range
     */
    public static MathDelimiter newMathDelimiter(final long delcode)
            throws InterpreterException {

        int classCode = (int) ((delcode >> CLASS_SHIFT));

        if (delcode < 0 || classCode > CLASS_MAX) {
            throw new HelpingException(getMyLocalizer(),
                    "TTP.BadDelimiterCode", "\"" + Long.toHexString(delcode));
        }
        MathClass mathClass = MathClass.getMathClass(classCode);
        MathGlyph smallChar = new MathGlyph(
                (int) ((delcode >> SMALL_CLASS_OFFSET) & CLASS_MASK),
                new UnicodeChar(
                        (int) ((delcode >> SMALL_CHAR_OFFSET) & CHAR_MASK)));
        MathGlyph largeChar = new MathGlyph(
                (int) ((delcode >> LARGE_CLASS_OFFSET) & CLASS_MASK),
                new UnicodeChar((int) (delcode & CHAR_MASK)));
        return new MathDelimiter(mathClass, smallChar, largeChar);
    }

    /**
     * Parse an extended <logo>ExTeX</logo> delimiter from a token source.
     *
     * @param context the interpreter context
     * @param source the token source to read from
     * @param typesetter the typesetter
     * @param mClass the math class
     * @param primitive the name of the primitive for error handling
     *
     * @return the MathDelimiter found
     *
     * @throws InterpreterException in case of an error
     */
    private static MathDelimiter parse(final Context context,
            final TokenSource source, Typesetter typesetter,
            final MathClass mClass, final String primitive)
            throws InterpreterException {

        int smallFam = (int) source.scanNumber(context);
        UnicodeChar smallChar = source.scanCharacterCode(context, typesetter,
                primitive);
        int largeFam = (int) source.scanNumber(context);
        UnicodeChar largeChar = source.scanCharacterCode(context, typesetter,
                primitive);

        return new MathDelimiter(mClass, new MathGlyph(smallFam, smallChar),
                new MathGlyph(largeFam, largeChar));
    }

    /**
     * Parse a math delimiter.
     *
     * <pre>
     *  \delimiter"1234567
     *  \delimiter open 22 `[ 1 `(
     * </pre>
     *
     * @param context the interpreter context
     * @param source the token source to read from
     * @param typesetter the typesetter
     * @param primitive the name of the primitive for error handling
     *
     * @return the MathDelimiter acquired
     *
     * @throws InterpreterException in case of an error
     */
    public static MathDelimiter parseDelimiter(final Context context,
            final TokenSource source, final Typesetter typesetter,
            final String primitive) throws InterpreterException {

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException("???");
        }
        if (t instanceof CodeToken) {
            Code code = context.getCode((CodeToken) t);
            if (code instanceof Delimiter) {
                return newMathDelimiter(source.scanNumber(context));
            } else {
                //TODO gene: expand and try again?
            }
        } else {
            MathDelimiter del = context.getDelcode(t.getChar());
            if (del != null) {
                return del;
            } else if (t instanceof OtherToken) {
                source.push(t);
                try {
                    return newMathDelimiter(source.scanNumber(context));
                } catch (MissingNumberException e) {
                    // fall through to error. the exception is remapped!
                }
            } else {
                source.push(t);
                switch (t.getChar().getCodePoint()) {
                    case 'b':
                        if (source.getKeyword(context, "bin")) {
                            return parse(context, source, typesetter,
                                    MathClass.BINARY, primitive);
                        }
                        break;
                    case 'c':
                        if (source.getKeyword(context, "close")) {
                            return parse(context, source, typesetter,
                                    MathClass.CLOSING, primitive);
                        }
                        break;
                    case 'l':
                        if (source.getKeyword(context, "large")) {
                            return parse(context, source, typesetter,
                                    MathClass.LARGE, primitive);
                        }
                        break;
                    case 'o':
                        if (source.getKeyword(context, "open")) {
                            return parse(context, source, typesetter,
                                    MathClass.OPENING, primitive);
                        } else if (source.getKeyword(context, "ord")) {
                            return parse(context, source, typesetter,
                                    MathClass.ORDINARY, primitive);
                        }
                        break;
                    case 'p':
                        if (source.getKeyword(context, "punct")) {
                            return parse(context, source, typesetter,
                                    MathClass.PUNCTUATION, primitive);
                        }
                        break;
                    case 'r':
                        if (source.getKeyword(context, "rel")) {
                            return parse(context, source, typesetter,
                                    MathClass.RELATION, primitive);
                        }
                        break;
                    case 'v':
                        if (source.getKeyword(context, "var")) {
                            return parse(context, source, typesetter,
                                    MathClass.VARIABLE, primitive);
                        }
                        break;
                    default:
                // fall-through to exception
                }
            }
        }

        throw new HelpingException(getMyLocalizer(), "TTP.MissingDelim");
    }

    /**
     * Translate the delimiter into a <logo>TeX</logo> encoded number or throw
     * an exception if this is not possible.
     *
     * @param del the delimiter to encode
     *
     * @return the <logo>TeX</logo> encoded delimiter
     *
     * @throws HelpingException in case of an error
     */
    public static long toTeX(final MathDelimiter del) throws HelpingException {

        if (del == null) {
            return -1;
        }

        long value = ((Integer) del.getMathClass().visit(MCV, null))
                .longValue() << CLASS_SHIFT;

        int fam0 = del.getSmallChar().getFamily();
        if (fam0 < FAM_MIN || fam0 > FAM_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        int c0 = del.getSmallChar().getCharacter().getCodePoint();
        if (c0 < CHAR_MIN || c0 > CHAR_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        int fam1 = del.getLargeChar().getFamily();
        if (fam1 < FAM_MIN || fam1 > FAM_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        int c1 = del.getLargeChar().getCharacter().getCodePoint();
        if (c1 < CHAR_MIN || c1 > CHAR_MAX) {
            throw new HelpingException(getMyLocalizer(), "ExtendedDelimiter");
        }
        value |= fam0 << SMALL_CLASS_OFFSET;
        value |= c0 << SMALL_CHAR_OFFSET;
        value |= fam1 << LARGE_CLASS_OFFSET;
        value |= c1;
        return value;
    }

    /**
     * Creates a new object.
     *
     * @param name the name for tracing and debugging
     */
    public AbstractTeXDelimter(final String name) {

        super(name);
    }

}
