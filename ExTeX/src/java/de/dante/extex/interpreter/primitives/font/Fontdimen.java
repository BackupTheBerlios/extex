/*
 * Copyright (C) 2004 The ExTeX Group and individual authors listed below
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 */

package de.dante.extex.interpreter.primitives.font;

import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.exception.EofException;
import de.dante.extex.interpreter.type.AbstractAssignment;
import de.dante.extex.interpreter.type.ExpandableCode;
import de.dante.extex.interpreter.type.Theable;
import de.dante.extex.interpreter.type.dimen.Dimen;
import de.dante.extex.interpreter.type.font.Font;
import de.dante.extex.interpreter.type.tokens.Tokens;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.util.GeneralException;

/**
 * This class provides an implementation for the primitive
 * <code>\fontdimen</code>.
 *
 * <doc name="fontdimen">
 * <h3>The Primitive <tt>\fontdimen</tt></h3>
 * <p>
 *  TODO missing documentation
 * </p>
 * <p>
 *  The formal description of this primitive is the following:
 *  <pre class="syntax">
 *    <tt>\fontdimen</tt> {@linkplain
 *      de.dante.extex.interpreter.TokenSource#scanNumber()
 *      &lang;8-bit&nbsp;number&rang;}  {@linkplain
 *      de.dante.extex.interpreter.TokenSource#getOptionalEquals()
 *      &lang;equals&rang;} {@linkplain
 *      de.dante.extex.interpreter.type.dimen#Dimen(Context,TokenSource)
 *      &lang;dimen&rang;}   </pre>
 * </p>
 * <p>
 *  Examples:
 *  <pre class="TeXSample">
 *    \fontdimen13\ff=5pt  </pre>
 *  <pre class="TeXSample">
 *    \the\fontdimen13\ff  </pre>
 * </p>
 * </doc>
 *
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @author <a href="mailto:m.g.n@gmx.de">Michael Niedermair</a>
 * @version $Revision: 1.17 $
 */
public class Fontdimen extends AbstractAssignment
        implements
            ExpandableCode,
            Theable {

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
            throws GeneralException {

        String key = getKey(context, source);
        source.skipSpace();
        Font font = source.getFont(context);
        source.getOptionalEquals(context);
        Dimen size = new Dimen(context, source);
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
            throws GeneralException {

        source.push(the(context, source, typesetter));
    }

    /**
     * Get the key for the fontdimen.
     *
     * @param context the interpreter context
     * @param source the source for new tokens
     *
     * @return the key
     *
     * @throws GeneralException in case of an error
     */
    protected String getKey(final Context context, final TokenSource source)
            throws GeneralException {

        Token t = source.getNonSpace(context);
        if (t == null) {
            throw new EofException(printableControlSequence(context));
        } else if (t.isa(Catcode.LEFTBRACE)) {
            source.push(t);
            String key = source.scanTokensAsString(context);
            if (key == null) {
                throw new EofException(printableControlSequence(context));
            }
            return key;
        }
        source.push(t);
        long idx = source.scanInteger(context);
        return "#" + Long.toString(idx);
    }

    /**
     * @see de.dante.extex.interpreter.type.Theable#the(
     *      de.dante.extex.interpreter.context.Context,
     *      de.dante.extex.interpreter.TokenSource, Typesetter)
     */
    public Tokens the(final Context context, final TokenSource source,
            final Typesetter typesetter) throws GeneralException {

        String key = getKey(context, source);
        source.skipSpace();
        Font font = source.getFont(context);
        Dimen size = font.getFontDimen(key);
        if (null == size) {
            size = Dimen.ZERO_PT;
        }
        return size.toToks(context.getTokenFactory());
    }

}