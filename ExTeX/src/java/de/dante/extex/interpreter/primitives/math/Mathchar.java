/*
 * Copyright (C) 2003-2004 The ExTeX Group and individual authors listed below
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

package de.dante.extex.interpreter.primitives.math;

import de.dante.extex.i18n.EofHelpingException;
import de.dante.extex.interpreter.Flags;
import de.dante.extex.interpreter.TokenSource;
import de.dante.extex.interpreter.context.Context;
import de.dante.extex.interpreter.type.count.Count;
import de.dante.extex.scanner.Catcode;
import de.dante.extex.scanner.Token;
import de.dante.extex.typesetter.Typesetter;
import de.dante.extex.typesetter.listMaker.NoadConsumer;
import de.dante.extex.typesetter.type.MathClass;
import de.dante.extex.typesetter.type.MathGlyph;
import de.dante.util.GeneralException;
import de.dante.util.UnicodeChar;

/**
 * This class provides an implementation for the primitive
 * <code>\mathchar</code>.
 *
 * <doc name="mathchar">
 * <h3>The Primitive <tt>\mathchar</tt></h3>
 * <p>
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
 *    \mathchar ...  </pre>
 * </p>
 * </doc>
 *
 * @author <a href="mailto:gene@gerd-neugebauer.de">Gerd Neugebauer</a>
 * @version $Revision: 1.9 $
 */
public class Mathchar extends AbstractMathCode {

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
    public boolean execute(final Flags prefix, final Context context,
            final TokenSource source, final Typesetter typesetter)
            throws GeneralException {

        NoadConsumer nc = getListMaker(typesetter);

        Token t = source.getToken();
        if (t == null) {
            throw new EofHelpingException(printableControlSequence(context));
        } else if (t.isa(Catcode.LEFTBRACE)) {
            //TODO extension unimplemented
            throw new RuntimeException("unimplemented");
        } else {
            source.push(t);
            insert(nc, new Count(context, source));
        }
        return true;
    }

    /**
     * ...
     *
     * @param nc ...
     * @param mathchar ...
     *
     * @throws GeneralException in case of an error
     */
    protected void insert(final NoadConsumer nc, final Count mathchar)
            throws GeneralException {

        long mc = mathchar.getValue();
        nc.add(MathClass.getMathClass((int) ((mc >> 12) & 0xf)), //
                new MathGlyph((int) ((mc >> 8) & 0xf), //
                        new UnicodeChar((int) (mc & 0xff))));
    }

}