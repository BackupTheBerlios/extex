/*
 * Copyright (C) 2004-2005 The ExTeX Group and individual authors listed below
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

package de.dante.extex.typesetter.type;

import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.exception.helping.MissingNumberException;
import de.dante.extex.interpreter.primitives.math.delimiter.Delimiter;
import de.dante.extex.scanner.type.CodeToken;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.TypesetterOptions;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.extex.typesetter.type.noad.util.MathContext;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;
import de.dante.util.framework.i18n.LocalizerFactory;

/**
 * This class provides a container for a delimiter consisting of a class, a
 * large and a small math glyph.
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.8 $
 */
public class MathDelimiter {

    /**
     * The constant <tt>CHAR_MASK</tt> contains the character mask.
     */
    private static final int CHAR_MASK = 0xff;

    /**
     * The constant <tt>CLASS_MAX</tt> contains the maximum number for a
     * math class.
     */
    private static final int CLASS_MAX = 0xf;

    /**
     * The field <tt>CLASS_SHIFT</tt> contains the number of bits to shift the
     * class rightwards in the TeX encoding of delimiters.
     */
    private static final int CLASS_SHIFT = 24;

    /**
     * The field <tt>largeChar</tt> contains the code of the large character.
     */
    private MathGlyph largeChar;

    /**
     * The field <tt>mathClass</tt> contains the class of this delimiter.
     */
    private MathClass mathClass;

    /**
     * The field <tt>smallChar</tt> contains the code of the small character.
     */
    private MathGlyph smallChar;

    /**
     * Creates a new object.
     *
     * @param context the interpreter context
     * @param source the token source to read from
     *
     * @throws GeneralException in case of an error
     */
    public MathDelimiter(final Context context, final TokenSource source)
            throws GeneralException {

        super();
        Token t = source.getToken(context);
        try {
            if (t == null) {
                throw new EofException("???");
            } else if (!(t instanceof CodeToken)) {
                long del = context.getDelcode(t.getChar()).getValue();
                if (del < 0) {
                    source.push(t);
                    del = source.scanNumber(context);
                    init(del);
                    return;
                }
            } else if (context.getCode((CodeToken) t) instanceof Delimiter) {
                long del = source.scanNumber(context);
                init(del);
                return;

            }
        } catch (MissingNumberException e) {
            // fall through to error. the exception is remapped!
        }
        throw new HelpingException(LocalizerFactory
                .getLocalizer(MathDelimiter.class.getName()),
                "TTP.MissingDelim");
    }

    /**
     * Creates a new object from the TeX encoding.
     * <p>
     * The TeX encoding interprets the number as 27 bit hex number:
     * <tt>"csyylxx</tt>. Here the digits have the following meaning:
     * <dl>
     *  <dt>c</dt>
     *  <dd>the math class of this delimiter. It has a range from 0 to 7.</dd>
     *  <dt>l</dt>
     *  <dd>the family for the large character. it has a range from 0 to 15.</dd>
     *  <dt>xx</dt>
     *  <dd>the character code of the large character.</dd>
     *  <dt>s</dt>
     *  <dd>the family for the small character. it has a range from 0 to 15.</dd>
     *  <dt>yy</dt>
     *  <dd>the character code of the small character.</dd>
     * </dl>
     * </p>
     *
     * @param delcode the TeX encoding for the delimiter
     *
     * @throws GeneralException in case of a parameter out of range
     */
    protected MathDelimiter(final long delcode) throws GeneralException {

        super();
        init(delcode);
    }

    /**
     * Getter for largeChar.
     *
     * @return the largeChar.
     */
    public MathGlyph getLargeChar() {

        return this.largeChar;
    }

    /**
     * Getter for mathClass.
     *
     * @return the mathClass.
     */
    public MathClass getMathClass() {

        return this.mathClass;
    }

    /**
     * Getter for smallChar.
     *
     * @return the smallChar.
     */
    public MathGlyph getSmallChar() {

        return this.smallChar;
    }

    /**
     * Initialize the state of this instance from a TeX encoded delimiter code.
     *
     * @param delcode the delimiter code in TeX encoding
     *
     * @throws HelpingException in case of an error
     */
    private void init(final long delcode) throws HelpingException {

        int classCode = (int) ((delcode >> CLASS_SHIFT));

        if (delcode < 0 || classCode > CLASS_MAX) {
            throw new HelpingException(LocalizerFactory
                    .getLocalizer(MathDelimiter.class.getName()),
                    "TTP.BadDelimiterCode", "\"" + Long.toHexString(delcode));
        }
        mathClass = MathClass.getMathClass(classCode);
        smallChar = new MathGlyph((int) ((delcode >> 20) & 0xf),
                new UnicodeChar((int) ((delcode >> 12) & CHAR_MASK)));
        largeChar = new MathGlyph((int) ((delcode >> 8) & 0xf),
                new UnicodeChar((int) (delcode & CHAR_MASK)));
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }

    /**
     * Append the printable representation of the  current instance to the
     * string buffer.
     *
     * @param sb the target string buffer
     *
     * @see "TTP [691]"
     */
    public void toString(final StringBuffer sb) {

        sb.append('\"');
        mathClass.toString(sb);
        smallChar.toString(sb);
        largeChar.toString(sb);
    }

    /**
     * Produce the nodes for s math delimiter.
     *
     * @param list the hbox to add nodes to
     * @param mathContext the mathematical context
     * @param context the typesetting options
     *
     * @see de.dante.extex.typesetter.type.noad.Noad#typeset(
     *      de.dante.extex.typesetter.NodeList,
     *      de.dante.extex.typesetter.type.noad.util.MathContext,
     *      de.dante.extex.typesetter.TypesetterOptions)
     */
    public void typeset(final NodeList list, final MathContext mathContext,
            final TypesetterOptions context) {

        //TODO gene: unimplemented
        throw new RuntimeException("unimplemented");
    }

}