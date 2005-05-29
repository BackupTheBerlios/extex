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

package de.dante.extex.interpreter.primitives.math;

import java.util.HashMap;
import java.util.Map;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.exception.helping.HelpingException;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.math.NoadConsumer;
import de.dante.extex.typesetter.type.math.MathClass;
import de.dante.extex.typesetter.type.noad.MathGlyph;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\mathchar</code>.
 *
 * <doc name="mathchar">
 * <h3>The Primitive <tt>\mathchar</tt></h3>
 * <p>
 *  The primitive <tt>\mathchar</tt> inserts a mathematical character consisting
 *  of a math class and a character code inti the current math list. This is
 *  supposed to work in math mode only.
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\mathchar ...</tt>  </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \mathchar"041  </pre>
 *  <pre class="TeXSample">
 *    \mathchar{ordinary}0 `A  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.20 $
 */
public class Mathchar extends AbstractMathCode {

    /**
     * The constant <tt>GLYPH_MASK</tt> contains the mask for a math glyph.
     */
    private static final int GLYPH_MASK = 0xfff;

    /**
     * The field <tt>mathClassMap</tt> contains the mapping from symbolic names
     * the the corresponding math class.
     */
    private static Map mathClassMap = new HashMap();

    static {
        mathClassMap.put("0", MathClass.getMathClass(0));
        mathClassMap.put("1", MathClass.getMathClass(1));
        mathClassMap.put("2", MathClass.getMathClass(2));
        mathClassMap.put("3", MathClass.getMathClass(3));
        mathClassMap.put("4", MathClass.getMathClass(4));
        mathClassMap.put("5", MathClass.getMathClass(5));
        mathClassMap.put("6", MathClass.getMathClass(6));
        mathClassMap.put("7", MathClass.getMathClass(7));
        mathClassMap.put("ordinary", MathClass.ORDINARY);
        mathClassMap.put("large", MathClass.LARGE);
        mathClassMap.put("binary", MathClass.BINARY);
        mathClassMap.put("relation", MathClass.RELATION);
        mathClassMap.put("opening", MathClass.OPENING);
        mathClassMap.put("closing", MathClass.CLOSING);
        mathClassMap.put("punctation", MathClass.PUNCTUATION);
        mathClassMap.put("variable", MathClass.VARIABLE);
        mathClassMap.put("ord", MathClass.ORDINARY);
        mathClassMap.put("bin", MathClass.BINARY);
        mathClassMap.put("rel", MathClass.RELATION);
        mathClassMap.put("open", MathClass.OPENING);
        mathClassMap.put("close", MathClass.CLOSING);
        mathClassMap.put("punct", MathClass.PUNCTUATION);
        mathClassMap.put("var", MathClass.VARIABLE);
    }

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Mathchar(final String name) {

        super(name);
    }

    /**
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        NoadConsumer nc = getListMaker(context, typesetter);

        Token t = source.getToken(context);
        if (t == null) {
            throw new EofException(printableControlSequence(context));
        } else if (t.isa(Catcode.LEFTBRACE)) {
            source.push(t);
            String mclass = source.scanTokensAsString(context);
            MathClass mc = (MathClass) (mathClassMap.get(mclass));
            if (mc == null) {
                throw new HelpingException(getLocalizer(), "MathClass", mclass);
            }
            int fam = (int) source.scanNumber(context);
            int code = (int) source.scanNumber(context);
            MathGlyph mg = new MathGlyph(fam, new UnicodeChar(code));
            nc.add(mc, mg);
        } else {
            source.push(t);
            insert(nc, Count.scanCount(context, source, typesetter));
        }
    }

    /**
     * Insert a mathematical character into the noad list of the current
     * list maker.
     *
     * @param nc the interface to the list maker
     * @param mathchar the mathematical character
     *
     * @throws InterpreterException in case of an error
     */
    protected void insert(final NoadConsumer nc, final long mathchar)
            throws InterpreterException {

        nc.add(MathClass.getMathClass((int) ((mathchar >> 12) & 0xf)), //
                new MathGlyph((int) (mathchar & GLYPH_MASK)));
    }

}