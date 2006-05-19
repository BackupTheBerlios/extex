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

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.InterpreterException;
import de.dante.extex.interpreter.exception.helping.EofException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.type.Catcode;
import de.dante.extex.scanner.type.CatcodeException;
import de.dante.extex.scanner.type.token.Token;
import de.dante.extex.typesetter.Typesetter;

/**
 * This class provides an implementation for the primitive
 * <code>\fontdimen</code>.
 *
 * <doc name="fontdimen">
 * <h3>The Primitive <tt>\fontdimen</tt></h3>
 * <p>
 *  The primitive <tt>\fontdimen</tt> can be used to set a font dimension value.
 *  Each font has an arbitrary number of dimen values which are addressed by
 *  an numerical index in <logo>TeX</logo>. In <logo>ExTeX</logo> this has been
 *  extended to arbitrary strings.
 * </p>
 * <p>
 *  The primitive expands to the value of the font dimension in a right hand
 *  context.
 * </p>
 *
 * <h4>Syntax</h4>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    &lang;fontdimen&rang;
 *       &rarr; <tt>\fontdimen</tt> {@linkplain
 *          de.dante.extex.interpreter.TokenSource#scanNumber(Context)
 *          &lang;8-bit&nbsp;number&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#getFont()
 *          &lang;font&rang;} {@linkplain
 *          de.dante.extex.interpreter.TokenSource#getOptionalEquals(Context)
 *          &lang;equals&rang;} {@linkplain
 *          de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *          &lang;dimen&rang;}   </pre>
 *  TODO gene: document Extension
 *
 * <h4>Examples</h4>
 *  <pre class="TeXSample">
 *    \fontdimen13\ff=5pt  </pre>
 *  <pre class="TeXSample">
 *    \the\fontdimen13\ff  </pre>
 *  <pre class="TeXSample">
 *    \the\fontdimen{em}\ff=8pt  </pre>
 *
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.36 $
 */
public class Fontdimen extends AbstractAssignment
        implements
            ExpandableCode,
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
    public Fontdimen(final String name) {

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

        String key = getKey(context, source, typesetter);
        source.skipSpace();
        Font font = source.getFont(context, getName());
        source.getOptionalEquals(context);
        Dimen size = Dimen.parse(context, source, typesetter);
        font.setFontDimen(key, size);
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
     * Get the key for the font dimen. According to <logo>TeX</logo> the key is
     * an arbitrary number. In <logo>ExTeX</logo> this has been extended to take
     * an expandable sequence of tokens enclosed in braces. The left brace acts
     * as indicator that this extension is used.
     * @param context the interpreter context
     * @param source the source for new tokens
     * @param typesetter the typesetter
     *
     * @return the key
     *
     * @throws InterpreterException in case of an error
     */
    protected String getKey(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(printableControlSequence(context));
        } else if (t.isa(Catcode.LEFTBRACE)) {
            source.push(t);
            String key = source.scanTokensAsString(context, getName());
            if (key == null) {
                throw new EofException(printableControlSequence(context));
            }
            return key;
        }
        source.push(t);
        long idx = source.scanInteger(context, typesetter);
        return Long.toString(idx);
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws InterpreterException {

        try {
            String key = getKey(context, source, typesetter);
            source.skipSpace();
            Font font = source.getFont(context, getName());
            Dimen size = font.getFontDimen(key);
            if (null == size) {
                size = Dimen.ZERO_PT;
            }
            return size.toToks(context.getTokenFactory());
        } catch (CatcodeException e) {
            throw new InterpreterException(e);
        }
    }

}
