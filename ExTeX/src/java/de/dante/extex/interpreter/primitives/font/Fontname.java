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

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractCode;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.TokenFactory;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\fontname</code>.
 *
 * <doc name="fontname">
 * <h3>The Primitive <tt>\fontname</tt></h3>
 * <p>
 *  The primitive <tt>\fontname</tt> can be used to retrieve the name of a font.
 *  It takes a font specification as argument. It expands to the name of the
 *  font. If this font is not loaded at its design size then the actual size
 *  is appended after the tokens <tt> at </tt>. All tokens produced this way
 *  are <i>other</i> tokens except of the spaces. Ths means that even the
 *  letters are of category <i>other</i>.
 * </p>
 * </doc>
 *
 * <h4>Example</h4>
 * <pre>
 * \font\myFont=cmr12
 * \fontname\myfont
 * &rArr; cmr12
 * </pre>
 *
 * <pre>
 * \font\myFont=cmr12 at 24pt
 * \fontname\myfont
 * &rArr; cmr12 at 24pt
 * </pre>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.19 $
 */
public class Fontname extends AbstractCode implements ExpandableCode {

    /**
     * The constant <tt>serialVersionUID</tt> contains the id for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new object.
     *
     * @param name the name for debugging
     */
    public Fontname(final String name) {

        super(name);
    }

    /**
     * Get the next <code>ControlSequenceToken</code> with the
     * <code>FontCode</code>
     * and put the font name on the stack.
     *
     * @see de.dante.extex.interpreter.type.Code#execute(
     *      de.dante.extex.interpreter.Flags,
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource,
     *      de.dante.extex.typesetter.Typesetter)
     */
    public void execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws InterpreterException {

        source.skipSpace();
        Font font;
        try {
            font = source.getFont(context);
        } catch (EofException e) {
            throw new EofException(printableControlSequence(context));
        }
        Tokens fontname = new Tokens(context, font.getFontName());
        Dimen size = font.getActualSize();
        if (font.getDesignSize().ne(size)) {
            TokenFactory tokenFactory = context.getTokenFactory();
            try {
                fontname.add(tokenFactory, " at ");
                fontname.add(size.toToks(tokenFactory));
            } catch (CatcodeException e) {
                throw new InterpreterException(e);
            }
        }
        source.push(fontname);
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

        execute(prefix, context, source, typesetter);
    }

}